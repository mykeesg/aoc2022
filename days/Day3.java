package days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Day3 extends Base {
    @Override
    public void runFirst() throws Throwable {
        System.out.printf("Sum of priorities: %1$s%n", solve1());
    }

    @Override
    public void runSecond() throws Throwable {
        System.out.printf("Sum of badge priorities: %1$s%n", solve2());
    }

    int priority(char ch) {
        if (ch >= 'a' && ch <= 'z') {
            return ch - 'a' + 1;
        }
        if (ch >= 'A' && ch <= 'Z') {
            return ch - 'A' + 27;
        }
        return 0;
    }

    private long solve1() throws Throwable {
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day3.txt"))) {
            AtomicLong result = new AtomicLong();
            String line;
            while ((line = reader.readLine()) != null) {

                int half = line.length() / 2;
                String first = line.substring(0, half);
                String second = line.substring(half);

                Set<Character> firstLetters = first.chars().mapToObj(ch -> (char) ch).collect(Collectors.toSet());
                Set<Character> secondLetters = second.chars().mapToObj(ch -> (char) ch).collect(Collectors.toSet());
                firstLetters.retainAll(secondLetters);

                firstLetters.forEach(ch -> result.addAndGet(priority(ch)));

            }
            return result.get();
        }
    }

    private long solve2() throws Throwable {
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day3.txt"))) {
            AtomicLong result = new AtomicLong();
            String line;
            long i = 0;

            Set<Character> firstElf = new HashSet<>();
            Set<Character> secondElf = new HashSet<>();
            Set<Character> thirdElf = new HashSet<>();

            while ((line = reader.readLine()) != null) {
                Set<Character> current;
                if (i == 0) {
                    current = firstElf;
                } else if (i == 1) {
                    current = secondElf;
                } else {
                    current = thirdElf;
                }

                for (char ch : line.toCharArray()) {
                    current.add(ch);
                }

                if (i == 2) {
                    firstElf.retainAll(secondElf);
                    firstElf.retainAll(thirdElf);

                    firstElf.forEach(ch -> result.addAndGet(priority(ch)));

                    firstElf.clear();
                    secondElf.clear();
                    thirdElf.clear();
                }

                i = (i + 1) % 3;

            }
            return result.get();
        }
    }
}