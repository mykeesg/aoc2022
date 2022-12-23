package util;

import java.util.Comparator;

public record Vector2D(int x, int y) {
    public static Vector2D from(String str, String separator) {
        String[] data = str.split(separator);
        return new Vector2D(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
    }

    public static Vector2D of(int i, int j) {
        return new Vector2D(i, j);
    }

    public Vector2D add(Vector2D that) {
        return new Vector2D(
                this.x() + that.x(),
                this.y() + that.y()
        );
    }

    public static final Comparator<Vector2D> manhattanDistance = (first, second) -> Math.abs(first.x() - second.x()) + Math.abs(first.y() - second.y());

    public final static Vector2D NORTH_WEST = new Vector2D(-1, 1);
    public final static Vector2D NORTH = new Vector2D(0, 1);
    public final static Vector2D NORTH_EAST = new Vector2D(1, 1);
    public final static Vector2D WEST = new Vector2D(-1, 0);
    public final static Vector2D ORIGIN = new Vector2D(0, 0);
    public final static Vector2D EAST = new Vector2D(1, 0);
    public final static Vector2D SOUTH_WEST = new Vector2D(-1, -1);
    public final static Vector2D SOUTH = new Vector2D(0, -1);
    public final static Vector2D SOUTH_EAST = new Vector2D(1, -1);
}
