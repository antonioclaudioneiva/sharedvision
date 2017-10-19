package poc.swg1;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Frame1 {

	private static final Object[][] data = {
			{ new Domain1("claudio", 1973, Color.BLUE), new Domain1("maria", 1975, Color.RED) },
			{ new Domain1("clara", 1979, Color.GREEN) } };

	public Frame1() {
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

		PanelWithBackground panelWithBackground = new PanelWithBackground();

		Domain1Table domain1Table = new Domain1Table();
		Domain1TablePanel domain1TablePanel = new Domain1TablePanel(domain1Table);

		panelWithBackground.add(new JScrollPane(domain1TablePanel));

		contentPane.add(panelWithBackground);

		frame.validate();
		frame.pack();
		frame.setVisible(true);
		domain1Table.addRow(data[0]);
		domain1Table.addRow(data[1]);
	}
}
