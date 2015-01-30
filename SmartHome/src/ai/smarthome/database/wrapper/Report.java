package ai.smarthome.database.wrapper;

import java.io.Serializable;
import java.util.ArrayList;

import ai.smarthome.database.table.OggettiTable;
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
	
	public Report(int id, String azione, String prolog) {
		this.ID = id;
		this.AZIONE = azione;
		this.PROLOG = prolog;
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
	
	public static void reset(SQLiteDatabase db) {
		ContentValues value = new ContentValues();
		value.put(OggettiTable.STATO, 0);
		db.delete(ReportTable.TABLE_NAME, null, null);
	}
	
	public static void insert(SQLiteDatabase db, int id, String azione, String prolog) {
		ContentValues value = new ContentValues();
		value.put(BaseColumns._ID, id);
		value.put(ReportTable.AZIONE, azione);
		value.put(ReportTable.PROLOG, prolog);
		db.insert(ReportTable.TABLE_NAME, null, value);
	}
	
	public static ArrayList<Report> getLista(SQLiteDatabase db) {
		ArrayList<Report> lista = new ArrayList<Report>();
		Cursor cursore = db.query(ReportTable.TABLE_NAME, ReportTable.COLUMNS, null, null, null, null, BaseColumns._ID);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Report(cursore.getInt(0), cursore.getString(1), cursore.getString(2)));
		return lista;
	}

}
