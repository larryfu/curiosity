package cn.larry.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 71. 简化路径
 */
public class SimplifyPath {
    public String simplifyPath(String path) {
        String[] pathEle = path.split("/");
        List<String> simplified = new ArrayList<>();
        for(String s:pathEle){
            if(s.equals("..") && simplified.size()>0){
                simplified.remove(simplified.size()-1);
            }else if(!s.equals(".") && !s.equals("") && !s.equals("..")  ){
                simplified.add(s);
            }
        }
        return "/"+String.join("/",simplified);
    }
}