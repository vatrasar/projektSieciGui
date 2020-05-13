package UI;

import lab4.Dane;
import lab4.PobranieDanych;
import lab4.Poi;
import lab4.Sensor;

import javax.swing.*;
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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}
