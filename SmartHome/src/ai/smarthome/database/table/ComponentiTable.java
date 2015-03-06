package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface ComponentiTable extends BaseColumns
	{
		String TABLE_NAME = "componenti";
	 
		String NOME = "nome";
		String TIPO = "tipo";
		String STATO = "stato";
		String PROLOG = "prolog";
		
		String[] COLUMNS = new String[] {
				_ID,
				NOME,
				TIPO,
				STATO,
				PROLOG
		};
	
}
