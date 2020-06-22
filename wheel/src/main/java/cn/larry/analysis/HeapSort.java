package cn.larry.analysis;

public class HeapSort {

    public static void main(String[] args) {
        heapSort(new int[]{5,9,6,3,2});

    }

    public static void heapSort(int[] nums){
        Heap heap = new Heap(nums.length);
        for (int n:nums){
            heap.add(n);
        }
        while (!heap.isEmpty()){
            System.out.println(heap.takeHead());
        }
    }



    static class Heap{

        public boolean isEmpty(){
            return size <= 0;
        }
        int[] table;
        private int size;
        private int cap;
        public Heap(int cap){
            this.cap = cap;
            table = new int[cap];
            size = 0;
        }

        public void add(int n){
            if(size >= cap){
                throw new RuntimeException();
            }
            table[size++] = n;
            swim(size-1);
        }

        public int takeHead(){
            int head = table[0];
            table[0] = table[--size];
            sink(0);
            return head;
        }


        private  void sink(int pos){
            int leftChild = getLeftChild(pos);
            int rightChild = getRightChild(pos);
            if(rightChild<size){
                int minChild = table[leftChild]< table[rightChild]?leftChild:rightChild;
                if(table[minChild] < table[pos]){
                    swap(table,minChild,pos);
                    sink(minChild);
                }
            }else if(leftChild < size){
                if(table[leftChild] < table[pos]){
                    swap(table,leftChild,pos);
                }
            }
        }

        private void swim(int pos){
            int p = pos;
            while(p >0){
                int parent = getParent(p);
                if(table[p] < table[parent]){
                    swap(table,p,parent);
                }else break;
                p = parent;
            }
        }

        private void swap(int[] nums,int p1,int p2){
            int tmp = nums[p1];
            nums[p1] = nums[p2];
            nums[p2] = tmp;
        }

        private int getParent(int pos){
            return (pos-1)/2;
        }

        private int getLeftChild(int pos){
            return pos*2+1;
        }
        private int getRightChild(int pos){
            return pos*2+2;
        }
    }


}
