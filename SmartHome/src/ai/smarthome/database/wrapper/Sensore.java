package ai.smarthome.database.wrapper;

import java.util.ArrayList;

import ai.smarthome.database.table.SensoriTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Sensore {

	public static final String ACCESO_SPENTO = "accceso/spento";
	public static final String APERTO_CHIUSO = "aperto/chiuso";
	
	private int ID;
	private String NOME;
	private String TIPO;
	private int STATO;
	
	public Sensore(int id, String nome, String tipo, int stato) {
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
		value.put(SensoriTable.NOME, nome);
		value.put(SensoriTable.TIPO, tipo);
		value.put(SensoriTable.STATO, stato);
		db.insert(SensoriTable.TABLE_NAME, null, value);
	}

	public static void reset(SQLiteDatabase db) {
		ContentValues value = new ContentValues();
		value.put(SensoriTable.STATO, 0);
		db.update(SensoriTable.TABLE_NAME, value, null, null);
	}
	
	public static void update(SQLiteDatabase db, String nome, boolean stato) {
		ContentValues value = new ContentValues();
		value.put(SensoriTable.STATO, (stato) ? 1 : 0);
		db.update(SensoriTable.TABLE_NAME, value, SensoriTable.NOME +" = \""+ nome+"\"", null);
	}
	
	public static ArrayList<Sensore> getLista(String tipo, int stato, SQLiteDatabase db) {
		ArrayList<Sensore> lista = new ArrayList<Sensore>();
		Cursor cursore = db.query(SensoriTable.TABLE_NAME, SensoriTable.COLUMNS, SensoriTable.STATO+" = "+ stato + "AND "+SensoriTable.TIPO+" = \""+ tipo+ "\"",	null, null, null, SensoriTable.NOME);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Sensore(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(2)));
		return lista;
	}
	
	public static ArrayList<Sensore> getAllLista(SQLiteDatabase db) {
		ArrayList<Sensore> lista = new ArrayList<Sensore>();
		Cursor cursore = db.query(SensoriTable.TABLE_NAME, SensoriTable.COLUMNS, null, null, null, null, SensoriTable.NOME);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Sensore(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(3)));
		return lista;
	}
	
	public static void apri(SQLiteDatabase db, String nome) {
		ContentValues value = new ContentValues();
		value.put(SensoriTable.STATO, 1);
		db.update(SensoriTable.TABLE_NAME, value, SensoriTable.NOME+" = \""+ nome+ "\"", null);
	}
	
	public static void chiudi(SQLiteDatabase db, String nome) {
		ContentValues value = new ContentValues();
		value.put(SensoriTable.STATO, 0);
		db.update(SensoriTable.TABLE_NAME, value, SensoriTable.NOME+" = \""+ nome+ "\"", null);
	}
	
	public static void accendi(SQLiteDatabase db, String nome) {
		ContentValues value = new ContentValues();
		value.put(SensoriTable.STATO, 1);
		db.update(SensoriTable.TABLE_NAME, value, SensoriTable.NOME+" = \""+ nome+ "\"", null);
	}
	
	public static void spegni(SQLiteDatabase db, String nome) {
		ContentValues value = new ContentValues();
		value.put(SensoriTable.STATO, 0);
		db.update(SensoriTable.TABLE_NAME, value, SensoriTable.NOME+" = \""+ nome+ "\"", null);
	}
	
}
