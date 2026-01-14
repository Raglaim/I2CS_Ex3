package Classes;

import Classes.Interfaces.Pixel2D;

/**
 * A concrete implementation of a 2D Circle.
 * <p>
 * This class represents a circle defined by an integer center coordinate (x, y)
 * and a floating-point radius. It provides methods for geometric calculations
 * (area, perimeter) and spatial queries (contains).
 * </p>
 */
public class Circle implements Classes.Interfaces.Circle {

    private int X = 0;
    private int Y = 0;
    private double RAD = 0;

    /**
     * Constructs a Circle with a specific center (x, y) and radius.
     *
     * @param x   the x-coordinate of the center
     * @param y   the y-coordinate of the center
     * @param rad the radius of the circle
     */
    public Circle(int x, int y, double rad) {
        X = x;
        Y = y;
        RAD = rad;
    }

    /**
     * Constructs a Circle using a Classes.Interfaces.Pixel2D object as the center.
     *
     * @param center the center point of the circle
     * @param rad    the radius of the circle
     */
    public Circle(Pixel2D center, double rad) {
        X = center.getX();
        Y = center.getY();
        RAD = rad;
    }

    /**
     * Creates a deep copy of this circle.
     *
     * @return a new Circle object with the same center and radius as this one.
     */
    @Override
    public Circle copy() {
        return new Circle(this.X, this.Y, this.RAD);
    }

    /**
     * Gets the X coordinate of the center.
     * @return the x coordinate.
     */
    public int getCenterX() {
        return this.X;
    }

    private void setCenterX(int x) {
        this.X = x;
    }

    /**
     * Gets the Y coordinate of the center.
     * @return the y coordinate.
     */
    public int getCenterY() {
        return this.Y;
    }

    private void setCenterY(int y) {
        this.Y = y;
    }

    /**
     * Returns the center of the circle as a Classes.Interfaces.Pixel2D object.
     * <p>Note: This returns a new Classes.Index2D instance representing the center.</p>
     *
     * @return the center point.
     */
    public Pixel2D getCenter() {
        return new Index2D(this.getCenterX(), this.getCenterY());
    }

    private void setCenter(Pixel2D center) {
        this.X = center.getX();
        this.Y = center.getY();
    }

    /**
     * Gets the radius of the circle.
     * @return the radius.
     */
    public double getRadius() {
        return this.RAD;
    }

    private void setRadius(double rad) {
        this.RAD = rad;
    }

    /**
     * Computes the area of the circle.
     * <p>Formula: &pi; * r^2</p>
     *
     * @return the area.
     */
    @Override
    public double area() {
        return Math.pow(this.getRadius(), 2) * Math.PI;
    }

    /**
     * Computes the perimeter (circumference) of the circle.
     * <p>Formula: 2 * &pi; * r</p>
     *
     * @return the perimeter.
     */
    @Override
    public double perimeter() {
        return 2 * Math.PI * this.getRadius();
    }

    /**
     * Computes the diameter of the circle.
     *
     * @return the diameter (2 * radius).
     */
    public double diameter() {
        return 2 * this.getRadius();
    }

    /**
     * Checks if a point (x, y) is inside the circle.
     *
     * @param x the x-coordinate of the point.
     * @param y the y-coordinate of the point.
     * @return true if the distance from (x,y) to the center is <= radius.
     */
    @Override
    public boolean contains(int x, int y) {
        Pixel2D p = new Index2D(x, y);
        return this.contains(p);
    }

    /**
     * Checks if a Classes.Interfaces.Pixel2D point is inside the circle.
     *
     * @param p the point to check.
     * @return true if the distance from p to the center is <= radius.
     */
    @Override
    public boolean contains(Pixel2D p) {
        return this.getCenter().distance2D(p) < this.getRadius();
    }

    @Override
    public String toString() {
        return ("Center: " + this.getCenter().toString() + ", Radius: " + this.getRadius());
    }

    /**
     * Checks if this circle is equal to another object.
     *
     * @param other the object to compare against.
     * @return true if other is a Circle with the same center and radius.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Circle)) return false;

        Circle c = (Circle) other;
        return this.getCenterX() == c.getCenterX() && this.getCenterY() == c.getCenterY() && this.getRadius() == c.getRadius();
    }
}