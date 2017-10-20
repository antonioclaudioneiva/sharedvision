package br.ucsal.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import br.ucsal.core.SharedVision;
import br.ucsal.properties.OperationModeEnum;
import br.ucsal.properties.PropertiesService;
import br.ucsal.screen.KeyHook;
import br.ucsal.util.TimerService;

public class ScreenVisualizer extends JPanel {

	private static final long serialVersionUID = 1L;

	private BufferedImage screenImage;

	private static JFrame jFrame = new JFrame(PropertiesService.getSystemFullIdentification());

	public ScreenVisualizer() {
		if (OperationModeEnum.TEACHER.equals(SharedVision.getInstance().getOperationMode())) {
			createTeacherScreenVisualizer();
		} else {
			createTeacherScreenVisualizer();
			// createStudentScreenVisualizer();
		}
	}

	// FIXME Instanciar corretamente JFrame com SwingUtilities
	public void createTeacherScreenVisualizer() {
		jFrame.setLocation(0, 0);
		jFrame.setPreferredSize(new Dimension(600,600));
		jFrame.getContentPane().add(this);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.pack();
		jFrame.setVisible(true);
	}

	// FIXME Instanciar corretamente JFrame com SwingUtilities
	public void createStudentScreenVisualizer() {
		KeyHook.blockWindowsKey();

		new Thread() {
			@Override
			public void run() {
				TimerService.sleep(20000);
				KeyHook.unblockWindowsKey();
			};
		}.start();

		jFrame.setLocationRelativeTo(null);
		jFrame.setUndecorated(true);
		jFrame.getContentPane().add(this);
		jFrame.setAlwaysOnTop(true);
		jFrame.setLocationByPlatform(true);
		jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jFrame.pack();
		GraphicsDevice d = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		d.setFullScreenWindow(jFrame);
		jFrame.setVisible(true);

	}

	public static JFrame getFrame() {
		return jFrame;
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
