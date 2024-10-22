package model.world;

import java.awt.Point;

public class Cover implements Damageable{
	private int currentHP;
	private Point location;
	private int x;
	private int y;

	public Cover(int y, int x) {
		this.x = x;
		this.y = y;
		location = new Point(y, x);
		int r = (int) Math.floor(Math.random() * (1000 - 100) + 100);
		setCurrentHP(r);
	}

	public int getCurrentHP() {
		return this.currentHP;
	}

	public void setCurrentHP(int x) {
		if (x < 0)
			this.currentHP = 0;
		else
			this.currentHP = x;
	}

	public Point getLocation() {
		return this.location;
	}

}
