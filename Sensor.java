package lab4;

import java.util.ArrayList;
import java.util.List;

public class Sensor {
	int identyfikator;
	static int pom=0;
	double x ;
	double y;
	int stan; //0- wylaczony, 1 dziala, 2 - rozladowany,3 - zepsuty
	int promien;
	int bateriaPojemnosc;
	List<Integer> p;// ktore poi widzi sensor
	List<Integer>s;// sasiedznie sensory
	public Sensor(double i, double j,int r) {
		// TODO Auto-generated constructor stub
		this.x=i;
		this.y=j;
		stan=1;
		promien=r;
		p = new ArrayList<Integer>();
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
	
}
