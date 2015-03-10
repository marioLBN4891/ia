package ai.smarthome.database.wrapper;

import java.io.Serializable;
import java.util.ArrayList;

import ai.smarthome.database.table.ReportTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class Report implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int ID;
	private String AZIONE;
	private String ITEM;
	private int STATO;
	private String PROLOG;
	private int SENTRECEIVED;
	private int NUOVO;
	
	public Report(int id, String azione, String item, int stato, String prolog, int sentreceived, int nuovo) {
		this.ID = id;
		this.AZIONE = azione;
		this.ITEM = item;
		this.STATO = stato;
		this.PROLOG = prolog;
		this.SENTRECEIVED = sentreceived;
		this.NUOVO = nuovo;
	}
	
	public Report(String azione, String item, int stato, String prolog, int sentreceived, int nuovo) {
		this.AZIONE = azione;
		this.ITEM = item;
		this.STATO = stato;
		this.PROLOG = prolog;
		this.SENTRECEIVED = sentreceived;
		this.NUOVO = nuovo;
	}
	
	public Report(Report report) {
		this.ID = report.getId();
		this.AZIONE = report.getAzione();
		this.ITEM = report.getItem();
		this.STATO = report.getStato();
		this.PROLOG = report.getProlog();
		this.SENTRECEIVED = report.getSentReceived();
		this.NUOVO = report.getNuovo();
	}

	public int getId() {
		return this.ID;
	}
	
	public String getAzione() {
		return this.AZIONE;
	}
	
	public String getItem() {
		return this.ITEM;
	}
	
	public int getStato() {
		return this.STATO;
	}
	
	public String getProlog() {
		return this.PROLOG;
	}
	
	public int getSentReceived() {
		return this.SENTRECEIVED;
	}
	
	public int getNuovo() {
		return this.NUOVO;
	}
	
	public static void reset(SQLiteDatabase db) {
		db.delete(ReportTable.TABLE_NAME, null, null);
	}
	
	public static long insert(SQLiteDatabase db, String azione, String item, int stato, String prolog, int sentreceived, int nuovo) {
		ContentValues value = new ContentValues();
		value.put(ReportTable.AZIONE, azione);
		value.put(ReportTable.ITEM, item);
		value.put(ReportTable.STATO, stato);
		value.put(ReportTable.PROLOG, prolog);
		value.put(ReportTable.SENTRECEIVED, sentreceived);
		value.put(ReportTable.NUOVO, nuovo);
		
		return db.insert(ReportTable.TABLE_NAME, null, value);
	}
	
	public static long insert(SQLiteDatabase db, Report report) {
		ContentValues value = new ContentValues();
		value.put(ReportTable.AZIONE, report.getAzione());
		value.put(ReportTable.ITEM, report.getItem());
		value.put(ReportTable.STATO, report.getStato());
		value.put(ReportTable.PROLOG, report.getProlog());
		value.put(ReportTable.SENTRECEIVED, report.getSentReceived());
		value.put(ReportTable.NUOVO, report.getNuovo());
		
		return db.insert(ReportTable.TABLE_NAME, null, value);
	}
	
	public static void updateReport(SQLiteDatabase db) {
		ContentValues value = new ContentValues();
		value.put(ReportTable.NUOVO, 0);
		db.update(ReportTable.TABLE_NAME, value, null, null);
	}
	
	public static ArrayList<Report> getLista(SQLiteDatabase db) {
		ArrayList<Report> lista = new ArrayList<Report>();
		Cursor cursore = db.query(ReportTable.TABLE_NAME, ReportTable.COLUMNS, null, null, null, null, BaseColumns._ID);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Report(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(3), cursore.getString(4), cursore.getInt(5), cursore.getInt(6)));
		return lista;
	}

	public static ArrayList<Report> getListaNuovi(SQLiteDatabase db) {
		ArrayList<Report> lista = new ArrayList<Report>();
		Cursor cursore = db.query(ReportTable.TABLE_NAME, ReportTable.COLUMNS, ReportTable.NUOVO+" = 1", null, null, null, BaseColumns._ID);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Report(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(3), cursore.getString(4), cursore.getInt(5), cursore.getInt(6)));
		return lista;
	}
	
	public static void insertFattiDedotti(SQLiteDatabase db, ArrayList<String> lista) {
		
		final String ON = "SHE ha acceso ";
		final String OFF = "SHE ha acceso ";
		final String OPEN = "SHE ha aperto ";
		final String CLOSE = "SHE ha Chiuso ";
		
		if (lista != null && !lista.isEmpty()) {
			for (String fattoDedotto : lista) {
				String prolog = fattoDedotto;
			
				fattoDedotto.replace("fact(", "");
				String[] parti = fattoDedotto.split(",");
				String fatto = parti[1];
				
				String azione = "";
				String item = "";
				int stato = 0;
				if (fatto.contains("On")) {
					stato = 1;
					item = fatto.replace("On", "");
					azione = ON+item;
				}
				if (fatto.contains("Off")) { 
					stato = 2;
					item = fatto.replace("Off", "");
					azione = OFF+item;
				}
				if (fatto.contains("Open")) {
					stato = 1;
					item = fatto.replace("Open", "");
					azione = OPEN+item;
				}
				if (fatto.contains("Close")) {
					stato = 0;
					item = fatto.replace("Close", "");
					azione = CLOSE+item;
				}
			
				ContentValues value = new ContentValues();
				value.put(ReportTable.AZIONE, azione);
				value.put(ReportTable.ITEM, item);
				value.put(ReportTable.STATO, stato);
				value.put(ReportTable.PROLOG, prolog);
				value.put(ReportTable.SENTRECEIVED, 1);
				value.put(ReportTable.NUOVO, 0);
				db.insert(ReportTable.TABLE_NAME, null, value);
			}
		}
	}
	
	
	
	
}
