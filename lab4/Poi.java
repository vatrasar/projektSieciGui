package lab4;

import java.util.ArrayList;
import java.util.List;

public class Poi implements Node {
	int identyfikator;
	double x;
	double y;
	public List<Sensor> coveringSensorsList ;
	List<Integer> widziane;// ktore sensory widz� poi

	public Poi(int i, int j) {
		// TODO Auto-generated constructor stub
		this.x=i;
		this.y=j;
		coveringSensorsList= new ArrayList<Sensor>();
		widziane=new ArrayList<Integer>();

	}
	public double getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public boolean isCovered() {
		if(coveringSensorsList.stream().anyMatch(sensor -> sensor.getStan()==1))
			return true;
		else
			return false;
	}
	public List<Integer> getWidziane() {
		return widziane;
	}
	public void setWidziane(List<Integer> widziane) {
		this.widziane = widziane;
	}
	public void widzianeDodaj(Integer w) {
		this.widziane.add(w);
	}

	@Override
	public Node clone() {
		Poi clone= new Poi((int)x,(int)y);
		clone.coveringSensorsList=new ArrayList<>();
		clone.identyfikator=identyfikator;
		clone.widziane=widziane;
		return clone;
	}

}
