package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface OggettiTable extends BaseColumns
	{
		String TABLE_NAME = "oggetti";
	 
		String NOME = "nome";
		String CLASSE = "classe";
		String STATO = "stato";
		String PROLOG = "prolog";
		
		String[] COLUMNS = new String[] {
				_ID,
				NOME,
				CLASSE,
				STATO,
				PROLOG
		};
	
}
