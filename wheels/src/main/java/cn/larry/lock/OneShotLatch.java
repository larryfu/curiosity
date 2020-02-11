package cn.larry.lock;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 基于AbstractQueuedSynchronizer的二元闭锁
 */

public class OneShotLatch {

    private final Sync sync = new Sync();

    public void signal(){
        sync.releaseShared(0);
    }

    public void await() throws InterruptedException{
        sync.acquireInterruptibly(0);
    }


    private class Sync extends AbstractQueuedSynchronizer{
        protected int tryAcquireShared(int ignored){
            return getState() == 1?1:-1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            setState(1);
            return true;
        }
    }
}
