package cn.larry.analysis;

public class FindK {

    public static void main(String[] args){
        int[] nums = new int[]{3,5,7,2,4,9};
        System.out.println(findKth(nums,3));
    }

    public static int findKth(int[] a,  int K) {
        int index = findK(a,0,a.length-1,K);
        if(index < 0) return -1;
        return a[index];
    }

    private static int findK(int[] a,int start,int end,int k){
        if(start> end )return -1;
        int index = partition(a,start,end);
        if(index == k )return index;
        if(index > k ){
            return   findK(a,start,index-1,k);
        }else{
            return  findK(a,index+1,end,k);
        }
    }


    private static int  partition(int[] nums,int start,int end){
        int n = nums[0];
        int low = start;
        int high = end+1;
        while(true){
            while(nums[++low] <= n)if(low >= end)break;
            while(nums[--high] >= n)if(high <= start)break;
            if(low < high){
                swap(nums,low,high);
            }else break;
        }
        swap(nums,0,high);
        return high;
    }

    private static void swap(int[] table,int i1,int i2){
        int tmp = table[i1];
        table[i1] = table[i2];
        table[i2] = tmp;
    }

}
