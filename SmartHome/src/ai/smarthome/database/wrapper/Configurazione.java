package ai.smarthome.database.wrapper;

import ai.smarthome.database.table.ConfigurazioneTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Configurazione {

	private String localita;
	private int meteo;
	private int temperaturaInt;
	private int temperaturaEst;
	private int umiditaInt;
	private int umiditaEst;
	private int vento;
	private int luminosita;
	private long data;
	private int ora;
	private int minuti;
	private int componenti;
	
	
	public Configurazione() {
		
	}
	
	public Configurazione(String localita, int meteo, int temperaturaInt, int temperaturaEst, int umiditaInt, int umiditaEst, int vento, int luminosita, long data, int ora, int minuti, int componenti) {
		this.localita = localita;
		this.meteo = meteo;
		this.temperaturaInt = temperaturaInt;
		this.temperaturaEst = temperaturaEst;
		this.umiditaInt = umiditaInt;
		this.umiditaEst = umiditaEst;
		this.vento = vento;
		this.luminosita = luminosita;
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
	
	public int getTemperaturaInt() {
		return this.temperaturaInt;
	}
	
	public int getTemperaturaEst() {
		return this.temperaturaEst;
	}
	
	public int getUmiditaInt() {
		return this.umiditaInt;
	}
	
	public int getUmiditaEst() {
		return this.umiditaEst;
	}
	
	public int getVento() {
		return this.vento;
	}
	
	public int getLuminosita() {
		return this.luminosita;
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
	
	public static void setConfigurazione(SQLiteDatabase db, String localita, int meteo, int temperaturaInt, int temperaturaEst, int umiditaInt, int umiditaEst, int vento, int luminosita, long data, int ora, int minuti, int componenti) {
		ContentValues value = new ContentValues();
		value.put(ConfigurazioneTable._ID, 1);
		value.put(ConfigurazioneTable.LOCALITA, localita);
		value.put(ConfigurazioneTable.METEO, meteo);
		value.put(ConfigurazioneTable.TEMPERATURAINT, temperaturaInt);
		value.put(ConfigurazioneTable.TEMPERATURAEST, temperaturaEst);
		value.put(ConfigurazioneTable.UMIDITAINT, umiditaInt);
		value.put(ConfigurazioneTable.UMIDITAEST, umiditaEst);
		value.put(ConfigurazioneTable.VENTO, vento);
		value.put(ConfigurazioneTable.LUMINOSITA, luminosita);
		value.put(ConfigurazioneTable.DATA, data);
		value.put(ConfigurazioneTable.ORA, ora);
		value.put(ConfigurazioneTable.MINUTI, minuti);
		value.put(ConfigurazioneTable.COMPONENTI, componenti);
		db.insert(ConfigurazioneTable.TABLE_NAME, null, value);
	}

	public static void updateConfigurazione(SQLiteDatabase db, String localita, int meteo, int temperaturaInt, int temperaturaEst, int umiditaInt, int umiditaEst, int vento, int luminosita, long data, int ora, int minuti, int componenti) {
		ContentValues value = new ContentValues();
		value.put(ConfigurazioneTable.LOCALITA, localita);
		value.put(ConfigurazioneTable.METEO, meteo);
		value.put(ConfigurazioneTable.TEMPERATURAINT, temperaturaInt);
		value.put(ConfigurazioneTable.TEMPERATURAEST, temperaturaEst);
		value.put(ConfigurazioneTable.UMIDITAINT, umiditaInt);
		value.put(ConfigurazioneTable.UMIDITAEST, umiditaEst);
		value.put(ConfigurazioneTable.VENTO, vento);
		value.put(ConfigurazioneTable.LUMINOSITA, luminosita);
		value.put(ConfigurazioneTable.DATA, data);
		value.put(ConfigurazioneTable.ORA, ora);
		value.put(ConfigurazioneTable.MINUTI, minuti);
		value.put(ConfigurazioneTable.COMPONENTI, componenti);
		db.update(ConfigurazioneTable.TABLE_NAME, value, ConfigurazioneTable._ID +" = 1", null);
	}
	
	public static void updateMeteo(SQLiteDatabase db, String localita, int meteo, int temperaturaInt, int temperaturaEst, int umiditaInt, int umiditaEst, int vento, int luminosita) {
		ContentValues value = new ContentValues();
		value.put(ConfigurazioneTable.LOCALITA, localita);
		value.put(ConfigurazioneTable.METEO, meteo);
		value.put(ConfigurazioneTable.TEMPERATURAINT, temperaturaInt);
		value.put(ConfigurazioneTable.TEMPERATURAEST, temperaturaEst);
		value.put(ConfigurazioneTable.UMIDITAINT, umiditaInt);
		value.put(ConfigurazioneTable.UMIDITAEST, umiditaEst);
		value.put(ConfigurazioneTable.VENTO, vento);
		value.put(ConfigurazioneTable.LUMINOSITA, luminosita);
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
					return new Configurazione(cursore.getString(1), cursore.getInt(2), cursore.getInt(3), cursore.getInt(4), cursore.getInt(5), cursore.getInt(6), cursore.getInt(7), cursore.getInt(8), cursore.getLong(9), cursore.getInt(10), cursore.getInt(11), cursore.getInt(12));
		return null;
	}
	
	public static String toString(SQLiteDatabase db) {
		Configurazione  configurazione = getConfigurazione(db);
		return "Loc:" + configurazione.getLocalita()+ " Meteo:" + configurazione.getMeteo() + " T_I:" + configurazione.getTemperaturaInt() + " T_E:" + configurazione.getTemperaturaEst() + " U_I:" + configurazione.getUmiditaInt() + " U_E:" + configurazione.getUmiditaEst() + " V:" + configurazione.getVento()+ " D:" + configurazione.getData()+ " H:" + configurazione.getOra()+ " M:" + configurazione.getMinuti();
	}
}
