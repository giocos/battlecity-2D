package it.unical.progetto.igpe.core;

public class Tree extends TmpWall {

	public Tree(int x, int y, World world) {
		super(x, y, world, true, true);
	}

	@Override
	public String toString() {
		return " TT ";
	}

}
