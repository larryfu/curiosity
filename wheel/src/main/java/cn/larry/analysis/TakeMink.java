package cn.larry.analysis;

public class TakeMink {

        public static void main(String[] args) {
            int[] nums = new int[]{3,6,1,5,2};
            int[] result = takeMinK(nums,3);
            for(int n:result){
                System.out.println(n);
            }
        }

        public static int[] takeMinK(int[] nums,int k){
            Heap heap = new Heap(k+1);
            for(int i = 0 ;i<nums.length;i++){
                heap.add(nums[i]);
                if(heap.size == k+1){
                    heap.takeHead();
                }
            }
            return heap.table;
        }

        static class Heap{
            private int[] table;
            int cap;
            int size= 0 ;
            public Heap(int cap){
                this.table = new int[cap];
                this.cap = cap;
            }

            public int takeHead(){
                int head = table[0];
                table[0] = table[--size];
                table[size] = 0;
                sink(0);
                return head;
            }

            public void add(int n){
                if(size < cap ){
                    table[size] = n;
                    swim(size);
                    size++;
                }
            }


            public int getParent(int n){
                if(n <=0 )return -1;
                return (n-1)/2;
            }

            public int getLeftChild(int n){
                int child =  n*2+1;
                if(child>= size){
                    return -1;
                }
                return child;
            }
            public int getRightChild(int n){
                int child =  n*2+2;
                if(child>= size){
                    return -1;
                }
                return child;
            }

            private void swap(int[] table,int i1,int i2){
                int tmp = table[i1];
                table[i1] = table[i2];
                table[i2] = tmp;
            }

            public void swim(int n){
                int parent = getParent(n);
                if(parent<0)return;
                if(table[parent] < table[n]){
                    swap(table,parent,n);
                    swim(parent);
                }
            }

            public void sink(int n){
                int left = getLeftChild(n);
                int right = getRightChild(n);
                if(left < 0 )return;
                else if(right <0){
                    if(table[n] < table[left]){
                        swap(table,n,left);
                        sink(left);
                    }
                }else{
                    int max = -1;
                    if(table[left]> table[right]){
                        max = left;
                    }else {
                        max = left;
                    }
                    if(table[n] < table[max]){
                        swap(table,n, max);
                        sink(max);
                    }
                }
            }
        }
    }
