package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;

import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Disarm;
import model.effects.Dodge;
import model.effects.Effect;
import model.effects.Embrace;
import model.effects.PowerUp;
import model.effects.Root;
import model.effects.Shield;
import model.effects.Shock;
import model.effects.Silence;
import model.effects.SpeedUp;
import model.effects.Stun;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Condition;
import model.world.Cover;
import model.world.Damageable;
import model.world.Direction;
import model.world.Hero;
import model.world.Villain;

public class Game {
	private Player firstPlayer;
	private Player secondPlayer;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private Object[][] board;
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private PriorityQueue turnOrder;
	private final static int BOARDHEIGHT = 5;
	private final static int BOARDWIDTH = 5;
	private ArrayList<Cover> covers;

	public Game(Player first, Player second) {
		turnOrder = new PriorityQueue(6);
		board = new Object[BOARDHEIGHT][BOARDWIDTH];
		this.firstPlayer = first;
		this.secondPlayer = second;
		availableChampions = new ArrayList<>();
		availableAbilities = new ArrayList<>();
		covers = new ArrayList<Cover>();
		placeChampions();
		placeCovers();
	}

	public Champion getCurrentChampion(){
		if(!turnOrder.isEmpty())
			return (Champion)(turnOrder.peekMin());
		else 
			return null;
	}

	public Player checkGameOver(){
		Boolean flag1 = false;
		Boolean flag2 = false;
		for(int i = 0; i < 3; i++){
			if((firstPlayer.getTeam().get(i)).getCondition() != Condition.KNOCKEDOUT){
				flag1 = true;
			}else{
				if((secondPlayer.getTeam().get(i)).getCondition() != Condition.KNOCKEDOUT){
					flag2 = true;
				}
			}
		}
		if(flag1 && flag2)
				return null;
			else if(!flag1)
				return secondPlayer;
			else
				return firstPlayer;
	}

	public void move(Direction d)throws UnallowedMovementException, NotEnoughResourcesException{
		Champion c = this.getCurrentChampion();
		if(c.getCurrentActionPoints() < 1){
			throw new NotEnoughResourcesException("Not enough action points to move");
		}
		if(c.getCondition() == Condition.ROOTED){
			throw new UnallowedMovementException();
		}
			Point t = c.getLocation();// else needed ?
		switch(d){
			case DOWN:
				t.y--;
				if((t.x >= 5 || t.x < 0 || t.y >= 5 || t.y < 0) &&board[t.y][t.x] != null  ){
					throw new UnallowedMovementException();
				}else
					c.setLocation(t);break;
			case UP: 
				t.y++;
				if((t.x >= 5 || t.x < 0 || t.y >= 5 || t.y < 0) &&board[t.y][t.x] != null ){
					throw new UnallowedMovementException();
				}else
					c.setLocation(t);break;
			case LEFT: 
				t.x--;
				if((t.x >= 5 || t.x < 0 || t.y >= 5 || t.y < 0) &&board[t.y][t.x] != null ){
					throw new UnallowedMovementException();
				}else
					c.setLocation(t);break;
			case RIGHT: 
				t.x++;
				if((t.x >= 5 || t.x < 0 || t.y >= 5 || t.y < 0) &&board[t.y][t.x] != null ){
					throw new UnallowedMovementException();
				}else
					c.setLocation(t);break;
		}
	}

