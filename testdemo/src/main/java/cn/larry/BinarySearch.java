package cn.larry;

public class BinarySearch {

    public static void main(String[] args) {
        int[] nums = new int[]{1, 3, 5, 6, 9};
        System.out.println(binarySearch(nums, 6));
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
