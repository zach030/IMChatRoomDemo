package comm;

import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MsgToServer implements MsgTransmit{
    private MsgQueue msgQueue = new MsgQueue();
    public MsgToServer(){

    }

    public void StartTransmit(Message message, Socket socket) {
        // server 消息队列
        System.out.println("----->[Server] Recv Message from Client: " + message.getFromId() + " , Content is :"
                + new String(message.getData(), StandardCharsets.UTF_8));
    }
}
