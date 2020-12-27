package comm;

public class MsgConsumer implements Runnable{
    private MsgQueue msgQueue;
    private String clientID;
    private Message message;

    public MsgConsumer(String clientID){
        this.clientID = clientID;
        this.msgQueue = new MsgQueue();
    }

    @Override
    public void run() {
        System.out.println("[Msg---Consumer] Client: "+this.clientID+", is consuming msg......");
        while (true){
            this.message = this.msgQueue.get(clientID);
        }
    }
}
