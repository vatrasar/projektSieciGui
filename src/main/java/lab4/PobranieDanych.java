package lab4;

import UI.Controller;

import lab4.Node.Poi;
import lab4.Node.Sensor;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.*;

public class PobranieDanych extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	int dlugoscOkna=1500;
	int szerokoscOkna=1500;
	int height = 10;
	int wiersze = 20;
	int kolumny = 2;



	List<Sensor> sensory;
	List<Poi> poi;
	JPanel panel;
	JLabel ConLabel = new JLabel("Con: ");
	JSpinner Con = new JSpinner(new SpinnerNumberModel(1, 0, 100, 0.1));
	JLabel CoffLabel1 = new JLabel("Coff + : ");
	JSpinner Coff1 = new JSpinner(new SpinnerNumberModel(1, -100, 100, 0.1));


	JLabel labC1 = new JLabel("C1");
	JSpinner spinC1 = new JSpinner(new SpinnerNumberModel(0.5, -100, 100, 0.1));

	JLabel labC2 = new JLabel("C2");
	JSpinner spinC2 = new JSpinner(new SpinnerNumberModel(4, -100, 100, 0.1));

	JLabel labC3 = new JLabel("C3");
	JSpinner spinC3 = new JSpinner(new SpinnerNumberModel(0.5, -100, 100, 0.1));

	JLabel labC4 = new JLabel("C4");
	JSpinner spinC4 = new JSpinner(new SpinnerNumberModel(0.8, -100, 100, 0.1));

	JLabel labDelta2 = new JLabel("delta2");
	JSpinner spinDelta2 = new JSpinner(new SpinnerNumberModel(0.2, -100, 100, 0.1));


//	JLabel CoffLabel2 = new JLabel("Coff - : ");
//	JSpinner Coff2 = new JSpinner(new SpinnerNumberModel(-10, -100, 100, 0.1));
	JLabel CLabel = new JLabel("C: ");
	JSpinner C = new JSpinner(new SpinnerNumberModel(0.5, 0, 100, 0.1));
	JSpinner pojemnoscBaterii = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
	JLabel pojemnoscBateriiLabel = new JLabel("Sensors batttery cappacity: ");
	JSpinner zuzycieBaterii = new JSpinner(new SpinnerNumberModel(1, 0, 100, 0.01));
	JLabel zuzycieBateriiLabel = new JLabel("Battery usage in 1 moment of time: ");
	JSpinner zasiegSensora = new JSpinner(new SpinnerNumberModel(35, 1, 50, 1));
	JLabel zasiegSensoraLabel = new JLabel("Sensor range: ");
	JSpinner liczbaSensorow = new JSpinner(new SpinnerNumberModel(5, 0, 1000, 1));
	JLabel liczbaSensorowLabel = new JLabel("Number of sensors: ");
	JLabel rozmieszczenieSensorowLabel = new JLabel("Sensor placement: ");
	JLabel rozmieszczeniePOILabel = new JLabel("POI placement ");

    ButtonGroup groupPOI = new ButtonGroup();
    JRadioButton POI36 = new JRadioButton("POI-36");
    JRadioButton POI121 = new JRadioButton("POI-121");
    JRadioButton POI441 = new JRadioButton("POI-441");
    ButtonGroup group = new ButtonGroup();
    JRadioButton sensoryLosowo = new JRadioButton("Randomly");
    JRadioButton sensoryManualnie = new JRadioButton("Manually");
    JRadioButton sensoryDeterministycznie = new JRadioButton("Deterministically");

	public JButton startButton = new JButton("Next");
	JButton debugButton = new JButton("Debug-0");
	JLabel pokrycieLabel = new JLabel("Q: ");
	SpinnerModel modelPokrycie = new SpinnerNumberModel(0.8, 0.0, 1, 0.01); //default value,lower bound,upper bound,increment by
	JSpinner pokrycie = new JSpinner(modelPokrycie);
	public Dane dane;
	JLabel areSensorsFromFileLabel = new JLabel("Sensors from file: ");
	JCheckBox areSensorsFromFileCheckBox=new JCheckBox("", false);
	Controller controller;

	public Dane getDane() {
		return dane;
	}

	public PobranieDanych(List<Sensor> sensory, List<Poi> p, Dane d, Controller controller){

		super("Pobieranie parametrów");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setSize(new Dimension(szerokoscOkna, dlugoscOkna));
		this.setLocation(50,50);
		setLayout(new GridLayout(wiersze,kolumny));
		this.controller=controller;
		this.controller.setCommonSettingsView(this);
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
		
		
		add(CLabel);
		add(C);
		
		add(ConLabel);
		add(Con);
		
		add(CoffLabel1);
		add(Coff1);
		
//		add(CoffLabel2);
//		add(Coff2);


		add(labC1);
		add(spinC1);

		add(labC2);
		add(spinC2);
//
		add(labC3);
		add(spinC3);
//
		add(labC4);
		add(spinC4);

		add(labDelta2);
		add(spinDelta2);
		
		add(rozmieszczenieSensorowLabel);

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

		add(areSensorsFromFileLabel);
        add(areSensorsFromFileCheckBox);
        areSensorsFromFileCheckBox.addActionListener(this);

 
        startButton.addActionListener(controller);
		debugButton.addActionListener(controller::startRewardDebug);
        add(startButton);
        add(debugButton);

		sensoryLosowo.setSelected(true);
		POI36.setSelected(true);
        pack();
		
//		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		 
 		if(source==areSensorsFromFileCheckBox)
		{
			if(areSensorsFromFileCheckBox.isSelected())
			{
//				sensoryLosowo.setEnabled(false);
//				sensoryManualnie.setEnabled(false);
//				sensoryDeterministycznie.setEnabled(false);
				File workingDirectory = new File(System.getProperty("user.dir"));

				final JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(workingDirectory);
				int returnVal = fc.showOpenDialog(this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					dane.setFileWithSensors(fc.getSelectedFile());
					dane.setAreSensorsFromFile(true);

					//This is where a real application would open the file.

				} else {
					areSensorsFromFileCheckBox.setSelected(false);
					sensoryLosowo.setEnabled(true);
					sensoryManualnie.setEnabled(true);
					sensoryDeterministycznie.setEnabled(true);
				}

			}
			else {
				sensoryLosowo.setEnabled(true);
				sensoryManualnie.setEnabled(true);
				sensoryDeterministycznie.setEnabled(true);
				dane.setAreSensorsFromFile(false);
			}
		}




	}

	private void initWithDebugData() {
//		dane.setPromien(40);
		dane.setWariant(6);
		dane.setTrybSensory(0);
//		dane.setC_offMinus(10);
//		dane.setC_on(5);
//		dane.setC_offPlus(7);
//		dane.setQ(0.4);
		dane.setLiczbaSensorow(3);
		dane.setAreSensorsFromFile(false);
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
		dane.setC((double)C.getValue());
		dane.setC_on((double)Con.getValue());
//		dane.setC_offMinus((double)Coff2.getValue());
		dane.setC_offPlus((double)Coff1.getValue());


		dane.setC1((double)spinC1.getValue());
		dane.setC2((double)spinC2.getValue());
		dane.setC3((double)spinC3.getValue());
		dane.setC4((double)spinC4.getValue());
		dane.setDelta2((double)spinDelta2.getValue());



	}

	
	
}
