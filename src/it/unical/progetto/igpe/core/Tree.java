package it.unical.progetto.igpe.core;

public class Tree extends GenericWall {

	public Tree(int x, int y, World world) {
		super(x, y, world, true, true);
	}

	@Override
	public String toString() {
		return " TT ";
	}

}
