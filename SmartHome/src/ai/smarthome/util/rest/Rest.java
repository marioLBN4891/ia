package ai.smarthome.util.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import ai.smarthome.database.wrapper.Report;
import ai.smarthome.util.LogView;
import ai.smarthome.util.Prolog;
import android.content.Context;


public class Rest  {
	final private static String serverAddress = "http://192.168.228.1:8080/ReLay/she";
	final static String GETPARAMETRIMAIL = "getParametriMail()";
	final static String GETMETEOLOCALE= "getMeteoLocale()";
	
	public static void sendPosition(String user, String latitude, String longitude) {
		try {
			URL url = new URL(serverAddress+"/sendPosition?user="+user+"+latitude="+latitude+"+longitude="+longitude);
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(1000 * 2);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			BufferedReader rd;
			conn.connect();
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String esito = null;
			String line = null;
			while ((line = rd.readLine()) != null) {
				JSONObject json = new JSONObject(line);
				esito = json.getString("esito");
				if(esito.contains("errore")) {
					LogView.info("Rest.sendPosition: ERRORE");
				}
			}
			boolean stato = Boolean.valueOf(esito);
			LogView.info("Rest.sendPosition: "+stato);
					
		} catch (Exception e) {
			LogView.info("Rest.sendPosition: ERRORE");
		} 
	}
	
	public static boolean startSimulazione() {
		
		try {
			URL url = new URL(serverAddress+"/startSimulazione");
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(1000 * 2);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			BufferedReader rd;
			conn.connect();
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String esito = null;
			String line = null;
			while ((line = rd.readLine()) != null) {
				JSONObject json = new JSONObject(line);
				esito = json.getString("esito");
				if(esito.contains("errore")) {
					LogView.info("Rest.startSimulazione: ERRORE");
					return false;
				}
			}
			boolean stato = Boolean.valueOf(esito);
			LogView.info("Rest.startSimulazione: "+stato);
			return stato;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.startSimulazione: ERRORE");
			return false;
		} 
		
	}
	
	public static boolean stopSimulazione() {
		
		try {
			URL url = new URL(serverAddress+"/stopSimulazione");
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(1000 * 2);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			BufferedReader rd;
			conn.connect();
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String esito = null;
			String line = null;
			while ((line = rd.readLine()) != null) {
				JSONObject json = new JSONObject(line);
				esito = json.getString("esito");
				if(esito.contains("errore")) {
					LogView.info("Rest.stopSimulazione: ERRORE");
					return false;
				}
			}
			boolean stato = Boolean.valueOf(esito);
			LogView.info("Rest.stopSimulazione: "+stato);
			return stato;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.stopSimulazione: ERRORE");
			return false;
		} 
		
	}

	public static boolean sendFactToServer(String fatto) {
		
		try {
			URL url = new URL(serverAddress+"/send?fatto="+fatto);
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(1000 * 2);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			BufferedReader rd;
			conn.connect();
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			JSONObject json = null;
			String line = null;
			while ((line = rd.readLine()) != null) {
				json = new JSONObject(line);
				if(line.contains("errore")) {
					LogView.info("Rest.send: ERRORE");
					return false;
				}
			}
			boolean stato = Boolean.valueOf(json.getBoolean("esito"));
			LogView.info("Rest.send: "+stato);
			return stato;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.send: ERRORE");
			return false;
		} 
		
	}

