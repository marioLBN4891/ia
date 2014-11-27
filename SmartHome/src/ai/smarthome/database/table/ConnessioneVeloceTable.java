package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface ConnessioneVeloceTable extends BaseColumns
	{
		String TABLE_NAME = "connessioneVeloce";
	 
		String MAIL = "MAIL";
		String PASSWORD = "password";
		
		String[] COLUMNS = new String[] {
				_ID, 
				MAIL, 
				PASSWORD
		};
	
}
