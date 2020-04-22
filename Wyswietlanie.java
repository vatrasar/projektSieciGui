package lab4;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import static java.lang.Math.*;

/**
 *
 */
public class Wyswietlanie  extends JFrame  {
	private static final long serialVersionUID = 1L;
	/**
	 * Informacja o autorze (pole tekstowe).
	 */
	JLabel l;
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
	int dlugoscOkna=1000;
	/**
	 * Szeroko�� GUI.
	 */
	int szerokoscOkna=1000;
	/**Wy�wietlenie GUI
	 */
	Wyswietlanie(List<Sensor> s , List<Poi> p){
		super("Symulacja optymalizacji WSN");
		this.height=10;
		this.setSize(new Dimension(szerokoscOkna, dlugoscOkna));
		setLayout(null);
		l=new JLabel("Autor:");
		l.setBounds(szerokoscOkna/3, 0, 350, height);
		add(l);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);
		wys=new obraz(dlugoscOkna-100,szerokoscOkna,s,p);
		wys.setBounds(5, 20, szerokoscOkna, dlugoscOkna-110);
		add(wys);
		}
	public void aktualizacja() {
		wys.repaint();
	}
}