package netSrv;

import comm.DataPack;
import comm.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private String Name;
    private String Host;
    private int Port;
    private int MaxConnNum;
    private ServerSocket serverSocket;

    public Server(String name, String host, int port, int maxConnNum) throws IOException {
        this.Name = name;
        this.Host = host;
        this.Port = port;
        this.MaxConnNum = maxConnNum;
        this.serverSocket = new ServerSocket(this.Port);
    }

    public void SocketWelcome() {
        System.out.println("Server Name:" + this.Name + ", listen at IP:" + this.Host + ",Port:" + this.Port + ", is Starting");
    }

    public void Start() throws IOException {
        this.SocketWelcome();
        while (true) {
            Socket socket = serverSocket.accept();
            ProcessSocket processSocket = new ProcessSocket(socket);
            new Thread(processSocket).start();
        }
    }

    static class ProcessSocket implements Runnable {
        private Socket socket;
        public ProcessSocket(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                HandleSocket();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void HandleSocket() throws IOException {
            while (true) {
                InputStream inputStream = socket.getInputStream();
                int eof = inputStream.read();
                if (eof == -1) {
                    System.out.println("input stream is null now");
                    break;
                }
                byte[] msg = new byte[eof];
                int len = inputStream.read(msg);
                ByteBuffer buffer = ByteBuffer.wrap(msg);
                Message message = DataPack.dp.Unpack(buffer);
                if (message.IsEndOfMessage()) {
                    break;
                }
                message.SetMessageLen(len);
                SocketManager.socketManager.DoTransmit(message, socket);
            }
            socket.close();
        }
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
