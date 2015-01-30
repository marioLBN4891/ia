package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface ReportTable extends BaseColumns
{
	String TABLE_NAME = "report";
 
	String AZIONE = "azione";
	String PROLOG = "prolog";
	
	String[] COLUMNS = new String[] {
			_ID,
			AZIONE,
			PROLOG
	};

}