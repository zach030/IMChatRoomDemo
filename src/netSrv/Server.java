package netSrv;

import comm.DataPack;
import comm.Message;
import utils.RecvMsgHandler;
import utils.SendMsgHandler;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Server {
    public static ArrayList<String> serverLogList = new ArrayList<>();
    private String Name;
    private String Host;
    private int Port;
    private int MaxConnNum;
    private ServerSocket serverSocket;
    private SendMsgHandler sendMsgHandler;
    private RecvMsgHandler recvMsgHandler;
    static boolean running;

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
        while (running) {
            Socket socket = this.serverSocket.accept();
            // 接收消息线程
            new Thread(new HandleClientMsg(socket)).start();
            // 更新在线客户端线程
            new Thread(new HandleClientSocket(socket)).start();
        }
    }

    private class HandleClientMsg extends Thread{
        int currentClientID;
        boolean clientStatus;
        Socket clientSocket;

        public HandleClientMsg(Socket socket) {
            this.clientSocket = socket;
            clientStatus = true;
        }

        public void run() {
            try {
                while (clientStatus) {
                    recvMsgHandler = new RecvMsgHandler(clientSocket);
                    Message message = recvMsgHandler.DoReceiveMsg();
                    currentClientID = message.getFromId();
                    System.out.println("[Server] Recv Msg from:"+message.getFromId()+", content:"+new String(message.getData(), StandardCharsets.UTF_8));
                    SocketManager.socketManager.Add2SocketManager(message, clientSocket);
                    SocketManager.socketManager.DoTransmit(message);
                }
                SocketManager.socketManager.RemoveDisableSocket(currentClientID);
                Server.serverLogList.add("[Server] Socket :" + clientSocket + ", is Closed...");
                System.out.println("[Server] Socket :" + clientSocket + ", is Closed...");
                clientSocket.close();
                this.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private class HandleClientSocket extends Thread {
        private Socket socket;

        public HandleClientSocket(Socket socket) {
            this.socket = socket;
        }

        private void SendAliveSocketList() throws IOException {
            ArrayList<Integer> clientsID = SocketManager.socketManager.GetAllAvailableClientList();
            //System.out.println("Clients list:" + clientsID);
            SendMsg(socket, Message.MsgType.SERVER_CLIENT_FRIENDS,clientsID.toString());
        }

        public void run() {
            while (running){
                try {
                    SendAliveSocketList();
                    sleep(1000);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void Stop() throws IOException {
        serverLogList.add(ServerByeBye());
        System.out.println(ServerByeBye());
        running = false;
        this.serverSocket.close();
    }

    // 封装后的服务器发送消息模块
    public void SendMsg(Socket socket, Message.MsgType msgType, String content) throws IOException {
        sendMsgHandler = new SendMsgHandler(-1, socket, 0, msgType, content);
        sendMsgHandler.DoSendMsg();
    }

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        Server.running = running;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int port) {
        Port = port;
    }

    public int getMaxConnNum() {
        return MaxConnNum;
    }

    public void setMaxConnNum(int maxConnNum) {
        MaxConnNum = maxConnNum;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

}
