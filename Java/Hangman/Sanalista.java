//@Author Joni Kemppainen, Oulun yliopisto
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sanalista {

	private List<String> sanalista;
	
	//Luokan konstruktori, jossa luodaan sanalista annetusta tekstitiedostosta
	public Sanalista(File listasanoista) throws IOException {
		try (BufferedReader bufReader = new BufferedReader(new FileReader(listasanoista))) {
			this.sanalista = new ArrayList<>();
			String line = bufReader.readLine();
			while (line != null) {
				sanalista.add(line);
				line = bufReader.readLine();
			}
		}
		catch(IOException e) {
			System.err.println("Nyt meni pieleen.");
		}
	
	}

//Luokan muut metodit
	public void setSanalista(List<String> sanalista) {
		this.sanalista = sanalista;
	}

	public List<String> annaSanat(){
		return sanalista;
	}
	
	public void tulostaLista() {
		System.out.println(annaSanat());
	}

}