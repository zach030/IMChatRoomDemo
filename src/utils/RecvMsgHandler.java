package utils;

import comm.DataPack;
import comm.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

// 接收消息封装
public class RecvMsgHandler {
    Socket socket;

    public RecvMsgHandler(Socket socket){
        this.socket =socket;
    }

    public Message DoReceiveMsg() throws IOException {
        InputStream inputStream = this.socket.getInputStream();
        int eof = inputStream.read();
        if (eof == -1) {
            System.out.println("input stream is null now");
            return null;
        }
        //System.out.println("eof is:" + eof);
        byte[] msg = new byte[eof];
        int len = inputStream.read(msg);
        ByteBuffer buffer = ByteBuffer.wrap(msg);
        Message message = DataPack.dp.Unpack(buffer);
        if (message.IsEndOfMessage()) {
            return null;
        }
        message.SetMessageLen(len);
        return message;
    }
}
