package Classes;

import Classes.Interfaces.Map2D;
import Classes.Interfaces.Pixel2D;

import javax.imageio.stream.ImageInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * This class represents a 2D map (int[w][h]) as a "screen" or a raster matrix or maze over integers.
 * This is the main class needed to be implemented.
 *
 * @author boaz.benmoshe
 *
 */
public class MyMap implements Map2D, Serializable{

    private static final int DEFAULT_W = 10;
    private static final int DEFAULT_H = 10;
    private static final int DEFAULT_V = 0;

    /**
     * Default constructor. Creates a map with default dimensions (10x10) and a default value of 0.
     */
    public MyMap() {
        init(DEFAULT_W, DEFAULT_H, DEFAULT_V);
    }

    private int W = 0;
    private int H = 0;
    private int[][] MAP = new int[W][H];
    private Boolean CYCLIC = false;

    /**
     * Creates a new map with specific width (w), height (h), and fills every pixel with the initial value (v).
     * @param w width
     * @param h height
     * @param v initial value
     */
    public MyMap(int w, int h, int v, boolean c) {init(w, h, v);}

    /**
     * Creates a square map where both width and height equal size.
     * @param size width and height
     */
    public MyMap(int size, boolean c) {this(size,size, 0, c);}

    /**
     * Constructs a map from a given 2D array. If the input array is null, it creates a default map instead.
     * @param data source 2D array
     */
    public MyMap(int[][] data, boolean c) {
        if (data == null) {
            init(DEFAULT_W, DEFAULT_H, DEFAULT_V);
            return;
        }
        init(data);
        this.setCyclic(c);
    }

    /**
     * Re-initializes the current map. It updates the width and height, creates a new internal 2D array, and sets every cell to the value v.
     */
    @Override
    public void init(int w, int h, int v) {
        int [][] ans;
        W = w;
        H = h;
        ans = new int[h][w];
        for (int y = 0; y < h; y+=1) {
            for (int x = 0; x < w; x+=1) {
                ans[y][x] = v;
            }
        }
        copy(ans, w, h);
    }

    /**
     * Re-initializes the map using a provided 2D array. It checks for errors (null or "ragged" arrays), calculates new dimensions,
     * and creates a "deep copy" of the data so changes to the original array won't affect the map later.
     */
    @Override
    public void init(int[][] arr) {
        int [][] ans;
        if (arr == null || arr.length == 0) {
            throw new RuntimeException("Null or empty array");
        }
        for (int[] row : arr) {
            if (row.length != arr[0].length) {
                throw new RuntimeException("Ragged array");
            }
        }
        int w = arr[0].length ;
        W = arr[0].length ;
        int h = arr.length;
        H = arr.length;
        ans = new int[h][w];
        for (int i = 0; i < h; i+=1) {
            System.arraycopy(arr[i], 0, ans[i], 0, w);
        }
        copy(ans, w, h);
    }

    /**
     * Returns a reference to the raw underlying 2D integer array (MAP).
     */
    @Override
    public int[][] getMap() {return this.MAP;}

    /**
     * Returns the width (number of columns) of the map.
     */
    @Override
    public int getWidth() {return this.W;}

    /**
     * Returns the height (number of rows) of the map.
     */
    @Override
    public int getHeight() {return this.H;}

    /**
     * Returns the integer color/value of the specific pixel at coordinates (x, y).
     */
    @Override
    public int getPixel(int x, int y) {return this.MAP[y][x];}

    /**
     * A wrapper for getPixel(x, y); gets the value using a Classes.Interfaces.Pixel2D object instead of separate x/y integers.
     */
    @Override
    public int getPixel(Pixel2D p) {return this.getPixel(p.getX(), p.getY());}

    /**
     * Sets the value (color) of the pixel at coordinates (x, y) to v.
     */
    @Override
    public void setPixel(int x, int y, int v) {this.MAP[y][x] = v;}

    /**
     * A wrapper for setPixel(x, y, v); sets the pixel value using a Classes.Interfaces.Pixel2D object.
     */
    @Override
    public void setPixel(Pixel2D p, int v) {this.MAP[p.getY()][p.getX()] = v;}

