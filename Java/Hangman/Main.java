//@Author Joni Kemppainen, Oulun yliopisto
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) throws IOException {
		//Scanner syötteiden ottoon
		Scanner syote = new Scanner(System.in);
		/*Tiedosto ja sen polku mistä se luetaan. Tämän joutuu vaihtamaan tehtävää tarkastaessa. Olen kopiounut sanat netistä hirsipuu.com nettisivulta ja olen poistanut
		ääkkösiä sisältävät sanat, koska niiden kanssa ilmeni ongelmia peliä pelatessa. */ 
		//Add your own path for your wordlist!
		//File sanat = new File("C:\\Users\\Your_path");
		Sanalista sanalista = new Sanalista(sanat);
		//Asetetaan arvausten vakiomääräksi 6, joka on monessa hirsipuu-pelissä vakio.
		int arvaustenMaara =  6;
		//Tulostetaan tiedoston sanat ihan selkeydeksi, jotta niiden lukeminen onnistuu. Pelaaja myös tietää mitkä sanat ovat mahdollisia.
		System.out.println(sanalista.annaSanat());
		Hirsipuu hirsipuu = new Hirsipuu(arvaustenMaara, sanalista);
		System.out.print("Arvattavan sanan pituus: "); 
		hirsipuu.viivatJaKirjaimet();
		
		//Pelin main-loop, jossa arvaukset tehdään ja peli jatkuu niin kauan kunnes 2 loopin keskeyttävää ehtoa eivät täyty.
		while(true) {
			System.out.println("Arvaa kirjain: ");
			Character kirjain = syote.nextLine().toLowerCase().charAt(0);
			hirsipuu.arvaa(kirjain);
			hirsipuu.viivatJaKirjaimet();
			System.out.println(hirsipuu.arvaukset());
			System.out.println(hirsipuu.arvauksiaJaljella());
			if(!hirsipuu.onLoppu()) {
				System.out.println("Onneksi olkoon, voitit pelin!");
				System.out.println("Kysytty sana oli: " + hirsipuu.sana());
				break;
		}
			if(hirsipuu.arvauksiaJaljella() == 0) {
				System.out.println("Hävisit pelin!");
				break;
		}
	}
	syote.close();
}
}