package days;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day25 extends Base {

    public Day25() {
        readInput();
    }

    private List<String> input;

    private int valueOf(char ch) {
        return switch (ch) {
            case '=' -> -2;
            case '-' -> -1;
            case '0' -> 0;
            case '1' -> 1;
            case '2' -> 2;
            default -> throw new IllegalArgumentException("Invalid digit: " + ch);
        };
    }

    private char valueOf(long digit) {
        return switch ((int) digit) {
            case 0 -> '0';
            case 1 -> '1';
            case 2 -> '2';
            case 3 -> '=';
            case 4 -> '-';
            default -> throw new IllegalArgumentException("Invalid digit: " + digit);
        };
    }

    private void readInput() {
        try {
            input = Files.readAllLines(Path.of("inputs/day25.txt"));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private String convert(long value) {
        StringBuilder result = new StringBuilder();

        while (value > 0) {
            result.insert(0, valueOf(value % 5));
            value -= ((value + 2) % 5) - 2;
            value /= 5;
        }

        return result.toString();
    }

    @Override
    void runFirst() throws Throwable {
        long result = 0;
        for (String line : input) {
            int pow = 0;
            for (char digit : new StringBuilder(line).reverse().toString().toCharArray()) {
                result += Math.pow(5, pow) * valueOf(digit);
                ++pow;
            }
        }
        System.out.println("Sum in base 10: " + result);
        String snafu = convert(result);
        System.out.println("Sum converted back to SNAFU: " + snafu);
    }

    @Override
    void runSecond() throws Throwable {

    }
}
