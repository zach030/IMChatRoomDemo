package netSrv;

import comm.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

// target: -1 --- to server;
// 0 ---- broadcast ;
// 1..... --- to client
public class SocketManager {
    public static SocketManager socketManager = new SocketManager();

    private HashMap<Integer, Socket> allClientSocketMap = new HashMap<>();
    private MsgTransmit msgTransmit;

    public SocketManager() {

    }

    public void DealSocket(Socket socket) {

    }

    public void DoTransmit(Message message, Socket socket) throws IOException {
        this.Add2SocketManager(message.getFromId(), socket);
        switch (message.getToId()){
            case -1:
                msgTransmit = new MsgToServer();
                break;
            case 0:
                msgTransmit = new MsgToBroadcast();
                break;
            default:
                msgTransmit = new MsgToClient();
        }
        Socket targetSocket = this.GetTargetSocket(message.getToId());
        // target socket==null : send to server； !=null : send to client;  unsupported broadcast
        msgTransmit.StartTransmit(message, targetSocket);
    }

    public void Add2SocketManager(int ID, Socket socket) {
        System.out.println("Successfully add client " + ID + " to socket manager!");
        this.allClientSocketMap.put(ID, socket);
    }

    public Socket GetTargetSocket(int id) {
        if (this.allClientSocketMap.get(id) == null) {
            return null;
        }
        return this.allClientSocketMap.get(id);
    }
}
