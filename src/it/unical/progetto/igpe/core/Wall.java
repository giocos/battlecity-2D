package it.unical.progetto.igpe.core;

public class Wall extends TmpWall {

	private int health;
	private AbstractStaticObject before; //salvo powerUp che cade
	
	public Wall(int x, int y, World world, int health, boolean shot, boolean tank) {
		super(x, y, world, shot, tank);
		this.health = health;
		before = null;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public AbstractStaticObject getBefore() {
		return before;
	}

	public void setBefore(AbstractStaticObject before) {
		this.before = before;
	}
}
