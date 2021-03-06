package ai.smarthome.util.xmlrpc;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import ai.smarthome.database.wrapper.Report;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.LogView;
import ai.smarthome.util.Prolog;
import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;

public class XMLRPC {

	final static String GETPARAMETRIMAIL = "getParametriMail()";
	final static String GETMETEOLOCALE= "getMeteoLocale()";
	final static int timeOut = 1000 * 5;
	
		
	public static boolean sendPosition(Utente user, String serverAddress, String latitude, String longitude) {
		try {
			String username = user.getUsername();
			String password = user.getPassword(); 
			
			URL url = new URL(serverAddress);
			
			ReasoningServiceClientOrganization client = new ReasoningServiceClientOrganization(url, username, password);
			
			boolean stato = client.sendPosition(username, latitude, longitude);
			LogView.info("XMLRPC.sendPosition: "+stato);		
			
			return stato;
		} catch (Exception e) {
			LogView.info("XMLRPC.sendPosition: ERRORE");
			return false;
		} 
	}
	
	public static boolean startServer(Utente user, String serverAddress) {
		try {
			String username = user.getUsername();
			String password = user.getPassword(); 
			
			URL url = new URL(serverAddress);
			
			ReasoningServiceClientOrganization client = new ReasoningServiceClientOrganization(url, username, password);
			
			boolean stato = client.startServer();
			LogView.info("XMLRPC.startServer: "+stato);		
			
			return stato;
		} catch (Exception e) {
			LogView.info("XMLRPC.startServer: ERRORE");
			return false;
		}  
	}

	public static boolean startSimulazione(Utente user, String serverAddress) {
		try {
			String username = user.getUsername();
			String password = user.getPassword(); 
			
			URL url = new URL(serverAddress);
			
			ReasoningServiceClientOrganization client = new ReasoningServiceClientOrganization(url, username, password);
			
			boolean stato = client.startSimulazione();
			LogView.info("XMLRPC.startSimulazione: "+stato);		
			
			return stato;
		} catch (Exception e) {
			LogView.info("XMLRPC.startSimulazione: ERRORE");
			e.printStackTrace();
			return false;
		} 
	}
	
	public static boolean retract(Utente user, String serverAddress, ArrayList<String> listaRetract) {
		try {
			String username = user.getUsername();
			String password = user.getPassword(); 
			
			URL url = new URL(serverAddress);
			
			ReasoningServiceClientOrganization client = new ReasoningServiceClientOrganization(url, username, password);
			
			Object[] lista = new Object[listaRetract.size()];
			int i = 0;
			for(String retract: listaRetract) {
				lista[i++] = retract;
			}
			
			boolean stato = client.retract(lista);
			LogView.info("XMLRPC.retract: "+stato);		
			
			return stato;
		} catch (Exception e) {
			LogView.info("XMLRPC.retract: ERRORE");
			return false;
		} 
	}

	@SuppressLint("DefaultLocale")
	public static HashMap<String, Serializable> inferisci(Utente user, SQLiteDatabase db, String serverAddress, ArrayList<String> listaProlog) {
		
		HashMap<String, Serializable> map = new HashMap<String, Serializable>();
		ArrayList<Report> listaDedotti = new ArrayList<Report>();
		
		map.put("esito", false);
		map.put("listaDedotti", listaDedotti);
		
		try {
			String username = user.getUsername();
			String password = user.getPassword(); 
			
			URL url = new URL(serverAddress);
			
			ReasoningServiceClientOrganization client = new ReasoningServiceClientOrganization(url, username, password);
			
			Object[] lista = new Object[listaProlog.size()+1];
			lista[0] = Prolog.setFatto("fact(_id,utente("+user.getNome().toLowerCase()+"),1.0).", 1);
			int indice = 1;
			for(String fatto: listaProlog) {
				lista[indice] = Prolog.setFatto(fatto, indice+1);
				indice++;
			}
			
			Object[] risultati = client.inferisci(lista);
			
			if(risultati != null) {
				if(risultati.length > 0) {
					for(int i=0; i < risultati.length; i++) {
						String fattoDedotto = (String) risultati[i];
						if (fattoDedotto.contains("_choice_"))
							listaDedotti.add(Prolog.fattoDedottoToReport(db,fattoDedotto));
					}
					map.put("esito", true);
					map.put("listaDedotti", listaDedotti);
					LogView.info("XMLRPC.inferisci: "+true);
				}
			}
			else 
				LogView.info("XMLRPC.inferisci: "+false);
			
			return map;
		} catch (Exception e) {
			LogView.info("XMLRPC.inferisci: ERRORE");
			e.printStackTrace();
			return map;
		}
	}

	public static Utente loginUtente(Utente user, String serverAddress) {
		try {
			String username = user.getUsername();
			String password = user.getPassword(); 
			
			URL url = new URL(serverAddress);
			
			ReasoningServiceClientOrganization client = new ReasoningServiceClientOrganization(url, username, password);
			
			Object[] lista = client.loginUtente(username, password);
			if(lista != null) {
				LogView.info("XMLRPC.loginUtente: true");
				return new Utente(username, password, (String) lista[0], (String) lista[1], (String) lista[2]);
			}
			else {
				LogView.info("XMLRPC.loginUtente: false");
				return null;
			}
					
			
			
		} catch (Exception e) {
			LogView.info("XMLRPC.loginUtente: ERRORE");
			e.printStackTrace();
			return null;
		} 
	}

}
