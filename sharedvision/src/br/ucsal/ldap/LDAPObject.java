package br.ucsal.ldap;

public class LDAPObject {

	private String cn;

	public LDAPObject(String cn) {
		super();
		this.cn = cn;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

}
