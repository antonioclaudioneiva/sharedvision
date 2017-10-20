package poc.swg1;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class FrameClassroom {

	public FrameClassroom() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					displayJFrame();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void displayJFrame() throws IOException {
		JFrame frame = new JFrame("My JFrame Example");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		int w = 500;
		int h = new Double(w * 15.50 / 11.90).intValue();
		frame.setPreferredSize(new Dimension(w, h));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = new Double(screenSize.getWidth()).intValue();
		int screenHeight = new Double(screenSize.getHeight()).intValue();
		frame.setLocation(screenWidth - w, screenHeight - h - 50);
		GridLayout gridLayout = new GridLayout(1, 1);
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(gridLayout);
		contentPane.add(new PanelClassroom(w, h));
		frame.validate();
		frame.pack();
		frame.setVisible(true);
	}
}
