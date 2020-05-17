package lab4.Node;

import lab4.Dane;
import lab4.La.HistoryItem;
import lab4.La.strategies.*;

import lab4.Utils.ToClone;
import lab4.Utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;



public class Sensor implements Node, ToClone {
	int identyfikator;
	static int pom=0;
	private double x ;
	private double y;
	public int stan; //0- wylaczony, 1 dziala, 2 - rozladowany,3 - zepsuty
	int promien;
	int bateriaPojemnosc;
	public int k;
	public double sum_u;
	public List<HistoryItem>memory;
	boolean isReadyToShare;
	Strategy lastUsedStrategy;


	public List<Poi> poisInRange;// ktore poi widzi sensor
	List<Integer>s;// sasiedznie sensory
	public List<Sensor> neighborSensors;

	public Sensor(double x, double y,int r,int batteryCappacity) {
		// TODO Auto-generated constructor stub
		this.x=x;
		this.y=y;
		stan=1;
		promien=r;
		poisInRange= new ArrayList<Poi>();
		s = new ArrayList<Integer>();
		this.setIdentyfikator(pom);
		pom++;
		sum_u=0;
		neighborSensors=new ArrayList<>();
		memory=new ArrayList<>();
		bateriaPojemnosc=batteryCappacity;
		sum_u=0;
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
	public double computeReword(Dane d, List<Sensor>sensorList)
	{
		if(poisInRange.size()==0)
			return 0;
		if(this.getStan()==0) {
			if(getCurrentLocalCoverageRate()-d.getQ()>=0) {
				return d.getC_offPlus();
			}else if(getCurrentLocalCoverageRate()-d.getQ()<0){
				return d.getC_offMinus()-(this.poisInRange.size()*(d.getQ()-getCurrentLocalCoverageRate()));
			}
		}else if(this.getStan()==1) {
			double sumaPoi=0;
			double suma=0;
			for(int counter=0;counter<this.s.size();counter++) {
				Integer i=this.s.get(counter);

				for(Sensor s2: sensorList) {
					if(s2.getIdentyfikator()==i) {
						sumaPoi=sumaPoi+s2.getStan2()*oblicz(s2);
						suma=suma+s2.getStan2();


					}

				}
//				System.out.println("ok");
			}
			sumaPoi+=this.poisInRange.size();
			sumaPoi=this.poisInRange.size()/sumaPoi;
		//	System.out.println(d.getC_on()*((d.getC()*sumaPoi)+((1-d.getC())*(1-(suma/(this.getS().size()+1))))));
			double result= d.getC_on()*((d.getC()*sumaPoi)+((1-d.getC())*(1-(suma/(this.getS().size()+1)))));
			if(((Double)result).isNaN())
			{
				System.out.println("NaN number in reward!");
			}
		}
		return -1; // błąd
	}

	public double getSum_u() {
		return sum_u;
	}

	public void setSum_u(double sum_u) {
		this.sum_u = sum_u;
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

		for(Poi p:poisInRange) {
			for(Sensor sensor:p.coveringSensorsList) {
				if(sensor!=this && !sensor.neighborSensors.contains(sensor))
					this.neighborSensors.add(sensor);
			}
		}

}
	public double getCurrentLocalCoverageRate() {
		if(poisInRange.size()==0)
			return 0;
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

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	@Override
	public ToClone clone() {
		Sensor clone= new Sensor(x,y,promien,bateriaPojemnosc);
		clone.poisInRange=new ArrayList<>();
		clone.bateriaPojemnosc=bateriaPojemnosc;
		clone.neighborSensors=new ArrayList<>();
		clone.s=s;
		clone.identyfikator=identyfikator;
		clone.stan=stan;
		clone.k=k;
		clone.isReadyToShare=isReadyToShare;
		clone.lastUsedStrategy=lastUsedStrategy;
		clone.memory= Utils.cloneList(memory);
		return clone;
	}

	public void useStrategy(Strategy strategy) {
		stan=strategy.decideAboutSensorState(neighborSensors,k);
		if(bateriaPojemnosc==0)
			stan=0;
		lastUsedStrategy=strategy;
	}

	public boolean isReadyToShare() {
		return isReadyToShare;
	}

	public void setReadyToShare(boolean readyToShare) {
		isReadyToShare = readyToShare;
	}

	public void discontReward(Dane data, List<Sensor> sensorsList) {

		if(memory.size()>=data.laData.h)
		{
			memory.remove(0);
		}

		double reward=computeReword(data,sensorsList);
		memory.add(new HistoryItem(reward,lastUsedStrategy));
		sum_u+=reward;
	}

	public void discontRewardRTS() {

		if(isReadyToShare)
		{
			int numRTSneigbors=0;
			double rewardSum=0;
			List<Sensor>rTSNeighbors=getRTSSensors();
			for(Sensor neigborSensor:rTSNeighbors)
			{


				numRTSneigbors++;
				rewardSum+=(neigborSensor.memory.get(memory.size()-1).getReward())/(neigborSensor.neighborSensors.size()+1);

			}
			if(poisInRange.size()==0)
			{
				System.out.println("discontRewardRTS");
			}
			double newRewardValue=memory.get(memory.size()-1).getReward()/(numRTSneigbors+1)+rewardSum;
			HistoryItem target=memory.get(memory.size()-1);
			sum_u-=target.getReward();
			sum_u+=newRewardValue;
			target.setReward(newRewardValue);

		}
	}

	private List<Sensor> getRTSSensors() {

		return neighborSensors.stream().filter(sensor -> sensor.isReadyToShare).collect(Collectors.toList());
	}

	public void useBestStrategy() {
		HistoryItem bestRecord = getBestRecordFromMemory();
		useStrategy(bestRecord.getStrategy());
	}

	public HistoryItem getBestRecordFromMemory() {
		HistoryItem bestRecord=memory.get(0);
		for(HistoryItem record:memory)
		{
			if(bestRecord.getReward()<record.getReward())
			{
				bestRecord=record;
			}
		}
		return bestRecord;
	}

	public void useRandomStrategy(Random random) {
		switch (random.nextInt(4))
		{
			case 0:
				useStrategy(new AllCStrategy());
				break;
			case 1:
				useStrategy(new KCStrategy());
				break;
			case 2:
				useStrategy(new KDCStrategy());
				break;
			case 3:
				useStrategy(new KDStrategy());
				break;
		}
	}

	public void eraseBattery() {
		if(bateriaPojemnosc!=0)
			bateriaPojemnosc--;
	}

	/**
	 * @return null when has no neighbours
	 */
	public Sensor getNeighborWithBestSumU() {
		if(neighborSensors.size()==0)
		{
			return null;
		}
		Sensor bestNeighbor=neighborSensors.get(0);
		for (Sensor neighbour:neighborSensors)
		{
			if(bestNeighbor.getSum_u()<neighbour.getSum_u())
			{
				bestNeighbor=neighbour;
			}
		}
		return bestNeighbor;
	}

	public double getLastReward() {
		return 	memory.get(memory.size()-1).getReward();
	}
}
