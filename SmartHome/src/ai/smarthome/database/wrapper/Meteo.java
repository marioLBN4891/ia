package ai.smarthome.database.wrapper;

import ai.smarthome.database.table.MeteoTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Meteo {

	private String localita;
	private int meteo;
	private int temperatura;
	private int umidita;
	private int vento;
	private long data;
	private int ora;
	private int minuti;
	
	
	public Meteo(String localita, int meteo, int temperatura, int umidita, int vento, long data, int ora, int minuti) {
		this.localita = localita;
		this.meteo = meteo;
		this.temperatura = temperatura;
		this.umidita = umidita;
		this.vento = vento;
		this.data = data;
		this.ora = ora;
		this.minuti = minuti;
	}
	
	
	public String getLocalita() {
		return this.localita;
	}
	
	public int getMeteo() {
		return this.meteo;
	}
	
	public int getTemperatura() {
		return this.temperatura;
	}
	
	public int getUmidita() {
		return this.umidita;
	}
	
	public int getVento() {
		return this.vento;
	}
	
	public long getData() {
		return this.data;
	}
	
	public int getOra() {
		return this.ora;
	}
	
	public int getMinuti() {
		return this.minuti;
	}
	
	public static void setConfigurazione(SQLiteDatabase db, String localita, int meteo, int temperatura, int umidita, int vento, long data, int ora, int minuti) {
		ContentValues value = new ContentValues();
		value.put(MeteoTable._ID, 1);
		value.put(MeteoTable.LOCALITA, localita);
		value.put(MeteoTable.METEO, meteo);
		value.put(MeteoTable.TEMPERATURA, temperatura);
		value.put(MeteoTable.UMIDITA, umidita);
		value.put(MeteoTable.VENTO, vento);
		value.put(MeteoTable.DATA, data);
		value.put(MeteoTable.ORA, ora);
		value.put(MeteoTable.MINUTI, minuti);
		db.insert(MeteoTable.TABLE_NAME, null, value);
	}

	public static void updateConfigurazione(SQLiteDatabase db, String localita, int meteo, int temperatura, int umidita, int vento, long data, int ora, int minuti) {
		ContentValues value = new ContentValues();
		value.put(MeteoTable.LOCALITA, localita);
		value.put(MeteoTable.METEO, meteo);
		value.put(MeteoTable.TEMPERATURA, temperatura);
		value.put(MeteoTable.UMIDITA, umidita);
		value.put(MeteoTable.VENTO, vento);
		value.put(MeteoTable.DATA, data);
		value.put(MeteoTable.ORA, ora);
		value.put(MeteoTable.MINUTI, minuti);
		db.update(MeteoTable.TABLE_NAME, value, MeteoTable._ID +" = 1", null);
	}
	
	public static void updateMeteo(SQLiteDatabase db, String localita, int meteo, int temperatura, int umidita, int vento) {
		ContentValues value = new ContentValues();
		value.put(MeteoTable.LOCALITA, localita);
		value.put(MeteoTable.METEO, meteo);
		value.put(MeteoTable.TEMPERATURA, temperatura);
		value.put(MeteoTable.UMIDITA, umidita);
		value.put(MeteoTable.VENTO, vento);
		db.update(MeteoTable.TABLE_NAME, value, MeteoTable._ID +" = 1", null);
	}
	
	public static void updateData(SQLiteDatabase db, long data) {
		ContentValues value = new ContentValues();
		value.put(MeteoTable.DATA, data);
		db.update(MeteoTable.TABLE_NAME, value, MeteoTable._ID +" = 1", null);
	}
	
	public static void updateOrario(SQLiteDatabase db, int ora, int minuti) {
		ContentValues value = new ContentValues();
		value.put(MeteoTable.ORA, ora);
		value.put(MeteoTable.MINUTI, minuti);
		db.update(MeteoTable.TABLE_NAME, value, MeteoTable._ID +" = 1", null);
	}
	
	public static Meteo getMeteo(SQLiteDatabase db) {
		Cursor cursore = db.query(MeteoTable.TABLE_NAME, MeteoTable.COLUMNS, null, null, null, null, MeteoTable.LOCALITA);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					return new Meteo(cursore.getString(1), cursore.getInt(2), cursore.getInt(3), cursore.getInt(4), cursore.getInt(5), cursore.getLong(6), cursore.getInt(7), cursore.getInt(8));
		return null;
	}
	
	public static String toString(SQLiteDatabase db) {
		Meteo  meteo = getMeteo(db);
		return "Loc:" + meteo.getLocalita()+ " Meteo:" + meteo.getMeteo() + " T:" + meteo.getTemperatura() + " U:" + meteo.getUmidita() + " V:" + meteo.getVento()+ " D:" + meteo.getData()+ " H:" + meteo.getOra()+ " M:" + meteo.getMinuti();
	}
}
