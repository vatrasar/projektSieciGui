package lab4;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.awt.BorderLayout;
import javax.swing.*;

public class PobranieDanych extends JFrame {
	private static final long serialVersionUID = 1L;
	int dlugoscOkna=900;
	int szerokoscOkna=400;
	int height = 10;
	int wiersze = 2;
	int kolumny = 2;
	JPanel panel;
	JTextField pokrycie = new JTextField();
	JLabel pokrycieLabel = new JLabel("Wymagane pokrycie POI: ");
	JTextField pojemnoscBaterii = new JTextField();
	JLabel pojemnoscBateriiLabel = new JLabel("Pojemnoœæ baterii sensora: ");
	JTextField zasiegSensora = new JTextField();
	JLabel zasiegSensoraLabel = new JLabel("Zasiêg sensora: ");
	JTextField liczbaSensorow = new JTextField();
	JLabel liczbaSensorowLabel = new JLabel("Liczba sensorów: ");
	JLabel rozmieszczenieSensorowLabel = new JLabel("Rozmieszczenie sensorów: ");
	PobranieDanych(){
		super("Pobieranie parametrów");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setSize(new Dimension(szerokoscOkna, dlugoscOkna));
		this.setLocation(50,50);
		setLayout(new GridLayout(wiersze,kolumny));
		
		zasiegSensoraLabel.setBounds(0,0,szerokoscOkna/2, height);
		zasiegSensora.setBounds(szerokoscOkna/2,0, szerokoscOkna, height);
		
		add(liczbaSensorowLabel);
		add(liczbaSensorow);
		
		add(pojemnoscBateriiLabel);
		add(pojemnoscBaterii);
		
		add(zasiegSensoraLabel);
		add(zasiegSensora);
		
		add(pokrycieLabel);
		add(pokrycie);
		
		add(rozmieszczenieSensorowLabel);
        JRadioButton sensoryLosowo = new JRadioButton("Losowe");
        JRadioButton sensoryManualnie = new JRadioButton("Manualne");
        JRadioButton sensoryDeterministycznie = new JRadioButton("Deterministyczne");
 
        ButtonGroup group = new ButtonGroup();
        group.add(sensoryLosowo);
        group.add(sensoryManualnie);
        group.add(sensoryDeterministycznie);
 
        add(sensoryLosowo);
        add(sensoryManualnie);
        add(sensoryDeterministycznie);
 
        pack();
		
		
		setVisible(true);
	}
}