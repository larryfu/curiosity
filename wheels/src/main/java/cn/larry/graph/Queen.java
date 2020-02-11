package cn.larry.graph;

import java.util.List;
import java.util.Stack;

public class Queen {
    public static void main(String[] args) {
        List<Position> positions = solveNQueens(4);
        if(positions!=null){
            for(Position p:positions)
            System.out.println(p);
        }
    }


        public static List<Position> solveNQueens(int n) {
            Stack<Position> positions = new Stack<>();
            positions.push(new Position(0,0));
            for(int i = 1;i<n;i++){
                for(int j=0;j<n;j++){
                    Position pos = new Position(i,j);
                    boolean able = true;
                    for(Position p :positions){
                        if(!canQueenCoexists(pos,p)){
                            able = false;
                            break;
                        }
                    }
                    if(able){
                        positions.push(pos);
                        break;
                    }else
                    if( j== n-1){
                        Position p = positions.pop();
                        i= p.x;
                        j = p.y+1;
                        if(j>=n){
                            return null;
                        }
                        break;
                    }
                }
            }
            return positions;
        }

        private static boolean canQueenCoexists(Position p1,Position p2){
            return (p1.x-p2.x)*(p1.y-p2.y) != 0
                    && Math.abs((p1.x-p2.x)/(p1.y-p2.y))!=1;
        }

    static class Position{
        public final int x;
        public final int y;
        public Position(int x,int y){
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "("+x+","+y+")";
        }
    }

}
