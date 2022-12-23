package days;

import util.Vector2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Day14 extends Base {

    public Day14() {
        try {
            readInput();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    //key -> coordinate
    //value -> what's there
    private final Map<Vector2D, String> cave = new HashMap<>();
    private Vector2D topLeft;
    private Vector2D bottomRight;
    private final static String CAVE_EMPTY = "⬛";
    private final static String CAVE_SAND = "🟨";
    private final static String CAVE_ROCK = "⬜";

    private final static Vector2D SPAWN = new Vector2D(500, 0);
    private int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

    private void readInput() throws Throwable {
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day14.txt"))) {
//        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/example.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] coords = line.split(" -> ");
                Vector2D prev = Vector2D.from(coords[0], ",");
                checkBoundingBox(prev);
                cave.put(prev, CAVE_ROCK);
                for (int ii = 1; ii < coords.length; ii++) {
                    Vector2D current = Vector2D.from(coords[ii], ",");
                    checkBoundingBox(current);
                    cave.put(current, CAVE_ROCK);
                    if (prev.x() == current.x()) {
                        int diff = Integer.signum(current.y() - prev.y());
                        for (int jj = prev.y() + diff; jj != current.y(); jj += diff) {
                            Vector2D middle = new Vector2D(prev.x(), jj);
                            cave.put(middle, CAVE_ROCK);
                        }
                    } else {
                        int diff = Integer.signum(current.x() - prev.x());
                        for (int jj = prev.x() + diff; jj != current.x(); jj += diff) {
                            Vector2D middle = new Vector2D(jj, prev.y());
                            cave.put(middle, CAVE_ROCK);
                        }
                    }
                    prev = current;
                }
            }

            topLeft = new Vector2D(minX, 0);
            bottomRight = new Vector2D(maxX, maxY);
        }
    }

    private void checkBoundingBox(Vector2D vector2D) {
        if (minX > vector2D.x()) minX = vector2D.x();
        if (maxX < vector2D.x()) maxX = vector2D.x();
        if (maxY < vector2D.y()) maxY = vector2D.y();
    }

    private Vector2D nextPossible(Vector2D vector2D) {

        // The Y axis is DOWNWARDS in the exercise -> going down is actually done by adding 'UP'
        Vector2D below = vector2D.add(Vector2D.NORTH);
        if (!cave.containsKey(below)) {
            return below;
        }

        Vector2D leftDiagonal = below.add(Vector2D.WEST);
        if (!cave.containsKey(leftDiagonal)) {
            return leftDiagonal;
        }

        Vector2D rightDiagonal = below.add(Vector2D.EAST);
        if (!cave.containsKey(rightDiagonal)) {
            return rightDiagonal;
        }

        return null;
    }

    @SuppressWarnings("unused")
    void printCave() {
        for (int ii = 0; ii <= bottomRight.y(); ++ii) {
            for (int jj = topLeft.x(); jj <= bottomRight.x(); ++jj) {
                Vector2D current = new Vector2D(jj, ii);
                System.out.print(cave.getOrDefault(current, CAVE_EMPTY));
            }
            System.out.println();
        }
    }

    @Override
    void runFirst() throws Throwable {
        int count = 0;
        while (true) {
            Vector2D currentPosition = SPAWN;

            Vector2D nextPosition = currentPosition;
            while (nextPosition != null && insideCave(currentPosition)) {
                currentPosition = nextPosition;
                nextPosition = nextPossible(nextPosition);
            }

            if (!insideCave(currentPosition)) {
                break;
            }
            ++count;
            cave.put(currentPosition, CAVE_SAND);
        }
        System.err.println("Total sand spawned: " + count);
        // printCave();
    }

    private boolean insideCave(Vector2D currentPosition) {
        return currentPosition.x() >= topLeft.x() && currentPosition.x() <= bottomRight.x() && currentPosition.y() <= bottomRight.y();
    }

    @Override
    void runSecond() throws Throwable {
        int count = 0;
        cave.entrySet().removeIf(entry -> entry.getValue().equals(CAVE_SAND));
        //two units lower
        int FLOOR = bottomRight.y() + 2;
        cave.entrySet().removeIf(entry -> entry.getValue().equals(CAVE_SAND));
        while (true) {
            Vector2D currentPosition = SPAWN;

            Vector2D nextPosition = currentPosition;
            while (nextPosition != null && nextPosition.y() < FLOOR) {
                currentPosition = nextPosition;
                nextPosition = nextPossible(nextPosition);
            }

            ++count;
            cave.put(currentPosition, CAVE_SAND);

            if (SPAWN.equals(currentPosition)) {
                break;
            }
        }
        System.err.println("Total sand spawned: " + count);
    }
}
