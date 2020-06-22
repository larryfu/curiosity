package cn.larry.leetcode;

/**
 * 287. 寻找重复数
 * 给定一个包含 n + 1 个整数的数组 nums，其数字都在 1 到 n 之间（包括 1 和 n），可知至少存在一个重复的整数。假设只有一个重复的整数，找出这个重复的数。
 *
 * 不能更改原数组（假设数组是只读的）
 * 二分查找不改变数组的情况下查找重复数字
 *
 *
 */
public class FindDup {
    public int findDuplicate(int[] nums) {
        int start=1;
        int end = nums.length+1;
        while(start < end ){
            if(start == end -1){
                return start;
            }
            int medium = (start+end)/2;
            int num = 0;
            for(int i = 0;i< nums.length;i++){
                if(nums[i] >= start && nums[i] < medium){
                    num++;
                }
            }
            if(num > medium - start){
                end = medium;
            }else{
                start = medium;
            }
        }
        return start;
    }

}