    /**
     * Checks if a given point p is within the valid boundaries of the map (i.e., x is between 0 and width, y is between 0 and height).
     * Returns true if valid, false otherwise.
     */
    @Override
    public boolean isInside(Pixel2D p) {return p.getX() >= 0 && p.getX() < this.W && p.getY() >= 0 && p.getY() < this.H;}

    @Override
    public boolean isCyclic() {return CYCLIC;}

    @Override
    public void setCyclic(boolean cy) {CYCLIC = cy;}

    /**
     * Checks if the current map has the exact same width and height as another map p.
     */
    public boolean sameDimensions(Map2D p) {return this.W == p.getWidth() && this.H == p.getHeight();}

    /**
     * Adds the values of another map p to the current map, pixel by pixel. This only happens if both maps have the same dimensions.
     */
    public void addMap2D(Map2D p) {
        if (this.sameDimensions(p)){
            for (int y = 0; y < this.H; y+=1) {
                for (int x = 0; x < this.W; x+=1) {
                    this.MAP[y][x] += p.getPixel(x, y);
                }
            }
        }
    }

    /**
     * Multiplies every pixel's value in the map by a given number (scalar).
     */
    public void mul(double scalar) {
        for (int y = 0; y < this.H; y+=1) {
            for (int x = 0; x < this.W; x+=1) {
                this.MAP[y][x] = (int)(this.MAP[y][x] * scalar);
            }
        }
    }

    /**
     * Resizes the map based on scaling factors sx (width scale) and sy (height scale).
     * It creates a new larger or smaller grid and uses nearest-neighbor logic to map pixels from the old grid to the new one.
     */
    public void rescale(double sx, double sy) {
        int newW = (int)(this.W * sx);
        int newH = (int)(this.H * sy);
        int [][] newMAP = new int[newH][newW];
        for (int y = 0; y < newH; y+=1) {
            for (int x = 0; x < newW; x+=1) {
                int oldX = (int)(x / sx);
                int oldY = (int)(y / sy);
                newMAP[y][x] = this.getPixel(oldX, oldY);
            }
        }
        this.W = newW;
        this.H = newH;
        this.MAP = newMAP;
    }

    /**
     * Draws a filled circle of a specific color defined by a center point and a radius rad.
     * It iterates through the grid and checks which pixels fall inside the mathematical definition of the circle.
     */
    public void drawCircle(Pixel2D center, double rad, int color) {
        Circle c = new Circle(center, rad);
        for (int i = 0; i < this.getHeight(); i+=1) {
            for (int j = 0; j < this.getWidth(); j+=1) {
                Pixel2D p = new Index2D(j, i);
                if (c.contains(p)) {this.setPixel(p, color);}
            }
        }
    }

    /**
     * Draws a straight line between point p1 and p2 in the given color.
     * It calculates the slope (m) and handles different cases (vertical, horizontal, steep slopes) to ensure the line is continuous.
     */
    public void drawLine(Pixel2D p1, Pixel2D p2, int color) {
        if (this.isInside(p1) && this.isInside(p2)) {
            int dx = Math.abs(p2.getX() - p1.getX());
            int dy = Math.abs(p2.getY() - p1.getY());
            double m = (double) dy /dx; // slope
            // 1. if p1 equals p2 - a single pixel will be drawn.
            if (p1.equals(p2)) {this.setPixel(p1, color);}
            // 2. assuming dx>=dy & p1.x<p2.x: dx+1 pixels will be drawn.
            // let f(x) be the linear function going throw p1&p2.
            // let x=p1.x, p1.x+1, p1.x+2...p1.x+dx (=p2.x)
            // all the pixels (x,round(f(x)) will be drawn.
            if (dx >= dy && p1.getX() < p2.getX()) {
                for (int i = 0; i <= dx; i+=1) {
                    int x = p1.getX() + i;
                    int fx = (int) ((m * (x - p1.getX())) + p1.getY());
                    this.setPixel(x-1, fx-1, color);
                }
            }
            // 3. assuming dx>=dy & p1.x>p2.x: the line p2,p1 will be drawn.
            if (dx >= dy && p1.getX() > p2.getX()) {drawLine(p2, p1, color);}
            // 4. assuming dx < dy & p1.y < p2.y: dy+1 pixels will be drawn.
            // let g(y) be the linear function going throw p1&p2.
            // let y=p1.y, p1.y+1, p1.y+2...p1.y+dy (=p2.y)
            // all the pixels (y,round(g(y)) will be drawn.
            if (dy > dx && p1.getY() < p2.getY()) {
                for (int i = 1; i <= dy+1; i+=1) {
                    int y = p1.getY() + i;
                    int gy = (int) (((y - p1.getY()) / m) + p1.getX());
                    this.setPixel(gy, y-1, color);
                }
            }
            // 5. assuming dy>dx & p1.y>p2.y: the line p2,p1 will be drawn.
            if (dy > dx && p1.getY() > p2.getY()) {drawLine(p2, p1, color);}
        }

    }

