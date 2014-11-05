package ai.smarthome.util.exception;

public class GeolocalizationException extends Exception {
 
	private static final long serialVersionUID = 1L;

	public GeolocalizationException() {
	}

	public GeolocalizationException(String messaggio) {
		super(messaggio);
	}
	
}