	public static HashMap<String, Serializable> inferisci() {
		
		HashMap<String, Serializable> risultati = new HashMap<String, Serializable>();
		ArrayList<Report> listaFattiDedotti = new ArrayList<Report>();
		
		risultati.put("esito", false);
		risultati.put("listaFattiDedotti", listaFattiDedotti);
		
		
		try {
			URL url = new URL(serverAddress+"/inferisci");
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(1000 * 5);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			BufferedReader rd;
			conn.connect();
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			JSONObject json = null;
			String line = null;
			while ((line = rd.readLine()) != null) {
				json = new JSONObject(line);
				if(line.contains("errore")) {
					LogView.info("Rest.inferisci: ERRORE");
					return risultati;
				}
			}
			JSONArray jsonArray = json.getJSONArray("listaFatti");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonFatto = jsonArray.getJSONObject(i);
				listaFattiDedotti.add(Prolog.fattoDedottoToReport(jsonFatto.getString("fatto")));
			}
			boolean stato = Boolean.valueOf(json.getBoolean("esito"));
			LogView.info("Rest.inferisci: "+stato);
			risultati.put("esito", stato);
			risultati.put("listaFattiDedotti", listaFattiDedotti);
			
			return risultati;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.inferisci: ERRORE");
			return risultati;
		} 
		
	}

	public static HashMap<String, Serializable> request(String fatto) {
		
		HashMap<String, Serializable> risultati = new HashMap<String, Serializable>();
		ArrayList<String> listaFatti = new ArrayList<String>();
		
		risultati.put("esito", false);
		risultati.put("listaFatti", listaFatti);
		
		
		try {
			URL url = new URL(serverAddress+"/request?fatto="+fatto);
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(1000 * 2);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			BufferedReader rd;
			conn.connect();
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			JSONObject json = null;
			String line = null;
			while ((line = rd.readLine()) != null) {
				json = new JSONObject(line);
				if(line.contains("errore")) {
					LogView.info("Rest.request: ERRORE");
					return risultati;
				}
			}
			JSONArray jsonArray = json.getJSONArray("listaFatti");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonFatto = jsonArray.getJSONObject(i);
				listaFatti.add(jsonFatto.getString("fatto"));
			}
			boolean stato = Boolean.valueOf(json.getBoolean("esito"));
			LogView.info("Rest.request: "+stato);
			risultati.put("esito", stato);
			risultati.put("listaFatti", listaFatti);
			
			return risultati;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.request: ERRORE");
			return risultati;
		} 
		
	}

	public static Map<String,String> getParametriMail() {
		
		final String EMAIL_FROM = "email_from";
		final String USERNAME = "username";
		final String PASSWORD = "password";
		final String SMTP = "smtp";
		final String PORTA_SMTP = "porta_smtp";
		final String PORTA_SF = "porta_sf";
		
		Map<String, String> parametri = new HashMap<String, String>();
		parametri.put(EMAIL_FROM, "she.casa.ia@gmail.com");
		parametri.put(USERNAME, "she.casa.ia@gmail.com");
		parametri.put(PASSWORD, "shecasaia");
		parametri.put(SMTP, "smtp.gmail.com");
		parametri.put(PORTA_SMTP, "465");
		parametri.put(PORTA_SF, "465");
		
		try {
			URL url = new URL(serverAddress+"/riceviParametriMail");
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(1000 * 2);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			BufferedReader rd;
			conn.connect();
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			JSONObject json = null;
			String line = null;
			while ((line = rd.readLine()) != null) {
				json = new JSONObject(line);
				if(line.contains("errore")) 
					LogView.info("Rest.getParametriMail: ERRORE");
			}
			
			parametri.put(EMAIL_FROM, json.getString(EMAIL_FROM));
			parametri.put(USERNAME, json.getString(USERNAME));
			parametri.put(PASSWORD, json.getString(PASSWORD));
			parametri.put(SMTP, json.getString(SMTP));
			parametri.put(PORTA_SMTP, json.getString(PORTA_SMTP));
			parametri.put(PORTA_SF, json.getString(PORTA_SF));
			LogView.info("Rest.getParametriMail: OK");
			return parametri;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.getParametriMail: ERRORE");
			return null;
		} 
		
	}
	
	public static Map<String,Integer> getMeteoLocale(Context context, final String city) {
		
		Map<String,Integer> parametri = null;
		try {
			URL url = new URL("http://5DayWeather.org/api.php?city="+city);
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(1000 * 5);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			BufferedReader rd;
			conn.connect();
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			JSONObject json = null;
			String line = null;
			while ((line = rd.readLine()) != null) {
				json = new JSONObject(line);
			}
			
			parametri = new HashMap<String,Integer>();
			
			JSONObject datiMeteo = json.getJSONObject("data");
			
			int tempInt = 0;
			int temperaturaC = ((datiMeteo.getInt("temperature")-32)*5)/9;
			tempInt = temperaturaC;
			if (temperaturaC <= -10 ) {
				temperaturaC = -10;
				tempInt = 2;
			}
			if (temperaturaC >= 40 ) {
				temperaturaC = 40;
				tempInt = 33;
			}
			temperaturaC = temperaturaC + 10;
			
			parametri.put("tempEst", temperaturaC);
			parametri.put("tempInt", tempInt);
			
			String meteo = datiMeteo.getString("skytext");
			int meteoClima = 0;
			if(meteo.equals("Clear")) meteoClima = 70;
			if(meteo.equals("Partly Cloudy")) meteoClima = 60;
			if(meteo.equals("Fair")) meteoClima = 90;
			if(meteo.equals("Mostly Cloudy")) meteoClima = 40;
			if(meteo.equals("T-storms")) 
				meteoClima = 20;
			
			parametri.put("meteo", meteoClima);
			parametri.put("visibilita", meteoClima);
			
			parametri.put("umiditaEst", datiMeteo.getInt("humidity"));
			parametri.put("umiditaInt", datiMeteo.getInt("humidity"));
			int vento = datiMeteo.getInt("wind");
			parametri.put("vento", vento);
			
			LogView.info("Rest.getMeteoLocale: OK");
			return parametri;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.getMeteoLocale: ERRORE");
			return null;
		} 
		
	}
	
	
}
