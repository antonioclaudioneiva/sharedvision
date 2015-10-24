package br.ucsal.screen;

import java.awt.AWTException;
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

	public ScreenService() throws AWTException {
		logger.info("Creating screen service.");
		createRobot();
		defineSharedScreenArea();
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
		logger.debug("Screen image captured.");
		convertScreenImageToByteArray(image);
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
