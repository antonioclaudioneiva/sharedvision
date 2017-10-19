package poc.swg1;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

public class Domain1Renderer extends JLabel implements TableCellRenderer, Icon {

	private static final long serialVersionUID = 1L;

	private static final int DOMAIN1_HEIGHT = 46;

	private static final int DOMAIN1_Y_DISPLACEMENT = DOMAIN1_HEIGHT / 2 - 5;

	private static final int DOMAIN1_WIDTH = 220;

	Border unselectedBorder = null;
	Border selectedBorder = null;
	Domain1 domain1 = null;

	public Domain1Renderer(boolean isBordered) {
		setIcon(this);
	}

	public Component getTableCellRendererComponent(JTable table, Object domain1, boolean isSelected, boolean hasFocus,
			int row, int column) {
		this.domain1 = (Domain1) domain1;
		if (domain1 != null) {
			setForeground(((Domain1) domain1).getColor());
		}
		return this;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (domain1 != null) {
			g.drawRect(x + 5, y - DOMAIN1_Y_DISPLACEMENT, DOMAIN1_WIDTH, DOMAIN1_HEIGHT);
		}
	}

	@Override
	public int getIconWidth() {
		return 10;
	}

	@Override
	public int getIconHeight() {
		return 10;
	}

}
