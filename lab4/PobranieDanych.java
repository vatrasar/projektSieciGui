package lab4;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;
import java.awt.BorderLayout;
import javax.swing.*;

public class PobranieDanych extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	int dlugoscOkna=900;
	int szerokoscOkna=400;
	int height = 10;
	int wiersze = 14;
	int kolumny = 2;



	List<Sensor> sensory;
	List<Poi> poi;
	JPanel panel;
	JLabel ConLabel = new JLabel("Con: ");
	JSpinner Con = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
	JLabel CoffLabel = new JLabel("Coff: ");
	JSpinner Coff = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
	JLabel CLabel = new JLabel("C: ");
	JSpinner C = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
	JSpinner pojemnoscBaterii = new JSpinner(new SpinnerNumberModel(10, 0, 100, 1));
	JLabel pojemnoscBateriiLabel = new JLabel("Pojemność baterii sensora: ");
	JSpinner zuzycieBaterii = new JSpinner(new SpinnerNumberModel(0.1, 0, 100, 0.01));
	JLabel zuzycieBateriiLabel = new JLabel("Zużycie baterii w j. czasu: ");
	JSpinner zasiegSensora = new JSpinner(new SpinnerNumberModel(5, 1, 50, 1));
	JLabel zasiegSensoraLabel = new JLabel("Zasięg sensora: ");
	JSpinner liczbaSensorow = new JSpinner(new SpinnerNumberModel(300, 0, 1000, 1));
	JLabel liczbaSensorowLabel = new JLabel("Liczba sensorów: ");
	JLabel rozmieszczenieSensorowLabel = new JLabel("Rozmieszczenie sensorów: ");
	JLabel rozmieszczeniePOILabel = new JLabel("Rozmieszczenie POI: ");

    ButtonGroup groupPOI = new ButtonGroup();
    JRadioButton POI36 = new JRadioButton("POI-36");
    JRadioButton POI121 = new JRadioButton("POI-121");
    JRadioButton POI441 = new JRadioButton("POI-441");
    ButtonGroup group = new ButtonGroup();
    JRadioButton sensoryLosowo = new JRadioButton("Losowe");
    JRadioButton sensoryManualnie = new JRadioButton("Manualne");
    JRadioButton sensoryDeterministycznie = new JRadioButton("Deterministyczne");
	JButton startButton = new JButton("Start");
	JButton debugButton = new JButton("Debug");
	JLabel pokrycieLabel = new JLabel("Wymagane pokrycie POI: ");
	SpinnerModel modelPokrycie = new SpinnerNumberModel(0.8, 0.5, 1, 0.01); //default value,lower bound,upper bound,increment by
	JSpinner pokrycie = new JSpinner(modelPokrycie);
	Dane dane;

	PobranieDanych(List<Sensor> sensory, List<Poi> p, Dane d){

		super("Pobieranie parametrów");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setSize(new Dimension(szerokoscOkna, dlugoscOkna));
		this.setLocation(50,50);
		setLayout(new GridLayout(wiersze,kolumny));
		
		dane = d;
		this.sensory = sensory;
		poi = p;
		
		zasiegSensoraLabel.setBounds(0,0,szerokoscOkna/2, height);
		zasiegSensora.setBounds(szerokoscOkna/2,0, szerokoscOkna, height);
		
		add(liczbaSensorowLabel);
		add(liczbaSensorow);
		
		add(pojemnoscBateriiLabel);
		add(pojemnoscBaterii);
		
		add(zuzycieBateriiLabel);
		add(zuzycieBaterii);
		
		add(zasiegSensoraLabel);
		add(zasiegSensora);
		
		add(pokrycieLabel);
		add(pokrycie);
		
		add(CoffLabel);
		add(Coff);

		add(CLabel);
		add(C);
		
		add(ConLabel);
		add(Con);
		
		add(rozmieszczenieSensorowLabel);
;
        group.add(sensoryLosowo);
        group.add(sensoryManualnie);
        group.add(sensoryDeterministycznie);
 
        add(sensoryLosowo);
        add(sensoryManualnie);
        add(sensoryDeterministycznie);
        
		add(rozmieszczeniePOILabel);
 
        groupPOI.add(POI36);
        groupPOI.add(POI121);
        groupPOI.add(POI441);
 
        add(POI36);
        add(POI121);
        add(POI441);
 
        startButton.addActionListener(this);
        add(startButton);
        debugButton.addActionListener(this);
        add(debugButton);

        pack();
		
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		 
 		if(source == startButton) {
			inicjalizacjaDanych();
			Main.runSimulation(dane,false);
// 			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			setVisible(false); //you can't see me!
			dispose();
		}else if(source==debugButton)
		{
			initWithDebugData();
			Main.runSimulation(dane,true);

			setVisible(false); //you can't see me!
			dispose();

		}




	}

	private void initWithDebugData() {
		dane.setPromien(40);
		dane.setWariant(36);
		dane.setTrybSensory(0);
		dane.setC_offMinus(10);
		dane.setC_on(5);
		dane.setC_offPlus(7);
		dane.setQ(0.4);
		dane.setLiczbaSensorow(3);
	}

	public int konwerterRozmieszczeniePOI() {
		if (POI36.isSelected())
			return 36;
		else if (POI121.isSelected())
			return 121;
		else if (POI441.isSelected())
			return 441;
		else
			return -1; //nie wybrano żadnej z opcji
	}
	
	public int konwerterRozmieszczenieSensorow() {
		if (sensoryDeterministycznie.isSelected())
			return 0;
		else if (sensoryManualnie.isSelected())
			return 1;
		else if (sensoryLosowo.isSelected())
			return 2;
		else
			return -1; //nie wybrano żadnej z opcji
	}
	
	public void inicjalizacjaDanych() {
		dane.setWariant(konwerterRozmieszczeniePOI());
		dane.przeliczPoi();
		dane.setLiczbaSensorow((int) liczbaSensorow.getValue());
		dane.setPromien((int) zasiegSensora.getValue());
		dane.setTrybSensory(konwerterRozmieszczenieSensorow());
		System.out.println(konwerterRozmieszczenieSensorow());
		dane.setBateria((double) zuzycieBaterii.getValue());
		dane.setQ((double) pokrycie.getValue());
		dane.setPojemnoscBaterii((int) pojemnoscBaterii.getValue());


	}

	
	
}
