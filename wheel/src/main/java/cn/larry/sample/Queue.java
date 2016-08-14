package cn.larry.sample;

/**
 * Created by fugz on 2016/8/2.
 */
public class Queue {
    private int[] elements;
    private final int capacity;
    private int used;
    private int inIndex;


    public Queue(int capacity) {
        this.elements = new int[capacity];
        this.capacity = capacity;
        this.used = 0;
        this.inIndex = 0;

    }

    public boolean offer(int a) {
        if (used == capacity)
            throw new IllegalStateException("队列已满");
        elements[inIndex] = a;
        inIndex = (inIndex + 1) % capacity;
        used++;
        return true;
    }


    public int pop() {
        if (used == 0)
            throw new IllegalStateException("队列为空");
        int index = inIndex - used;
        if (index < 0)
            index = index + capacity;
        int i = elements[index];
        used--;
        return i;
    }

    public static void main(String[] args) {
        Queue queue = new Queue(8);
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        queue.offer(4);
        for (int i = 5; i < 100; i++) {
            System.out.println(queue.pop());
           // queue.offer(i);
        }
    }
}
