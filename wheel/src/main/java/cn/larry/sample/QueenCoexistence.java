package cn.larry.sample;

import java.util.ArrayList;

/**
 * n 皇后问题
 */
public class QueenCoexistence {

    public static void main(String[] args) {
        calculate(4);
    }

    public static void calculate(int num) {
        Node head = new Node(null, null, new ArrayList<Node>());
        for (int i = 0; i < num; i++) {
            head.children.add(new Node(head, new Node.Position(0, i), new ArrayList<Node>())); //初始化第一行起始queen位置
        }
        for (Node node : head.children) {
            search(node, num);
        }
    }


    private static void search(Node currentNode, int num) {
        int[][] matrix = new int[num][num];
        Node cnode = currentNode;
        while (cnode != null) {
            if (cnode.position != null)
                setAvaibility(matrix, cnode.position);
            cnode = cnode.father;
        }
        int nextX = currentNode.position.x + 1;
        if (nextX >= matrix.length) { //queen已全部放置完毕
            System.out.println("find solution");
            Node node = currentNode;
            while (node != null) {
                if (node.position != null)
                    System.out.printf(node.position.toString());
                node = node.father;
            }
            System.out.println();
        } else {
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[nextX][i] == 0) {
                    currentNode.children.add(new Node(currentNode, new Node.Position(nextX, i), new ArrayList<Node>()));
                }
            }
            for (Node node : currentNode.children) {
                search(node, num);
            }
        }
    }

    public static void setAvaibility(int[][] matrix, Node.Position p) {
        int len = matrix.length;
        for (int i = 0; i < len; i++) {
            matrix[p.x][i] = 1;
            matrix[i][p.y] = 1;
        }
        int x = p.x, y = p.y;
        for (; x >= 0 && x < len && y >= 0 && y < len; x++, y++) {
            matrix[x][y] = 1;
        }
        for (x = p.x, y = p.y; x >= 0 && x < len && y >= 0 && y < len; x--, y--) {
            matrix[x][y] = 1;
        }
        for (x = p.x, y = p.y; x >= 0 && x < len && y >= 0 && y < len; x++, y--) {
            matrix[x][y] = 1;
        }
        for (x = p.x, y = p.y; x >= 0 && x < len && y >= 0 && y < len; x--, y++) {
            matrix[x][y] = 1;
        }
    }

}
