package model.world;
import java.util.ArrayList;
import model.effects.Stun;

import java.util.ArrayList;

import model.effects.Stun;



public class AntiHero extends Champion {

	public AntiHero(String name, int maxHP, int mana, int maxActions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, mana, maxActions, speed, attackRange, attackDamage);
	}
<<<<<<< HEAD
	
	
=======

>>>>>>> 746b49927c47dc31c056f9a59990256673ca3bdb
	@Override
	public void useLeaderAbility(ArrayList<Champion> targets) {
		for (int i = 0; i < targets.size(); i++) {
			Stun s = new Stun(2);
			(targets.get(i)).getAppliedEffects().add(s);
		}
	}
}
