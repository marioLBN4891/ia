package ai.smarthome.util;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LogView {

	public static void printLogOggetti(SQLiteDatabase db) {
		
	}
	
	public static void info(String messaggio) {
		Log.i("SmartHome__", messaggio);
	}
}
