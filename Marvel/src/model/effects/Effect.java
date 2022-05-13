package model.effects;
//import java.lang.Cloneable;

import model.world.Champion;

public abstract class Effect implements Cloneable {
	private String name;
	private int duration;
	private EffectType type;

	public Effect(String name, int duration, EffectType type) {
		this.name = name;
		this.duration = duration;
		this.type = type;
	}
	
	public Object clone() //overriding the needed method from the cloneable interface
		throws CloneNotSupportedException {
		return super.clone();
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public EffectType getType() {
		return type;
	}
	
	public abstract void apply(Champion c);
	
	public abstract void remove(Champion c);


}

