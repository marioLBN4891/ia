package ai.smarthome.util.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class Rest  {

	final static String GETPARAMETRIMAIL = "getParametriMail()";
	final static String GETMETEOLOCALE= "getMeteoLocale()";
	final static String OK = "esito OK";
	final static String KO = "esito KO";
	
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
			URL url = new URL("http://192.168.141.1:8080/WebServerProlog/prolog/riceviParametriMail");
		
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
					Log.i(GETPARAMETRIMAIL, KO);
			}
			
			parametri.put(EMAIL_FROM, json.getString(EMAIL_FROM));
			parametri.put(USERNAME, json.getString(USERNAME));
			parametri.put(PASSWORD, json.getString(PASSWORD));
			parametri.put(SMTP, json.getString(SMTP));
			parametri.put(PORTA_SMTP, json.getString(PORTA_SMTP));
			parametri.put(PORTA_SF, json.getString(PORTA_SF));
			Log.i(GETPARAMETRIMAIL, OK);
			return parametri;
					
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(GETPARAMETRIMAIL, KO);
			return parametri;
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
			
			Log.i(GETMETEOLOCALE, OK);
			return parametri;
					
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(GETMETEOLOCALE, KO);
			return parametri;
		} 
		
	}
}
