package cn.larry.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 95. 不同的二叉搜索树 II
 * 给定一个整数 n，生成所有由 1 ... n 为节点所组成的 二叉搜索树 。
 */
public class DifferentTree {

    public List<TreeNode> generateTrees(int n) {
        if(n == 0)return new ArrayList<>();
        return buildBST(1,n);

    }

    private List<TreeNode> buildBST(int start,int end){
        List<TreeNode> list = new ArrayList<>();
        if(start > end) {
            list.add(null);
            return list;
        }
        if(start == end) {
            list.add(new TreeNode(start));
            return list;
        }

        for(int i = start;i<=end;i++){
            List<TreeNode> leftChildren =  buildBST(start,i-1);
            List<TreeNode> rightChildren = buildBST(i+1,end);
            for(TreeNode left:leftChildren){
                for(TreeNode right:rightChildren){
                    TreeNode root = new TreeNode(i);
                    root.left = left;
                    root.right = right;
                    list.add(root);
                }
            }
        }
        return list;
    }
}

   class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; }
  }
