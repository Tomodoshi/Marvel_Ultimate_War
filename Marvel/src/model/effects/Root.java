package model.effects;

import model.world.Champion;
import model.world.Condition;
public class Root extends Effect {

	public Root(int duration) {
		super("Root", duration, EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) {
		if(!(c.getCondition() == Condition.INACTIVE)){
			c.setCondition(Condition.ROOTED);
		}
	}

	@Override
	public void remove(Champion c) {
		if(c.getCondition() != Condition.INACTIVE) {
			int counter = 0;
			for (int i = 0; i <= c.getAppliedEffects().size(); i++) {
				Effect e = c.getAppliedEffects().get(i);
				if(e instanceof Root) {
					counter++;
				}
			}
			if(counter <= 1) {
				c.setCondition(Condition.ACTIVE);
				
			}else {
				c.setCondition(Condition.ROOTED);
			}
		}
	}
}