    /**
     * Draws a filled rectangle defined by two opposite corners p1 and p2.
     * It calculates the min/max x and y bounds and fills every pixel within that box.
     */
    public void drawRect(Pixel2D p1, Pixel2D p2, int color) {
        if (this.isInside(p1) && this.isInside(p2)) {
            int xStart = Math.min(p1.getX(), p2.getX());
            int xEnd = Math.max(p1.getX(), p2.getX());
            int yStart = Math.min(p1.getY(), p2.getY());
            int yEnd = Math.max(p1.getY(), p2.getY());
            for (int i = yStart; i <= yEnd; i+=1) {
                for (int j = xStart; j <= xEnd; j+=1) {
                    this.setPixel(j, i, color);
                }
            }
        }
    }

    /**
     * Overrides the default object comparison. It checks if two maps are identical by comparing their dimensions and then checking every single pixel value.
     */
    @Override
    public boolean equals(Object ob) {
        if (!(ob instanceof Map2D)) {return false;}
        else if (!(this.sameDimensions((Map2D)ob))) {return false;}
        else {
            for (int y = 0; y < this.H; y+=1) {
                for (int x = 0; x < this.W; x+=1) {
                    if (this.getPixel(x,y) != ((Map2D) ob).getPixel(x,y)) {return false;}
                }
            }
            return true;
        }
    }
    /**
     * Implements a Flood Fill algorithm (like the "paint bucket" tool). It changes the start pixel and all connected pixels
     * of the same original color to new_v. It returns the total number of pixels changed.
     * <a href="https://en.wikipedia.org/wiki/Flood_fill">Wikipedia link</a>
     */
    @Override
    public int fill(Pixel2D start, int new_v) {
        int ans = 0; // making result
        boolean cyclic = this.isCyclic();
        int startingColor = this.getPixel(start);
        // checking if the starting pixel is already the new color
        if (startingColor == new_v) {return 0;}
        this.setPixel(start, new_v);
        ans += 1;

        // using a BFS method to set every pixel that is visited into the new color
        PixelsContainer q = new PixelsContainer();
        q.enqueue(start);
        if (!cyclic) {
            while (!q.isEmpty()) {
                Pixel2D node = q.dequeue();
                PixelsContainer neighbours = this.checkNeighboursNotCyclic(node, startingColor);
                for (Pixel2D next : neighbours.getList()) {
                    if (this.getPixel(next) != new_v) {
                        q.enqueue(next);
                        this.setPixel(next, new_v);
                        ans += 1;
                    }
                }
            }

        }
        else {
            while (!q.isEmpty()) {
                Pixel2D node = q.dequeue();
                PixelsContainer neighbours = this.checkNeighboursCyclic(node, startingColor);
                for (Pixel2D next : neighbours.getList()) {
                    if (this.getPixel(next) != new_v) {
                        q.enqueue(next);
                        this.setPixel(next, new_v);
                        ans += 1;
                    }
                }
            }
        }
        return ans;
    }

    /**
     * Finds the shortest path from start to end using Breadth-First Search (BFS).
     * It treats pixels with the value obsColor as obstacles (walls) that cannot be traversed. It returns an array of pixels representing the path.
     * <a href="https://en.wikipedia.org/wiki/Breadth-first_search">Wikipedia link</a>
     */
    @Override
    public Pixel2D[] shortestPath(Pixel2D start, Pixel2D end, int obsColor) {
        Pixel2D[] ans;  // the result.

        Map2D maze = this.preppingMaze(obsColor);

        // checking if there is a way to get from s pixel to e pixel
        maze.fill(start,1);
        if (maze.getPixel(end) != 1) {return null;}

        // making a BFS dictionary
        Map<Pixel2D, Pixel2D> prev = this.solve(start, obsColor);

        // using the dictionary to get from e pixel to s pixel
        PixelsContainer container = reconstructPath(end, prev);
        ans = container.getList();

        return ans;
    }

