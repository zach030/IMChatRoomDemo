package msg;

import java.nio.ByteBuffer;

public class DataPack {
    public static DataPack dp = new DataPack();
    public DataPack() { }

    // msg-len  end-flag    data-len   from-client-id  to-client-id  msg-type data
    public ByteBuffer Pack(Message message) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(message.GetMessageLen()+1);
        byteBuffer.put((byte) message.GetMessageLen());
        byteBuffer.put((byte) (message.getDataLen()>>8));
        byteBuffer.put((byte) message.getDataLen());
        byteBuffer.put((byte) message.getFromId());
        byteBuffer.put((byte) message.getToId());
        byteBuffer.put((byte) message.getMsgType().ordinal());
        byteBuffer.put(message.getData());
        return byteBuffer;
    }

    public Message Unpack(ByteBuffer byteBuffer) {
        Message sendMsg = new Message();
        // msgLen----isEnd----dataLen----data
        byte head = byteBuffer.get();
        sendMsg.setEndFlag(head);
        sendMsg.setDataLen(head+byteBuffer.get());
        int dataLen = sendMsg.getDataLen();
        sendMsg.setFromId(byteBuffer.get());
        sendMsg.setToId(byteBuffer.get());
        sendMsg.setMsgType(Message.MsgType.valueOf(byteBuffer.get()));
        byte[] data = new byte[dataLen];
        byteBuffer.get(data);
        sendMsg.setData(data);
        return sendMsg;
    }
}
