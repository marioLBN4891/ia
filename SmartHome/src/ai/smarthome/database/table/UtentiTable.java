package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface UtentiTable extends BaseColumns
	{
		String TABLE_NAME = "utenti";
	 
		String MAIL = "mail";
		String PASSWORD = "password";
		String COGNOME = "cognome";
		String NOME = "nome";
			 
		String[] COLUMNS = new String[] {
				_ID, 
				MAIL, 
				PASSWORD,
				COGNOME,
				NOME
		};
	
}
