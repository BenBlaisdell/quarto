package gui;

import game.Piece;
import game.traits.*;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public abstract class PieceSpace extends JButton {
	private static final long serialVersionUID = -1997851138185640117L;
	
	protected static final Color bgColor = Color.WHITE;
	
	private int index;
	private int dimensions;
	protected Piece piece;
	
	public PieceSpace(int index, int dimensions, Piece piece) {
		this.index = index;
		this.dimensions = dimensions;
		this.piece = piece;
		refreshGraphics(false);
		setBorder(null);
		setContentAreaFilled(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setSize(dimensions, dimensions);
	}
	
	public static ImageIcon getIcon(Piece piece, int dimensions, boolean rounded, boolean faint) {
		BufferedImage pieceImg = createPieceImg(piece, dimensions);
		
		BufferedImage baseImg = new BufferedImage(dimensions, dimensions, BufferedImage.TYPE_INT_ARGB);
		Graphics2D baseGfx = (Graphics2D) baseImg.getGraphics();
		
	    RenderingHints renderHints = new RenderingHints(
	             RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON);
	    baseGfx.setRenderingHints(renderHints);
	    
	    Shape baseShape;
	    
	    if (rounded) {
	    	double corner = 2.0 * ((double) dimensions) / 7.0;
	    	baseShape = new RoundRectangle2D.Double(0, 0, dimensions, dimensions, corner, corner);
	    }
	    else
	    	baseShape = new Rectangle2D.Double(0, 0, dimensions, dimensions);
	    
	    baseGfx.setColor(bgColor);
	    baseGfx.fill(baseShape);
	    
	    if (faint) {
			float[] scales = { 1f, 1f, 1f, .4f };
			float[] offsets = new float[4];
			RescaleOp filter = new RescaleOp(scales, offsets, null);
			baseGfx.drawImage(pieceImg, filter, 0, 0);
	    }
	    else
	    	baseGfx.drawImage(pieceImg, 0, 0, null);
	    
	    return new ImageIcon(baseImg);
	}
	
	public void refreshGraphics(boolean rounded) {
		ImageIcon icon = getIcon(piece, dimensions, rounded, false);
		setIcon(icon);
		setDisabledIcon(icon);
	}
	
	private static void setPieceColor(Graphics2D buttonGfx, Piece piece) {
		if (piece == null)
			return;
				
		Color color;
		if (piece.color == ColorTrait.LIGHT)
			color = GUI.LIGHT_COLOR;
		else
			color = GUI.DARK_COLOR;
		
		buttonGfx.setColor(color);
	}
	
	private static void processShape(Graphics2D buttonGfx, Piece piece, int dimensions) {
		if (piece == null)
			return;
		
		double shapeDim = (5.0/7.0) * dimensions;
		double shapeLoc = (((double) dimensions) / 2.0) - (shapeDim / 2.0);
		
		Shape shape;
		if (piece.shape == ShapeTrait.CIRCLE)
			shape = new Ellipse2D.Double(shapeLoc, shapeLoc, shapeDim, shapeDim);
		else
			shape = new Rectangle2D.Double(shapeLoc, shapeLoc, shapeDim, shapeDim);
		
		buttonGfx.fill(shape);
	}
	
	private static void processCenter(Graphics2D buttonGfx, Piece piece, int dimensions) {	
		if (piece == null || piece.center == CenterTrait.SOLID)
			return;
		
		// Clear a hole in the center to make a line between the fill and outer ring
		
		buttonGfx.setComposite(AlphaComposite.Clear);
		
		double middleDim = (3.0/7.0) * dimensions;
		double middleLoc = (((double) dimensions) / 2.0) - (middleDim / 2.0);
		
		Shape outer;
		if (piece.shape == ShapeTrait.CIRCLE)
			outer = new Ellipse2D.Double(middleLoc, middleLoc, middleDim, middleDim);
		else
			outer = new Rectangle2D.Double(middleLoc, middleLoc, middleDim, middleDim);
		
		buttonGfx.fill(outer);
		
		buttonGfx.setComposite(AlphaComposite.SrcOver);
		
		// Fill in the center hole

		double innerDim = (2.0/7.0) * dimensions;
		double innerLoc = (((double) dimensions) / 2.0) - (innerDim / 2.0);
		
		Shape inner;
		if (piece.shape == ShapeTrait.CIRCLE)
			inner = new Ellipse2D.Double(innerLoc, innerLoc, innerDim, innerDim);
		else
			inner = new Rectangle2D.Double(innerLoc, innerLoc, innerDim, innerDim);
		
		buttonGfx.fill(inner);
	}
	
	private static void processHeight(Graphics2D buttonGfx, Piece piece, int dimensions) {
		if (piece == null || piece.height == HeightTrait.SHORT)
			return;

		buttonGfx.setComposite(AlphaComposite.Clear);
		
		double barWidth = ((double) dimensions) / 14.0;
		double barLoc = (((double) dimensions) / 2.0) - (barWidth / 2.0);
		
		buttonGfx.fill(new Rectangle2D.Double(barLoc, 0, barWidth, dimensions));
		buttonGfx.fill(new Rectangle2D.Double(0, barLoc, dimensions, barWidth));
		
		buttonGfx.setComposite(AlphaComposite.SrcOver);

	}
		
	public static BufferedImage createPieceImg(Piece piece, int dimensions) {
		
		BufferedImage buttonImg = new BufferedImage(dimensions, dimensions, BufferedImage.TYPE_INT_ARGB);
		Graphics2D buttonGfx = (Graphics2D) buttonImg.getGraphics();
		
	    RenderingHints renderHints = new RenderingHints(
	             RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON);
	    buttonGfx.setRenderingHints(renderHints);
						
	    setPieceColor(buttonGfx, piece);
	    
		processShape(buttonGfx, piece, dimensions);
		processHeight(buttonGfx, piece, dimensions);
		processCenter(buttonGfx, piece, dimensions);
				
		return buttonImg;
	}

	public void updatePiece(Piece piece) {
		this.piece = piece;
		refreshGraphics(false);
	}

	public int getIndex() {
		return index;
	}
	
	public abstract void setActive(boolean active);
	
}
