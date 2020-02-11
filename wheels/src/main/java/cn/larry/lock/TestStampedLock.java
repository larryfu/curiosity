package cn.larry.lock;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.StampedLock;

public class TestStampedLock {

    public static void main(String[] args) throws InterruptedException{

        final StampedLock lock = new StampedLock();

        new Thread(){

            public void run(){

                long readLong = lock.writeLock();

                LockSupport.parkNanos(6100000000L);

                lock.unlockWrite(readLong);

            }

        }.start();

        Thread.sleep(100);

        for( int i = 0; i < 3; ++i)

        new Thread(new OccupiedCPUReadThread(lock)).start();

    }

    private static class OccupiedCPUReadThread implements Runnable{

        private StampedLock lock;

        public OccupiedCPUReadThread(StampedLock lock){

            this.lock = lock;

        }

        public void run(){

            Thread.currentThread().interrupt();

            long lockr = lock.readLock();

            System.out.println(Thread.currentThread().getName() + " get read lock");

            lock.unlockRead(lockr);
        }

    }


//    先开启一个线程获取写锁并保持6秒，再开启三个带着中断状态的线程去获取读锁（readLock方法），结果是3个核心被占据了近6秒。
//    原因在于没有使用保存/复原中断状态的机制，通过hack源码，插入保存中断和返回前恢复中断的相关代码即可修复：
//
//    boolean interrupted = false;
//
//        if(interrupted)
//
//            Thread.currentThread().interrupt();
//
//        return ns;
//    view sourceprint?
//
//            if(Thread.interrupted()){
//
//        if(interruptible)
//
//        return cancelWaiter(node, p, true);
//
//    else
//
//        interrupted = true;
//
//    }


}
