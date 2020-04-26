package cn.larry;

public class MergeSort {

    private static void mergeSort(int[] nums) {
        int length = nums.length;
        for (int step = 2; step < 2*length; step *= 2) {
            for (int i = 0; i < length; i += step) {
                int start = i;
                int medium = Math.min(i + step / 2, length);
                int end = Math.min(i + step, length);
                if (medium != end) {
                    merge(nums, start, medium, end);
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] nums = new int[]{3,1,2,9,5,4,7};
        //merge(nums,0,1,2);
        mergeSort(nums);
        for(int i:nums){
            System.out.println(i);
        }
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
//        for(int k=0;k<tmp.length;k++){
//            nums[start+k]=tmp[k];
//        }
    }

}
