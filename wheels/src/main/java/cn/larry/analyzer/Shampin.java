package cn.larry.analyzer;

public class Shampin {
    public static void main(String[] args) {

        shampinRate(19,3,0);
    }

    public static double shampinRate(int n,int i,int j){
        if(n>  (i+1)*(i+1)){
            return 1;
        }
        if(j>i){
            System.out.println("error j>i");
            return -1;
        }
        if(n<=0){
            return 0;
        }
        if(i<=0){
            return n-1;
        }
        double[][] over = new double[i+1][i+1];
        over[0][0] = n-1;
        for(int k = 1;k<=i;k++){
            for(int l = 0;l<=k;l++){
                double cur = 0.5 * over[k-1][l];
                if(l>0){
                    cur = cur + 0.5* over[k-1][l-1];
                }
                if(cur >1){
                    over[k][l] = cur-1;
                }else {
                    over[k][l] = 0;
                }
            }
        }
       double cur = 0.5*over[i-1][j];
        if(j>0){
           cur = cur + 0.5 *over[i-1][j-1] ;
        }
        if(cur>1){
            cur = 1;
        }
        System.out.println(cur);
        return cur;
    }
}
