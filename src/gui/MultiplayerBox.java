package gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

class MultiplayerBox extends JPanel {
	private static final long serialVersionUID = -9066834063238699584L;

	public MultiplayerBox(JPanel inner) {
		setOpaque(false);
		inner.setOpaque(false);
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = 0;
		add(inner, constraints);
		
		setBorder(new EmptyBorder(30, 25, 25, 25));

		
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Rectangle bounds = g.getClip().getBounds();
		
		BufferedImage img = new BufferedImage((int) bounds.getWidth(), (int) bounds.getHeight(), BufferedImage.TYPE_INT_ARGB);

		
		Graphics2D gfx = (Graphics2D) img.getGraphics();
		
		
		
	    RenderingHints renderHints = new RenderingHints(
	             RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON);
	    gfx.setRenderingHints(renderHints);
	    
	    gfx.setColor(Color.WHITE);
	    AlphaComposite transComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .15f);
	    gfx.setComposite(transComposite);
		RoundRectangle2D.Double base = new RoundRectangle2D.Double(0, 5, getWidth(), getHeight()-5, 15, 15);
		gfx.fill(base);
		
		gfx.setComposite(AlphaComposite.Clear);
		RoundRectangle2D.Double cutout = new RoundRectangle2D.Double(5, 10, getWidth()-10, getHeight()-15, 10, 10);
		gfx.fill(cutout);
		
		Font font = GUI.mainFont.deriveFont(18f);
		FontRenderContext context = new FontRenderContext(null, true, true);
		TextLayout layout = new TextLayout("Multiplayer", font, context);
		Rectangle2D textBounds = layout.getPixelBounds(context, 0, 0);
		
		float textX = (float) ((((double) getWidth()) / 2) - textBounds.getCenterX());
		float textY = (float) (5.0 + 5.0/2.0 + (layout.getAscent() / 2));
		
		Rectangle2D.Double textCutout = new Rectangle2D.Double(textX-5, 5, textBounds.getWidth()+10, 5);
		gfx.fill(textCutout);

		gfx.setComposite(AlphaComposite.SrcOver);

		
		layout.draw(gfx, textX, textY);

		
		Graphics2D panelGfx = (Graphics2D) g;
		
		panelGfx.drawImage(img, 0, 0, null);

		
	}
	
}
