package br.ucsal.communication.client;

import java.io.Serializable;

public class Client implements Serializable {

	private static final long serialVersionUID = 1L;

	private String identification;

	private String hostAddress;

	public Client(String identification) {
		super();
		this.identification = identification;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public void setHostAddress(String host) {
		this.hostAddress = host;
	}

	@Override
	public String toString() {
		return "Client [identification=" + identification + ", hostAddress=" + hostAddress + "]";
	}

}
