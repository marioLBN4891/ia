package ai.smarthome.database.wrapper;

import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;

public class Componente {

	private static final String A_S = "accceso/spento";
	private static final String A_C = "aperto/chiuso";
	
	private String nome;
	private boolean stato;
	private String tipo;
	
	public Componente(String nome, boolean stato, String tipo) {
		this.nome = nome;
		this.stato = stato;
		this.tipo = tipo;
	}
	
	
	public ArrayList<Componente> getListaComponenti(SQLiteDatabase db) {
		ArrayList<Componente> lista = new ArrayList<Componente>();
		
		
		return lista;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setStato(boolean stato) {
		this.stato = stato;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getNome() {
		return this.nome;
	}

	public boolean getStato() {
		return this.stato;
	}

	public String getTipo() {
		return this.tipo;
	}
}
