package lab4;

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
	private double c_offMinus;
	private double c_on;
	private double C;
	List<List<Sensor>>listsOfSensorsForEachSecond;
	List<Poi>listOfPoi;
	List<Sensor>listOfSensors;

	public void setListOfPoi(List<Poi> listOfPoi) {
		this.listOfPoi = listOfPoi;
	}

	public double getC_offPlus() {
		return c_offPlus;
	}

	public void setC_offPlus(double c_offPlus) {
		this.c_offPlus = c_offPlus;
	}

	public double getC_offMinus() {
		return c_offMinus;
	}

	public void setC_offMinus(double c_offMinus) {
		this.c_offMinus = c_offMinus;
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
