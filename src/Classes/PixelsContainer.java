package Classes;

import Classes.Interfaces.Pixel2D;

/**
 * A container class for managing a dynamic collection of Classes.Interfaces.Pixel2D objects.
 * <p>
 * This implementation uses a standard array that resizes itself (grows or shrinks)
 * every time an element is added or removed. It provides FIFO (First-In-First-Out)
 * behavior via the {@link #enqueue} and {@link #dequeue} methods.
 * </p>
 */
public class PixelsContainer implements Classes.Interfaces.PixelsContainer {

    private Pixel2D[] LIST = {};

    /**
     * Constructs an empty PixelsContainer.
     */
    public PixelsContainer() {}

    /**
     * Constructs a PixelsContainer with a specific initial size.
     * <p>Note: This initializes the array with null elements. The container will
     * strictly have this length immediately.</p>
     *
     * @param size the initial size of the internal array
     */
    public PixelsContainer(int size) {
        this.LIST = new Pixel2D[size];
    }

    /**
     * Constructs a PixelsContainer initialized with a given array of pixels.
     *
     * @param list the array of Classes.Interfaces.Pixel2D objects to wrap
     */
    public PixelsContainer(Pixel2D[] list) {
        this.LIST = list;
    }

    /**
     * Returns the underlying array of pixels.
     *
     * @return the internal array containing the pixels
     */
    public Pixel2D[] getList() {
        return LIST;
    }

    /**
     * Returns the current number of elements in the container.
     *
     * @return the length of the internal array
     */
    public int getLength() {
        return this.LIST.length;
    }

    /**
     * Adds a new pixel to the end of the container.
     * <p>
     * This method creates a new array of size N+1, copies the existing elements,
     * and adds the new pixel at the last index.
     * </p>
     *
     * @param p the Classes.Interfaces.Pixel2D object to add
     */
    public void enqueue(Pixel2D p) {
        Pixel2D[] newList = new Pixel2D[this.LIST.length + 1];
        System.arraycopy(this.LIST, 0, newList, 0, this.LIST.length);
        newList[this.LIST.length] = p;
        this.LIST = newList;
    }

    /**
     * Removes and returns the first pixel in the container.
     * <p>
     * This method creates a deep copy of the pixel to return (using Classes.Index2D),
     * shrinks the internal array by 1, and shifts all remaining elements to the left.
     * </p>
     *
     * @return a copy of the pixel that was at the head of the container
     */
    public Pixel2D dequeue() {
        Pixel2D[] tempList = new Pixel2D[this.LIST.length];
        Pixel2D p = new Index2D(this.LIST[0]); // Creates a deep copy
        System.arraycopy(this.LIST, 0, tempList, 0, this.LIST.length);
        this.LIST = new Pixel2D[tempList.length -1];
        System.arraycopy(tempList, 1, this.LIST, 0, this.LIST.length);
        return p;
    }

    /**
     * Removes and returns the pixel at the specified index.
     * <p>
     * This method creates a deep copy of the target pixel, shrinks the internal array,
     * and shifts elements to fill the gap.
     * </p>
     *
     * @param i the index of the pixel to remove
     * @return a copy of the pixel that was removed
     */
    public Pixel2D dequeue(int i) {
        Pixel2D[] tempList = new Pixel2D[this.LIST.length];
        Pixel2D p = new Index2D(this.LIST[i]);
        System.arraycopy(this.LIST, 0, tempList, 0, this.LIST.length);
        this.LIST = new Pixel2D[tempList.length -1];
        System.arraycopy(tempList, 0, this.LIST, 0, i);
        System.arraycopy(tempList, i+1, this.LIST, i, tempList.length-i-1);
        return p;
    }

    /**
     * Checks if the container is empty.
     *
     * @return true if the internal array length is 0, false otherwise
     */
    public Boolean isEmpty() {
        return this.LIST.length == 0;
    }

    /**
     * Reverses the order of elements in the internal array in-place.
     */
    public void reverse() {
        for (int i = 0; i < this.LIST.length / 2; i+=1) {
            Pixel2D temp = this.LIST[i];
            this.LIST[i] = this.LIST[this.LIST.length - (i+1)];
            this.LIST[this.LIST.length - (i+1)] = temp;
        }
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < this.LIST.length; i+=1) {
            ans.append(this.LIST[i].toString());
            ans.append(",");
        }
        return ans.toString();
    }
}