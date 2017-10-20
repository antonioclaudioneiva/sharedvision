package br.ucsal.communication.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import br.ucsal.communication.client.Client;
import br.ucsal.communication.client.ThreadClient;
import br.ucsal.core.SharedVision;
import br.ucsal.properties.OperationModeEnum;
import br.ucsal.screen.KeyHook;
import br.ucsal.screen.ScreenService;

public class ThreadServer extends Thread {

	public static final int REQUEST_SCREEN_IMAGE = 1;

	public static final int REQUEST_LOCK_KEYS = 2;

	public static final int REQUEST_UNLOCK_KEYS = 3;

	public static final int WELCOME_CODE = 2;

	private Socket socket;

	private ScreenService screenService;

	// FIXME Refatorar!
	public Map<Integer, ThreadClient> studentsConnecteds = new HashMap<>();

	// FIXME Refatorar!
	public static ThreadServer instance = null;

	public ThreadServer(Socket socket, ScreenService screenService) {
		instance = this;
		this.socket = socket;
		this.screenService = screenService;
		logger.info("Connected client: " + this.socket.getInetAddress().getHostAddress() + ".");
		if (OperationModeEnum.TEACHER.equals(SharedVision.getInstance().getOperationMode())) {
			connectStudentBack(this.socket.getInetAddress().getHostAddress());
		}
		sendWelcomeMessage();
		receiveClientIdentification(this.socket.getInetAddress().getHostAddress());
	}

	private void connectStudentBack(String clientHostAddress) {
		Integer lastNumberIp = obterUltimoByteIp(clientHostAddress);
		ThreadClient threadClient = new ThreadClient(clientHostAddress, 2001, null, null);
		studentsConnecteds.put(lastNumberIp, threadClient);
	}

	private Integer obterUltimoByteIp(String clientHostAddress) {
		Integer pos = clientHostAddress.indexOf(".");
		pos = clientHostAddress.indexOf(".", pos + 1);
		pos = clientHostAddress.indexOf(".", pos + 1);
		String lastNumberIp = clientHostAddress.substring(pos + 1);
		// FIXME Testes!!! Retirar!!!
		if (lastNumberIp.equals("86")) {
			lastNumberIp = "12";
		}
		System.out.println("lastNumberIp=" + lastNumberIp);
		return Integer.parseInt(lastNumberIp);
	}

	private void sendWelcomeMessage() {
		logger.debug("Sending welcome message...");
		try {
			new DataOutputStream(socket.getOutputStream()).writeInt(WELCOME_CODE);
			socket.getOutputStream().flush();
			logger.debug("Welcome message sent.");
		} catch (IOException e) {
			logger.error("Welcome message sent failed: " + e.getMessage());
		}
	}

	private void receiveClientIdentification(String hostAddress) {
		logger.debug("Receiving client identification...");
		ObjectInputStream objectInputStream;
		try {
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			Client client = (Client) objectInputStream.readObject();
			client.setHostAddress(hostAddress);
			logger.info("Received client identification: " + client);
		} catch (Exception e) {
			logger.error("Client identification receipt failed: " + e.getMessage());
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
				logger.error("Waiting clientRequestCode failed: " + e.getMessage());
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

		case REQUEST_LOCK_KEYS:
			logger.info("Locking keys...");
			KeyHook.unblockWindowsKey();
			logger.debug("Keys lockeds.");
			break;

		case REQUEST_UNLOCK_KEYS:
			logger.info("Unlocking keys...");
			KeyHook.blockWindowsKey();
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
		logger.debug("Sending screen image size: " + screenImageByte.length + " bytes");
		sendScreenImageData(size);
		socket.getOutputStream().flush();
		logger.debug("Screen screen image size sent.");
	}

	final static Logger logger = Logger.getLogger(ThreadServer.class);
}
