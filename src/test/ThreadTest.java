package test;

public class ThreadTest {
    static boolean running = true;
    static class Test extends Thread{

        public void run(){
            while (running){
                run1();
            }
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        public void run1(){
                System.out.println("I am 1");
        }
        public void run2(){
                System.out.println("I am 2");
        }
    }

    public static void main(String []args){
        Test test = new Test();
        test.start();
    }
}
