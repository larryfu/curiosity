package cn.larry.leetcode;

/**
 * 200. 岛屿数量
 * 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
 */
public class IsLandNum {

    public int numIslands(char[][] grid) {
        if(grid.length == 0 || grid[0].length ==0 ) return 0;
        boolean[][] mark = new boolean[grid.length][grid[0].length];
        int iland=0;
        for(int i = 0;i< grid.length;i++){
            for(int j = 0;j<grid[0].length;j++){
                if(!mark[i][j]){
                    if(grid[i][j] == '1'){
                        iland++;
                        mark[i][j] = true;
                        markAround(grid,i,j,mark);
                    }
                    mark[i][j] = true;
                }
            }
        }
        return iland;
    }

    private void markAround(char[][] grid,int i,int j, boolean[][] mark){
        if(i-1 >= 0 && !mark[i-1][j] && grid[i-1][j] == '1'){
            mark[i-1][j] = true;
            markAround(grid,i-1,j,mark);
        }
        if(i+1 < grid.length && !mark[i+1][j] && grid[i+1][j] == '1'){
            mark[i+1][j]= true;
            markAround(grid,i+1,j,mark);
        }
        if(j-1>=0 && !mark[i][j-1]&& grid[i][j-1] == '1'){
            mark[i][j-1]=true;
            markAround(grid,i,j-1,mark);
        }
        if(j+1 < grid[0].length && ! mark[i][j+1]&& grid[i][j+1] == '1'){
            mark[i][j+1] = true;
            markAround(grid,i,j+1,mark);
        }
    }

}
