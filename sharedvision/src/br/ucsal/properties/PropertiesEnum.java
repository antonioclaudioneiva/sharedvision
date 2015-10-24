package br.ucsal.properties;

public enum PropertiesEnum {

	SYSTEM_VERSION("system.version"), 
	SCREEN_REFRESH_TIME("screen.service.refresh.time"), 
	LDAP_AVALIABLE("ldap.avaliable"),
	LDAP_URL("ldap.url"),
	LDAP_PASSWD_SERVICO("ldap.passwdServico"),
	LDAP_USER_SERVICO("ldap.userServico"),
	LDAP_FILTER("ldap.filter"), 
	HOST("host");

	private String key;
	
	private PropertiesEnum(String key){
		this.key = key;
	}

	public String getKey() {
		return key;
	}
	
}
