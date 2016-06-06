package cn.larry.analyzer;

/**
 * Created by fugz on 2016/5/24.
 */
public class Interval {

    private int lower;
    private int upper;

    public int length() {
        return upper - lower;
    }

    public Interval(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("下界不能大于上界");
        this.lower = lower;
        this.upper = upper;
    }

    public Interval intersection(Interval interval) {
        if (this.upper < interval.lower || this.lower > interval.upper)
            return null;
        return new Interval(Math.max(this.lower, interval.lower), Math.min(this.upper, interval.upper));
    }

    public Interval union(Interval interval) {
        return new Interval(Math.min(this.lower, interval.lower), Math.max(this.upper, interval.upper));
    }

    public int getUpper() {
        return upper;
    }

    public int getLower() {
        return lower;
    }

    @Override
    public String toString() {
        return "(" + lower + "," + upper + ")";
    }
}
