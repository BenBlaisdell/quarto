package gui;

import game.Piece;

public class ReserveSpace extends PieceSpace {
	private static final long serialVersionUID = 7887253480096069180L;

	public ReserveSpace(int index, Piece piece) {
		super(index, 34, piece);
	}

	@Override
	public void setActive(boolean active) {
		setEnabled(active && (piece != null));
	}
}
