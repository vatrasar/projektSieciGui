package lab4;

import UI.Controller;
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
public class RozmieszczenieManualne  extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;


	JButton startButton;
	JLabel l;
	JPanel manual;
	int height;
	int dlugoscOkna=650;
	int szerokoscOkna=600;
	Simulation simulation;
	Dane d;
	List<Poi> p;
	List<Sensor> sensory;
	public RozmieszczenieManualne(List<Sensor> s, List<Poi> p, Dane d, String author,Controller controller){
		super("Symulacja optymalizacji WSN");
		this.height=40;
		this.setSize(new Dimension(szerokoscOkna, dlugoscOkna));
		setLayout(null);
		l=new JLabel("Autor:"+author);
		l.setBounds(szerokoscOkna*3/5, 0, 350, height);
		add(l);
		startButton = new JButton("Start");

		startButton.addActionListener(controller::endManualDeploy);
		add(startButton);
		startButton.setBounds(szerokoscOkna*4/5, 0, 80, height);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);
		manual=new obrazRozmieszczenieManualne(dlugoscOkna-100,szerokoscOkna,d,s,p);

		manual.setBounds(5, 60, szerokoscOkna, dlugoscOkna);
		add(manual);
		this.d = d;
		this.p = p;
		sensory = s;
		}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==startButton)
			{

				Main.runExperiment(d, false, p);
				setVisible(false); //you can't see me!
				dispose();
			}
	}
	
	public void skalowanieSensorow(){
		 for(Sensor s: sensory) {
			 s.setY((s.getY()-5)/5);
			 s.setX((s.getX()-5)/5);
		 }
	}

	public List<Sensor> getListOfSensors() {
		skalowanieSensorow();
		return sensory;
	}
}