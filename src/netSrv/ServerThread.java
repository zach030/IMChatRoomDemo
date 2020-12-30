package netSrv;

import comm.DataPack;
import comm.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ServerThread extends Thread {
    Socket clientSocket;

    public ServerThread(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            HandleSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void HandleSocket() throws IOException {
        int clientId = 0;
        while (true) {
            InputStream inputStream = clientSocket.getInputStream();
            if (inputStream==null){
                break;
            }
            int eof = inputStream.read();
            if (eof == -1) {
                Server.serverLogList.add("[Server]  input stream is null now");
                System.out.println("[Server]  input stream is null now");
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
            clientId = message.getFromId();
            SocketManager.socketManager.Add2SocketManager(message, clientSocket);
            SocketManager.socketManager.DoTransmit(message);
        }
        SocketManager.socketManager.RemoveDisableSocket(clientId);
        Server.serverLogList.add("[Server] Socket :" + clientSocket + ", is Closed...");
        System.out.println("[Server] Socket :" + clientSocket + ", is Closed...");
        clientSocket.close();
        this.interrupt();
    }
}
