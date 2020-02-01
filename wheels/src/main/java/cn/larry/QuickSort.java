package cn.larry;

public class QuickSort {


    public static void main(String[] args) {
        int[] nums =new int[]{1,2,5};
        quickSort(nums);
        for(int i:nums)
        System.out.print(i+" ");

    }


    public static void quickSort(int[] nums){
        int start = 0;
        int end = nums.length-1;
        sortOnce(nums,start,end);
    }



    private static void sortOnce(int[] nums,int start,int end){
        if(start>=end) return ;
        int n = nums[start];
        int i = start;
        int j = end;
        while (i<j){
            while (nums[i] <= n && i<end )i++;
            while (nums[j] >= n && j>start )j--;
            if(i<j)
                swap(nums,i,j);
        }
        swap(nums,start,j);
        sortOnce(nums,start,j-1);
        sortOnce(nums,i+j,end);
    }

    private static void swap(int[] nums,int i,int j){
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
