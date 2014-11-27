package ai.smarthome.database.wrapper;

import java.io.Serializable;

import ai.smarthome.database.table.ConnessioneVeloceTable;
import ai.smarthome.database.table.UtentiTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Utente implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String ID;
	private String MAIL;
	private String PASSWORD;
	private String COGNOME;
	private String NOME;
	
	public Utente (String id, String mail, String password, String cognome, String nome) {
		this.ID = id;
		this.MAIL = mail;
		this.PASSWORD = password;
		this.COGNOME = cognome;
		this.NOME = nome;
	}
	
	
	public static void setUtente(SQLiteDatabase db, String mail, String password, String cognome, String nome) {
		ContentValues value = new ContentValues();
		value.put(UtentiTable.MAIL, mail);
		value.put(UtentiTable.PASSWORD, password);
		value.put(UtentiTable.COGNOME, cognome);
		value.put(UtentiTable.NOME, nome);
		db.insert(UtentiTable.TABLE_NAME, null, value);
	}
	
	public static void updateUtente(SQLiteDatabase db, String mail, String password) {
		ContentValues value = new ContentValues();
		value.put(UtentiTable.PASSWORD, password);
		db.update(UtentiTable.TABLE_NAME, value, UtentiTable.MAIL+" = \""+ mail + "\"", null);
	}		
	
	public static void setConnessioneVeloce(SQLiteDatabase db, String mail, String password) {
		ContentValues value = new ContentValues();
		value.put(ConnessioneVeloceTable.MAIL, mail);
		value.put(ConnessioneVeloceTable.PASSWORD, password);
		db.delete(ConnessioneVeloceTable.TABLE_NAME, null, null);
		db.insert(ConnessioneVeloceTable.TABLE_NAME, null, value);
	}
		
	public static void deleteConnessioneVeloce(SQLiteDatabase db) {
		db.delete(ConnessioneVeloceTable.TABLE_NAME, null, null);
	}
		
	public static Utente getConnessioneVeloce(SQLiteDatabase db) {
		Cursor cursore = db.query(
			ConnessioneVeloceTable.TABLE_NAME, 			// nome della tabella
			ConnessioneVeloceTable.COLUMNS, 			// array dei nomi delle colonne da ritornare
			null,										// filtro da applicare ai dati 
			null,										// argomenti su cui filtrare i dati (nel caso in cui nel filtro siano presenti parametri)
			null, 										// group by da eseguire
			null, 										// clausola having da usare
			ConnessioneVeloceTable.MAIL);			// ordinamento da applicare ai dati
			
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) {
				return isUtenteRegistered(db, cursore.getString(1), cursore.getString(2));
			}
			
		return null;
	}
	
	public static Cursor geListatUtenti(SQLiteDatabase db) {
		
		return db.query(
			UtentiTable.TABLE_NAME, 					// nome della tabella
			UtentiTable.COLUMNS, 						// array dei nomi delle colonne da ritornare
			null,										// filtro da applicare ai dati 
			null,										// argomenti su cui filtrare i dati (nel caso in cui nel filtro siano presenti parametri)
			null, 										// group by da eseguire
			null, 										// clausola having da usare
			UtentiTable.MAIL);						// ordinamento da applicare ai dati
	}
	
	public static Utente isUtenteRegistered(SQLiteDatabase db, String mail, String password) {
		
		Cursor cursore = db.query(
			UtentiTable.TABLE_NAME, 					// nome della tabella
			UtentiTable.COLUMNS, 						// array dei nomi delle colonne da ritornare
			UtentiTable.MAIL+" = \""+ mail + "\" AND " + UtentiTable.PASSWORD +" = \""+ password +"\"",										// filtro da applicare ai dati 
			null,										// argomenti su cui filtrare i dati (nel caso in cui nel filtro siano presenti parametri)
			null, 										// group by da eseguire
			null, 										// clausola having da usare
			null);						// ordinamento da applicare ai dati
		
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) {
				return (new Utente(cursore.getString(0), cursore.getString(1), cursore.getString(2), cursore.getString(3), cursore.getString(4)));
			}
		
		return null;
	}
	
	public static Utente sendMailToUtenteRegistered(SQLiteDatabase db, String mail) {
		
		Cursor cursore = db.query(
			UtentiTable.TABLE_NAME, 					// nome della tabella
			UtentiTable.COLUMNS, 						// array dei nomi delle colonne da ritornare
			UtentiTable.MAIL +" = \""+ mail +"\"",										// filtro da applicare ai dati 
			null,										// argomenti su cui filtrare i dati (nel caso in cui nel filtro siano presenti parametri)
			null, 										// group by da eseguire
			null, 										// clausola having da usare
			null);						// ordinamento da applicare ai dati
		
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) {
				return (new Utente(cursore.getString(0), cursore.getString(1), cursore.getString(2), cursore.getString(3), cursore.getString(4)));
			}
		
		return null;
	}
	
	public static boolean isMailRegistered(SQLiteDatabase db, String mail) {
		
		Cursor cursore = db.query(
			UtentiTable.TABLE_NAME, 					// nome della tabella
			UtentiTable.COLUMNS, 						// array dei nomi delle colonne da ritornare
			UtentiTable.MAIL+" = \""+ mail +"\"",										// filtro da applicare ai dati 
			null,										// argomenti su cui filtrare i dati (nel caso in cui nel filtro siano presenti parametri)
			null, 										// group by da eseguire
			null, 										// clausola having da usare
			UtentiTable.MAIL);						// ordinamento da applicare ai dati
		
		if (cursore.getCount() > 0)
			return true;
		else
			return false;
	}

	public static void printLogUtenti(SQLiteDatabase db) {
		Cursor cursore = geListatUtenti(db);
		
		try {
    		while (cursore.moveToNext()) 
    			Log.i("SHE_Utenti", cursore.getLong(0) + " " + cursore.getString(1) + "\t " + cursore.getString(2)+ "\t " + cursore.getString(3)+ "\t " + cursore.getString(4));
    	}  
    	finally {
    		cursore.close();
    	}
	}
	
	
	
	public void setId(String id) {
		this.ID = id;
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
