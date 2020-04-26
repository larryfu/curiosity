package cn.larry.graph;

import com.google.gson.internal.$Gson$Preconditions;

import java.util.Stack;

public class DirectedCycle {

    private boolean[] marked;
    private int[] edgeTo;
    private Stack<Integer> cycle;
    private  boolean[] onStack;


    public DirectedCycle(Digraph g){
        onStack = new boolean[g.V()];
        marked = new boolean[g.V()];
        edgeTo = new int[g.V()];
        for(int i = 0;i<g.V();i++){
            if(!marked[i]) dfs(g,i);
        }
    }

    private boolean hashCycle(){
        return cycle !=null;
    }
    private Iterable<Integer> cycle(){
        return cycle;
    }

    private void dfs(Digraph g,int v){
        onStack[v] = true;
        marked[v] = true;
        for(int w:g.adj(v)){
            if(this.hashCycle()){
                return;
            } else if(!marked[w]){
                edgeTo[w] = v;
                dfs(g,w);
            }else if(onStack[w]){
                cycle = new Stack<>();
                for(int x = v;x!=w;x = edgeTo[x])
                    cycle.push(x);
                cycle.push(w);
                cycle.push(v);
            }
        }
        onStack[v] = false;
    }
}
