package netSrv;

import msg.Message;
import utils.RecvMsgHandler;
import utils.SendMsgHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Server {
    private String Name;
    private String Host;
    private int Port;
    private int MaxConnNum;
    private ServerSocket serverSocket;
    private boolean running;

    public Server(String name, String host, int port, int maxConnNum) throws IOException {
        this.Name = name;
        this.Host = host;
        this.Port = port;
        this.MaxConnNum = maxConnNum;
        this.serverSocket = new ServerSocket(this.Port);
        running = true;
    }

    public String ServerWelcome() {
        return "[Server Start] Server  Name:" + this.Name + ", listen at IP:" + this.Host + ",Port:" + this.Port + ", is Starting......";
    }

    public String ServerByeBye() {
        return "[Server Stop] Server Name : " + this.Name + ", is stop......";
    }

    public void Start() throws IOException {
        //0 开启日志
        System.out.println(ServerWelcome());
        //1 处理socket的主线程
        int currentClient = 0;
        while (running && currentClient <= MaxConnNum) {
            Socket socket = this.serverSocket.accept();
            currentClient++;
            // 接收消息线程
            new Thread(new HandleClientMsg(socket)).start();
            // 更新在线客户端线程
            new Thread(new HandleClientSocket(socket)).start();
        }
    }

    private static class HandleClientMsg extends Thread {
        int currentClientID;
        RecvMsgHandler recvMsgHandler;
        Socket clientSocket;

        public HandleClientMsg(Socket socket) {
            this.clientSocket = socket;
            recvMsgHandler = new RecvMsgHandler(clientSocket);
        }

        private void ProcessClientMsg() throws IOException {
            Message message = recvMsgHandler.DoReceiveMsg();
            if (message == null) {
                clientSocket.close();
                return;
            }
            currentClientID = message.getFromId();
            System.out.println("[Server] Recv Msg from:" + message.getFromId() + ", content:" + new String(message.getData(), StandardCharsets.UTF_8));
            SocketManager.socketManager.Add2SocketManager(message, clientSocket);
            SocketManager.socketManager.DoTransmit(message);
        }

        public void run() {
            while (!clientSocket.isClosed()) {
                try {
                    ProcessClientMsg();
                    sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            SocketManager.socketManager.RemoveDisableSocket(currentClientID);
            System.out.println("[Server] Socket :" + clientSocket + ", is Closed...");
        }
    }

    private class HandleClientSocket extends Thread {
        private Socket socket;

        public HandleClientSocket(Socket socket) {
            this.socket = socket;
        }

        private void SendAliveSocketList() throws IOException {
            ArrayList<Integer> clientsID = SocketManager.socketManager.GetAllAvailableClientList();
            SendMsg(socket, Message.MsgType.SERVER_CLIENT_FRIENDS, clientsID.toString());
        }

        public void run() {
            while (!socket.isClosed()) {
                try {
                    SendAliveSocketList();
                    sleep(2000);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void Stop() throws IOException {
        System.out.println(ServerByeBye());
        running = false;
        this.serverSocket.close();
    }

    // 封装后的服务器发送消息模块
    public void SendMsg(Socket socket, Message.MsgType msgType, String content) throws IOException {
        SendMsgHandler sendMsgHandler = new SendMsgHandler(-1, socket, 0, msgType, content);
        sendMsgHandler.DoSendMsg();
    }
}
