package cn.larry;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 写时加锁的List
 */
public class WriteLockList<T> {

    private ReentrantLock lock = new ReentrantLock();

    private List<T> origin;

    public WriteLockList(List<T> list) {
        this.origin = list;
    }

    public boolean add(T t) {
        lock.lock();
        try {
          return   origin.add(t);
        } finally {
            lock.unlock();
        }
    }

    public T set(int index,T t){
        lock.lock();
        try{
            return origin.set(index, t);
        }finally {
            lock.unlock();
        }
    }


    public T remove(int index) {
        lock.lock();
        try {
            return origin.remove(index);
        } finally {
            lock.unlock();
        }
    }

}