	public void attack(Direction d) throws NotEnoughResourcesException, UnallowedMovementException, ChampionDisarmedException, InvalidAlgorithmParameterException{
		Champion c = this.getCurrentChampion();
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		int range = c.getAttackRange();
		
		if(c.getCurrentActionPoints() < 2){
			throw new NotEnoughResourcesException();
		}
		if(!c.getAppliedEffects().isEmpty()){
			for (Effect e : c.getAppliedEffects()) {
				if(e instanceof Disarm){
					throw new ChampionDisarmedException();
				}
			}
		}
		ArrayList temp = getSeq(range, d);
		if(temp.size() > 0 && temp.get(0) instanceof Cover){
			targets.add((Damageable)(temp.get(0)));
		}else if(temp.size() > 0 && temp.get(0) instanceof Champion){
			if(getFoe() == firstPlayer){
				if(firstPlayer.getTeam().contains(temp.get(0))){
					if(checkEffect((Champion)(temp.get(0)))){
						for (int i = 0; i < ((Champion)(temp.get(0))).getAppliedEffects().size(); i++) {
							Effect e = ((Champion)(temp.get(0))).getAppliedEffects().get(i);
							if(e instanceof Dodge){
								double var = Math.random();
								if(var >= 0.5){
									((Champion)(temp.get(0))).getAppliedEffects().remove(e);
									i--;
								}else{
									if(isBonusDmg((Champion)(temp.get(0)))){
										c.setAttackDamage((int)(c.getAttackDamage()*1.5));
										targets.add((Damageable)(temp.get(0)));
										c.setAttackDamage((int)(c.getAttackDamage()/1.5));
									}else{
										targets.add((Damageable)(temp.get(0)));
									}
								}
							}else if(e instanceof Shield){
								((Champion)(temp.get(0))).getAppliedEffects().remove(e);
								i--;
							}
						}
					}else{
						if(isBonusDmg((Champion)(temp.get(0)))){
							c.setAttackDamage((int)(c.getAttackDamage()*1.5));
							targets.add((Damageable)(temp.get(0)));
							c.setAttackDamage((int)(c.getAttackDamage()/1.5));
						}else{
							targets.add((Damageable)(temp.get(0)));
						}
					}
				}else{
					throw new InvalidAlgorithmParameterException();
				}
			}else{
				if(secondPlayer.getTeam().contains(temp.get(0))){
					if(checkEffect((Champion)(temp.get(0)))){
						for (int i = 0; i < ((Champion)(temp.get(0))).getAppliedEffects().size(); i++) {
							Effect e = ((Champion)(temp.get(0))).getAppliedEffects().get(i);
							if(e instanceof Dodge){
								double var = Math.random();
								if(var >= 0.5){
									((Champion)(temp.get(0))).getAppliedEffects().remove(e);
									i--;
								}else{
									if(isBonusDmg((Champion)(temp.get(0)))){
										c.setAttackDamage((int)(c.getAttackDamage()*1.5));
										targets.add((Damageable)(temp.get(0)));
										c.setAttackDamage((int)(c.getAttackDamage()/1.5));
									}else{
										targets.add((Damageable)(temp.get(0)));
									}
								}
							}else if(e instanceof Shield){
								((Champion)(temp.get(0))).getAppliedEffects().remove(e);
								i--;
							}
						}
					}else{
						if(isBonusDmg((Champion)(temp.get(0)))){
							c.setAttackDamage((int)(c.getAttackDamage()*1.5));
							targets.add((Damageable)(temp.get(0)));
							c.setAttackDamage((int)(c.getAttackDamage()/1.5));
						}else{
							targets.add((Damageable)(temp.get(0)));
						}
					}
				}
			}
		}

		for (int i = 0; i< covers.size(); i++) {
			Cover cov = covers.get(i);
			if(cov.getCurrentHP() == 0){
				covers.remove(cov);
				i--;
			}
		}
	}


