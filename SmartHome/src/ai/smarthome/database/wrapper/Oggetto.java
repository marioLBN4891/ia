package ai.smarthome.database.wrapper;

import java.io.Serializable;
import java.util.ArrayList;

import ai.smarthome.database.table.OggettiTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class Oggetto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String DISPENSA = "dispensa";
	public static final String MOBILE = "mobile";
	
	private String NOME;
	private String CLASSE;
	private int STATO;
	
	public Oggetto(String nome, String classe, int stato) {
		this.NOME = nome;
		this.CLASSE = classe;
		this.STATO = stato;
	}

	public void setNome(String nome) {
		this.NOME = nome;
	}
	
	public void setClasse(String classe) {
		this.CLASSE = classe;
	}
	
	public void setStato(int stato) {
		this.STATO = stato;
	}
	
	
	public String getNome() {
		return this.NOME;
	}
	public String getClasse() {
		return this.CLASSE;
	}
	public int getStato() {
		return this.STATO;
	}
	
	
	public static void setOggetto(SQLiteDatabase db, String nome, String classe, int stato) {
		ContentValues value = new ContentValues();
		value.put(OggettiTable.NOME, nome);
		value.put(OggettiTable.CLASSE, classe);
		value.put(OggettiTable.STATO, stato);
		db.insert(OggettiTable.TABLE_NAME, null, value);
	}

	public static void reset(SQLiteDatabase db) {
		ContentValues value = new ContentValues();
		value.put(OggettiTable.STATO, 0);
		db.update(OggettiTable.TABLE_NAME, value, null, null);
	}
	
	public static ArrayList<Oggetto> getLista(String classe, int stato, SQLiteDatabase db) {
		ArrayList<Oggetto> lista = new ArrayList<Oggetto>();
		Cursor cursore = db.query(OggettiTable.TABLE_NAME, OggettiTable.COLUMNS, OggettiTable.STATO+" = "+ stato + "AND "+OggettiTable.CLASSE+" = \""+ classe +"\"",	null, null, null, OggettiTable.NOME);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Oggetto(cursore.getString(1), cursore.getString(2), cursore.getInt(3)));
		return lista;
	}
	
	public static ArrayList<Oggetto> getAllLista(SQLiteDatabase db) {
		ArrayList<Oggetto> lista = new ArrayList<Oggetto>();
		Cursor cursore = db.query(OggettiTable.TABLE_NAME, OggettiTable.COLUMNS, null, null, null, null, OggettiTable.NOME);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Oggetto(cursore.getString(1), cursore.getString(2), cursore.getInt(3)));
		return lista;
	}
	
	public static void prendi(SQLiteDatabase db, String nome) {
		ContentValues value = new ContentValues();
		value.put(OggettiTable.STATO, 1);
		db.update(OggettiTable.TABLE_NAME, value, OggettiTable.NOME+" = \""+ nome+ "\"", null);
	}
	
	public static void lascia(SQLiteDatabase db, String nome) {
		ContentValues value = new ContentValues();
		value.put(OggettiTable.STATO, 0);
		db.update(OggettiTable.TABLE_NAME, value, OggettiTable.NOME+" = \""+ nome+ "\"", null);
	}
	
}
