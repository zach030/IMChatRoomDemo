package comm;

import msg.Message;

import java.io.IOException;
import java.net.Socket;

// 将 msg 推送到 queue
public interface MsgTransmit {
    void StartTransmit(Message message, Socket socket) throws IOException;
}
