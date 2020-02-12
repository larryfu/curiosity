package cn.larry;

import java.util.*;

/**
 * 4个数字，加减乘除算24，暴力解法
 */
public class Calculate24 {

    static final char[] ca = new char[]{'+','-','*','/'};

    public static void main(String[] args) {
        int[] nums = new int[]{5,2,3,4};
        Set<Double> results = calculate24(nums);
        System.out.println(results.size());
        for(Double r:results){
            System.out.println(r);
        }
    }

    public static Set<Double> calculate24(int[] nums){
        List<List<Integer>> lists = combination(nums);
        List<List<Character>> lists1 = opCombination(nums.length-1);
        Set<Double> results = new HashSet<>();
        for(List<Integer> integers:lists){
            for(List<Character> characters :lists1){
                double result = calcualte(integers,characters);
                results.add(result);
            }
        }
        return results;
    }

    /**
     *  计算一种情况
     * @param nums
     * @param characters
     * @return
     */
    private static double calcualte(List<Integer> nums,List<Character> characters){
        if(nums.size()-characters.size()!=1)throw new IllegalStateException();
        double firstOp = nums.get(0);
        for(int i = 0;i<characters.size();i++){
            int secondOp = nums.get(i+1);
            char op = characters.get(i);
            firstOp = calculate(firstOp,secondOp,op);
        }
        return firstOp;
    }

    private static double calculate(double first,double second,char op){
        switch (op){
            case '+': return first+second;
            case '-': return first-second;
            case '*': return first*second;
            case '/': return first/second;
            default: throw new IllegalArgumentException();
        }
    }


    /**
     * 算运算符的排列,递归
     * @param n
     * @return
     */
    public static List<List<Character>> opCombination(int n){
        if(n<=0)return new ArrayList<>();
        List<List<Character>> lists =new ArrayList<>();
        if(n == 1 ){
            for(char c:ca)
                lists.add(Arrays.asList(c));
            return lists;
        }
        List<List<Character>>  lists1= opCombination(n-1);
        for(List<Character> lc:lists1){
            for(char c:ca){
               List<Character> clist = new LinkedList<>(lc);
               clist.add(c);
               lists.add(clist);
            }
        }
        return lists;
    }

    /**
     * 算数字的所有组合，递推，动态规划
     */
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

}
