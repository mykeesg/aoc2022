package days;

import util.Vector2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Day23 extends Base {

    private final Set<Vector2D> elves = new HashSet<>();
    private Vector2D bottomLeft = Vector2D.ORIGIN;
    private Vector2D topRight = Vector2D.ORIGIN;

    private int emptyCount = 0;
    private int rounds = 0;
    private int moveIndex = 0;

    record MovementProposal(Vector2D first, Vector2D second, Vector2D third, Vector2D then) {
    }

    private final List<MovementProposal> movements = List.of(
            new MovementProposal(Vector2D.NORTH_WEST, Vector2D.NORTH, Vector2D.NORTH_EAST, Vector2D.NORTH),
            new MovementProposal(Vector2D.SOUTH_WEST, Vector2D.SOUTH, Vector2D.SOUTH_EAST, Vector2D.SOUTH),
            new MovementProposal(Vector2D.NORTH_WEST, Vector2D.WEST, Vector2D.SOUTH_WEST, Vector2D.WEST),
            new MovementProposal(Vector2D.NORTH_EAST, Vector2D.EAST, Vector2D.SOUTH_EAST, Vector2D.EAST)
    );

    public Day23() {
        readInput();
        simulate();
    }

    private void readInput() {
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day23.txt"))) {
            final int SIZE = 73;
            String line;
            for (int y = 0; y < SIZE; ++y) {
                line = reader.readLine();
                for (int x = 0; x < SIZE; ++x) {
                    if (line.charAt(x) == '#') {
                        elves.add(new Vector2D(x, -y));
                    }
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

    }

    boolean empty(Vector2D position) {
        return !elves.contains(position);
    }

    Vector2D checkMove(MovementProposal proposal, Vector2D position) {
        if (empty(position.add(proposal.first())) && empty(position.add(proposal.second())) && empty(position.add(proposal.third()))) {
            return position.add(proposal.then());
        }
        return null;
    }

    Vector2D moveElf(Vector2D position) {
        int emptySpaces = 0;
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                Vector2D current = position.add(Vector2D.of(i, j));
                if (empty(current)) {
                    ++emptySpaces;
                }
            }
        }
        if (8 == emptySpaces) {
            // does not want to move at all
            return null;
        }

        for (int i = 0; i < 4; ++i) {
            int idx = (moveIndex + i) % 4;
            MovementProposal proposal = movements.get(idx);
            Vector2D moveTo = checkMove(proposal, position);
            if (moveTo != null) {
                return moveTo;
            }
        }
        //wants to move but can not
        return null;
    }

    void simulate() {
        rounds = 1;
        // key -> coordinate to move towards
        // value -> how many elves want to move here
        Map<Vector2D, Long> moveProposals = new HashMap<>();

        // key -> current elf position
        // value -> where it wants to move
        Map<Vector2D, Vector2D> requiredMoves = new HashMap<>();
        while (true) {
            moveProposals.clear();
            requiredMoves.clear();
            // 1st part of the movement
            elves.forEach(elf -> {
                Vector2D moveTo = moveElf(elf);
                if (moveTo != null) {
                    requiredMoves.put(elf, moveTo);
                    moveProposals.put(moveTo, moveProposals.getOrDefault(moveTo, 0L) + 1L);
                }
            });

            boolean elvesMoved = false;
            // 2nd part of the movement
            for (var elf : requiredMoves.entrySet()) {
                if (moveProposals.get(elf.getValue()) == 1L) {
                    elves.remove(elf.getKey());
                    elves.add(elf.getValue());
                    elvesMoved = true;
                }
            }

            if (!elvesMoved) {
                break;
            }

            if (rounds == 10) {
                calculateBoundingBox();
                emptyCount = 0;
                for (int ii = bottomLeft.y(); ii <= topRight.y(); ++ii) {
                    for (int jj = bottomLeft.x(); jj <= topRight.x(); ++jj) {
                        if (empty(Vector2D.of(jj, ii))) {
                            ++emptyCount;
                        }
                    }
                }
            }
            //rotate moves
            moveIndex = (moveIndex + 1) % 4;
            ++rounds;
        }
    }

    @Override
    void runFirst() throws Throwable {
        System.out.println("Empty spaces inside the area after round 10: " + emptyCount);
    }

    @Override
    void runSecond() throws Throwable {
        System.out.println("The first round where no elf moved: " + rounds);
    }

    private final static String ELF = "🤶";
    private final static String EMPTY = "⬛";

    void calculateBoundingBox() {
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        for (Vector2D elf : elves) {
            if (minX > elf.x()) minX = elf.x();
            if (maxX < elf.x()) maxX = elf.x();

            if (minY > elf.y()) minY = elf.y();
            if (maxY < elf.y()) maxY = elf.y();
        }

        bottomLeft = new Vector2D(minX, minY);
        topRight = new Vector2D(maxX, maxY);
    }

    void showElves() {
        for (int ii = bottomLeft.y(); ii <= topRight.y(); ++ii) {
            for (int jj = bottomLeft.x(); jj <= topRight.x(); ++jj) {
                Vector2D current = new Vector2D(jj, ii);
                if (elves.contains(current)) {
                    System.out.print(ELF);
                } else {
                    System.out.print(EMPTY);
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        System.out.println();
    }
}