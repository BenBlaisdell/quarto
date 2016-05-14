package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

public class TurnTimer extends JLabel implements ActionListener {

	private Timer timer;
	int remaining;
	private Font font;
	private FontRenderContext fontRendContext;
	private RenderingHints renderHints;
	private GamePanel gamePanel;
	
	public TurnTimer(GamePanel gamePanel) {
		super(new ImageIcon(), CENTER);
		
		this.gamePanel = gamePanel;
		
		fontRendContext = GUI.getFontRendContext();
	    renderHints = new RenderingHints(
	             RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON);
		
		font = GUI.monospacedFont.deriveFont(22f);
		remaining = 0;
		updateView();
	}
	
	public void startTimer(int seconds) {
		remaining = seconds;
		if (timer != null)
			timer.stop();
		timer = new Timer(1000, this);
		updateView();
		timer.start();
	}
	
	private String formatTime(int field) {
		String sField = (new Integer(field)).toString();
		while (sField.length() < 2)
			sField = "0" + sField;
		return sField;
	}
	
	private void updateView() {
		String minutes = formatTime(remaining / 60);
		String seconds = formatTime(remaining % 60);
		
		TextLayout layout = new TextLayout(minutes + ":" + seconds, font, fontRendContext);
		Rectangle2D bounds = layout.getPixelBounds(fontRendContext, 0, 0);
		int width = (int) layout.getAdvance();
		int height = (int) bounds.getHeight();
		BufferedImage textImg = new BufferedImage(width, height+3, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D textGfx = (Graphics2D) textImg.getGraphics();
		textGfx.setRenderingHints(renderHints);
		textGfx.setColor(Color.WHITE);
		
		int y = (int) -bounds.getY();
		
		layout.draw(textGfx, 0, y);

		setIcon(new ImageIcon(textImg));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (--remaining <= 0) {
			remaining = 0;
			gamePanel.timerFinished();
			timer.stop();
		}
		
		updateView();
	}

	public void stop() {
		timer.stop();
	}
	
}
