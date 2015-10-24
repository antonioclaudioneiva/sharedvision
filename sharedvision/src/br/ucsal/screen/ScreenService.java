package br.ucsal.screen;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import br.ucsal.properties.PropertiesService;
import br.ucsal.util.TimerService;

public class ScreenService extends Thread {

	private byte[] imageByte;
	
	private Robot robot;
	
	private Rectangle screenRect;
	
	private BufferedImage mouseImage = null;

	public ScreenService() throws AWTException {
		logger.info("Creating screen service.");
		createRobot();
		defineSharedScreenArea();
		createMouseImage();
		logger.info("Screen service created.");
	}

	private void createRobot() throws AWTException {
		logger.debug("Creating robot.");
		robot = new Robot();
		logger.debug("Robot created.");
	}

	private void defineSharedScreenArea() {
		logger.debug("Setting shared screen area...");
		screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		logger.debug("Shared screen area set.");
	}

	private void createMouseImage() {
		logger.debug("Creating cursor image.");
		mouseImage = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = mouseImage.createGraphics();
		graphics.setColor(Color.BLACK);
		graphics.drawLine(0, 10, 20, 10);
		graphics.drawLine(10, 0, 10, 20);
		logger.debug("Cursor image created.");
	}
	
	@Override
	public void run() {
		while (true) {
			screenCapture();
			TimerService.sleep(PropertiesService.getScreenRefreshTime());
		}
	}

	private synchronized void screenCapture() {
		logger.debug("Capturing screen image...");
		BufferedImage image = robot.createScreenCapture(screenRect);
		drawMouse(image);
		logger.debug("Screen image captured.");
		convertScreenImageToByteArray(image);
	}

	private void drawMouse(BufferedImage image) {
		logger.debug("Drawing mouse image...");
		PointerInfo pointer = MouseInfo.getPointerInfo();
		int x = (int) pointer.getLocation().getX();
		int y = (int) pointer.getLocation().getY();
		image.getGraphics().drawImage(mouseImage, x, y, null);
		logger.debug("Mouse image draw.");
	}

	private void convertScreenImageToByteArray(BufferedImage image) {
		logger.debug("Converting screen image to bytearray...");
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "JPG", byteArrayOutputStream);
			imageByte = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.flush();
			byteArrayOutputStream.close();
			logger.debug("Screen image converted to bytearray: " + imageByte.length + " bytes.");
		} catch (IOException e) {
			logger.error("Convert screen image to bytearray error: " + e.getMessage());
		}
	}

	public synchronized byte[] getScreenImageByte() {
		return imageByte;
	}

	final static Logger logger = Logger.getLogger(ScreenService.class);
}
