//@Author Joni Kemppainen, Oulun yliopisto

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.IOException;


public class Hirsipuu {
	
	private int arvaustenMaara;
	private Sanalista sanalista;
	private List<Character> arvausLista;
	private String arvottuSana;
	
		
//Luokan konstruktori, joka tarvitsisi argumentikseen periaatteessa vain Sanalista-luokan sanalista-olion, jos arvaustenmäärä asetettaisiin vakioksi, mutta tehtävänanto ei sitä salli.
	public Hirsipuu(int arvaustenMaara, Sanalista sanalista) throws IOException {
		this.arvaustenMaara = arvaustenMaara;
		this.arvausLista = new ArrayList<>();
		Random random = new Random();
		List<String> lista = sanalista.annaSanat(); 
		this.arvottuSana = lista.get(random.nextInt(lista.size()));
	}
	
	//Piirtää aluksi sanan kirjainten verran viivoja, jotta pelaajan on helpompi hahmottaa sanan pituutta. Korvaa myös viivoja kirjaimilla sitä mukaa, kun pelaaja tekee arvauksia.
	public void viivatJaKirjaimet() {
		for(int i = 0; i < arvottuSana.length(); i++) {
			if(arvausLista.contains(arvottuSana.charAt(i))){
				System.out.print(arvottuSana.charAt(i));
			}
			else {
				System.out.print("_");
			}
		}
		System.out.println();
	}
	
	//Arvaus-metodi, joka tarkistaa oliko pelaajan arvaus oikein vai ei.
	public boolean arvaa(Character merkki) {
		if(arvottuSana.indexOf(merkki)!= -1){
		arvausLista.add(merkki);
		System.out.println("Oikein!");
		return true;
	} else {
		arvausLista.add(merkki);
		arvaustenMaara -= 1;
		System.out.println("Väärin meni.");
		return false;
	}
}	
	//Tarkistaa pelin tilan.
	public boolean onLoppu() {
		for(int k = 0; k < arvottuSana.length(); k++) {
			if(arvausLista.contains(arvottuSana.charAt(k))){
				continue;
			} else {
				return true;
			}
		}
		return false;
	}
	
	//Luokan muut vaaditut metodit
	
	public List<Character> arvaukset(){
		return arvausLista;
	}
	public int arvauksiaJaljella() {
		return arvaustenMaara;
	}
	public String sana() {
		return arvottuSana;
	}
	public Sanalista getSanalista() {
		return sanalista;
	}
	public void setSanalista(Sanalista sanalista) {
		this.sanalista = sanalista;
	}
	
}