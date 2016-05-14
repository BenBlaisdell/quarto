package gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;

class FooterButton extends JButton {
	private static Font FONT = GUI.mainFont.deriveFont(18f);
	
	private static final int WIDTH = 138;
	private static final int HEIGHT = 30;
	
	private static final Color bg = new Color(255, 255, 255, 100);
	
	public FooterButton(String text) {
		BufferedImage textImg = GUI.getTextImg(text, FONT, null, bg);
		BufferedImage exitImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D exitGfx = (Graphics2D) exitImg.getGraphics();
		int x = (WIDTH/2) - (textImg.getWidth()/2);
		int y = (HEIGHT/2) - (textImg.getHeight()/2);
	    RenderingHints renderHints = new RenderingHints(
	             RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON);
	    exitGfx.setRenderingHints(renderHints);
	    
	    exitGfx.setColor(Color.WHITE);
		RoundRectangle2D.Double base = new RoundRectangle2D.Double(0, 0, WIDTH, HEIGHT, 10, 10);
		exitGfx.fill(base);
		
		exitGfx.setComposite(AlphaComposite.Clear);
		RoundRectangle2D.Double cutout = new RoundRectangle2D.Double(2, 2, WIDTH-4, HEIGHT-4, 8, 8);
		exitGfx.fill(cutout);

		exitGfx.setColor(bg);
		exitGfx.setComposite(AlphaComposite.SrcOver);
		exitGfx.fillRoundRect(1, 1, WIDTH-2, HEIGHT-2, 9, 9);
		exitGfx.setComposite(AlphaComposite.Clear);
		exitGfx.fillRect(x, y, textImg.getWidth(), textImg.getHeight());
		exitGfx.setComposite(AlphaComposite.SrcOver);
		exitGfx.drawImage(textImg, x, y, null);
		
		setIcon(new ImageIcon(exitImg));
		setOpaque(false);
		setContentAreaFilled(false);
		setBorder(null);
		setCursor(new Cursor(Cursor.HAND_CURSOR));

	}
}