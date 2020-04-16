package lab4;

import java.util.ArrayList;
import java.util.List;

public class Sensor {
	int identyfikator;
	int x ;
	int y;
	int stan; //0- wy³¹czony, 1 dzia³a, 2 - roz³adowany,3 - zepsuty
	int promien;
	List<Integer> p;// ktore poi widzi sensor
	List<Integer>s;// s¹siedznie sensory
	public Sensor(int i, int j,int r) {
		// TODO Auto-generated constructor stub
		this.x=i;
		this.y=j;
		stan=1;
		promien=r;
		p = new ArrayList<Integer>();
		s = new ArrayList<Integer>();
	}
	public int getPromien() {
		return promien;
	}
	public void setPromien(int promien) {
		this.promien = promien;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getStan() {
		return stan;
	}
	public void setStan(int stan) {
		this.stan = stan;
	}
}
