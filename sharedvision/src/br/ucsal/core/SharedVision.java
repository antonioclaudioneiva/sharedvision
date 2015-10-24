package br.ucsal.core;

import java.awt.AWTException;

import org.apache.log4j.Logger;

import br.ucsal.client.Client;
import br.ucsal.client.ThreadClient;
import br.ucsal.gui.ScreenVisualizer;
import br.ucsal.ldap.LDAPObject;
import br.ucsal.ldap.LDAPService;
import br.ucsal.properties.OperationModeEnum;
import br.ucsal.properties.ParameterTypeEnum;
import br.ucsal.properties.PropertiesDefault;
import br.ucsal.properties.PropertiesEnum;
import br.ucsal.properties.PropertiesService;
import br.ucsal.screen.ScreenService;
import br.ucsal.server.Listener;
import br.ucsal.util.TimerService;

public class SharedVision {

	private OperationModeEnum operationMode = OperationModeEnum.STUDENT;

	private String host = null;

	private Integer port = 2000;

	public static void main(String[] args) {
		new SharedVision(args);
	}

	public SharedVision(String[] args) {
		initParameters(args);
		start();
	}

	private void start() {
		startServer();
		if (OperationModeEnum.STUDENT.equals(this.operationMode)) {
			startClient();
		}
	}

	private void startServer() {
		logger.info("Starting server...");
		try {
			ScreenService screenService = new ScreenService();
			screenService.start();
			TimerService.sleep(1000);

			new Listener(screenService, port).start();
			logger.info("Server started.");
		} catch (AWTException e) {
			logger.error("Start server failed:" + e.getMessage());
		}
	}

	private void startClient() {
		ScreenVisualizer screenVisualizer = new ScreenVisualizer();
		Client client = identifyClient();
		new ThreadClient(host, port, screenVisualizer, client).start();
	}

	private Client identifyClient() {
		String clientIdentification;
		LDAPObject ldapObject = new LDAPService().getLDAP();
		if (ldapObject != null) {
			clientIdentification = ldapObject.getCn();
		} else {
			clientIdentification = System.getProperty("user.name");
		}
		return new Client(clientIdentification);
	}

	private void initParameters(String[] parameters) {
		logger.debug("Initializing parameters...");
		for (String parameter : parameters) {
			try {
				String name = parameter.split(":")[0].substring(1).toUpperCase();
				String value = parameter.split(":")[1];
				setParameter(name, value);
			} catch (ArrayIndexOutOfBoundsException e) {
				logger.error("Parameter initialize failed: " + e.getMessage());
			}
		}
		logger.debug("Parameters initialized.");
		initMissingParameters();
	}

	private void initMissingParameters() {
		logger.debug("Initializing default parameters...");
		if (host == null) {
			host = PropertiesService.getProperty(PropertiesEnum.HOST);
			if (host == null) {
				host = PropertiesDefault.HOST_DEFAULT;
			}
		}
		logger.debug("Default parameters initialized.");
	}

	private void setParameter(String name, String value) {
		logger.debug("Setting parameter: " + name + "(" + value + ")");
		try {
			ParameterTypeEnum type = ParameterTypeEnum.valueOf(name);
			setParameter(type, value);
		} catch (IllegalArgumentException e) {
			logger.error("Invalid parameter type: " + name);
		}
	}

	private void setParameter(ParameterTypeEnum type, String value) {
		switch (type) {
		case MODE:
			this.operationMode = OperationModeEnum.valueOf(value);
			break;
		case HOST:
			this.host = value;
			break;
		case PORT:
			this.port = Integer.valueOf(value);
			break;
		default:
			break;
		}

	}

	final static Logger logger = Logger.getLogger(SharedVision.class);
}
