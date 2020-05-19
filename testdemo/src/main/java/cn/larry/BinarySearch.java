package cn.larry;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BinarySearch {

    public static void main(String[] args) throws InterruptedException {
        int[] nums = new int[]{1, 3, 5, 6, 9};
        String s = "21";
        List<String > stringList = new ArrayList<>();
        InputStream inputStream ;
        Class<String> stringClass = String.class;
        System.out.println(binarySearch(nums, 7));
        TimeUnit.SECONDS.sleep(1000);
    }

    private static int binarySearch(int[] nums, int n) {
        return search(nums, 0, nums.length - 1, n);
    }

    private static int search(int[] nums, int start, int end, int num) {
        while (start <= end) {
            int medium = (start + end) / 2;
            if (nums[start] == num) return start;
            if (nums[medium] == num) return medium;
            if (nums[end] == num) return end;
            if (start == end) return -1;
            if (num > nums[medium]) {
                start = medium + 1;
                end = end - 1;
            } else {
                end = medium - 1;
                start = start + 1;
            }
        }
        return -1;
    }
}
