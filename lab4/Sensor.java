package lab4;

import java.util.ArrayList;
import java.util.List;

public class Sensor implements Node {
	int identyfikator;
	static int pom=0;
	private double x ;
	private double y;
	int stan; //0- wylaczony, 1 dziala, 2 - rozladowany,3 - zepsuty
	int promien;
	int bateriaPojemnosc;
	List<Poi> poisInRange;// ktore poi widzi sensor
	List<Integer>s;// sasiedznie sensory
	public Sensor(double x, double y,int r) {
		// TODO Auto-generated constructor stub
		this.x=x;
		this.y=y;
		stan=1;
		promien=r;
		poisInRange= new ArrayList<Poi>();
		s = new ArrayList<Integer>();
		this.setIdentyfikator(pom);
		pom++;
	}
	public int getPromien() {
		return promien;
	}
	public void setPromien(int promien) {
		this.promien = promien;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public int getStan() {
		return stan;
	}
	private int getStan2() {
		if (stan!=1)
				return 0;
		else
			return 1;
	}
	public void setStan(int stan) {
		this.stan = stan;
	}
	public int getIdentyfikator() {
		return identyfikator;
	}
	public void setIdentyfikator(int identyfikator) {
		this.identyfikator = identyfikator;
	}
	public int getBateriaPojemnosc() {
		return bateriaPojemnosc;
	}
	public void setBateriaPojemnosc(int bateriaPojemnosc) {
		this.bateriaPojemnosc = bateriaPojemnosc;
	}
	public void wypisz() {
		System.out.println(x+ " "+y);
	}
	
	public List<Poi> getPoisInRange() {
		return poisInRange;
	}
	public void setPoisInRange(List<Poi> poisInRange) {
		this.poisInRange = poisInRange;
	}
	public List<Integer> getS() {
		return s;
	}
	public void setS(List<Integer> s) {
		this.s = s;
	}
	public double computeReword(Dane d)
	{
		if(this.getStan()==0) {
			if(getCurrentLocalCoverageRate()-d.getQ()>=0) {
				return d.getC_offPlus();
			}else if(getCurrentLocalCoverageRate()-d.getQ()<0){
				return d.getC_offMinus()-(this.poisInRange.size()*(d.getQ()-getCurrentLocalCoverageRate()));
			}
		}else if(this.getStan()==1) {
			double sumaPoi=0;
			double suma=0;
			for(Integer i: this.s) {
				for(Sensor s2: d.getListOfSensors()) {
					if(s2.getIdentyfikator()==i) {
						sumaPoi=sumaPoi+s2.getStan2()*oblicz(s2);
						suma=s2.getStan2();
					}
				}
			}
			sumaPoi+=this.poisInRange.size();
			sumaPoi=this.poisInRange.size()/sumaPoi;
			return d.getC_on()*((d.getC()*sumaPoi)+(1-d.getC())*(1-(suma/this.getS().size()+1)));
		}
		return -1; // błąd
	}
	private double oblicz(Sensor s) {
		List<Integer> pom= new ArrayList<Integer>();
		pom=this.getS();
		pom.retainAll(s.getS());//wybranie tych samych poi które widzą sensory
		return pom.size();
	}
	
	public void addPoi() {
		for(Poi p:poisInRange) {
		p.widzianeDodaj(this.getIdentyfikator());
		}
	}
	public void SensorSasiednie() {
		for(Poi p:poisInRange) {
			for(Integer i:p.getWidziane()) {
				if(i!=this.getIdentyfikator())
				this.s.add(i);
			}
		}
}
	public double getCurrentLocalCoverageRate() {
		int numberOfCoveredPois = getNumberOfCoveredPois();
		return ((double)numberOfCoveredPois)/poisInRange.size();
	}

	private int getNumberOfCoveredPois() {
		int numberOfCoveredPois=0;
		for(Poi poi:poisInRange)
		{
			if(poi.isCovered())
				numberOfCoveredPois++;
		}
		return numberOfCoveredPois;
	}
}
