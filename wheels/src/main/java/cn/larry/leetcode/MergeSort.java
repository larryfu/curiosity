package cn.larry.leetcode;

/**
 * 自顶向下的归并排序,递归
 */
public class MergeSort {

    public static void main(String[] args) {
        int[] nums = new int[]{3,4,1,2,5};
        mergeSort(nums);
        for(int n:nums){
            System.out.println(n);
        }
    }

    public static void mergeSort(int[] nums){
        if(nums == null || nums.length<=1) {
            return ;
        }
        mergeSort(nums,0,nums.length);
    }

    private static void mergeSort(int[] nums,int start,int end){
        if(start==end || start+1 == end)return;
        int medium = (start +end)/2;
        mergeSort(nums,start,medium);
        mergeSort(nums,medium,end);
        merge(nums,start,medium,end);
    }

    private static void merge(int[] nums, int start, int medium, int end) {
        int i = start;
        int j = medium;
        int[] tmp = new int[end - start];
        for (int k = 0; k < tmp.length; k++) {
            if (i >= medium) tmp[k] = nums[j++];
            else if (j >= end) tmp[k] = nums[i++];
            else if (nums[i] < nums[j]) tmp[k] = nums[i++];
            else tmp[k] = nums[j++];
        }
        System.arraycopy(tmp, 0, nums, start, tmp.length);
    }

    private int[] merge(int[] l,int[] r){
        int[] merger = new int[l.length+r.length];
        int il = 0;
        int ir =0;
        for(int i = 0;i<merger.length;i++){
            if(il>= l.length){
                merger[i] = r[ir++];
            }else if(ir >= r.length){
                merger[i] = l[il++];
            }else if(l[il]< r[ir]){
                merger[i] = l[il++];
            }else{
                merger[i] = r[ir++];
            }
        }
        return merger;
    }
}
