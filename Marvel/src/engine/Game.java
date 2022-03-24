package engine;

import model.world.Champion;
import model.abilities.Ability;
import model.world.Cover;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.String;

public class Game {
	private Player firstPlayer;
	private Player secondPlayer;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private Object[][] board;
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private PriorityQueue turnOrder;
	private final static int BOARDHEIGHT = 0;
	private final static int BOARDWIDTH = 0;

	public Game(Player first, Player second) {
		board = new Object[5][5];
		this.firstPlayer = first;
		this.secondPlayer = second;
		placeChampion();
		placeCovers();
	}
	
	private void placeChampion(){
		//add Champions here
	}
	
	private void placeCovers(){
		//add Covers here
	}
	
	public static void loadChampions(String filePath) throws IOException {
		String currentLine = "";
		FileReader fileReader = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fileReader);
		while ((currentLine = br.readLine())!= null) {
			String [] result = currentLine.split((","));
			//add Champions from CSV
		}
		br.close();
	}
	
	public static void loadAbilities(String filePath) throws IOException {
		String currentLine = "";
		FileReader fileReader = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fileReader);
		while ((currentLine = br.readLine())!= null) {
			String [] result = currentLine.split((","));
			//add abilities from CSV
		}
		br.close();
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
