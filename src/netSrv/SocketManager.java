package netSrv;

import comm.DataPack;
import comm.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

public class SocketManager {
    public static SocketManager socketManager = new SocketManager();
    private HashMap<Integer, Socket> allClientSocketMap = new HashMap<>();

    public SocketManager() {

    }

    public void DealSocket(Socket socket) {

    }

    public void DoTransmit(Message message, Socket socket) throws IOException {
        this.Add2SocketManager(message.getFromId(), socket);
        Socket targetSocket = this.GetTargetSocket(message.getToId());
        if (targetSocket==null){
            return;
        }
        OutputStream out = targetSocket.getOutputStream();
        out.write(DataPack.dp.Pack(message).array());
        out.flush();
    }

    public void Add2SocketManager(int ID, Socket socket) {
        System.out.println("Successfully add client " + ID + " to socket manager!");
        this.allClientSocketMap.put(ID, socket);
    }

    public Socket GetTargetSocket(int id) {
        if (this.allClientSocketMap.get(id) == null) {
            System.err.println("您的好友未上线!");
            return null;
        }
        return this.allClientSocketMap.get(id);
    }
}
