package cn.larry;

import java.util.ArrayList;
import java.util.List;

/**
 * 寻找共同祖先节点
 */
public class FindSameParent {

    public  void findParent(Node root,int a,int b) {
        dfs(root,a,b,new ArrayList<>());
    }

    public static void main(String[] args) {
        Node node = new Node(1);
        node.leftChild = new Node(2);
        node.rightChild = new Node(5);
        node.leftChild.leftChild = new Node(3);
        node.leftChild.leftChild.rightChild = new Node(4);
        node.leftChild.leftChild.leftChild = new Node(6);
        List<Node> path = new ArrayList<>();
        findPath(node,4,path);
      //  System.out.println();

        FindSameParent findSameParent = new FindSameParent();
        findSameParent.dfs(node,3,5,new ArrayList<>());
        System.out.println();


    }

    public static boolean findPath(Node node,int a,List<Node> path){
        if(node.value == a){
            path.add(node);
            return true;
        }
        if(node.leftChild !=null){
            path.add(node);
            boolean  leftFind = findPath(node.leftChild,a,path);
            if(!leftFind){
                path.remove(path.size()-1);
            }else return true;
        }
        if(node.rightChild !=null){
            path.add(node);
           boolean rightFind = findPath(node.rightChild,a,path);
            if(!rightFind){
                path.remove(path.size()-1);
            }else return true;
        }
        return false;
    }

    List<Node> patha ;
    List<Node> pathb;
    private void dfs(Node node, int a, int b, List<Node> path){
        path.add(node);
       if(node.value == a){
           patha = new ArrayList<>(path);
       }
       if(node.value == b){
           pathb = new ArrayList<>(path);
       }
       if(node.leftChild!=null){
           dfs(node.leftChild,a,b,path);
       }
       if(node.rightChild!=null){
           dfs(node.rightChild,a,b,path);
       }
       //移除本次添加的node
        path.remove(path.size()-1);
    }

}


class Node{
    int value;
    Node leftChild;
    Node rightChild;
    public Node(int value){
        this.value = value;
    }
}