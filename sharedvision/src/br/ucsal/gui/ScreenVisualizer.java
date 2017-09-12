package br.ucsal.gui;

import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import br.ucsal.properties.PropertiesService;

public class ScreenVisualizer extends JPanel implements WindowListener {

	private static final long serialVersionUID = 1L;

	private BufferedImage screenImage;
	
	private JFrame jFrame = new JFrame(PropertiesService.getSystemFullIdentification());

	public ScreenVisualizer() {
		jFrame.setLocationRelativeTo(null);
		jFrame.setUndecorated(true);
		jFrame.getContentPane().add(this);
		jFrame.setAlwaysOnTop(true);
		jFrame.setLocationByPlatform(true);
		jFrame.pack();

		GraphicsDevice d = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		d.setFullScreenWindow(jFrame);

		jFrame.setVisible(true);

		jFrame.addWindowListener(this);
	}

	public void windowOpened(WindowEvent event) {
	};

	public void windowActivated(WindowEvent event) {
	};

	public void windowDeactivated(WindowEvent event) {
		jFrame.toFront();
	}

	public void windowIconified(WindowEvent event) {
	};

	public void windowDeiconified(WindowEvent event) {
	};

	public void windowClosed(WindowEvent event) {
	};

	public void windowClosing(WindowEvent event) {
	};

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
