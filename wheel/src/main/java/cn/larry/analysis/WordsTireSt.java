package cn.larry.analysis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by larryfu on 16-5-21.
 */
public class WordsTireSt {

    //   private static int R = Character.MAX_VALUE;
    private Node root;

    static class Node {
        String value;
        Map<Character, Node> next = new HashMap<>();
    }

    public String get(String key) {
        Node x = get(root, key, 0);
        return x.value;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next.get(c), key, d + 1);
    }

    public void put(String key, String value) {
        if (key == null || value == null) return;
        root = put(root, key, value, 0);
    }

    private Node put(Node x, String key, String value, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.value = value;
            return x;
        }
        char c = key.charAt(d);
        x.next.put(c, put(x.next.get(c), key, value, d + 1));
        return x;
    }

    public String longestPrefixOf(String s) {
        int length = search(root, s, 0, 0);
        return s.substring(0, length);

    }

    public String longestPrefixWithRange(String s, int start, int end) {
        return searchWithRange(root, s, 0, null, start, end);
    }

    private String searchWithRange(Node x, String s, int d, String prifex, int start, int end) {
        if (x == null) return prifex;
        if (x.value != null) prifex = x.value;
        if (d == end - start) return prifex;
        char c = s.charAt(start + d);
        return searchWithRange(x.next.get(c), s, d + 1, prifex, start, end);
    }


    public int longestPrefixOfWithRange(String s, int start, int end) {
        if (end <= start) return 0;
        return searchWithRange(root, s, 0, 0, start, end);
    }

    private int searchWithRange(Node x, String s, int d, int length, int start, int end) {
        if (x == null) return length;
        if (x.value != null) length = d;
        if (d == end - start) return length;
        char c = s.charAt(start + d);
        return searchWithRange(x.next.get(c), s, d + 1, length, start, end);
    }

    private int search(Node x, String s, int d, int length) {
        if (x == null) return length;
        if (x.value != null) length = d;
        if (d == s.length()) return length;
        char c = s.charAt(d);
        return search(x.next.get(c), s, d + 1, length);
    }

}
