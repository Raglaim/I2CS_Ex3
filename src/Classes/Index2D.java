package Classes;

import Classes.Interfaces.Pixel2D;

public class Index2D implements Pixel2D {

    private int X = 0;
    private int Y = 0;

    public Index2D(int w, int h) {
        X = w;
        Y = h;
    }
    public Index2D(Pixel2D other) {
        X = other.getX();
        Y = other.getY();
    }
    @Override
    public int getX() {
        return X;
    }

    @Override
    public int getY() {
        return Y;
    }

    @Override
    public double distance2D(Pixel2D p2) {
        return Math.sqrt(Math.pow(p2.getX() - this.X, 2) + Math.pow(p2.getY() - this.Y, 2));
    }

    @Override
    public String toString() {
        String ans = null;
        ans = "(" + X + "," + Y + ")";
        return ans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Index2D)) return false;
        Index2D other = (Index2D) o;
        return this.X == other.X && this.Y == other.Y;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(X);
        result = 31 * result + Integer.hashCode(Y);
        return result;

    }
}
