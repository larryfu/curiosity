package cn.larry.graph;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class DepthFirstOrder {

    private boolean[] marked;
    private Queue<Integer> pre;
    private Queue<Integer> post;
    private Stack<Integer> reversPost;

    public DepthFirstOrder(Digraph g){
        pre = new LinkedList<>();
        post = new LinkedList<>();
        reversPost = new Stack<>();
        marked=  new boolean[g.V()];
        for(int v=0;v<g.V();v++){
            dfs(g,v);
        }
    }

    private void dfs(Digraph g,int v){
        pre.offer(v);
        marked[v] =true;
        for(int w:g.adj(v)){
            if(!marked[w]){
                dfs(g,w);
            }
        }
        post.offer(v);
        reversPost.push(v);
    }
}
