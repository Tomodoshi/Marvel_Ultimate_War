package model.effects;

import model.world.Champion;

public class SpeedUp extends Effect {

	public SpeedUp(int duration) {
		super("SpeedUp", duration, EffectType.BUFF);
	}

	@Override
	public void apply(Champion c) {
		int temp = c.getSpeed();
		c.setSpeed((int) (temp * 1.15));
		temp = c.getMaxActionPointsPerTurn();
		c.setMaxActionPointsPerTurn(temp + 1);
		temp = c.getCurrentActionPoints();
		c.setCurrentActionPoints(temp + 1);		
	}

	@Override
	public void remove(Champion c) {
		int temp = c.getSpeed();
		c.setSpeed((int) (temp / 1.15));
		temp = c.getMaxActionPointsPerTurn();
		c.setMaxActionPointsPerTurn(temp - 1);
		temp = c.getCurrentActionPoints();
		c.setCurrentActionPoints(temp - 1);			
	}
}
