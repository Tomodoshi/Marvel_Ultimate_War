package model.abilities;

import java.util.ArrayList;

import model.world.Damageable;

public class HealingAbility extends Ability {
	private int healAmount;

	public HealingAbility(String name, int cost, int baseCoolDown, int castRange, AreaOfEffect area, int required, int healAmount) {
		super(name, cost, baseCoolDown, castRange, area, required);
		this.healAmount = healAmount;
	}

	public int getHealAmount() {
		return healAmount;
	}

	public void setHealAmount(int healAmount) {
		this.healAmount = healAmount;
	}

	@Override
	public void execute(ArrayList<Damageable> targets) {
		for (Damageable target : targets) {
			target.setCurrentHP(target.getCurrentHP() + healAmount);
		}
	}

}
