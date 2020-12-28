package netSrv;

import java.io.Serializable;

public class Request implements Serializable {
    private int clientID;
    private int messageTo;
    private String fromName;
    private String content;

    public Request(int clientID, int messageTo, String fromName,String content) {
        this.clientID = clientID;
        this.messageTo = messageTo;
        this.fromName = fromName;
        this.content = content;
    }

    public String toString(){
        return "hi";
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public int getMessageTo() {
        return messageTo;
    }

    public void setMessageTo(int messageTo) {
        this.messageTo = messageTo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