	public void castAbility(Ability a)throws NotEnoughResourcesException, CloneNotSupportedException, AbilityUseException, InvalidTargetException{
		Champion c = this.getCurrentChampion();
		if(c.getCurrentActionPoints() < a.getRequiredActionPoints()){
			throw new NotEnoughResourcesException();
		}
		if(!c.getAppliedEffects().isEmpty()){
			for (Effect e : c.getAppliedEffects()) {
				if(e instanceof Silence){
					throw new AbilityUseException();
				}
			}
		}
		if(a.getCurrentCooldown() > 0){
			throw new AbilityUseException();
		}
		if(a.getManaCost() > c.getMana()){
			throw new AbilityUseException();
		}
		int range = a.getCastRange();
		ArrayList<Cover> cov = getCovers();
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		
		if(a instanceof DamagingAbility){
			if(getFoe() == firstPlayer){
				for (Champion d : firstPlayer.getTeam()) {
					if(d != c && getDistance(c.getLocation(), d.getLocation()) <= range){
						if(checkEffect(d)){
							for (int i = 0; i < d.getAppliedEffects().size(); i++) {
								Effect e = d.getAppliedEffects().get(i);
								if(e instanceof Shield){
									d.getAppliedEffects().remove(e);
									i--;
								}
							}
						}else{
							targets.add(d);
						}
					}else{
						throw new InvalidTargetException();
					}
				}
			}else{
				for (Champion d : secondPlayer.getTeam()) {
					if(d != c && getDistance(c.getLocation(), d.getLocation()) <= range){
						if(checkEffect(d)){
							for (int i = 0; i < d.getAppliedEffects().size(); i++) {
								Effect e = d.getAppliedEffects().get(i);
								if(e instanceof Shield){
									d.getAppliedEffects().remove(e);
									i--;
								}
							}
						}else{
							targets.add(d);
						}
					}else{
						throw new InvalidTargetException();
					}
				}
			}
				for (Cover co: cov){
					int dist =  getDistance(co.getLocation(), c.getLocation());
					if(dist <= range)
						targets.add(co);
				}

			((DamagingAbility)(a)).execute(targets);
		}
		else{
			if(a instanceof HealingAbility){
				if(a.getCastArea() == AreaOfEffect.SELFTARGET){
					targets.add((Damageable)(c));
				}
				else if(getFoe() == firstPlayer){
					for (Champion m : secondPlayer.getTeam()) {
						if(m != c && getDistance(m.getLocation(), c.getLocation()) <= range){
							targets.add(m);
						}
					}
				}
				else{
					for (Champion champ : secondPlayer.getTeam()) {
						if(champ != c && getDistance(champ.getLocation(), c.getLocation()) <= range){
							targets.add(champ);
						}
					}
				}
			}
			else{
				if(getFoe() == firstPlayer){
					for (Champion d : firstPlayer.getTeam()) {
						if(d != c && getDistance(c.getLocation(), d.getLocation()) <= range)
							targets.add(d);
					}
				}
				else{
					for (Champion d : secondPlayer.getTeam()) {
						if(d != c && getDistance(c.getLocation(), d.getLocation()) <= range)
							targets.add(d);
					}
				}
				
				((CrowdControlAbility)(a)).execute(targets);
			}
		}

		for (int i = 0; i< covers.size(); i++) {
			Cover cove = covers.get(i);
			if(cove.getCurrentHP() == 0){
				covers.remove(cove);
				i--;
			}
		}
	}

	public void castAbility(Ability a, Direction d) throws NotEnoughResourcesException, UnallowedMovementException, CloneNotSupportedException, AbilityUseException{
		Champion c = getCurrentChampion();
		if(c.getCurrentActionPoints() < a.getRequiredActionPoints()){
			throw new NotEnoughResourcesException();
		}
		if(a.getManaCost() > c.getMana()){
			throw new AbilityUseException();
		}
		
		if(!c.getAppliedEffects().isEmpty()){
			for (Effect e : c.getAppliedEffects()) {
				if(e instanceof Silence){
					throw new AbilityUseException();
				}
			}
		}

		if(a.getCurrentCooldown() > 0){
			throw new AbilityUseException();
		}

		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		int range = a.getCastRange();
		if(getFoe() == firstPlayer)
			if(a instanceof HealingAbility){
				for (Object o : getSeq(range, d)) {
					if(o instanceof Champion && secondPlayer.getTeam().contains((Champion)(o)))
						targets.add((Champion)(o));
				}
				((HealingAbility)(a)).execute(targets);
			}
				else
					if(a instanceof DamagingAbility){
						for (Object o : getSeq(range, d)) {
							if(o instanceof Cover){
								targets.add((Cover)(o));
							}else if(o instanceof Champion && firstPlayer.getTeam().contains((Champion)(o)))
								if(checkEffect((Champion)(o))){
									for (Effect e : ((Champion)(o)).getAppliedEffects()){
										if(e instanceof Shield){
											((Champion)(o)).getAppliedEffects().remove(e);										
										}
									}
								}else{
									targets.add((Champion)(o));
								}
						}
						((DamagingAbility)(a)).execute(targets);
					}else{
						for (Object o : getSeq(range, d)) {
							if(o instanceof Champion && firstPlayer.getTeam().contains((Champion)(o)))
								targets.add((Champion)(o));
						}
						((CrowdControlAbility)(a)).execute(targets);
					}
		else
			if(a instanceof HealingAbility){
				for (Object o : getSeq(range, d)) {
					if(o instanceof Champion && firstPlayer.getTeam().contains((Champion)(o)))
						targets.add((Champion)(o));
				}
				((HealingAbility)(a)).execute(targets);
			}
			else
				if(a instanceof DamagingAbility){
					for (Object o : getSeq(range, d)) {
						if(o instanceof Cover){
							targets.add((Cover)(o));
						}else if(o instanceof Champion && secondPlayer.getTeam().contains((Champion)(o)))
							if(checkEffect((Champion)(o))){
								for (Effect e : ((Champion)(o)).getAppliedEffects()){
									if(e instanceof Shield){
										((Champion)(o)).getAppliedEffects().remove(e);										
									}
								}
							}else{
								targets.add((Champion)(o));
							}
					}
					((DamagingAbility)(a)).execute(targets);
				}else{
					for (Object o : getSeq(range, d)) {
						if(o instanceof Champion && secondPlayer.getTeam().contains((Champion)(o)))
							targets.add((Champion)(o));
					}
					((CrowdControlAbility)(a)).execute(targets);
				}
				

		for (int i = 0; i < covers.size(); i++) {
			Cover civic = covers.get(i);
			if(civic.getCurrentHP() == 0){
				covers.remove(civic);
				i--;
			}
		}
	}

