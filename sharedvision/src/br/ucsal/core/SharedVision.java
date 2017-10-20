package br.ucsal.core;

import java.awt.AWTException;
import java.io.IOException;

import org.apache.log4j.Logger;

import br.ucsal.communication.client.Client;
import br.ucsal.communication.client.ThreadClient;
import br.ucsal.communication.server.Listener;
import br.ucsal.gui.ScreenVisualizer;
import br.ucsal.gui.SysTray;
import br.ucsal.ldap.LDAPObject;
import br.ucsal.ldap.LDAPService;
import br.ucsal.properties.OperationModeEnum;
import br.ucsal.properties.ParameterTypeEnum;
import br.ucsal.properties.PropertiesDefault;
import br.ucsal.properties.PropertiesEnum;
import br.ucsal.properties.PropertiesService;
import br.ucsal.screen.ScreenService;
import br.ucsal.util.TimerService;

public class SharedVision {

	private OperationModeEnum operationMode = OperationModeEnum.STUDENT;

	private String teacherHost = null;

	private Integer teacherServerPort = 2000;

	// FIXME Parametrizar essa porta.
	private Integer studentServerPort = 2001;

	private static SharedVision instance = null;

	public static void main(String[] args) {
		new SharedVision(args);
	}

	public SharedVision(String[] args) {
		instance = this;
		initParameters(args);
		start();
	}

	public static SharedVision getInstance() {
		return instance;
	}

	public OperationModeEnum getOperationMode() {
		return operationMode;
	}

	public Integer getStudentServerPort() {
		return studentServerPort;
	}

	private void start() {
		startServer();
		if (OperationModeEnum.STUDENT.equals(this.operationMode)) {
			startClient();
		} else {
			SysTray sysTray = new SysTray();
			try {
				sysTray.createSysTray();
			} catch (IOException e) {
				logger.error("SysTray startup failed.");
			}
		}
	}

	private void startServer() {
		logger.info("Starting server...");
		try {
			ScreenService screenService = new ScreenService();
			screenService.start();
			TimerService.sleep(1000);
			if (OperationModeEnum.TEACHER.equals(operationMode)) {
				new Listener(screenService, teacherServerPort).start();
			} else {
				new Listener(screenService, studentServerPort).start();
			}
			logger.info("Server started.");
		} catch (AWTException e) {
			logger.error("Start server failed:" + e.getMessage());
		}
	}

	private void startClient() {
		ScreenVisualizer screenVisualizer = new ScreenVisualizer();
		Client client = identifyClient();
		new ThreadClient(teacherHost, teacherServerPort, screenVisualizer, client).start();
	}

	private void setHost(String value) {
		teacherHost = value;
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
		if (teacherHost == null) {
			teacherHost = PropertiesService.getProperty(PropertiesEnum.HOST);
			if (teacherHost == null) {
				teacherHost = PropertiesDefault.HOST_DEFAULT;
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
			setHost(value);
			break;
		case PORT:
			this.teacherServerPort = Integer.valueOf(value);
			break;
		default:
			break;
		}

	}

	final static Logger logger = Logger.getLogger(SharedVision.class);
}
