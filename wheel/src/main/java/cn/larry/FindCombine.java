package cn.larry;

import org.apache.lucene.search.IndexSearcher;

import java.util.*;

public class FindCombine {

    public static void main(String[] args) {
        int[] nums = new int[]{2,3,5};
        List<List<Integer>> combines = findCombine(nums,8);
        for(List<Integer> list:combines){
            list.forEach(System.out::print);
            System.out.println();
        }
    }


   public static List<List<Integer>> findCombine(int[] nums,int target){
       int min = Integer.MAX_VALUE;
       Set<Integer> integerSet = new HashSet<>();
       for(int n:nums){
           integerSet.add(n);
           if(n<min) min = n;
       }
       return findCombine(integerSet,target,min);
   }

   private static Map<Integer,List<List<Integer>>> solutions = new HashMap<>();

   private static List<List<Integer>> findCombine(Set<Integer> integers,int target,int min){

       if(solutions.containsKey(target)){
           return solutions.get(target);
       }

       //IndexSearcher

       List<List<Integer>> combines = new ArrayList<>();
       if(integers.contains(target)){
           List<Integer> list = new ArrayList<>();
           list.add(target);
           combines.add(list);
       }

       for(int n :integers){
           int left = target - n;
           if(left>= min ){
               List<List<Integer>> inters = null;
               if(solutions.containsKey(left)){
                   inters = solutions.get(left);
               }else
             inters = findCombine(integers,left,min);
            for(List<Integer> integerList:inters){
                if(n>=integerList.get(integerList.size()-1)){
                    List<Integer> slist = new ArrayList<>(integerList);
                    slist.add(n);
                    combines.add(slist);
                }
            }
           }
       }
       solutions.put(target,combines);
       return combines;
   }
}
