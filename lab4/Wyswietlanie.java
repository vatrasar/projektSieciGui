package lab4;

import lab4.Node.Poi;
import lab4.Node.Sensor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

/**
 *
 */
public class Wyswietlanie  extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;


	JButton stepButton;
	/**
	 * Informacja o autorze (pole tekstowe).
	 */
	JLabel l;
	JLabel coverageRate;
	/**
	 */
	JPanel wys;
	/**
	 * Wysoko�c pola z autorem.
	 */
	int height;
	/**
	 * D�ugo�� GUI.
	 */
	int dlugoscOkna=650;
	/**
	 * Szeroko�� GUI.
	 */
	int szerokoscOkna=600;
	/**Wy�wietlenie GUI
	 */

	Simulation simulation;
	Wyswietlanie(List<Sensor> s , List<Poi> p, String author){
		super("Symulacja optymalizacji WSN");

		this.height=40;
		this.setSize(new Dimension(szerokoscOkna, dlugoscOkna));
		setLayout(null);
		l=new JLabel("Autor:"+author);
		l.setBounds(szerokoscOkna*3/5, 0, 350, height);
		add(l);
		stepButton = new JButton("Step");

		stepButton.addActionListener(this);
		add(stepButton);
		stepButton.setBounds(szerokoscOkna*4/5, 0, 80, height);

		coverageRate=new JLabel("Aktualny poziom pokrycia:");
		coverageRate.setBounds(szerokoscOkna/5, 0, 350, height);
		add(coverageRate);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);
		wys=new obraz(dlugoscOkna-100,szerokoscOkna,s,p);

		wys.setBounds(5, 60, szerokoscOkna, dlugoscOkna);
		add(wys);
		}

	public void setSimulation(Simulation simulation) {
		this.simulation = simulation;
	}

	public void aktualizacja(double coverageRate) {
		this.coverageRate.setText(String.format("Aktualny poziom pokrycia:%.2f",coverageRate));
		wys.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==stepButton)
		{
			synchronized(simulation) {
				simulation.notify();
			}
		}
	}
}