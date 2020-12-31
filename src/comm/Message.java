package comm;

public class Message {
    public enum MsgType{
        // 发送方， 接收方，消息类型
        CLIENT_CLIENT_MESSAGE,SERVER_CLIENT_NOTICE,SERVER_CLIENT_FRIENDS,CLIENT_SERVER_NOTICE;
        public static MsgType valueOf(int ordinal) {
            if (ordinal < 0 || ordinal >= values().length) {
                throw new IndexOutOfBoundsException("Invalid ordinal");
            }
            return values()[ordinal];
        }
    }
    private int MessageLen;
    private MsgType MsgType;
    private int EndFlag;
    private int FromId; // client-id
    private int ToId; // client-id
    private int DataLen;
    private byte[] Data;

    public Message() {

    }

    public void SetMessageLen(int len){
        this.MessageLen = len;
    }

    public int GetMessageLen() {
        // 1: msg-len   1: len>>8    1:len
        return Data.length + 6;
    }

    public Message(int fromID,int toID, byte[] data,MsgType msgType) {
        this.FromId = fromID;
        this.ToId = toID;
        this.DataLen = data.length;
        this.Data = data;
        this.MsgType = msgType;
    }

    public int getToId() {
        return ToId;
    }

    public void setToId(int toId) {
        ToId = toId;
    }

    public int getId() {
        return FromId;
    }

    public int getEndFlag() {
        return EndFlag;
    }

    public void setEndFlag(int endFlag) {
        EndFlag = endFlag;
    }

    public int getFromId() {
        return FromId;
    }

    public void setFromId(int fromId) {
        FromId = fromId;
    }

    public void setId(int id) {
        FromId = id;
    }

    public int getDataLen() {
        return this.DataLen;
    }

    public void setDataLen(int dataLen) {
        DataLen = dataLen;
    }

    public byte[] getData() {
        return Data;
    }

    public void setData(byte[] data) {
        Data = data;
    }

    public Message.MsgType getMsgType() {
        return MsgType;
    }

    public void setMsgType(Message.MsgType msgType) {
        MsgType = msgType;
    }

    public boolean IsEndOfMessage() {
        return this.EndFlag == 1;
    }
}
