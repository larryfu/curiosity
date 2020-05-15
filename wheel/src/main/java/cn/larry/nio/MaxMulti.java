package cn.larry.nio;

public class MaxMulti {

    public static void main(String[] args) {
        int[] nums = new int[]{0, 0, 0,-5, 0};
        System.out.println(maxMulti(nums));
    }

    public static long maxMulti(int[] nums){
        int max = 0;
        int current = 0;
        for(int i = 0;i<nums.length;i++){
            if(nums[i]!=0){
                if(current == 0) current =1;
                current = current*nums[i];
                if(current>max) max = current;
            }else{
                current = 0;
            }
        }
        return max;
    }
}
