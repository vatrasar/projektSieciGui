package lab4;

import lab4.La.LaData;
import lab4.Node.Poi;
import lab4.Node.Sensor;

import java.io.File;
import java.util.List;

public class Dane {
	double q;
	double bateria;//zurzycie bateri w jednostce czasu
	int pojemnoscBaterii;
	int wariant; //np. 36,221, itd
	int promien;
	int trybSensory; //0 deterministyczne, 1 manualne, 2 losowe
	int liczbaSensorow;
	private double c_offPlus;

	private double c_on;
	private double C;
	private double C1;
	private double C2;
	private double C3;
	private double C4;
	private double delta2;
	private double epsValue; //from evolutionary approach
	private boolean areSensorsFromFile;
	List<List<Sensor>>listsOfSensorsForEachSecond;
	public List<Poi>listOfPoi;
	public List<Sensor>listOfSensors;
	private File fileWithSensors;
	public LaData laData;
	public long randomSeed;


	public double getEpsValue() {
		return epsValue;
	}



	public void setEpsValue(double epsValue) {
		this.epsValue = epsValue;
	}

	public double getC1() {
		return C1;
	}

	public void setC1(double c1) {
		C1 = c1;
	}

	public double getC2() {
		return C2;
	}

	public void setC2(double c2) {
		C2 = c2;
	}

	public double getC3() {
		return C3;
	}

	public void setC3(double c3) {
		C3 = c3;
	}

	public double getC4() {
		return C4;
	}

	public void setC4(double c4) {
		C4 = c4;
	}

	public double getDelta2() {
		return delta2;
	}

	public void setDelta2(double delta2) {
		this.delta2 = delta2;
	}

	public Dane() {
		randomSeed= 60;
	}

	public boolean areSensorsFromFile() {
		return areSensorsFromFile;
	}

	public void setAreSensorsFromFile(boolean areSensorsFromFile) {
		this.areSensorsFromFile = areSensorsFromFile;
	}

	public File getFileWithSensors() {
		return fileWithSensors;
	}

	public void setFileWithSensors(File fileWithSensors) {
		this.fileWithSensors = fileWithSensors;
	}

	public void setListOfPoi(List<Poi> listOfPoi) {
		this.listOfPoi = listOfPoi;
	}

	public double getC_offPlus() {
		return c_offPlus;
	}

	public void setC_offPlus(double c_offPlus) {
		this.c_offPlus = c_offPlus;
	}



	public double getC_on() {
		return c_on;
	}

	public void setC_on(double c_on) {
		this.c_on = c_on;
	}

	public double getC() {
		return C;
	}

	public void setC(double c) {
		C = c;
	}

	public void setListOfSensors(List<Sensor> listOfSensors) {
		this.listOfSensors = listOfSensors;
	}

	public List<Sensor> getListOfSensors() {
		return listOfSensors;
	}

	public List<List<Sensor>> getListsOfSensorsForEachSecond() {
		return listsOfSensorsForEachSecond;
	}

	public void setListsOfSensorsForEachSecond(List<List<Sensor>> listOfSensorsForEachSecond) {
		this.listsOfSensorsForEachSecond = listOfSensorsForEachSecond;
	}

	public List<Poi> getListOfPoi() {
		return listOfPoi;
	}

	public double getQ() {
		return q;
	}

	public void setQ(double q) {
		this.q = q;
	}

	public double getBateria() {
		return bateria;
	}

	public void setBateria(double bateria) {
		this.bateria = bateria;
	}
	
	public int getPojemnoscBaterii() {
		return pojemnoscBaterii;
	}

	public void setPojemnoscBaterii(int pojemnoscBaterii) {
		this.pojemnoscBaterii = pojemnoscBaterii;
	}

	public int getWariant() {
		return wariant;
	}

	public void setWariant(int wariant) {
		this.wariant = wariant;
	}

	public int getPromien() {
		return promien;
	}

	public void setPromien(int promien) {
		this.promien = promien;
	}

	public int getTrybSensory() {
		return trybSensory;
	}

	public void setTrybSensory(int trybSensory) {
		this.trybSensory = trybSensory;
	}

	public int getLiczbaSensorow() {
		return liczbaSensorow;
	}

	public void setLiczbaSensorow(int liczbaSensorow) {
		this.liczbaSensorow = liczbaSensorow;
	}
	public void przeliczPoi() {
		wariant=(int)Math.sqrt(wariant);
	}

	public String getlocationCreationTypeName() {

		switch (trybSensory)
		{
			case 0:
				return "d";
			case 1:
				return "h";
			default:
				return "r";
		}

	}


}
