package ai.smarthome.database.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ai.smarthome.database.table.OggettiTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class Oggetto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String DISPENSA = "Dispensa";
	public static final String MOBILE = "Mobile";
	public static final String FRIGORIFERO = "Frigorifero";
	public static final String CUCINA = "Cucina";
	
	private int ID;
	private String NOME;
	private String CLASSE;
	private int STATO;
	private String PROLOG;
	
	public Oggetto(int id, String nome, String classe, int stato, String prolog) {
		this.ID = id;
		this.NOME = nome;
		this.CLASSE = classe;
		this.STATO = stato;
		this.PROLOG = prolog;
	}

	public int getId() {
		return this.ID;
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
	public String getProlog() {
		return this.PROLOG;
	}
	
	public static void setConfigurazione(SQLiteDatabase db, String nome, String classe, int stato, String prolog) {
		ContentValues value = new ContentValues();
		value.put(OggettiTable.NOME, nome);
		value.put(OggettiTable.CLASSE, classe);
		value.put(OggettiTable.STATO, stato);
		value.put(OggettiTable.PROLOG, prolog);
		db.insert(OggettiTable.TABLE_NAME, null, value);
	}

	public static void reset(SQLiteDatabase db) {
		ContentValues value = new ContentValues();
		value.put(OggettiTable.STATO, 0);
		db.update(OggettiTable.TABLE_NAME, value, null, null);
	}
	
	public static void update(SQLiteDatabase db, String nome, boolean stato) {
		ContentValues value = new ContentValues();
		value.put(OggettiTable.STATO, (stato) ? 1 : 0);
		db.update(OggettiTable.TABLE_NAME, value, OggettiTable.NOME +" = \""+ nome+"\"", null);
	}
	
	public static ArrayList<Oggetto> getLista(String classe, int stato, SQLiteDatabase db) {
		ArrayList<Oggetto> lista = new ArrayList<Oggetto>();
		Cursor cursore = db.query(OggettiTable.TABLE_NAME, OggettiTable.COLUMNS, OggettiTable.STATO+" = "+ stato + " AND "+OggettiTable.CLASSE+" = \""+ classe +"\"",	null, null, null, OggettiTable.NOME);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Oggetto(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(3), cursore.getString(4)));
		return lista;
	}
	
	
	public static CharSequence[] getListaCharSequence(String tipo, int stato, SQLiteDatabase db) {
		List<String> listItems = new ArrayList<String>();
		Cursor cursore;
		if (tipo == null)
			cursore = db.query(OggettiTable.TABLE_NAME, OggettiTable.COLUMNS, OggettiTable.STATO+" = "+ stato,	null, null, null, OggettiTable.NOME);
		else
			cursore = db.query(OggettiTable.TABLE_NAME, OggettiTable.COLUMNS, OggettiTable.STATO+" = "+ stato + " AND "+OggettiTable.CLASSE+" = \""+ tipo +"\"",	null, null, null, OggettiTable.NOME);
		
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
				listItems.add(cursore.getString(1));
		
		CharSequence[] charSequenceItems = listItems.toArray(new CharSequence[listItems.size()]);;
		
		return charSequenceItems;
	}
	
	public static ArrayList<Oggetto> getAllLista(SQLiteDatabase db) {
		ArrayList<Oggetto> lista = new ArrayList<Oggetto>();
		Cursor cursore = db.query(OggettiTable.TABLE_NAME, OggettiTable.COLUMNS, null, null, null, null, OggettiTable.NOME);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Oggetto(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(3), cursore.getString(4)));
		return lista;
	}
	
	public static ArrayList<Oggetto> getListaPresi(SQLiteDatabase db) {
		ArrayList<Oggetto> lista = new ArrayList<Oggetto>();
		Cursor cursore = db.query(OggettiTable.TABLE_NAME, OggettiTable.COLUMNS, OggettiTable.STATO+" = "+ 1, null, null, null, OggettiTable.NOME);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Oggetto(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(3), cursore.getString(4)));
		return lista;
	}
	
	public static void cambiaStato(SQLiteDatabase db, String nome, int stato) {
		ContentValues value = new ContentValues();
		value.put(OggettiTable.STATO, stato);
		db.update(OggettiTable.TABLE_NAME, value, OggettiTable.NOME+" = \""+ nome+ "\"", null);
	}
	
	public static String getProlog(SQLiteDatabase db, String nome) {
		Cursor cursore = db.query(OggettiTable.TABLE_NAME, OggettiTable.COLUMNS, OggettiTable.NOME+" = \""+ nome+"\"", null, null, null, OggettiTable.NOME);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					return cursore.getString(4);
		return null;
	}
	
	public static boolean checkOggetto(String tipo, int stato, SQLiteDatabase db) {
		Cursor cursore;
		if (tipo == null) 
			cursore = db.query(OggettiTable.TABLE_NAME, OggettiTable.COLUMNS, OggettiTable.STATO+" = "+ stato,	null, null, null, OggettiTable.NOME);
		else
			cursore = db.query(OggettiTable.TABLE_NAME, OggettiTable.COLUMNS, OggettiTable.STATO+" = "+ stato + " AND "+OggettiTable.CLASSE+" = \""+ tipo +"\"",	null, null, null, OggettiTable.NOME);
		
		if (cursore.getCount() > 0)
			return true;
		else
			return false;
	}
}
