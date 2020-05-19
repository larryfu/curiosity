package cn.larry;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class Concurrenttest {

   static ExecutorService service = new ThreadPoolExecutor(20,20,10, TimeUnit.SECONDS,new LinkedTransferQueue<>());
   static ExecutorService service2 = new ThreadPoolExecutor(20,20,10, TimeUnit.SECONDS,new LinkedTransferQueue<>());


    public static void main(String[] args) throws InterruptedException {
        int num = 10000000;
        long statr = System.currentTimeMillis();
        for(int i = 0;i<num;i++){
            incr();
        }
        System.out.println("1 thread simple incr cost "+(System.currentTimeMillis()-statr));
        long stat1 = System.currentTimeMillis();
        for(int i = 0;i<num;i++){
            incrLock();
        }
        System.out.println("1 thread lock incr cost "+(System.currentTimeMillis()-stat1));

        long stat2 = System.currentTimeMillis();
        for(int i = 0;i<num;i++){
            incrAtomic();
        }
        System.out.println("1 thread atomic incr cost "+(System.currentTimeMillis()-stat2));
        incr(10,num);
        incrLock(10,num);
        incrSimple(10,num);
    }

    public static void incr(int thread,int num) throws InterruptedException {
        long start = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(thread);
        int numPer = num/thread;
        for(int i = 0;i<thread;i++){
            service.submit(()->{
                for(int j=0;j<numPer;j++)
               incrAtomic();
                latch.countDown();
            });
        }
        latch.await();
        System.out.println(thread+" thread atomic incr cost "+(System.currentTimeMillis() - start));
    }

    public static void incrSimple(int thread,int num) throws InterruptedException {
        long start = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(thread);
        int numPer = num/thread;
        for(int i = 0;i<thread;i++){
            service.submit(()->{
                for(int j=0;j<numPer;j++)
                   num2++;
                latch.countDown();
            });
        }
        latch.await();
        System.out.println(num2);
        System.out.println(thread+" thread atomic incr cost "+(System.currentTimeMillis() - start));
    }

    public static void incrLock(int thread,int num) throws InterruptedException {
        long start = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(thread);
        int numPer = num/thread;
        for(int i = 0;i<thread;i++){
            service2.submit(()->{
                for(int j=0;j<numPer;j++)
                    incrLock();
                latch.countDown();
            });
        }
        latch.await();
        System.out.println(thread+" thread lock incr cost "+(System.currentTimeMillis() - start));
    }



    private static long num  = 0;
    private static long num2  = 0;

    private static AtomicLong anum = new AtomicLong(0);

    public static void incr(){
        num++;
    }

    public static synchronized void incrLock(){
        num++;
    }

    public static void incrAtomic(){
        anum.incrementAndGet();
    }
}
