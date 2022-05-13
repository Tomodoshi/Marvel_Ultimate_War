package model.effects;

import model.abilities.Ability;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;

public class PowerUp extends Effect {

	public PowerUp(int duration) {
		super("PowerUp", duration, EffectType.BUFF);
	}

	@Override
	public void apply(Champion c) {
		for (Ability a : c.getAbilities()) {
			
			if (a instanceof DamagingAbility) {
				int temp = ((DamagingAbility) (a)).getDamageAmount();
				((DamagingAbility) (a)).setDamageAmount((int) (temp * 1.2));
				
			}
			
			if (a instanceof HealingAbility) {
				int temp = ((HealingAbility) (a)).getHealAmount();
				((HealingAbility) (a)).setHealAmount((int) (temp * 1.2));

			}

		}

	}

	@Override
	public void remove(Champion c) {
		for (Ability a : c.getAbilities()) {
			
			if (a instanceof DamagingAbility) {
				int temp = ((DamagingAbility) (a)).getDamageAmount();
				((DamagingAbility) (a)).setDamageAmount((int) (temp / 1.2));
				
			}
			
			if (a instanceof HealingAbility) {
				int temp = ((HealingAbility) (a)).getHealAmount();
				((HealingAbility) (a)).setHealAmount((int) (temp / 1.2));

			}

		}

		
	}
}
