package cn.larry.analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fugz on 2016/5/24.
 */
public class Analyzer {

    public static void main(String[] args) {
        List<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(0, 2));
        intervals.add(new Interval(1, 4));
        intervals.add(new Interval(2, 3));
        intervals.add(new Interval(3, 4));
        intervals.add(new Interval(4, 6));
        intervals.add(new Interval(5, 6));
        List<List<Interval>> results = getCombination(intervals);
        for (List<Interval> intervals1 : results) {
            System.out.println(intervals1);
        }

    }

    public static List<List<Interval>> getCombination(List<Interval> intervals) {
        Collections.sort(intervals, (i1, i2) -> i1.getLower() - i2.getLower());
        Interval interval = getStart(intervals);
        List<List<Interval>> results = new ArrayList<>();
        List<Interval> start = getSameLevel(intervals, interval);
        for (Interval in : start) {
            List<Interval> ins = new ArrayList<>();
            ins.add(in);
            generateCombination(results, ins, intervals);
        }
        return results;
    }

    public static Interval getStart(List<Interval> intervals) {
        Interval shortest = intervals.get(0);
        for (Interval interval : intervals) {
            if (interval.getLower() == shortest.getLower() && interval.length() < shortest.length())
                shortest = interval;
        }
        return shortest;
    }


    private static void generateCombination(List<List<Interval>> total, List<Interval> current, List<Interval> origin) {
        Interval in1 = getNext(origin, current.get(current.size() - 1));
        if (in1 == null) {
            total.add(current);
            return;
        }
        List<Interval> sameLevel = getSameLevel(origin, in1);
        for (Interval in : sameLevel) {
            List<Interval> ins = new ArrayList<>(current);
            ins.add(in);
            generateCombination(total, ins, origin);
        }
    }

    private static List<Interval> getSameLevel(List<Interval> intervals, Interval interval) {
        return intervals.stream().filter(in -> in.getLower() >= interval.getLower() && interval.intersection(in) != null).collect(Collectors.toList());
    }

    private static Interval getNext(List<Interval> sorted, Interval base) {
        Interval in = null;
        for (Interval interval : sorted)
            if (interval.getLower() > base.getUpper()) {
                if (in == null || interval.getLower() < in.getLower() || interval.getLower() == in.getLower() && interval.length() < in.length())
                    in = interval;
            }
        return in;
    }
}
