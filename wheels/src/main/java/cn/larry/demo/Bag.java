package cn.larry.demo;


import java.util.*;

/**
 * 动态规划解背包问题
 */

public class Bag {

    public static void main(String[] args) {
        List<Item> items = new ArrayList<>();
        items.add(new Item(3,7));
        items.add(new Item(2,4));
        items.add(new Item(4,11));
        items.add(new Item(3,6));
        System.out.println(maxValue(8,items));

    }

    /**
     * 多副本背包问题
     * @param n
     * @param items
     * @return
     */
    private  static int maxValue(int n, List<Item> items){
        Map<Integer,Integer> bagValue = new HashMap<>(); //背包容量  最大价值
        bagValue.put(0,0);
        int maxValue = 0;
        for(int i=1;i<=n;i++){
            for(Item item:items){
                int left = i-item.weight;
                if(left>=0) { //背包能装下item
                    int  value = item.value +bagValue.get(left);
                    if(value>maxValue) maxValue = value;
                }
            }
            bagValue.put(i,maxValue);
        }
        return bagValue.get(n);
    }

    /**
     * 单副本背包问题
     * @param n
     * @param items
     * @return
     */
    private static int maxValueSingle(int n,List<Item> items){
        Map<Integer,BagSolution> solutionMap = new HashMap<>();
        int maxValue = 0;
        BagSolution best = null;
        for(int i = 0;i<=n;i++){
            for(int j = 0;j<items.size();j++){
                Item item = items.get(j);
                int left = i - item.weight;
                if(left >= 0){ //背包能装下item
                    BagSolution solution = solutionMap.get(left);
                    if(solution == null) solution = new BagSolution(0);
                    if( !solution.items.contains(j)){ //item j未被使用
                        int value = item.value + solution.value;
                        if(value>maxValue){
                            maxValue = value;
                            BagSolution solution1 = new BagSolution(value);
                            solution1.items.addAll(solution.items);
                            solution1.items.add(j);
                            best = solution1;
                        }
                    }
                }
            }
            solutionMap.put(i,best);
        }
        return solutionMap.get(n).value;
    }

    static class BagSolution{
        int value;
        Set<Integer> items;
        public BagSolution(int value){
            this .value = value;
            this.items = new HashSet<>();
        }
    }

    static class Item{
        int weight;
        int value;
        public Item(int w,int v){
            this.weight = w;
            this.value = v;
        }
    }
}
