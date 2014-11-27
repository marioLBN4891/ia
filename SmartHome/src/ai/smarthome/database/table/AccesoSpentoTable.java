package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface AccesoSpentoTable extends BaseColumns
	{
		String TABLE_NAME = "accesoSpento";
		
		String ACCESO = "acceso";
		String ID_COMPONENTE = "idComponente";
		
		String[] COLUMNS = new String[] {
				_ID,
				ACCESO,
				ID_COMPONENTE
		};
	
}
