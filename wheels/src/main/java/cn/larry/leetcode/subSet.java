package cn.larry.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 90. 子集 II
 * 给定一个可能包含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）。
 * <p>
 * 说明：解集不能包含重复的子集。
 * <p>
 * 递归解法，1ms，时间打败了99.97% ！
 */
public class subSet {

    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null) {
            return result;
        }
        Arrays.sort(nums);
        //找到大小从0-size的子集
        for (int i = 0; i <= nums.length; i++) {
            result.addAll(subSetWithStart(nums, 0, i));
        }
        return result;
    }

    private List<List<Integer>> subSetWithStart(int[] nums, int start, int n) {
        List<List<Integer>> result = new ArrayList<>();
        if (n <= 0 || start >= nums.length || nums.length - start < n) {
            if (n == 0) {
                result.add(new ArrayList<>());
            }
            return result;
        }
        int pre = -1;
        for (int i = start; i < nums.length; i++) {
            if (i > 0 && nums[i] == pre) { //去重，如果当前值与前一个值相等则跳过，因为当前值和前一个值的结果是重复的
                continue;
            }
            //递归，找到剩余数组里n-1 大小的子集
            List<List<Integer>> subResults = subSetWithStart(nums, i + 1, n - 1);
            for (List<Integer> list : subResults) {
                list.add(0, nums[i]);
                result.add(list);
            }
            pre = nums[i]; //记录前值
        }
        return result;
    }
}
