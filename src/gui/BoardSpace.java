package gui;

import game.Piece;

public class BoardSpace extends PieceSpace {
	private static final long serialVersionUID = 6688195899095796425L;

	public BoardSpace(int index, Piece piece) {
		super(index, 70, piece);
	}

	public void setActive(boolean active) {
		setEnabled(active && (piece == null));
	}
}
