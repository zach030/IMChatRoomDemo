package comm;

import netSrv.SocketManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MsgToBroadcast implements MsgTransmit {
    public void StartTransmit(Message message, Socket socket) throws IOException {
        ArrayList<Socket> availableSocket = SocketManager.socketManager.GetAllAvailableSocketList();
        for (Socket socket1 : availableSocket){
            System.out.println("[SocketManager] BroadCast Msg To Socket: "+socket1);
            OutputStream out = socket1.getOutputStream();
            out.write(DataPack.dp.Pack(message).array());
            out.flush();
        }
    }
}
