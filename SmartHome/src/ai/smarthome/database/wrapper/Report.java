package ai.smarthome.database.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ai.smarthome.database.table.ReportTable;
import ai.smarthome.util.LogView;
import ai.smarthome.util.Prolog;
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
	private int LETTO;
	
	public Report(int id, String azione, String item, int stato, String prolog, int sentreceived, int nuovo, int letto) {
		this.ID = id;
		this.AZIONE = azione;
		this.ITEM = item;
		this.STATO = stato;
		this.PROLOG = prolog;
		this.SENTRECEIVED = sentreceived;
		this.NUOVO = nuovo;
		this.LETTO = letto;
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
		this.LETTO = report.getLetto();
	}

	public int getId() {
		return this.ID;
	}
	
	public void setId(int id) {
		this.ID = id;
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
	
	public void setProlog(String prolog) {
		this.PROLOG = prolog;
	}
		
	public int getSentReceived() {
		return this.SENTRECEIVED;
	}
	
	public int getNuovo() {
		return this.NUOVO;
	}
	
	public int getLetto() {
		return this.LETTO;
	}
	
	public static void reset(SQLiteDatabase db) {
		db.delete(ReportTable.TABLE_NAME, null, null);
	}
	
	public static long insert(SQLiteDatabase db, int id, String azione, String item, int stato, String prolog, int sentreceived, int nuovo, int letto) {
		ContentValues value = new ContentValues();
		value.put(ReportTable._ID, id);
		value.put(ReportTable.AZIONE, azione);
		value.put(ReportTable.ITEM, item);
		value.put(ReportTable.STATO, stato);
		value.put(ReportTable.PROLOG, prolog);
		value.put(ReportTable.SENTRECEIVED, sentreceived);
		value.put(ReportTable.NUOVO, nuovo);
		value.put(ReportTable.LETTO, letto);
		return db.insert(ReportTable.TABLE_NAME, null, value);
	}
	
	public static long insert(SQLiteDatabase db, Report report) {
		ContentValues value = new ContentValues();
		value.put(ReportTable._ID, report.getId());
		value.put(ReportTable.AZIONE, report.getAzione());
		value.put(ReportTable.ITEM, report.getItem());
		value.put(ReportTable.STATO, report.getStato());
		value.put(ReportTable.PROLOG, report.getProlog());
		value.put(ReportTable.SENTRECEIVED, report.getSentReceived());
		value.put(ReportTable.NUOVO, report.getNuovo());
		value.put(ReportTable.LETTO, report.getLetto());
		
		return db.insert(ReportTable.TABLE_NAME, null, value);
	}
	
	public static void updateReportLetti(SQLiteDatabase db) {
		ContentValues value = new ContentValues();
		value.put(ReportTable.NUOVO, 0);
		value.put(ReportTable.LETTO, 0);
		db.update(ReportTable.TABLE_NAME, value, null, null);
	}
	
	public static void updateFattiUser(SQLiteDatabase db, String prolog, int id) {
		ContentValues value = new ContentValues();
		value.put(ReportTable.PROLOG, prolog);
		db.update(ReportTable.TABLE_NAME, value, ReportTable._ID+" = "+id, null);
	}
	
	public static void updateFattiComponente(SQLiteDatabase db, String prolog, int id) {
		ContentValues value = new ContentValues();
		value.put(ReportTable.PROLOG, prolog);
		db.update(ReportTable.TABLE_NAME, value, ReportTable._ID+" = "+id, null);
	}
	
	public static ArrayList<Report> getLista(SQLiteDatabase db) {
		ArrayList<Report> lista = new ArrayList<Report>();
		Cursor cursore = db.query(ReportTable.TABLE_NAME, ReportTable.COLUMNS, null, null, null, null, BaseColumns._ID);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Report(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(3), cursore.getString(4), cursore.getInt(5), cursore.getInt(6), cursore.getInt(7)));
		return lista;
	}

	public static ArrayList<Report> getListaNuovi(SQLiteDatabase db) {
		ArrayList<Report> lista = new ArrayList<Report>();
		Cursor cursore = db.query(ReportTable.TABLE_NAME, ReportTable.COLUMNS, ReportTable.LETTO+" = 1", null, null, null, BaseColumns._ID);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Report(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(3), cursore.getString(4), cursore.getInt(5), cursore.getInt(6), cursore.getInt(7)));
		return lista;
	}
	
	public static ArrayList<Report> getListaReportIniziali(SQLiteDatabase db) {
	
		ArrayList<Report> lista = new ArrayList<Report>();
		Cursor cursore = db.query(ReportTable.TABLE_NAME, ReportTable.COLUMNS, ReportTable.SENTRECEIVED+" = 0 and "+ReportTable.NUOVO+" = 0", null, null, null, BaseColumns._ID);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
				if (!cursore.getString(4).contains("action"))
					lista.add(new Report(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(3), cursore.getString(4), cursore.getInt(5), cursore.getInt(6), cursore.getInt(7)));
		return lista;
	}
	
	public static CharSequence[] getListaDedottiNuoviCharSequence(SQLiteDatabase db) {
		List<String> listItems = new ArrayList<String>();
		Cursor cursore = db.query(ReportTable.TABLE_NAME, ReportTable.COLUMNS, ReportTable.ITEM+" != \"\" AND "+ReportTable.NUOVO+" = 1 AND "+ReportTable.SENTRECEIVED+" = 1",	null, null, null, ReportTable._ID);
		
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
				listItems.add(cursore.getString(1).replace("C@SA: ", ""));
		
		CharSequence[] charSequenceItems = listItems.toArray(new CharSequence[listItems.size()]);;
		
		return charSequenceItems;
	}
	
	public static boolean checkListaDedottiNuovi(SQLiteDatabase db) {
		Cursor cursore = db.query(ReportTable.TABLE_NAME, ReportTable.COLUMNS, ReportTable.ITEM+" != \"\" AND "+ReportTable.NUOVO+" = 1 AND "+ReportTable.SENTRECEIVED+" = 1",	null, null, null, ReportTable._ID);
		if (cursore.getCount() > 0)
			return true;
		else
			return false;
	}
	
	public static Report getProlog(SQLiteDatabase db, String azione) {
		Cursor cursore = db.query(ReportTable.TABLE_NAME, ReportTable.COLUMNS, ReportTable.AZIONE+" = \""+ azione +"\"",	null, null, null, ReportTable._ID);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
				 return new Report(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(3), cursore.getString(4), cursore.getInt(5), cursore.getInt(6), cursore.getInt(7));
		return null;
	}
	
	public static boolean isPresentFatto(SQLiteDatabase db, String fattoDedotto) {
		String parti[] = fattoDedotto.split(",");
		Cursor cursore = db.query(ReportTable.TABLE_NAME, ReportTable.COLUMNS, ReportTable.SENTRECEIVED+" = 1",	null, null, null, ReportTable._ID);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) {
				LogView.info(parti[1]+" "+cursore.getString(4));			
				if (cursore.getString(4).contains(parti[1])) {
					LogView.info("TROVATO");
					return true;
					}
					
			}
		return false;
	}
	
	public static ArrayList<Report> getListaFattiDedottiSalvati(SQLiteDatabase db) {
		ArrayList<Report> lista = new ArrayList<Report>();
		Cursor cursore = db.query(ReportTable.TABLE_NAME, ReportTable.COLUMNS, ReportTable.SENTRECEIVED+" = 1", null, null, null, BaseColumns._ID);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
					lista.add(new Report(cursore.getInt(0), cursore.getString(1), cursore.getString(2), cursore.getInt(3), cursore.getString(4), cursore.getInt(5), cursore.getInt(6), cursore.getInt(7)));
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
				value.put(ReportTable.LETTO, 1);
				
				db.insert(ReportTable.TABLE_NAME, null, value);
			}
		}
	}

	public static Report addNuovoFatto(SQLiteDatabase db, Report report) {
		Cursor cursore = db.query(ReportTable.TABLE_NAME, ReportTable.COLUMNS, null, null, null, null, BaseColumns._ID);
		if (cursore.getCount() > 0) {
			int indice = cursore.getCount()+1;
			report.setId(indice);
			report.setProlog(Prolog.setFatto(report));
			insert(db, report);
		}
		return report;
	}
	
	public static Report addNuovoFattoDedotto(SQLiteDatabase db, Report report) {
		Cursor cursore = db.query(ReportTable.TABLE_NAME, ReportTable.COLUMNS, null, null, null, null, ReportTable._ID);
		if (cursore.getCount() > 0) {
			int indice = cursore.getCount()+1;
			report.setId(indice);
			insert(db, report);
		}
		return report;
	}

	public static void cambiaStatoComponente(SQLiteDatabase db, String checkedItem) {
		Cursor cursore = db.query(ReportTable.TABLE_NAME, ReportTable.COLUMNS, ReportTable.ITEM+" = \""+checkedItem+"\" and "+ReportTable.SENTRECEIVED+" = 0", null, null, null, BaseColumns._ID);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
				if  (cursore.getString(4).contains("_choice")) {
					LogView.info(cursore.getString(4));
					Report rep = new Report(cursore.getInt(0), cursore.getString(1), cursore.getString(2), (cursore.getInt(3) == 0? 1:0), cursore.getString(4), cursore.getInt(5), cursore.getInt(6), cursore.getInt(7));
					if (rep.getProlog().contains(",open"))
						rep.setProlog(rep.getProlog().replace(",open", ",close"));
					if (rep.getProlog().contains(",close"))
						rep.setProlog(rep.getProlog().replace(",close", ",open"));
					if (rep.getProlog().contains(",on"))
						rep.setProlog(rep.getProlog().replace(",on", ",off"));
					if (rep.getProlog().contains(",off") && !rep.getProlog().contains("air_conditioner") && !rep.getProlog().contains("microwave"))
						rep.setProlog(rep.getProlog().replace(",off", ",on"));
					if (rep.getProlog().contains(",low"))
						rep.setProlog(rep.getProlog().replace(",low", ",off"));
					if (rep.getProlog().contains(",middle"))
						rep.setProlog(rep.getProlog().replace(",middle", ",off"));
					if (rep.getProlog().contains(",max"))
						rep.setProlog(rep.getProlog().replace(",max", ",off"));
					if (rep.getProlog().contains(",cook"))
						rep.setProlog(rep.getProlog().replace(",cook", ",off"));
					if (rep.getProlog().contains(",defrost"))
						rep.setProlog(rep.getProlog().replace(",defrost", ",off"));
					if (rep.getProlog().contains(",off") && rep.getProlog().contains("air_conditioner"))
						rep.setProlog(rep.getProlog().replace(",off", ",low"));
					if (rep.getProlog().contains(",off") && rep.getProlog().contains("microwave"))
						rep.setProlog(rep.getProlog().replace(",off", ",cook"));
					updateFattiComponente(db, rep.getProlog(), rep.getId());
				}
	}

	public static void stampaLista(SQLiteDatabase db) {
		Cursor cursore = db.query(ReportTable.TABLE_NAME, ReportTable.COLUMNS, null,	null, null, null, ReportTable._ID);
		if (cursore.getCount() > 0)
			while (cursore.moveToNext()) 
				LogView.info(cursore.getInt(0)+" "+cursore.getString(1)+"\t"+cursore.getString(2)+"\t"+cursore.getInt(3)+"\t"+cursore.getString(4)+"\t"+cursore.getInt(5)+"\t"+cursore.getInt(6)+"\t"+cursore.getInt(7));
		
	}
	
	
}
