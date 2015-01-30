package ai.smarthome.database;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

import ai.smarthome.database.table.ComponentiTable;
import ai.smarthome.database.table.ConnessioneVeloceTable;
import ai.smarthome.database.table.ConfigurazioneTable;
import ai.smarthome.database.table.OggettiTable;
import ai.smarthome.database.table.ReportTable;
import ai.smarthome.database.table.SensoriTable;
import ai.smarthome.database.table.UtentiTable;
import ai.smarthome.database.wrapper.Componente;
import ai.smarthome.database.wrapper.Configurazione;
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
		
		String sqlConfigurazioneTable = "CREATE TABLE {0} ({1} INTEGER NOT NULL, {2} TEXT NOT NULL, {3} INTEGER NOT NULL, {4} INTEGER NOT NULL, {5} INTEGER NOT NULL, {6} INTEGER NOT NULL, {7} LONG NOT NULL, {8} INTEGER NOT NULL, {9} INTEGER NOT NULL, {10} INTEGER NOT NULL);";
		db.execSQL(MessageFormat.format(sqlConfigurazioneTable, ConfigurazioneTable.TABLE_NAME, BaseColumns._ID, ConfigurazioneTable.LOCALITA, ConfigurazioneTable.METEO, ConfigurazioneTable.TEMPERATURA, ConfigurazioneTable.UMIDITA, ConfigurazioneTable.VENTO, ConfigurazioneTable.DATA, ConfigurazioneTable.ORA, ConfigurazioneTable.MINUTI, ConfigurazioneTable.COMPONENTI));
	
		String sqlReportTable = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY, {2} TEXT NOT NULL, {3} TEXT NOT NULL);";
		db.execSQL(MessageFormat.format(sqlReportTable, ReportTable.TABLE_NAME, BaseColumns._ID, ReportTable.AZIONE, ReportTable.PROLOG));
		
	}

	private void popolaTabelleDatabase(SQLiteDatabase db) {
		popolaUtentiTable(db);
		popolaOggettiTable(db);
		popolaComponentiTable(db);
		popolaSensoriTable(db);
		popolaConfigurazioneTable(db);
	}
	
	private void popolaOggettiTable(SQLiteDatabase db) {
		Oggetto.setConfigurazione(db, "Pane", Oggetto.DISPENSA, 0);
		Oggetto.setConfigurazione(db, "Pasta", Oggetto.DISPENSA, 0);
		Oggetto.setConfigurazione(db, "Caffè", Oggetto.DISPENSA, 0);
		Oggetto.setConfigurazione(db, "Pentola", Oggetto.MOBILE, 0);
		Oggetto.setConfigurazione(db, "Padella", Oggetto.MOBILE, 0);
		Oggetto.setConfigurazione(db, "Piatto", Oggetto.MOBILE, 0);
		Oggetto.setConfigurazione(db, "Posate", Oggetto.MOBILE, 0);
		Oggetto.setConfigurazione(db, "Bicchiere", Oggetto.MOBILE, 0);
		Oggetto.setConfigurazione(db, "Zucchero", Oggetto.DISPENSA, 0);
		Oggetto.setConfigurazione(db, "Latte", Oggetto.FRIGORIFERO, 0);
		Oggetto.setConfigurazione(db, "Acqua", Oggetto.FRIGORIFERO, 0);
		Oggetto.setConfigurazione(db, "Uova", Oggetto.FRIGORIFERO, 0);
		Oggetto.setConfigurazione(db, "Biscotti", Oggetto.DISPENSA, 0);
		Oggetto.setConfigurazione(db, "Sale", Oggetto.CUCINA, 0);
	}
	
	private void popolaComponentiTable(SQLiteDatabase db) {
		Componente.setConfigurazione(db, "Televisione", Componente.ACCESO_SPENTO, 0);
		Componente.setConfigurazione(db, "Radio", Componente.ACCESO_SPENTO, 0);
		Componente.setConfigurazione(db, "Condizionatore", Componente.ACCESO_SPENTO, 0);
		Componente.setConfigurazione(db, "Balcone", Componente.APERTO_CHIUSO, 0);
		Componente.setConfigurazione(db, "Macchina del caffè", Componente.ACCESO_SPENTO, 0);
		Componente.setConfigurazione(db, "Tostapane", Componente.ACCESO_SPENTO, 0);
		Componente.setConfigurazione(db, "Fornello", Componente.ACCESO_SPENTO, 0);
		Componente.setConfigurazione(db, "Forno", Componente.ACCESO_SPENTO, 0);
		Componente.setConfigurazione(db, "Illuminazione", Componente.ACCESO_SPENTO, 0);
		Componente.setConfigurazione(db, "Dispensa", Componente.APERTO_CHIUSO, 0);
		Componente.setConfigurazione(db, "Mobile", Componente.APERTO_CHIUSO, 0);
		Componente.setConfigurazione(db, "Frigorifero", Componente.APERTO_CHIUSO, 0);
	}
	
	private void popolaSensoriTable(SQLiteDatabase db) {
		Sensore.setConfigurazione(db, "Rilevamento temperatura", Sensore.ACCESO_SPENTO, 1);
		Sensore.setConfigurazione(db, "Rilevamento umidità", Sensore.ACCESO_SPENTO, 1);
		Sensore.setConfigurazione(db, "Rilevamento vento", Sensore.ACCESO_SPENTO, 1);
		Sensore.setConfigurazione(db, "Rilevamento presenza", Sensore.ACCESO_SPENTO, 1);
		Sensore.setConfigurazione(db, "Rilevamento sonoro", Sensore.ACCESO_SPENTO, 1);
	}
	
	private void popolaConfigurazioneTable(SQLiteDatabase db) {
		final Calendar c = Calendar.getInstance();
    	int hour = c.get(Calendar.HOUR_OF_DAY);
    	int minute = c.get(Calendar.MINUTE);
		Configurazione.setConfigurazione(db, "-", 50, 50, 50, 50, new Date().getTime(), hour, minute, 0);
	}
	
	private void popolaUtentiTable(SQLiteDatabase db) {
		Utente.setUtente(db, "mario.labianca@poste.it", "123456", "Labianca1", "mario1");
	}
}