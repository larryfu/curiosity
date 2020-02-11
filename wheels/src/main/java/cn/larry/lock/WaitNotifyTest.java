package cn.larry.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WaitNotifyTest {

    static Object o = new Object();
    static final ExecutorService service = new ThreadPoolExecutor(10,100,100, TimeUnit.SECONDS,new LinkedBlockingQueue<>());
    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<5;i++)
            service.submit(()->{
                //  synchronized (o){
                try{
                    System.out.println("wait");
                    o.wait();
                    System.out.println(Thread.currentThread()+" notifyed ");
                    Thread.sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                //  }
            });
        //    Thread.sleep(100);
        service.submit(()->{
            System.out.println("notify");
            synchronized (o){
                o.notifyAll();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
