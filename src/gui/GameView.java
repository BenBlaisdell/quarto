package gui;

import game.Game;

public interface GameView {
	public void updateState();
	public void updateTurn();
	public void updatePicked();
	public void updateReserve(int index);
	public void updateBoard(int index);
}
