package ai.smarthome.database.wrapper;

import ai.smarthome.database.table.ConfigurazioneTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Configurazione {

	private String localita;
	private int meteo;
	private int temperatura;
	private int umidita;
	private int vento;
	private long data;
	private int ora;
	private int minuti;
	private int componenti;
	
	
	public Configurazione() {
		
	}
	
	public Configurazione(String localita, int meteo, int temperatura, int umidita, int vento, long data, int ora, int minuti, int componenti) {
		this.localita = localita;
		this.meteo = meteo;
		this.temperatura = temperatura;
		this.umidita = umidita;
		this.vento = vento;
		this.data = data;
		this.ora = ora;
		this.minuti = minuti;
		this.componenti = componenti;
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
	
	public int getComponenti() {
		return this.componenti;
	}
	
	public static void setConfigurazione(SQLiteDatabase db, String localita, int meteo, int temperatura, int umidita, int vento, long data, int ora, int minuti, int componenti) {
		ContentValues value = new ContentValues();
		value.put(ConfigurazioneTable._ID, 1);
		value.put(ConfigurazioneTable.LOCALITA, localita);
		value.put(ConfigurazioneTable.METEO, meteo);
		value.put(ConfigurazioneTable.TEMPERATURA, temperatura);
		value.put(ConfigurazioneTable.UMIDITA, umidita);
		value.put(ConfigurazioneTable.VENTO, vento);
		value.put(ConfigurazioneTable.DATA, data);
		value.put(ConfigurazioneTable.ORA, ora);
		value.put(ConfigurazioneTable.MINUTI, minuti);
		value.put(ConfigurazioneTable.COMPONENTI, componenti);
		db.insert(ConfigurazioneTable.TABLE_NAME, null, value);
	}

	public static void updateConfigurazione(SQLiteDatabase db, String localita, int meteo, int temperatura, int umidita, int vento, long data, int ora, int minuti, int componenti) {
		ContentValues value = new ContentValues();
		value.put(ConfigurazioneTable.LOCALITA, localita);
		value.put(ConfigurazioneTable.METEO, meteo);
		value.put(ConfigurazioneTable.TEMPERATURA, temperatura);
		value.put(ConfigurazioneTable.UMIDITA, umidita);
		value.put(ConfigurazioneTable.VENTO, vento);
		value.put(ConfigurazioneTable.DATA, data);
		value.put(ConfigurazioneTable.ORA, ora);
		value.put(ConfigurazioneTable.MINUTI, minuti);
		value.put(ConfigurazioneTable.COMPONENTI, componenti);
		db.update(ConfigurazioneTable.TABLE_NAME, value, ConfigurazioneTable._ID +" = 1", null);
	}
	
	public static void updateMeteo(SQLiteDatabase db, String localita, int meteo, int temperatura, int umidita, int vento) {
		ContentValues value = new ContentValues();
		value.put(ConfigurazioneTable.LOCALITA, localita);
		value.put(ConfigurazioneTable.METEO, meteo);
		value.put(ConfigurazioneTable.TEMPERATURA, temperatura);
		value.put(ConfigurazioneTable.UMIDITA, umidita);
		value.put(ConfigurazioneTable.VENTO, vento);
		db.update(ConfigurazioneTable.TABLE_NAME, value, ConfigurazioneTable._ID +" = 1", null);
	}
	
	public static void updateData(SQLiteDatabase db, long data) {
		ContentValues value = new ContentValues();
		value.put(ConfigurazioneTable.DATA, data);
		db.update(ConfigurazioneTable.TABLE_NAME, value, ConfigurazioneTable._ID +" = 1", null);
	}
	
	public static void updateOrario(SQLiteDatabase db, int ora, int minuti) {
		ContentValues value = new ContentValues();
		value.put(ConfigurazioneTable.ORA, ora);
		value.put(ConfigurazioneTable.MINUTI, minuti);
		db.update(ConfigurazioneTable.TABLE_NAME, value, ConfigurazioneTable._ID +" = 1", null);
	}
	
	public static void updateComponenti(SQLiteDatabase db, int componenti) {
		ContentValues value = new ContentValues();
		value.put(ConfigurazioneTable.COMPONENTI, componenti);
		db.update(ConfigurazioneTable.TABLE_NAME, value, ConfigurazioneTable._ID +" = 1", null);
	}
	
	public static Configurazione getConfigurazione(SQLiteDatabase db) {
		Cursor cursore = db.query(ConfigurazioneTable.TABLE_NAME, ConfigurazioneTable.COLUMNS, null, null, null, null, ConfigurazioneTable.LOCALITA);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					return new Configurazione(cursore.getString(1), cursore.getInt(2), cursore.getInt(3), cursore.getInt(4), cursore.getInt(5), cursore.getLong(6), cursore.getInt(7), cursore.getInt(8), cursore.getInt(9));
		return null;
	}
	
	public static String toString(SQLiteDatabase db) {
		Configurazione  configurazione = getConfigurazione(db);
		return "Loc:" + configurazione.getLocalita()+ " Meteo:" + configurazione.getMeteo() + " T:" + configurazione.getTemperatura() + " U:" + configurazione.getUmidita() + " V:" + configurazione.getVento()+ " D:" + configurazione.getData()+ " H:" + configurazione.getOra()+ " M:" + configurazione.getMinuti();
	}
}
