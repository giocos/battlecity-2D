package it.unical.progetto.igpe.core;

public class Ice extends TmpWall {

	public Ice(int x, int y, World world) {
		super(x, y, world, true, true);
	}

	@Override
	public String toString() {
		return " @@ ";
	}
}
