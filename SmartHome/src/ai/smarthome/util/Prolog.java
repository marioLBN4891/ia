package ai.smarthome.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ai.smarthome.database.wrapper.Componente;
import ai.smarthome.database.wrapper.Configurazione;
import ai.smarthome.database.wrapper.Report;
import ai.smarthome.util.rest.Rest;
import android.database.sqlite.SQLiteDatabase;

public class Prolog {

	private final static String FORM_FATTO = "fact(_id,_fatto,_certezza).";
	private static String ID= "_id";
	private static String FATTO = "_fatto";
	private static String CERTEZZA = "_certezza";
		
	public static boolean startSimulazione() {
		return Rest.startSimulazione();
	}
	
	public static boolean stopSimulazione() {
		return Rest.stopSimulazione();
	}
	
	@SuppressWarnings("unchecked")
	public static boolean eseguiConfigurazione(SQLiteDatabase db, String timestamp) {
		
		Report.reset(db);
		
		ArrayList<Report> listaFatti = new ArrayList<Report>();
		
		listaFatti.add(isPresentUser(timestamp, false));
		
		Configurazione meteo = Configurazione.getConfigurazione(db);
		listaFatti.add(dataSimulazione(timestamp, meteo.getData()));
		listaFatti.add(oraSimulazione(timestamp, meteo.getOra()));
		listaFatti.add(temperaturaSimulazione(timestamp, meteo.getTemperaturaInt(), true));
		listaFatti.add(temperaturaSimulazione(timestamp, meteo.getTemperaturaInt(), false));
		listaFatti.add(umiditaSimulazione(timestamp, meteo.getUmiditaInt(), true));
		listaFatti.add(umiditaSimulazione(timestamp, meteo.getUmiditaInt(), false));
		listaFatti.add(ventoSimulazione(timestamp, meteo.getVento()));
		
		ArrayList<Report> listaFattiComponenti = new ArrayList<Report>();
		listaFattiComponenti = componentiSimulazione(timestamp, Componente.getAllLista(db));
		for(Report fattoComp : listaFattiComponenti)
			listaFatti.add(fattoComp);
		
		for(Report report : listaFatti) {
			Report.insert(db, report);
		}
		
		ArrayList<Report> listaReport= Report.getLista(db);
		for(Report report : listaReport) {
			if (!Rest.sendFactToServer(setFatto(report))) 
				return false;
		}
		
		HashMap<String, Serializable> risultati = Rest.inferisci();
		boolean stato = (Boolean) risultati.get("esito");
		ArrayList<Report> listaFattiDedotti = null;
		if(stato) {
			listaFattiDedotti = (ArrayList<Report>) risultati.get("listaFattiDedotti");
			if (listaFattiDedotti == null)
				return false;
			if (listaFattiDedotti.isEmpty())
				return true;
		}
		
		for(Report report : listaFattiDedotti) {
			Report.insert(db, report);
			if (!report.getItem().equals("")) {
				if (report.getStato() == 0)
					Componente.OffClose(db, report.getItem());
				if (report.getStato() == 1)
				Componente.OnOpen(db, report.getItem());
			}
		}
		
		return true;
	}
	
	
	@SuppressWarnings("unchecked")
	public static boolean sendFatto(SQLiteDatabase db, Report report) {
		Report.insert(db, report);
		
		ArrayList<Report> listaReport= Report.getListaNuovi(db);
		for(Report r : listaReport) {
			if (Rest.sendFactToServer(setFatto(r)))  {
			
				HashMap<String, Serializable> risultati = Rest.inferisci();
				boolean stato = (Boolean) risultati.get("esito");
				ArrayList<Report> listaFattiDedotti = null;
				if(stato) {
					listaFattiDedotti = (ArrayList<Report>) risultati.get("listaFattiDedotti");
					if (listaFattiDedotti == null)
						return false;
					if (listaFattiDedotti.isEmpty())
						return true;
				}
			
				for(Report r1 : listaFattiDedotti) {
					Report.insert(db, r1);
					if (!r1.getItem().equals("")) {
						if (r1.getStato() == 0)
						Componente.OffClose(db, r1.getItem());
					if (report.getStato() == 1)
						Componente.OnOpen(db, r1.getItem());
					}
				}
				return true;
			}
			else
				return false;
		}
		return false;
	}
	
	
	private static Report isPresentUser(String timestamp, boolean presenza) {
		if (presenza) 
			return (new Report("L\'utente è presente in cucina", "",0, creaStringaFatto("is_present_yes("+timestamp+")", "1.0"), 0, 1));
		else
			return (new Report("L\'utente non è presente in cucina", "",0, creaStringaFatto("is_present_no("+timestamp+")", "1.0"), 0, 1));
	}

