package netSrv;

import comm.Message;

import java.io.IOException;

public interface MsgHandler {
    void SendMsg(int toID, Message.MsgType msgType, String content) throws IOException;
    //void ReceiveMsg();
}
