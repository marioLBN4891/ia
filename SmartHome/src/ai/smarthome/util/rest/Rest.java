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
import android.database.sqlite.SQLiteDatabase;


public class Rest  {
	final private static String projectPath = "/ReLay/she";
	final static String GETPARAMETRIMAIL = "getParametriMail()";
	final static String GETMETEOLOCALE= "getMeteoLocale()";
	final static int timeOut = 1000 * 5;
	
	public static void sendPosition(String serverAddress, String user, String latitude, String longitude) {
		try {
			URL url = new URL(serverAddress+projectPath+"/sendPosition?user="+user+"+latitude="+latitude+"+longitude="+longitude);
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(timeOut);
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
	
	public static boolean startServer(String serverAddress) {
		
		try {
			URL url = new URL(serverAddress+projectPath+"/start");
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(timeOut);
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
					LogView.info("Rest.startServer: ERRORE");
					return false;
				}
			}
			boolean stato = Boolean.valueOf(esito);
			LogView.info("Rest.startServer: "+stato);
			return stato;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.startServer: ERRORE");
			return false;
		} 
		
	}

	public static boolean startSimulazione(String serverAddress) {
		
		try {
			URL url = new URL(serverAddress+projectPath+"/startSimulazione");
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(timeOut);
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
	
	public static boolean stopSimulazione(String serverAddress) {
		
		try {
			URL url = new URL(serverAddress+projectPath+"/stopSimulazione");
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(timeOut);
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

	public static boolean nuovoRun(String serverAddress) {
		
		try {
			URL url = new URL(serverAddress+projectPath+"/nuovorun");
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(timeOut);
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
					LogView.info("Rest.nuovoRun: ERRORE");
					return false;
				}
			}
			boolean stato = Boolean.valueOf(esito);
			LogView.info("Rest.nuovoRun: "+stato);
			return stato;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.nuovoRun: ERRORE");
			return false;
		} 
		
	}

	public static boolean sendFactToServer(String serverAddress, String fatto) {
		
		try {
			URL url = new URL(serverAddress+projectPath+"/sendFact?fatto="+fatto);
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(timeOut);
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

	public static boolean sendRetractToServer(String serverAddress, String retract) {
		
		try {
			URL url = new URL(serverAddress+projectPath+"/sendRetract?retract="+retract);
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(timeOut);
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

	public static HashMap<String, Serializable> inferisci(SQLiteDatabase db, String serverAddress) {
		
		HashMap<String, Serializable> risultati = new HashMap<String, Serializable>();
		ArrayList<Report> listaFattiDedotti = new ArrayList<Report>();
		
		risultati.put("esito", false);
		risultati.put("listaFattiDedotti", listaFattiDedotti);
		
		
		try {
			URL url = new URL(serverAddress+projectPath+"/inferisci");
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(timeOut);
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
				listaFattiDedotti.add(Prolog.fattoDedottoToReport(db, jsonFatto.getString("fatto")));
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

	// metodo non pi� utilizzato
	public static HashMap<String, Serializable> request(String serverAddress, String fatto) {
		
		HashMap<String, Serializable> risultati = new HashMap<String, Serializable>();
		ArrayList<String> listaFatti = new ArrayList<String>();
		
		risultati.put("esito", false);
		risultati.put("listaFatti", listaFatti);
		
		
		try {
			URL url = new URL(serverAddress+projectPath+"/request?fatto="+fatto);
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(timeOut);
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

	public static Map<String,String> getParametriMail(String serverAddress) {
		
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
			URL url = new URL(serverAddress+projectPath+"/riceviParametriMail");
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(timeOut);
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
	
	public static Map<String,Integer> getMeteoLocale(Context context, String city) {
		
		Map<String,Integer> parametri = null;
		try {
			if (city == null)
				city = "Roma";
			
			URL url = new URL("http://5DayWeather.org/api.php?city="+city);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(timeOut*10);
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
			
			parametri.put("umiditaEst", datiMeteo.getInt("humidity")/10);
			parametri.put("umiditaInt", datiMeteo.getInt("humidity")/10);
			int vento = datiMeteo.getInt("wind");
			parametri.put("vento", vento);
			parametri.put("luminosita", meteoClima/10);
			LogView.info("Rest.getMeteoLocale: OK");
			return parametri;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.getMeteoLocale: ERRORE");
			return null;
		} 
		
	}
	
public static Map<String,Integer> getMeteoLocale2(Context context, String city) {
		
		Map<String,Integer> parametri = null;
		try {
			if (city == null)
				city = "Roma";
			
			URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(timeOut*10);
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
			JSONArray datiWeather = json.getJSONArray("weather");
			JSONObject weather = datiWeather.getJSONObject(0);
			String mainWeather = weather.getString("main");
			
			JSONObject datiMain = json.getJSONObject("main");
			JSONObject datiWind = json.getJSONObject("wind");
			
			long temp = datiMain.getLong("temp");
			long humidity = datiMain.getLong("humidity");
			long speed = datiWind.getLong("speed");
			
			int umiditaEst = 0, umiditaInt = 0, vento = 0, tempEst = 0, tempInt = 0, visibilita = 0, meteoClima = 0, luminosita = 0;
			
		
			if(mainWeather.equals("Clear")) meteoClima = 70;
			if(mainWeather.equals("Clouds")) meteoClima = 40;
			if(mainWeather.equals("Rain")) meteoClima = 20;
			
			umiditaInt = (int) humidity;
			if(umiditaInt > 50) 
				umiditaEst = umiditaInt-15;
			else
				umiditaEst = umiditaInt+20;
			
			
			vento = (int) speed;
			
			parametri.put("tempEst", tempEst);
			parametri.put("tempInt", tempInt);
			parametri.put("meteo", meteoClima);
			parametri.put("visibilita", visibilita);
			parametri.put("umiditaEst", umiditaEst);
			parametri.put("umiditaInt", umiditaInt);
			parametri.put("vento", vento);
			parametri.put("luminosita", luminosita);
			LogView.info("Rest.getMeteoLocale: OK");
			return parametri;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogView.info("Rest.getMeteoLocale: ERRORE");
			return null;
		} 
		
	}
}
