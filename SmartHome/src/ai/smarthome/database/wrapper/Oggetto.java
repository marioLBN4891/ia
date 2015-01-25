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
	
	private int ID;
	private String NOME;
	private String CLASSE;
	private int STATO;
	
	public Oggetto(int id, String nome, String classe, int stato) {
		this.ID = id;
		this.NOME = nome;
		this.CLASSE = classe;
		this.STATO = stato;
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
	
	
	public static void setConfigurazione(SQLiteDatabase db, String nome, String classe, int stato) {
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
					lista.add(new Oggetto(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(3)));
		return lista;
	}
	
	
	public static CharSequence[] getListaCharSequence(SQLiteDatabase db, int stato) {
		List<String> listItems = new ArrayList<String>();
		if(stato == 0) {
			if(Componente.checkDispensaMobileAperta(Componente.APERTO_CHIUSO, 1, Oggetto.DISPENSA, db)) {
				Cursor cursore = db.query(OggettiTable.TABLE_NAME, OggettiTable.COLUMNS, OggettiTable.STATO+" = "+ stato + " AND "+OggettiTable.CLASSE+" = \""+ Oggetto.DISPENSA +"\"",	null, null, null, OggettiTable.NOME);
				if (cursore.getCount() > 0)
					while (cursore.moveToNext()) 
						listItems.add(cursore.getString(1));
			}
			if(Componente.checkDispensaMobileAperta(Componente.APERTO_CHIUSO, 1, Oggetto.MOBILE, db)) {
				Cursor cursore = db.query(OggettiTable.TABLE_NAME, OggettiTable.COLUMNS, OggettiTable.STATO+" = "+ stato + " AND "+OggettiTable.CLASSE+" = \""+ Oggetto.MOBILE +"\"",	null, null, null, OggettiTable.NOME);
				if (cursore.getCount() > 0)
					while (cursore.moveToNext()) 
						listItems.add(cursore.getString(1));
			}
		}
		if (stato == 1) {
			Cursor cursore = db.query(OggettiTable.TABLE_NAME, OggettiTable.COLUMNS, OggettiTable.STATO+" = "+ stato,	null, null, null, OggettiTable.NOME);
			if (cursore.getCount() > 0)
				while (cursore.moveToNext()) 
					listItems.add(cursore.getString(1));
		}
		CharSequence[] charSequenceItems = listItems.toArray(new CharSequence[listItems.size()]);;
		
		return charSequenceItems;
	}
	
	public static ArrayList<Oggetto> getAllLista(SQLiteDatabase db) {
		ArrayList<Oggetto> lista = new ArrayList<Oggetto>();
		Cursor cursore = db.query(OggettiTable.TABLE_NAME, OggettiTable.COLUMNS, null, null, null, null, OggettiTable.NOME);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Oggetto(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(3)));
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
