package UI;

import lab4.Dane;
import lab4.Statistics;
import lab4.Main;
import lab4.PobranieDanych;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {

    LaSettingsView laSettingsView;
    PobranieDanych commonSettingsView;
    JFrame laSettingsFrame;
    ResultsPresentationView resultsPresentationView;
    Statistics statistics;

    Dane data;


    public Controller(LaSettingsView laSettingsView) {
        this.laSettingsView = laSettingsView;
        laSettingsFrame=new JFrame("Ustawienia algorytmu LA");
        this.laSettingsView.btnSimulation.addActionListener(this);
        this.laSettingsView.btnDebug.addActionListener(this::actionDebug);

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

//        this.frame.setContentPane(laSettingsView.mainPanel);
        if(actionEvent.getSource()==commonSettingsView.startButton) {
            commonSettingsView.inicjalizacjaDanych();
            commonSettingsView.setVisible(false);
            data = commonSettingsView.getDane();
            showLaSettings();
        }else if(actionEvent.getSource()==laSettingsView.btnSimulation)
        {
            data.laData=laSettingsView.getLaData();
            statistics=new Statistics();


            laSettingsFrame.setVisible(false); //you can't see me!
            Main.computeSolution(data,statistics,false,this);



        }

    }

    public void showChartView() {
        resultsPresentationView=new ResultsPresentationView();
        resultsPresentationView.btnMeanrewardChart.addActionListener(this::showMeanRewardChart);
        laSettingsFrame.setLocation(700,300);
        laSettingsFrame.setContentPane(resultsPresentationView.mainPanel);
        resultsPresentationView.setMeanRewardChart(statistics);
        laSettingsFrame.pack();
        laSettingsFrame.setVisible(true);
    }

    public void actionDebug(ActionEvent actionEvent) {

        data.laData=laSettingsView.getLaData();
        Main.computeSolution(data, statistics, true,this);

        laSettingsFrame.setVisible(false); //you can't see me!
        laSettingsFrame.dispose();

//

    }
    public void showMeanRewardChart(ActionEvent actionEvent) {


        resultsPresentationView.showChart();

    }

    private void showLaSettings() {
        laSettingsFrame.setContentPane(laSettingsView.mainPanel);
        laSettingsFrame.setLocation(700,300);
//            laSettingsFrame.setSize(700,700);
        laSettingsFrame.pack();
        laSettingsFrame.setVisible(true);
    }

    public void setCommonSettingsView(PobranieDanych frame) {

        this.commonSettingsView = frame;
    }
}
