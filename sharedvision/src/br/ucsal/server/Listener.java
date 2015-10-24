package br.ucsal.server;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.log4j.Logger;

import br.ucsal.screen.ScreenService;

public class Listener extends Thread {

	private ScreenService screenService;
	private Integer port;

	public Listener(ScreenService screenService, Integer port) {
		this.screenService = screenService;
		this.port = port;
	}

	public void run() {
		ServerSocket listener = null;
		try {
			logger.info("Listener starting (port "+port+")...");
			listener = new ServerSocket(port);
			logger.info("Listener started.");
			while (true) {
				new ThreadServer(listener.accept(), screenService).start();
			}
		} catch (IOException e) {
			logger.error("Listener start error:"+e.getMessage());
		} finally {
			if (listener != null) {
				try {
					logger.info("Listener stopping...");
					listener.close();
					logger.info("Listener stopped.");
				} catch (IOException e) {
					logger.error("Listener stop error:"+e.getMessage());
				}
			}
		}
	};

	final static Logger logger = Logger.getLogger(Listener.class);
}
