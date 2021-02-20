package it.unical.progetto.igpe.core;

public class Rocket extends AbstractDynamicObject {

	private AbstractDynamicObject tank;
	private boolean firstAnimationNo;
	private boolean rocketForPlayer;	
	private boolean oneTimeSound;

	public Rocket(int x, int y, World world, Direction direction, AbstractDynamicObject tank) {		
		super(x, y, world, direction);
		this.tank = tank;
		this.curr = tank; 	
		oneTimeSound = true;
		this.setFirstAnimationNo(true);
		rocketForPlayer(); 
	}
	
	@Override
	public void update() {
		super.update();	
		if (!(curr instanceof Tank) && !(next instanceof Tank) && canGo)
			getWorld().getWorld()[getX()][getY()] = this;
	}
	
	@Override
	public boolean sameObject() {

		if( next == tank && !(this.getRect().intersects(getTank().getRect()))){
			return true;							
		}
		
		if (!(next instanceof Wall) && !(next instanceof Tank) && !(next instanceof Rocket)
				 && !(next instanceof Flag)) {
			curr=next;
			return true;
		}
		return false;
	}
	
	public void updateRect() {
		if (this.getDirection() == Direction.UP) {
			getRect().setLocation((int) (getxGraphics() + (getSizePixel() - 9)),  (int) getyGraphics() + ((getSizePixel() / 2) - 4));
		} else if (getDirection() == Direction.DOWN) {
			getRect().setLocation((int) getxGraphics(), (int) getyGraphics() + ((getSizePixel() / 2) - 4));
		} else if (getDirection() == Direction.LEFT) {
			getRect().setLocation((int) (getxGraphics() + ((getSizePixel() / 2) - 4)), (int) getyGraphics() + (getSizePixel()- 9));
		} else if (getDirection() == Direction.RIGHT) {
			getRect().setLocation((int) (getxGraphics() + ((getSizePixel() / 2) - 4)), (int) getyGraphics());
		}	
	}
	
	void rocketForPlayer(){
		if(tank instanceof PlayerTank){
			if(tank.getContRocket()<1){	
				setUpdateObject(true);
				setRocketForPlayer(true);
				}
			else{
				setUpdateObject(false);
				setRocketForPlayer(false);
			}
		}
		else{
			setUpdateObject(true);
			setRocketForPlayer(true);
		}
	}
	
	@Override
	public void setDirection(Direction direction) {
		super.setDirection(direction);
	}

	@Override
	public String toString() {
		return " -- ";
	}

	public AbstractDynamicObject getTank() {
		return tank;
	}

	public void setTank(AbstractDynamicObject tank) {
		this.tank = tank;
	}

	public boolean isRocketForPlayer() {
		return rocketForPlayer;
	}

	public void setRocketForPlayer(boolean rocketForPlayer) {
		this.rocketForPlayer = rocketForPlayer;
	}

	public boolean isFirstAnimationNo() {
		return firstAnimationNo;
	}

	public void setFirstAnimationNo(boolean firstAnimationNo) {
		this.firstAnimationNo = firstAnimationNo;
	}

	public boolean isOneTimeSound() {
		return oneTimeSound;
	}

	public void setOneTimeSound(boolean ontTimeSound) {
		this.oneTimeSound = ontTimeSound;
	}
}
