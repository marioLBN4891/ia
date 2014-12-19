package ai.smarthome.util;

import java.util.ArrayList;

import ai.smarthome.database.wrapper.Componente;
import ai.smarthome.database.wrapper.Oggetto;
import ai.smarthome.database.wrapper.Sensore;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LogView {

	public static void info(String messaggio) {
		Log.i("SmartHome__", messaggio);
	}
	
	public static void componenti(SQLiteDatabase db) {
		ArrayList<Componente> lista = Componente.getAllLista(db);
		String messaggio = "";
		for(Componente c : lista) {
			messaggio += c.getNome()+" -"+c.getTipo()+" -"+c.getStato()+"\n";
		}
		info(messaggio);
	}
	
	public static void sensori(SQLiteDatabase db) {
		ArrayList<Sensore> lista = Sensore.getAllLista(db);
		String messaggio = "";
		for(Sensore s : lista) {
			messaggio += s.getNome()+" -"+s.getTipo()+" -"+s.getStato()+"\n";
		}
		info(messaggio);
	}
	
	public static void oggetti(SQLiteDatabase db) {
		ArrayList<Oggetto> lista = Oggetto.getAllLista(db);
		String messaggio = "";
		for(Oggetto o : lista) {
			messaggio += o.getNome()+" -"+o.getClasse()+" -"+o.getStato()+"\n";
		}
		info(messaggio);
	}
}
