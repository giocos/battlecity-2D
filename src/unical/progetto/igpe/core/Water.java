package unical.progetto.igpe.core;

public class Water extends AllWall {

	@Override
	public String toString() {
		return " ~~ ";
	}			

	public Water(int x, int y, World world) {
		super(x, y, world, true, false);
	}
}
