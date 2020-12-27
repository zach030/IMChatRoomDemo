package netSrv;

import comm.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// target: -1 --- to server;
// 0 ---- broadcast ;
// 1..... --- to client

//TODO socket 与 msg queue 组成 hashmap

public class SocketManager {
    public static SocketManager socketManager = new SocketManager();

    private HashMap<Integer, Socket> allClientSocketMap = new HashMap<>();
    private MsgTransmit msgTransmit;

    public SocketManager() {

    }

    public void StartMsgQueue(Message message){
        MsgProducer msgProducer = new MsgProducer(message,Integer.toString(message.getFromId()));
        msgProducer.run();
    }

    public void DoTransmit(Message message) throws IOException {
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
        // target socket==null : send to server； !=null : send to client; broadcast
        msgTransmit.StartTransmit(message, targetSocket);
    }

    public void Add2SocketManager(int ID, Socket socket) {
        System.out.println("[SocketManager]  Successfully add client " + ID + " to socket manager!");
        this.allClientSocketMap.put(ID, socket);
    }

    public Socket GetTargetSocket(int id) {
        if (this.allClientSocketMap.get(id) == null) {
            return null;
        }
        return this.allClientSocketMap.get(id);
    }

    public ArrayList<Socket> GetAllAvailableSocketList(){
        ArrayList<Socket> allSockets = new ArrayList<>();
        Iterator<Map.Entry<Integer, Socket>> map1it=allClientSocketMap.entrySet().iterator();
        while(map1it.hasNext())
        {
            Map.Entry<Integer, Socket> entry= map1it.next();
            allSockets.add(entry.getValue());
        }
        return allSockets;
    }
}
