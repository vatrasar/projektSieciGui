package lab4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int t=0;
		List<Sensor> sensory = new ArrayList<Sensor>();
		Dane d=new Dane();
		//wczytanie danych od u¿ytkownika
		
		
		
		List<Poi> p = new ArrayList<Poi>();
		//odczytanie z pliku poi i sensory z pdozia³u
		
		
		//algorytm
			do{
				
			}while(pokrycie()>=d.getQ());
		Wyswietlanie w=new Wyswietlanie(sensory,p);
		
			poczekaj();
			w.aktualizacja();
		}

	private static void poczekaj(){
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	private static double pokrycie(){
		//do zrobienia
		return 1.0;
}
}
