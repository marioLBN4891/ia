package ai.smarthome.database.wrapper;

import java.io.Serializable;
import java.util.ArrayList;

import ai.smarthome.database.table.OggettiTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class Oggetto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String NOME;
	
	public Oggetto(String nome) {
		this.NOME = nome;
	}

	public void setNome(String nome) {
		this.NOME = nome;
	}
	
	public String getNome() {
		return this.NOME;
	}
	
	
	public static void setOggetto(SQLiteDatabase db, String nome, int preso) {
		ContentValues value = new ContentValues();
		value.put(OggettiTable.NOME, nome);
		value.put(OggettiTable.PRESO, preso);
		db.insert(OggettiTable.TABLE_NAME, null, value);
	}

	public static Cursor getListaOggetti(SQLiteDatabase db) {
		return db.query(OggettiTable.TABLE_NAME, OggettiTable.COLUMNS, null, null, null, null, OggettiTable.NOME);			
	}	
	
	
	public static void configuraListaOggetti(SQLiteDatabase db) {
		ContentValues value = new ContentValues();
		value.put(OggettiTable.PRESO, 1);
		db.update(OggettiTable.TABLE_NAME, value, null, null);
	}
	
	public static ArrayList<Oggetto> getListaOggettiPresi(SQLiteDatabase db) {
		ArrayList<Oggetto> lista = new ArrayList<Oggetto>();
		Cursor cursore = db.query(OggettiTable.TABLE_NAME, OggettiTable.COLUMNS, OggettiTable.PRESO+" = 1",	null, null, null, OggettiTable.NOME);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Oggetto(cursore.getString(2)));
		return lista;
	}
	
	public static ArrayList<Oggetto> getListaOggettiDaPrendere(SQLiteDatabase db) {
		ArrayList<Oggetto> lista = new ArrayList<Oggetto>();
		Cursor cursore = db.query(OggettiTable.TABLE_NAME, OggettiTable.COLUMNS, OggettiTable.PRESO+" = 0",	null, null, null, OggettiTable.NOME);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Oggetto(cursore.getString(2)));
		return lista;
	}
	
	public static void prendiOggetto(SQLiteDatabase db, String nome) {
		ContentValues value = new ContentValues();
		value.put(OggettiTable.PRESO, 1);
		db.update(OggettiTable.TABLE_NAME, value, OggettiTable.NOME+" = \""+ nome+ "\"", null);
	}
	
	public static void lasciaOggetti(SQLiteDatabase db, String nome) {
		ContentValues value = new ContentValues();
		value.put(OggettiTable.PRESO, 0);
		db.update(OggettiTable.TABLE_NAME, value, OggettiTable.NOME+" = \""+ nome+ "\"", null);
	}
	
}
