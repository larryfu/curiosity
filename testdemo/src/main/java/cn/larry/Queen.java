package cn.larry;



import java.util.ArrayList;
import java.util.List;
import java.util.Stack;



public class Queen {

    public static void main(String[] args) {

        for(int i = 0;i<15;i++){
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

    /**
     * (0,5)
     * (1,1)
     * (2,6)
     * (3,0)
     * (4,3)
     * (5,7)
     * (6,4)
     * (7,2)
     */

    public static List<List<Position>> solveQueen(int n) {
        List<List<Position>> results = new ArrayList<>();
        Stack<Position> positions = new Stack<>();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    Position pos = new Position(i, j);
                    boolean able = true;
                    for (Position p : positions) {
                        if (!canCoexists(pos, p)) {
                            able = false;
                            break;
                        }
                    }
                    if (able) {
                        positions.push(pos);
                        if (i == n - 1 && positions.size() == n) {
                            results.add(new ArrayList<>(positions.subList(0, positions.size())));
                            Position p = positions.pop();
                            i = p.x;
                            j = p.y;
                            while (j==n-1){
                                if (positions.isEmpty())
                                    return results;
                                p = positions.pop();
                                i = p.x;
                                j = p.y;
                            }
                        } else break;
                    } else {
                        while (j == n - 1) {
                            if (positions.isEmpty())
                                return results;
                            Position p = positions.pop();
                            i = p.x;
                            j = p.y;
                        }
                    }
                }

       //     }
        }


        return results;
    }

    private static boolean canCoexists(Position p1, Position p2) {
        return (p1.x - p2.x) * (p1.y - p2.y) != 0 &&
                Math.abs((p1.x - p2.x)) != Math.abs((p1.y - p2.y));
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
