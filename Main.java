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
		
		//wczytanie danych od u�ytkownika
		
		//dane testowe
		/*
		d.setWariant(121);
		d.przeliczPoi();
		d.setLiczbaSensorow(317);
		d.setPromien(2);
		d.setTrybSensory(0);
		*/
		
		//zapis wsp�lrzednych sensor�w i POI
		p.addAll(poi(p,d.getWariant())); //wsp�lrzedne POI
		//zapis sensor�w
		sensory.addAll(sensorRozlorzenie(sensory,d.getPromien(),d.getLiczbaSensorow(),d.getTrybSensory(),d.getWariant()));

		
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
		int pom = wariant;
		int pom2= 100/(pom-1);
		for(int i=0;i<pom;i++) {
			for(int j=0;j<pom;j++) {
			l.add(new Poi(i*pom2,j*pom2));
			}
		}
		return l;
	}
	private static List<Sensor> sensorRozlorzenie(List<Sensor> s,int r, int ile,int wybor,int wariant){
	//	if(wybor==0) {
			return detemrinistcyzny(s, r, ile,wariant);
		/*}else if(wybor==1) {
			
		}else {
			
		}*/
				
	}
	private static List<Sensor> detemrinistcyzny(List<Sensor> s,int r, int ile,int p){
		int pom2=(int) Math.floor(10000/ile);
		int pom3= (int) Math.floor(Math.sqrt(pom2));
		int pomss=(int) Math.floor(100/pom3);
		int pom4= (int) Math.ceil((ile/pomss));
		int pom5=(int) Math.floor(100/pom4);
		int roz=0;
			for(int i=2;i<100 && roz<ile;i=i+pom5) {
				for(int j=0;j<100 && roz<ile;j=j+pom3) {
						s.add(new Sensor(j,i,r));
						roz++;
			}
		}
			//poprawka na granice, je�li r <10 i poi=36
			if(r<0.5*p) {
			List<Sensor> poprawka = new ArrayList<Sensor>();
			poprawka.addAll(poprawka(s,r,ile,pomss));
			s.clear();
			return poprawka;
			}
		return s;
	}
	private static List<Sensor> poprawka(List<Sensor> s,int r, int ile,int pom){
		//na x
		int pom10=ile%pom;
		if(pom10==0) {
			pom10=pom;
		}
		for(int i=0;i<pom;i++) {
			int pom2=(i*pom+pom)-1;;
			if(ile>pom2) {
			}
			Sensor se = null;
			for(Sensor poms:s) {
				if(poms.getIdentyfikator()==pom2) {
				se=poms;
				}
			}
			s.remove(se);
			if(se!=null) {
			int roznica=100-se.getX();
			if(roznica>r)
				se.setX(se.getX()+(roznica-r)+1);
			s.add(se);
			}
			
		}
			
// na y
		for(int i=0;i<pom10;i++) {
			int pom2=ile-pom10+i;
			Sensor se = null;
			for(Sensor poms:s) {
				if(poms.getIdentyfikator()==pom2) {
				se=poms;
				}
			}
			s.remove(se);
			int roznica=100-se.getY();
			if(roznica>r)
			se.setY(se.getY()+(roznica-r)+1);
			s.add(se);
		}
		return s;
	}

}