	private static ArrayList<Report> componentiSimulazione(String timestamp, ArrayList<Componente> listaComp) {
		
		ArrayList<Report> listaFatti = new ArrayList<Report>();
		for (Componente comp : listaComp) {
			String fatto = comp.getProlog();
			String stato = "";
			if (comp.getTipo().equals(Componente.ACCESO_SPENTO) && comp.getStato() == 0)
				stato = "off";
			if (comp.getTipo().equals(Componente.ACCESO_SPENTO) && comp.getStato() == 1)
				stato = "on";
			if (comp.getTipo().equals(Componente.APERTO_CHIUSO) && comp.getStato() == 0)
				stato = "close";
			if (comp.getTipo().equals(Componente.APERTO_CHIUSO) && comp.getStato() == 1)
				stato = "open";
			listaFatti.add(new Report(comp.getNome()+" è "+stato , comp.getNome(), comp.getStato(), creaStringaFatto(fatto+"("+timestamp+","+stato+")", "1.0"), 0, 1));
			
		}
		
		return listaFatti;
	}
	
	private static Report dataSimulazione(String timestamp, long data) {
	
		Date dataSim = new Date();
		dataSim.setTime(data);
		@SuppressWarnings("deprecation")
		int mese = dataSim.getMonth();
		
    	switch (mese) {
		case 1: return (new Report("Mese", "", 0, creaStringaFatto("month_january("+timestamp+")", "1.0"), 0, 1));
		case 2: return (new Report("Mese", "", 0, creaStringaFatto("month_february("+timestamp+")", "1.0"), 0, 1));
		case 3: return (new Report("Mese", "", 0, creaStringaFatto("month_march("+timestamp+")", "1.0"), 0, 1));
		case 4: return (new Report("Mese", "", 0, creaStringaFatto("month_april("+timestamp+")", "1.0"), 0, 1));
		case 5: return (new Report("Mese", "", 0, creaStringaFatto("month_may("+timestamp+")", "1.0"), 0, 1));
		case 6: return (new Report("Mese", "", 0, creaStringaFatto("month_june("+timestamp+")", "1.0"), 0, 1));
		case 7: return (new Report("Mese", "", 0, creaStringaFatto("month_july("+timestamp+")", "1.0"), 0, 1));
		case 8: return (new Report("Mese", "", 0, creaStringaFatto("month_august("+timestamp+")", "1.0"), 0, 1));
		case 9: return (new Report("Mese", "", 0, creaStringaFatto("month_september("+timestamp+")", "1.0"), 0, 1));
		case 10: return (new Report("Mese", "", 0, creaStringaFatto("month_october("+timestamp+")", "1.0"), 0, 1));
		case 11: return (new Report("Mese", "", 0, creaStringaFatto("month_november("+timestamp+")", "1.0"), 0, 1));
		case 12: return (new Report("Mese", "", 0, creaStringaFatto("month_december("+timestamp+")", "1.0"), 0, 1));
		}
		return null;
		
	}
	
