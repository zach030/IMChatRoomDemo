package comm;

import netSrv.Client;
import netSrv.SocketManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MsgToServer implements MsgTransmit{
    private MsgQueue msgQueue = new MsgQueue();
    public MsgToServer(){

    }

    public void StartTransmit(Message message, Socket socket) throws IOException {
        // server 消息队列
        System.out.println("----->[Server] Recv Message from Client: " + message.getFromId() + " , Content is :"
                + new String(message.getData(), StandardCharsets.UTF_8));
        OutputStream outputStream = socket.getOutputStream();
        ArrayList<Integer> clientsID = SocketManager.socketManager.GetAllAvailableClientList();
        Message msgToClient = new Message(-1,message.getFromId(),clientsID.toString().getBytes());
        ByteBuffer outBuffer = DataPack.dp.Pack(msgToClient);
        outputStream.write(outBuffer.array());
        outputStream.flush();
    }
}
