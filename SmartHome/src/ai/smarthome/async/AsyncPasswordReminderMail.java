package ai.smarthome.async;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import ai.smarthome.R;
import ai.smarthome.activity.PasswordActivity;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.PasswordReminderMail;
import ai.smarthome.util.exception.PasswordReminderException;
import ai.smarthome.util.rest.Rest;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.MessageQueue.IdleHandler;
import android.widget.Toast;

public class AsyncPasswordReminderMail extends AsyncTask<Void, Void, Void> {

	private final PasswordActivity passwordActivity;
	private final Utente utente;
	 
	public AsyncPasswordReminderMail(PasswordActivity passwordActivity) {
		this.passwordActivity = passwordActivity;
		this.utente = passwordActivity.getUtente();
	}
	
		
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		passwordActivity.findViewById(R.id.sendMailButton).setClickable(false);
	}


	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		passwordActivity.findViewById(R.id.sendMailButton).setClickable(true);
	}


	@Override
	protected Void doInBackground(Void... params) {
		Looper.prepare();	// Prepare looper
        MessageQueue queue = Looper.myQueue();	                 // Register Queue listener hook
        queue.addIdleHandler(new IdleHandler() {
        	int mReqCount = 0;

            @Override
            public boolean queueIdle() {
            	if (++mReqCount == 2) {
            		Looper.myLooper().quit();     // Quit looper
                    return false;
                } else
                	return true;
                }
        });
        Map<String, String> parametri = Rest.getParametriMail();
        try {
			PasswordReminderMail.sendPasswordReminderMail(utente.getMail(), utente.getPassword(), parametri);
			Toast.makeText(passwordActivity.getApplicationContext(), "Mail inviata con successo", Toast.LENGTH_SHORT).show();
		} catch (AddressException e) {
			Toast.makeText(passwordActivity.getApplicationContext(), "Impossibile inviare mail", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (MessagingException e) {
			Toast.makeText(passwordActivity.getApplicationContext(), "Impossibile inviare mail", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (PasswordReminderException e) {
			Toast.makeText(passwordActivity.getApplicationContext(), "Impossibile inviare mail", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
        Looper.loop();  // Start looping Message Queue- Blocking call
        
        return null;
     }
	
}
