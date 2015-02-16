package ai.smarthome.util;

import java.util.ArrayList;
import java.util.Date;

import ai.smarthome.database.wrapper.Componente;
import ai.smarthome.database.wrapper.Configurazione;
import ai.smarthome.util.rest.Rest;
import android.database.sqlite.SQLiteDatabase;

public class Prolog {

	@SuppressWarnings("deprecation")
	public static boolean startSimulazione(SQLiteDatabase db) {
		
		if (!Rest.assertPrologV2("utente(t0)"))
			return false;
		
		ArrayList<Componente> listaComp = Componente.getAllLista(db);
		for (Componente comp : listaComp) {
			String componente = comp.getNome().toLowerCase();
			if (comp.getTipo().equals(Componente.ACCESO_SPENTO) && comp.getStato() == 0)
				if (!Rest.assertPrologV2(componente+"Off(t0)"))
					return false;
			if (comp.getTipo().equals(Componente.ACCESO_SPENTO) && comp.getStato() == 1)
				if (!Rest.assertPrologV2(componente+"On(t0)"))
					return false;
			if (comp.getTipo().equals(Componente.APERTO_CHIUSO) && comp.getStato() == 0)
				if (!Rest.assertPrologV2(componente+"Close(t0)"))
					return false;
			if (comp.getTipo().equals(Componente.APERTO_CHIUSO) && comp.getStato() == 1)
				if (!Rest.assertPrologV2(componente+"Open(t0)"))
					return false;
		}
		
		Configurazione meteo = Configurazione.getConfigurazione(db);
		
		if (!Rest.assertPrologV2("ora(t0,"+meteo.getOra()+")"))
			return false;
		
		Date data = new Date();
    	data.setTime(meteo.getData());
		
    	if (!Rest.assertPrologV2("data(t0,"+data.getMonth()+","+data.getDate()+")"))
			return false;
		
    	if (!Rest.assertPrologV2("temperatura(t0,"+meteo.getTemperatura()+")"))
			return false;
    	
    	if (!Rest.assertPrologV2("umidita(t0,"+meteo.getUmidita()+")"))
			return false;
    	
    	if (!Rest.assertPrologV2("vento(t0,"+meteo.getVento()+")"))
			return false;
		return true;
	}
}
