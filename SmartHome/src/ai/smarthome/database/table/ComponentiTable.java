package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface ComponentiTable extends BaseColumns
	{
		String TABLE_NAME = "componenti";
	 
		String NOME = "nome";
		
		String[] COLUMNS = new String[] {
				_ID,
				NOME
		};
	
}
