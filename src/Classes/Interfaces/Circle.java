package Classes.Interfaces;

/**
 * An interface representing a 2D Circle in a Cartesian coordinate system.
 * <p>
 * A Circle is defined by a center point (Classes.Interfaces.Pixel2D) and a radius (double).
 * </p>
 */
public interface Circle {

    /**
     * Creates and returns an independent deep copy of this Circle.
     * <p>
     * The new Circle instance must have the same center coordinates and radius
     * as this instance, but must be a separate object in memory.
     * </p>
     *
     * @return a new Circle instance that is an independent copy of this one.
     */
    Circle copy();

    /**
     * Computes the area of this circle.
     * <p>Formula: Area = &pi; * r^2</p>
     *
     * @return the area of the circle.
     */
    double area();

    /**
     * Computes the perimeter (circumference) of this circle.
     * <p>Formula: Perimeter = 2 * &pi; * r</p>
     *
     * @return the perimeter (circumference) of the circle.
     */
    double perimeter();

    /**
     * Tests whether the integer-coordinate point (x, y) lies inside this circle.
     * <p>
     * A point is considered inside if the distance from the point to the center
     * is less than or equal to the radius.
     * </p>
     *
     * @param x the x-coordinate of the point.
     * @param y the y-coordinate of the point.
     * @return true if the point is inside or on the boundary; false otherwise.
     */
    boolean contains(int x, int y);

    /**
     * Tests whether the given Classes.Interfaces.Pixel2D is inside this circle.
     *
     * @param p the point to test.
     * @return true if the point is inside or on the boundary, otherwise false.
     * @throws NullPointerException if p is null.
     */
    boolean contains(Pixel2D p);

    /**
     * Returns a human-readable description of this circle.
     * <p>Example format: "Circle: Center=(10,10), Radius=5.0"</p>
     *
     * @return a string representation of the circle.
     */
    String toString();

    /**
     * Tests equality between this circle and another object.
     * <p>
     * Two circles are considered equal if they have the same center
     * coordinates and the same radius.
     * </p>
     *
     * @param other another object to compare.
     * @return true if other is a Circle with the same center and radius; false otherwise.
     */
    boolean equals(Object other);

    // Optional: You might want to force implementations to provide these accessors
    // Classes.Interfaces.Pixel2D getCenter();
    // double getRadius();
}