package lab4;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.List;

import javax.swing.JPanel;
 
 public class obrazRozmieszczenieManualne extends JPanel implements MouseListener, MouseMotionListener {
 
	 int liczbaSensorow;
	 int liczbaUstawionychSensorow;
	 List<Sensor> sensory;
	 List<Poi> p;
	 int promien;
	 int batteryCappacity;
 	public obrazRozmieszczenieManualne(int a, int b, Dane d, List<Sensor> sensory, List<Poi> p) {
 		addMouseListener(this);
 		addMouseMotionListener(this);
 		this.setSize(new Dimension(a, b));
 		liczbaUstawionychSensorow = 0;
 		liczbaSensorow = d.liczbaSensorow; 
 		this.sensory = sensory;
 		this.promien = d.promien;
 		this.p = p;
 		this.batteryCappacity=d.pojemnoscBaterii;

 	}
 
 	@Override
 	public void mouseDragged(MouseEvent arg0) {
 	}
 
 	@Override
 	public void mouseMoved(MouseEvent arg0) {
 	}
 
 	@Override
 	public void mouseClicked(MouseEvent e) {
 		double x = e.getX();
 		double y = e.getY();
 		if(liczbaUstawionychSensorow < liczbaSensorow) {
 			sensory.add(new Sensor(x, y, promien,batteryCappacity));
 			repaint();
 			liczbaUstawionychSensorow++;
 		}
 	}
 
 	@Override
 	public void mouseEntered(MouseEvent e) {
 	}
 
 	@Override
 	public void mouseExited(MouseEvent e) {
 	}
 
 	@Override
 	public void mousePressed(MouseEvent e) {
 	}
 
 	@Override
 	public void mouseReleased(MouseEvent e) {
 	}
 	
 	@Override
 	protected void paintComponent(Graphics g) {
		 super.paintComponent(g);
		 Graphics2D g2d = (Graphics2D) g;
		 g2d.setColor(Color.black);
		 //rysowanie POI
		 for(Poi s: p) {
			 drawCircle(g2d, (s.getX()*5)+5, (s.getY()*5)+5, 1) ;
		 }
		 //rysowanie sensor�w
		 for(Sensor s: sensory) {
			g2d.setColor(Color.blue);
			drawCircle(g2d, s.getX(), (s.getY()), 2);
			drawCircle(g2d, s.getX(), s.getY(), s.getPromien()*5);
		 }
 	}
 
    public static void drawCircle(Graphics2D graphics, double x, double y, double radius) {
        Shape circle = new Ellipse2D.Double(x - radius, y - radius, radius * 2.0, radius * 2.0);
        graphics.draw(circle);/*from  ww  w.  ja  va2 s  .c  om*/
    }
 
 }