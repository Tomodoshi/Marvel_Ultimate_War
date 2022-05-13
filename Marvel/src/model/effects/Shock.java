package model.effects;

import model.world.Champion;

public class Shock extends Effect {

	public Shock(int duration) {
		super("Shock", duration, EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) {
		int temp = c.getSpeed();
		c.setSpeed((int) (temp * 0.9));
		temp = c.getAttackDamage();
		c.setAttackDamage((int)(temp * 0.9));
		temp = c.getMaxActionPointsPerTurn();
		c.setMaxActionPointsPerTurn(temp - 1);
		temp = c.getCurrentActionPoints();
		c.setCurrentActionPoints(temp - 1);
		
	}

	@Override
	public void remove(Champion c) {
		int temp = c.getSpeed();
		c.setSpeed((int) (temp / 0.9));	
		temp = c.getAttackDamage();
		c.setAttackDamage((int)(temp / 0.9));
		temp = c.getMaxActionPointsPerTurn();
		c.setMaxActionPointsPerTurn(temp + 1);
		temp = c.getCurrentActionPoints();
		c.setCurrentActionPoints(temp + 1);
	}
}
