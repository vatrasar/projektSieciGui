package UI;





import lab4.Dane;
import lab4.Node.Poi;
import lab4.Node.Sensor;
import lab4.PobranieDanych;


import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;

import java.util.ArrayList;

public class UiThread extends Thread {

    Controller controller;
    @Override
    public void run() {
        setLookAndFeel();
        LaSettingsView laSettingsView =new LaSettingsView();
        controller=new Controller(laSettingsView);
        JFrame frame=new PobranieDanych(new ArrayList<Sensor>(),new ArrayList<Poi>(),new Dane(),controller);

//        frame.setSize(200,200);


//        frame.setContentPane(commonSettingsView.mainPanel);
        frame.setVisible(true);


    }

    private void setLookAndFeel() {

//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.getFont("Label.font");

        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
