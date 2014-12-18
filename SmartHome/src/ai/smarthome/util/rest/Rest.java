package ai.smarthome.util.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import ai.smarthome.util.LogView;
import android.content.Context;


public class Rest  {
	final private static String serverAddress = "http://192.168.141.1:8080/WebServerProlog/prolog";
	final static String GETPARAMETRIMAIL = "getParametriMail()";
	final static String GETMETEOLOCALE= "getMeteoLocale()";
	
	
	public static void apri() {
		
		try {
			URL url = new URL(serverAddress+"/apri");
		
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
					LogView.info("Rest.apri: ERRORE");
			}
			
			LogView.info("Rest.apri: OK");
			return ;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.apri: ERRORE");
			return ;
		} 
		
	}
	
	public static void chiudi() {
		
		try {
			URL url = new URL(serverAddress+"/chiudi");
		
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
					LogView.info("Rest.chiudi: ERRORE");
			}
			
			LogView.info("Rest.chiudi: OK");
			return ;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.chiudi: ERRORE");
			return ;
		} 
		
	}
	
	public static void accendi() {
		
		try {
			URL url = new URL(serverAddress+"/accendi");
		
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
					LogView.info("Rest.accendi: ERRORE");
			}
			
			LogView.info("Rest.accendi: OK");
			return ;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.accendi: ERRORE");
			return ;
		} 
		
	}
	
	public static void spegni() {
		
		try {
			URL url = new URL(serverAddress+"/chiudi");
		
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
					LogView.info("Rest.spegni: ERRORE");
			}
			
			LogView.info("Rest.spegni: OK");
			return ;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.spegni: ERRORE");
			return ;
		} 
		
	}
	
	public static void prendi() {
		
		try {
			URL url = new URL(serverAddress+"/prendi");
		
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
					LogView.info("Rest.prendi: ERRORE");
			}
			
			LogView.info("Rest.prendi: OK");
			return ;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.prendi: ERRORE");
			return ;
		} 
		
	}

	public static void lascia() {
		
		try {
			URL url = new URL(serverAddress+"/lascia");
		
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
					LogView.info("Rest.lascia: ERRORE");
			}
			
			LogView.info("Rest.lascia: OK");
			return ;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.lascia: ERRORE");
			return ;
		} 
		
	}

	public static void consenti() {
		
		try {
			URL url = new URL(serverAddress+"/consenti");
		
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
					LogView.info("Rest.consenti: ERRORE");
			}
			
			LogView.info("Rest.consenti: OK");
			return ;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.consenti: ERRORE");
			return ;
		} 
		
	}

	public static void rifiuta() {
		
		try {
			URL url = new URL(serverAddress+"/rifiuta");
		
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
					LogView.info("Rest.rifiuta: ERRORE");
			}
			
			LogView.info("Rest.rifiuta: OK");
			return ;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.rifiuta: ERRORE");
			return ;
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
			
			int temperaturaC = ((datiMeteo.getInt("temperature")-32)*5)/9;
			if (temperaturaC <= 10 ) temperaturaC = 10;
			if (temperaturaC >= 35 ) temperaturaC = 35;
			temperaturaC = (temperaturaC - 10) * 4;
			
			parametri.put("tempEst", temperaturaC);
			
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
			
			parametri.put("umidita", datiMeteo.getInt("humidity"));
			
			int vento = 2 * datiMeteo.getInt("wind");
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
