package ai.smarthome.database.wrapper;

import java.io.Serializable;

public class Configurazione implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private long dataMilliTime;
	private int hour;
	private int minute;
	

	public Configurazione () {
		
	}
	
	public void setDataMilliTime(long dataMilliTime) {
		this.dataMilliTime = dataMilliTime;
	}
	
	public long getDataMilliTime() {
		return this.dataMilliTime;
	}
	
	public void setHour(int hour) {
		this.hour = hour;
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
}