package cn.larry.leetcode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 一个长度为n的数组包含了0——n-1的数字，其中有数字重复和缺失，找到重复的和缺失的
 */
public class FindDuplicate {

    public static void main(String[] args) {
        int[] nums = new int[]{0, 3, 1, 3, 4};
        System.out.println(findAbsence(nums));
    }

    /**
     * 找到重复的
     * 空间复杂度O(1)时间复杂度O(n)
     * @param nums
     * @return
     */
    public static int findDuplicate(int[] nums) {
        if (nums == null || nums.length < 2) {
            return -1;
        }
        for (int i = 0; i < nums.length; i++) {
            while (nums[i] != i) {
                int n = nums[i];
                if (nums[n] == n) {
                    return n;
                }
                swap(nums, i, n);
            }
        }
        return -1;
    }

    /**
     * 找到所有重复的和缺失的
     * @param nums
     * @return
     */

    public static int findAbsence(int[] nums) {
        Set<Integer> dup = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            while (nums[i] != i && nums[i] != -1) {
                int n = nums[i];
                if (nums[n] == n) {
                    dup.add(n);
                    nums[i] = -1;
                    break;
                }
                swap(nums, i, n);
            }
        }
        List<Integer> absence = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == -1) {
                absence.add(i);
            }
        }
        return absence.get(0);
    }


    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

}
