package gui;

import gui.GUI.GUIState;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainView extends JPanel {
	private GUI gui;
	private BufferedImage background;

	public MainView(final GUI gui) {
		this.gui = gui;
		
		setOpaque(false);
				
		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(gui.getWidth(), gui.getHeight()));
		
		RoundedButton singleButton = new RoundedButton(400, 50, "Single Player");
		singleButton.setAlignmentX(CENTER_ALIGNMENT);
		singleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.changeView(GUIState.GAME);
				gui.getGameView().setSinglePlayer();
			}
		});

		JPanel innerMultiplayerGrid = new JPanel();
		innerMultiplayerGrid.setLayout(new GridLayout(1, 2, 10, 0));
		
		RoundedButton localButton = new RoundedButton(195, 50, "Local");
		innerMultiplayerGrid.add(localButton);
		localButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.changeView(GUIState.GAME);
			}
		});

		RoundedButton remoteButton = new RoundedButton(195, 50, "Remote");
		innerMultiplayerGrid.add(remoteButton);
		
		JPanel multiplayerBox = new MultiplayerBox(innerMultiplayerGrid);
		
		RoundedButton settingsButton = new RoundedButton(195, 50, "Settings");
		settingsButton.setAlignmentX(CENTER_ALIGNMENT);
		
		RoundedButton instButton = new RoundedButton(195, 50, "How to play");
		settingsButton.setAlignmentX(CENTER_ALIGNMENT);
		
		JPanel settingsAndInst = new JPanel();
		settingsAndInst.setOpaque(false);
		settingsAndInst.setLayout(new GridLayout(1, 2, 10, 0));
		settingsAndInst.add(settingsButton);
		settingsAndInst.add(instButton);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 0, 10, 0);
		
		c.gridy = 0;
		c.weighty = 1;
		c.anchor = GridBagConstraints.PAGE_END;
		add(new JLabel(new ImageIcon(GUI.getTextImg("QUARTO", GUI.mainFont.deriveFont(50f), Color.WHITE, null))), c);
		
		c.gridy = 1;
		c.weighty = 0;
		add(singleButton, c);
		
		c.gridy = 2;
		c.weighty = 0;
		c.insets.top = 5;
		add(multiplayerBox, c);
		
		c.gridy = 3;
		c.weighty = 1;
		c.insets.top = 10;
		c.anchor = GridBagConstraints.PAGE_START;
		add(settingsAndInst, c);
		
	}

	
}
