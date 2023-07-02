package cn.larry.demo;

import java.util.*;

public class SubLocation {

    public List<Integer> subLocations(String str, String[] words) {
        if (str == null || str.isEmpty() || words == null || words.length == 0) {
            return null;
        }
        int wordNum = words.length;
        int wordLen = words[0].length();
        if (str.length() < wordLen * wordNum) {
            return null;
        }
        Map<Integer, Integer> indexWord = new HashMap<>();
        List<Integer> results = new ArrayList<>();

    }

    //str.substring(index,)
    public boolean isCombine(String str, int startIndex, Map<Integer, List<Integer>> indexWord, Map<String, List<Integer>> wordIndexes, String[] words) {

        int wordLen = words[0].length();
        int len = words.length * wordLen;
        if (str.length() < startIndex + len) {
            return false;
        }
        Set<Integer> containedWords = new HashSet<>();
        for (int i = 0; i < len; i++) {
            int index = startIndex + i;
            if (indexWord.get(index) != null) {
                List<Integer> integers = indexWord.get(index);
                if (addToSet(containedWords, integers)) {
                    i += wordLen - 1;
                } else {
                    return false;
                }
            } else {
                String wd = str.substring(startIndex, startIndex + wordLen);
                List<Integer> indexes = wordIndexes.get(wd);
                indexWord.put(index, indexes);
                if (indexes == null) {
                    return false;
                }
                if (addToSet(containedWords, indexes)) {
                    i += wordLen - 1;
                } else {
                    return false;
                }
            }
            if (containedWords.size() == words.length) {
                return true;
            }
        }
    }

    private boolean addToSet(Set<Integer> set, List<Integer> list) {
        for (Integer i : list) {
            if (set.add(i)) {
                return true;
            }
        }
        return false;
    }
}
