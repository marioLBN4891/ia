package ai.smarthome.database;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

import ai.smarthome.database.table.ComponentiTable;
import ai.smarthome.database.table.ConnessioneVeloceTable;
import ai.smarthome.database.table.MeteoTable;
import ai.smarthome.database.table.OggettiTable;
import ai.smarthome.database.table.SensoriTable;
import ai.smarthome.database.table.UtentiTable;
import ai.smarthome.database.wrapper.Componente;
import ai.smarthome.database.wrapper.Meteo;
import ai.smarthome.database.wrapper.Oggetto;
import ai.smarthome.database.wrapper.Sensore;
import ai.smarthome.database.wrapper.Utente;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class DatabaseHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "SmartHome.db";
 	private static final int SCHEMA_VERSION = 1;			// versione del database DA CAMBIARE AD OGNI MODIFICA
 	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		creaTabelleDatabase(db);
		popolaTabelleDatabase(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	private void creaTabelleDatabase(SQLiteDatabase db) {
		String sqlUtentiTable = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT, {2} TEXT NOT NULL, {3} TEXT NOT NULL, {4} TEXT NOT NULL, {5} TEXT NOT NULL);";
		db.execSQL(MessageFormat.format(sqlUtentiTable, UtentiTable.TABLE_NAME, BaseColumns._ID, UtentiTable.MAIL, UtentiTable.PASSWORD, UtentiTable.COGNOME, UtentiTable.NOME));
		
		String sqlUConnessioneVeloceTable = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT, {2} TEXT NOT NULL, {3} TEXT NOT NULL);";
		db.execSQL(MessageFormat.format(sqlUConnessioneVeloceTable, ConnessioneVeloceTable.TABLE_NAME, BaseColumns._ID, ConnessioneVeloceTable.MAIL, ConnessioneVeloceTable.PASSWORD));
		
		String sqlComponentiTable = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT, {2} TEXT NOT NULL, {3} TEXT NOT NULL, {4} INTEGER NOT NULL);";
		db.execSQL(MessageFormat.format(sqlComponentiTable, ComponentiTable.TABLE_NAME, BaseColumns._ID, ComponentiTable.NOME, ComponentiTable.TIPO, ComponentiTable.STATO));
		
		String sqlSensoriTable = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT, {2} TEXT NOT NULL, {3} TEXT NOT NULL, {4} INTEGER NOT NULL);";
		db.execSQL(MessageFormat.format(sqlSensoriTable, SensoriTable.TABLE_NAME, BaseColumns._ID, SensoriTable.NOME, SensoriTable.TIPO, SensoriTable.STATO));
		
		String sqlOggettiTable = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT, {2} TEXT NOT NULL, {3} TEXT NOT NULL, {4} INTEGER NOT NULL);";
		db.execSQL(MessageFormat.format(sqlOggettiTable, OggettiTable.TABLE_NAME, BaseColumns._ID, OggettiTable.NOME, OggettiTable.CLASSE, OggettiTable.STATO));
		
		String sqlMeteoTable = "CREATE TABLE {0} ({1} INTEGER NOT NULL, {2} TEXT NOT NULL, {3} INTEGER NOT NULL, {4} INTEGER NOT NULL, {5} INTEGER NOT NULL, {6} INTEGER NOT NULL, {7} LONG NOT NULL, {8} INTEGER NOT NULL, {9} INTEGER NOT NULL);";
		db.execSQL(MessageFormat.format(sqlMeteoTable, MeteoTable.TABLE_NAME, BaseColumns._ID, MeteoTable.LOCALITA, MeteoTable.METEO, MeteoTable.TEMPERATURA, MeteoTable.UMIDITA, MeteoTable.VENTO, MeteoTable.DATA, MeteoTable.ORA, MeteoTable.MINUTI));
	}

	private void popolaTabelleDatabase(SQLiteDatabase db) {
		popolaUtentiTable(db);
		popolaOggettiTable(db);
		popolaComponentiTable(db);
		popolaSensoriTable(db);
		popolaMeteoTable(db);
	}
	
	private void popolaOggettiTable(SQLiteDatabase db) {
		Oggetto.setOggetto(db, "pane", Oggetto.DISPENSA, 0);
		Oggetto.setOggetto(db, "pasta", Oggetto.DISPENSA, 0);
		Oggetto.setOggetto(db, "caffè", Oggetto.DISPENSA, 0);
		Oggetto.setOggetto(db, "pentola", Oggetto.MOBILE, 0);
		Oggetto.setOggetto(db, "padella", Oggetto.MOBILE, 0);
		Oggetto.setOggetto(db, "piatto", Oggetto.MOBILE, 0);
		Oggetto.setOggetto(db, "posate", Oggetto.MOBILE, 0);
		Oggetto.setOggetto(db, "bicchiere", Oggetto.MOBILE, 0);
		Oggetto.setOggetto(db, "zucchero", Oggetto.DISPENSA, 0);
		Oggetto.setOggetto(db, "latte", Oggetto.DISPENSA, 0);
		Oggetto.setOggetto(db, "biscotti", Oggetto.DISPENSA, 0);
	}
	
	private void popolaComponentiTable(SQLiteDatabase db) {
		Componente.setComponente(db, "Televisione", Componente.ACCESO_SPENTO, 0);
		Componente.setComponente(db, "Radio", Componente.ACCESO_SPENTO, 0);
		Componente.setComponente(db, "Condizionatore", Componente.ACCESO_SPENTO, 0);
		Componente.setComponente(db, "Balcone", Componente.APERTO_CHIUSO, 0);
		Componente.setComponente(db, "Macchina del caffè", Componente.ACCESO_SPENTO, 0);
		Componente.setComponente(db, "Tostapane", Componente.ACCESO_SPENTO, 0);
		Componente.setComponente(db, "Illuminazione", Componente.ACCESO_SPENTO, 0);
		
	}
	
	private void popolaSensoriTable(SQLiteDatabase db) {
		Sensore.setSensore(db, "Rilevamento temperatura", Sensore.ACCESO_SPENTO, 1);
		Sensore.setSensore(db, "Rilevamento umidità", Sensore.ACCESO_SPENTO, 1);
		Sensore.setSensore(db, "Rilevamento vento", Sensore.ACCESO_SPENTO, 1);
		Sensore.setSensore(db, "Rilevamento presenza", Sensore.ACCESO_SPENTO, 1);
		Sensore.setSensore(db, "Rilevamento sonoro", Sensore.ACCESO_SPENTO, 1);
	}
	
	private void popolaMeteoTable(SQLiteDatabase db) {
		final Calendar c = Calendar.getInstance();
    	int hour = c.get(Calendar.HOUR_OF_DAY);
    	int minute = c.get(Calendar.MINUTE);
		Meteo.setConfigurazione(db, "-", 50, 50, 50, 50, new Date().getTime(), hour, minute);
	}
	
	private void popolaUtentiTable(SQLiteDatabase db) {
		Utente.setUtente(db, "mario.labianca@poste.it", "123456", "Labianca1", "mario1");
	}
}