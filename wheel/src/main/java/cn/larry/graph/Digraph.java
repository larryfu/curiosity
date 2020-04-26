package cn.larry.graph;

import java.util.ArrayList;
import java.util.List;

public class Digraph {

    private int V; //节点数

    private List<Integer>[] adj; //与节点连接的点

    private int E;

    public int V(){
        return V;
    }

    private int E(){
        return E;
    }


    public Digraph(int V){
        this.V = V;
        this.E = 0;
        adj = new List[V];
        for(int i = 0;i<V;i++){
            adj[i] = new ArrayList<>();
        }
    }

    public void addEdge(int s,int e){
        adj[s].add(e);
        E++;
    }

    public List<Integer> adj(int v){
        return adj[v];
    }

}
