package ai.smarthome.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ai.smarthome.database.wrapper.Componente;
import ai.smarthome.database.wrapper.Configurazione;
import ai.smarthome.util.rest.Rest;
import android.database.sqlite.SQLiteDatabase;

public class Prolog {

	private final static String FORM_FATTO = "fact(_id,_fatto,_certezza).";
	private static String ID = "_id";
	private static String FATTO = "_fatto";
	private static String CERTEZZA = "_certezza";
		
	public static boolean startSimulazione() {
		return Rest.start();
	}
	
	public static boolean stopSimulazione() {
		return Rest.stop();
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static ArrayList<String> eseguiConfigurazione(SQLiteDatabase db) {
		
		ArrayList<String> listaFattiConfigurazione = new ArrayList<String>();
		ArrayList<String> listaFattiDedotti = new ArrayList<String>();
		int id = 1;
		listaFattiConfigurazione.add(creaStringaFatto(id, "utente(t0)", "1"));
		
		Configurazione meteo = Configurazione.getConfigurazione(db);
		
		Date data = new Date();
    	data.setTime(meteo.getData());
		String fattoData = "data(t0,"+data.getMonth()+","+data.getDate()+")";
		String fattoOra = "ora(t0,"+meteo.getOra()+")";
		String fattoTemperatura = "temperatura(t0,"+meteo.getTemperaturaInt()+")";
		String fattoUmidita = "umidita(t0,"+meteo.getUmiditaInt()+")";
		String fattoVento = "vento(t0,"+meteo.getVento()+")";
    	
		listaFattiConfigurazione.add(creaStringaFatto(++id, fattoData, "1"));
		listaFattiConfigurazione.add(creaStringaFatto(++id, fattoOra, "1"));
		listaFattiConfigurazione.add(creaStringaFatto(++id, fattoTemperatura, "1"));
		listaFattiConfigurazione.add(creaStringaFatto(++id, fattoUmidita, "1"));
		listaFattiConfigurazione.add(creaStringaFatto(++id, fattoVento, "1"));
		
		ArrayList<Componente> listaComp = Componente.getAllLista(db);
		for (Componente comp : listaComp) {
			id++;
			String componente = comp.getProlog();
			if (comp.getTipo().equals(Componente.ACCESO_SPENTO) && comp.getStato() == 0)
				componente = componente+"Off(t0)";
			if (comp.getTipo().equals(Componente.ACCESO_SPENTO) && comp.getStato() == 1)
				componente = componente+"On(t0)";
			if (comp.getTipo().equals(Componente.APERTO_CHIUSO) && comp.getStato() == 0)
				componente = componente+"Close(t0)";
			if (comp.getTipo().equals(Componente.APERTO_CHIUSO) && comp.getStato() == 1)
				componente = componente+"Open(t0)";
			listaFattiConfigurazione.add(creaStringaFatto(id, componente, "1"));
		}
		
		
		for(String fatto : listaFattiConfigurazione) {
			HashMap<String, Serializable> risultati = Rest.request(fatto);
			if ((Boolean) risultati.get("esito") == true) {
				ArrayList<String> listaTemp = (ArrayList<String>) risultati.get("listaFatti");
				for (String fattoTemp : listaTemp) {
					listaFattiDedotti.add(fattoTemp);
				}
			}
		}
		return listaFattiDedotti;
		
	}
	
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> eseguiFatto(int id, String fatto,String certezza) {
		
		HashMap<String, Serializable> risultati = Rest.request(creaStringaFatto(id, fatto, certezza));
		if ((Boolean) risultati.get("esito") == true) 
			return (ArrayList<String>) risultati.get("listaFatti");
		else
			return null;
		
	}
	
	private static String isPresentUser(int id, boolean presenza, String timestamp) {
		if (presenza)
			return creaStringaFatto(id, "is_present_yes("+timestamp+")", "1.0");
		else
			return creaStringaFatto(id, "is_present_no("+timestamp+")", "1.0");
	}

	private static ArrayList<String> componentiSimulazione(int id, String timestamp, ArrayList<Componente> listaComp) {
		
		ArrayList<String> listaFatti = new ArrayList<String>();
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
			listaFatti.add(creaStringaFatto(id, fatto+"("+timestamp+","+stato+")", "1.0"));
			id++;
		}
		
		return listaFatti;
	}
	
