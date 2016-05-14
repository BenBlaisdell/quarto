package game;

import java.util.ArrayList;
import java.util.Observable;

import game.traits.CenterTrait;
import game.traits.ColorTrait;
import game.traits.HeightTrait;
import game.traits.ShapeTrait;

/**
 * A representation of a Quarto board.
 * Coordinates are in the form (x, y) with (0, 0) at the top left corner.
 * 
 * @author Ben
 */
public class Game extends Observable {

	
	/** 
	 * Number of pieces per side of the board
	 */
	public static final int SIDE_LENGTH = 4;
	
	
	/**
	 * Total number of Quarto pieces
	 */
	public static final int NUM_PIECES = SIDE_LENGTH * SIDE_LENGTH;
	
	public static final int STATE_PICKING = 1;
	public static final int STATE_PLACING = 2;
	public static final int STATE_NONE = 3;
	public static final int STATE_END = 4;
	
	public static final int P1 = 1;
	public static final int P2 = 2;
	
	enum QuartoGroup {
		
	}

	
	private boolean sqareWin;
	
	private Piece[] reserve;
	private Board board;
	
	private int state;
	
	private int turn;
		
	private Piece pickedPiece;
		
	/**
	 * Creates an empty <code>Board</code> and initializes the standard <code>Piece</code>s.
	 */
	public Game() {
		state = STATE_NONE;
	}
	
	public void newGame(boolean squareWin) {
		newGame(P1, squareWin);
	}
	
	public void forfeit() {
		changeState(STATE_END);
		
		ArrayList<GameChange> changes = new ArrayList<GameChange>();
		changes.add(new GameChange(GameChange.STATE, state));
		
		setChanged();
		notifyObservers(changes);

	}
	
	public void newGame(int turn, boolean squareWin) {
		this.sqareWin = squareWin;
		board = new Board();
		reserve = new Piece[NUM_PIECES];
		initBoard();
		initReserve();
		pickedPiece = null;
		this.turn = turn;
		state = STATE_PICKING;
		
		ArrayList<GameChange> changes = new ArrayList<GameChange>();
		
		for (int i = 0; i < NUM_PIECES; i++) {
			changes.add(new GameChange(GameChange.BOARD, i));
			changes.add(new GameChange(GameChange.RESERVE, i));
		}
		
		changes.add(new GameChange(GameChange.PICKED, 0));
		changes.add(new GameChange(GameChange.TURN, turn));
		changes.add(new GameChange(GameChange.STATE, state));
		
		setChanged();
		notifyObservers(changes);
	}
	
	private void initBoard() {
		for (int x = 0; x < SIDE_LENGTH; x++) {
			for (int y = 0; y < SIDE_LENGTH; y++) {
				putPiece(null, x, y);
			}
		}
	}
	
	public int getState() {
		return state;
	}

	private void initReserve() {
		
		int pieceIndex = 0;
		
		for (CenterTrait center : CenterTrait.values()) {
			for (ColorTrait color : ColorTrait.values()) {
				for (HeightTrait height : HeightTrait.values()) {
					for (ShapeTrait shape : ShapeTrait.values()) {
						
						Piece piece = new Piece(center, color, height, shape);
						reserve[pieceIndex++] = piece;
						
					}
				}
			}
		}
			
	}

	/**
	 * @param x The x coordinate from which to get the <code>Piece</code>
	 * @param y The y coordinate from which to get the <code>Piece</code>
	 * @return The Piece at (x, y)
	 */
	public final Piece getBoardPiece(int x, int y) {
		return board.getPiece(15 - (4 * y) - x);
	}
	
	public Piece getReservePiece(int index) {
		return reserve[index];
	}
	
	/**
	 * @param x The x coordinate to check
	 * @param y The y coordinate to check
	 * @return If there is some <code>Piece</code> at this point on the <code>Board</code>
	 */
	public boolean isPieceAt(int x, int y) {
		return (getBoardPiece(x, y) != null);
	}
	
	/**
	 * Places a Piece at (x, y)
	 * 
	 * @param piece The <code>Piece</code> to place
	 * @param x The x coordinate at which to place the <code>Piece</code>
	 * @param y The y coordinate at which to place the <code>Piece</code>
	 */
	public void putPiece(Piece piece, int x, int y) {
		board.putPiece(piece, 15 - (4 * y) - x);
	}
	
	public void replacePiece(Piece piece, int index) {
		reserve[index] = piece;
	}
		
	/**
	 * @param location
	 * @return
	 */
	private int[] locToCoors(int location) {
		return new int[]{location % 4, location / 4};
	}
	
	/**
	 * @param location
	 * @return
	 */
	public void makePick(int location) {
		if (state != STATE_PICKING)
			return;
		
		int[] coors = locToCoors(location);
		
		if (reserve[location] == null)
			return;
			
		pickedPiece = reserve[location];
		reserve[location] = null;
		
		switchTurn();
		changeState(STATE_PLACING);
		
		ArrayList<GameChange> changes = new ArrayList<GameChange>();
		
		changes.add(new GameChange(GameChange.PICKED, 0));
		changes.add(new GameChange(GameChange.RESERVE, location));
		changes.add(new GameChange(GameChange.TURN, turn));
		changes.add(new GameChange(GameChange.STATE, state));
		
		setChanged();
		notifyObservers(changes);

	}
	
	private void switchTurn() {
		if (turn == P1)
			turn = P2;
		else
			turn = P1;
		
	}
	
