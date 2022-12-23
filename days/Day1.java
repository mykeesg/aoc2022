package days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Day1 extends Base {
    @Override
    public void runFirst() throws Throwable {
        System.out.printf("Max calories carried by a single elf: %1$s%n", solve(1));
    }

    @Override
    public void runSecond() throws Throwable {
        System.out.printf("Total calories carried by the top 3 elves: %1$s%n", solve(3));
    }

    private long solve(int totalElves) throws Throwable {
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day1.txt"))) {
            Set<Long> maxes = new HashSet<>();
            Consumer<Long> checkMax = (sum) -> {
                if (maxes.size() < totalElves) {
                    maxes.add(sum);
                    return;
                }

                // orElse will never run as the set is not empty.
                long min = maxes.stream().min(Long::compareTo).orElse(Long.MIN_VALUE);
                if (min < sum) {
                    maxes.remove(min);
                    maxes.add(sum);
                }
            };

            long sum = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    checkMax.accept(sum);
                    sum = 0;
                } else {
                    sum += Long.parseLong(line);
                }
            }
            // don't forget the last one
            checkMax.accept(sum);

            return maxes.stream().mapToLong(l -> l).sum();
        }
    }
}