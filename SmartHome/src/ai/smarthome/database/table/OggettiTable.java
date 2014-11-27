package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface OggettiTable extends BaseColumns
	{
		String TABLE_NAME = "oggetti";
	 
		String NOME = "nome";
		String PRESO = "preso";
		
		String[] COLUMNS = new String[] {
				_ID,
				NOME,
				PRESO
		};
	
}
