package game;

public class GameChange {
	
	public static final int STATE = 1;
	public static final int TURN = 2;
	public static final int PICKED = 3;
	public static final int RESERVE = 4;
	public static final int BOARD = 5;
	
	public final int type;
	public final int value;
	
	public GameChange(int type, int value) {
		this.type = type;
		this.value = value;
	}

}
