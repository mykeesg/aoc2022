package util;

public record Pair<U, V>(U first, V second) {
    public static <U, V> Pair<U, V> of(U first, V second) {
        return new Pair<>(first, second);
    }

    @Override
    public String toString() {
        return String.format("(%1$s;%2$s)", first(), second());
    }
}