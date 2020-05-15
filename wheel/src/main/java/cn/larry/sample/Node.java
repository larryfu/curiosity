package cn.larry.sample;



import java.util.List;

public class Node {


    Node father;

    List<Node> children;

    Position position;

    public Node(Node father, Position position, List<Node> children) {
        this.father = father;
        this.children = children;
        this.position = position;
    }


    public static class Position {
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int x, y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public String toString() {
            return "("+x + "," + y + ")";
        }
    }
}
