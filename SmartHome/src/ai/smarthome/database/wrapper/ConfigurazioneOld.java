package ai.smarthome.database.wrapper;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConfigurazioneOld implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int posizioneFragment;
	private Utente utente;
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
	
	public ConfigurazioneOld () {
		
		this.posizioneFragment = 0;
		utente = null;
    	sensoriDisponibili = new HashMap<String, Boolean>();
		for(int i=0; i < sensori.length; i++) 
			this.sensoriDisponibili.put(sensori[i], true);
		
		componentiDisponibili = new HashMap<String, Boolean>();
		for(int i=0; i < componenti.length; i++) 
			this.componentiDisponibili.put(componenti[i], false);
	}
	
	// metodi SETTER/GETTER utente
	
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
	
	public String getComponentoToString(int idComponente) {
		boolean stato = componentiDisponibili.get(componenti[idComponente]);
		return componenti[idComponente]+": "+ getStato(stato);
	}
	
	public String getSensoreToString(int idSensore) {
		boolean stato = sensoriDisponibili.get(sensori[idSensore]);
		return sensori[idSensore]+": "+ getStato(stato);
	}
	
	// metodi PRIVATE 
	
	private static String getStato(boolean stato) {
		if (stato)
		   return "ON";
		else
		   return "OFF";
	}
	
	
	public ArrayList<String> toPrologRules(String tempo) {
		ArrayList<String> listaRegole = new ArrayList<String>();
		
		listaRegole.add(regolaPrologData(tempo));
		
		for(int i=0; i < componenti.length; i++) 
			listaRegole.add(regolaPrologComponenti(tempo, componenti[i], this.componentiDisponibili.get(componenti[i])));
		
		
		return listaRegole;
	}
	
	
	@SuppressWarnings("deprecation")
	private String regolaPrologData(String tempo) {
		
		String regola = "";
		Date data = new Date();
  //  	data.setTime(this.dataMilliTime);
    	
    	Calendar dataAttuale = Calendar.getInstance();
    	dataAttuale.set(data.getYear(), data.getMonth(), data.getDate());
    	
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
    	
    	if (dataAttuale.after(inizioanno) && dataAttuale.before(primavera)) 
    		regola = "inverno("+tempo+")";
    		
    	if (dataAttuale.after(primavera) && dataAttuale.before(estate)) 
        	regola = "primavera("+tempo+")";
        
        if (dataAttuale.after(estate) && dataAttuale.before(autunno)) 
        	regola = "estate("+tempo+")";
        
        if (dataAttuale.after(autunno) && dataAttuale.before(inverno)) 
           	regola = "autunno("+tempo+")";
        
        if (dataAttuale.after(inverno) && dataAttuale.before(fineanno)) 
           	regola = "inverno("+tempo+")";   	
            
       return regola;
	}
	
	
	
	private String regolaPrologComponenti(String tempo, String componente, boolean stato) {
		
		String regola = "";
		if (stato) {
			if (componente.equals("Televisione")) 			regola = "tvAccesa("+tempo+")";
			if (componente.equals("Radio")) 				regola =  "radioAccesa("+tempo+")";
			if (componente.equals("Condizionatore")) 		regola =  "condizionatoreAcceso("+tempo+")";
			if (componente.equals("Balcone"))				regola =  "balconeApertoCompleto("+tempo+")";
			if (componente.equals("Macchina del Caffè"))	regola =  "macchinaCaffeAccesa("+tempo+")";
			if (componente.equals("Illuminazione"))			regola =  "illuminazioneAccesa("+tempo+")";
		}
		else {
			if (componente.equals("Televisione")) 			regola = "tvSpenta("+tempo+")";
			if (componente.equals("Radio"))					regola = "radioSpenta("+tempo+")";
			if (componente.equals("Condizionatore")) 		regola = "condizionatoreSpento("+tempo+")"; 
			if (componente.equals("Balcone")) 				regola = "balconeChiuso("+tempo+")";
			if (componente.equals("Illuminazione")) 		regola = "illuminazioneSpenta("+tempo+")";
		}
		
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
		if (valore > 65 && valore <= 80) 
			return "meteoSereno("+tempo+")";
		if (valore > 80) 
			return "meteoSole("+tempo+")";
		return null;
		
    }
}