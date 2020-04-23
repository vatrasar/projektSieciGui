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
		List<Poi> p = new ArrayList<Poi>();
		//wczytanie danych od u�ytkownika
		PobranieDanych load = new PobranieDanych();
		//test
		d.setWariant(36);
		
		
		//zapis wsp�lrzednych sensor�w i POI
		p.addAll(poi(p,d.getWariant())); //wsp�lrzedne POI
		
		
		//algorytm
		Wyswietlanie w=new Wyswietlanie(sensory,p);
			do{
				
			}while(pokrycie()>=d.getQ());
		
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
	private static List<Poi>poi (List<Poi> l,int wariant){
		int pom = (int)Math.sqrt(wariant);
		int pom2= 100/pom-1;
		for(int i=0;i<pom;i++) {
			for(int j=0;j<pom;j++) {
			l.add(new Poi(i*pom2,j*pom2));
			}
		}
		return l;
	}
}
