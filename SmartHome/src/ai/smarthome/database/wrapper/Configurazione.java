package ai.smarthome.database.wrapper;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Configurazione implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int posizioneFragment;
	private long dataMilliTime;
	private int hour;
	private int minute;
	
	private static String[] sensori = new String[] {
			"TEMPERATURA", 
			"UMIDITA'", 
			"VENTO",
			"PRESENZA",
			"SONORO"
	};
	
	private Map<String, Boolean> sensoriDisponibili;

	public Configurazione () {
		
		this.posizioneFragment = 0;
		
		Date data = new Date();
    	this.dataMilliTime = data.getTime();
		
		
		sensoriDisponibili = new HashMap<String, Boolean>();
		for(int i=0; i < sensori.length; i++) 
			this.sensoriDisponibili.put(sensori[i], true);
		
		final Calendar c = Calendar.getInstance();
    	this.hour = c.get(Calendar.HOUR_OF_DAY);
    	this.minute = c.get(Calendar.MINUTE);
		
		
	}
	
	public void setPosizione(int posizione) {
		this.posizioneFragment = posizione;
	}
	
	public int getPosizione() {
		return this.posizioneFragment;
	}
	
	public void setDataMilliTime(long dataMilliTime) {
		this.dataMilliTime = dataMilliTime;
	}
	
	public long getDataMilliTime() {
		return this.dataMilliTime;
	}
	
	@SuppressLint("SimpleDateFormat")
	public String getDataToString() {
		Date data = new Date();
    	data.setTime(this.dataMilliTime);
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
    	return sdf.format(data);
	}
	
	public void setHour(int hour) {
		this.hour = hour;
	}
	
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
	
	
	public int getHour() {
		return this.hour;
	}
	
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	public int getMinute() {
		return this.minute;
	}
	
	public String getOraToString() {
		return (pad(this.hour)+":"+pad(this.minute));
	}
	
	
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
	
	
	public String getSensoriToString() {
		String listaSensori = "";
		for(int i=0; i < sensori.length; i++) 
			listaSensori += sensori[i]+": "+ verificaStatoSensore(this.sensoriDisponibili.get(sensori[i]))+"\n";
		return listaSensori;
	}
	
	private static String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
	
	private static String verificaStatoSensore(boolean stato) {
		if (stato)
		   return "ON";
		else
		   return "OFF";
	}
}