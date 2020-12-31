package netSrv;

import ui.ServerMonitor;
import utils.SendMsgHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server{
    public static ArrayList<String> serverLogList = new ArrayList<>();
    private String Name;
    private String Host;
    private int Port;
    private int MaxConnNum;
    private ServerSocket serverSocket;
    private SendMsgHandler sendMsgHandler;
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
        serverLogList.add(this.ServerWelcome());
        System.out.println(this.ServerWelcome());
        while (running){
            Socket socket = this.serverSocket.accept();
            // 接收消息线程
            ServerThread serverThread = new ServerThread(socket);
            serverThread.start();
        }
    }

    public void Stop() throws IOException {
        serverLogList.add(ServerByeBye());
        System.out.println(ServerByeBye());
        running = false;
        this.serverSocket.close();
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
