package lab4.Node;

import lab4.Dane;
import lab4.La.HistoryItem;
import lab4.La.strategies.*;

import lab4.Utils.ToClone;
import lab4.Utils.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static lab4.Utils.Utils.getCommonPois;


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

		double reward=0;
		if(poisInRange.size()==0)
			return 0;
		if(this.getStan()==0) {

			if(d.getQ()<=getCurrentLocalCoverageRate() && getCurrentLocalCoverageRate() <=d.getQ()+d.getDelta2()) {
				reward=d.getC_offPlus();
			}else if(getCurrentLocalCoverageRate()>d.getQ()+d.getDelta2()){
				double d1=getCurrentLocalCoverageRate()-(d.getQ()+d.getDelta2());
				reward=d.getC_offPlus()-d.getC1()*d1;
			}else if(getCurrentLocalCoverageRate()<d.getQ())
			{
				double d2=d.getQ()-getCurrentLocalCoverageRate();
				reward= d.getC_offPlus()-d.getC2()*d2;
			}


		}
		else if(this.getStan()==1) {
			int i0=0,i1=0,i2=0;
			double d1=0,d2=0;
			for(var neighbour:neighborSensors)
			{

				if(neighbour.getCurrentLocalCoverageRate()>d.getQ()+d.getDelta2())
				{
					d1=d1+(neighbour.getCurrentLocalCoverageRate()-(d.getQ()+d.getDelta2()));
					i1++;
				}
				if(neighbour.getCurrentLocalCoverageRate()<d.getQ())
				{
					d2=d2+(d.getQ()-neighbour.getCurrentLocalCoverageRate());
					i2++;
				}
			}
			double d1_avg=0,d2_avg=0;
			if(i1>0)
			{
				d1_avg=d1/i1;
			}

			if(i2>0)
			{
				d2_avg=d2/i2;
			}



			reward=d.getC_on()*getMStar()-d.getC3()*d1_avg-d.getC4()*d2_avg;



		}
		if(reward<0)
			return 0;
		else
			return reward;


	}

	public double getMStar() {

		if(poisInRange.size()==0)
			return 0;
		int poiSum=0;
		for(var neighbour:neighborSensors)
		{
			if(neighbour.getStan()==1)
			{
				List<Poi> commonPois=getCommonPois(this,neighbour);
				poiSum+=commonPois.size();
			}
		}
		return poisInRange.size() / (poisInRange.size() + poiSum + 0.0);
	}

	private double computeRevOn(Dane data, List<Sensor> sensorList) {

		int sumList=0;
		int numberOfNeighborsOfNeighbors=0;
		for(var sensor:neighborSensors)
		{
			sumList+=(sensor.poisInRange.size()*sensor.neighborSensors.size());
			numberOfNeighborsOfNeighbors+=sensor.neighborSensors.size();
		}

		double b=1-numberOfNeighborsOfNeighbors/(neighborSensors.size()+1.0);
		double a=poisInRange.size()/(poisInRange.size()+sumList+0.0);
		double result=data.getC_on()*(data.getC()*a+(1-data.getC())*b);
		return result;
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

		connectWithNeighbors();

	}

	public void connectWithNeighbors() {
		Set<Sensor> neighbourSet = new HashSet<>(neighborSensors);
		for(Poi p:poisInRange) {
			for(Sensor sensor:p.coveringSensorsList) {
				if(sensor!=this)
					neighbourSet.add(sensor);
			}
		}
		neighborSensors.addAll(neighbourSet);
	}




	public double getCurrentLocalCoverageRate() {
		if(poisInRange.size()==0)
			return 1;
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
//		LocalDateTime start=LocalDateTime.now();
		if(memory.size()>=data.laData.h)
		{
			memory.remove(0);
		}

		double reward=computeReword(data,sensorsList);
		memory.add(new HistoryItem(reward,lastUsedStrategy));
		sum_u+=reward;
//		LocalDateTime end=LocalDateTime.now();
//		System.out.println("czas:"+(end.getSecond()-start.getSecond()));
	}

	public void discontRewardRTS() {

		if(isReadyToShare)
		{
			if(poisInRange.size()==0)
			{
				return;
			}
			int numRTSneigbors=0;
			double rewardSum=0;
			List<Sensor>rTSNeighbors=getRTSSensors();
			for(Sensor neigborSensor:rTSNeighbors)
			{


				numRTSneigbors++;
				rewardSum+=(neigborSensor.memory.get(memory.size()-1).getReward())/(neigborSensor.neighborSensors.size()+1);

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

	public Strategy getLastStrategy() {
		return lastUsedStrategy;
	}

	public void clearNeighbours() {
		neighborSensors=new ArrayList<>();
	}

	public void switchState() {
		if(stan==1)
			stan=0;
		else
			stan=1;
	}

	public String getPoiCoverageString() {
		List<Poi>result= poisInRange.stream().filter(x->x.isCovered()).collect(Collectors.toList());
		return result.size()+"/"+poisInRange.size();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Sensor sensor = (Sensor) o;
		return identyfikator == sensor.identyfikator &&
				Double.compare(sensor.x, x) == 0 &&
				Double.compare(sensor.y, y) == 0 &&
				stan == sensor.stan &&
				promien == sensor.promien;
	}

	@Override
	public int hashCode() {
		return Objects.hash(identyfikator, x, y, stan, promien);
	}
}
