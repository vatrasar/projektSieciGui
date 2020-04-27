package lab4;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

	/** Wy�wietlenie .
	 * 
	 * 
	 *
	 */

	public class obraz extends JPanel implements MouseListener, MouseMotionListener{
		int szerokoscOkna;
		List<Sensor> sensory;
		List<Poi> p;
		int liczbaManualnychSensorow;
		private int dlugosc;
		obraz(int a,int b, List<Sensor> s , List<Poi> p){
	 		addMouseListener(this);
	 		addMouseMotionListener(this);
	 		liczbaManualnychSensorow=0;
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
	    
	 	@Override
	 	public void mouseDragged(MouseEvent arg0) {
	 		System.out.println("mouseDragged");
	 	}
	 
	 	@Override
	 	public void mouseMoved(MouseEvent arg0) {
	 		System.out.println("mouseMoved");
	 	}
	 
	 	@Override
	 	public void mouseClicked(MouseEvent e) {
	 		int x = e.getX();
	 		int y = e.getY();
	 		if (liczbaManualnychSensorow)
	 		sensory.add(arg0)
	 		.add(new Point(x, y));
	 		repaint();
	 	}
	 
	 	@Override
	 	public void mouseEntered(MouseEvent e) {
	 		System.out.println("mouseEntered");
	 	}
	 
	 	@Override
	 	public void mouseExited(MouseEvent e) {
	 		System.out.println("mouseExited");
	 	}
	 
	 	@Override
	 	public void mousePressed(MouseEvent e) {
	 		System.out.println("mousePressed");
	 	}
	 
	 	@Override
	 	public void mouseReleased(MouseEvent e) {
	 		System.out.println("mouseReleased");
	 	}
	 	
	 	public void rozmieszczenieManualne() {
	 		
	 	}
	}