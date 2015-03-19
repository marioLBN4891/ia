package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface ImpostazioniTable extends BaseColumns
	{
		String TABLE_NAME = "impostazioni";
	 
		String INDIRIZZO = "INDIRIZZO";
		
		String[] COLUMNS = new String[] {
				_ID,
				INDIRIZZO
		};
	
}
