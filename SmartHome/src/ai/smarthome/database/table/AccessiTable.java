package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface AccessiTable extends BaseColumns
	{
		String TABLE_NAME = "accessi";
	 
		String USERNAME = "nome";
		String ACCESSO = "accesso";
		
	 
		String[] COLUMNS = new String[] {
				_ID, 
				USERNAME, 
				ACCESSO
		};
	
}
