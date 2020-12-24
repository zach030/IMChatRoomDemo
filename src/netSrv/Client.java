package netSrv;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private int ID;
    private String NickName;
    private Socket socket;

    public Client(int id, String nickName){
        this.ID = id;
        this.NickName =nickName;
    }

    public void InitClientSocket(String host, int port) throws IOException {
        this.socket = new Socket(host,port);
    }

    public void Start(){

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
