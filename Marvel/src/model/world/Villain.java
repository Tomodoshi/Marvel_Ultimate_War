package model.world;
import java.util.ArrayList;

import java.util.ArrayList;

public class Villain extends Champion {

	public Villain(String name, int maxHP, int mana, int maxActions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, mana, maxActions, speed, attackRange, attackDamage);
	}
<<<<<<< HEAD
	
=======

>>>>>>> 746b49927c47dc31c056f9a59990256673ca3bdb
	@Override
	public void useLeaderAbility(ArrayList<Champion> targets) {
		for (Champion c : targets) {
			if(c.getCurrentHP()/c.getMaxHP() < 0.3){
				c.setCurrentHP(0);
				c.setCondition(Condition.KNOCKEDOUT);
			}
		}
<<<<<<< HEAD

=======
		
>>>>>>> 746b49927c47dc31c056f9a59990256673ca3bdb
	}
}
