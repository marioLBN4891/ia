package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface ApertoChiusoTable extends BaseColumns
	{
		String TABLE_NAME = "apertoChiuso";
		
		String APERTO = "aperto";
		String ID_COMPONENTE = "idComponente";
		
		String[] COLUMNS = new String[] {
				_ID,
				APERTO,
				ID_COMPONENTE
		};
	
}
