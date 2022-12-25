package days;

import util.Vector2D;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day24 extends Base {

    public Day24() {
        readInput();
        simulate();
    }

    private int totalMinutes;
    private int originalMinutes;
    private final List<Vector2D> hurricanePositions = new ArrayList<>();
    private final List<Vector2D> hurricaneDirections = new ArrayList<>();

    private Vector2D start;
    private Vector2D end;
    private static final Vector2D MAP_SIZE = Vector2D.of(122, 27);

    private void readInput() {
        try {
            List<String> data = Files.readAllLines(Path.of("inputs/day24.txt"));
            for (int y = 0; y < MAP_SIZE.y(); ++y) {
                String row = data.get(y);
                for (int x = 0; x < MAP_SIZE.x(); ++x) {
                    if (row.charAt(x) != '.' && row.charAt(x) != '#') {
                        Vector2D position = Vector2D.of(x, MAP_SIZE.y() - y - 1);
                        //origin is bottom left, not top left
                        Vector2D direction = switch (row.charAt(x)) {
                            case '<' -> Vector2D.WEST;
                            case '>' -> Vector2D.EAST;
                            case '^' -> Vector2D.NORTH;
                            case 'v' -> Vector2D.SOUTH;
                            default -> null;
                        };
                        if (direction != null) {
                            hurricanePositions.add(position);
                            hurricaneDirections.add(direction);
                        }
                    }
                }
            }
            start = Vector2D.of(1, MAP_SIZE.y() - 1);
            end = Vector2D.of(MAP_SIZE.x() - 2, 0);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private void simulate() {
        Set<Vector2D> path = new HashSet<>();
        int minutes = 0;

        path.add(start);
        while (!path.contains(end)) {
            updateHurricanes();
            findPossiblePaths(path, end);
            minutes += 1;
        }
        originalMinutes = minutes;

        path.clear();
        path.add(end);
        while (!path.contains(start)) {
            updateHurricanes();
            findPossiblePaths(path, start);
            minutes += 1;
        }

        path.clear();
        path.add(start);
        while (!path.contains(end)) {
            updateHurricanes();
            findPossiblePaths(path, end);
            minutes += 1;
        }
        this.totalMinutes = minutes;
    }

    private void updateHurricanes() {
        for (int ii = 0; ii < hurricanePositions.size(); ++ii) {
            Vector2D position = hurricanePositions.get(ii);
            Vector2D direction = hurricaneDirections.get(ii);

            Vector2D updatedPosition = position.add(direction);
            // this should be (and could be) done somehow prettier
            if (updatedPosition.x() == 0) {
                updatedPosition = Vector2D.of(MAP_SIZE.x() - 2, updatedPosition.y());
            } else if (updatedPosition.x() == MAP_SIZE.x() - 1) {
                updatedPosition = Vector2D.of(1, updatedPosition.y());
            } else if (updatedPosition.y() == 0) {
                updatedPosition = Vector2D.of(updatedPosition.x(), MAP_SIZE.y() - 2);
            } else if (updatedPosition.y() == MAP_SIZE.y() - 1) {
                updatedPosition = Vector2D.of(updatedPosition.x(), 1);
            }
            hurricanePositions.set(ii, updatedPosition);
        }
    }


    private void findPossiblePaths(Set<Vector2D> path, Vector2D goal) {
        Set<Vector2D> updatedPath = new HashSet<>();
        for (Vector2D tile : path) {
            if (!hurricanePositions.contains(tile)) {
                updatedPath.add(tile);
            }

            for (Vector2D direction : Vector2D.MAJOR_AXIS) {
                Vector2D nextTile = tile.add(direction);

                // it is out of bounds, which would be skipped given the _next_ condition, but it should be included.
                if (nextTile.equals(goal)) {
                    updatedPath.add(nextTile);
                }

                // can't step out of bounds
                if (nextTile.x() == 0 || nextTile.y() == 0 || nextTile.x() == MAP_SIZE.x() - 1 || nextTile.y() == MAP_SIZE.y() - 1) {
                    continue;
                }

                if (!hurricanePositions.contains(nextTile)) {
                    updatedPath.add(nextTile);
                }
            }
        }
        path.clear();
        path.addAll(updatedPath);
    }


    @Override
    void runFirst() throws Throwable {
        System.out.println("The exit can be reached in " + originalMinutes + " minutes.");
    }

    @Override
    void runSecond() throws Throwable {
        System.out.println("Escorting the elves, going back for grabbing the snack and reaching the exit again is done in " + totalMinutes + " minutes.");
    }

    private final static String HURRICANE = "🟦";
    private final static String EMPTY = "⬛";
    private final static String START = "🟨";
    private final static String END = "🎄";

    void displayMap() {
        for (int y = 0; y < MAP_SIZE.y(); ++y) {
            for (int x = 0; x < MAP_SIZE.x(); ++x) {
                Vector2D current = new Vector2D(x, MAP_SIZE.y() - y - 1);
                if (current.equals(start)) {
                    System.out.print(START);
                } else if (current.equals(end)) {
                    System.out.print(END);
                } else {
                    if (hurricanePositions.contains(current)) {
                        System.out.print(HURRICANE);
                    } else {
                        System.out.print(EMPTY);
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        System.out.println();
    }
}
