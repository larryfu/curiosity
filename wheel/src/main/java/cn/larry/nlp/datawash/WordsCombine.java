package cn.larry.nlp.datawash;

/**
 * Created by larry on 7/17/2016.
 */
public class WordsCombine {

    private int front;
    private int end;

    public WordsCombine(int front, int end) {
        this.front = front;
        this.end = end;
    }

    public int getEnd() {
        return end;
    }

    public int getFront() {
        return front;
    }

    @Override
    public int hashCode() {
        return front * end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this.getClass() != o.getClass())
            return false;
        if (this.front == ((WordsCombine) o).front && this.end == ((WordsCombine) o).end)
            return true;
        return false;
    }

    public String toString() {
        return "(" + front + "," + end + ")";
    }
}
