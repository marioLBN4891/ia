package ai.smarthome.util.exception;

public class PasswordReminderException extends Exception {
 
	private static final long serialVersionUID = 1L;

	public PasswordReminderException() {
	}

	public PasswordReminderException(String messaggio) {
		super(messaggio);
	}
	
}