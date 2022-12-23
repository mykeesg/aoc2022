package days;

import util.Vector2D;
import util.Dijkstra;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Day12 extends Base {
    public Day12() {
        try {
            readInput();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void runFirst() throws Throwable {
        solve(end, (node -> node.equals(start)));
    }

    @Override
    void runSecond() throws Throwable {
        solve(end, (node -> at(node) == 'a'));
    }

    private void solve(Vector2D start, Predicate<Vector2D> goal) {
        List<Vector2D> nodes = new ArrayList<>();
        int rowIndex = 0;
        for (var row : map) {
            int colIndex = 0;
            for (var ignored : row) {
                nodes.add(new Vector2D(rowIndex, colIndex));
                ++colIndex;
            }
            ++rowIndex;
        }
        Dijkstra<Vector2D> dijkstra = new Dijkstra<>(
                nodes,
                start,
                goal,
                (a, b) -> 1L,
                this::validNeighbours
        );

        dijkstra.run();
        if (dijkstra.hasPath()) {
            System.out.println("Steps taken to reach goal: " + dijkstra.getPath().size());
        } else {
            System.out.println("Could not find path!");
        }
    }

    List<Vector2D> validNeighbours(Vector2D coordinate) {
        return Stream.of(
                        new Vector2D(coordinate.x() - 1, coordinate.y()),
                        new Vector2D(coordinate.x() + 1, coordinate.y()),
                        new Vector2D(coordinate.x(), coordinate.y() - 1),
                        new Vector2D(coordinate.x(), coordinate.y() + 1)
                ).filter(other -> validMove(coordinate, other))
                .toList();
    }

    private char at(Vector2D coordinate) {
        return map.get(coordinate.x()).get(coordinate.y());
    }

    boolean validMove(Vector2D from, Vector2D to) {
        //out of bounds
        if (to.x() < 0 || to.x() >= map.size()) return false;
        List<Character> row = map.get(to.x());
        if (to.y() < 0 || to.y() >= row.size()) return false;
        //negative is also good
        return at(from) - at(to) <= 1;
    }

    private Vector2D start, end;

    private final List<List<Character>> map = new ArrayList<>();

    void readInput() throws Throwable {
        map.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day12.txt"))) {
            String line;
            int rowIndex = 0;
            while ((line = reader.readLine()) != null) {
                List<Character> row = new ArrayList<>(line.length());
                int colIndex = 0;
                for (char ch : line.toCharArray()) {
                    if (ch == 'S') {
                        start = new Vector2D(rowIndex, colIndex);
                        row.add('a');
                    } else if (ch == 'E') {
                        end = new Vector2D(rowIndex, colIndex);
                        row.add('z');
                    } else {
                        row.add(ch);
                    }
                    ++colIndex;
                }
                map.add(row);
                ++rowIndex;
            }
        }
    }
}
