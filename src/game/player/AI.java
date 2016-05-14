package game.player;

import game.Game;
import game.Piece;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Random;

public class AI {

	private Random rand;

	// The utility map
	Hashtable<Character, Hashtable<Long, Double>> mappings;


	/**
	 * Initiates the data file
	 */
	public AI() {
		rand = new Random();
		//initData();
	}

	/**
	 * Load the utility values into memory
	 */
	public void initData() {
		try {
			FileInputStream dataFile = new FileInputStream("utility");
			ObjectInputStream objInStream = new ObjectInputStream(dataFile);
			mappings = (Hashtable<Character, Hashtable<Long, Double>>) objInStream.readObject();
		} catch (Exception e) {}
	}


	/**
	 * Checks a board to see if there is a valid Quarto and calls it if there is.
	 * 
	 * @param game
	 * @return
	 */
	private boolean checkQuarto(Game game) {
		if (game.isQuarto()) {
			game.callQuarto();
			return true;
		}

		return false;
	}


	/**
	 * Submits a "pick" move to a quarto game
	 * 
	 * @param game
	 */
	public void pick(Game game) {
		if (checkQuarto(game))
			return;

		int pick;

		// Pick randomly until an index is found corresponding to a filled space in the reserve
		while (game.getReservePiece(pick = rand.nextInt(16)) == null);

		game.makePick(pick);

		return;

		/* *********************************** */

		/*
		Piece[][] board = game.getBoard();
		Piece[] reserve = game.getReserve();

		// Minimize the maximum utility of board state possible after placing the picked piece
		double minUtil = Double.MAX_VALUE;
		int bestPick = -1;

		// Check all pieces left in the reserve
		for (int reserveIndex = 0; reserveIndex < Game.NUM_PIECES; reserveIndex++) {
			Piece piece = reserve[reserveIndex];

			double maxUtil = -1;
			int bestPlace = -1;

			// Find the maximum utility after placing this piece
			// I.e. the utility of the board after the opponent places the piece in the best space
			for (int boardIndex = 0; boardIndex < Game.NUM_PIECES; boardIndex++) {

				if (reserve[reserveIndex] != null) {
					board[boardIndex/4][boardIndex%4] = piece;

					double curUtil = getUtility(board);
					if (curUtil > maxUtil)
						maxUtil = curUtil;

					board[boardIndex/4][boardIndex%4] = null;
				}

			}

			// Replace if the maximum is lower than our current maximum
			if (maxUtil != -1 && maxUtil < minUtil) {
				maxUtil = maxUtil;
				bestPick = reserveIndex;
			}
		}

		game.makePick(bestPick);

		*/
	}


	/**
	 * Submits a "place" move to a quarto game
	 * 
	 * @param game
	 */
	public void place(Game game) { 		
		int place;

		// Pick randomly until an index is found corresponding to a filled space in the reserve
		while (game.getBoardPiece(place = rand.nextInt(16)) != null);

		game.makePlace(place);

		return;

		/* *********************************** */

		/*
		Piece[][] board = game.getBoard();
		Piece[] reserve = game.getReserve();

		// Minimize the maximum utility of board state possible after placing the picked piece
		double maxUtil = -1;
		int bestPlace = -1;

		Piece piece = game.getPicked();

		// Check all slots on the board
		for (int boardIndex = 0; boardIndex < Game.NUM_PIECES; boardIndex++) {

				if (board[boardIndex/4][boardIndex%4] == null) {
					board[boardIndex/4][boardIndex%4] = piece;

					double curUtil = getUtility(board);
					if (curUtil > maxUtil) {
						maxUtil = curUtil;
						bestPlace = boardIndex;
					}

					board[boardIndex/4][boardIndex%4] = null;
				}

		}


		game.makePlace(bestPlace);
		*/

	}

	/**
	 * Finds the utility of a orientation of a quarto board
	 * 
	 * @param board
	 * @return
	 */
	private double getUtility(Piece[][] board) {
		return 0;
	}


}
