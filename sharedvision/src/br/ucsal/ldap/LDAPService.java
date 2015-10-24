package br.ucsal.ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

import br.ucsal.client.ThreadClient;
import br.ucsal.properties.PropertiesEnum;
import br.ucsal.properties.PropertiesService;

public class LDAPService {

	private DirContext dirContext;
	
	private LDAPObject lDAPObject = null;

	private SearchControls ctrl;

	public LDAPService() {
		if (PropertiesService.isLDAPAvaliable()) {
			if (configureLDAPContext()) {
				retrieveLDAPObject();
			}
		}
	}

	public LDAPObject getLDAP() {
		return lDAPObject;
	}

	private void retrieveLDAPObject() {
		logger.info("Retrieving LDAP object...");
		String userName = System.getProperty("user.name");
		String filter = PropertiesService.getProperty(PropertiesEnum.LDAP_FILTER);
		filter = filter.replaceAll("%username%", userName);
		NamingEnumeration<SearchResult> enumeration;
		try {
			enumeration = dirContext.search("dc=serpro,dc=gov,dc=br", filter, ctrl);
			if (enumeration.hasMore()) {
				SearchResult rs = (SearchResult) enumeration.next();
				Attributes attrs = rs.getAttributes();
				lDAPObject = new LDAPObject((String) attrs.get("cn").get());
				logger.info("LDAP object retrieved.");
			} else {
				logger.error("Retrieve LDAP object failed: no result found.");
			}
		} catch (NamingException e) {
			logger.error("Retrieve LDAP object failed: "+e.getMessage());
		}
	}

	private Boolean configureLDAPContext() {
		logger.info("Configuring LDAP Context...");
		String url = PropertiesService.getProperty(PropertiesEnum.LDAP_URL);
		String passwdServico = PropertiesService.getProperty(PropertiesEnum.LDAP_PASSWD_SERVICO);
		String userServico = PropertiesService.getProperty(PropertiesEnum.LDAP_USER_SERVICO);

		Hashtable<String, String> env = new Hashtable<>();
		env.put(Context.PROVIDER_URL, url);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, userServico);
		env.put(Context.SECURITY_CREDENTIALS, passwdServico);
		ctrl = new SearchControls();
		ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
		logger.info("LDAP Contexct configured.");
		try {
			dirContext = new InitialDirContext(env);
			logger.info("LDAP Context configured.");
			return true;
		} catch (NamingException e) {
			logger.info("LDAP Context configuration failed: " + e.getMessage());
			return false;
		}
	}

	// Logger
	final static Logger logger = Logger.getLogger(ThreadClient.class);
}