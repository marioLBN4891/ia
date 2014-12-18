package ai.smarthome.database.wrapper;

import java.util.ArrayList;

import ai.smarthome.database.table.SensoriTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Sensore {

	public static final String ACCESO_SPENTO = "accceso/spento";
	public static final String APERTO_CHIUSO = "aperto/chiuso";
	
	private String nome;
	private String tipo;
	private int stato;
	
	public Sensore(String nome, String tipo, int stato) {
		this.nome = nome;
		this.stato = stato;
		this.tipo = tipo;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public void setStato(int stato) {
		this.stato = stato;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public String getTipo() {
		return this.tipo;
	}
	
	public int getStato() {
		return this.stato;
	}

	public static void setSensore(SQLiteDatabase db, String nome, String tipo, int stato) {
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
	
	public static ArrayList<Oggetto> getLista(String tipo, int stato, SQLiteDatabase db) {
		ArrayList<Oggetto> lista = new ArrayList<Oggetto>();
		Cursor cursore = db.query(SensoriTable.TABLE_NAME, SensoriTable.COLUMNS, SensoriTable.STATO+" = "+ stato + "AND "+SensoriTable.TIPO+" = \""+ tipo+ "\"",	null, null, null, SensoriTable.NOME);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Oggetto(cursore.getString(1), cursore.getString(2), cursore.getInt(2)));
		return lista;
	}
	
	public static ArrayList<Oggetto> getAllLista(SQLiteDatabase db) {
		ArrayList<Oggetto> lista = new ArrayList<Oggetto>();
		Cursor cursore = db.query(SensoriTable.TABLE_NAME, SensoriTable.COLUMNS, null, null, null, null, SensoriTable.NOME);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Oggetto(cursore.getString(1), cursore.getString(2), cursore.getInt(3)));
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
