package ai.smarthome.database.wrapper;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Configurazione implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int posizioneFragment;
	private Utente utente;
	private long dataMilliTime;
	private int hour;
	private int minute;
	private Map<String, Boolean> sensoriDisponibili;
	private Map<String, Boolean> componentiDisponibili;
	private Map<String, Integer> climaDisponibili;
	
	private static String[] sensori = new String[] {
			"Temperatura", 
			"Umidità", 
			"Vento",
			"Presenza",
			"Sonoro"
	};
	
	private static String[] componenti = new String[] {
		"Televisione", 
		"Radio", 
		"Condizionatore",
		"Balcone",
		"Macchina del Caffè",
		"Illuminazione"
	};
	
	private static String[] clima = new String[] {
		"Temperatura esterna", 
		"Temperatura interna",
		"Umidità",
		"Vento",
		"Visibilità",
		"Meteo"
	};
	
	public Configurazione () {
		
		this.posizioneFragment = 0;
		
		Date data = new Date();
    	this.dataMilliTime = data.getTime();
		
		utente = null;
    	
		sensoriDisponibili = new HashMap<String, Boolean>();
		for(int i=0; i < sensori.length; i++) 
			this.sensoriDisponibili.put(sensori[i], true);
		
		componentiDisponibili = new HashMap<String, Boolean>();
		for(int i=0; i < componenti.length; i++) 
			this.componentiDisponibili.put(componenti[i], false);
		
		climaDisponibili = new HashMap<String, Integer>();
		for(int i=0; i < clima.length; i++) 
			this.climaDisponibili.put(clima[i], 50);
		
		
		final Calendar c = Calendar.getInstance();
    	this.hour = c.get(Calendar.HOUR_OF_DAY);
    	this.minute = c.get(Calendar.MINUTE);
		
		
	}
	
	// metodi SETTER/GETTER utnte
	
	public void setUtente(Utente user) {
		this.utente = user;
	}
		
	public Utente getUtente() {
		return this.utente;
	}
	
	// metodi SETTER/GETTER posizione fragment
	
	public void setPosizione(int posizione) {
		this.posizioneFragment = posizione;
	}
	
	public int getPosizione() {
		return this.posizioneFragment;
	}
	
	// metodi SETTER/GETTER data
	
	public void setDataMilliTime(long dataMilliTime) {
		this.dataMilliTime = dataMilliTime;
	}
	
	public long getDataMilliTime() {
		return this.dataMilliTime;
	}
	
	
	
	
	// metodi SETTER ora 2
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	
	// metodi SETTER sensori 5 
	
	public void setSensoreTemperatura(boolean stato) {
		this.sensoriDisponibili.put(sensori[0], stato);
	}
	
	public void setSensoreUmidita(boolean stato) {
		this.sensoriDisponibili.put(sensori[1], stato);
	}
	
	public void setSensoreVento(boolean stato) {
		this.sensoriDisponibili.put(sensori[2], stato);
	}
	
	public void setSensorePresenza(boolean stato) {
		this.sensoriDisponibili.put(sensori[3], stato);
	}
	
	public void setSensoreSonoro(boolean stato) {
		this.sensoriDisponibili.put(sensori[4], stato);
	}
	
	
	
	// metodi SETTER componenti 5
	
	public void setComponenteTelevisione(boolean stato) {
		this.componentiDisponibili.put(componenti[0], stato);
	}
	
	public void setComponenteRadio(boolean stato) {
		this.componentiDisponibili.put(componenti[1], stato);
	}
	
	public void setComponenteCondizionatore(boolean stato) {
		this.componentiDisponibili.put(componenti[2], stato);
	}
	
	public void setComponenteBalcone(boolean stato) {
		this.componentiDisponibili.put(componenti[3], stato);
	}
	
	public void setComponenteMacchinaCaffe(boolean stato) {
		this.componentiDisponibili.put(componenti[4], stato);
	}
	
	public void setComponenteIlluminazione(boolean stato) {
		this.componentiDisponibili.put(componenti[5], stato);
	}
	
	// metodi SETTER clima 6
	
	public void setClimaTemperaturaEsterna(int stato) {
		this.climaDisponibili.put(clima[0], stato);
	}
	
	public void setClimaTemperaturaInterna(int stato) {
		this.climaDisponibili.put(clima[1], stato);
	}
	
	public void setClimaUmidita(int stato) {
		this.climaDisponibili.put(clima[2], stato);
	}
	
	public void setClimaVento(int stato) {
		this.climaDisponibili.put(clima[3], stato);
	}
	
	public void setClimaVisibilita(int stato) {
		this.climaDisponibili.put(clima[4], stato);
	}
	
	public void setClimaMeteo(int stato) {
		this.climaDisponibili.put(clima[5], stato);
	}
	
	
	// metodi GETTER ora 2
	
	public int getHour() {
		return this.hour;
	}
	
	public int getMinute() {
		return this.minute;
	}
	
	
	
	// metodi GETTER sensori 5
	
	public boolean getSensoreTemperatura() {
		return this.sensoriDisponibili.get(sensori[0]);
	}
	
	public boolean getSensoreUmidita() {
		return this.sensoriDisponibili.get(sensori[1]);
	}
	
	public boolean getSensoreVento() {
		return this.sensoriDisponibili.get(sensori[2]);
	}
	
	public boolean getSensorePresenza() {
		return this.sensoriDisponibili.get(sensori[3]);
	}
	
	public boolean getSensoreSonoro() {
		return this.sensoriDisponibili.get(sensori[4]);
	}
	
	
	
	// metodi GETTER componenti 5
	
	public boolean getComponenteTelevisione() {
		return this.componentiDisponibili.get(componenti[0]);
	}
	
	public boolean getComponenteRadio() {
		return this.componentiDisponibili.get(componenti[1]);
	}
	
	public boolean getComponenteCondizionatore() {
		return this.componentiDisponibili.get(componenti[2]);
	}
	
	public boolean getComponenteBalcone() {
		return this.componentiDisponibili.get(componenti[3]);
	}
	
	public boolean getComponenteMacchinaCaffe() {
		return this.componentiDisponibili.get(componenti[4]);
	}	
	
	public boolean getComponenteIlluminazione() {
		return this.componentiDisponibili.get(componenti[5]);
	}
	
	// metodi GETTER clima 6
	
	public int getClimaTemperaturaEsterna() {
		return this.climaDisponibili.get(clima[0]);
	}
	
	public int getClimaTemperaturaInterna() {
		return this.climaDisponibili.get(clima[1]);
	}
	
	public int getClimaUmidita() {
		return this.climaDisponibili.get(clima[2]);
	}
	
	public int getClimaVento() {
		return this.climaDisponibili.get(clima[3]);
	}
	
	public int getClimaVisibilita() {
		return this.climaDisponibili.get(clima[4]);
	}
	
	public int getClimaMeteo() {
		return this.climaDisponibili.get(clima[5]);
	}
	
	
	// metodi TO_STRING 
	
	public String getOraToString() {
		return (pad(this.hour)+":"+pad(this.minute));
	}
	
	@SuppressLint("SimpleDateFormat")
	public String getDataToString() {
		Date data = new Date();
    	data.setTime(this.dataMilliTime);
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
    	return sdf.format(data);
	} 
	
	public String getComponentoToString(int idComponente) {
		boolean stato = componentiDisponibili.get(componenti[idComponente]);
		return componenti[idComponente]+": "+ getStato(stato);
	}
	
	public String getSensoreToString(int idSensore) {
		boolean stato = sensoriDisponibili.get(sensori[idSensore]);
		return sensori[idSensore]+": "+ getStato(stato);
	}
	
	// metodi PRIVATE 
	
	private static String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
	
	private static String getStato(boolean stato) {
		if (stato)
		   return "ON";
		else
		   return "OFF";
	}
	
	
	public ArrayList<String> toPrologRules(String tempo) {
		ArrayList<String> listaRegole = new ArrayList<String>();
		
		listaRegole.add(regolaPrologData(tempo));
		listaRegole.add(regolaPrologOra(tempo));
		
		for(int i=0; i < componenti.length; i++) 
			listaRegole.add(regolaPrologComponenti(tempo, componenti[i], this.componentiDisponibili.get(componenti[i])));
		
		for(int i=0; i < clima.length; i++) 
			listaRegole.add(regolaPrologClima(tempo, clima[i], this.climaDisponibili.get(clima[i])));
		
		return listaRegole;
	}
	
	
	@SuppressWarnings("deprecation")
	private String regolaPrologData(String tempo) {
		
		String regola = "";
		Date data = new Date();
    	data.setTime(this.dataMilliTime);
    	
    	Calendar inizioanno = Calendar.getInstance();
    	Calendar primavera = Calendar.getInstance();
    	Calendar estate = Calendar.getInstance();
    	Calendar autunno = Calendar.getInstance();
    	Calendar inverno = Calendar.getInstance();
    	Calendar fineanno = Calendar.getInstance();
    	inizioanno.set(data.getYear(), 1, 1);
    	primavera.set(data.getYear(), 3, 20);
    	estate.set(data.getYear(), 6, 21);
    	autunno.set(data.getYear(), 9, 23);
    	inverno.set(data.getYear(), 12, 21);
    	fineanno.set(data.getYear(), 12, 31);
    	
    	if (data.compareTo(inizioanno.getTime()) >= 0 && data.compareTo(primavera.getTime()) < 0) 
    		regola = "inverno("+tempo+")";
    		
    	if (data.compareTo(primavera.getTime()) >= 0 && data.compareTo(estate.getTime()) < 0) 
        	regola = "primavera("+tempo+")";
        
        if (data.compareTo(estate.getTime()) >= 0 && data.compareTo(autunno.getTime()) < 0) 
        	regola = "estate("+tempo+")";
        
        if (data.compareTo(autunno.getTime()) >= 0 && data.compareTo(inverno.getTime()) < 0) 
           	regola = "autunno("+tempo+")";
        
        if (data.compareTo(inverno.getTime()) >= 0 && data.compareTo(fineanno.getTime()) <= 0) 
           	regola = "inverno("+tempo+")";   	
            
       return regola;
	}
	
	
	private String regolaPrologOra(String tempo) {
		
		String regola = "ora("+tempo+", "+this.hour+")";
		return regola;
	}
	
	
	private String regolaPrologComponenti(String tempo, String componente, boolean stato) {
		
		String regola = "";
		if (componente.equals("Televisione") && stato)
			regola = "tvAccesa("+tempo+")";
		else
			regola = "tvSpenta("+tempo+")";
		
		if (componente.equals("Radio") && stato) 
			regola =  "radioAccesa("+tempo+")";
		else 
			regola = "radioSpenta("+tempo+")";
		
		if (componente.equals("Condizionatore") && stato)
			regola =  "condizionatoreAcceso("+tempo+")";
		else
			regola = "condizionatoreSpento("+tempo+")"; 
		
		if (componente.equals("Balcone") && stato) 
			regola =  "balconeApertoCompleto("+tempo+")";
		else
			regola = "balconeChiuso("+tempo+")";
		
		if (componente.equals("Macchina del Caffè") && stato) 
			regola =  "macchinaCaffeAccesa("+tempo+")";
		else
			regola = "macchinaCaffeAccesa("+tempo+")";
		
		return regola;
	}
	
	
	private String regolaPrologClima(String tempo, String clima, int valore) {
		
		String regola = "";
		if (clima.equals("Temperatura esterna")) 
			regola = "temperatura("+tempo+", "+getTemperatura(valore)+")";
		
		if (clima.equals("Umidità"))
			regola = "umidita("+tempo+", "+valore+")";
		
		if (clima.equals("Vento"))
			regola = "vento("+tempo+", "+valore+")";
		
		if (clima.equals("Visibilità"))
			regola = "visibilita("+tempo+", "+valore+")";
		
		if (clima.equals("Meteo"))
			regola = getMeteo(tempo, valore);
		
		return regola;
	}	
	
	
	private int getTemperatura(int valore) {
    	return (10 + (valore/4)) ;
	}
	
	
	private String getMeteo(String tempo, int valore) {
    	if (valore <= 35) 
    		return "meteoPioggia("+tempo+")";
		if (valore > 35 && valore <= 65) 
			return "meteoNuvole("+tempo+")";
		if (valore > 65) 
			return "meteoSole("+tempo+")";
		return null;
		
    }
}