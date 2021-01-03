package comm;

import msg.Message;
import java.io.IOException;
import java.net.Socket;

public interface MsgTransmit {
    void StartTransmit(Message message, Socket socket) throws IOException;
}
