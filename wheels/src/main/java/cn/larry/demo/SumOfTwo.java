package cn.larry.demo;

import java.util.HashMap;
import java.util.Map;

/**
 * 在数组中找到和为target的两个数，
 * 通过hashMap实现复杂度为O(n)的算法
 */
public class SumOfTwo {

    public int[] twoSum(int[] nums, int target) {
        Map<Integer,Integer> map = new HashMap<>();
        for(int i=0;i<nums.length;i++){
            int complement = target - nums[i];
            if(map.containsKey(complement)){
                return new int[]{map.get(complement),i};
            }
            map.put(nums[i],i);
        }
        return null;
    }
}
