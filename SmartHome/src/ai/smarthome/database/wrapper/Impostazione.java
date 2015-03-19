package ai.smarthome.database.wrapper;

import ai.smarthome.database.table.ImpostazioniTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Impostazione {

	private String indirizzo;
	
	
	public Impostazione() {
		
	}
	
	public Impostazione(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	
	public String getIndirizzo() {
		return this.indirizzo;
	}
	
	public static void setConfigurazione(SQLiteDatabase db, String indirizzo) {
		ContentValues value = new ContentValues();
		value.put(ImpostazioniTable._ID, 1);
		value.put(ImpostazioniTable.INDIRIZZO, indirizzo);
		db.insert(ImpostazioniTable.TABLE_NAME, null, value);
	}

	public static void updateIndirizzo(SQLiteDatabase db, String indirizzo) {
		ContentValues value = new ContentValues();
		value.put(ImpostazioniTable.INDIRIZZO, indirizzo);
		db.update(ImpostazioniTable.TABLE_NAME, value, ImpostazioniTable._ID +" = 1", null);
	}
	
	public static String getIndirizzo(SQLiteDatabase db) {
		Cursor cursore = db.query(ImpostazioniTable.TABLE_NAME, ImpostazioniTable.COLUMNS, null, null, null, null, ImpostazioniTable.INDIRIZZO);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					return cursore.getString(1);
		return null;
	}
	
	
}
