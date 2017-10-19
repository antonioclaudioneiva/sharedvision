package poc.swg1;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class FrameWithBackground {

	public FrameWithBackground() {
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
		Double w = 500d;
		Double h = w * 15.50 / 11.90;
		frame.setPreferredSize(new Dimension(w.intValue(), h.intValue()));
		frame.setLocationRelativeTo(null);
		GridLayout gridLayout = new GridLayout(1, 1);
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(gridLayout);
		contentPane.add(new PanelWithBackground());
		frame.validate();
		frame.pack();
		frame.setVisible(true);
	}
}
