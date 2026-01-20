package client;

import Classes.Index2D;
import Classes.Interfaces.Map2D;
import Classes.Interfaces.Pixel2D;
import Classes.MyMap;
import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

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
        int[][] board = game.getGame(0);
        boolean cyclic = game.isCyclic();
        MyMap boardMap = new MyMap(board, cyclic);
        int obs = Game.getIntColor(Color.BLUE, code);
        boardMap.setPixel(13,11,obs);
        Index2D pm = new Index2D(game.getPos(code));
        GhostCL[] ghosts = game.getGhosts(code);
        Index2D[] gs = new Index2D[ghosts.length];
        for (int i = 0; i < ghosts.length; i+=1) {
            if (ghosts[i].getStatus() == 1) {
                Index2D g = new Index2D(ghosts[i].getPos(code));
                gs[i] = g;
            }
        }

        Pixel2D[] cgs = getClosestGhosts(boardMap, pm, gs, obs);
        int dl = getDangerLevel(boardMap, pm, cgs, obs);
        _count++;

        if (ghosts[0].remainTimeAsEatable(code) > 2) {return closestPink(boardMap, pm, cgs, obs);}
        if (dl >= 25) {
            if (closestPinkDistance(boardMap, pm, cgs, obs).length <= 3) {return closestPink(boardMap, pm, cgs, obs);}
            return run(boardMap, pm, cgs, obs);
        }
        return closestPink(boardMap, pm, cgs, obs);
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

    private static Pixel2D[] closestPinkDistance(MyMap board, Pixel2D pm, Pixel2D[] gs, int obs) {
        Map2D all_distance_board_map = board.allDistance(pm, obs);

        Pixel2D bestTarget = null;
        double bestScore = Double.MAX_VALUE;

        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                if (board.getPixel(x, y) == 3 || board.getPixel(x, y) == 5) {
                    Pixel2D tempTarget = new Index2D(x, y);
                    int dist = all_distance_board_map.getPixel(x, y);
                    if (dist > bestScore) continue;
                    int danger = getDangerLevel(board, tempTarget, gs, obs);
                    switch (calcPaths(board, tempTarget, obs)){
                        case 2 -> danger += 125;
                        case 3 -> danger += 150;
                    }
                    double score = dist + (danger * 10);
                    if (score < bestScore) {
                        bestScore = score;
                        bestTarget = tempTarget;
                    }
                }
            }
        }

        if (bestTarget == null) {return null;}

        return calcPath(board, pm, bestTarget, obs);
    }

    private static int closestPink(MyMap board, Pixel2D pm, Pixel2D[] gs, int obs) {return go(board, closestPinkDistance(board, pm, gs, obs), obs);}

        private static int go(Map2D board, Pixel2D[] path, int obs) {
        if (path != null && path.length > 1) {
            Pixel2D current = path[0];
            Pixel2D nextStep = path[1];
            return coordsToDirection(board, current, nextStep);
        }
        return randomDir();
    }

    private static int run(Map2D board, Pixel2D current, Pixel2D[] gs, int obs) {
        int safestDir = -1;
        int minDanger = Integer.MAX_VALUE;
        for (int dir = 1; dir <= 4; dir++) {
            Pixel2D nextStep = switch (dir) {
                case Game.UP -> current.move(1, 0);
                case Game.LEFT -> current.move(0, -1);
                case Game.DOWN -> current.move(-1, 0);
                case Game.RIGHT -> current.move(0, 1);
                default -> null;
            };
            if (!board.isInside(nextStep)) {
                nextStep = switch (dir) {
                    case Game.UP -> new Index2D(0, current.getY());
                    case Game.LEFT -> new Index2D(current.getX(), board.getWidth()-1);
                    case Game.DOWN -> new Index2D(board.getWidth()-1, current.getY());
                    case Game.RIGHT -> new Index2D(current.getX(), 0);
                    default -> null;
                };
            }
            if (nextStep == null || board.getPixel(nextStep) == obs) {continue;}

            if (isDeadEnd(board, current, nextStep, obs)) {
                int currentDanger = 999;
                if (currentDanger < minDanger) {
                    minDanger = currentDanger;
                    safestDir = dir;
                }
                continue;
            }

            int dangerLevel = getDangerLevel(board, nextStep, gs, obs);

            if (dangerLevel < minDanger) {
                minDanger = dangerLevel;
                safestDir = dir;
            }
        }

        if (safestDir == -1) {return randomDir();}

        return safestDir;
    }

    private static int calcPaths(Map2D board, Pixel2D current, int obs) {
        int openPaths = 0;
        for (int dir = 1; dir <= 4; dir++) {
            Pixel2D neighbor = switch (dir) {
                case Game.UP -> current.move(1, 0);
                case Game.LEFT -> current.move(0, -1);
                case Game.DOWN -> current.move(-1, 0);
                case Game.RIGHT -> current.move(0, 1);
                default -> null;
            };
            if (!board.isInside(neighbor)) {
                neighbor = switch (dir) {
                    case Game.UP -> new Index2D(0, current.getY());
                    case Game.LEFT -> new Index2D(current.getX(), board.getHeight()-1);
                    case Game.DOWN -> new Index2D(board.getWidth()-1, current.getY());
                    case Game.RIGHT -> new Index2D(current.getX(), 0);
                    default -> null;
                };
            }
            if (neighbor != null && board.getPixel(neighbor) != obs) {openPaths++;}
        }
        return openPaths;
    }

    private static boolean isDeadEnd(Map2D board, Pixel2D current, Pixel2D next, int obs) {
        int openPaths = 0;
        for (int dir = 1; dir <= 4; dir++) {
            Pixel2D neighbor = switch (dir) {
                case Game.UP -> next.move(1, 0);
                case Game.LEFT -> next.move(0, -1);
                case Game.DOWN -> next.move(-1, 0);
                case Game.RIGHT -> next.move(0, 1);
                default -> null;
            };
            if (!board.isInside(neighbor)) {
                neighbor = switch (dir) {
                    case Game.UP -> new Index2D(0, next.getY());
                    case Game.LEFT -> new Index2D(next.getX(), board.getHeight()-1);
                    case Game.DOWN -> new Index2D(board.getWidth()-1, next.getY());
                    case Game.RIGHT -> new Index2D(next.getX(), 0);
                    default -> null;
                };
            }
            if (neighbor != null && !neighbor.equals(current) && board.getPixel(neighbor) != obs) {openPaths++;}
        }
        return openPaths == 0;
    }

    private static int getDangerLevel(Map2D board, Pixel2D cord, Pixel2D[] gs, int obs) {
        int level = 0;
        for (Pixel2D g : gs) {
            if (g == null) continue;
            int dist = calcDis(board, cord, g, obs);
            if (dist < 3) {level += 50;}
            else if (dist < 5) {level += 25;}
            else if (dist < 7) {level += 15;}
            else if (dist < 9) {level += 10;}
            else if (dist < 11) {level += 5;}
        }
        return level;
    }

    private static int coordsToDirection(Map2D board, Pixel2D current, Pixel2D next) {
        int dx = next.getX() - current.getX();
        int dy = next.getY() - current.getY();
        int height = board.getHeight();
        int width = board.getWidth();

        if (Math.abs(dx) > height / 2) {
            if (dx > 0) return Game.DOWN;
            if (dx < 0) return Game.UP;
        } else {
            if (dx > 0) return Game.UP;
            if (dx < 0) return Game.DOWN;
        }

        if (Math.abs(dy) > width / 2) {
            if (dy > 0) return Game.LEFT;
            if (dy < 0) return Game.RIGHT;
        } else {
            if (dy < 0) return Game.LEFT;
            if (dy > 0) return Game.RIGHT;
        }

        return Game.UP;
    }

    private static Pixel2D[] getClosestGhosts(Map2D board, Pixel2D cord, Pixel2D[] gs, int obs) {
        Pixel2D[] activeGhosts = Arrays.stream(gs).filter(Objects::nonNull).toArray(Pixel2D[]::new);

        Arrays.sort(activeGhosts, new Comparator<Pixel2D>() {
            @Override
            public int compare(Pixel2D g1, Pixel2D g2) {
                int d1 = calcDis(board, cord, g1, obs);
                int d2 = calcDis(board, cord, g2, obs);
                return Integer.compare(d1, d2);
            }
        });

        int resultSize = Math.min(4, activeGhosts.length);
        Pixel2D[] cgs = new Index2D[resultSize];

        System.arraycopy(activeGhosts, 0, cgs, 0, resultSize);

        return cgs;
    }

    private static Pixel2D[] calcPath(Map2D board, Pixel2D start, Pixel2D end, int obs) {return board.shortestPath(start, end, obs);}
    private static int calcDis(Map2D board, Pixel2D start, Pixel2D end, int obs){
        Pixel2D[] path = calcPath(board, start, end, obs);
        if (path != null) {return path.length;}
        else {return 0;}
    }
}