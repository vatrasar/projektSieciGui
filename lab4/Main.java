package lab4;

import UI.UiThread;
import lab4.La.LaAlgorithm;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new UiThread());

		System.out.println("lol");
		// TODO Auto-generated method stub
		int t=0; //ile ejdnsotek czasu trwa symulacja
		List<Sensor> sensory = new ArrayList<Sensor>();

		List<Poi> p = new ArrayList<Poi>();
		Dane d=new Dane();
		//wczytanie danych od u�ytkownika
//		PobranieDanych load = new PobranieDanych(sensory, p, d);
		//dane testowe



		}

	public static void runSimulation(Dane data,boolean isDebug)
	{
		List<Poi> p = new ArrayList<Poi>();

		//zapis wsp�lrzednych sensor�w i POI
		poi(p,data.getWariant()); //wsp�lrzedne POI
		//zapis sensor�w


		int t=0;
		//algorytm

		data.setListOfPoi(p);
		data.setListOfSensors(getSensorsList(data,isDebug));
		
		if (data.getTrybSensory() == 1)
		{
		EventQueue.invokeLater(new Runnable() {
 			@Override
 			public void run() {
 				RozmieszczenieManualne manual = new RozmieszczenieManualne(data.getListOfSensors(), p, data, "");
 				manual.repaint();
 			}
 		});
		}else {
			runExperiment(data, isDebug, p);
		}
	}
	
	public static void runExperiment(Dane data,boolean isDebug, List<Poi> p) {
		
		Utils.connectSensorsWithPoi(data.listOfPoi,data.listOfSensors,data.promien);
		for(Sensor s :data.getListOfSensors()) {
			s.addPoi();
			s.SensorSasiednie();
		}
		LaAlgorithm algorithm=new LaAlgorithm(data);
		data.setListsOfSensorsForEachSecond(algorithm.getShedule());
//		data.setListsOfSensorsForEachSecond(naiveAlgorithm(data.listOfSensors));

		if(isDebug)
		{
			saveDebugData(data);
		}


		saveExperimentDataToFile(data);

		Wyswietlanie visualisation=new Wyswietlanie(data.getListOfSensors(),p,"");
		Simulation simulation=new Simulation(data,visualisation,isDebug);
		visualisation.setSimulation(simulation);
		simulation.start();
	}

	private static List<Sensor> getSensorsList(Dane data,boolean isDebug) {
		List<Sensor> sensors = new ArrayList<Sensor>();
		if(isDebug) {
			sensors.addAll(getDebugSensorsDistribution(data.getPromien(), data));
			data.setLiczbaSensorow(sensors.size());
		}
		else if(data.areSensorsFromFile())
		{
			sensors=getSensorsListFormFile(data.getFileWithSensors(),data.getPromien(),data.pojemnoscBaterii);
			data.setLiczbaSensorow(sensors.size());

		}
		else
			sensorRozlozenie(sensors,data.getPromien(),data.getLiczbaSensorow(),data.getTrybSensory(),data.getWariant(),data.pojemnoscBaterii);

		return sensors;
	}

	private static List<Sensor> getSensorsListFormFile(File fileWithSensors,int sensorSensingRange,int batteryCappacity) {
		List<Sensor>sensorsList = new ArrayList<>();

		try {
			Scanner scanner = new Scanner(fileWithSensors);
			scanner.nextLine();//abandon first line

			while(scanner.hasNextLine()) {
				String strSensorCordinates = scanner.nextLine();
				String[] strTabSensorCordinates = strSensorCordinates.split(" ");
				Sensor newSensor = new Sensor(Double.parseDouble(strTabSensorCordinates[0]), Double.parseDouble(strTabSensorCordinates[1]), sensorSensingRange,batteryCappacity);
				sensorsList.add(newSensor);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return sensorsList;
	}

	private static void saveDebugData(Dane data) {
		if(data.getListsOfSensorsForEachSecond().size()==0)
			return;
		List<Sensor> list=data.getListsOfSensorsForEachSecond().get(0),  allSensors=data.getListOfSensors();
		try {
			PrintWriter printWriter=new PrintWriter("debugData.plt");
			printWriter.println("#parameters of simulation: WSN-"+data.getListOfSensors().size()+data.getlocationCreationTypeName()+",POI-"+data.listOfPoi.size());
			printWriter.println("# who is ON/OFF");
//			printWriter.println("#s_num s_i s_i-1 s_i-2 q q_curr rev");

			allSensors.forEach(sensor->sensor.setStan(0));
			list.forEach(sensor->sensor.setStan(1));
			String outString=DebugFile.generate(data);
			printWriter.print(outString);
//			printWriter.println(String.format("%d %d %d %d %.2f %.2f %.2f",1,allSensors.get(0).getStan(),allSensors.get(1).getStan(),allSensors.get(2).getStan(),data.getQ(),allSensors.get(0).getCurrentLocalCoverageRate(),allSensors.get(0).computeReword(data)));
//
//
//			printWriter.println(String.format("%d %d %d %d %.2f %.2f %.2f",2,allSensors.get(1).getStan(),allSensors.get(0).getStan(),allSensors.get(2).getStan(),data.getQ(),allSensors.get(1).getCurrentLocalCoverageRate(),allSensors.get(1).computeReword(data)));
//
//			printWriter.println(String.format("%d %d %d %d %.2f %.2f %.2f",3,allSensors.get(2).getStan(),allSensors.get(0).getStan(),allSensors.get(1).getStan(),data.getQ(),allSensors.get(2).getCurrentLocalCoverageRate(),allSensors.get(2).computeReword(data)));
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}



	private static void saveExperimentDataToFile(Dane data) {


		saveLocationOfSensors(data.getListOfSensors(),data.getlocationCreationTypeName());

	}

	private static void saveLocationOfSensors(List<Sensor>listOfSensors,String locationCreationType) {

		try {

			PrintWriter out=new PrintWriter("WSN-"+listOfSensors.size()+locationCreationType+".txt");
			out.println("#x y");
			for(Sensor sensor:listOfSensors)
				out.println(sensor.getX()+" "+sensor.getY());
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param sensorList
	 * @return returns list of two(or more) subsets of sensorList
	 */
	private static List<List<Sensor>> naiveAlgorithm(List<Sensor>sensorList)
	{
		List<List<Sensor>>sensorListSubSets=new ArrayList<>();
		int divide=3;
		while (sensorList.size()%divide!=0)
		{
			divide++;
		}
		int subsetSize=sensorList.size()/divide;
		List<Sensor>sensorListCopy=new ArrayList<>(sensorList);
		while (sensorListCopy.size()!=0)
		{
			sensorListSubSets.add(sensorListCopy.subList(0,subsetSize));

			sensorListCopy=sensorListCopy.stream().skip(subsetSize).collect(Collectors.toList());
		}
		return sensorListSubSets;
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

		for(int i=0;i<pom;i++)
		{
			for(int j=0;j<pom;j++)
			{

				l.add(new Poi(i*pom2,j*pom2));
			}
		}
		return l;
	}
	private static List<Sensor> sensorRozlozenie(List<Sensor> s,int r, int ile,int wybor,int wariant,int batteryCappacity){
		if(wybor==0) {
			return detemrinistczny(s, r, ile,wariant,batteryCappacity);
		}else if(wybor==2)
			return losowy(s, r, ile,wariant,batteryCappacity);
		else
			return manualny(s, r, ile,wariant,batteryCappacity);
	}
	private static List<Sensor> detemrinistczny(List<Sensor> s,int r, int ile,int p,int batteryCappacity){
		int pom2=(int) Math.floor(10000/ile);
		int pom3= (int) Math.floor(Math.sqrt(pom2));
		int pomss=(int) Math.floor(100/pom3);
		int pom4= (int) Math.ceil((ile/pomss));
		int pom5=(int) Math.floor(100/pom4);
		int roz=0;
			for(int i=2;i<100 && roz<ile;i=i+pom5) {
				for(int j=0;j<100 && roz<ile;j=j+pom3) {
						s.add(new Sensor(j,i,r,batteryCappacity));
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
			int roznica=(int) (100-se.getX());
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
			int roznica=(int) (100-se.getY());
			if(roznica>r)
			se.setY(se.getY()+(roznica-r)+1);
			s.add(se);
		}
		return s;
	}
	
	private static List<Sensor> losowy(List<Sensor> s, int r, int ile, int p, int batteryCappacity){
		Random generator = new Random();
			for(int i=0;i<ile;i++) {
				double x = generator.nextDouble()*100;
				double y = generator.nextDouble()*100;
				s.add(new Sensor(x, y, r,batteryCappacity));
		}
		return s;
	}
	private static List<Sensor> manualny(List<Sensor> s, int r, int ile, int p, int batteryCappacity){
		//TODO
		return s;
	}



	private static Collection<? extends Sensor> getDebugSensorsDistribution(int range, Dane data) {
		List<Sensor>debugSesors = new ArrayList<>();
		if(data.areSensorsFromFile())
		{
			debugSesors=getSensorsListFormFile(data.getFileWithSensors(),range,data.pojemnoscBaterii);
		} else {
			debugSesors.add(new Sensor(20, 80, range,data.pojemnoscBaterii));
			debugSesors.add(new Sensor(60, 40, range,data.pojemnoscBaterii));
			debugSesors.add(new Sensor(30, 30, range,data.pojemnoscBaterii));
		}
		return debugSesors;
	}
}
