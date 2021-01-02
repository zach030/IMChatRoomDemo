package netSrv;

import msg.Message;
import utils.RecvMsgHandler;
import utils.SendMsgHandler;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    //TODO ID范围仅支持-128，127 即1byte大小
    private int ID;
    private String NickName;
    private Socket socket;
    boolean onlineStatus;
    // 接收消息handler
    private RecvMsgHandler recvMsgHandler;
    // 接收消息队列 及 缓冲栈
    // 客户端之间通信 队列
    private ArrayList<AcceptMsg> fromClientMsg = new ArrayList<>();
    private Stack<AcceptMsg> fromClientMsgStack = new Stack<>();
    AcceptMsg acceptClientMsg;
    // 服务端通知 队列
    private ArrayList<AcceptMsg> fromServerNotice = new ArrayList<>();
    private Stack<AcceptMsg> fromServerNoticeStack = new Stack<>();
    AcceptMsg acceptServerNotice;
    // 服务端 好友列表
    private ArrayList<AcceptMsg> fromServerFriends = new ArrayList<>();
    private Stack<AcceptMsg> fromServerFriendsStack = new Stack<>();
    AcceptMsg acceptServerFriends;

    // 发送消息handler
    private SendMsgHandler sendMsgHandler;
    // 发送消息队列
    private ArrayList<Message> sendMsgs = new ArrayList<>();

    private static ArrayList<String> receiveMsg = new ArrayList<>();

    public Client(int id, String nickName, String host, int port) throws IOException {
        this.ID = id;
        this.NickName = nickName;
        this.socket = new Socket(host, port);
        this.onlineStatus = true;
        recvMsgHandler = new RecvMsgHandler(this.socket);
        this.ClientOnline();
        this.ClientWork();
    }

    //-----------------------------------------------------------//
    // 客户端上线
    public void ClientOnline() throws IOException {
        // 0 上线日志
        PrintOnlineMsg();
        // 1 发消息给 server
        SayHelloToServer();
    }

    // 客户端工作
    public void ClientWork() {
        // 接收消息thread
        new Thread(new ClientThread()).start();
        // 读取客户端消息 thread
        new Thread(new ReadClientMsg()).start();
        // 读取服务端通知 thread
        new Thread(new ReadServerNotice()).start();
        // 读取服务端好友信息 thread
        new Thread(new ReadServerFriends()).start();
    }

    // 客户端下线
    public void ClientOffLine() {
        //下线
        this.onlineStatus = false;
    }

    //----------------------------------------------------------//
    //  接收消息模块
    private static class AcceptMsg {
        int fromId;
        String content;
        boolean readStatus;

        public AcceptMsg(int from, String content) {
            this.fromId = from;
            this.content = content;
            readStatus = false;
        }

        public String ReadMsg() {
            this.readStatus = true;
            return content;
        }
    }

    public String Transfer2ClientMsg(AcceptMsg acceptMsg) {
        return "客户端 " + acceptMsg.fromId + ": " + acceptMsg.content + "\n";
    }

    public ArrayList<String> Transfer2FriendList(AcceptMsg acceptMsg) {
        ArrayList<String> friendsList = new ArrayList<>();
        String allFriends = acceptMsg.content.replace("[", "").replace("]", "");
        String[] friends = allFriends.split(", ");
        Pattern pattern = Pattern.compile("[0-9]+");
        for (String friend : friends) {
            //System.out.println("read friend is:" + friend);
            Matcher matcher = pattern.matcher(friend);
            if (matcher.matches()) {
                System.out.println("friends : " + friend);
                friendsList.add(friend);
            }
        }
        return friendsList;
    }

    private class ClientThread extends Thread {
        @Override
        public void run() {
            while (onlineStatus) {
                try {
                    sleep(1000);
                    // 处理接收消息的func
                    Message message = recvMsgHandler.DoReceiveMsg();
                    AddMessageToQueue(message);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void AddMessageToQueue(Message message) {
            if (message == null) {
                return;
            }
            System.out.println("----->[Client Recv] Recv Message From Id =  " + message.getFromId() +
                    ", Message is : " + new String(message.getData(), StandardCharsets.UTF_8));
            switch (message.getMsgType()) {
                //TODO 根据msg-type 加入不同队列
                case SERVER_CLIENT_NOTICE:
                    AcceptMsg acceptMsg = new AcceptMsg(message.getFromId(), new String(message.getData(), StandardCharsets.UTF_8));
                    fromServerNotice.add(acceptMsg);
                    fromServerNoticeStack.push(acceptMsg);
                    break;
                case CLIENT_CLIENT_MESSAGE:
                    AcceptMsg acceptMsg1 = new AcceptMsg(message.getFromId(), new String(message.getData(), StandardCharsets.UTF_8));
                    fromClientMsg.add(acceptMsg1);
                    fromClientMsgStack.push(acceptMsg1);
                    break;
                case SERVER_CLIENT_FRIENDS:
                    AcceptMsg acceptMsg2 = new AcceptMsg(message.getFromId(), new String(message.getData(), StandardCharsets.UTF_8));
                    fromServerFriends.add(acceptMsg2);
                    fromServerFriendsStack.push(acceptMsg2);
                    break;
                case CLIENT_SERVER_NOTICE:
                    System.out.println("unsupported msg type!");
                    break;
            }
        }
    }

    //------------------------------------------------------//
    //发送消息模块
    public void SendMsg(int toID, Message.MsgType msgType, String content) throws IOException {
        sendMsgHandler = new SendMsgHandler(this.ID, this.socket, toID, msgType, content);
        sendMsgHandler.DoSendMsg();
    }

    //------------------------------------------------//
    // utils
    private void PrintOnlineMsg() {
        System.out.println("[Client Online] Client ID = " + ID + ", NickName = " + this.NickName + ", is Online......");
    }

    private void SayHelloToServer() throws IOException {
        SendMsg(-1, Message.MsgType.CLIENT_SERVER_NOTICE, "Client ID: " + this.getID()
                + ", NickName: " + this.NickName + ", is online");
    }

    //-----------------------读取消息队列------------------------//
    // 读客户端消息
    private class ReadClientMsg extends Thread {
        public void run() {
            while (onlineStatus) {
                try {
                    if (!fromClientMsgStack.empty()) {
                        acceptClientMsg = fromClientMsgStack.pop();
                        System.out.println("[Client] Read msg from client:" + acceptClientMsg.content);
                    } else {
                        acceptClientMsg = null;
                    }
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 读服务端通知
    private class ReadServerNotice extends Thread {
        public void run() {
            while (onlineStatus) {
                try {
                    if (!fromServerNoticeStack.empty()) {
                        acceptServerNotice = fromServerNoticeStack.pop();
                        System.out.println("[Client] Read Server Notice:" + acceptServerNotice.content);
                    } else {
                        acceptServerNotice = null;
                    }
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 读服务端好友列表
    private class ReadServerFriends extends Thread {
        public void run() {
            while (onlineStatus) {
                try {
                    if (!fromServerFriendsStack.empty()) {
                        acceptServerFriends = fromServerFriendsStack.pop();
                        System.out.println("[Client] Read Server friends list:" + acceptServerFriends.content);
                    } else {
                        acceptServerFriends = null;
                    }
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //------------------------------------------------------------//

    public AcceptMsg getAcceptClientMsg() {
        return acceptClientMsg;
    }

    public AcceptMsg getAcceptServerNotice() {
        return acceptServerNotice;
    }

    public AcceptMsg getAcceptServerFriends() {
        return acceptServerFriends;
    }

    public void ShutDownOutPut() throws IOException {
        this.socket.shutdownOutput();
    }

    public void CloseSocket() throws IOException {
        this.socket.close();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

}
