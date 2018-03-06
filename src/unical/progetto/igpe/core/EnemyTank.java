package unical.progetto.igpe.core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

public class EnemyTank extends Tank {

	private int point;
	private boolean appearsInTheMap;
	private boolean powerUpOn;
	private boolean noUpdateG;
	private boolean stopEnemy;        // powerUp TIMER
	private boolean stopEnemyGraphic; // powerUp TIMER
	///////////////////////// EASY && MEDIUM ///////////////////////////
	private boolean[] directions;
	private boolean everySecond ;
	private long switchT; 				//switcha da easy a medium
	private boolean easy;
	//////////////////////////// DIFFICULT //////////////////////////////
	private ArrayList<Point> blocchi;
	private Cell[][] grid;
	private PriorityQueue<Cell> open;
	private boolean[][] minimalRoute;
	private boolean closed[][];
	private int startI, startJ;
	private int endI, endJ;
	public final int V_H_COST;
	public final int V_H_COST_BRICK;
	private boolean hasApath; // IL NEMICO CON LA DIFFICOLTA' MASSIMA CI DICE SE HA TROVATO UN PERCORSO O MENO
	private int randomObject;
//	private int xPast;
//	private int yPast;
	private int shotTimeEverySecond;
	
	public EnemyTank(int x, int y, World world, Speed speed, Speed speedShot, Direction direction, int health,
			int point, int numOfPlayers) {
		super(x, y, world, speed, speedShot, direction, health);

		this.setPoint(point);
		this.appearsInTheMap = false;
		this.powerUpOn = false;
		this.noUpdateG = false;
		this.stopEnemy = false;
		this.stopEnemyGraphic = false;
		this.setReadyToSpawn(false);
		this.setUpdateObject(true);
		this.setRandomObject(new Random().nextInt(numOfPlayers));
		this.minimalRoute = new boolean[world.getRow()][world.getColumn()];
		this.closed = new boolean[world.getRow()][world.getColumn()];
		this.grid = new Cell[world.getRow()][world.getColumn()];
		this.blocchi = new ArrayList<>();
		this.V_H_COST = 5;
		this.V_H_COST_BRICK = 20;
		this.hasApath = false;
		this.everySecond = true;
		this.switchT = 0;
		this.easy=true;
		this.directions = new boolean[4];
//		this.xPast = -1;
//		this.yPast = -1;
		this.shotTimeEverySecond=1;
		for (int i = 0; i < directions.length; i++) {directions[i] = false;}
	}

	@Override
	public void update() {
//		xPast = getX();
//		yPast = getY();
		super.update();
		getWorld().getWorld()[getX()][getY()] = this;
	}

	@Override
	public boolean sameObject() {

//		if (next instanceof Rocket && ((Rocket) next).getTank() == this) {
//			next = ((Rocket) next).getCurr();
//			return false;
//		}

		if (!(next instanceof Wall) && !(next instanceof Tank) && !(next instanceof Water) && !(next instanceof Rocket)
				&& !(next instanceof Flag)) {

			if (next instanceof PowerUp && ((PowerUp) next).getPowerUp() == Power.HELMET) {
				if (!(((PowerUp) next).getBefore() instanceof Tree) && !(((PowerUp) next).getBefore() instanceof Ice)
						&& !(((PowerUp) next).getBefore() instanceof Water)) {
					curr = null;
				} else if (((PowerUp) next).getBefore() instanceof Water) {
					curr = ((PowerUp) next).getBeforeWater();
				} else if (((PowerUp) next).getBefore() instanceof Tree || ((PowerUp) next).getBefore() instanceof Ice)
					curr = ((PowerUp) next).getBefore();
			} else
				curr = next;

			return true;
		}
		return false;
	}

	////////////////////////////// EASY////////////////////////////////////////////////////////////

	public void easy() {

		if (!canGo && everySecond) {
			chooseDirection();
			int dir = -1;
			do {
				dir = new Random().nextInt(4);
			} while (!directions[dir]);
			setDir(dir);
			everySecond = false;
		}	
	}

	public void chooseDirection() {

		int x = getX();
		int y = getY();
		int left, right, up, down;

		// up
		if (x > 0) {
			up = x - 1;
			if (!(getWorld().getWorld()[up][y] instanceof SteelWall) && !(getWorld().getWorld()[up][y] instanceof Water)) {
				directions[0] = true;
			} else
				directions[0] = false;
		} else
			directions[0] = false;

		// down
		if (x < getWorld().getRow() - 1) {
			down = x + 1;
			if (!(getWorld().getWorld()[down][y] instanceof SteelWall) && !(getWorld().getWorld()[down][y] instanceof Water)) {
				directions[1] = true;

			} else
				directions[1] = false;
		} else
			directions[1] = false;

		// right
		if (y < getWorld().getColumn() - 1) {
			right = y + 1;
			if (!(getWorld().getWorld()[x][right] instanceof SteelWall) && !(getWorld().getWorld()[x][right] instanceof Water)) {
				directions[2] = true;
			} else
				directions[2] = false;
		} else
			directions[2] = false;

		// left
		if (y > 0) {
			left = y - 1;
			if (!(getWorld().getWorld()[x][left] instanceof SteelWall) && !(getWorld().getWorld()[x][left] instanceof Water)) {
				directions[3] = true;
			} else
				directions[3] = false;
		} else
			directions[3] = false;
	}
	

