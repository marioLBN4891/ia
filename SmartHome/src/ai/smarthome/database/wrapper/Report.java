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
	private String PROLOG;
	private String ITEM;
	private int STATO;
	
	public Report(int id, String azione, String prolog, String item, int stato) {
		this.ID = id;
		this.AZIONE = azione;
		this.PROLOG = prolog;
		this.ITEM = item;
		this.STATO = stato;
		
	}

	public int getId() {
		return this.ID;
	}
	
	public String getAzione() {
		return this.AZIONE;
	}
	
	public String getProlog() {
		return this.PROLOG;
	}
	
	public String getItem() {
		return this.ITEM;
	}
	
	public int getStato() {
		return this.STATO;
	}
	
	public static void reset(SQLiteDatabase db) {
		db.delete(ReportTable.TABLE_NAME, null, null);
	}
	
	public static void insert(SQLiteDatabase db, int id, String azione, String prolog, String item, int stato) {
		ContentValues value = new ContentValues();
		value.put(BaseColumns._ID, id);
		value.put(ReportTable.AZIONE, azione);
		value.put(ReportTable.PROLOG, prolog);
		value.put(ReportTable.ITEM, item);
		value.put(ReportTable.STATO, stato);
		db.insert(ReportTable.TABLE_NAME, null, value);
	}
	
	public static ArrayList<Report> getLista(SQLiteDatabase db) {
		ArrayList<Report> lista = new ArrayList<Report>();
		Cursor cursore = db.query(ReportTable.TABLE_NAME, ReportTable.COLUMNS, null, null, null, null, BaseColumns._ID);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Report(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getString(3), cursore.getInt(4)));
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
				String id = parti[0];
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
				value.put(BaseColumns._ID, id);
				value.put(ReportTable.AZIONE, azione);
				value.put(ReportTable.PROLOG, prolog);
				value.put(ReportTable.ITEM, item);
				value.put(ReportTable.STATO, stato);
				db.insert(ReportTable.TABLE_NAME, null, value);
			}
		}
	}
	
	public static ArrayList<Report> listaDedottiToListaReport(ArrayList<String> listaDedotti) {
		
		ArrayList<Report> listaReport = new ArrayList<Report>();
		
		final String ON = "SHE ha acceso ";
		final String OFF = "SHE ha acceso ";
		final String OPEN = "SHE ha aperto ";
		final String CLOSE = "SHE ha Chiuso ";
		
		if (listaDedotti != null && !listaDedotti.isEmpty()) {
			for (String fattoDedotto : listaDedotti) {
				String prolog = fattoDedotto;
			
				fattoDedotto.replace("fact(", "");
				String[] parti = fattoDedotto.split(",");
				String id = parti[0];
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
			
				Report value = new Report(Integer.parseInt(id), azione, prolog, item, stato);
				listaReport.add(value);
			}
		}
		return listaReport;
	}
	
	
}
