package visualizer.Util;

import java.util.Objects;

public class Pair<Type1, Type2> {
    private Type1 first;
    private Type2 second;
    private int hashCode;
    public Pair() {
        this.first = null;
        this.second = null;
    }

    public Pair(Type1 first, Type2 second) {
        this.first = first;
        this.second = second;
        this.hashCode = Objects.hash(first, second);
    }

    public void setFirst(Type1 first) {
        this.first = first;
        this.hashCode = Objects.hash(first, second);
    }

    public void setSecond(Type2 second) {
        this.second = second;
        this.hashCode = Objects.hash(first, second);
    }

    public Type1 getFirst() {
        return this.first;
    }

    public Type2 getSecond() {
        return this.second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Pair that = (Pair) o;
        return first == that.getFirst() && second == that.getSecond();
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public String toString() {
        return "First: " + this.first + " Second: " + this.second;
    }
}