	private static String dataSimulazione(int id, String timestamp, int mese) {
	
		switch (mese) {
		case 1: return creaStringaFatto(id, "month_january("+timestamp+")", "1.0");
		case 2: return creaStringaFatto(id, "month_february("+timestamp+")", "1.0");
		case 3: return creaStringaFatto(id, "month_march("+timestamp+")", "1.0");
		case 4: return creaStringaFatto(id, "month_april("+timestamp+")", "1.0");
		case 5: return creaStringaFatto(id, "month_may("+timestamp+")", "1.0");
		case 6: return creaStringaFatto(id, "month_june("+timestamp+")", "1.0");
		case 7: return creaStringaFatto(id, "month_july("+timestamp+")", "1.0");
		case 8: return creaStringaFatto(id, "month_august("+timestamp+")", "1.0");
		case 9: return creaStringaFatto(id, "month_september("+timestamp+")", "1.0");
		case 10: return creaStringaFatto(id, "month_october("+timestamp+")", "1.0");
		case 11: return creaStringaFatto(id, "month_november("+timestamp+")", "1.0");
		case 12: return creaStringaFatto(id, "month_december("+timestamp+")", "1.0");
		}
		return null;
	}
	
	private static String oraSimulazione(int id, String timestamp, int ora) {
		
		switch (ora) {
		case 1: return creaStringaFatto(id, "time_1("+timestamp+")", "1.0");
		case 2: return creaStringaFatto(id, "time_2("+timestamp+")", "1.0");
		case 3: return creaStringaFatto(id, "time_3("+timestamp+")", "1.0");
		case 4: return creaStringaFatto(id, "time_4("+timestamp+")", "1.0");
		case 5: return creaStringaFatto(id, "time_5("+timestamp+")", "1.0");
		case 6: return creaStringaFatto(id, "time_6("+timestamp+")", "1.0");
		case 7: return creaStringaFatto(id, "time_7("+timestamp+")", "1.0");
		case 8: return creaStringaFatto(id, "time_8("+timestamp+")", "1.0");
		case 9: return creaStringaFatto(id, "time_9("+timestamp+")", "1.0");
		case 10: return creaStringaFatto(id, "time_10("+timestamp+")", "1.0");
		case 11: return creaStringaFatto(id, "time_11("+timestamp+")", "1.0");
		case 12: return creaStringaFatto(id, "time_12("+timestamp+")", "1.0");
		case 13: return creaStringaFatto(id, "time_13("+timestamp+")", "1.0");
		case 14: return creaStringaFatto(id, "time_14("+timestamp+")", "1.0");
		case 15: return creaStringaFatto(id, "time_15("+timestamp+")", "1.0");
		case 16: return creaStringaFatto(id, "time_16("+timestamp+")", "1.0");
		case 17: return creaStringaFatto(id, "time_17("+timestamp+")", "1.0");
		case 18: return creaStringaFatto(id, "time_18("+timestamp+")", "1.0");
		case 19: return creaStringaFatto(id, "time_19("+timestamp+")", "1.0");
		case 20: return creaStringaFatto(id, "time_20("+timestamp+")", "1.0");
		case 21: return creaStringaFatto(id, "time_21("+timestamp+")", "1.0");
		case 22: return creaStringaFatto(id, "time_22("+timestamp+")", "1.0");
		case 23: return creaStringaFatto(id, "time_23("+timestamp+")", "1.0");
		case 0: return creaStringaFatto(id, "time_0("+timestamp+")", "1.0");
		}
		return null;
	}
	
	private static String umiditaSimulazione(int id, boolean interno, int umidita, String timestamp) {
		String stato = null;
		if (interno) 
			stato = "int_";
		else
			stato = "ext_";
		
		if (umidita>= 0 && umidita < 20) return creaStringaFatto(id, stato+"humidity_v0_20("+timestamp+")", "1.0");
		if (umidita>= 20 && umidita < 30) return creaStringaFatto(id, stato+"humidity_v20_30("+timestamp+")", "1.0");
		if (umidita>= 30 && umidita < 40) return creaStringaFatto(id, stato+"humidity_v30_40("+timestamp+")", "1.0");
		if (umidita>= 40 && umidita < 50) return creaStringaFatto(id, stato+"humidity_v40_50("+timestamp+")", "1.0");
		if (umidita>= 50 && umidita < 60) return creaStringaFatto(id, stato+"humidity_v50_60("+timestamp+")", "1.0");
		if (umidita>= 60 && umidita < 70) return creaStringaFatto(id, stato+"humidity_v60_70("+timestamp+")", "1.0");
		if (umidita>= 70 && umidita < 80) return creaStringaFatto(id, stato+"humidity_v70_80("+timestamp+")", "1.0");
		if (umidita>= 80 && umidita < 90) return creaStringaFatto(id, stato+"humidity_v80_90("+timestamp+")", "1.0");
		if (umidita>= 90 && umidita <= 100) return creaStringaFatto(id, stato+"humidity_v90_100("+timestamp+")", "1.0");
		
		return null;
	}
	
