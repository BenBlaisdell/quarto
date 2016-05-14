package gui;

import game.Game;
import game.Piece;
import game.player.AI;
import gui.GUI.GUIState;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GamePanel extends JPanel implements GameView {
	
	private static final int RESERVE_SPACE_WIDTH = 34;
	private static final int BOARD_SPACE_WIDTH = 70;
	private static final int BORDER_WIDTH = 2;
	private static final int PADDING = 10;
	
	private GUI gui;
	public Game game;
		
	private BoardSpace[] boardSpaces;
	private ReserveSpace[] reserveSpaces;
	private PieceSpace pickedSpace;
	private GameStateView gsView;
	
	protected Font mainFont;
	protected Font monospacedFont;
	private TurnTimer turnTimer;
	private AI ai;

	public GamePanel(Game game, GUI gui) {	
		this.game = game;
		this.gui = gui;
		
		mainFont = gui.mainFont;
		monospacedFont = gui.monospacedFont;

		setLayout(new GridBagLayout());
		
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setOpaque(false);
		
		init();
	}
	
	public void init() {
		ai = null;
		removeAll();
		initGSView();
		initBoardView();
		initPickedSpace();
		initReserveView();
		initFooter();
	}

	private void initGSView() {
		gsView = new GameStateView(this);
		GridBagConstraints c = new GridBagConstraints(0, 0, 4, 1, 0, 1, 
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 10, 0), 0, 0);
		add(gsView, c);
	}


	private void initPickedSpace() {
		pickedSpace = new BoardSpace(0, null);
		pickedSpace.setEnabled(false);
		GridBagConstraints c = new GridBagConstraints(4, 1, 1, 8, 1, 0, 
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(10, 10, 10, 10), 0, 0);
		add(pickedSpace, c);		
	}


	private void initReserveView() {
		reserveSpaces = new ReserveSpace[Game.NUM_PIECES];
		
		ActionListener reserveListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int index = ((PieceSpace) event.getSource()).getIndex();
				game.makePick(index);
			}
		};
		
		GridBagConstraints c = new GridBagConstraints(5, 1, 1, 1, 0, 0, 
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(1, 1, 1, 1), 0, 0);
		
		for (int i = 0; i < Game.NUM_PIECES; i++) {
			reserveSpaces[i] = new ReserveSpace(i, null);
			reserveSpaces[i].addActionListener(reserveListener);
			
			int xCoor = i % 2;
			int yCoor = i / 2;
			
			c.gridx = xCoor + 5;
			c.gridy = yCoor + 1;
			
			final int START_BORDER_X = 0;
			final int END_BORDER_X = 1;
			
			c.insets.left = (xCoor == START_BORDER_X) ? 0 : 1;
			c.insets.right = (xCoor == END_BORDER_X) ? 0 : 1;
			
			final int START_BORDER_Y = 0;
			final int END_BORDER_Y = 8;
			
			c.insets.top = (yCoor == START_BORDER_Y) ? 0 : 1;
			c.insets.bottom = (yCoor == END_BORDER_Y) ? 0 : 1;
			
			add(reserveSpaces[i], c);
		}
		
	}

	private void initBoardView() {
		boardSpaces = new BoardSpace[Game.NUM_PIECES];
		
		ActionListener boardListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int index = ((PieceSpace) event.getSource()).getIndex();
				game.makePlace(index);
			}
		};

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 2, 0, 0, 
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0);
		
		for (int i = 0; i < Game.NUM_PIECES; i++) {
			boardSpaces[i] = new BoardSpace(i, null);
			boardSpaces[i].addActionListener(boardListener);
			
			int xCoor = i % 4;
			int yCoor = i / 4;
			
			c.gridx = xCoor;
			c.gridy = (yCoor * 2) + 1;
			
			final int START_BORDER = 0;
			final int END_BORDER = 3;
			
			c.insets.left = (xCoor == START_BORDER) ? 0 : 1;
			c.insets.right = (xCoor == END_BORDER) ? 0 : 1;
			
			c.insets.top = (yCoor == START_BORDER) ? 0 : 1;
			c.insets.bottom = (yCoor == END_BORDER) ? 0 : 1;
			
			add(boardSpaces[i], c);
		}

	}
	
	private void initFooter() {
		
		GridBagConstraints c;
		
		JButton exitButton = new FooterButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gui.changeView(GUIState.MAIN);
			}
		});
		
		c = new GridBagConstraints(0, 9, 2, 1, 0, 0, 
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, 
				new Insets(10, 0, 0, 0), 0, 0);
		add(exitButton, c);
		
		JButton quartoButton = new FooterButton("Quarto");
		
		quartoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.callQuarto();
			}
		});
		
		c = new GridBagConstraints(2, 9, 2, 1, 0, 0, 
				GridBagConstraints.LINE_END, GridBagConstraints.NONE, 
				new Insets(10, 0, 0, 0), 0, 0);
		add(quartoButton, c);

		
		turnTimer = new TurnTimer(this);
		c = new GridBagConstraints(5, 9, 2, 1, 0, 0, 
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(10, 0, 0, 0), 0, 0);
		add(turnTimer, c);

	}

	private void activateView(PieceSpace[] spaces, boolean active) {
		for (PieceSpace space : spaces)
			space.setActive(active);		
	}
	
	@Override
	public void updateState() {
		boolean enableReserve;
		boolean enableBoard;
		
		switch (game.getState()) {
			case Game.STATE_PICKING:
				updateBoardHover();
				
				if (ai != null && game.getTurn() == Game.P2) {
					ai.pick(game);
					return;
				}
				
				gsView.setState(Game.STATE_PICKING);
				enableReserve = true;
				enableBoard = false;
				break;
				
			case Game.STATE_PLACING:
				updateBoardHover();
				
				if (ai != null && game.getTurn() == Game.P2) {
					ai.place(game);
					return;
				}
				
				gsView.setState(Game.STATE_PLACING);
				enableReserve = false;
				enableBoard = true;
				break;
				
			case Game.STATE_END:
				int[][] quarto = game.getQuarto();
				
				if (quarto != null) {
					for (int i = 0; i < quarto.length; i++) {
						int x = quarto[i][0];
						int y = quarto[i][1];
						int index = x + (4 * y);
						boardSpaces[index].refreshGraphics(true);
					}
				}
				
				gsView.setEnd(!game.isStalemate(), game.getWinner());
				turnTimer.stop();
				enableReserve = false;
				enableBoard = false;
				break;
				
			default:
				enableReserve = false;
				enableBoard = false;
				break;
		}
		
		activateView(reserveSpaces, enableReserve);
		activateView(boardSpaces, enableBoard);		
	}

	private void updateBoardHover() {
		Piece picked = game.getPicked();
		
		ImageIcon faintIcon = PieceSpace.getIcon(picked, 70, true, true);
			
		for (BoardSpace space : boardSpaces) {
			space.setRolloverIcon(faintIcon);
			space.setSelectedIcon(faintIcon);
			space.setPressedIcon(faintIcon);
		}
	}

	@Override
	public void updateTurn() {
		gsView.setTurn(game.getTurn());
		turnTimer.startTimer(300);
	}

	@Override
	public void updatePicked() {
		pickedSpace.updatePiece(game.getPicked());
		
	}

	@Override
	public void updateReserve(int index) {
		Piece piece = game.getReservePiece(index);
		reserveSpaces[index].updatePiece(piece);
	}

	@Override
	public void updateBoard(int index) {
		Piece piece = game.getBoardPiece(index);
		boardSpaces[index].updatePiece(piece);
	}

	public void timerFinished() {
		game.forfeit();
	}

	public void setSinglePlayer() {
		ai = new AI();
	}
	
}
