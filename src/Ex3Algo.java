import Classes.Index2D;
import Classes.Interfaces.Map2D;
import Classes.Interfaces.Pixel2D;
import Classes.MyMap;
import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;
import java.security.spec.RSAOtherPrimeInfo;

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
        boolean isCyclic = game.isCyclic();
        MyMap board_map = new MyMap(board, isCyclic);
        int wallColor = Game.getIntColor(Color.BLUE, code);
        String pos = game.getPos(code);
        Index2D pm = new Index2D(pos);
        GhostCL[] ghosts = game.getGhosts(code);
        Index2D[] gs = new Index2D[ghosts.length];
        for (int i = 0; i < ghosts.length; i+=1) {
            Index2D g = new Index2D(ghosts[i].getPos(code));
            gs[i] = g;
        }
		_count++;

        int cg_index = closest_ghost(board_map, pm, gs, wallColor);
        Pixel2D cg = gs[cg_index];
        int cg_dis = calc_dis(board_map, pm, cg, wallColor);
        Pixel2D[] cg_path = calc_path(board_map, pm, cg, wallColor);
        double eatable = ghosts[cg_index].remainTimeAsEatable(code);

        if (this._count > 50) {
            if (cg_dis < 3) {
                if (eatable > 0) {
                    return go(board_map, cg_path, false, wallColor);
                } else {
                    return go(board_map, cg_path, true, wallColor);
                }
            }
        }
        return closest_pink(board_map, pm, wallColor);
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

    private static int closest_pink(MyMap board, Pixel2D pm, int obs) {
        Pixel2D target = null;

        Map2D all_distance_board_map = board.allDistance(pm, obs);

        int min_dis = 1000;
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                if (board.getPixel(x,y) == 3){
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

        Pixel2D[] path = calc_path(board, pm, target, obs);

        return go(board, path, false, obs);
    }

    private static int go(MyMap board, Pixel2D[] path, boolean is_reversed, int obs) {
        if (is_reversed) {
            if (path != null && path.length > 1) {
                Pixel2D current = path[0];
                Pixel2D nextStep = path[1];
                int dir = reverseDirection(coordsToDirection(current, nextStep, board));
                nextStep = switch (dir) {
                    case Game.UP -> current.move(1, 0);
                    case Game.LEFT -> current.move(0, -1);
                    case Game.DOWN -> current.move(-1, 0);
                    case Game.RIGHT -> current.move(0, 1);
                    default -> nextStep;
                };
                if (board.getPixel(nextStep) == obs) {
                    nextStep = switch (dir) {
                        case Game.UP, Game.DOWN -> {
                            dir = Game.LEFT;
                            yield current.move(0, -1);
                        }
                        case Game.LEFT, Game.RIGHT -> {
                            dir = Game.UP;
                            yield current.move(1, 0);
                        }
                        default -> nextStep;
                    };
                    if (board.getPixel(nextStep) == obs) {
                        nextStep = switch (dir) {
                            case Game.UP -> {
                                dir = Game.DOWN;
                                yield current.move(-1, 0);
                            }
                            case Game.LEFT -> {
                                dir = Game.RIGHT;
                                yield current.move(0, 1);
                            }
                            default -> nextStep;
                        };
                    }
                }
                return dir;
            }
        }
        else {
            if (path != null && path.length > 1) {
                Pixel2D current = path[0];
                Pixel2D nextStep = path[1];
                return coordsToDirection(current, nextStep, board);
            }
        }
        return randomDir();
    }

    // You need to pass the map dimensions to calculate the wrap accurately
    private static int coordsToDirection(Pixel2D current, Pixel2D next, Map2D board) {
        int dx = next.getX() - current.getX();
        int dy = next.getY() - current.getY();
        int height = board.getHeight();
        int width = board.getWidth();

        // --- X-Axis (Vertical) ---
        // Check if the distance is larger than half the map height (implies wrapping)
        if (Math.abs(dx) > height / 2) {
            // Wrap logic: Invert normal direction
            if (dx > 0) return Game.DOWN; // e.g., Moved from Bottom(0) to Top(99) via wrap
            if (dx < 0) return Game.UP;   // e.g., Moved from Top(99) to Bottom(0) via wrap
        } else {
            // Normal logic
            if (dx > 0) return Game.UP;
            if (dx < 0) return Game.DOWN;
        }

        // --- Y-Axis (Horizontal) ---
        // Check if the distance is larger than half the map width (implies wrapping)
        if (Math.abs(dy) > width / 2) {
            // Wrap logic: Invert normal direction
            if (dy > 0) return Game.LEFT;  // e.g., Moved from Left(0) to Right(99) via wrap
            if (dy < 0) return Game.RIGHT; // e.g., Moved from Right(99) to Left(0) via wrap
        } else {
            // Normal logic
            if (dy < 0) return Game.LEFT;
            if (dy > 0) return Game.RIGHT;
        }

        return Game.UP; // Default
    }

    private static int reverseDirection(int dir) {
        return switch (dir) {
            case Game.UP -> Game.DOWN;
            case Game.LEFT -> Game.RIGHT;
            case Game.DOWN -> Game.UP;
            case Game.RIGHT -> Game.LEFT;
            default -> randomDir();
        };
    }

    private static int closest_ghost(MyMap board, Pixel2D pm, Pixel2D[] gs, int obs) {
        Pixel2D g = new Index2D(gs[0]);
        int dis = calc_dis(board, pm, g, obs);
        int index = 0;
        for (int i = 1; i < gs.length; i+=1) {
            g = gs[i];
            int temp_dis = calc_dis(board, pm, g, obs);
            if (temp_dis < dis) {
                dis = temp_dis;
                index = i;
            }
        }
        return index;
    }

    private static Pixel2D[] calc_path(MyMap board, Pixel2D start, Pixel2D end, int obs) {return board.shortestPath(start, end, obs);}
    private static int calc_dis(MyMap board, Pixel2D start, Pixel2D end, int obs){return calc_path(board, start, end, obs).length;}
}