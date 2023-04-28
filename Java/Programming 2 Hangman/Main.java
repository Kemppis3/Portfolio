//@Author Joni Kemppainen, Oulun yliopisto
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) throws IOException {
		//Scanner sy�tteiden ottoon
		Scanner syote = new Scanner(System.in);
		/*Tiedosto ja sen polku mist� se luetaan. T�m�n joutuu vaihtamaan teht�v�� tarkastaessa. Olen kopiounut sanat netist� hirsipuu.com nettisivulta ja olen poistanut
		��kk�si� sis�lt�v�t sanat, koska niiden kanssa ilmeni ongelmia peli� pelatessa. */ 
		File sanat = new File("C:\\Users\\Joni\\Documents\\sanat.txt");
		Sanalista sanalista = new Sanalista(sanat);
		//Asetetaan arvausten vakiom��r�ksi 6, joka on monessa hirsipuu-peliss� vakio.
		int arvaustenMaara =  6;
		//Tulostetaan tiedoston sanat ihan selkeydeksi, jotta niiden lukeminen onnistuu. Pelaaja my�s tiet�� mitk� sanat ovat mahdollisia.
		System.out.println(sanalista.annaSanat());
		Hirsipuu hirsipuu = new Hirsipuu(arvaustenMaara, sanalista);
		System.out.print("Arvattavan sanan pituus: "); 
		hirsipuu.viivatJaKirjaimet();
		
		//Pelin main-loop, jossa arvaukset tehd��n ja peli jatkuu niin kauan kunnes 2 loopin keskeytt�v�� ehtoa eiv�t t�yty.
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
				System.out.println("H�visit pelin!");
				break;
		}
	}
	syote.close();
}
}
