package model.world;

import java.util.ArrayList;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;

public class Hero extends Champion {

	public Hero(String name, int maxHP, int mana, int maxActions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, mana, maxActions, speed, attackRange, attackDamage);
	}
	
	@Override
	public void useLeaderAbility(ArrayList<Champion> targets) {
		for (Champion c : targets) {
			for (int i = 0; i < c.getAppliedEffects().size(); i++) {
				Effect e = c.getAppliedEffects().get(i);
				if(e.getType() == EffectType.DEBUFF){
					c.getAppliedEffects().remove(e);
					i--;
				}
			}
			Embrace Emb = new Embrace(2);
			c.getAppliedEffects().add(Emb);
		}

	}
}
