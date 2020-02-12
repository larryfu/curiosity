package cn.larry.demo;

/**
 * 动态规划，编辑距离
 */
public class StrDistance {
    public int minDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();
        int[][] distances = new int[n+1][m+1];
        for(int i=0;i<=n;i++)distances[i][0]=i;
        for(int i=0;i<=m;i++)distances[0][i]=i;
        for(int i = 1;i<=n;i++){
            for(int j=1;j<=m;j++){
                if(word1.charAt(i-1) == word2.charAt(j-1)){
                    distances[i][j] = distances[i-1][j-1];
                }else{
                    distances[i][j] = Math.min(distances[i-1][j]+1,Math.min(distances[i][j-1]+1,distances[i-1][j-1]+1));
                }
            }
        }
        return distances[n][m];
    }
}
