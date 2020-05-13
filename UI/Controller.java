package UI;

import lab4.Dane;
import lab4.PobranieDanych;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {

    LaSettingsView laSettingsView;
    PobranieDanych commonSettingsView;
    JFrame laSettingsFrame;

    Dane data;
    public Controller(LaSettingsView laSettingsView) {
        this.laSettingsView = laSettingsView;
        laSettingsFrame=new JFrame("Ustawienia algorytmu LA");

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

//        this.frame.setContentPane(laSettingsView.mainPanel);
        if(actionEvent.getSource()==commonSettingsView.startButton) {
            commonSettingsView.setVisible(false);
            data = commonSettingsView.getDane();
            laSettingsFrame.setContentPane(laSettingsView.mainPanel);
            laSettingsFrame.setLocation(700,300);
//            laSettingsFrame.setSize(700,700);
            laSettingsFrame.pack();
            laSettingsFrame.setVisible(true);
        }
    }

    public void setCommonSettingsView(PobranieDanych frame) {

        this.commonSettingsView = frame;
    }
}
