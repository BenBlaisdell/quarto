package game;

import game.traits.CenterTrait;
import game.Game;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Hashtable;

public class Test {
	
	public static void main(String[] args) {
		long state = 20922789888000L;
		do {
			state--;
		} while (state != 0);
		
		System.out.println("done");
	}
	
	public static void build() {
		Game game = new Game();
		game.newGame(true);
		
		Hashtable<Character, Hashtable<Long, Double>> mappings = new Hashtable<Character, Hashtable<Long, Double>>();
		
		State state = new State(game);
		classify(game, state, mappings, 0);
		
		System.out.println("outer: " + mappings.size());
		
		long inner = 0;
		for (char key : mappings.keySet()) {
			Hashtable<Long, Double> match = mappings.get(key);
			inner += match.size();
		}
		
		System.out.println("inner: " + inner);

	}

	public static double classify(Game game, State state, Hashtable<Character, Hashtable<Long, Double>> mappings, int reserveIndex) {
		double utility;
		
		if (reserveIndex == 16) {
			
			utility = (game.isQuarto() ? 1 : 0);
			setUtility(game, state, mappings, utility);
			return utility;
			
		}
		
		utility = 0;
		
		for (int boardIndex = 0; boardIndex < Game.NUM_PIECES; boardIndex++) {
			if (game.isPieceAt(boardIndex%4, boardIndex/4))
				continue;
			
			Piece piece = game.getReservePiece(reserveIndex);
			game.putPiece(piece, boardIndex%4, boardIndex/4);
			state.updateSquare(piece, boardIndex);
			
			//utility += classify(game, state, mappings, reserveIndex+1, discount * .90);
			
			game.putPiece(null, boardIndex%4, boardIndex/4);
			state.updateSquare(null, boardIndex);

		}
		
		setUtility(game, state, mappings, utility);//(game.isQuarto() ? 1 : 0) * discount;
		return utility;

	}


	private static void setUtility(Game game, State state, Hashtable<Character, Hashtable<Long, Double>> mappings, double utility) {
		Hashtable<Long, Double> match = null;
		char curOrientation = (char) state.filledSpaces.toLongArray()[0];
		
		for (int i = 0; i < 4; i++) {
			if ((match = mappings.get(curOrientation)) != null)
				break;
			
			char flipped = flip(curOrientation);
			
			if ((match = mappings.get(flipped)) != null)
				break;
			
			curOrientation = rotate(curOrientation);
		}
		
		if (match == null)
			match = new Hashtable<Long, Double>();
		
		setUtilityGivenMatch(game, state, match, utility);
		
	}

	private static final long SHAPE_BITS = 0x1111111111111111L;
	private static final long HEIGHT_BITS= 0x2222222222222222L;
	private static final long COLOR_BITS= 0x4444444444444444L;
	private static final long CENTER_BITS= 0x8888888888888888L;
	
	private static void setUtilityGivenMatch(Game game, State state, Hashtable<Long, Double> match, double utility) {
		long flippedState = 0;
		Double currentUtility = null;
		
		for (int currentTraitSwitch = 0; currentTraitSwitch < 16; currentTraitSwitch++) {
			flippedState = state.generalizedTraits.toLongArray()[0];
			
			for (int flipTest = 0; flipTest < 4; flipTest++) {
				int traitBit = 1 << flipTest;
				flipTraitBits(flippedState, traitBit);
			}
			
			if ((currentUtility = match.get(flippedState)) != null)
				break;
		}
		
		if (currentUtility == null)
			currentUtility = new Double(0.0);
		
		match.put(flippedState, currentUtility.doubleValue() + utility);
	}

	private static void flipTraitBits(long flippedState, int traitBit) {
		for (int i = 0; i < Game.NUM_PIECES; i++) {
			flippedState = (traitBit << i) ^ flippedState;
		}
	}

	private static char rotate(char filledSpaces) {
		char rotated = 0;
		
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				int index = x + (y * 4);
				
				int xRotated = y;
				int yRotated = 3 - x;
				
				int rotatedIndex = xRotated + (yRotated * 4);
				
				boolean spaceFilled = ((0x8000 >>> index) & filledSpaces) > 0;
				
				if (spaceFilled)
					rotated |= 0x8000 >> rotatedIndex;
			}
		}
		
		return rotated;
	}


	private static char flip(char filledSpaces) {
		char flipped = 0;
		
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				int index = x + (y * 4);
				
				int xFlipped = y;
				int yFlipped = x;
				
				int flippedIndex = xFlipped + (yFlipped * 4);
				
				boolean spaceFilled = ((0x8000 >>> index) & filledSpaces) > 0;
				
				if (spaceFilled)
					flipped |= 0x8000 >> flippedIndex;
			}
		}
		
		return flipped;
	}
}
