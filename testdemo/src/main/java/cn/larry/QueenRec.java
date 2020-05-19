package cn.larry;

public class QueenRec {



    private final int NUM;

    int[] positions;

    private int count;

    public static void main(String[] args) {
        QueenRec queenRec = new QueenRec(8);
        queenRec.putQueen(0);
        System.out.println(queenRec.count);
    }

    public QueenRec(int num){
        this.NUM = num;
        positions = new int[num];
    }


    private void putQueen(int n){
        if(n==NUM){
            count++;
            return;
        }
        for(int i = 0;i<NUM;i++){
            positions[n] = i;
            if(canCoexists(n)) {
               putQueen(n+1);
            }
        }
    }

    private boolean canCoexists(int n){
        for(int i=0;i<n;i++){
            if(!canCoexists(i,n)){
                return false;
            }
        }
        return true;
    }

    private boolean canCoexists(int x1,int x2){
        int y1 = positions[x1];
        int y2 = positions[x2];
        return  !(x1==x2 || y1 == y2 || Math.abs(x1-x2) == Math.abs(y1-y2));
    }

}
