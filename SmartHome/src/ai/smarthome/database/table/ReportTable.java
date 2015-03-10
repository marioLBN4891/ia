package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface ReportTable extends BaseColumns
{
	String TABLE_NAME = "report";
 
	String AZIONE = "azione";
	String ITEM = "item";
	String STATO = "stato";
	String PROLOG = "prolog";
	String SENTRECEIVED= "sentreceived";
	String NUOVO = "nuovo";
	
	String[] COLUMNS = new String[] {
			_ID,
			AZIONE,
			ITEM,
			STATO,
			PROLOG,
			SENTRECEIVED,
			NUOVO
	};

}