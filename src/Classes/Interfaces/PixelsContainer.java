package Classes.Interfaces;

/**
 * An interface representing a collection (container) of Classes.Interfaces.Pixel2D objects.
 * <p>
 * This container supports operations for adding, removing, and retrieving pixels,
 * behaving similarly to a list or a queue. Implementations should ensure efficient
 * management of the underlying storage (e.g., resizing arrays or managing linked nodes).
 * </p>
 *
 * @
 */
public interface PixelsContainer {

    /**
     * Returns an array containing all of the elements in this container in proper sequence.
     * <p>The returned array is safe to modify without affecting the internal
     * state of the container (if the implementation performs a shallow copy of the structure).</p>
     *
     * @return an array containing all pixels in the container
     */
    Pixel2D[] getList();

    /**
     * Returns the number of elements currently held in this container.
     *
     * @return the count of Classes.Interfaces.Pixel2D objects
     */
    int getLength();

    /**
     * Adds the specified Classes.Interfaces.Pixel2D to the end of this container.
     * <p>This operation is equivalent to "enqueueing" or "adding" an element
     * to the collection.</p>
     *
     * @param p the Classes.Interfaces.Pixel2D object to be added
     */
    void enqueue(Pixel2D p);

    /**
     * Removes and returns the first element (the head) of this container.
     * <p>This typically follows FIFO (First-In-First-Out) order.</p>
     *
     * @return the Classes.Interfaces.Pixel2D at the front of the container, or null if empty
     */
    Pixel2D dequeue();

    /**
     * Removes and returns the element at the specified index in this container.
     * <p>Subsequent elements are shifted to fill the gap.</p>
     *
     * @param i the index of the element to be removed
     * @return the Classes.Interfaces.Pixel2D that was removed
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    Pixel2D dequeue(int i);

    /**
     * Tests whether this container contains no elements.
     *
     * @return true if this container contains no pixels; false otherwise
     */
    Boolean isEmpty();

    /**
     * Reverses the order of the elements in this container.
     * <p>For example, if the container holds [A, B, C], after calling reverse(),
     * it will hold [C, B, A].</p>
     */
    void reverse();

    /**
     * Returns a string representation of the container.
     * <p>The string should typically list the contained pixels in order.</p>
     *
     * @return a string representing the state of the container
     */
    String toString();
}