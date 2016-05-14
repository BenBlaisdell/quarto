package game;

import java.nio.channels.NonWritableChannelException;
import java.util.BitSet;
import java.util.Hashtable;

import game.traits.Trait;

public class State {
	
	private static final char LAST_INDEX = 0x0001;
	private static final long LAST_PIECE = 0x000000000000000FL;


	public BitSet generalizedTraits;
	public BitSet filledSpaces;
		
	/**
	 * Creates a <code>State</code> for a (not necessarily empty) <code>Board</code>
	 * 
	 * @param board The <code>Board</code> for which to create a state
	 */
	public State(Game board) {
		generalizedTraits = new BitSet(16 * 4);
		filledSpaces = new BitSet(16);
		
		// Build filledSpaces and traitTotals
		for (int index = 0; index < Game.NUM_PIECES; index++)
			updateSquare(board.getBoardPiece(index), index);

		
	}

	public void updateSquare(Piece piece, int index) {
		index = 15 - index;
		
		if (piece == null) {
			filledSpaces.clear(index);
			generalizedTraits.clear(index * 4, (index + 1) * 4);
		}
		else {
			filledSpaces.set(index);
			for (int i = 0; i < 4; i++) {
				if (((1 << i) & piece.getByteRepresentation()) != 0)
					generalizedTraits.set((index * 4) + i);
			}
		}
	}

}
