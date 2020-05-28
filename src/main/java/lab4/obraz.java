package lab4;

import lab4.Node.Poi;
import lab4.Node.Sensor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.List;

import javax.swing.JPanel;

	/** Wy�wietlenie .
	 * 
	 * 
	 *
	 */

	public class obraz extends JPanel{
		int szerokoscOkna;
		List<Sensor> sensory;
		List<Poi> p;
		int liczbaManualnychSensorow;
		private int dlugosc;
		obraz(int a, int b, List<Sensor> s , List<Poi> p){
			this.szerokoscOkna=a;
			this.dlugosc=b;
			this.setSize(new Dimension(a, b));
			this.sensory=s;
			this.p=p;
		}
		/**
		 *Rysowanie 
		 */
		protected void paintComponent(Graphics g) {
			 super.paintComponent(g);
			 Graphics2D g2d = (Graphics2D) g;
			 g2d.setColor(Color.black);
			 //rysowanie POI
			 for(Poi s: p) {
				 drawCircle(g2d, (s.getX()*5)+5, (s.getY()*5)+5, 1) ;
			 }
			 //rysowanie sensorów
			 for(Sensor s: sensory) {
				 if(s.getStan()==0) {
					 g2d.setColor(Color.red);
				 }else if(s.getStan()==1) {
					 g2d.setColor(Color.green);
				 }else {
					 g2d.setColor(Color.orange);
				 }
				 drawCircle(g2d, (s.getX()*5)+5, (s.getY()*5)+5, 1) ;
				 drawCircle(g2d, (s.getX()*5)+5, (s.getY()*5)+5, s.getPromien()*5) ;
			 }
		}
	
		
	    public static void drawCircle(Graphics2D graphics, double x, double y, double radius) {
	        Shape circle = new Ellipse2D.Double(x - radius, y - radius, radius * 2.0, radius * 2.0);
	        graphics.draw(circle);/*from  ww  w.  ja  va2 s  .c  om*/
	    }
	}