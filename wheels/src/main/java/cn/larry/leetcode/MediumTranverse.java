package cn.larry.leetcode;

import java.util.*;

/**
 * 中序遍历二叉树，非递归
 */
public class MediumTranverse {

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        if (root == null) return list;
        Stack<TreeNode> stack = new Stack<>();
        TreeNode node = root;
        while (node != null || !stack.isEmpty()) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
            node = stack.pop();
            list.add(node.val);
            node = node.right;
        }
        return list;
    }

    public static void main(String[] args) {
        TreeNode node = new TreeNode(1);
        node.left = new TreeNode(2);
        node.right = new TreeNode(3);
        node.left.right = new TreeNode(4);
         List<Integer> integers = preTraversal(node);
         integers.forEach(System.out::println);
    }

    /**
     * 先序遍历非递归
     * 将先序遍历左右互换，反向输出之后是后序遍历
     * @param root
     * @return
     */
    public static List<Integer> preTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode node = root;
        while (node != null || !stack.isEmpty()) {
            while (node != null) {
                stack.push(node);
                list.add(node.val);
                node = node.left;
            }
            node = stack.pop();
            node = node.right;
        }
        return list;
    }

    /**
     *  中序遍历，非递归
     * @param root
     * @return
     */
    public List<Integer> inorderTraversalOld(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        if (root == null) return list;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        Set<TreeNode> traversaled = new HashSet<>();
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            if (node.left == null || traversaled.contains(node.left)) {
                list.add(node.val);
                if (node.left == null && node.right != null) {
                    stack.push(node.right);
                }
                traversaled.add(node);
            } else {
                if (node.right != null) {
                    stack.push(node.right);
                }
                stack.push(node);
                stack.push(node.left);
            }
        }
        return list;
    }
}
