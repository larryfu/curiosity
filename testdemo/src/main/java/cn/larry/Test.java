package cn.larry;

public class Test {




    static class Node<T>{
        T value;
        Node<T> next;
        public Node(T v){
            this.value = v;
        }
    }


    public static void main(String[] args) {
        byte[] arr = new byte[5000000];
        for(int i = 0;i<arr.length;i++){
            arr[i]=  (byte) i;
        }
        Node<Byte> node = new Node<>((byte)1);
        Node head = node;

        for(int i = 0;i<5000000;i++){
            node.next = new Node<>((byte)i);
            node = node.next;
        }
        long start = System.currentTimeMillis();
        for(int i = 0;i<arr.length;i++){
            byte j = arr[i];
            j--;
        }
        System.out.println("cost "+(System.currentTimeMillis() - start));
        Node<Byte> n = head;
        long start1 = System.currentTimeMillis();
        while (n!=null){
            byte j = n.value ;
            j++;
            n = n.next;
        }
        System.out.println("linked list cost "+(System.currentTimeMillis()-start1));
    }
}
