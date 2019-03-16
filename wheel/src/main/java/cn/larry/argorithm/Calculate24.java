package cn.larry.argorithm;

import java.util.*;

/**
 * 速算24 ，速算任意值，支持任意个数字，任意种运算符，
 * 暴力搜索所有组合
 */
public class Calculate24 {

    public static void main(String[] args) {
        String[] ops = new String[]{"+", "-", "*", "/"};
        int[] nums = new int[]{1, 2, 3, 4};
        List<int[]> list = combination(nums);
        List<List<String>> opCOms = opCombination(3);
        Set<Integer> integers = new HashSet<>();
        for (int[] ints : list) {
            for (List<String> stringList : opCOms) {
                int result = calculate(ints, stringList);
                integers.add(result);
                if (result == 24) {
                    System.out.println();
                }
            }
        }
        System.out.println(integers.size());
//        for(int[] ints:list){
//            int result = ints[0];
//            for(int i=1;i<ints.length;i++){
//                for(String op:ops){
//                    result = calculate(op,result,ints[i]);
//                }
//            }
//        }
    }

    static int calculate(int[] numbers, List<String> ops) {
        if (numbers.length != ops.size() + 1) {
            throw new IllegalArgumentException();
        }
        int result = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            int num = numbers[i];
            String op = ops.get(i - 1);
            result = operate(op, result, num);
        }
        return result;
    }

    private static int operate(String op, int result, int num) {
        if (op.equals("+")) return result + num;
        if (op.equals("-")) return result - num;
        if (op.equals("*")) return result * num;
        if (op.equals("/") && num != 0) return result / num;
        throw new IllegalArgumentException(" illegal op " + op);
    }

    /**
     * 算数组 numbers的所有组合，动态规划法
     *
     * @param numbers
     * @return
     */
    public static List<int[]> combination(int[] numbers) {
        Map<Integer, List<int[]>> map = new HashMap<>();
        map.put(0, Arrays.asList(new int[0]));
        for (int i = 1; i <= numbers.length; i++) {
            int newNum = numbers[i - 1];
            List<int[]> currents = new ArrayList<>();
            for (int[] pre : map.get(i - 1)) {
                for (int j = i - 1; j >= 0; j--) {
                    int[] combine = new int[i];
                    for (int k = 0, index = 0; k < i; k++) {
                        combine[k] = k == j ? newNum : pre[index++];
                    }
                    currents.add(combine);
                }
            }
            map.put(i, currents);
        }
        return map.get(numbers.length);
    }

    public static List<List<String>> opCombination(int nums) {
        String[] ops = new String[]{"+", "-", "*", "/"};
        Map<Integer, List<List<String>>> combineMap = new HashMap<>();
        combineMap.put(0, Arrays.asList(new ArrayList<>()));
        for (int i = 1; i <= nums; i++) {
            List<List<String>> opComs = combineMap.get(i - 1);
            List<List<String>> newComs = new ArrayList<>();
            for (List<String> opCom : opComs) {
                for (String op : ops) {
                    List<String> newCom = new ArrayList<>(opCom);
                    newCom.add(op);
                    newComs.add(newCom);
                }
            }
            combineMap.put(i, newComs);
        }
        return combineMap.get(nums);
    }

    public void printCombine(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            // for(int )
        }
    }


}
