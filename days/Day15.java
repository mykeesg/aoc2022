package days;

import util.Vector2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Day15 extends Base {

    public Day15() {
        try {
            readInput();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private final Map<Vector2D, Vector2D> items = new HashMap<>();

    private void readInput() throws Throwable {
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day15.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] coords = line.split(" ");
                int x = Integer.parseInt(coords[2].substring(2, coords[2].length() - 1));
                int y = Integer.parseInt(coords[3].substring(2, coords[3].length() - 1));

                Vector2D sensor = new Vector2D(x, y);

                x = Integer.parseInt(coords[8].substring(2, coords[8].length() - 1));
                y = Integer.parseInt(coords[9].substring(2));

                Vector2D beacon = new Vector2D(x, y);

                items.put(sensor, beacon);

            }
        }
    }

    @Override
    void runFirst() throws Throwable {
        int row = 2_000_000;
        List<Vector2D> overlappedXIntervals = getIntervals(row);
        int count = findCoordinate(overlappedXIntervals, row);
        System.err.println("Positions taken in row " + row + ": " + count);
    }

    private int findCoordinate(List<Vector2D> intervals, int row) {
        int result = 0;

        Set<Integer> beaconsColumnsInRow = items.values().stream()
                .filter(beacon -> beacon.y() == row)
                .map(Vector2D::x)
                .collect(Collectors.toSet());

        int minColumn = intervals.stream().map(Vector2D::x).min(Integer::compareTo).orElse(Integer.MAX_VALUE);
        int maxColumn = intervals.stream().map(Vector2D::y).max(Integer::compareTo).orElse(Integer.MIN_VALUE);

        for (int column = minColumn; column <= maxColumn; ++column) {
            if (!beaconsColumnsInRow.contains(column)) {
                for (Vector2D interval : intervals) {
                    if (interval.x() <= column && column <= interval.y()) {
                        result += 1;
                        break; //position already taken, no need to check the rest
                    }
                }
            }
        }
        return result;
    }

    private List<Vector2D> getIntervals(int row) {
        List<Vector2D> intervals = new ArrayList<>();
        for (var entry : items.entrySet()) {
            Vector2D sensor = entry.getKey();
            Vector2D beacon = entry.getValue();

            int distance = Vector2D.manhattanDistance.compare(sensor, beacon);

            //we need to check if it overlaps row 2_000_000 ->
            // dist > |row - y()|
            //  ===
            // dist - |row - y()| > 0
            int diff = distance - Math.abs(row - sensor.y());
            if (diff > 0) {
                // filter the coords with the "overlapped range" -> there cannot be any beacons here

                //it is not a Coordinate, but the start and end of an interval on the given row
                intervals.add(new Vector2D(sensor.x() - diff, sensor.x() + diff));
            }
        }
        return intervals;
    }

    @Override
    void runSecond() throws Throwable {

        List<Integer> positiveLines = new ArrayList<>();
        List<Integer> negativeLines = new ArrayList<>();

        int pos = 0, neg = 0;

        for (var entry : items.entrySet()) {
            Vector2D sensor = entry.getKey();
            Vector2D beacon = entry.getValue();

            int distance = Vector2D.manhattanDistance.compare(sensor, beacon);

            // calculate the 4 lines representing the sides of the current bounding box separated by their slope
            negativeLines.add(sensor.x() + sensor.y() - distance);
            negativeLines.add(sensor.x() + sensor.y() + distance);

            positiveLines.add(sensor.x() - sensor.y() - distance);
            positiveLines.add(sensor.x() - sensor.y() + distance);
        }

        for (int ii = 0; ii < positiveLines.size(); ++ii) {
            for (int jj = ii + 1; jj < negativeLines.size(); ++jj) {

                int firstLine = positiveLines.get(ii);
                int secondLine = positiveLines.get(jj);

                // there's a gap of 2 cells between these lines, therefore there is space
                // for a single grid to be taken as the position
                if (Math.abs(firstLine - secondLine) == 2) {
                    pos = Math.min(firstLine, secondLine) + 1;
                }

                firstLine = negativeLines.get(ii);
                secondLine = negativeLines.get(jj);

                // same goes here
                if (Math.abs(firstLine - secondLine) == 2) {
                    neg = Math.min(firstLine, secondLine) + 1;
                }

            }
        }

        int x = (neg + pos) / 2;
        int y = (neg - pos) / 2;
        long score = (x * 4_000_000L + y);
        System.err.println("Possible score of beacon location: " + score);
    }
}
