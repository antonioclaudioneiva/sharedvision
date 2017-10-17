package br.ucsal.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import br.ucsal.gui.ScreenVisualizer;
import br.ucsal.properties.PropertiesService;
import br.ucsal.server.ThreadServer;
import br.ucsal.util.TimerService;

public class ThreadClient extends Thread {

	private String host;
	
	private Integer port;
	
	private Socket socket;

	private ScreenVisualizer screenVisualizer;
	
	private Client client;

	public ThreadClient(String host, Integer port, ScreenVisualizer screenVisualizer, Client client) {
		this.host = host;
		this.port = port;
		this.screenVisualizer = screenVisualizer;
		this.client = client;
		requestConnection();
		sendClientIdentification();
	}

	private void sendClientIdentification() {
		logger.debug("Sending client identification...");
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectOutputStream.writeObject(client);
			logger.debug("Client identification sent.");
		} catch (IOException e) {
			logger.error("Client identification sent failed: "+e.getMessage());
		}
	}

	private void requestConnection() {
		logger.info("Request server connection ("+host+":"+port+")...");
		try {
			socket = new Socket(host, port);
			int serverResponse = new DataInputStream(socket.getInputStream()).readInt();
			logger.info("Server connected (response=" + serverResponse + ").");
		} catch (IOException e) {
			logger.error("Request server connection failed: " + e.getMessage());
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void run() {
		while (true) {
			BufferedImage screenImage = requestScreenImage();
			if (screenImage != null) {
				screenVisualizer.setScreenImage(screenImage);
			}
			TimerService.sleep(PropertiesService.getScreenRefreshTime());
		}
	}

	private BufferedImage requestScreenImage() {
		logger.debug("Request screen image...");
		byte[] clientRequestCode = new byte[1];
		clientRequestCode[0] = ThreadServer.REQUEST_SCREEN_IMAGE;
		try {
			sendRequest(clientRequestCode);
			BufferedImage screenImage = receiveScreenImage();
			logger.debug("Screen image received");
			return screenImage;
		} catch (IOException e) {
			logger.error("Request screen image error: " + e.getMessage());
		}
		return null;
	}
	
	private BufferedImage requestUnlockKeys() {
		logger.debug("Request screen image...");
		byte[] clientRequestCode = new byte[1];
		clientRequestCode[0] = ThreadServer.REQUEST_UNLOCK_KEYS;
		try {
			sendRequest(clientRequestCode);
			BufferedImage screenImage = receiveScreenImage();
			logger.debug("Screen image received");
			return screenImage;
		} catch (IOException e) {
			logger.error("Request screen image error: " + e.getMessage());
		}
		return null;
	}

	private void sendRequest(byte[] clientRequestCode) throws IOException {
		logger.debug("Sending request (" + clientRequestCode[0] + ")...");
		socket.getOutputStream().write(clientRequestCode);
		socket.getOutputStream().flush();
		logger.debug("Request sent.");
	}

	private BufferedImage receiveScreenImage() throws IOException {
		int screenImageSize = receiveImagScreenImageSize();
		BufferedImage screenImage = receiveScreenImageData(screenImageSize);
		return screenImage;
	}

	private int receiveImagScreenImageSize() throws IOException {
		logger.debug("Receiving screen image size...");
		byte[] screenImageSizeBuffer = new byte[4];
		socket.getInputStream().read(screenImageSizeBuffer);
		int screenImageSize = ByteBuffer.wrap(screenImageSizeBuffer).asIntBuffer().get();
		logger.debug("Received screen image size: " + screenImageSize + " bytes.");
		return screenImageSize;
	}

	private BufferedImage receiveScreenImageData(int screenImageSize) throws IOException {
		logger.debug("Waiting screen image data...");
		byte[] imageScreenBuffer = new byte[screenImageSize];
		int read = 0;
		int total = 0;
		do {
			read = socket.getInputStream().read(imageScreenBuffer, total, screenImageSize - total);
			total += read;
		} while (total < screenImageSize);
		BufferedImage screenImage = ImageIO.read(new ByteArrayInputStream(imageScreenBuffer));
		logger.debug("Received screen image data.");
		return screenImage;
	}

	final static Logger logger = Logger.getLogger(ThreadClient.class);
}