	public void castAbility(Ability a, int x, int y) throws NotEnoughResourcesException, CloneNotSupportedException, AbilityUseException{
		Champion c = getCurrentChampion();
		if(c.getCurrentActionPoints() < a.getRequiredActionPoints()){
			throw new NotEnoughResourcesException();
		}
		if(a.getManaCost() > c.getMana()){
			throw new AbilityUseException();
		}
		if(!c.getAppliedEffects().isEmpty()){
			for (Effect e : c.getAppliedEffects()) {
				if(e instanceof Silence){
					throw new AbilityUseException();
				}
			}
		}
		if(a.getCurrentCooldown() > 0){
			throw new AbilityUseException();
		}
		int dist = getDistance(c.getLocation(), new Point(y,x));
		int range = a.getCastRange();
		ArrayList<Damageable> targets = new ArrayList<Damageable>();

		if(a instanceof HealingAbility){
			if(board[y][x] instanceof Champion){
				if(getFoe() == firstPlayer){
					if(secondPlayer.getTeam().contains(((Champion)(board[y][x]))) && dist <= range)
						targets.add(((Champion)(board[y][x])));
				}else
					if(getFoe() == secondPlayer){
						if(firstPlayer.getTeam().contains(((Champion)(board[y][x]))) && dist <= range)
							targets.add(((Champion)(board[y][x])));
					}
			}
			((HealingAbility)(a)).execute(targets);
		}else
			if(a instanceof DamagingAbility){
				if(board[y][x] instanceof Cover && dist <= range)
					targets.add((Cover)(board[y][x]));
				else{
					if(board[y][x] instanceof Champion){
						if(getFoe() == firstPlayer)
							if(firstPlayer.getTeam().contains((Champion)(board[y][x])) && dist <= range){
								if(checkEffect((Champion)(board[y][x]))){
									for (int i = 0; i < ((Champion)(board[y][x])).getAppliedEffects().size(); i++) {
										Effect e = ((Champion)(board[y][x])).getAppliedEffects().get(i);
										if(e instanceof Shield){
											((Champion)(board[y][x])).getAppliedEffects().remove(e);
											i--;
										}
									}
								}else{
									targets.add((Champion)(board[y][x]));
								}
							}
						if(getFoe() == secondPlayer)
							if(secondPlayer.getTeam().contains((Champion)(board[y][x])) && dist <= range){
								if(checkEffect((Champion)(board[y][x]))){
									for (int i = 0; i < ((Champion)(board[y][x])).getAppliedEffects().size(); i++) {
										Effect e = ((Champion)(board[y][x])).getAppliedEffects().get(i);
										if(e instanceof Shield){
											((Champion)(board[y][x])).getAppliedEffects().remove(e);
											i--;
										}
									}
								}else{
									targets.add((Champion)(board[y][x]));
								}
							}
					((DamagingAbility)(a)).execute(targets);
					}else
						if(a instanceof CrowdControlAbility){
							if(board [y][x] instanceof Champion)
								if(getFoe() == firstPlayer)
									if(firstPlayer.getTeam().contains((Champion)(board[y][x])) && dist <= range){
										targets.add((Champion)(board[y][x]));
									}

								if(getFoe() == secondPlayer)
									if(secondPlayer.getTeam().contains((Champion)(board[y][x])) && dist <= range)
										targets.add((Champion)(board[y][x]));
							((CrowdControlAbility)(a)).execute(targets);
						}
					}
				}
		for (int i = 0; i< covers.size(); i++) {
			Cover cov1 = covers.get(i);
			if(cov1.getCurrentHP() == 0){
				covers.remove(cov1);
				i--;
			}
		}
	}

