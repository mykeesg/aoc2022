package days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Day5 extends Base {


    @Override
    void runFirst() throws Throwable {
        System.out.printf("Items ending up on the top: %1$s%n", solve1(true));
    }

    @Override
    void runSecond() throws Throwable {
        System.out.printf("Items ending up on the top: %1$s%n", solve1(false));
    }

    /*
            [L] [M]         [M]
        [D] [R] [Z]         [C] [L]
        [C] [S] [T] [G]     [V] [M]
[R]     [L] [Q] [B] [B]     [D] [F]
[H] [B] [G] [D] [Q] [Z]     [T] [J]
[M] [J] [H] [M] [P] [S] [V] [L] [N]
[P] [C] [N] [T] [S] [F] [R] [G] [Q]
[Z] [P] [S] [F] [F] [T] [N] [P] [W]
 1   2   3   4   5   6   7   8   9
     */
    void initStacks() {
        stacks.clear();
        stacks.add(new LinkedList<>(List.of("Z", "P", "M", "H", "R")));
        stacks.add(new LinkedList<>(List.of("P", "C", "J", "B")));
        stacks.add(new LinkedList<>(List.of("S", "N", "H", "G", "L", "C", "D")));
        stacks.add(new LinkedList<>(List.of("F", "T", "M", "D", "Q", "S", "R", "L")));
        stacks.add(new LinkedList<>(List.of("F", "S", "P", "Q", "B", "T", "Z", "M")));
        stacks.add(new LinkedList<>(List.of("T", "F", "S", "Z", "B", "G")));
        stacks.add(new LinkedList<>(List.of("N", "R", "V")));
        stacks.add(new LinkedList<>(List.of("P", "G", "L", "T", "D", "V", "C", "M")));
        stacks.add(new LinkedList<>(List.of("W", "Q", "N", "J", "F", "M", "L")));
    }

    private final List<Deque<String>> stacks = new ArrayList<>(9);

    private String solve1(boolean reverseOrder) throws Throwable {
        initStacks();
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day5.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                int amount = Integer.parseInt(data[1]);
                int from = Integer.parseInt(data[3]) - 1;
                int to = Integer.parseInt(data[5]) - 1;

                if (reverseOrder) {
                    for (int ii = 0; ii < amount; ++ii) {
                        stacks.get(to).addLast(stacks.get(from).removeLast());
                    }
                } else {
                    Deque<String> tmp = new LinkedList<>();
                    for (int ii = 0; ii < amount; ++ii) {
                        tmp.addLast(stacks.get(from).removeLast());
                    }
                    for (int ii = 0; ii < amount; ++ii) {
                        stacks.get(to).addLast(tmp.removeLast());
                    }
                }
            }
        }
        for (Deque<String> stack : stacks) {
            if (!stack.isEmpty()) {
                result.append(stack.getLast());
            }
        }
        return result.toString();
    }
}