	////////////////////////////// MEDIUM //////////////////////////////////////////////////////////	
	
	public void medium() { 
	
			if(easy && switchT < 10)	
				easy();
			
			else if(!easy && switchT < 4)	
				difficult(GameManager.flag.getX(), GameManager.flag.getY());
			
			if(switchT >= 15 && easy) {
				switchT = 0;
				easy = false;
			}else if( switchT >= 5 && !easy) {
				switchT = 0;
				easy = true;
			}
			
	}
	
	////////////////////////////// DIFFICULT //////////////////////////////////////////////////////

	void checkAndUpdateCost(Cell current, Cell t, int cost) {

		// se t è nullo o close[i][j] è stato gia visitato
		if (t == null || closed[t.i][t.j])
			return;

		int t_final_cost = t.heuristicCost + cost;

		boolean inOpen = open.contains(t);
		if (!inOpen || t_final_cost < t.finalCost) {
			t.finalCost = t_final_cost;
			t.parent = current;
			// new route
			if (!inOpen)
				open.add(t);
		}
	}

	public void AStar() {

		// add the start location to open list.
		if (grid[startI][startJ] != null)
			open.add(grid[startI][startJ]);

		Cell current;

		while (true) {
			current = open.poll();
			if (current == null)
				break;
			// set true corrent pos
			closed[current.i][current.j] = true;

			// if near flag return
			if (current.equals(grid[endI][endJ])) {
				return;
			}

			Cell t;
			if (current.i - 1 >= 0) {
				t = grid[current.i - 1][current.j];
				checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
			}

			if (current.j - 1 >= 0) {
				t = grid[current.i][current.j - 1];
				checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
			}

			if (current.j + 1 < grid[0].length) {
				t = grid[current.i][current.j + 1];
				checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
			}

			if (current.i + 1 < grid.length) {
				t = grid[current.i + 1][current.j];
				checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
			}
		}
	}

	public void searchRoute(int x, int y, int si, int sj, int ei, int ej, ArrayList<Point> blocchi2) {
		open = new PriorityQueue<>((Object o1, Object o2) -> {
			Cell c1 = (Cell) o1;
			Cell c2 = (Cell) o2;
			return c1.finalCost < c2.finalCost ? -1 : c1.finalCost > c2.finalCost ? 1 : 0;
		});

		setStartCell(si, sj);
		setEndCell(ei, ej);

		for (int i = 0; i < x; ++i) {
			for (int j = 0; j < y; ++j) {
				grid[i][j] = new Cell(i, j);
				if (world.getWorld()[i][j] instanceof BrickWall)
					grid[i][j].heuristicCost = (Math.abs(i - endI) + Math.abs(j - endJ)) + V_H_COST_BRICK;
				else {
					grid[i][j].heuristicCost = Math.abs(i - endI) + Math.abs(j - endJ);
				}
			}
		}
		grid[si][sj].finalCost = 0;

		for (int i = 0; i < blocchi2.size(); ++i) {
			setBlocked((int) blocchi2.get(i).getX(), (int) blocchi2.get(i).getY());
		}

		AStar();

//		if (getX() == xPast && getY() == yPast) {
//			setDirectionRandomForBlock();
//		} else {
			if (closed[endI][endJ]) {
				Cell current = grid[endI][endJ];
				minimalRoute[current.i][current.j] = true;
				while (current.parent != null) {
					current = current.parent;
					minimalRoute[current.i][current.j] = true;
				}
				hasApath = true;
			} else {
				hasApath = false;
			}
//		}
	}

	public void difficult(int objectX, int objectY) {

		blocchi.clear();
		for (int a = 0; a < world.getRow(); a++) {
			for (int b = 0; b < world.getColumn(); b++) {
				if (world.getWorld()[a][b] != null && world.getWorld()[a][b] != this
						&& (world.getWorld()[a][b] instanceof SteelWall || world.getWorld()[a][b] instanceof EnemyTank
								|| (world.getWorld()[a][b] instanceof Water
										&& (a != GameManager.flag.getX() && b != GameManager.flag.getY()))
								|| (world.getWorld()[a][b] instanceof Water
										&& (a == GameManager.flag.getX() || b == GameManager.flag.getY())
										&& noPresentSteelWall(a, b)))) {
					blocchi.add(new Point(a, b));
				}
			}
		}

		for (int i = 0; i < world.getRow(); i++) {
			for (int j = 0; j < world.getColumn(); j++) {
				minimalRoute[i][j] = false;
				closed[i][j] = false;
				grid[i][j] = null;
			}
		}

		searchRoute(world.getRow(), world.getColumn(), getX(), getY(), objectX, objectY, blocchi);

		int currX = getX();
		int currY = getY();

		minimalRoute[currX][currY] = false;

		if (hasApath) {
			if (currX - 1 >= 0) {
				if (minimalRoute[currX - 1][currY]) {
					setDirection(Direction.UP);
				}
			}

			if (currY - 1 >= 0) {
				if (minimalRoute[currX][currY - 1]) {
					setDirection(Direction.LEFT);
				}
			}

			if (currY + 1 < world.getColumn() - 1) {
				if (minimalRoute[currX][currY + 1]) {
					setDirection(Direction.RIGHT);
				}
			}

			if (currX + 1 < world.getRow() - 1) {
				if (minimalRoute[currX + 1][currY]) {
					setDirection(Direction.DOWN);
				}

			}
		}

	}

