package netSrv;

import comm.*;
import org.omg.CORBA.Request;

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

    public void StartMsgQueue(Message message) {
        MsgProducer msgProducer = new MsgProducer(message, Integer.toString(message.getFromId()));
        msgProducer.start();
    }

    public void DoTransmit(Message message) throws IOException {
        switch (message.getToId()) {
            case -1:
                msgTransmit = new MsgToServer();
                break;
            case 0:
                msgTransmit = new MsgToBroadcast();
                break;
            default:
                msgTransmit = new MsgToClient();
        }
        Socket targetSocket = this.GetTargetSocket(message);
        // target socket==self : send to server； !=null : send to client; broadcast
        msgTransmit.StartTransmit(message, targetSocket);
    }

    public void RemoveDisableSocket(int id){
        this.allClientSocketMap.remove(id);
    }

    public void Add2SocketManager(Message message, Socket socket) {
        System.out.println("[SocketManager]  Successfully add client " + message.getFromId() + " to socket manager!");
        this.allClientSocketMap.put(message.getFromId(), socket);
        System.out.println(allClientSocketMap);
    }

    public Socket GetTargetSocket(Message message) {
        if (this.allClientSocketMap.get(message.getToId()) == null) {
            // 如果没有目标socket，则说明服务器回写消息，target是本身
            return this.allClientSocketMap.get(message.getFromId());
        }
        return this.allClientSocketMap.get(message.getToId());
    }

    public ArrayList<Socket> GetAllAvailableSocketList() {
        ArrayList<Socket> allSockets = new ArrayList<>();
        Iterator<Map.Entry<Integer, Socket>> map1it = allClientSocketMap.entrySet().iterator();
        while (map1it.hasNext()) {
            Map.Entry<Integer, Socket> entry = map1it.next();
            allSockets.add(entry.getValue());
        }
        return allSockets;
    }

    public ArrayList<Integer> GetAllAvailableClientList(){
        ArrayList<Integer> allClients = new ArrayList<>();
        System.out.println(allClientSocketMap);
        Iterator<Map.Entry<Integer, Socket>> map1it = allClientSocketMap.entrySet().iterator();
        while (map1it.hasNext()) {
            Map.Entry<Integer, Socket> entry = map1it.next();
            allClients.add(entry.getKey());
        }
        return allClients;
    }
}
