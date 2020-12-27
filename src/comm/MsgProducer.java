package comm;

public class MsgProducer implements Runnable {

    private MsgQueue msgQueue;
    private Message messageSend2Queue;
    private String ClientID;

    public MsgProducer(Message message, String clientID) {
        this.messageSend2Queue = message;
        this.msgQueue = new MsgQueue();
        this.ClientID = clientID;
    }

    @Override
    public void run() {
        System.out.println("[Msg---Producer] Client: "+this.ClientID+", is producing msg......");
        while (true){
            this.msgQueue.put(this.messageSend2Queue, this.ClientID);
        }
    }
}
