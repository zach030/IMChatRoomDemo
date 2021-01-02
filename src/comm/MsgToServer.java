package comm;

import msg.DataPack;
import msg.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MsgToServer implements MsgTransmit{
    public MsgToServer(){

    }
    public void StartTransmit(Message message, Socket socket) throws IOException {
        // server 消息队列
        System.out.println("----->[Server] Recv Message from Client: " + message.getFromId() + " , Content is :"
                + new String(message.getData(), StandardCharsets.UTF_8));
        OutputStream out = socket.getOutputStream();
        Message message1 = new Message(-1,0,"Welcome to chat-room".getBytes(), Message.MsgType.SERVER_CLIENT_NOTICE);
        out.write(DataPack.dp.Pack(message1).array());
        out.flush();
    }
}
