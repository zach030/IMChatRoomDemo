package netSrv;

import comm.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

public class SocketManager {
    private HashMap<Integer, Socket> allClientSocketMap = new HashMap<>();

    public SocketManager() {

    }

    public void DealSocket(Socket socket) {

    }

    public void DoTransmit(Message message, Socket socket) throws IOException {
        this.Add2SocketManager(message.getFromId(), socket);
        OutputStream out = this.GetTargetSocket(message.getToId()).getOutputStream();
        out.write(message.getData());
        out.flush();
    }

    public void Add2SocketManager(int ID, Socket socket) {
        System.out.println("Successfully add client "+ID+" to socket manager!");
        this.allClientSocketMap.put(ID, socket);
    }

    public Socket GetTargetSocket(int id){
        return this.allClientSocketMap.get(id);
    }
}
