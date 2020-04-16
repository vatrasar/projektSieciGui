package lab4;

import java.util.ArrayList;
import java.util.List;

public class Poi {
	int identyfikator;
	int x;
	int y;
	List<Integer> p ;// ktore sensory widz¹ poi
	public Poi(int i, int j) {
		// TODO Auto-generated constructor stub
		this.x=i;
		this.y=j;
		p= new ArrayList<Integer>();
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
}
