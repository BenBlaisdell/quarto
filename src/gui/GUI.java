package gui;

import game.Game;
import game.Piece;
import game.traits.CenterTrait;
import game.traits.HeightTrait;
import game.traits.ShapeTrait;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GUI implements Observer {
	
	protected enum GUIState {
		MAIN, GAME, SETTINGS;
	}
	
	protected static final Color LIGHT_COLOR = new Color(132, 26, 15);
	protected static final Color DARK_COLOR = new Color(46, 173, 229);
	protected static final Color BG_COLOR = new Color(255, 255, 255);
	
	
	protected static final Font mainFont = initFont("src/gui/fonts/Helvetica-Bold.ttf", Font.SANS_SERIF);
	protected static final Font monospacedFont = initFont("src/gui/fonts/MonospaceTypewriter.ttf", Font.MONOSPACED);
	
	private static FontRenderContext fontRendContext;
	
	private GamePanel gameView;
	private MainView mainMenu;
	private SettingsView settingsView;
	private JFrame window;
	private JPanel container;
	private int width;
	private int height;
		
	
	public GUI(Game game) {
		
		window = new JFrame("Quarto");

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		gameView = new GamePanel(game, this);				
		
		Dimension screenSize = gameView.getPreferredSize();
		window.setSize(screenSize);
		width = (int) screenSize.getWidth();
		height = (int) screenSize.getHeight();
		
		mainMenu = new MainView(this);
		settingsView = new SettingsView(this);
		
		final Image background = (new ImageIcon("src/gui/images/background.jpg")).getImage();
		
		container = new JPanel(){
			private static final long serialVersionUID = -9032003834659454132L;
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(background, 0, 0, null);
				
			}
		};
		container.setSize(screenSize);
		window.add(container);
		
		
		changeView(GUIState.MAIN);
		
		window.setContentPane(container);

		window.pack();

		window.setLocationRelativeTo(null);  // centering jframe

		window.setVisible(true);

	}
	
	private static Font initFont(String fileName, String type) {
		Font font;
		
		try {
			File file = new File(fileName);
			font = Font.createFont(Font.TRUETYPE_FONT, file);			
		} catch (FontFormatException | IOException e) {
			font = Font.getFont(type);
		}
		
		return font;
	}
	
	public void changeView(GUIState state) {
		JPanel view = null;
		switch (state) {
			case MAIN:
				view = mainMenu;
				break;
			case GAME:
				view = gameView;
				getGameView().init();
				getGameView().game.newGame(true);
				break;
			case SETTINGS:
				view = settingsView;
				break;
		}
		container.removeAll();
		container.add(view);
		container.revalidate();
		container.repaint();
	}
	
	
	public static BufferedImage getTextImg(String text, Font font, Color textColor, Color bgColor) {
		getFontRendContext();
		
		TextLayout layout = new TextLayout(text, font, fontRendContext);
		Rectangle2D bounds = layout.getPixelBounds(fontRendContext, 0, 0);
		
		int width = (int) Math.ceil(bounds.getWidth());
		int height = (int) Math.ceil(bounds.getHeight());
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gfx = (Graphics2D) img.getGraphics();
	    RenderingHints renderHints = new RenderingHints(
	             RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON);
		gfx.setRenderingHints(renderHints);
		
		int x = (int) -bounds.getX();
		int y = (int) -bounds.getY();
		
		if (bgColor != null) {
			gfx.setColor(bgColor);
			gfx.fillRect(0, 0, width, height);
		}
		
		if (textColor != null)
			gfx.setColor(textColor);
		else
			gfx.setComposite(AlphaComposite.Clear);
		
		layout.draw(gfx, x, y);
		
		return img;
		
	}

	public static FontRenderContext getFontRendContext() {
		if (fontRendContext == null) {
			BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			Graphics2D gfx = (Graphics2D) img.getGraphics();
		    RenderingHints renderHints = new RenderingHints(
		             RenderingHints.KEY_ANTIALIASING,
		             RenderingHints.VALUE_ANTIALIAS_ON);
		    gfx.setRenderingHints(renderHints);
		    fontRendContext = gfx.getFontRenderContext();
		}
		return fontRendContext;
	}

	public GamePanel getGameView() {
		return gameView;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}


	
}