	public void useLeaderAbility() throws LeaderNotCurrentException, LeaderAbilityAlreadyUsedException{
		Champion c = getCurrentChampion();
		ArrayList<Champion> targets = new ArrayList<Champion>();

		if(getFoe() == firstPlayer){
			if(c != secondPlayer.getLeader())
				throw new LeaderNotCurrentException();
			if(secondLeaderAbilityUsed)
				throw new LeaderAbilityAlreadyUsedException();
			
			if(c instanceof Hero){
				targets = secondPlayer.getTeam();
				c.useLeaderAbility(targets);
			}
			if(c instanceof Villain){
				targets = secondPlayer.getTeam();
				c.useLeaderAbility(targets);
			}else{
				for(int i = 0; i < 3; i++){
					if(secondPlayer.getTeam().get(i) != secondPlayer.getLeader())
						targets.add(secondPlayer.getTeam().get(i));

					if(firstPlayer.getTeam().get(i) != firstPlayer.getLeader())
						targets.add(firstPlayer.getTeam().get(i));
				}
				c.useLeaderAbility(targets);
			}
			secondLeaderAbilityUsed = true;
		}else{
			if(c != firstPlayer.getLeader())
				throw new LeaderNotCurrentException();
			if(firstLeaderAbilityUsed)
				throw new LeaderAbilityAlreadyUsedException();
			
			if(c instanceof Hero){
				targets = firstPlayer.getTeam();
				c.useLeaderAbility(targets);
			}
			if(c instanceof Villain){
				targets = firstPlayer.getTeam();
				c.useLeaderAbility(targets);
			}else{
				for(int i = 0; i < 3; i++){
					if(secondPlayer.getTeam().get(i) != secondPlayer.getLeader())
						targets.add(secondPlayer.getTeam().get(i));

					if(firstPlayer.getTeam().get(i) != firstPlayer.getLeader())
						targets.add(firstPlayer.getTeam().get(i));
				}
				c.useLeaderAbility(targets);
			}
			firstLeaderAbilityUsed = true;
		}	
	}

	public void endTurn(){
		
			do{
				turnOrder.remove();
				if(turnOrder.isEmpty())
					prepareChampionTurns();
				Champion c = getCurrentChampion();
				c.setCurrentActionPoints(c.getMaxActionPointsPerTurn());
				for (Ability a : c.getAbilities()) {
					a.setCurrentCooldown(a.getCurrentCooldown() - 1);
				}
				for(int i = 0; i < c.getAppliedEffects().size(); i++) {
					Effect e = c.getAppliedEffects().get(i);
					e.setDuration(e.getDuration() - 1);
					if(e.getDuration() == 0) {
						c.getAppliedEffects().remove(i);
						e.remove(c);
						i--;
					}
				}
				
				
			}while(getCurrentChampion().getCondition() == Condition.INACTIVE);

	}

	private void prepareChampionTurns(){
		for(int i = 0; i < 3; i++){
			if(firstPlayer.getTeam().get(i).getCondition() != Condition.KNOCKEDOUT)
				turnOrder.insert(firstPlayer.getTeam().get(i));
			if(secondPlayer.getTeam().get(i).getCondition() != Condition.KNOCKEDOUT)
				turnOrder.insert(secondPlayer.getTeam().get(i));
		}
	}
	

