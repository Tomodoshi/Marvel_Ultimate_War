package model.world;

import java.util.ArrayList;
import engine.Player;
import model.abilities.Ability;
import model.effects.*;


import java.awt.Point;

public abstract class Champion implements Comparable , Damageable {
	private String name;
	private int maxHP;
	private int currentHP;
	private int mana;
	private int maxActionPointsPerTurn;
	private int currentActionPoints;
	private int attackRange;
	private int attackDamage;
	private int speed;
	private ArrayList<Ability> abilities;
	private ArrayList<Effect> appliedEffects;
	private Condition condition;
	private Point location;

	public Champion(String name, int maxHP, int mana, int maxActions, int speed, int attackRange, int attackDamage) {
		this.name = name;
		this.maxHP = maxHP;
		this.currentHP = maxHP;
		this.mana = mana;
		maxActionPointsPerTurn = maxActions;
		currentActionPoints = maxActions;
		this.speed = speed;
		this.attackRange = attackRange;
		this.attackDamage = attackDamage;
		condition = Condition.ACTIVE;
		abilities = new ArrayList<>();
		appliedEffects = new ArrayList<>();
	}
	
	public int compareTo(Object o) {
		if(o instanceof Champion) {
			if(this.speed > ((Champion)(o)).speed) {
				return -1;
			}else
				if(this.speed < ((Champion)(o)).speed) 
					return 1;
				else 
					if(this.name.compareTo(((Champion)(o)).name) < 0) {
						return -1;
					}
					else return 1;
		}else
			return 0;
	}
	
<<<<<<< HEAD
 	public abstract void useLeaderAbility(ArrayList<Champion> targets);
=======
	public abstract void useLeaderAbility(ArrayList<Champion> targets);
>>>>>>> 746b49927c47dc31c056f9a59990256673ca3bdb
	
	public int getCurrentHP() {
		return this.currentHP;
	}

	public void setCurrentHP(int currentHP) {
		if (currentHP < 0)
			this.currentHP = 0;
		else if (currentHP > maxHP)
			this.currentHP = maxHP;
		else
			this.currentHP = currentHP;
	}

	public int getMaxActionPointsPerTurn() {
		return maxActionPointsPerTurn;
	}

	public void setMaxActionPointsPerTurn(int maxActionPointsPerTurn) {
		this.maxActionPointsPerTurn = maxActionPointsPerTurn;
	}

	public int getAttackDamage() {
		System.out.println(14.1);
		return this.attackDamage;
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getCurrentActionPoints() {
		return currentActionPoints;
	}

	public void setCurrentActionPoints(int currentActionPoints) {
		if (currentActionPoints < 0)
			this.currentActionPoints = 0;
		else if (currentActionPoints > this.maxActionPointsPerTurn)
			this.currentActionPoints = this.maxActionPointsPerTurn;
		else
			this.currentActionPoints = currentActionPoints;
	}

	public int getAttackRange() {
		return attackRange;
	}

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public ArrayList<Effect> getAppliedEffects() {
		return appliedEffects;
	}

}
