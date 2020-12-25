package comm;

import java.io.IOException;
import java.net.Socket;

public interface MsgTransmit {
    public void StartTransmit(Message message, Socket socket) throws IOException;
}
