package model.world;

import java.awt.Point;

public interface Damageable {

	public abstract Point getLocation();
	public abstract int getCurrentHP();
	public abstract void setCurrentHP(int hp);
}
