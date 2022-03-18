package model.world;

import java.awt.Point;

public class Cover {
	private int currentHP; //never be below zero
	private Point location;
	private int x;
	private int y;

	Cover(int x, int y) {
		this.x = x;
		this.y = y;
		int r = (int) Math.floor(Math.random() * (1000 - 100) + 100);
		setCurrentHP(r);
	}

	public int getCurrentHP() {
		return this.currentHP;
	}

	public void setCurrentHP(int x) {
		this.currentHP = x;
	}

	public Point getLoctaion() {
		return this.location;
	}

}
