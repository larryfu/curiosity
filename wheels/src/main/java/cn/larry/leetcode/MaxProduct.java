package cn.larry.leetcode;

/**
 * 152. 乘积最大子数组
 * 给你一个整数数组 nums ，请你找出数组中乘积最大的连续子数组（该子数组中至少包含一个数字），并返回该子数组所对应的乘积。
 */
public class MaxProduct {

    public int maxProduct(int[] nums) {
        int max = nums[0];
        int[][]  dp = new int[nums.length][2];
        dp[0][0] =Math.max(nums[0],0);//  包含当前数的最大的积
        dp[0][1] = Math.min(nums[0],0);//  包含当前数的绝对值最大的负数积，用于下一个数为负时计算最大积
        for(int i=1;i< nums.length;i++){
            int n  = nums[i];
            if(n >= 0){  //根据当前数的正负更新dp数组
                dp[i][0] = Math.max(dp[i-1][0],1) * n;
                dp[i][1] = dp[i-1][1] *n;
            }
            if(n < 0 ){
                dp[i][0] = dp[i-1][1] * n;
                dp[i][1] = Math.max(dp[i-1][0],1)*n;
            }
            if(dp[i][0] > max) max =dp[i][0];
        }
        return max;
    }
}