    /**
     * Generates a "distance map". Starting from start, it calculates the distance (number of steps) to every other reachable pixel.
     * Unreachable pixels are set to -1. Reachable pixels are set to their distance from the start point.
     */
    @Override
    public Map2D allDistance(Pixel2D start, int obsColor) {
        Map2D ans;  // the result.

        Map2D maze = this.preppingMaze(obsColor);

        // setting every reachable pixel to 1
        maze.fill(start,1);

        // setting every unreachable pixel to -1
        for (int y = 0; y < this.getHeight(); y+=1) {
            for (int x = 0; x < this.getWidth(); x+=1) {
                Pixel2D p = new Index2D(x,y);
                if (maze.getPixel(p) == 0) {
                    maze.setPixel(p,-1);
                }
            }
        }

        maze.fill(start,0);

        ans = new MyMap(maze.getMap(), maze.isCyclic());
        // setting every pixel to its distance from the start
        for (int y = 0; y < this.getHeight(); y+=1) {
            for (int x = 0; x < this.getWidth(); x+=1) {
                Pixel2D p = new Index2D(x,y);
                if (maze.getPixel(p) == 0) {
                    PixelsContainer path = new PixelsContainer(maze.shortestPath(start, p, -1));
                    int pathLength = path.getLength();
                    ans.setPixel(p,pathLength - 1);
                }
            }
        }

        return ans;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < this.H-1; y+=1) {
            for (int x = 0; x < this.W; x+=1) {
                sb.append(this.MAP[y][x]).append(' ');
            }
            sb.append('\n');
        }
        for (int x = 0; x < this.W; x+=1) {
            sb.append(this.MAP[this.H-1][x]).append(' ');
        }
        return sb.toString();
    }

    public static MyMap mapFromString(String mapS) {
        String[] rows = mapS.split("\n");
        int h = rows.length;
        rows[0] = rows[0].trim();
        int w = rows[0].replace(" ", "").length();
        int[][] m = new int[h][w];
        for (int y = 0; y < h; y++) {
            String[] pixels = rows[y].trim().split(" ");
            for (int x = 0; x < w; x+=1) {
                m[y][x] = Integer.parseInt(pixels[x]);
            }
        }
        return new MyMap(m, false);
    }


    ////////////////////// Private Methods ///////////////////////

    private void copy(int[][] ans, int w, int h) {
        this.MAP = new int[h][w];
        for (int i = 0; i < ans.length; i+=1) {
            MAP[i] = new int[ans[i].length];
            System.arraycopy(ans[i], 0, MAP[i], 0, ans[i].length);
        }
    }

    private Map<Pixel2D, Pixel2D> solve(Pixel2D s, int obs) {
        MyMap maze = new MyMap(this.getMap(), this.isCyclic());
        
        for (int y = 0; y < maze.getHeight(); y+=1) {
            for (int x = 0; x < maze.getWidth(); x+=1) {
                if (maze.getPixel(x, y) != obs) {
                    maze.setPixel(x, y, 0);
                }
            }
        }

        int v = maze.getPixel(s);
        boolean cyclic = maze.isCyclic();
        PixelsContainer q = new PixelsContainer();
        q.enqueue(s);
        Map<Pixel2D,Boolean> visited = new HashMap<>();
        for (int y = 0; y < maze.H; y+=1) {
            for (int x = 0; x < maze.W; x+=1) {
                Pixel2D p = new Index2D(x,y);
                if (maze.getPixel(p) == v) {
                    visited.put(p, false);
                }
            }
        }
        visited.replace(s,true);

        Map<Pixel2D,Pixel2D> prev = new HashMap<>();
        if (!cyclic) {
            while (!q.isEmpty()){
                Pixel2D node = q.dequeue();
                PixelsContainer neighbours = maze.checkNeighboursNotCyclic(node,v);

                for (Pixel2D next : neighbours.getList()) {
                    if (!visited.get(next)) {
                        q.enqueue(next);
                        visited.put(next, true);
                        prev.put(next,node);
                    }
                }
            }
        }
        else {
            while (!q.isEmpty()){
                Pixel2D node = q.dequeue();
                PixelsContainer neighbours = maze.checkNeighboursCyclic(node,v);

                for (Pixel2D next : neighbours.getList()) {
                    if (!visited.get(next)) {
                        q.enqueue(next);
                        visited.put(next, true);
                        prev.put(next,node);
                    }
                }
            }
        }


        return prev;
    }

    public PixelsContainer checkNeighboursNotCyclic(Pixel2D node, int v) {
        PixelsContainer neighbours = new PixelsContainer();

        // Right
        Pixel2D rNode = new Index2D(node.getX()+1,node.getY());
        if (this.isInside(rNode) && this.getPixel(rNode) == v) {
            neighbours.enqueue(rNode);
        }

        // Left
        Pixel2D lNode = new Index2D(node.getX()-1,node.getY());
        if (this.isInside(lNode) && this.getPixel(lNode) == v) {
            neighbours.enqueue(lNode);
        }

        // Up
        Pixel2D uNode = new Index2D(node.getX(),node.getY()+1);
        if (this.isInside(uNode) && this.getPixel(uNode) == v) {
            neighbours.enqueue(uNode);
        }

        // Down
        Pixel2D dNode = new Index2D(node.getX(),node.getY()-1);
        if (this.isInside(dNode) && this.getPixel(dNode) == v) {
            neighbours.enqueue(dNode);
        }

        return neighbours;
    }

    private PixelsContainer checkNeighboursCyclic(Pixel2D node, int v) {
        PixelsContainer neighbours = new PixelsContainer();

        // Right
        Pixel2D rNode = new Index2D(node.getX()+1,node.getY());
        if (node.getX() == this.getWidth()-1) {
            rNode = new Index2D(0,node.getY());
        }
        if (this.getPixel(rNode) == v) {
            neighbours.enqueue(rNode);
        }

        // Left
        Pixel2D lNode = new Index2D(node.getX()-1,node.getY());
        if (node.getX() == 0) {
            lNode = new Index2D(this.getWidth()-1,node.getY());
        }
        if (this.getPixel(lNode) == v) {
            neighbours.enqueue(lNode);
        }

        // Up
        Pixel2D uNode = new Index2D(node.getX(),node.getY()+1);
        if (node.getY() == this.getHeight()-1) {
            uNode = new Index2D(node.getX(),0);
        }
        if (this.getPixel(uNode) == v) {
            neighbours.enqueue(uNode);
        }

        // Down
        Pixel2D dNode = new Index2D(node.getX(),node.getY()-1);
        if (node.getY() == 0) {
            dNode = new Index2D(node.getX(),this.getHeight()-1);
        }
        if (this.getPixel(dNode) == v) {
            neighbours.enqueue(dNode);
        }

        return neighbours;
    }

    public PixelsContainer reconstructPath(Pixel2D e, Map<Pixel2D, Pixel2D> prev){
        PixelsContainer path = new PixelsContainer();
        for (Pixel2D at = e; at != null ; at = prev.get(at)) {
            path.enqueue(at);
        }

        path.reverse();

        return path;
    }

    private Map2D preppingMaze(int obsColor){
        // making a copy for the maze
        Map2D maze = new MyMap(this.getMap(), this.isCyclic());

        // setting obsColor to -1
        if (obsColor != -1) {
            for (int y = 0; y < this.getHeight(); y+=1) {
                for (int x = 0; x < this.getWidth(); x+=1) {
                    Pixel2D p = new Index2D(x,y);
                    if (maze.getPixel(p) == obsColor) {
                        maze.setPixel(p,-1);
                    }
                }
            }
            obsColor = -1;
        }

        // setting every else pixel to 0
        for (int y = 0; y < this.getHeight(); y+=1) {
            for (int x = 0; x < this.getWidth(); x+=1) {
                Pixel2D p = new Index2D(x,y);
                if (maze.getPixel(p) != obsColor) {
                    maze.setPixel(p,0);
                }
            }
        }
        return maze;
    }
}
