package comm;

import msg.DataPack;
import msg.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class MsgToClient implements MsgTransmit {
    public void StartTransmit(Message message, Socket targetSocket) throws IOException {
        if (targetSocket == null) {
            System.err.println("您的好友不存在");
            return;
        }
        System.out.println("[SocketManager] Transmit Msg To Socket: "+targetSocket);
        OutputStream out = targetSocket.getOutputStream();
        out.write(DataPack.dp.Pack(message).array());
        out.flush();
    }
}
