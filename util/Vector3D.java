package util;

import java.util.Comparator;

public record Vector3D(int x, int y, int z) {
    public static Vector3D from(String str, String separator) {
        String[] data = str.split(separator);
        return new Vector3D(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]));
    }

    public Vector3D add(Vector3D that) {
        return new Vector3D(
                this.x() + that.x(),
                this.y() + that.y(),
                this.z() + that.z()
        );
    }

    public static final Comparator<Vector3D> manhattanDistance = (first, second) ->
            Math.abs(first.x() - second.x())
                    + Math.abs(first.y() - second.y())
                    + Math.abs(first.z() - second.z());

    // Right handed, OpenGL-like coordinate system
    public final static Vector3D UP = new Vector3D(0, 1, 0);
    public final static Vector3D DOWN = new Vector3D(0, -1, 0);
    public final static Vector3D LEFT = new Vector3D(-1, 0, 0);
    public final static Vector3D RIGHT = new Vector3D(1, 0, 0);
    public final static Vector3D FRONT = new Vector3D(0, 0, 1);
    public final static Vector3D REAR = new Vector3D(0, 0, -1);
}
