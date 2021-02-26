package it.unical.progetto.igpe.core;

public class TmpWall extends AbstractStaticObject {

	private boolean shot;
	private boolean tank;

	public TmpWall(int x, int y, World world, boolean shot, boolean tank) {
		super(x, y, world);
		this.shot = shot;
		this.tank = tank;
	} 

	public boolean isShot() {
		return shot;
	}

	public void setShot(boolean shot) {
		this.shot = shot;
	}

	public boolean isTank() {
		return tank;
	}

	public void setTank(boolean tank) {
		this.tank = tank;
	}

}
