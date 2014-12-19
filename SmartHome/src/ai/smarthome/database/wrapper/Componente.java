package ai.smarthome.database.wrapper;

import java.util.ArrayList;

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
	
	public Componente(int id, String nome, String tipo, int stato) {
		this.ID = id;
		this.NOME = nome;
		this.STATO = stato;
		this.TIPO = tipo;
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

	public static void setConfigurazione(SQLiteDatabase db, String nome, String tipo, int stato) {
		ContentValues value = new ContentValues();
		value.put(ComponentiTable.NOME, nome);
		value.put(ComponentiTable.TIPO, tipo);
		value.put(ComponentiTable.STATO, stato);
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
		Cursor cursore = db.query(ComponentiTable.TABLE_NAME, ComponentiTable.COLUMNS, ComponentiTable.STATO+" = "+ stato + "AND "+ComponentiTable.TIPO+" = \""+ tipo +"\"",	null, null, null, ComponentiTable.NOME);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Componente(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(3)));
		return lista;
	}
	
	public static ArrayList<Componente> getAllLista(SQLiteDatabase db) {
		ArrayList<Componente> lista = new ArrayList<Componente>();
		Cursor cursore = db.query(ComponentiTable.TABLE_NAME, ComponentiTable.COLUMNS, null, null, null, null, ComponentiTable.NOME);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Componente(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(3)));
		return lista;
	}
	
	public static void apri(SQLiteDatabase db, String nome) {
		ContentValues value = new ContentValues();
		value.put(ComponentiTable.STATO, 1);
		db.update(ComponentiTable.TABLE_NAME, value, ComponentiTable.NOME+" = \""+ nome+ "\"", null);
	}
	
	public static void chiudi(SQLiteDatabase db, String nome) {
		ContentValues value = new ContentValues();
		value.put(ComponentiTable.STATO, 0);
		db.update(ComponentiTable.TABLE_NAME, value, ComponentiTable.NOME+" = \""+ nome+ "\"", null);
	}
	
	public static void accendi(SQLiteDatabase db, String nome) {
		ContentValues value = new ContentValues();
		value.put(ComponentiTable.STATO, 1);
		db.update(ComponentiTable.TABLE_NAME, value, ComponentiTable.NOME+" = \""+ nome+ "\"", null);
	}
	
	public static void spegni(SQLiteDatabase db, String nome) {
		ContentValues value = new ContentValues();
		value.put(ComponentiTable.STATO, 0);
		db.update(ComponentiTable.TABLE_NAME, value, ComponentiTable.NOME+" = \""+ nome+ "\"", null);
	}
	
}
