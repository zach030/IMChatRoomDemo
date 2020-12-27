package comm;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

// 消息队列
//TODO 消息推送到msg queue， 提供读写两个方法
public class MsgQueue {
    private Queue<Message> MsgQueue = new LinkedList<>();
    private final static int MAX_SIZE = 20;

    public MsgQueue(){
    }

    public synchronized void put(Message message, String threadName){
        if(MsgQueue.size() >= MAX_SIZE){
            try {
                super.wait();  //当生产满了后让生产线程等待
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        MsgQueue.add(message);
        //System.out.println(threadName + new String(message.getData(), StandardCharsets.UTF_8));
        super.notify();  //每次添加一个数据就唤醒一个消费等待的线程来消费
    }

    public synchronized Message get(String threadName) {
        if(MsgQueue.size() == 0){
            try {
                super.wait();  //当产品仓库为空的时候让消费线程等待
                System.out.println(threadName + ": wait");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        super.notify();  //当数据不为空的时候就唤醒一个生产线程来生产
        return MsgQueue.remove();
    }
}
