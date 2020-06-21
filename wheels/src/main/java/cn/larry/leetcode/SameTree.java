package cn.larry.leetcode;

import java.util.Stack;

/**
 * 100. 相同的树
 */
public class SameTree {
    public boolean isSameTree(TreeNode p, TreeNode q) {

        Stack<Integer> stack = new Stack<>();


        if (p == null && q == null) {
            return true;
        }
        if ((p == null && q != null) || (p != null && q == null)) {
            return false;
        }
        if (p.val != q.val) {
            return false;
        }
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }


    public boolean isSymmetric(TreeNode root) {
        if(root == null)return true;
        return isSymetricNode(root.left,root.right);
    }

    public boolean isSymetricNode(TreeNode n1,TreeNode n2){
        if(n1 == null && n2 == null) return true;
        if(n1 == null && n2!=null || n1 !=null && n2 == null)return false;
        if(n1.val!=n2.val ) return false;
        return isSymetricNode(n1.left,n2.right) && isSymetricNode(n1.right,n2.left);
    }
}
