package pe.confianza.preevaluadorapi.bean;

public class ApiBodyAutoriza {
	
	private String username;
	private String password;
	private String urlApiOauth;
	private String clientID;
	private String clientSecred;
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrlApiOauth() {
		return urlApiOauth;
	}
	public void setUrlApiOauth(String urlApiOauth) {
		this.urlApiOauth = urlApiOauth;
	}
	/**
	 * @return the clientID
	 */
	public String getClientID() {
		return clientID;
	}
	/**
	 * @param clientID the clientID to set
	 */
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	/**
	 * @return the clientSecred
	 */
	public String getClientSecred() {
		return clientSecred;
	}
	/**
	 * @param clientSecred the clientSecred to set
	 */
	public void setClientSecred(String clientSecred) {
		this.clientSecred = clientSecred;
	}
	
	
	

}
