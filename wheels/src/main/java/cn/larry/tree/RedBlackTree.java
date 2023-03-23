package cn.larry.tree;

import java.util.ArrayList;
import java.util.List;

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
            if (!parent.isRed) {

            }
        }

        //叶子红节点可直接删除
        if (node.isLeaf && node.isRed) {
            Node<K, V> node1 = node.parent;
            node1.leftChild = null;
        }

        Node<K, V> bigNear = node.rightChild;
        if (bigNear != null) {
            while (bigNear.leftChild != null) {
                bigNear = bigNear.leftChild;
            }
        }
        if (bigNear == null) {
            bigNear = node;
        }

        //将三个单节点节点合并为4节点
        List<Node<K, V>> nodes = new ArrayList<>();
        while (isMergeable4Node(bigNear)) {
            nodes.add(bigNear);
            bigNear = bigNear.parent;
        }
        for (int i = nodes.size() - 1; i >= 0; i--) {
            Node<K, V> node1 = nodes.get(i);
            if (isMergeable4Node(node1)) {
                merge24Node(node1);
            }
        }

        //将大于key的最小node删除
        deleteLeafNonSingleNode(bigNear);
        node.key = bigNear.key;
        node.value = bigNear.value;


        decompose4node(bigNear.rightChild != null ? bigNear.rightChild : bigNear.parent);

        return node;

    }

    //删除叶子节点，且无需合并单节点
    public void deleteLeafNonSingleNode(Node<K, V> node) {
        if (node.root) {
            root = null;
            return;
        }
        if (node.isRed) {
            node.parent.leftChild = null;
            return;
        }
        Node<K, V> brother = getBrother(node);

        if (brother.leftChild != null && brother.leftChild.isRed) {
            //是左子节点
            if (node.parent.leftChild == node) {
                node.key = node.parent.key;
                node.value = node.parent.value;
                node.parent.key = brother.leftChild.key;
                node.parent.value = brother.leftChild.value;
                brother.leftChild = null;
                return;
            } else {
                node.key = node.parent.key;
                node.value = node.parent.value;
                node.parent.key = brother.key;
                node.parent.value = brother.value;
                brother.key = brother.leftChild.key;
                brother.value = brother.leftChild.value;
                brother.leftChild = null;
                return;
            }
        }
        if (node.parent.isRed) {
            node.key = node.parent.key;
            node.value = node.parent.value;
            node.parent.key = brother.key;
            node.parent.value = brother.value;
            node.parent.rightChild = null;
            node.isRed = true;
            return;
        }
        //是三个单节点
        throw new IllegalArgumentException("pure single node");
    }

    //向上分解四节点
    public void decompose4node(Node<K, V> node) {

    }

    private boolean isMergeable4Node(Node<K, V> node) {
        if (node.isRed) {
            return false;
        }
        if (node.leftChild != null && node.leftChild.isRed) {
            return false;
        }
        if (node.parent == null || node.parent.isRed) {
            return false;
        }
        Node<K, V> brother = getBrother(node);
        if (brother == null || brother.isRed) {
            return false;
        }
        if (brother.leftChild != null && brother.leftChild.isRed) {
            return false;
        }
        return true;
    }

    private void merge24Node(Node<K, V> node) {

    }

    public Node<K, V> getBrother(Node<K, V> node) {
        if (node == null || node.parent == null) {
            return null;
        }
        if (node.parent.leftChild == node) {
            return node.parent.rightChild;
        } else {
            return node.leftChild;
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
        Node<K, V> parent;
        boolean root;
        Node<K, V> leftChild;
        Node<K, V> rightChild;
        boolean isRed;
        boolean isLeaf;
    }


}
