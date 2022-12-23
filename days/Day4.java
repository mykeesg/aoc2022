package days;

import java.io.BufferedReader;
import java.io.FileReader;

public class Day4 extends Base {

    record Interval(int start, int end) {

        boolean contains(Interval other) {
            return this.start <= other.start && other.end <= this.end;
        }

        boolean overlaps(Interval other) {
            return (this.start <= other.start && other.start <= this.end) ||
                    (this.start <= other.end && other.end <= this.end);

        }

        static Interval from(String str) {
            String[] data = str.split("-");
            return new Interval(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
        }
    }

    @Override
    public void runFirst() throws Throwable {
        System.out.printf("Total assignment pairs with full coverage: %1$s%n", solve1());
    }

    @Override
    public void runSecond() throws Throwable {
        System.out.printf("Total assignment pairs with overlaps: %1$s%n", solve2());
    }

    private long solve1() throws Throwable {
        long result = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day4.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Interval first = Interval.from(data[0]);
                Interval second = Interval.from(data[1]);

                if (first.contains(second) || second.contains(first)) {
                    result++;
                }
            }
        }
        return result;
    }

    private long solve2() throws Throwable {
        long result = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day4.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Interval first = Interval.from(data[0]);
                Interval second = Interval.from(data[1]);

                if (first.overlaps(second) || second.overlaps(first)) {
                    result++;
                }
            }
        }
        return result;
    }
}