package poc.swg1;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PanelWithBackground extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final String BACKGROUND_IMAGE_FILE_NAME = "/images/sala2.png";

	private Image backgroundImage;

	public PanelWithBackground() throws IOException {
		loadImg();
	}

	private void loadImg() throws IOException {
		InputStream imageResource = PanelWithBackground.class.getResourceAsStream(BACKGROUND_IMAGE_FILE_NAME);
		backgroundImage = ImageIO.read(imageResource);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	}
}
