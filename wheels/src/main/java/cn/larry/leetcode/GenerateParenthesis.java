package cn.larry.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 22. 括号生成
 * 数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 有效的 括号组合。
 */
public class GenerateParenthesis {
    public List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        if(n==0){
            result.add("");
            return result;
        }
        if(n == 1){
            result.add("()");
            return result;
        }
        for(int i=0;i<n;i++){
            List<String> pres = generateParenthesis(i);
            for(String pre:pres){
                List<String> sufs = generateParenthesis(n-1-i);
                for(String suf:sufs){
                    StringBuilder sb = new StringBuilder();
                    sb.append('(');
                    sb.append(pre);
                    sb.append(')');
                    sb.append(suf);
                    result.add(sb.toString());
                }
            }
        }
        return result;
    }
}
