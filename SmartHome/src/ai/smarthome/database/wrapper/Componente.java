package ai.smarthome.database.wrapper;

import java.util.ArrayList;
import java.util.List;

import ai.smarthome.database.table.ComponentiTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Componente {

	public static final String ACCESO_SPENTO = "accceso/spento";
	public static final String APERTO_CHIUSO = "aperto/chiuso";
	
	private int ID;
	private String NOME;
	private String TIPO;
	private int STATO;
	private String PROLOG;
	
	public Componente(int id, String nome, String tipo, int stato, String prolog) {
		this.ID = id;
		this.NOME = nome;
		this.STATO = stato;
		this.TIPO = tipo;
		this.PROLOG = prolog;
	}
	
	public int getId() {
		return this.ID;
	}
	public String getNome() {
		return this.NOME;
	}
	
	public String getTipo() {
		return this.TIPO;
	}
	
	public int getStato() {
		return this.STATO;
	}
	
	public String getProlog() {
		return this.PROLOG;
	}

	public static void setConfigurazione(SQLiteDatabase db, String nome, String tipo, int stato, String prolog) {
		ContentValues value = new ContentValues();
		value.put(ComponentiTable.NOME, nome);
		value.put(ComponentiTable.TIPO, tipo);
		value.put(ComponentiTable.STATO, stato);
		value.put(ComponentiTable.PROLOG, prolog);
		db.insert(ComponentiTable.TABLE_NAME, null, value);
	}

	public static void reset(SQLiteDatabase db) {
		ContentValues value = new ContentValues();
		value.put(ComponentiTable.STATO, 0);
		db.update(ComponentiTable.TABLE_NAME, value, null, null);
	}
	
	public static void update(SQLiteDatabase db, String nome, boolean stato) {
		ContentValues value = new ContentValues();
		value.put(ComponentiTable.STATO, (stato) ? 1 : 0);
		db.update(ComponentiTable.TABLE_NAME, value, ComponentiTable.NOME +" = \""+ nome+"\"", null);
	}
	
	public static ArrayList<Componente> getLista(String tipo, int stato, SQLiteDatabase db) {
		ArrayList<Componente> lista = new ArrayList<Componente>();
		Cursor cursore = db.query(ComponentiTable.TABLE_NAME, ComponentiTable.COLUMNS, ComponentiTable.STATO+" = "+ stato + " AND "+ComponentiTable.TIPO+" = \""+ tipo +"\"",	null, null, null, ComponentiTable.NOME);
		
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Componente(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(3), cursore.getString(4)));
		
		return lista;
	}
	
	public static CharSequence[] getListaCharSequence(String tipo, int stato, SQLiteDatabase db) {
		List<String> listItems = new ArrayList<String>();
		Cursor cursore = db.query(ComponentiTable.TABLE_NAME, ComponentiTable.COLUMNS, ComponentiTable.STATO+" = "+ stato + " AND "+ComponentiTable.TIPO+" = \""+ tipo +"\"",	null, null, null, ComponentiTable.NOME);
		
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
				listItems.add(cursore.getString(1));
		
		CharSequence[] charSequenceItems = listItems.toArray(new CharSequence[listItems.size()]);;
		
		return charSequenceItems;
	}
	
	public static ArrayList<Componente> getAllLista(SQLiteDatabase db) {
		ArrayList<Componente> lista = new ArrayList<Componente>();
		Cursor cursore = db.query(ComponentiTable.TABLE_NAME, ComponentiTable.COLUMNS, null, null, null, null, ComponentiTable.NOME);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Componente(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(3), cursore.getString(4)));
		return lista;
	}
	
	public static void cambiaStato(SQLiteDatabase db, String nome, int stato) {
		ContentValues value = new ContentValues();
		value.put(ComponentiTable.STATO, stato);
		db.update(ComponentiTable.TABLE_NAME, value, ComponentiTable.NOME+" = \""+ nome+ "\"", null);
	}
	
	public static String getProlog(SQLiteDatabase db, String nome) {
		Cursor cursore = db.query(ComponentiTable.TABLE_NAME, ComponentiTable.COLUMNS, ComponentiTable.NOME+" = \""+ nome+"\"", null, null, null, ComponentiTable.NOME);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					return cursore.getString(4);
		return null;
	}
	
	public static boolean checkComponente(String tipo, int stato, SQLiteDatabase db) {
		Cursor cursore = db.query(ComponentiTable.TABLE_NAME, ComponentiTable.COLUMNS, ComponentiTable.STATO+" = "+ stato + " AND "+ComponentiTable.TIPO+" = \""+ tipo +"\"",	null, null, null, ComponentiTable.NOME);
		if (cursore.getCount() > 0)
			return true;
		else
			return false;
	}
	
	
	
}
