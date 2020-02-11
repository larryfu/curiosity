package cn.larry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Calculate24 {

    public static void main(String[] args) {
      //  List<Inte>
        int[] nums = new int[]{1,2,3,4};
        List<List<Integer>> lists = combination(nums);

        for(int i=0;i<3;i++){

            for(char c:ca){

            }
        }
        for(List<Integer> list:lists){

        }
        System.out.println(lists.size());
        for(List<Integer> l:lists){
            for(Integer i:l){
                System.out.print(i+",");
            }
            System.out.println();
        }
    }

    public static List<List<Character>> opCombination(int n){
        char[] ca = new char[]{'+','-','*','/'};
        List<List<Integer>> lists = new ArrayList<>();
        for(int i=0;i<n;i++){
            List<List<Character>> newList = new ArrayList<>();
            newList.add(Arrays.asList());
            for(char c:ca){

            }
        }
    }

    public static List<List<Integer>> combination(int[] nums){
        List<List<Integer>> lists = new ArrayList<>();
        lists.add(Arrays.asList(nums[0]));
        for(int j=1;j<nums.length;j++){
            int n = nums[j];
            List<List<Integer>> newList = new ArrayList<>();
            for(List<Integer> l:lists){
                for(int i=0;i<=l.size();i++){
                    List<Integer> nl = new LinkedList<>(l);
                    nl.add(i,n);
                    newList.add(nl);
                }
            }
            lists = newList;
        }
        return lists;
    }

    public List<Integer> calculate(int[] nums){
        List<Integer> integers = new LinkedList<>();
        for(int num:nums){
            integers.add(num);
        }
return integers;
    }
}
