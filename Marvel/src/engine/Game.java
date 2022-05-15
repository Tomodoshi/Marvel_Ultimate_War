package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.*;
import model.effects.*;
import model.world.*;

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

	public Game(Player first, Player second) {
		turnOrder = new PriorityQueue(6);
		board = new Object[BOARDHEIGHT][BOARDWIDTH];
		this.firstPlayer = first;
		this.secondPlayer = second;
		availableChampions = new ArrayList<>();
		availableAbilities = new ArrayList<>();
		placeChampions();
		placeCovers();
	}

	public Champion getCurrentChampion(){
		if(!turnOrder.isEmpty())
			return (Champion)(turnOrder.remove());
		else 
			return null;
	}

	public Player checkGameOver(){
		if(firstPlayer.getTeam().isEmpty()) 
			return secondPlayer;
		else
			if(secondPlayer.getTeam().isEmpty())
				return firstPlayer;
			else
				return null;
	}

	public void move(Direction d)throws UnallowedMovementException{
		Champion c = this.getCurrentChampion();
		Point t = c.getLocation();
		switch(d){
			case DOWN:
				t.y--;
				c.setLocation(t);break;
			case UP: 
				t.y++;
				c.setLocation(t);break;
			case LEFT: 
				t.x--;
				c.setLocation(t);break;
			case RIGHT: 
				t.x++;
				c.setLocation(t);break;
		}
	}

	public void attack(Direction d){
		Champion c = this.getCurrentChampion();
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		int range = c.getAttackRange();
		switch(d){
			case DOWN:
				for(int i = 1; i <= range; i++){
					if(isDamageable(c.getLocation().y-i, c.getLocation().x))
						targets.add(((Damageable)(board[c.getLocation().y-i][c.getLocation().x])));
				}break;
			case LEFT:
				for(int i = 1; i <= range; i++){
					if(isDamageable(c.getLocation().y-i, c.getLocation().x))
						targets.add(((Damageable)(board[c.getLocation().y][c.getLocation().x-i])));
				}break;
			case RIGHT:
				for(int i = 1; i <= range; i++){
					if(isDamageable(c.getLocation().y-i, c.getLocation().x))
						targets.add(((Damageable)(board[c.getLocation().y][c.getLocation().x+i])));
				}break;
			case UP:
				for(int i = 1; i <= range; i++){
					if(isDamageable(c.getLocation().y-i, c.getLocation().x))
						targets.add(((Damageable)(board[c.getLocation().y+i][c.getLocation().x])));
				}
		}
	}

	public void castAbility(Ability a)throws NotEnoughResourcesException{
		Champion c = this.getCurrentChampion();
		int range = c.getAttackRange();
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		if(a instanceof DamagingAbility){
			
			((DamagingAbility)(a)).execute(targets);throw new NotEnoughResourcesException();
		}
		else
			if(a instanceof HealingAbility)
				if(firstPlayer.getTeam().contains(c)){
					for (Champion champ : firstPlayer.getTeam()) {
						if(champ != c && getDistance(champ.getLocation(), c.getLocation()) <= c.getAttackRange())
							targets.add(champ);
					}
					((HealingAbility)(a)).execute(targets);throw new NotEnoughResourcesException();
				}else{
					for (Champion champ : secondPlayer.getTeam()) {
						if(champ != c && getDistance(champ.getLocation(), c.getLocation()) <= c.getAttackRange())
							targets.add(champ);
					}
					((HealingAbility)(a)).execute(targets);throw new NotEnoughResourcesException();
				}
	}

	public void castAbility(Ability a, int x, int y){
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		if(a instanceof HealingAbility){
			if(board[y][x] instanceof Champion){
				targets.add((Champion)(board[y][x]));
				((HealingAbility)(a)).execute(targets);
			}
		}
	}

	// -----------------------------------Helper Methods--------------------------------------

	public Boolean isDamageable(int y, int x){
		if(board[y][x] instanceof Champion || board[y][x] instanceof Cover)
			return true;
		return false;
	}

<<<<<<< HEAD
	public void castAbility(Ability a)throws NotEnoughResourcesException{
		Champion c = this.getCurrentChampion();
		int range = c.getAttackRange();
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		if(a instanceof DamagingAbility){
			for(int i = range*-1; i < range;i++){
				for(int j = range*-1; i < range; i++){
					if(i != 0 && j != 0){
						if(isDamageable(c.getLocation().y-i, c.getLocation().x-j)){
							targets.add((Damageable)(board[c.getLocation().y-i][c.getLocation().x-j]));
						}
					}
				}
			}
			((DamagingAbility)(a)).execute(targets);throw new NotEnoughResourcesException();
		}
		else
			if(a instanceof HealingAbility){
				for(int i = range*-1; i < range;i++){
					for(int j = range*-1; i < range; i++){
						if(i != 0 && j != 0){
							if(board[c.getLocation().y-i][c.getLocation().x-j] instanceof Champion){
								targets.add((Damageable)(board[c.getLocation().y-i][c.getLocation().x-j]));
							}
						} 
					}
				}
				((HealingAbility)(a)).execute(targets);throw new NotEnoughResourcesException();
			}
=======
	public static int getDistance(Point p1, Point p2){
		return  (int)(Math.abs(p1.getX() - p2.getX()) - (int)(Math.abs(p1.getY() - p2.getY())));
>>>>>>> 323a434fbb615503c80de93162b82093b4a74d6a
	}
	
	
	public void castAbility(Ability a, Direction d)throws NotEnoughResourcesException{
		if(a instanceof AreaofEffect.DIRECTIONAL ) {
			
		}
	}

	// ---------------------------------------------------------------------------------------


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
