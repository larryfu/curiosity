package cn.larry.graph;

import java.util.List;

public class DirectedDFS {


    private boolean[] marked;

    private Digraph digraph;

    public DirectedDFS(Digraph digraph,int v){
        this.digraph = digraph;
        this.marked = new boolean[digraph.V()];
        mark(v);
    }

    public DirectedDFS(Digraph digraph, List<Integer> init){
        this.digraph = digraph;
        marked = new boolean[digraph.V()];
        for(int i:init){
            mark(i);
        }
    }

    private void mark(int v){
        marked[v] = true;
        for(int n :digraph.adj(v)){
            if(!marked[n]){
                mark(n);
            }
        }
    }

    public boolean marked(int v){
        return marked[v];
    }
}
