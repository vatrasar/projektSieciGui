package lab4;

public class Dane {
	double q;;
	int bateria;//zurzycie bateri w jednostce czasu
	int wariant; //np. 36,221, itd
	int promien;
	int trybSensory; //0 deterministyczne rzlorzenie
	int liczbaSensorow;

	public double getQ() {
		return q;
	}

	public void setQ(double q) {
		this.q = q;
	}

	public int getBateria() {
		return bateria;
	}

	public void setBateria(int bateria) {
		this.bateria = bateria;
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
