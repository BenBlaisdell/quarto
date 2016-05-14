package gui;

import gui.GUI.GUIState;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class RoundedButton extends JButton {
	private static final long serialVersionUID = 3051418418422150026L;
	
	private static FontRenderContext context = new FontRenderContext(null, true, true);
	private static Font font = GUI.mainFont.deriveFont(24f);
	
	public RoundedButton(int width, int height, String text) {
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gfx = (Graphics2D) img.getGraphics();
		
	    RenderingHints renderHints = new RenderingHints(
	             RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON);
	    gfx.setRenderingHints(renderHints);
		
	    gfx.setColor(Color.WHITE);
		RoundRectangle2D.Double base = new RoundRectangle2D.Double(0, 0, width, height, 10, 10);
		gfx.fill(base);
		
		TextLayout layout = new TextLayout(text, font, context);
		Rectangle2D bounds = layout.getPixelBounds(context, 0, 0);
		
		float textX = (float) ((((double) width) / 2) - bounds.getCenterX());
		float textY = (float) ((((double) height) / 2) + (layout.getAscent() / 2));

		gfx.setComposite(AlphaComposite.Clear);
		layout.draw(gfx, textX, textY);
		
		setOpaque(false);
		setContentAreaFilled(false);
		setBorder(null);
		setCursor(new Cursor(Cursor.HAND_CURSOR));

		setIcon(new ImageIcon(img));


	}

}
