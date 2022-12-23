package days;

import util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Day13 extends Base {
    private static class Packet {
        private final int value;
        private final List<Packet> subPackets;

        Packet(int value) {
            this(value, new ArrayList<>());
        }

        Packet(Packet packet) {
            this(-1, Collections.singletonList(packet));
        }

        Packet(int value, List<Packet> subPackets) {
            this.value = value;
            this.subPackets = subPackets;
        }

        int value() {
            return value;
        }

        boolean isNumber() {
            return value() != -1;
        }

        boolean isList() {
            return !isNumber();
        }
    }

    private final List<Pair<Packet, Packet>> packets = new ArrayList<>();
    private final static Packet firstDivider = new Packet(new Packet(2));
    private final static Packet secondDivider = new Packet(new Packet(6));

    public Day13() {
        try {
            readInput();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private void readInput() throws Throwable {
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day13.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Packet first = parseStr(line);
                line = reader.readLine();
                Packet second = parseStr(line);
                reader.readLine();

                packets.add(new Pair<>(first, second));
            }
        }
    }

    private Packet parseStr(String str) {
        Packet packet = new Packet(-1);
        int ii = 1;
        while (ii < str.length()) {
            int jj;
            //1st character of the passed string is always "["
            if (str.charAt(ii) == '[') {
                // new sub-packet list found
                int depth = 1;
                jj = ii + 1;
                while (depth > 0) {
                    if (str.charAt(jj) == ']') {
                        depth -= 1;
                    } else if (str.charAt(jj) == '[') {
                        depth += 1;
                    }
                    jj++;
                }
                //recursively parse whatever is between
                packet.subPackets.add(parseStr(str.substring(ii, jj)));
            } else {
                jj = str.indexOf(',', ii + 1);
                if (jj == -1) {
                    //find the end of this list
                    jj = str.indexOf(']', ii);
                }
                String value = str.substring(ii, jj);
                if (!value.isBlank()) {
                    Packet number = new Packet(Integer.parseInt(value));
                    packet.subPackets.add(number);
                }
                // else: it's an end of a packet list that does not need to be added,
                // as it's done above in the recursive call
            }
            ii = jj + 1;
        }
        return packet;
    }

    @Override
    void runFirst() throws Throwable {
        int sum = 0;
        for (int ii = 0; ii < packets.size(); ii++) {
            var pair = packets.get(ii);
            if (comparePackets(pair.first(), pair.second()) == 1) {
                sum += (ii + 1);
            }
        }
        System.out.println("Sum of packet-pair indices with proper ordering: " + sum);
    }

    private int comparePackets(Packet left, Packet right) {
        int ii = 0;
        while (ii < left.subPackets.size() || ii < right.subPackets.size()) {
            if (left.subPackets.size() <= ii) {
                //"If the left list runs out of items first, the inputs are in the right order"
                return 1;
            }
            if (right.subPackets.size() <= ii) {
                //"If the right list runs out of items first, the inputs are not in the right order"
                return -1;
            }

            Packet leftSubPackets = left.subPackets.get(ii);
            Packet rightSubPackets = right.subPackets.get(ii);

            // "If exactly one value is an integer, convert the integer to a list which contains that integer as its only value ... "
            if (leftSubPackets.isList() && rightSubPackets.isNumber()) {
                Packet wrapper = new Packet(new Packet(rightSubPackets.value()));
                // "... then retry the comparison."
                int compare = comparePackets(leftSubPackets, wrapper);
                if (compare != 0) {
                    return compare;
                } else {
                    //"If the lists are the same length and no comparison makes a decision about the order, continue checking the next part of the input."
                    ii++;
                    continue;
                }
            }
            if (leftSubPackets.isNumber() && rightSubPackets.isList()) {
                Packet wrapper = new Packet(new Packet(leftSubPackets.value()));
                // "... then retry the comparison."
                int compare = comparePackets(wrapper, rightSubPackets);
                if (compare != 0) {
                    return compare;
                } else {
                    //"If the lists are the same length and no comparison makes a decision about the order, continue checking the next part of the input."
                    ii++;
                    continue;
                }
            }

            if (leftSubPackets.isList() && rightSubPackets.isList()) {
                int compare = comparePackets(leftSubPackets, rightSubPackets);
                if (compare != 0) {
                    return compare;
                } else {
                    //"If the lists are the same length and no comparison makes a decision about the order, continue checking the next part of the input."
                    ii++;
                    continue;
                }
            }

            //"If both values are integers, the lower integer should come first."

            // If the left integer is lower than the right integer, the inputs are in the right order.
            if (leftSubPackets.value() < rightSubPackets.value()) {
                return 1;
            }
            // If the left integer is higher than the right integer, the inputs are not in the right order.
            if (rightSubPackets.value() < leftSubPackets.value()) {
                return -1;
            }
            // Otherwise, the inputs are the same integer; continue checking the next part of the input.
            ii++;
        }
        return 0;
    }

    @Override
    void runSecond() throws Throwable {
        List<Packet> ordered = Stream.concat(packets.stream().flatMap((pair) -> Stream.of(pair.first(), pair.second())),
                        Stream.of(firstDivider, secondDivider))
                .sorted((left, right) -> -1 * comparePackets(left, right))
                .toList();

        int result = 1;
        for (int ii = 0; ii < ordered.size(); ++ii) {
            if (ordered.get(ii) == firstDivider || ordered.get(ii) == secondDivider) {
                result *= (ii + 1);
            }
        }

        System.out.println("Product of divider packets after sorting: " + result);
    }
}
