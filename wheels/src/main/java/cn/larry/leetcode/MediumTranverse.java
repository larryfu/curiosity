package cn.larry.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 中序遍历二叉树，非递归
 */
public class MediumTranverse {

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        if(root == null) return list;
        Stack<TreeNode> stack = new Stack<>();
        TreeNode node = root;
        while(node!=null || !stack.isEmpty()){
            while(node !=null){
                stack.push(node);
                node = node.left;
            }
            node = stack.pop();
            list.add(node.val);
            node = node.right;
        }
        return list;
    }
}
