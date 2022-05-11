package exceptions;

public class ChampionDisarmedException extends GameActionException {
	public ChampionDisarmedException() {
		super();
	}

	public ChampionDisarmedException(String s) {
		super();
		System.out.println(s);
	}
}
