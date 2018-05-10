package cn.larry.sort;

import java.util.Random;

/**
 * Created by larryfu on 2016/3/31.
 *
 * @author larryfu
 */
public class QuickSortStd {

    public static void main(String[] args) {
        Random random = new Random();
        int[] ints = new int[100];
        for (int i = 0; i < 100; i++)
            ints[i] = random.nextInt(1000);
        quickSort(ints);
        for (int i = 0; i < 100; i++)
            System.out.print(ints[i]+" ");
    }

    public static void quickSort(int[] nums) {
        if (nums.length == 0) return;
        sort(nums, 0, nums.length - 1);
    }

    private static void sort(int[] nums, int low, int high) {
        if (low >= high) return;
        int j = partition(nums, low, high);
        sort(nums, low, j - 1);
        sort(nums, j + 1, high);
    }

    private static int partition(int[] nums, int low, int high) {
        int tag = nums[low];
        int i = low, j = high + 1;
        while (true) {
            while (nums[++i] <= tag) if (i == high) break;
            while (nums[--j] >= tag) if (j == low) break;
            if (i >= j) break;
            swap(nums, i, j);
        }
        swap(nums, low, j);
        return j;
    }


    private static void swap(int[] nums, int index1, int index2) {
        int temp = nums[index1];
        nums[index1] = nums[index2];
        nums[index2] = temp;
    }

}
