package poc.swg1;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.JTable;

public class Domain1TablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	JTable table;

	public Domain1TablePanel(JTable table) {
		this.table = table;
		setLayout(new BorderLayout());
		add(table);
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// Rectangle r1 = table.getCellRect(5, 0, false);
		// Rectangle r2 = table.getCellRect(3, 2, false);
		// g2.setPaint(Color.red);
		// double x1 = r1.getCenterX();
		// double y1 = r1.getCenterY();
		// double x2 = r2.getCenterX();
		// double y2 = r2.getCenterY();
		// g2.draw(new Line2D.Double(x1, y1, x2, y2));
		g2.fillRect(table.getX() - 1, table.getY() + 1, table.getWidth() + 5, table.getHeight() + 5);
	}
}
