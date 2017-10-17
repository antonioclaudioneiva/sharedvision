package br.ucsal.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import br.ucsal.client.Client;
import br.ucsal.screen.KeyHook;
import br.ucsal.screen.ScreenService;

public class ThreadServer extends Thread {

	public static final int REQUEST_SCREEN_IMAGE = 1;

	public static final int REQUEST_UNLOCK_KEYS = 3;
	
	public static final int WELCOME_CODE = 2;

	private Socket socket;

	private ScreenService screenService;

	public ThreadServer(Socket socket, ScreenService screenService) {
		this.socket = socket;
		this.screenService = screenService;
		logger.info("Connected client: " + this.socket.getInetAddress().getHostAddress()+".");
		sendWelcomeMessage();
		receiveClientIdentification(this.socket.getInetAddress().getHostAddress());
	}

	private void sendWelcomeMessage() {
		logger.debug("Sending welcome message...");
		try {
			new DataOutputStream(socket.getOutputStream()).writeInt(WELCOME_CODE);
			socket.getOutputStream().flush();
			logger.debug("Welcome message sent.");
		} catch (IOException e) {
			logger.error("Welcome message sent failed: "+e.getMessage());
		}
	}

	private void receiveClientIdentification(String hostAddress) {
		logger.debug("Receiving client identification...");
		ObjectInputStream objectInputStream;
		try {
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			Client client = (Client) objectInputStream.readObject();
			client.setHostAddress(hostAddress);
			logger.info("Received client identification: "+client);
		} catch (Exception e) {
			logger.error("Client identification receipt failed: " +e.getMessage());
		}
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				byte[] clientRequestCode = new byte[1];
				logger.debug("Waiting clientRequestCode...");
				socket.getInputStream().read(clientRequestCode);
				logger.debug("Received clientRequestCode (" + clientRequestCode[0] + ").");
				executeClientRequest(clientRequestCode[0]);
			} catch (IOException e) {
				logger.error("Waiting clientRequestCode failed: "+e.getMessage());
			}
		}
	}

	private void executeClientRequest(byte clientRequestCode) throws IOException {
		switch (clientRequestCode) {
		case REQUEST_SCREEN_IMAGE:
			logger.debug("Sending screen image...");
			sendScreenImage();
			logger.debug("Screen image sent.");
			break;

		case REQUEST_UNLOCK_KEYS:
			logger.debug("Unlocking keys...");
			KeyHook.unblockWindowsKey();
			logger.debug("Keys unlockeds.");
			break;

		default:
			break;
		}
	}

	private void sendScreenImage() throws IOException {
		byte[] screenImageByte = screenService.getScreenImageByte();
		byte[] size = ByteBuffer.allocate(4).putInt(screenImageByte.length).array();
		sendScreenImageSize(screenImageByte, size);
        sendScreenImageData(screenImageByte);
	}

	private void sendScreenImageData(byte[] screenImageByte) throws IOException {
		logger.debug("Sending screen image data...");
		socket.getOutputStream().write(screenImageByte);
		socket.getOutputStream().flush();
		logger.debug("Screen screen image data.");
	}

	private void sendScreenImageSize(byte[] screenImageByte, byte[] size) throws IOException {
		logger.debug("Sending screen image size: "+screenImageByte.length+" bytes");
        sendScreenImageData(size);
		socket.getOutputStream().flush();
		logger.debug("Screen screen image size sent.");
	}

	final static Logger logger = Logger.getLogger(ThreadServer.class);
}
