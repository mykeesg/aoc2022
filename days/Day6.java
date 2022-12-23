package days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Day6 extends Base {
    @Override
    void runFirst() throws Throwable {
        System.out.printf("Characters to process to find first mark: %1$s%n", solve(4));
    }

    @Override
    void runSecond() throws Throwable {
        System.out.printf("Characters to process to find first message: %1$s%n", solve(14));
    }

    private long solve(int value) throws Throwable {
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day6.txt"))) {
            long result = 0;
            String line = reader.readLine();

            Deque<Character> chars = new LinkedList<>();
            for (char ch : line.toCharArray()) {
                chars.addLast(ch);
                ++result;
                if (chars.size() == value) {
                    Set<Character> letters = new HashSet<>(chars);
                    if (letters.size() == value) {
                        return result;
                    } else {
                        chars.removeFirst();
                    }
                }
            }
            return result;
        }
    }
}
