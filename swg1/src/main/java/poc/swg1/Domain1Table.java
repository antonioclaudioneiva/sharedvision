package poc.swg1;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Domain1Table extends JTable {

	private static final long serialVersionUID = 1L;

	private static final String[] columnNames = { "Col1", "Col2" };

	public Domain1Table() {
		super(new DefaultTableModel(columnNames, 0) {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

		});

		setDefaultRenderer(Domain1.class, new Domain1Renderer(true));

		setRowHeight(50);

		setShowGrid(false);
		
		setTableHeader(null);

		addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = rowAtPoint(evt.getPoint());
				int col = columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0) {
					Object obj = ((DefaultTableModel) getModel()).getValueAt(row, col);
					System.out.println(obj);
				}
			}
		});
	}

	public void addRow(Object[] domain1) {
		((DefaultTableModel) getModel()).addRow(domain1);
	}

}