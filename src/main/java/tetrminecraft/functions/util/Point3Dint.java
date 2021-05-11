package tetrminecraft.functions.util;

public class Point3Dint {

    private int x;
    private int y;
    private int z;

    public Point3Dint(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void addX(int x) {
        this.x += x;
    }

    public void addY(int y) {
        this.y += y;
    }

    public void addZ(int z) {
        this.z += z;
    }

    public int getAbsoluteX() {
        return Math.abs(x);
    }

    public int getAbsoluteY() {
        return Math.abs(y);
    }

    public int getAbsoluteZ() {
        return Math.abs(z);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public boolean isNonZeroX() {
        return x != 0;
    }

    public boolean isNonZeroY() {
        return y != 0;
    }

    public boolean isNonZeroZ() {
        return z != 0;
    }

}
