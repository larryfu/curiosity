package cn.larry.tree;

public class RedBlackTree<K extends Comparable<K>, V> {

    Node root;


    public V get(K k) {

        Node<K, V> node = doGet(k, root);

        if (node != null && k.equals(node.key)) {
            return node.value;
        } else {
            return null;
        }
    }

    public Node<K, V> doGet(K k, Node<K, V> node) {
        if (node == null || k == null) {
            return null;
        }
        if (k.equals(node.key)) {
            return node;
        }
        if (node.isLeaf) {
            return node;
        }
        if (k.compareTo(node.key) < 0) {
            return doGet(k, node.leftChild);
        }
        return doGet(k, node.rightChild);
    }

    public Node<K, V> put(K k, V v) {
        Node<K, V> node = doGet(k, root);
        if (node != null && k.equals(node.key)) {
            node.value = v;
            return node;
        }
        if (node != null) {
            Node<K, V> putNode = new Node<K, V>();
            putNode.key = k;
            putNode.value = v;
            putNode.isLeaf = true;
            putNode.isRed = true;
            putNode.parent = node;
            if (k.compareTo(node.key) < 0) {
                node.isLeaf = false;
                node.leftChild = putNode;
                if (node.isRed) {
                    Node<K, V> parent = node.parent;
                    Node<K, V> node1 = rotateRight(parent);
                    putNode.isRed = false;
                    node.isRed = parent.isRed;
                    parent.isRed = false;
                }
            } else {
                node.isLeaf = false;
                node.rightChild = putNode;
                if (node.isRed) {
                    Node<K, V> parent = node.parent;
                    rotateLeft(node);
                    rotateRight(parent);
                    putNode.isRed = false;
                    node.isRed = parent.isRed;
                    parent.isRed = false;
                }
            }
        }


    }

    public Node<K, V> delete(K k, Node<K, V> root) {
        Node<K, V> node = doGet(k, root);
        if (!k.equals(node.key)) {
            return null;
        }
        Node<K, V> parent = node.parent;
        while (parent != null) {
            if(!parent.isRed){

            }
        }

        //叶子红节点可直接删除
        if (node.isLeaf && node.isRed) {
            Node<K, V> node1 = node.parent;
            node1.leftChild = null;
        }

        //处在三节点中
        if (node.isRed || node.leftChild != null && node.leftChild.isRed) {

        }

    }

    public Node<K, V> rotateLeft(Node node) {
        Node<K, V> parent = node.parent;
        Node<K, V> x = node.rightChild;
        node.rightChild = x.leftChild;
        x.leftChild = node;
        x.isRed = node.isRed;
        node.isRed = true;
        if (parent != null) {
            if (parent.leftChild == node) {
                parent.leftChild = x;
            }
            if (parent.rightChild == node) {
                parent.rightChild = x;
            }
        }
        return x;

    }

    public Node<K, V> rotateRight(Node node) {

    }

    static class Node<K extends Comparable<K>, V> {
        K key;
        V value;
        Node parent;
        boolean root;
        Node<K, V> leftChild;
        Node<K, V> rightChild;
        boolean isRed;
        boolean isLeaf;
    }


}
