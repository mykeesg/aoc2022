package days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Day21 extends Base {

    public Day21() {
        try {
            readInput();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    class Monkey {
        final String name;
        String operation;
        String left, right;

        Monkey(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Monkey #" + name;
        }

        void invalidate() {
            if (left != null && right != null) {
                // otherwise it's a constant there's no point in removing its value
                cache.remove(this);
                monkeys.get(left).invalidate();
                monkeys.get(right).invalidate();
            }
        }

        boolean calculateDependencies() {
            if (left != null && right != null) {
                if (left.equals(me.name)) {
                    dependencies.put(name, left);
//                    System.out.println("Monkey #" + name + " depends on ME (L) to answer.");
                    return true;
                }
                if (right.equals(me.name)) {
                    dependencies.put(name, right);
//                    System.out.println("Monkey #" + name + " depends on ME (R) to answer.");
                    return true;
                }
                if (monkeys.get(left).calculateDependencies()) {
                    dependencies.put(name, left);
//                    System.out.println("Monkey #" + name + " depends on " + left + "(L) to answer.");
                    return true;
                }
                if (monkeys.get(right).calculateDependencies()) {
                    dependencies.put(name, right);
//                    System.out.println("Monkey #" + name + " depends on " + right + "(R) to answer.");
                    return true;
                }
            }
            return false;
        }

        void reverse(long expected) {
            if (me == this) {
                System.out.println("I should yell " + expected);
                return;
            }
            if (left != null && right != null) {
                if (dependencies.containsKey(left)) {
                    long fixed = monkeys.get(right).get();
                    long expectedResult = switch (this.operation) {
                        case "+" -> expected - fixed;
                        case "-" -> expected + fixed;
                        case "*" -> expected / fixed;
                        case "/" -> expected * fixed;
                        default -> throw new Error();
                    };
//                    System.out.println("For #" + name + " to make " + expected + " = " + left + operation + fixed + " the LEFT side (" + left + ") should be: " + expectedResult);
                    monkeys.get(left).reverse(expectedResult);
                } else if (dependencies.containsKey(right)) {
                    long fixed = monkeys.get(left).get();
                    long expectedResult = switch (this.operation) {
                        case "+" -> expected - fixed;
                        case "-" -> fixed - expected;
                        case "*" -> expected / fixed;
                        case "/" -> fixed / expected;
                        default -> throw new Error();
                    };
//                    System.out.println("For #" + name + " to make " + expected + " = " + fixed + operation + right + " the RIGHT side (" + right + ") should be: " + expectedResult);
                    monkeys.get(right).reverse(expectedResult);
                } else {
//                    System.out.println("Monkey #" + name + " will always yell " + get());
                }
            } else {
                // System.out.println("Monkey #" + name + " will always yell " + get());
            }
        }

        long get() {
            if (cache.containsKey(this)) {
                return cache.get(this);
            }

            long leftValue = monkeys.get(left).get();
            long rightValue = monkeys.get(right).get();

            long result = switch (operation) {
                case "+" -> leftValue + rightValue;
                case "-" -> leftValue - rightValue;
                case "*" -> leftValue * rightValue;
                case "/" -> leftValue / rightValue;
                default -> throw new Error();
            };
            cache.put(this, result);
            return result;
        }
    }

    private final Map<Monkey, Long> cache = new HashMap<>();
    private final Map<String, Monkey> monkeys = new HashMap<>();
    private final Map<String, String> dependencies = new HashMap<>();

    private Monkey root;
    private Monkey me;

    private void readInput() throws Throwable {
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day21.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                String monkeyName = data[0].substring(0, data[0].length() - 1);

                Monkey monkey = new Monkey(monkeyName);
                monkeys.put(monkeyName, monkey);

                if (data.length == 2) {
                    cache.put(monkey, Long.parseLong(data[1]));
                } else {
                    monkey.left = data[1];
                    monkey.operation = data[2];
                    monkey.right = data[3];
                }
            }
            root = monkeys.get("root");
            me = monkeys.get("humn");
        }
    }

    @Override
    void runFirst() throws Throwable {
        System.out.println("root value: " + root.get());
    }

    @Override
    void runSecond() throws Throwable {
        Monkey leftMonkey = monkeys.get(root.left);
        Monkey rightMonkey = monkeys.get(root.right);
        root.invalidate();
        dependencies.clear();
        dependencies.put(me.name, me.name);
        root.calculateDependencies();

        if (dependencies.containsKey(leftMonkey.name)) {
            leftMonkey.reverse(rightMonkey.get());
        } else {
            rightMonkey.reverse(leftMonkey.get());
        }
    }
}
