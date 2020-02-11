package cn.larry;



import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 回溯法解N皇后问题
 */

public class Queen {

    public static void main(String[] args) {
        for(int i = 0;i<=8;i++){
            List<List<Position>> positionList = solveQueen(i);
            if (positionList != null){
                System.out.println(positionList.size());
//            for (List<Position> positions : positionList){
//                for (Position p : positions) {
//                    System.out.print(p);
//                }
//                System.out.println();
//            }
            }
        }
    }

    public static List<List<Position>> solveQueen(int n) {
        List<List<Position>> results = new ArrayList<>();
        Stack<Position> positions = new Stack<>();
            for (int i = 0; i < n; i++) { //i为横坐标
                for (int j = 0; j < n; j++) {  //j为纵坐标
                    Position pos = new Position(i, j);
                    boolean able = true;
                    //当前位置是否可行
                    for (Position p : positions) {
                        if (!canCoexists(pos, p)) {
                            able = false;
                            break;
                        }
                    }
                    boolean needBack = false; //是否需要回溯
                    if (able) {
                        positions.push(pos);
                        if (i == n - 1 && positions.size() == n) { //找到一种解
                            results.add(new ArrayList<>(positions.subList(0, positions.size())));
                            //回溯，继续搜索解空间，找下一组解
                            needBack = true;
                        } else break; //还未找到解继续下一行搜索
                    } else { //不可行
                        needBack = j==n-1;//如果纵坐标到底则回溯
                    }
                    if(needBack){
                        do{
                            if (positions.isEmpty()) //整个解空间已搜索完毕
                                return results;
                            Position p = positions.pop();
                            i = p.x;
                            j = p.y;
                        }while (j==n-1);
                    }
                }
        }
        return results;
    }

    /**
     * 判断两个皇后能否共存
     * @param p1
     * @param p2
     * @return
     */
    private static boolean canCoexists(Position p1, Position p2) {
        return (p1.x - p2.x) * (p1.y - p2.y) != 0 && //不在同一条水平竖直线上
                Math.abs((p1.x - p2.x)) != Math.abs((p1.y - p2.y)); //不在同一条斜线上
    }

    static class Position {
        final int x;
        final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }
}