	private void changeState(int newState) {
		state = newState;
	}
	
	/**
	 * @param location
	 * @return
	 */
	public void makePlace(int location) {
		int[] coors = locToCoors(location);
		
		if (isPieceAt(coors[0], coors[1]))
			return;
		
		putPiece(pickedPiece, coors[0], coors[1]);
		pickedPiece = null;
		changeState(STATE_PICKING);

		
		ArrayList<GameChange> changes = new ArrayList<GameChange>();
		
		changes.add(new GameChange(GameChange.PICKED, 0));
		changes.add(new GameChange(GameChange.BOARD, location));
		changes.add(new GameChange(GameChange.STATE, state));

		setChanged();
		notifyObservers(changes);
	}
	
	/**
	 * @return
	 */
	public boolean isQuarto() {
		return getQuarto() != null;
	}
	
	public int[][] getQuarto() {
		int[][] quarto;
		
		if ((quarto = getLinesQuarto()) != null)
			return quarto;
		
		if (sqareWin)
			quarto = getSquaresQuarto();
		
		return quarto;
	}
	
	/**
	 * @return
	 */
	public int[][] getLinesQuarto() {
		for (int i = 0; i < SIDE_LENGTH; i++) {
			Piece[] colPieces = new Piece[SIDE_LENGTH];
			int[][] colCoors = new int[SIDE_LENGTH][];
			
			Piece[] rowPieces = new Piece[SIDE_LENGTH];
			int[][] rowCoors = new int[SIDE_LENGTH][];
			
			for (int j = 0; j < SIDE_LENGTH; j++) {
				colPieces[j] = getBoardPiece(i, j);
				colCoors[j] = new int[]{i, j};
				
				rowPieces[j] = getBoardPiece(j, i);
				rowCoors[j] = new int[]{j, i};
			}
			
			if (isQuarto(colPieces))
				return colCoors;
			
			if (isQuarto(rowPieces))
				return rowCoors;
				
		}
		
		int d1x = 0;
		int d1y = 0;
		Piece[] diag1Pieces = new Piece[SIDE_LENGTH];
		int[][] diag1Coors = new int[SIDE_LENGTH][];
		
		int d2x = 0;
		int d2y = 3;
		Piece[] diag2Pieces = new Piece[SIDE_LENGTH];
		int[][] diag2Coors = new int[SIDE_LENGTH][];
		
		for (int i = 0; i < SIDE_LENGTH; i++) {
			diag1Pieces[i] = getBoardPiece(d1x, d1y);
			diag1Coors[i] = new int[]{d1x++, d1y++};
			
			diag2Pieces[i] = getBoardPiece(d2x, d2y);
			diag1Coors[i] = new int[]{d2x++, d2y--};
		}
		
		if (isQuarto(diag1Pieces))
			return diag1Coors;
		
		if (isQuarto(diag2Pieces))
			return diag2Coors;

		
		return null;
	}
	
	
	
	/**
	 * @return
	 */
	public int[][] getSquaresQuarto() {
		for (int x = 0; x < SIDE_LENGTH-1; x++) {			
			for (int y = 0; y < SIDE_LENGTH-1; y++) {
				
				Piece[] group = new Piece[SIDE_LENGTH];
				int[][] coors = new int[SIDE_LENGTH][];
				
				int index = 0;
				for (int xi = x; xi <= x+1; xi++) {
					for (int yi = y; yi <= y+1; yi++) {
						group[index] = getBoardPiece(xi, yi);
						coors[index] = new int[]{xi, yi};
						index++;
					}
				}
				
				if (isQuarto(group))
					return coors;
			}
		}
		return null;
	}
	
	/**
	 * @param group
	 * @return
	 */
	public boolean isQuarto(Piece[] group) {
		// Start out with all 1's
		// Remove 1 if slot has 0 index sub trait
		byte andTraits = (byte) 0x0f;
		
		// Start out with all 0's
		// Remove 0 if slot has 1 index sub trait
		byte orTraits = (byte) 0x00;
		
		for (int pIndex = 0; pIndex < group.length; pIndex++) {
			if (group[pIndex] == null)
				return false;
			
			byte curPiece = group[pIndex].getByteRepresentation();
			andTraits &= curPiece;
			orTraits |= curPiece;
		}
		
		// At least one trait with all the same sub trait
		if (andTraits != 0x00 || orTraits != 0x0f)
			return true;
		
		return false;
	}

	/**
	 * @return An array of the <code>Piece</code>s
	 */
	public final Piece[] getReserve() {
		return reserve;
	}

	public Piece getBoardPiece(int index) {
		int[] coors = locToCoors(index);
		return getBoardPiece(coors[0], coors[1]);
	}

	public Piece getPicked() {
		return pickedPiece;
	}

	public int getTurn() {
		return turn;
	}
	
	public int getWinner() {
		if (isStalemate())
			return -1;
		
		if (!isQuarto())
			switchTurn();
		
		return turn;

	}
	
	public void callQuarto() {		
		changeState(STATE_END);
		
		ArrayList<GameChange> changes = new ArrayList<GameChange>();
		changes.add(new GameChange(GameChange.STATE, state));
		setChanged();
		notifyObservers(changes);

	}

	public boolean isStalemate() {
		for (int i = 0; i < NUM_PIECES; i++) {
			if (getReservePiece(i) != null)
				return false;
		}
		
		return !isQuarto();
	}

	
}
