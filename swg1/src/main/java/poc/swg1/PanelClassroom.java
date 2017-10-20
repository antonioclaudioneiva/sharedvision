package poc.swg1;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

//FIXME Criar um cache com os objetos e suas posição, ao invés de criá-los do zero a cada redesenho da tela.
public class PanelClassroom extends JPanel {

	private static final int RIGHT_FIX_SPACING = 10;

	private static final int BOTTOM_FIX_SPACING = 32;

	private static final long serialVersionUID = 1L;

	private static final Color STAND_BACKGROUND_COLOR = new Color(192, 192, 192);

	private static final Color STAND_BORDER_COLOR = Color.BLACK;

	private static final Color MONITOR_BACKGROUND_COLOR = Color.WHITE;

	private static final Color MONITOR_BORDER_COLOR = Color.BLACK;

	private static final Color CHAIR_BACKGROUND_COLOR = new Color(153, 204, 255);

	private static final Color CHAIR_BORDER_COLOR = Color.BLACK;

	private static final Color BOARD_BACKGROUND_COLOR = new Color(160, 160, 160);

	private static final Color BOARD_BORDER_COLOR = Color.BLACK;

	private static final int QTY_ROWS = 4;

	private int width;

	private int height;

	private int standWidth;

	private int standHeight;

	private int standArcSize;

	private int monitorWidth;

	private int monitorHeight;

	private int monitorWidthShift;

	private int monitorHeightShift;

	private int chairSeatWidth;

	private int chairSeatHeight;

	private int chairBackWidth;

	private int chairBackHeight;

	private int chairBackWidthShift;

	private int chairSeatWidthShift;

	private int completeStandHeigh;

	private int completeStandWidth;

	private int completeStandWallWidthSpacing;

	private int completeStandStandHeightSpacing;

	private int boardWidth;

	private int boardHeight;

	private int boardWidthShift;

	public PanelClassroom(int width, int height) {
		this.width = width;
		this.height = height;
		calcObjectsSize();
	}

	private void calcObjectsSize() {
		standWidth = width * 37 / 100;
		standHeight = standWidth / 3;
		standArcSize = standWidth * 7 / 100;

		monitorWidth = standWidth / 4;
		monitorHeight = monitorWidth;
		monitorWidthShift = (standWidth / 2 - monitorWidth) / 2;
		monitorHeightShift = (standHeight - monitorHeight) / 2;

		chairBackWidth = standWidth / 3;
		chairBackHeight = chairBackWidth * 10 / 100;
		chairBackWidthShift = (standWidth / 2 - chairBackWidth) / 2;

		chairSeatWidth = chairBackWidth * 90 / 100;
		chairSeatHeight = chairBackHeight * 2;
		chairSeatWidthShift = (standWidth / 2 - chairSeatWidth) / 2;

		completeStandHeigh = standHeight + chairBackHeight + chairSeatHeight;
		completeStandWidth = standWidth;

		completeStandWallWidthSpacing = width * 2 / 100;

		completeStandStandHeightSpacing = height / (QTY_ROWS + 1);

		boardWidth = width * 60 / 100;
		boardHeight = new Double(height * 0.7 / 100).intValue();
		boardWidthShift = (width - boardWidth) / 2;

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		dramCompleteStands(g);
		drawBoard(g);
	}

	private void dramCompleteStands(Graphics g) {
		boolean reversed = false;
		for (int row = 0; row < QTY_ROWS; row++) {
			if (row == QTY_ROWS - 1) {
				reversed = true;
			}
			drawCompleteStand(g, completeStandWallWidthSpacing,
					completeStandHeigh + completeStandStandHeightSpacing * row, reversed);
			drawCompleteStand(g, width - completeStandWidth - completeStandWallWidthSpacing - RIGHT_FIX_SPACING,
					completeStandHeigh + completeStandStandHeightSpacing * row, false);
		}
	}

	private void drawCompleteStand(Graphics g, int x, int y, boolean reversed) {
		drawStand(g, x, y);
		drawMonitor(g, x, y, 0);
		drawMonitor(g, x, y, 1);
		drawChair(g, x, y, 0, false);
		drawChair(g, x, y, 1, reversed);
	}

	private void drawStand(Graphics g, int x, int y) {
		g.setColor(STAND_BACKGROUND_COLOR);
		g.fillRoundRect(x, y, standWidth, standHeight, standArcSize, standArcSize);
		g.setColor(STAND_BORDER_COLOR);
		g.drawRoundRect(x, y, standWidth, standHeight, standArcSize, standArcSize);
	}

	private void drawMonitor(Graphics g, int x, int y, int pos) {
		x = x + pos * standWidth / 2 + monitorWidthShift;
		g.setColor(MONITOR_BACKGROUND_COLOR);
		g.fillRect(x, y + monitorHeightShift, monitorWidth, monitorHeight);
		g.setColor(MONITOR_BORDER_COLOR);
		g.drawRect(x, y + monitorHeightShift, monitorWidth, monitorHeight);
	}

	private void drawChair(Graphics g, int x, int y, int pos, boolean reversed) {
		drawChairBack(g, x, y, pos, reversed);
		drawChairSeat(g, x, y, pos, reversed);
	}

	private void drawChairBack(Graphics g, int x, int y, int pos, boolean reversed) {
		x = x + pos * standWidth / 2 + chairBackWidthShift;
		if (reversed) {
			g.setColor(CHAIR_BACKGROUND_COLOR);
			g.fillRect(x, y + chairSeatHeight + standHeight, chairBackWidth, chairBackHeight);
			g.setColor(CHAIR_BORDER_COLOR);
			g.drawRect(x, y + chairSeatHeight + standHeight, chairBackWidth, chairBackHeight);
		} else {
			g.setColor(CHAIR_BACKGROUND_COLOR);
			g.fillRect(x, y - chairBackHeight - chairSeatHeight, chairBackWidth, chairBackHeight);
			g.setColor(CHAIR_BORDER_COLOR);
			g.drawRect(x, y - chairBackHeight - chairSeatHeight, chairBackWidth, chairBackHeight);
		}
	}

	private void drawChairSeat(Graphics g, int x, int y, int pos, boolean reversed) {
		x = x + pos * standWidth / 2 + chairSeatWidthShift;
		if (reversed) {
			g.setColor(CHAIR_BACKGROUND_COLOR);
			g.fillRect(x, y + standHeight, chairSeatWidth, chairSeatHeight);
			g.setColor(CHAIR_BORDER_COLOR);
			g.drawRect(x, y + standHeight, chairSeatWidth, chairSeatHeight);
		} else {
			g.setColor(CHAIR_BACKGROUND_COLOR);
			g.fillRect(x, y - chairSeatHeight, chairSeatWidth, chairSeatHeight);
			g.setColor(CHAIR_BORDER_COLOR);
			g.drawRect(x, y - chairSeatHeight, chairSeatWidth, chairSeatHeight);
		}
	}

	private void drawBoard(Graphics g) {
		g.setColor(BOARD_BACKGROUND_COLOR);
		g.fillRect(boardWidthShift, height - boardHeight - BOTTOM_FIX_SPACING, boardWidth, boardHeight);
		g.setColor(CHAIR_BORDER_COLOR);
		g.drawRect(boardWidthShift, height - boardHeight - BOTTOM_FIX_SPACING, boardWidth, boardHeight);
	}

}
