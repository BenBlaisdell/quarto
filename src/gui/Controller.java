package gui;

import game.Game;
import game.GameChange;
import game.Piece;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

public class Controller implements Observer {
	
	private Game game;
	private ArrayList<GameView> views;
	
	public static void main(String[] args) {
		new Controller();
	}
	
	public Controller() {
		game = new Game();
		game.addObserver(this);
		
		views = new ArrayList<GameView>();
		attachView((new GUI(game)).getGameView());
		
	}
	
	private void attachView(GameView view) {
		views.add(view);
	}

	@Override
	public void update(Observable o, Object arg) {
		ArrayList<GameChange> changes = (ArrayList<GameChange>) arg;
		
		for (GameChange change : changes)
			update(change);
	}

	private void update(GameChange change) {
		switch (change.type) {
			case GameChange.STATE:
				updateState();
				break;
			
			case GameChange.TURN:
				updateTurn();
				break;
				
			case GameChange.PICKED:
				updatePicked();
				break;
				
			case GameChange.RESERVE:
				updateReserve(change.value);
				break;
				
			case GameChange.BOARD:
				updateBoard(change.value);
				break;
		}
		
	}

	public void updateState() {
		for (GameView view : views)
			view.updateState();
	}
	
	public void updateTurn() {
		for (GameView view : views)
			view.updateTurn();
	}
	
	public void updatePicked() {
		for (GameView view : views)
			view.updatePicked();
	}
	
	public void updateReserve(int index) {
		for (GameView view : views)
			view.updateReserve(index);
	}
	
	public void updateBoard(int index) {
		for (GameView view : views)
			view.updateBoard(index);
	}

	
}
