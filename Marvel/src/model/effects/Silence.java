package model.effects;

import model.world.Champion;

public class Silence extends Effect {

	public Silence(int duration) {
		super("Silence", duration, EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) {
		int temp = c.getMaxActionPointsPerTurn();
		c.setMaxActionPointsPerTurn(temp + 2);	
		temp = c.getCurrentActionPoints();
		c.setCurrentActionPoints(temp + 2);
	}

	@Override
	public void remove(Champion c) {
		int temp = c.getMaxActionPointsPerTurn();
		c.setMaxActionPointsPerTurn(temp - 2);	
		temp = c.getCurrentActionPoints();
		c.setCurrentActionPoints(temp - 2);
	}
}
