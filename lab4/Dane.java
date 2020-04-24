package lab4;

public class Dane {
	double q;;
	double bateria;//zurzycie bateri w jednostce czasu
	int pojemnoscBaterii;
	int wariant; //np. 36,221, itd
	int promien;
	int trybSensory; //0 deterministyczne, 1 manualne, 2 losowe
	int liczbaSensorow;

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
	
}
