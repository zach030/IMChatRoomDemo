package comm;

public class Message {
    private int MessageLen;
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
        return Data.length + 4;
    }

    public Message(int fromID,int toID, byte[] data) {
        this.FromId = fromID;
        this.ToId = toID;
        this.DataLen = data.length;
        this.Data = data;
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

    public boolean IsEndOfMessage() {
        return this.EndFlag == 1;
    }
}
