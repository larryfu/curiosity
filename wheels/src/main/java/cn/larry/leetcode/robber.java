package cn.larry.leetcode;

/**
 * 198. 打家劫舍
 * 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
 *
 * 给定一个代表每个房屋存放金额的非负整数数组，计算你 不触动警报装置的情况下 ，一夜之内能够偷窃到的最高金额。
 *
 *
 */
    public class robber{
    public int rob(int[] nums) {
        if(nums.length == 0) return 0;
        int[][] dp = new int[nums.length][2];
        dp[0][0] = 0;//不打劫本屋
        dp[0][1] = nums[0]; //打劫本屋
        for(int i=1;i<nums.length;i++){
            dp[i][0] = Math.max(dp[i-1][0],dp[i-1][1]); //不打劫本屋
            dp[i][1] = dp[i-1][0]+nums[i];//打劫本屋
        }
        return Math.max(dp[nums.length-1][0],dp[nums.length-1][1]);
    }
}