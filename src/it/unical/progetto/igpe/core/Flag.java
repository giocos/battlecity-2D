package it.unical.progetto.igpe.core;

public class Flag extends AbstractStaticObject {

	private int inc;
	private boolean hit;

	public Flag(int x, int y, World world) {
		super(x, y, world);
		this.inc = 0;
		this.hit = false;
	}

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public int getInc() {
		return inc;
	}

	public void setInc(int inc) {
		this.inc = inc;
	}

	@Override
	public String toString() {
		return " && ";
	}
}