	private static Report oraSimulazione(String timestamp, int ora) {
		
		switch (ora) {
		case 1: return (new Report("Ora", "", 0, creaStringaFatto("time_1("+timestamp+")", "1.0"), 0, 1));
		case 2: return (new Report("Ora", "", 0, creaStringaFatto("time_2("+timestamp+")", "1.0"), 0, 1));
		case 3: return (new Report("Ora", "", 0, creaStringaFatto("time_3("+timestamp+")", "1.0"), 0, 1));
		case 4: return (new Report("Ora", "", 0, creaStringaFatto("time_4("+timestamp+")", "1.0"), 0, 1));
		case 5: return (new Report("Ora", "", 0, creaStringaFatto("time_5("+timestamp+")", "1.0"), 0, 1));
		case 6: return (new Report("Ora", "", 0, creaStringaFatto("time_6("+timestamp+")", "1.0"), 0, 1));
		case 7: return (new Report("Ora", "", 0, creaStringaFatto("time_7("+timestamp+")", "1.0"), 0, 1));
		case 8: return (new Report("Ora", "", 0, creaStringaFatto("time_8("+timestamp+")", "1.0"), 0, 1));
		case 9: return (new Report("Ora", "", 0, creaStringaFatto("time_9("+timestamp+")", "1.0"), 0, 1));
		case 10: return (new Report("Ora", "", 0, creaStringaFatto("time_10("+timestamp+")", "1.0"), 0, 1));
		case 11: return (new Report("Ora", "", 0, creaStringaFatto("time_11("+timestamp+")", "1.0"), 0, 1));
		case 12: return (new Report("Ora", "", 0, creaStringaFatto("time_12("+timestamp+")", "1.0"), 0, 1));
		case 13: return (new Report("Ora", "", 0, creaStringaFatto("time_13("+timestamp+")", "1.0"), 0, 1));
		case 14: return (new Report("Ora", "", 0, creaStringaFatto("time_14("+timestamp+")", "1.0"), 0, 1));
		case 15: return (new Report("Ora", "", 0, creaStringaFatto("time_15("+timestamp+")", "1.0"), 0, 1));
		case 16: return (new Report("Ora", "", 0, creaStringaFatto("time_16("+timestamp+")", "1.0"), 0, 1));
		case 17: return (new Report("Ora", "", 0, creaStringaFatto("time_17("+timestamp+")", "1.0"), 0, 1));
		case 18: return (new Report("Ora", "", 0, creaStringaFatto("time_18("+timestamp+")", "1.0"), 0, 1));
		case 19: return (new Report("Ora", "", 0, creaStringaFatto("time_19("+timestamp+")", "1.0"), 0, 1));
		case 20: return (new Report("Ora", "", 0, creaStringaFatto("time_20("+timestamp+")", "1.0"), 0, 1));
		case 21: return (new Report("Ora", "", 0, creaStringaFatto("time_21("+timestamp+")", "1.0"), 0, 1));
		case 22: return (new Report("Ora", "", 0, creaStringaFatto("time_22("+timestamp+")", "1.0"), 0, 1));
		case 23: return (new Report("Ora", "", 0, creaStringaFatto("time_23("+timestamp+")", "1.0"), 0, 1));
		case 0: return (new Report("Ora", "", 0, creaStringaFatto("time_0("+timestamp+")", "1.0"), 0, 1));
		
		}
		return null;
	}
	
