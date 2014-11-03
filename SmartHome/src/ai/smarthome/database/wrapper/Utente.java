package ai.smarthome.database.wrapper;

import java.io.Serializable;

public class Utente implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String ID;
	private String USERNAME;
	private String PASSWORD;
	private String COGNOME;
	private String NOME;
	private String MAIL;
	

	public Utente (String id, String username, String password, String cognome, String nome, String mail) {
		this.ID = id;
		this.USERNAME = username;
		this.PASSWORD = password;
		this.COGNOME = cognome;
		this.NOME = nome;
		this.MAIL = mail;
		
	}
	
	public void setId(String id) {
		this.ID = id;
	}
	
	public void setUsername(String username) {
		this.USERNAME = username;
	}
	
	public void setPassword(String password) {
		this.PASSWORD = password;
	}
	
	public void setCognome(String cognome) {
		this.COGNOME = cognome;
	}
	
	public void setNome(String nome) {
		this.NOME = nome;
	}
	
	public void setMail(String mail) {
		this.MAIL = mail;
	}
	
	public String getId() {
		return this.ID;
	}
	
	public String getUsername() {
		return this.USERNAME;
	}
	
	public String getPassword() {
		return this.PASSWORD;
	}
	
	public String getCognome() {
		return this.COGNOME;
	}
	
	public String getNome() {
		return this.NOME;
	}
	
	public String getMail() {
		return this.MAIL;
	}
}
