package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface SensoriTable extends BaseColumns
	{
		String TABLE_NAME = "sensori";
	 
		String NOME = "nome";
		String TIPO = "tipo";
		String STATO = "stato";
		
		String[] COLUMNS = new String[] {
				_ID,
				NOME,
				TIPO,
				STATO
		};
	
}