	private static Report umiditaSimulazione(String timestamp, int umidita, boolean interno ) {
		String stato = null;
		if (interno) 
			stato = "int_";
		else
			stato = "ext_";
		if (umidita>= 0 && umidita < 20) return (new Report("Ora", "", 0, creaStringaFatto(stato+"humidity_v0_20("+timestamp+")", "1.0"), 0, 1));
		
		if (umidita>= 20 && umidita < 30) return (new Report("Ora", "", 0, creaStringaFatto(stato+"humidity_v20_30("+timestamp+")", "1.0"), 0, 1));
		
		if (umidita>= 30 && umidita < 40) return (new Report("Ora", "", 0, creaStringaFatto(stato+"humidity_v30_40("+timestamp+")", "1.0"), 0, 1));
		
		if (umidita>= 40 && umidita < 50) return (new Report("Ora", "", 0, creaStringaFatto(stato+"humidity_v40_50("+timestamp+")", "1.0"), 0, 1));
		
		if (umidita>= 50 && umidita < 60) return (new Report("Ora", "", 0, creaStringaFatto(stato+"humidity_v50_60("+timestamp+")", "1.0"), 0, 1));
		
		if (umidita>= 60 && umidita < 70) return (new Report("Ora", "", 0, creaStringaFatto(stato+"humidity_v60_70("+timestamp+")", "1.0"), 0, 1));
		
		if (umidita>= 70 && umidita < 80) return (new Report("Ora", "", 0, creaStringaFatto(stato+"humidity_v70_80("+timestamp+")", "1.0"), 0, 1));
		
		if (umidita>= 80 && umidita < 90) return (new Report("Ora", "", 0, creaStringaFatto(stato+"humidity_v80_90("+timestamp+")", "1.0"), 0, 1));
		
		if (umidita>= 90 && umidita <= 100) return (new Report("Ora", "", 0, creaStringaFatto(stato+"humidity_v90_100("+timestamp+")", "1.0"), 0, 1));
		
		
		return null;
	}
	
	private static Report temperaturaSimulazione(String timestamp, int temperatura, boolean interno ) {
		String stato = null;
		if (interno) 
			stato = "internal_";
		else
			stato = "external_";
		if (temperatura < -10) return (new Report("Temperatura "+stato, "", 0, creaStringaFatto(stato+"vBelowMinus10("+timestamp+")", "1.0"), 0, 1));
		
		if (temperatura>= -10 && temperatura < 0) return (new Report("Temperatura "+stato, "", 0, creaStringaFatto(stato+"temp_vMinus10_0("+timestamp+")", "1.0"), 0, 1));
		
		if (temperatura>= 0 && temperatura < 10 && !interno) return (new Report("Temperatura "+stato, "", 0, creaStringaFatto(stato+"temp_v0_10("+timestamp+")", "1.0"), 0, 1));
		
		if (temperatura>= 0 && temperatura < 5 && interno) return (new Report("Temperatura "+stato, "", 0, creaStringaFatto(stato+"temp_v0_5("+timestamp+")", "1.0"), 0, 1));
		
		if (temperatura>= 5 && temperatura < 10 && interno) return (new Report("Temperatura "+stato, "", 0, creaStringaFatto(stato+"temp_v5_10("+timestamp+")", "1.0"), 0, 1));
		
		if (temperatura>= 10 && temperatura < 20) return (new Report("Temperatura "+stato, "", 0, creaStringaFatto(stato+"temp_v10_20("+timestamp+")", "1.0"), 0, 1));
		
		if (temperatura>= 20 && temperatura < 30) return (new Report("Temperatura "+stato, "", 0, creaStringaFatto(stato+"temp_v20_30("+timestamp+")", "1.0"), 0, 1));
		
		if (temperatura>= 30 && temperatura < 40) return (new Report("Temperatura "+stato, "", 0, creaStringaFatto(stato+"temp_v30_40("+timestamp+")", "1.0"), 0, 1));
		
		if (temperatura>= 40) return (new Report("Temperatura "+stato, "", 0, creaStringaFatto(stato+"temp_vPlus40("+timestamp+")", "1.0"), 0, 1));
		
		
		return null;
	}
	
