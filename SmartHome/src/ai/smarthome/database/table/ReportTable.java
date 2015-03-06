package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface ReportTable extends BaseColumns
{
	String TABLE_NAME = "report";
 
	String AZIONE = "azione";
	String PROLOG = "prolog";
	String ITEM = "item";
	String STATO = "stato";
	
	String[] COLUMNS = new String[] {
			_ID,
			AZIONE,
			PROLOG,
			ITEM,
			STATO
	};

}