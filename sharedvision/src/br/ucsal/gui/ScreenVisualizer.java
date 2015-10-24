package br.ucsal.gui;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import br.ucsal.properties.PropertiesService;

public class ScreenVisualizer extends JPanel {

	private static final long serialVersionUID = 1L;

	private BufferedImage screenImage;

	public ScreenVisualizer() {
		JFrame jFrame = new JFrame(PropertiesService.getSystemFullIdentification());
//		jFrame.setPreferredSize(new Dimension(400, 300));
		jFrame.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		jFrame.getContentPane().add(this);
		jFrame.pack();
		jFrame.setVisible(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (screenImage != null) {
			g.drawImage(screenImage, 0, 0, null); 
		}
	}

	public void setScreenImage(BufferedImage screenImage) {
		this.screenImage = screenImage;
		this.repaint();
	}

}