	// ----------------------------------------Helper Methods--------------------------------------

	private Boolean isDamageable(int y, int x){
		if(board[y][x] instanceof Champion || board[y][x] instanceof Cover)
			return true;
		return false;
	}

	private static int getDistance(Point p1, Point p2){
		return  (int)(Math.abs(p1.getX() - p2.getX()) - (int)(Math.abs(p1.getY() - p2.getY())));
	}

	private Player getFoe(){
		Champion c = getCurrentChampion();
		if(firstPlayer.getTeam().contains(c))
			return secondPlayer;
		else
			return firstPlayer;
	}

	private Boolean isFoe(){
		Champion c = getCurrentChampion();
		if(firstPlayer.getTeam().contains(c)){
			return false;
		}else
			return true;
	}

	private void prepCoversMethod(){
		for(int i = 0; i < 5; i++){
			for (int j = 0; j < 5; j++) {
				if(board[j][i] instanceof Cover){
					covers.add((Cover)(board[j][i]));
				}
			}
		}
	}

	private ArrayList getCovers(){
		return covers;
	}

	private Boolean isBonusDmg(Champion d){
		Champion c = getCurrentChampion();
		if(c instanceof Hero && d instanceof Villain){
			return true;
		}else if(c instanceof Villain && d instanceof Hero){
			return true;
		}

		if(c instanceof AntiHero && d instanceof Villain){
			return true;
		}else if(c instanceof Villain && d instanceof AntiHero){
			return true;
		}

		if(c instanceof AntiHero && d instanceof Hero){
			return true;
		}else if(c instanceof Hero && d instanceof AntiHero){
			return true;
		}

		return false;
	}

	private ArrayList getSeq(int range, Direction d) throws UnallowedMovementException{
		ArrayList<Object> o = new ArrayList<>();
		Champion c = getCurrentChampion();
		Point t = c.getLocation();
		for(int i = 1; i <= range; i++){
			switch(d){
				case DOWN:
					t.y--;
					if(!(t.x >= 5 || t.x < 0 || t.y >= 5 || t.y < 0) && board[t.y][t.x] != null)
						o.add(board[t.y][t.x]);
					break;
				case LEFT:
					t.x--;
					if(!(t.x >= 5 || t.x < 0 || t.y >= 5 || t.y < 0) && board[t.y][t.x] != null)
						o.add(board[t.y][t.x]);
					break;
				case RIGHT:
					t.x++;
					if(!(t.x >= 5 || t.x < 0 || t.y >= 5 || t.y < 0) && board[t.y][t.x] != null)
						o.add(board[t.y][t.x]);
					break;
				case UP:
					t.y++;
					if(!(t.x >= 5 || t.x < 0 || t.y >= 5 || t.y < 0) && board[t.y][t.x] != null)
						o.add(board[t.y][t.x]);
					break;
			}
		}
		return o;
	}

	private static Boolean checkEffect(Champion s){
		if(s.getAppliedEffects().isEmpty())
			return true;
		return false;
	}

	// --------------------------------------------------------------------------------------------


