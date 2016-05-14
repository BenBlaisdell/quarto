package gui;

import game.Game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameStateView extends JPanel {
	
	private GamePanel gameView;
	
	private FontRenderContext context;
	private Font font;
		
	private JLabel playerLabel;
	private JLabel actionLabel;
	
	private ImageIcon p1Icon;
	private String p1Name = "Player 1";
	
	private ImageIcon p2Icon;
	private String p2Name = "Player 2";
	
	private ImageIcon pickingIcon;
	private final String pickingText = "picking";
	
	private ImageIcon placingIcon;
	private String placingText = "placing";
	
	private ImageIcon winIcon;
	private String winText = "WINS!";
	
	private ImageIcon tieIcon;
	private String tieText = "STALEMATE!";
	
	
	public GameStateView(GamePanel gameView) {
		this.gameView = gameView;
		font = gameView.mainFont.deriveFont(24f);
		
		context = new FontRenderContext(null, true, true);
		
		p1Icon = new ImageIcon(GUI.getTextImg(p1Name, font, Color.WHITE, null));
		p2Icon = new ImageIcon(GUI.getTextImg(p2Name, font, Color.WHITE, null));
		pickingIcon = new ImageIcon(GUI.getTextImg(pickingText, font, Color.WHITE, null));
		placingIcon = new ImageIcon(GUI.getTextImg(placingText, font, Color.WHITE, null));
		winIcon = new ImageIcon(GUI.getTextImg(winText, font, Color.WHITE, null));
		tieIcon = new ImageIcon(GUI.getTextImg(tieText, font, Color.WHITE, null));
		
		playerLabel = new JLabel();
		playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		playerLabel.setOpaque(false);
		
		actionLabel = new JLabel();
		actionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		actionLabel.setOpaque(false);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(false);
		
		add(playerLabel);
		add(actionLabel);
		
		setTurn(Game.P1);
		setState(Game.STATE_PICKING);
	}
	
	public void setEnd(boolean win, int player) {
		setTurn(player);
		
		if (win)
			actionLabel.setIcon(winIcon);
		else
			actionLabel.setIcon(tieIcon);
		
	}
	
	public void setTurn(int player) {
		switch (player) {
			case (Game.P1):
				playerLabel.setIcon(p1Icon);
				break;
			
			case (Game.P2):
				playerLabel.setIcon(p2Icon);
				break;
				
			default:
				playerLabel.setIcon(null);
				break;
		}
	}
	
	public void setState(int state) {
		switch (state) {
			case Game.STATE_PICKING:
				actionLabel.setIcon(pickingIcon);
				break;
			
			case Game.STATE_PLACING:
				actionLabel.setIcon(placingIcon);
				break;
		}
	}
	
}
