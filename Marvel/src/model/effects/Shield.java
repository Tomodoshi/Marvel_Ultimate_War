package model.effects;

import model.abilities.DamagingAbility;
import model.world.Champion;

public class Shield extends Effect {

	public Shield(int duration) {
		super("Shield", duration, EffectType.BUFF);
	}

	@Override
	public void apply(Champion c) {
		int temp = c.getSpeed();
		c.setSpeed((int) (temp * 1.02));
	}

	@Override
	public void remove(Champion c) {
		int temp = c.getSpeed();
		c.setSpeed((int) (temp / 1.02));
		
	}
}
