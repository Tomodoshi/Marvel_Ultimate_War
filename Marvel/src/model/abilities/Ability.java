package model.abilities;

import java.util.ArrayList;

import model.world.Cover;
import model.world.Damageable;
import model.world.Champion;
import model.abilities.DamagingAbility;

public abstract class Ability {
	private String name;
	private int manaCost;
	private int baseCooldown;
	private int currentCooldown; //  and write
	private int castRange;
	private int requiredActionPoints;
	private AreaOfEffect castArea;

	public Ability(String name, int cost, int baseCoolDown, int castRange, AreaOfEffect area, int required) {
		this.name = name;
		this.manaCost = cost;
		this.baseCooldown = baseCoolDown;
		this.castRange = castRange;
		this.castArea = area;
		this.requiredActionPoints = required;

	}
	
	public abstract void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException;

	public String getName() {
		return name;
	}

	public int getCurrentCooldown() {
		return currentCooldown;
	}

	public void setCurrentCooldown(int currentCooldown) {
		if (currentCooldown < 0)
			this.baseCooldown = 0;
		else if (currentCooldown > this.baseCooldown)
			this.currentCooldown = this.baseCooldown;
		else
			this.currentCooldown = currentCooldown;
	}

	public int getManaCost() {
		return manaCost;
	}

	public int getBaseCooldown() {
		return baseCooldown;
	}

	public int getCastRange() {
		return castRange;
	}

	public int getRequiredActionPoints() {
		return requiredActionPoints;
	}

	public AreaOfEffect getCastArea() {
		return castArea;
	}

}