	private static Report ventoSimulazione(String timestamp, int vento) {
		
		if (vento >= 0 && vento <= 1) return (new Report("Vento", "", 0, creaStringaFatto("wind_calmo("+timestamp+")", "1.0"), 0, 1));
		
    	if (vento >= 2 && vento <= 3) return (new Report("Vento", "", 0, creaStringaFatto("wind_bava_di_vento("+timestamp+")", "1.0"), 0, 1));
		
    	if (vento >= 4 && vento <= 6) return (new Report("Vento", "", 0, creaStringaFatto("wind_brezza_leggera("+timestamp+")", "1.0"), 0, 1));
		
    	if (vento >= 7 && vento <= 10) return (new Report("Vento", "", 0, creaStringaFatto("wind_brezza("+timestamp+")", "1.0"), 0, 1));
		
    	if (vento >= 11 && vento <= 16) return (new Report("Vento", "", 0, creaStringaFatto("wind_brezza_vivace("+timestamp+")", "1.0"), 0, 1));
		
    	if (vento >= 17 && vento <= 21) return (new Report("Vento", "", 0, creaStringaFatto("wind_brezza_tesa("+timestamp+")", "1.0"), 0, 1));
		
    	if (vento >= 22 && vento <= 27) return (new Report("Vento", "", 0, creaStringaFatto("wind_vento_fresco("+timestamp+")", "1.0"), 0, 1));
		
    	if (vento >= 28 && vento <= 33) return (new Report("Vento", "", 0, creaStringaFatto("wind_vento_forte("+timestamp+")", "1.0"), 0, 1));
		
    	if (vento >= 34 && vento <= 40) return (new Report("Vento", "", 0, creaStringaFatto("wind_burrasca_moderata("+timestamp+")", "1.0"), 0, 1));
		
    	if (vento >= 41 && vento <= 47) return (new Report("Vento", "", 0, creaStringaFatto("wind_burrasca_forte("+timestamp+")", "1.0"), 0, 1));
		
    	if (vento >= 48 && vento <= 55) return (new Report("Vento", "", 0, creaStringaFatto("wind_tempesta("+timestamp+")", "1.0"), 0, 1));
		
    	if (vento >= 56 && vento <= 63) return (new Report("Vento", "", 0, creaStringaFatto("wind_fortunale("+timestamp+")", "1.0"), 0, 1));
		
    	if (vento >= 64) return (new Report("Vento", "", 0, creaStringaFatto("wind_uragano("+timestamp+")", "1.0"), 0, 1));
		
		
		return null;
	}
	
	public static String creaStringaFatto(String fatto,String certezza) {
		
		String newFatto = FORM_FATTO;
		newFatto = newFatto.replace(FATTO, fatto);
		newFatto = newFatto.replace(CERTEZZA, certezza);
		LogView.info(newFatto);
		return newFatto;
	}
	
	private static String setFatto(Report r) {
		
		String newFatto = r.getProlog();
		newFatto = newFatto.replace(ID, String.valueOf(r.getId()));
		LogView.info(newFatto);
		return newFatto;
	}
	
	public static Report fattoDedottoToReport(String fattoDedotto) {
		
		final String ON = "SHE ha acceso ";
		final String OFF = "SHE ha acceso ";
		final String OPEN = "SHE ha aperto ";
		final String CLOSE = "SHE ha chiuso ";
		if (fattoDedotto != null) {
			
			fattoDedotto.replace("fact(", "");
			String[] parti = fattoDedotto.split(",");
			String fatto = parti[1];
			
			String[] parziale = fatto.split("(");
			String azione = parziale[0];
			String item = "";
			int stato = 0;
			
			if (fatto.contains("on")) {
				stato = 1;
				String[] partiItem = fatto.split("_");
				item = partiItem[0];
				azione = ON+item;
			}
			if (fatto.contains("off")) { 
				stato = 0;
				String[] partiItem = fatto.split("_");
				item = partiItem[0];
				azione = OFF+item;
			}
			if (fatto.contains("open")) {
				stato = 1;
				String[] partiItem = fatto.split("_");
				item = partiItem[0];
				azione = OPEN+item;
			}
			if (fatto.contains("close")) {
				stato = 0;
				String[] partiItem = fatto.split("_");
				item = partiItem[0];
				azione = CLOSE+item;
			}
			return (new Report(azione, item, stato, fattoDedotto, 1, 1));
		}
		else
			return null;
		
	}

	
}