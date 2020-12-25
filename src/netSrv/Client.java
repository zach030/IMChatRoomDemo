package netSrv;

import comm.DataPack;
import comm.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Client {
    private int ID;
    private String NickName;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    public Client(int id, String nickName, String host, int port) throws IOException {
        this.ID = id;
        this.NickName = nickName;
        this.socket = new Socket(host, port);
        this.outputStream = this.socket.getOutputStream();
        this.inputStream = this.socket.getInputStream();
        this.ClientOnline();
    }

    public void ClientOnline() throws IOException {
        //上线
        Message message = this.PrepareMsg(-1, "Client ID:" + this.getID()
                + ",NickName:" + this.NickName + ", is online");
        this.SendMsg(message);
    }

    public void ClientOffLine() {
        //下线
    }

    public void DoSendMsg(int targetID, String content) throws IOException {
        this.SendMsg(this.PrepareMsg(targetID,content));
    }

    private Message PrepareMsg(int targetID, String content) {
        Message message = new Message(this.ID, targetID, content.getBytes());
        System.out.println("<------Send Message to server: " + new String(message.getData(), StandardCharsets.UTF_8));
        return message;
    }

    private void SendMsg(Message message) throws IOException {
        ByteBuffer outBuffer = DataPack.dp.Pack(message);
        outputStream.write(outBuffer.array());
        outputStream.flush();
        System.out.println("<---------- 用户:" + this.ID + ",昵称:" + this.NickName + ",成功发送消息");
    }

    public void ReceiveMsg() {
        Ready2Receive ready2Receive = new Ready2Receive(this.socket);
        while (true) {
            new Thread(ready2Receive).start();
        }
    }

    static class Ready2Receive implements Runnable {
        private Socket socket;
        public Ready2Receive(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                HandleReceivingSocket();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        public void HandleReceivingSocket() throws IOException {
            while (true) {
                InputStream inputStream = this.socket.getInputStream();
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
                System.out.println("----->Recv Message By Transmit: " + message.getFromId() + " , content is :"
                        + new String(message.getData(), StandardCharsets.UTF_8));
            }
            socket.close();
        }
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
