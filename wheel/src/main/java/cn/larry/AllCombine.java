package cn.larry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllCombine {

    public static void main(String[] args) {
        List<String> strs = allCombine("abc");
        for(String s:strs){
            System.out.println(s);
        }
    }
    static Map<String,List<String>> resultMap = new HashMap<>();

    public static List<String> allCombine(String str){
        if(resultMap.containsKey(str)){
            return  resultMap.get(str);
        }
        List<String> list = new ArrayList<>();
        if(str == null){
            return list;
        }
        if(str.length() == 0 || str.length() == 1 ){
            list.add(str);
            return list;
        }
        char[] chars = str.toCharArray();
        for(int i = 0;i<chars.length;i++){
            StringBuilder sb = new StringBuilder(str);
            sb.deleteCharAt(i);
            List<String> subList =  allCombine(sb.toString());
            for(String s:subList){
                list.add(chars[i]+s);
            }
        }
        resultMap.put(str,list);
        return list;
    }
}
