package ai.smarthome.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ai.smarthome.database.wrapper.Componente;
import ai.smarthome.database.wrapper.Configurazione;
import ai.smarthome.database.wrapper.Impostazione;
import ai.smarthome.database.wrapper.Oggetto;
import ai.smarthome.database.wrapper.Report;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.xmlrpc.XMLRPC;
import android.database.sqlite.SQLiteDatabase;

public class Prolog {

	private final static String FORM_FATTO = "fact(_id,_fatto,_certezza).";
	private static String ID= "_id";
	private static String FATTO = "_fatto";
	private static String CERTEZZA = "_certezza";
		
	public static boolean startSimulazione(Utente user, SQLiteDatabase db) {
		return XMLRPC.startSimulazione(user, Impostazione.getIndirizzo(db));
	}
	
	public static boolean stopSimulazione(Utente user, SQLiteDatabase db) {
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean eseguiConfigurazione(SQLiteDatabase db, String timestamp, Utente user, Report reportAction) {
		String serverAddress = Impostazione.getIndirizzo(db);
		
		Report.updateReportLetti(db);
		
		ArrayList<Report> listaFatti = new ArrayList<Report>();
		
		listaFatti = Report.getListaToUse(db);
			
		if (listaFatti == null || listaFatti.isEmpty()) {
			Configurazione meteo = Configurazione.getConfigurazione(db);
			listaFatti.add(dataSimulazione(timestamp, meteo.getData()));
			listaFatti.add(oraSimulazione(timestamp, meteo.getOra()));
			listaFatti.add(temperaturaSimulazione(timestamp, meteo.getTemperaturaInt(), true));
			listaFatti.add(temperaturaSimulazione(timestamp, meteo.getTemperaturaInt(), false));
			listaFatti.add(umiditaSimulazione(timestamp, meteo.getUmiditaInt(), true));
			listaFatti.add(umiditaSimulazione(timestamp, meteo.getUmiditaInt(), false));
			listaFatti.add(ventoSimulazione(timestamp, meteo.getVento()));
			listaFatti.add(luminositaSimulazione(timestamp, meteo.getLuminosita()));
			
			ArrayList<Report> listaFattiComponenti = new ArrayList<Report>();
			listaFattiComponenti = componentiSimulazione(timestamp, Componente.getAllLista(db));
			for(Report fattoComp : listaFattiComponenti) {
				listaFatti.add(fattoComp);
			}
			for(Report r : listaFatti) 
				Report.insert(db, r);
		}
		
		if (reportAction != null) {
			Report r1 = isPresentUser(timestamp, user.isPresente(), true);
			Report r2 = statoAttualeUser(timestamp, user.isPresente(), user.getEsterno(), true);
			listaFatti.add(r1);
			listaFatti.add(r2);
			listaFatti.add(reportAction);
			Report.insert(db, reportAction);
		}
		else {
			Report r1 = isPresentUser(timestamp, user.isPresente(), true);
			Report r2 = statoAttualeUser(timestamp, user.isPresente(), user.getEsterno(), true);
			listaFatti.add(r1);
			listaFatti.add(r2);
			Report.insert(db, r1);
			Report.insert(db, r2);
		}
		
		ArrayList<Oggetto> listaOggetti = Oggetto.getListaPresi(db);
		if(!listaOggetti.isEmpty() && listaOggetti != null) {
			ArrayList<Report> listaFattiOggetti = new ArrayList<Report>();
			listaFattiOggetti = oggettiSimulazione(timestamp, listaOggetti);
			for(Report fattoOgg : listaFattiOggetti) {
				listaFatti.add(fattoOgg);
			}
		}
		
		ArrayList<String> listaProlog = new ArrayList<String>();
		for(Report repTosend : listaFatti) {
			listaProlog.add(repTosend.getProlog());
		}
		
		HashMap<String, Serializable> risultati = XMLRPC.inferisci(user, db, serverAddress, listaProlog);
		boolean stato = (Boolean) risultati.get("esito");
		ArrayList<Report> listaFattiDedotti = null;
		if(stato) {
			listaFattiDedotti = (ArrayList<Report>) risultati.get("listaDedotti");
			if (listaFattiDedotti == null)
				return false;
			if (listaFattiDedotti.isEmpty())
				return true;
		}
		else
			return false;
		
	//	ArrayList<Report> listaFattiDedottiSalvati = Report.getListaFattiDedottiSalvati(db);
		for(Report dedtToSave : listaFattiDedotti) {
	//		if (!checkDeduzioneRidondante(db, dedtToSave.getProlog(), listaFattiDedottiSalvati)) {
				Report.insert(db, dedtToSave);
				if (!dedtToSave.getItem().equals("")) {
					Componente.cambiaStato(db, dedtToSave.getItem(), dedtToSave.getStato());
					Report.cambiaStatoComponente(db, dedtToSave.getItem(), dedtToSave.getStato());
				}
	//		}
		}
		
		return true;
		
	}
	
	
	public static boolean retract(SQLiteDatabase db, String timestamp, Utente user, ArrayList<Report> listaReport) {
		String serverAddress = Impostazione.getIndirizzo(db);
		
		Report.updateReportLetti(db);
		
		ArrayList<String> listaRetract = new ArrayList<String>();
		
		for(Report report: listaReport) {
			listaRetract.add(report.getProlog());
		}
		
		if (XMLRPC.retract(user, serverAddress, listaRetract)) {
			for(Report report: listaReport) {
				Report.insert(db, report);
			}
		}
		else
			return false;
		
		return true;
	}
	
	
	public static Report isPresentUser(String timestamp, boolean presenza, boolean attuale) {
		int nuovo = 0;
		if (attuale)
			nuovo = 1;
		if (presenza) 
			return (new Report("L\'utente è presente in cucina", "",0, creaStringaFatto("is_present_kitchen("+timestamp+")", "1.0"), 0, nuovo, 1, 0));
		else
			return (new Report("L\'utente non è presente in cucina", "",0, creaStringaFatto("is_present_kitchen_out("+timestamp+")", "1.0"), 0, nuovo, 1, 0));
	}
	
	public static Report statoAttualeUser(String timestamp, boolean presenza, boolean esterno, boolean attuale) {
		int nuovo = 0;
		if (attuale)
			nuovo = 1;
		if (presenza)
			if (esterno)
				return (new Report("L\'utente viene dall\'esterno", "",0, creaStringaFatto("came_out("+timestamp+")", "1.0"), 0, nuovo, 1, 0));
			else
				return (new Report("L\'utente viene da un\'altra camera", "",0, creaStringaFatto("came_in("+timestamp+")", "1.0"), 0, nuovo, 1, 0));
		else
			if (esterno)
				return (new Report("L\'utente va all\'esterno", "",0, creaStringaFatto("go_out("+timestamp+")", "1.0"), 0, 1, 1, 0));
			else
				return (new Report("L\'utente va in un\'altra camera", "",0, creaStringaFatto("go_in("+timestamp+")", "1.0"), 0, 1, 1, 0));
	}
	
	private static ArrayList<Report> componentiSimulazione(String timestamp, ArrayList<Componente> listaComp) {
		
		ArrayList<Report> listaFatti = new ArrayList<Report>();
		for (Componente comp : listaComp) {
			String fatto = comp.getProlog()+"_choice";
			String stato = "";
			String statoAzione = "";
			if(!(comp.getNome().equals("Dispensa") || comp.getNome().equals("Mobile") || comp.getNome().equals("Frigorifero"))) {
				if (comp.getTipo().equals(Componente.ACCESO_SPENTO) && comp.getStato() == 0) {
					stato = "off";
					statoAzione = "spento";
				}
				if (comp.getTipo().equals(Componente.ACCESO_SPENTO) && comp.getStato() == 1) {
					stato = "on";
					statoAzione = "acceso";
				}
				
				if (comp.getProlog().equals("air_conditioner")) {
					if(comp.getStato() == 1) {
						stato = "low";
						statoAzione = "acceso: potenza bassa";
					}
					if(comp.getStato() == 2) {
						stato = "middle";
						statoAzione = "acceso: potenza media";
					}
					if(comp.getStato() == 3) {
						stato = "max";
						statoAzione = "acceso: potenza massima";
					}
					if(comp.getStato() == 4) {
						stato = "max";
						statoAzione = "acceso: deumidificatore";
					}
				}
				if (comp.getProlog().equals("radiator")) {
					if(comp.getStato() == 1) {
						stato = "low";
						statoAzione = "acceso: potenza bassa";
					}
					if(comp.getStato() == 2) {
						stato = "middle";
						statoAzione = "acceso: potenza media";
					}
					if(comp.getStato() == 3) {
						stato = "max";
						statoAzione = "acceso: potenza massima";
					}
				}
				
				if (comp.getProlog().equals("lighting")) {
					if(comp.getStato() == 1) {
						stato = "100";
						statoAzione = "acceso: intensità bassa";
					}
					if(comp.getStato() == 2) {
						stato = "middle";
						statoAzione = "acceso: intensità media";
					}
					if(comp.getStato() == 3) {
						stato = "max";
						statoAzione = "acceso: intensità massima";
					}
				}
				
				if (comp.getProlog().equals("microwave_oven"))
					if(comp.getStato() == 1) {
						stato = "heat";
						statoAzione = "acceso: riscaldamento";
					}
					if(comp.getStato() == 2) {
						stato = "defrost";
						statoAzione = "acceso: scongelamento";
					}
					
				if (comp.getTipo().equals(Componente.APERTO_CHIUSO) && comp.getStato() == 0) {
					stato = "close";
					statoAzione = "chiuso";
				}
				if (comp.getTipo().equals(Componente.APERTO_CHIUSO) && comp.getStato() == 1) {
					stato = "open";
					statoAzione = "aperto";
				}
				listaFatti.add(new Report(comp.getNome()+" è "+statoAzione , comp.getNome(), comp.getStato(), creaStringaFatto(fatto+"_"+stato+"("+timestamp+")", "1.0"), 0, 0, 1, 1));
			}
		}
		
		return listaFatti;
	}
	
	private static ArrayList<Report> oggettiSimulazione(String timestamp, ArrayList<Oggetto> listaOgg) {
	
		ArrayList<Report> listaFatti = new ArrayList<Report>();
		for (Oggetto ogg : listaOgg) {
			listaFatti.add(new Report("Hai preso "+ogg.getNome(), ogg.getNome(), ogg.getStato(), creaStringaFatto("pick_"+ogg.getProlog()+"("+timestamp+")", "1.0"), 0, 0, 1, 0));
		}
		
		return listaFatti;
	}

	private static Report dataSimulazione(String timestamp, long data) {
	
		Date dataSim = new Date();
		dataSim.setTime(data);
		@SuppressWarnings("deprecation")
		int mese = dataSim.getMonth();
		
		String meseIta = "";
		String meseFatto = "month_";
    	switch (mese+1) {
		case 1: meseIta = "Gennaio";
				meseFatto = meseFatto.replace("_", "_january");
				break;
		case 2: meseIta = "Febbraio";
				meseFatto = meseFatto.replace("_", "_february");
				break;
		case 3: meseIta = "Marzo";
				meseFatto = meseFatto.replace("_", "_march");
				break;
		case 4: meseIta = "Aprile";
				meseFatto = meseFatto.replace("_", "_april");
				break;
		case 5: meseIta = "Maggio";
				meseFatto = meseFatto.replace("_", "_may");
				break;
		case 6: meseIta = "Giugno";
				meseFatto = meseFatto.replace("_", "_june");
				break;
		case 7: meseIta = "Luglio";
				meseFatto = meseFatto.replace("_", "_july");
				break;
		case 8: meseIta = "Agosto";
				meseFatto = meseFatto.replace("_", "_august");
				break;
		case 9: meseIta = "Settembre";
				meseFatto = meseFatto.replace("_", "_september");
				break;
		case 10: meseIta = "Ottobre";
				meseFatto = meseFatto.replace("_", "_october");
				break;
		case 11: meseIta = "Novembre";
				meseFatto = meseFatto.replace("_", "_november");
				break;
		case 12: meseIta = "Dicembre";
				meseFatto = meseFatto.replace("_", "_december");
				break;
		}
		return (new Report("E' il Mese di "+meseIta, "", 0, creaStringaFatto(meseFatto+"("+timestamp+")", "1.0"), 0, 0, 1, 1));
		
	}
	
	private static Report oraSimulazione(String timestamp, int ora) {
		String fatto = "time_"+ora;
		return (new Report("Sono le ore "+ora, "", 0, creaStringaFatto(fatto+"("+timestamp+")", "1.0"), 0, 0, 1, 1));
	}
	
	private static Report umiditaSimulazione(String timestamp, int umidita, boolean interno ) {
		String stato = "";
		String statoIta = "Umidità ";
		
		if (interno) {
			stato = "int_humidity_v";
			statoIta += "interna: "+umidita+"%";
		}	
		else {
			stato = "ext_humidity_v";
			statoIta += "esterna: "+umidita+"%";
		}
		
		if (umidita>= 0 && umidita < 20) stato = stato.replace("v","v0_20");
		if (umidita>= 20 && umidita < 30) stato = stato.replace("v","v20_30");
		if (umidita>= 30 && umidita < 40) stato = stato.replace("v","v30_40");
		if (umidita>= 40 && umidita < 50) stato = stato.replace("v","v40_50");
		if (umidita>= 50 && umidita < 60) stato = stato.replace("v","v50_60");
		if (umidita>= 60 && umidita < 70) stato = stato.replace("v","v60_70");
		if (umidita>= 70 && umidita < 80) stato = stato.replace("v","v70_80");
		if (umidita>= 80 && umidita < 90) stato = stato.replace("v","v80_90");
		if (umidita>= 90 && umidita <= 100) stato = stato.replace("v","v90_100");
		
		return (new Report(statoIta, "", 0, creaStringaFatto(stato+"("+timestamp+")", "1.0"), 0, 0, 1, 1));
	}
	
	private static Report luminositaSimulazione(String timestamp, int luminosita) {
		String stato = "tot_lux_v";
		String statoIta = "Luminosità esterna: "+luminosita+"0 Lux";
		
		if (luminosita>= 0 && luminosita < 10) stato = stato.replace("v","v0_100");
		if (luminosita>= 10 && luminosita < 20) stato = stato.replace("v","v100_200");
		if (luminosita>= 20 && luminosita < 30) stato = stato.replace("v","v200_300");
		if (luminosita>= 30 && luminosita < 40) stato = stato.replace("v","v300_400");
		if (luminosita>= 40 && luminosita < 50) stato = stato.replace("v","v400_500");
		if (luminosita>= 50 && luminosita < 60) stato = stato.replace("v","v500_600");
		if (luminosita>= 60 && luminosita < 80) stato = stato.replace("v","v600_800");
		if (luminosita>= 80 && luminosita <= 100) stato = stato.replace("v","v800_1000");
		
		return (new Report(statoIta, "", 0, creaStringaFatto(stato+"("+timestamp+")", "1.0"), 0, 0, 1, 1));
	}
	
	private static Report temperaturaSimulazione(String timestamp, int temperatura, boolean interno ) {
		String stato = "";
		String statoIta = "Temperatura ";
		
		if (interno) {
			stato = "internal_temp_v";
			statoIta += "interna: "+temperatura+ " C°";
		}	
		else {
			stato = "external_temp_v";
			statoIta += "esterna: "+temperatura+ " C°";
		}
		
		if (temperatura < -10) stato = stato.replace("v","vBelowMinus10");
		if (temperatura>= -10 && temperatura < 0) stato = stato.replace("v","vMinus10_0");
		if (temperatura>= 0 && temperatura < 10 && !interno) stato = stato.replace("v","v0_10");
		if (temperatura>= 0 && temperatura < 5 && interno) stato = stato.replace("v","v0_5");
		if (temperatura>= 5 && temperatura < 10 && interno) stato = stato.replace("v","v5_10");
		if (temperatura>= 10 && temperatura < 20) stato = stato.replace("v","v10_20");
		if (temperatura>= 20 && temperatura < 30) stato = stato.replace("v","v20_30");
		if (temperatura>= 30 && temperatura < 40) stato = stato.replace("v","v30_40");
		if (temperatura>= 40) stato = stato.replace("v","vPlus40");
		
		return (new Report(statoIta, "", 0, creaStringaFatto(stato+"("+timestamp+")", "1.0"), 0, 0, 1, 1));
	}
	
	private static Report ventoSimulazione(String timestamp, int vento) {
		
		String stato = "";
		
		if (vento >= 0 && vento <= 1) stato = "calmo";
		if (vento >= 2 && vento <= 3) stato = "bava_di_vento";
		if (vento >= 4 && vento <= 6) stato = "brezza_leggera";
		if (vento >= 7 && vento <= 10) stato = "brezza";
		if (vento >= 11 && vento <= 16) stato = "brezza_vivace";
		if (vento >= 17 && vento <= 21) stato = "brezza_tesa";
		if (vento >= 22 && vento <= 27) stato = "vento_fresco";
		if (vento >= 28 && vento <= 33) stato = "vento_forte";
		if (vento >= 34 && vento <= 40) stato = "burrasca_moderata";
		if (vento >= 41 && vento <= 47) stato = "burrasca_forte";
		if (vento >= 48 && vento <= 55) stato = "tempesta";
		if (vento >= 56 && vento <= 63) stato = "fortunale";
		if (vento >= 64) stato = "uragano";
		
		return (new Report("Vento: "+stato.replace("_", " "), "", 0, creaStringaFatto("wind_"+stato+"("+timestamp+")", "1.0"), 0, 0, 1, 1));
	}
	
	
	public static String creaStringaFatto(String fatto,String certezza) {
		
		String newFatto = FORM_FATTO;
		newFatto = newFatto.replace(FATTO, fatto);
		newFatto = newFatto.replace(CERTEZZA, certezza);
		return newFatto;
	}
	
	public static String setFatto(String fatto, int indice) {
		
		String newFatto = fatto;
		newFatto = newFatto.replace(ID, String.valueOf(indice));
		return newFatto;
	}
	
	public static Report fattoDedottoToReport(SQLiteDatabase db, String fattoDedotto) {
		final String CASA = "C@SA: ";
		final String ON = " acceso";
		final String OFF = " spento";
		final String OPEN = " aperto";
		final String CLOSE = " chiuso";
		
		int stato = 0;
		String azione = "";
		String item = "";
	
		if (fattoDedotto != null) {
			ArrayList<Componente> listaComp = Componente.getAllLista(db);
			for (Componente comp : listaComp)
				if(fattoDedotto.contains(comp.getProlog())) {
					item = comp.getNome();
				
					if (comp.getProlog().contains("air_conditioner")) {
						if (fattoDedotto.contains("_low")) {
							stato = 1;
							azione = CASA+item+ON+": potenza bassa";
						}
						if (fattoDedotto.contains("_middle")) {
							stato = 2;
							azione = CASA+item+ON+": potenza media";
						}
						if (fattoDedotto.contains("_max")) {
							stato = 3;
							azione = CASA+item+ON+": potenza massima";
						}
						if (fattoDedotto.contains("_dehumidifier")) {
							stato = 4;
							azione = CASA+item+ON+": deumidificatore";
						}
					}
					
					if (comp.getProlog().contains("radiator")) {
						if (fattoDedotto.contains("_low")) {
							stato = 1;
							azione = CASA+item+ON+": potenza bassa";
						}
						if (fattoDedotto.contains("_middle")) {
							stato = 2;
							azione = CASA+item+ON+": potenza media";
						}
						if (fattoDedotto.contains("_max")) {
							stato = 3;
							azione = CASA+item+ON+": potenza massima";
						}
					}
					
					if (comp.getProlog().contains("microwave_oven")) {
						if (fattoDedotto.contains("_heat")) {
							stato = 1;
							azione = CASA+item+ON+": riscaldamento";
						}
						if (fattoDedotto.contains("_defrost")) {
							stato = 2;
							azione = CASA+item+ON+": scongelamento";
						}
					}
					
					if (comp.getProlog().contains("lighting")) {
						if (fattoDedotto.contains("_100")) {
							stato = 1;
							azione = CASA+item+ON+": intensità bassa";
						}
						if (fattoDedotto.contains("_200")) {
							stato = 2;
							azione = CASA+item+ON+": intensità media";
						}
						if (fattoDedotto.contains("_300")) {
							stato = 3;
							azione = CASA+item+ON+": intensità alta";
						}
					}
					
					if (fattoDedotto.contains("_on")) {
						stato = 1;
						azione = CASA+item+ON;
					}
					if (fattoDedotto.contains("_off")) { 
						stato = 0;
						azione = CASA+item+OFF;
					}
					if (fattoDedotto.contains("_open")) {
						stato = 1;
						azione = CASA+item+OPEN;
					}
					if (fattoDedotto.contains("_close")) {
						stato = 0;
						azione = CASA+item+CLOSE;
					}
				}
			
			if(fattoDedotto.contains("season_winter")) 	azione = "C@SA: inverno";
			if(fattoDedotto.contains("season_spring")) 	azione = "C@SA: primavera";
			if(fattoDedotto.contains("season_summer")) 	azione = "C@SA: estate";
			if(fattoDedotto.contains("season_fall")) 	azione = "C@SA: autunno";
			if(fattoDedotto.contains("season_cold")) 	azione = "C@SA: stagione fredda";
			if(fattoDedotto.contains("season_warm")) 	azione = "C@SA: stagione calda";
			
			if(fattoDedotto.contains("time_morning")) 	azione = "C@SA: mattina";
			if(fattoDedotto.contains("time_afternoon")) azione = "C@SA: pomeriggio";
			if(fattoDedotto.contains("time_evening")) 	azione = "C@SA: sera";
			if(fattoDedotto.contains("time_night")) 	azione = "C@SA: notte";
			if(fattoDedotto.contains("time_breakfast_yes")) azione = "C@SA: ora della colazione";
			if(fattoDedotto.contains("time_lunch_yes")) 	azione = "C@SA: ora del pranzo";
			if(fattoDedotto.contains("time_dinner_yes")) 	azione = "C@SA: ora della cena";
			if(fattoDedotto.contains("time_break_yes")) 	azione = "C@SA: ora del break";
		/*	
			if(fattoDedotto.contains("humidier_equal")) 			azione = CASA+"umidità interna ed esterna sono uguali";
			if(fattoDedotto.contains("humidier_inside_low")) 		azione = CASA+"umidità interna poco inferiore rispetto l'esterno";
			if(fattoDedotto.contains("humidier_inside_middle"))		azione = CASA+"umidità interna abbastanza inferiore rispetto l'esterno";
			if(fattoDedotto.contains("humidier_inside_high")) 		azione = CASA+"umidità interna molto inferiore rispetto l'esterno";
			if(fattoDedotto.contains("humidier_outside_low")) 		azione = CASA+"umidità interna poco superiore rispetto l'esterno";
			if(fattoDedotto.contains("humidier_outside_middle")) 	azione = CASA+"umidità esterna abbastanza superiore rispetto l'esterno";
			if(fattoDedotto.contains("humidier_outside_high")) 		azione = CASA+"umidità esterna molto superiore rispetto l'esterno";
		*/
			if(fattoDedotto.contains("int_humidity_accettable_no_low")) 	azione = CASA+"umidità interna non accettabile: bassa";
			if(fattoDedotto.contains("int_humidity_accettable_yes")) 		azione = CASA+"umidità interna accettabile: media";
			if(fattoDedotto.contains("int_humidity_accettable_no_high")) 	azione = CASA+"umidità interna non accettabile: alta";
		/*	
			if(fattoDedotto.contains("warmer_inside_low")) 		azione = CASA+"temp. interna poco superiore rispetto l'esterno";
			if(fattoDedotto.contains("warmer_inside_middle")) 	azione = CASA+"temp. interna abbastanza superiore rispetto l'esterno";
			if(fattoDedotto.contains("warmer_inside_high")) 	azione = CASA+"temp. interna molto superiore rispetto l'esterno";
			if(fattoDedotto.contains("warmer_equal")) 			azione = CASA+"temp. interna ed esterna sono uguali";
			if(fattoDedotto.contains("warmer_outside_low")) 	azione = CASA+"temp. interna poco inferiore rispetto l'esterno";
			if(fattoDedotto.contains("warmer_outside_middle")) 	azione = CASA+"temp. interna abbastanza inferiore rispetto l'esterno";
			if(fattoDedotto.contains("warmer_outside_high")) 	azione = CASA+"temp. interna molto inferiore rispetto l'esterno";
		*/	
			if(fattoDedotto.contains("int_temperature_accettable_no_cold")) 	azione = CASA+"temp. interna non accettabile: fredda";
			if(fattoDedotto.contains("int_temperature_accettable_yes")) 		azione = CASA+"temp. interna accettabile: ottimale";
			if(fattoDedotto.contains("int_temperature_accettable_no_hod")) 		azione = CASA+"temp. interna non accettabile: calda";
			
			if(fattoDedotto.contains("is_cooking")) 	azione = CASA+"stai cucinando";
			if(fattoDedotto.contains("is_breakfast")) 	azione = CASA+"stai facendo colazione";
			if(fattoDedotto.contains("is_breaking")) 	azione = CASA+"stai facendo pausa";
						
			return (new Report(azione, item, stato, fattoDedotto, 1, 1, 1, 0));
		}
		else
			return null;
		
	}

	@SuppressWarnings("unused")
	private static boolean checkDeduzioneRidondante(SQLiteDatabase db, String fatto, ArrayList<Report> listaFattiDedottiSalvati) {
		if (listaFattiDedottiSalvati == null || listaFattiDedottiSalvati.isEmpty())
			return false;
		
		if (fatto.contains("season") || fatto.contains("time") || fatto.contains("humidier") || fatto.contains("accettable") || fatto.contains("warmer")) {
			String parti[] = fatto.split(",");
			for (Report r : listaFattiDedottiSalvati) {
				if (r.getProlog().contains(parti[1]))
					return true;
			}
			return false;
		}
		else
			return false;
			
	}
	
	public static String formatFatto(String fatto) {
		String fattoIniziale = fatto;
		String parziale = fattoIniziale.replace("fact(", "");
		String parti[] = parziale.split(",");
		
		String fattoFinale = "fact(_id";
		for (int i=1; i < parti.length; i++)
			fattoFinale += ","+parti[i];
		
		return fattoFinale;
	}
}