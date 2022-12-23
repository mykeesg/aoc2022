package days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Day9 extends Base {

    static class Knot {
        private int x, y;

        public Knot() {
            this(0, 0);
        }

        public Knot(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void move(Knot other) {
            move(other.x, other.y);
        }

        public void move(int dx, int dy) {
            this.x += dx;
            this.y += dy;
        }

        @Override
        public String toString() {
            return String.format("(%1$s;%2$s)", x, y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Knot that = (Knot) o;
            return x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            int low = (y & Short.MAX_VALUE);
            int high = (x & Short.MAX_VALUE) << 16;
            return low | high;
        }

        public Knot copy() {
            return new Knot(this.x, this.y);
        }
    }

    final static Knot UP = new Knot(0, 1);
    final static Knot DOWN = new Knot(0, -1);
    final static Knot LEFT = new Knot(-1, 0);
    final static Knot RIGHT = new Knot(1, 0);

    @Override
    void runFirst() throws Throwable {
        System.out.println("Coordinates visited by the tail at least once with 1 knot: " + solve(1));
    }

    @Override
    void runSecond() throws Throwable {
        System.out.println("Coordinates visited by the tail at least once with 9 knots: " + solve(9));
    }

    private int solve(int numOfKnots) throws Throwable {
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day9.txt"))) {
            Set<Knot> coords = new HashSet<>();
            List<Knot> knots = new ArrayList<>(numOfKnots + 1);
            for (int ii = 0; ii < numOfKnots + 1; ++ii) {
                knots.add(new Knot());
            }

            Knot head = knots.get(0);
            Knot tail = knots.get(knots.size() - 1);


            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");

                String dir = data[0];
                int steps = Integer.parseInt(data[1]);

                for (int ii = 0; ii < steps; ii++) {
                    switch (dir) {
                        case "U" -> head.move(UP);
                        case "D" -> head.move(DOWN);
                        case "L" -> head.move(LEFT);
                        case "R" -> head.move(RIGHT);
                    }
                    for (int jj = 1; jj < knots.size(); ++jj) {
                        Knot parent = knots.get(jj - 1);
                        Knot child = knots.get(jj);

                        int dX = parent.x - child.x;
                        int dY = parent.y - child.y;

                        if (Math.abs(dX) == 2 || Math.abs(dY) == 2) {
                            child.move(Integer.signum(dX), Integer.signum(dY));
                        }
                    }
                    coords.add(tail.copy());
                }
            }
            return coords.size();
        }
    }
}
