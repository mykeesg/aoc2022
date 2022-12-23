package days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.function.Function;

public class Day11 extends Base {

    static class Monkey {
        private final Deque<Long> items;
        private final Function<Long, Long> inspection;
        private final long testValue;
        private final Function<Long, Long> test;
        private long inspectedItems = 0;

        Monkey(Deque<Long> items, Function<Long, Long> inspection, long testValue, Function<Long, Long> test) {
            this.items = items;
            this.inspection = inspection;
            this.testValue = testValue;
            this.test = test;
        }

        public long inspect(long item) {
            inspectedItems += 1;
            return this.inspection.apply(item);
        }

        public long getInspectedItems() {
            return inspectedItems;
        }
    }

    private final List<Monkey> monkeys = new ArrayList<>();

    @Override
    void runFirst() throws Throwable {
        System.out.println("Monkey business level after 20 rounds: " + solve(20, true));
    }

    @Override
    void runSecond() throws Throwable {

        System.out.println("Monkey business level after 10000 rounds: " + solve(10000, false));
    }

    private long solve(int rounds, boolean decay) throws Throwable {
        monkeys.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day11.txt"))) {
            String line;
            while (reader.readLine() != null) {
                //skip monkey header
                line = reader.readLine();
                Deque<Long> items = new LinkedList<>(
                        Arrays.stream(line.substring(line.indexOf(": ") + 2).split(", "))
                                .map(Long::parseLong)
                                .toList());

                String[] data;
                line = reader.readLine();
                data = line.split(" ");
                Function<Long, Long> operation;
                {
                    String operand = data[data.length - 1];
                    if ("old".equals(operand)) {
                        operation = switch (data[data.length - 2]) {
                            case "+" -> (old) -> old + old;
                            case "*" -> (old) -> old * old;
                            default -> throw new IllegalArgumentException();
                        };
                    } else {
                        long value = Long.parseLong(operand);
                        operation = switch (data[data.length - 2]) {
                            case "+" -> (old) -> old + value;
                            case "*" -> (old) -> old * value;
                            default -> throw new IllegalArgumentException();
                        };
                    }
                }

                line = reader.readLine();
                data = line.split(" ");
                long testValue = Long.parseLong(data[data.length - 1]);

                line = reader.readLine();
                data = line.split(" ");
                long ifTrue = Long.parseLong(data[data.length - 1]);

                line = reader.readLine();
                data = line.split(" ");
                long ifFalse = Long.parseLong(data[data.length - 1]);

                monkeys.add(new Monkey(
                        items,
                        operation,
                        testValue,
                        (stress) -> (stress % testValue) == 0 ? ifTrue : ifFalse
                ));

                reader.readLine();
            }
        }

        long modulo = monkeys.stream().map(m -> m.testValue).mapToLong(v -> v).reduce(1, (res, val) -> res * val);

        for (int ii = 0; ii < rounds; ++ii) {
            monkeys.forEach(monkey -> {
                while (!monkey.items.isEmpty()) {
                    long item = monkey.items.removeFirst();
                    long stress = monkey.inspect(item);

                    if (decay) {
                        stress = stress / 3L;
                    }

                    while (stress >= modulo) {
                        stress = stress % modulo;
                    }

                    long to = monkey.test.apply(stress);
                    monkeys.get((int) to).items.addLast(stress);
                }
            });
        }

        monkeys.sort(Comparator.comparing(Monkey::getInspectedItems).reversed());

        Monkey firstMonkey = monkeys.get(0);
        Monkey secondMonkey = monkeys.get(1);

        return firstMonkey.inspectedItems * secondMonkey.inspectedItems;
    }
}