	public void placeChampions() {

		for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
			board[0][i + 1] = firstPlayer.getTeam().get(i);
			firstPlayer.getTeam().get(i).setLocation(new Point(0, i + 1));
			board[4][i + 1] = secondPlayer.getTeam().get(i);
			secondPlayer.getTeam().get(i).setLocation(new Point(4, i + 1));

		}
	}

	private void placeCovers() {
		int counter = 0;
		while (counter < 5) {
			int y = (int) (Math.random() * 3) + 1;
			int x = (int) (Math.random() * 5) + 0;
			if (board[y][x] == null) {
				Cover c = new Cover(y, x);
				board[y][x] = c;
				counter++;
			}
		}
	}

	public static Ability locateAbility(String s) {
		int index = 0;
		for (int i = 0; i < availableAbilities.size(); i++) {
			if (availableAbilities.get(i).getName().equals(s)) {
				index = i;
				break;
			}
		}
		return availableAbilities.get(index);
	}

	public static void loadChampions(String filePath) throws IOException {
		availableChampions = new ArrayList<Champion>();
		String currentLine = "";
		FileReader fileReader = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fileReader);
		while ((currentLine = br.readLine()) != null) {
			String[] result = currentLine.split((","));
			String name = result[1];
			int maxHP = Integer.parseInt(result[2]);
			int mana = Integer.parseInt(result[3]);
			int actions = Integer.parseInt(result[4]);
			int speed = Integer.parseInt(result[5]);
			int attackRange = Integer.parseInt(result[6]);
			int attackDamage = Integer.parseInt(result[7]);
			String ability1 = result[8];
			String ability2 = result[9];
			String ability3 = result[10];

			if (result[0].charAt(0) == 'H') {
				Hero h = new Hero(name, maxHP, mana, actions, speed, attackRange, attackDamage);

				h.getAbilities().add(locateAbility(ability1));
				h.getAbilities().add(locateAbility(ability2));
				h.getAbilities().add(locateAbility(ability3));

				availableChampions.add(h);
			} else if (result[0].charAt(0) == 'A') {
				AntiHero a = new AntiHero(name, maxHP, mana, actions, speed, attackRange, attackDamage);

				a.getAbilities().add(locateAbility(ability1));
				a.getAbilities().add(locateAbility(ability2));
				a.getAbilities().add(locateAbility(ability3));
				availableChampions.add(a);
			} else {
				Villain v = new Villain(name, maxHP, mana, actions, speed, attackRange, attackDamage);

				v.getAbilities().add(locateAbility(ability1));
				v.getAbilities().add(locateAbility(ability2));
				v.getAbilities().add(locateAbility(ability3));
				availableChampions.add(v);
			}

		}
		br.close();
	}

	public static void loadAbilities(String filePath) throws IOException {
		availableAbilities = new ArrayList<Ability>();
		String currentLine = "";
		FileReader fileReader = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fileReader);
		while ((currentLine = br.readLine()) != null) {
			String[] result = currentLine.split((","));
			Ability c;
			if (result[0].equals("DMG")) {
				c = new DamagingAbility(result[1], Integer.parseInt(result[2]), Integer.parseInt(result[4]),
						Integer.parseInt(result[3]), AreaOfEffect.valueOf(result[5]), Integer.parseInt(result[6]),
						Integer.parseInt(result[7]));

			} else if (result[0].equals("HEL")) {
				c = new HealingAbility(result[1], Integer.parseInt(result[2]), Integer.parseInt(result[4]),
						Integer.parseInt(result[3]), AreaOfEffect.valueOf(result[5]), Integer.parseInt(result[6]),
						Integer.parseInt(result[7]));

			} else {
				Effect p = createEffect(result[7], Integer.parseInt(result[8]));

				c = new CrowdControlAbility(result[1], Integer.parseInt(result[2]), Integer.parseInt(result[4]),
						Integer.parseInt(result[3]), AreaOfEffect.valueOf(result[5]), Integer.parseInt(result[6]), p);
			}
			availableAbilities.add(c);
		}
		br.close();
	}

	private static Effect createEffect(String name, int duration) {
		Effect p = null;
		switch (name) {
		case "Shield":
			p = new Shield(duration);
			break;
		case "PowerUp":
			p = new PowerUp(duration);
			break;
		case "SpeedUp":
			p = new SpeedUp(duration);
			break;
		case "Embrace":
			p = new Embrace(duration);
			break;
		case "Dodge":
			p = new Dodge(duration);
			break;
		case "Disarm":
			p = new Disarm(duration);
			break;
		case "Silence":
			p = new Silence(duration);
			break;
		case "Root":
			p = new Root(duration);
			break;
		case "Shock":
			p = new Shock(duration);
			break;
		case "Stun":
			p = new Stun(duration);
			break;
		}
		return p;
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}

	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}

	public Object[][] getBoard() {
		return board;
	}

	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public static int getBoardheight() {
		return BOARDHEIGHT;
	}

	public static int getBoardwidth() {
		return BOARDWIDTH;
	}

}
