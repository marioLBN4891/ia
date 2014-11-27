package ai.smarthome.util;

import ai.smarthome.database.wrapper.Oggetto;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LogView {

	public static void printLogOggetti(SQLiteDatabase db) {
		Cursor cursore = Oggetto.getListaOggetti(db);	
		try {
    		while (cursore.moveToNext())
    			Log.i("SHE_Oggetti", cursore.getLong(0) + " " + cursore.getString(1) + "\t " + cursore.getInt(2));
    	}  
    	finally {
    		cursore.close();
    	}
	}
	
}
