package utils;

import comm.DataPack;
import comm.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

// 发送消息方法封装
public class SendMsgHandler {
    Socket fromSocket;
    int fromID;
    int sendToID;
    String content;
    Message.MsgType msgType;

    public SendMsgHandler(int fromID, Socket socket, int sendToID, Message.MsgType msgType,String content) {
        this.fromID = fromID;
        this.fromSocket = socket;
        this.sendToID = sendToID;
        this.msgType = msgType;
        this.content = content;
    }

    public void DoSendMsg() throws IOException {
        this.sendMsg(this.prepareMsg());
    }

    private Message prepareMsg() {
        Message message = new Message(fromID, sendToID, content.getBytes(), msgType);
        System.out.println("<------[Send Message] To Server: " + new String(message.getData(), StandardCharsets.UTF_8));
        return message;
    }

    private void sendMsg(Message message) throws IOException {
        ByteBuffer outBuffer = DataPack.dp.Pack(message);
        OutputStream outputStream = fromSocket.getOutputStream();
        outputStream.write(outBuffer.array());
        outputStream.flush();
    }
}
