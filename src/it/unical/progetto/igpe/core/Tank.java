package it.unical.progetto.igpe.core;

public abstract class Tank extends AbstractDynamicObject{

	private int countdown;
	private long spawnTime;
	private boolean protection;
	private boolean oldDirection;
	private Direction direction;
	private Direction tmpDirection;
	private long timerEffect;
	private boolean readyToSpawn;
	
	public Tank(int x, int y, World world, Speed speed, Speed speedShot, Direction direction, int health) {
		super(x, y, world, speed, speedShot, direction, health);
		this.setCountdown(0);
		this.direction = Direction.STOP;
		tmpDirection = Direction.STOP;
		timerEffect = -1;
		protection = false;
	}
	
	public void updateRect() {
		getRect().setLocation((int)getxGraphics()+getDifferenceTank(),(int)getyGraphics()+getDifferenceTank());
	}

	public boolean isReadyToSpawn() {
		return readyToSpawn;
	}

	public void setReadyToSpawn(boolean readyToSpawn) {
		this.readyToSpawn = readyToSpawn;
	}

	public int getCountdown() {
		return countdown;
	}

	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}

	public long getSpawnTime() {
		return spawnTime;
	}

	public void setSpawnTime(long spawnTime) {
		this.spawnTime = spawnTime;
	}

	public void setOld(Direction old) {
		this.direction = old;
	}

	public boolean isOldDirection() {
		return oldDirection;
	}

	public void setOldDirection(boolean oldDirection) {
		this.oldDirection = oldDirection;
	}
		
	public Direction getTmpDirection() {
		return tmpDirection;
	}

	public void setTmpDirection(Direction tmpDirection) {
		this.tmpDirection = tmpDirection;
	}

	public long getTimerEffect() {
		return timerEffect;
	}

	public void setTimerEffect(long timerEffect) {
		this.timerEffect = timerEffect;
	}

	public boolean isProtection() {
		return protection;
	}

	public void setProtection(boolean protection) {
		this.protection = protection;
	}

	@Override
	public Direction getDirection() {
		return direction;
	}

	@Override
	public void update(){
		super.update();
	}
}
