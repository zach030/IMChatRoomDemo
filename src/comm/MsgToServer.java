package comm;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MsgToServer implements MsgTransmit{
    public void StartTransmit(Message message, Socket socket) {
        System.out.println("----->[Server] Recv Message from Client: " + message.getFromId() + " , Content is :"
                + new String(message.getData(), StandardCharsets.UTF_8));
    }
}