	private static String temperaturaSimulazione(int id, boolean interno, int temperatura, String timestamp) {
		String stato = null;
		if (interno) 
			stato = "internal_";
		else
			stato = "external_";
		
		if (temperatura < -10) return creaStringaFatto(id, stato+"vBelowMinus10("+timestamp+")", "1.0");
		if (temperatura>= -10 && temperatura < 0) return creaStringaFatto(id, stato+"temp_vMinus10_0("+timestamp+")", "1.0");
		if (temperatura>= 0 && temperatura < 10 && !interno) return creaStringaFatto(id, stato+"temp_v0_10("+timestamp+")", "1.0");
		if (temperatura>= 0 && temperatura < 5 && interno) return creaStringaFatto(id, stato+"temp_v0_5("+timestamp+")", "1.0");
		if (temperatura>= 5 && temperatura < 10 && interno) return creaStringaFatto(id, stato+"temp_v5_10("+timestamp+")", "1.0");
		if (temperatura>= 10 && temperatura < 20) return creaStringaFatto(id, stato+"temp_v10_20("+timestamp+")", "1.0");
		if (temperatura>= 20 && temperatura < 30) return creaStringaFatto(id, stato+"temp_v20_30("+timestamp+")", "1.0");
		if (temperatura>= 30 && temperatura < 40) return creaStringaFatto(id, stato+"temp_v30_40("+timestamp+")", "1.0");
		if (temperatura>= 40) return creaStringaFatto(id, stato+"temp_vPlus40("+timestamp+")", "1.0");
		
		return null;
	}
	
	private static String ventoSimulazione(int id, int vento, String timestamp) {
		
		if (vento >= 0 && vento <= 1) return creaStringaFatto(id,"wind_calmo("+timestamp+")", "1.0");
    	if (vento >= 2 && vento <= 3) return creaStringaFatto(id,"wind_bava_di_vento("+timestamp+")", "1.0");
    	if (vento >= 4 && vento <= 6) return creaStringaFatto(id,"wind_brezza_leggera("+timestamp+")", "1.0");
    	if (vento >= 7 && vento <= 10) return creaStringaFatto(id,"wind_brezza("+timestamp+")", "1.0");
    	if (vento >= 11 && vento <= 16) return creaStringaFatto(id,"wind_brezza_vivace("+timestamp+")", "1.0");
    	if (vento >= 17 && vento <= 21) return creaStringaFatto(id,"wind_brezza_tesa("+timestamp+")", "1.0");;
    	if (vento >= 22 && vento <= 27) return creaStringaFatto(id,"wind_vento_fresco("+timestamp+")", "1.0");
    	if (vento >= 28 && vento <= 33) return creaStringaFatto(id,"wind_vento_forte("+timestamp+")", "1.0");
    	if (vento >= 34 && vento <= 40) return creaStringaFatto(id,"wind_burrasca_moderata("+timestamp+")", "1.0");
    	if (vento >= 41 && vento <= 47) return creaStringaFatto(id,"wind_burrasca_forte("+timestamp+")", "1.0");
    	if (vento >= 48 && vento <= 55) return creaStringaFatto(id,"wind_tempesta("+timestamp+")", "1.0");
    	if (vento >= 56 && vento <= 63) return creaStringaFatto(id,"wind_fortunale("+timestamp+")", "1.0");
    	if (vento >= 64) return creaStringaFatto(id,"wind_uragano("+timestamp+")", "1.0");
		
		return null;
	}
	
	private static String creaStringaFatto(int id, String fatto,String certezza) {
		
		String newFatto = FORM_FATTO;
		newFatto = newFatto.replace(ID, String.valueOf(id));
		newFatto = newFatto.replace(FATTO, fatto);
		newFatto = newFatto.replace(CERTEZZA, certezza);
		LogView.info(newFatto);
		return newFatto;
	}
	
}