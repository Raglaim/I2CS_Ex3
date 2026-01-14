import Classes.Index2D;
import Classes.Interfaces.Pixel2D;
import Classes.MyMap;
import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;

/**
 * This is the major algorithmic class for Ex3 - the PacMan game:
 *
 * This code is a very simple example (random-walk algorithm).
 * Your task is to implement (here) your PacMan algorithm.
 */
public class Ex3Algo implements PacManAlgo{
	private int _count;
	public Ex3Algo() {_count=0;}
	@Override
	/**
	 *  Add a short description for the algorithm as a String.
	 */
	public String getInfo() {
		return null;
	}
	@Override
	/**
	 * This ia the main method - that you should design, implement and test.
	 */
	public int move(PacmanGame game) {
        int code = 0;
        int[][] board = game.getGame(code);
        String pos = game.getPos(code);
        int wallColor = Game.getIntColor(Color.BLUE, code);

		if(_count==0 || _count==300) {
			printBoard(board);
			int blue = Game.getIntColor(Color.BLUE, code);
			int pink = Game.getIntColor(Color.PINK, code);
			int black = Game.getIntColor(Color.BLACK, code);
			int green = Game.getIntColor(Color.GREEN, code);
			System.out.println("Blue=" + blue + ", Pink=" + pink + ", Black=" + black + ", Green=" + green);
			System.out.println("Pacman coordinate: "+pos);
			GhostCL[] ghosts = game.getGhosts(code);
			printGhosts(ghosts);
			int up = Game.UP, left = Game.LEFT, down = Game.DOWN, right = Game.RIGHT;
		}
		_count++;
        return closest_pink(board, pos, wallColor);
    }
	private static void printBoard(int[][] b) {
		for(int y =0;y<b[0].length;y++){
			for(int x =0;x<b.length;x++){
				int v = b[x][y];
				System.out.print(v+"\t");
			}
			System.out.println();
		}
	}
	private static void printGhosts(GhostCL[] gs) {
		for(int i=0;i<gs.length;i++){
			GhostCL g = gs[i];
			System.out.println(i+") status: "+g.getStatus()+",  type: "+g.getType()+",  pos: "+g.getPos(0)+",  time: "+g.remainTimeAsEatable(0));
		}
	}
	private static int randomDir() {
		int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
		int ind = (int)(Math.random()*dirs.length);
		return dirs[ind];
	}

    private static int closest_pink(int[][] board, String pos_string, int obs) {
        Pixel2D target = null;
        Pixel2D pos = new Index2D(pos_string);
        MyMap board_map = new MyMap(board);

        MyMap all_distance_board_map = (MyMap) board_map.allDistance(pos, obs);

        int min_dis = 1000;
        for (int y = 0; y < board_map.getHeight(); y++) {
            for (int x = 0; x < board_map.getWidth(); x++) {
                if (board_map.getPixel(x,y) == 3){
                    int dist = all_distance_board_map.getPixel(x,y);
                    if (dist < min_dis) {
                        min_dis = dist;
                        target = new Index2D(x,y);
                    }
                }
            }
        }

        if (target == null) {
            return randomDir();
        }

        Pixel2D[] path = board_map.shortestPath(pos, target, obs);

        if (path != null && path.length > 1) {
            Pixel2D nextStep = path[1];
            return coordsToDirection(pos, nextStep);
        }
        return randomDir();
    }

    private static int coordsToDirection(Pixel2D current, Pixel2D next) {
        if (next.getY() > current.getY()) return Game.RIGHT;
        if (next.getY() < current.getY()) return Game.LEFT;
        if (next.getX() > current.getX()) return Game.UP;
        if (next.getX() < current.getX()) return Game.DOWN;
        return Game.UP; // Default
    }
}