	public void setDirectionRandomForBlock() {
		int random = new Random().nextInt(4);
		if (random == 0) {
			setDirection(Direction.UP);
		} else if (random == 1) {
			setDirection(Direction.DOWN);
		} else if (random == 2) {
			setDirection(Direction.LEFT);
		} else if (random == 3) {
			setDirection(Direction.RIGHT);
		}
	}

	private boolean noPresentSteelWall(int x, int y) {
		for (int b = 0; b < world.getColumn(); b++) {
			if (((b > y && b < GameManager.flag.getY()) || (b < y && b > GameManager.flag.getY()))
					&& world.getWorld()[GameManager.flag.getX()][b] instanceof SteelWall) {
				return true;
			}
		}

		for (int b = 0; b < world.getRow(); b++) {
			if (((b > x && b < GameManager.flag.getX()) || (b < x && b > GameManager.flag.getX()))
					&& world.getWorld()[b][GameManager.flag.getY()] instanceof SteelWall) {
				return true;
			}
		}
		return false;
	}

	class Cell {
		int heuristicCost = 0; // Heuristic cost
		int finalCost = 0; // G+H
		int i, j;
		Cell parent;

		Cell(int i, int j) {
			this.i = i;
			this.j = j;
		}

		@Override
		public String toString() {
			return "[" + this.i + ", " + this.j + "]";
		}
	}

	public void setBlocked(int i, int j) {
		grid[i][j] = null;
	}

	public void setStartCell(int i, int j) {
		startI = i;
		startJ = j;
	}

	public void setEndCell(int i, int j) {
		endI = i;
		endJ = j;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////

	public void setDir(int dir) {
		switch (dir) {
		case 0:
			this.setDirection(Direction.UP);
			break;
		case 1:
			this.setDirection(Direction.DOWN);
			break;
		case 2:
			this.setDirection(Direction.RIGHT);
			break;
		case 3:
			this.setDirection(Direction.LEFT);
			break;
		default:
			this.setDirection(Direction.STOP);
			break;
		}
	}

	public boolean isAppearsInTheMap() {
		return appearsInTheMap;
	}

	public void setAppearsInTheMap(boolean appearsInTheMap) {
		this.appearsInTheMap = appearsInTheMap;
	}

	public boolean isPowerUpOn() {
		return powerUpOn;
	}

	public void setPowerUpOn(boolean powerUpOn) {
		this.powerUpOn = powerUpOn;
	}

	public boolean isNoUpdateG() {
		return noUpdateG;
	}

	public void setNoUpdateG(boolean noUpdateG) {
		this.noUpdateG = noUpdateG;
	}

	public boolean isStopEnemy() {
		return stopEnemy;
	}

	public void setStopEnemy(boolean stopEnemy) {
		this.stopEnemy = stopEnemy;
	}

	public boolean isStopEnemyGraphic() {
		return stopEnemyGraphic;
	}

	public void setStopEnemyGraphic(boolean stopEnemyGraphic) {
		this.stopEnemyGraphic = stopEnemyGraphic;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public boolean isHasApath() {
		return hasApath;
	}

	public void setHasApath(boolean hasApath) {
		this.hasApath = hasApath;
	}

	public int getRandomObject() {
		return randomObject;
	}

	public void setRandomObject(int randomObject) {
		this.randomObject = randomObject;
	}

//	public int getxPast() {
//		return xPast;
//	}
//
//	public void setxPast(int xPast) {
//		this.xPast = xPast;
//	}
//
//	public int getyPast() {
//		return yPast;
//	}
//
//	public void setyPast(int yPast) {
//		this.yPast = yPast;
//	}

	public long getSwitchT() {
		return switchT;
	}
	
	public boolean isEverySecond() {
		return everySecond;
	}

	public void setEverySecond(boolean everySecond) {
		this.everySecond = everySecond;
	}
	
	public void setSwitchT(long switchT) {
		this.switchT = switchT;
	}

	public int getShotTimeEverySecond() {
		return shotTimeEverySecond;
	}

	public void setShotTimeEverySecond(int shotTimeEverySecond) {
		this.shotTimeEverySecond = shotTimeEverySecond;
	}
}
