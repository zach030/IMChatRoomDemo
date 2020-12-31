package netSrv;

import comm.DataPack;
import comm.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ServerThread extends Thread {
    int currentClientID;
    boolean clientStatus;
    Socket clientSocket;

    public ServerThread(Socket socket) {
        this.clientSocket = socket;
        clientStatus = true;
    }

    public void run() {
        try {
            //处理socket发送的消息
            HandleSocket();
            //给客户端socket发消息，更新好友列表
            SendAliveSocketList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SendAliveSocketList() throws IOException {
        while (clientStatus) {
            OutputStream outputStream = clientSocket.getOutputStream();
            ArrayList<Integer> clientsID = SocketManager.socketManager.GetAllAvailableClientList();
            Message msgToClient = new Message(-1, currentClientID, clientsID.toString().getBytes(),Message.MsgType.SERVER_CLIENT_FRIENDS);
            ByteBuffer outBuffer = DataPack.dp.Pack(msgToClient);
            outputStream.write(outBuffer.array());
            outputStream.flush();
        }
    }

    private void makeClientDie(){
        clientStatus =false;
    }

    private void HandleSocket() throws IOException {
        while (clientStatus) {
            InputStream inputStream = clientSocket.getInputStream();
            if (inputStream == null) {
                makeClientDie();
                continue;
            }
            int eof = inputStream.read();
            if (eof == -1) {
                Server.serverLogList.add("[Server]  input stream is null now");
                System.out.println("[Server]  input stream is null now");
                makeClientDie();
                continue;
            }
            byte[] msg = new byte[eof];
            int len = inputStream.read(msg);
            ByteBuffer buffer = ByteBuffer.wrap(msg);
            Message message = DataPack.dp.Unpack(buffer);
            if (message.IsEndOfMessage()) {
                makeClientDie();
                break;
            }
            message.SetMessageLen(len);
            currentClientID = message.getFromId();
            SocketManager.socketManager.Add2SocketManager(message, clientSocket);
            SocketManager.socketManager.DoTransmit(message);
        }
        SocketManager.socketManager.RemoveDisableSocket(currentClientID);
        Server.serverLogList.add("[Server] Socket :" + clientSocket + ", is Closed...");
        System.out.println("[Server] Socket :" + clientSocket + ", is Closed...");
        clientSocket.close();
        this.interrupt();
    }
}
