package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface ConfigurazioneTable extends BaseColumns
	{
		String TABLE_NAME = "configurazione";
	 
		String LOCALITA = "localita";
		String METEO = "meteo";
		String TEMPERATURAINT = "temperaturaInt";
		String TEMPERATURAEST = "temperaturaEst";
		String UMIDITAINT = "umiditaInt";
		String UMIDITAEST = "umiditaEst";
		String VENTO = "vento";
		String LUMINOSITA = "luminosita";
		String DATA = "data";
		String ORA = "ora";
		String MINUTI = "minuti";
		String COMPONENTI = "componenti";
				
		String[] COLUMNS = new String[] {
				_ID,
				LOCALITA,
				METEO,
				TEMPERATURAINT,
				TEMPERATURAEST,
				UMIDITAINT,
				UMIDITAEST,
				VENTO,
				LUMINOSITA,
				DATA,
				ORA,
				MINUTI,
				COMPONENTI
		};
	
}
