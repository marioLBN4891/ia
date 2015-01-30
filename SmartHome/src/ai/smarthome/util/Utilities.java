package ai.smarthome.util;

import java.util.regex.Pattern;

import ai.smarthome.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;

public class Utilities {
	
	public static void chiudiApplicazione(final Context context) {
		new AlertDialog.Builder(context).setIcon(R.drawable.exit).setTitle("Esci")
        .setMessage("Chiudere l'applicazione?")
        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	((Activity)context).finish();
            }
        }).setNegativeButton("No", null).show();
	}

	
	public static void getLogoutMenuOnActivity(final Context context, Menu menu) {
		((Activity)context).getMenuInflater().inflate(R.menu.logout, menu);
	}
	
	public static boolean emailValida(String email) {
		final Pattern EMAIL = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
		Pattern pattern = EMAIL;
	    return pattern.matcher(email).matches();
	}
	
	
	
	public static boolean checkSpace(String stringa) {
		return (!stringa.equals(stringa.trim().replaceAll(" ", ""))); 
	}
	
	
}
