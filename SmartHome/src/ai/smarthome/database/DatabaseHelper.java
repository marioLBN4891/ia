package ai.smarthome.database;

import java.text.MessageFormat;

import ai.smarthome.database.table.AccesoSpentoTable;
import ai.smarthome.database.table.ApertoChiusoTable;
import ai.smarthome.database.table.ComponentiTable;
import ai.smarthome.database.table.ConnessioneVeloceTable;
import ai.smarthome.database.table.OggettiTable;
import ai.smarthome.database.table.UtentiTable;
import ai.smarthome.database.wrapper.Oggetto;
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
		
		String sqlComponentiTable = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT, {2} TEXT NOT NULL);";
		db.execSQL(MessageFormat.format(sqlComponentiTable, ComponentiTable.TABLE_NAME, BaseColumns._ID, ComponentiTable.NOME));
		
		String sqlAccesoSpentoTable = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT, {2} INTEGER NOT NULL, {3} INTEGER NOT NULL, FOREIGN KEY({2}) REFERENCES {4}({5}));";
		db.execSQL(MessageFormat.format(sqlAccesoSpentoTable, AccesoSpentoTable.TABLE_NAME, BaseColumns._ID, AccesoSpentoTable.ACCESO, AccesoSpentoTable.ID_COMPONENTE, ComponentiTable.TABLE_NAME, BaseColumns._ID));
		
		String sqlApertoChiusoTable = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT, {2} INTEGER NOT NULL, {3} INTEGER NOT NULL, FOREIGN KEY({2}) REFERENCES {4}({5}));";
		db.execSQL(MessageFormat.format(sqlApertoChiusoTable, ApertoChiusoTable.TABLE_NAME, BaseColumns._ID, ApertoChiusoTable.APERTO, ApertoChiusoTable.ID_COMPONENTE, ComponentiTable.TABLE_NAME, BaseColumns._ID));
		
		String sqlOggettiTable = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT, {2} TEXT NOT NULL, {3} INTEGER NOT NULL);";
		db.execSQL(MessageFormat.format(sqlOggettiTable, OggettiTable.TABLE_NAME, BaseColumns._ID, OggettiTable.NOME, OggettiTable.PRESO));
	}

	private void popolaTabelleDatabase(SQLiteDatabase db) {
		popolaUtentiTable(db);
		popolaOggettiTable(db);
	}
	
	private void popolaOggettiTable(SQLiteDatabase db) {
		Oggetto.setOggetto(db, "pane", 0);
		Oggetto.setOggetto(db, "pasta", 0);
		Oggetto.setOggetto(db, "caffè", 0);
		Oggetto.setOggetto(db, "pentola", 0);
		Oggetto.setOggetto(db, "padella", 0);
		Oggetto.setOggetto(db, "zucchero", 0);
		Oggetto.setOggetto(db, "latte", 0);
	}
	
	private void popolaUtentiTable(SQLiteDatabase db) {
	//	Utente.setUtente(db, "mariol", "123456", "Labianca", "mario", "mariolabianca84@gmail.com");
		Utente.setUtente(db, "mario.labianca@poste.it", "123456", "Labianca1", "mario1");
		
	}
}