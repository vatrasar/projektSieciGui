package UI;

import lab4.*;
import lab4.Node.Poi;
import lab4.Utils.AppException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static lab4.Main.getSensorsList;
import static lab4.Main.poi;

public class Controller implements ActionListener {

    LaSettingsView laSettingsView;
    PobranieDanych commonSettingsView;
    JFrame laSettingsFrame;
    ResultsPresentationView resultsPresentationView;
    Statistics statistics;
    Wyswietlanie visualisation;
    RewardDebug rewardDebug;
    Dane data;
    RozmieszczenieManualne manual;
    ProgressView progressView;


    public Controller(LaSettingsView laSettingsView) {
        this.laSettingsView = laSettingsView;
        laSettingsFrame=new JFrame("Ustawienia algorytmu LA");
        this.laSettingsView.btnSimulation.addActionListener(this);
//        this.laSettingsView.btnDebug.addActionListener(this::actionDebug);



    }


    public void actionPerformed(ActionEvent actionEvent) {

//        this.frame.setContentPane(laSettingsView.mainPanel);
        if(actionEvent.getSource()==commonSettingsView.startButton) {
            commonSettingsView.inicjalizacjaDanych();
            commonSettingsView.setVisible(false);
            data = commonSettingsView.getDane();

            List<Poi> p = new ArrayList<Poi>();

            //zapis wsp�lrzednych sensor�w i POI
            poi(p,data.getWariant()); //wsp�lrzedne POI
            //zapis sensor�w


            int t=0;
            //algorytm

            data.setListOfPoi(p);


            if(data.getTrybSensory()==1)
            {
                manual = new RozmieszczenieManualne(new ArrayList<>(), p, data, "",this);
                manual.repaint();
            }
            else
                showLaSettings();

        }else if(actionEvent.getSource()==laSettingsView.btnSimulation)
        {
            data.laData=laSettingsView.getLaData();
            statistics=new Statistics();


            laSettingsFrame.setVisible(false); //you can't see me!
            progressView=new ProgressView();
            laSettingsFrame.setContentPane(progressView.panel1);
//            laSettingsFrame.pack();


            Main.computeSolution(data,statistics,false,this,progressView);
            laSettingsFrame.setSize(new Dimension(400,200));
            laSettingsFrame.setVisible(true);



        }

    }

    public void showChartView() {
        resultsPresentationView=new ResultsPresentationView();
        resultsPresentationView.btnMeanrewardChart.addActionListener(this::actionDebug);
        resultsPresentationView.btnActiveSensorsCharts.addActionListener(this::showActiveSensorsChart);
        resultsPresentationView.btnStrategiesCharts.addActionListener(this::showStrategiesChart);
        resultsPresentationView.btnDebug.addActionListener(this::actionDebug);
        resultsPresentationView.btnKStrategyChart.addActionListener(this::showKStrategiesChart);

        laSettingsFrame.setLocation(700,300);
        laSettingsFrame.setContentPane(resultsPresentationView.mainPanel);
        resultsPresentationView.setMeanRewardChart(statistics);
        laSettingsFrame.pack();
        laSettingsFrame.setVisible(true);
    }

    private void showKStrategiesChart(ActionEvent actionEvent) {
        resultsPresentationView.strategiesKChart(statistics);
        resultsPresentationView.comboStrategies.addActionListener(this::comboStrategieChanged);
    }
    private void comboStrategieChanged(ActionEvent actionEvent) {
        resultsPresentationView.strategiesKChart(statistics);
    }



    private void showStrategiesChart(ActionEvent actionEvent) {
        resultsPresentationView.strategiesChart(statistics);
    }

    public void showActiveSensorsChart(ActionEvent actionEvent)
    {
        resultsPresentationView.showActiveSensorsChart(statistics);
    }
    public void actionDebug(ActionEvent actionEvent) {

        data.laData=laSettingsView.getLaData();
        Main.runExperiment(data,true,data.getListOfPoi());

        laSettingsFrame.setVisible(false); //you can't see me!
        laSettingsFrame.dispose();

//

    }
    public void showMeanRewardChart(ActionEvent actionEvent) {


        resultsPresentationView.setMeanRewardChart(statistics);

    }

    private void showLaSettings() {
        laSettingsFrame.setContentPane(laSettingsView.mainPanel);
        laSettingsFrame.setLocation(700,300);
//            laSettingsFrame.setSize(700,700);
        laSettingsFrame.pack();
        laSettingsFrame.setVisible(true);
    }

    public void startRewardDebug(ActionEvent actionEvent)
    {
        commonSettingsView.inicjalizacjaDanych();
        data=commonSettingsView.getDane();

        try {
            showVisualisation();

            Debug.buildNetwork(data);
            Debug.createReward1File(data);
            Debug.createReward2File(data);
            showRewardDebugView();
        } catch (AppException ignored) {

        }



    }

    private void showRewardDebugView() {
        rewardDebug=new RewardDebug();
        laSettingsFrame.setContentPane(rewardDebug.panel1);

        laSettingsFrame.setVisible(true);
        laSettingsFrame.pack();
        rewardDebug.switch0.addActionListener(this::switchState);
        rewardDebug.switch1.addActionListener(this::switchState);
        rewardDebug.switch2.addActionListener(this::switchState);
        rewardDebug.switch3.addActionListener(this::switchState);
        rewardDebug.switch4.addActionListener(this::switchState);
        updateReward();
        updateStateLabels();
        updatePoisCoverage();
    }

    private void updatePoisCoverage() {
        rewardDebug.labPoiCoverage0.setText(data.getListOfSensors().get(0).getPoiCoverageString());
        rewardDebug.labPoiCoverage1.setText(data.getListOfSensors().get(1).getPoiCoverageString());
        rewardDebug.labPoiCoverage2.setText(data.getListOfSensors().get(2).getPoiCoverageString());
        rewardDebug.labPoicoverage3.setText(data.getListOfSensors().get(3).getPoiCoverageString());
        rewardDebug.labPoiCoverage4.setText(data.getListOfSensors().get(4).getPoiCoverageString());
    }

    private void switchState(ActionEvent actionEvent)
    {
        Object soruce=actionEvent.getSource();
        if(soruce==rewardDebug.switch0)
        {

            data.getListOfSensors().get(0).switchState();


        }
        if(soruce==rewardDebug.switch1)
        {

            data.getListOfSensors().get(1).switchState();


        }
        if(soruce==rewardDebug.switch2)
        {

            data.getListOfSensors().get(2).switchState();


        }
        if(soruce==rewardDebug.switch3)
        {

            data.getListOfSensors().get(3).switchState();



        }
        if(soruce==rewardDebug.switch4)
        {
            if(data.getListOfSensors().size()>=5) {
                data.getListOfSensors().get(4).switchState();


            }
        }
        updateReward();
        updateStateLabels();
        updatePoisCoverage();
        visualisation.aktualizacja(computeCoverRate());


    }

    private void updateStateLabels() {

        rewardDebug.labState0.setText(data.getListOfSensors().get(0).getStan()+"");
        rewardDebug.labState1.setText(data.getListOfSensors().get(1).getStan()+"");
        rewardDebug.labState2.setText(data.getListOfSensors().get(2).getStan()+"");
        rewardDebug.labState3.setText(data.getListOfSensors().get(3).getStan()+"");
        rewardDebug.labState4.setText(data.getListOfSensors().get(4).getStan()+"");
    }

    private void updateReward() {

        rewardDebug.labReward0.setText(String.format("%.2f",data.getListOfSensors().get(0).computeReword(data, data.getListOfSensors())));
        rewardDebug.labReward1.setText(String.format("%.2f",data.getListOfSensors().get(1).computeReword(data, data.getListOfSensors())));
        rewardDebug.labReward2.setText(String.format("%.2f",data.getListOfSensors().get(2).computeReword(data, data.getListOfSensors())));
        rewardDebug.labReward3.setText(String.format("%.2f",data.getListOfSensors().get(3).computeReword(data, data.getListOfSensors())));
        rewardDebug.labReward4.setText(String.format("%.2f",data.getListOfSensors().get(4).computeReword(data, data.getListOfSensors())));
    }



    private void showVisualisation() throws AppException {
        if(!data.areSensorsFromFile())
        {
            JOptionPane.showMessageDialog(null, "Przed użyciem debug musisz podać plik z kordynatami dla 5 sensorów");
            throw new AppException("No file with sensors selected");
        }

        data.setListOfSensors(Main.getSensorsListFormFile(data.getFileWithSensors(),data.getPromien(),data.getPojemnoscBaterii()));
        if(data.getListOfSensors().size()!=5)
        {

            JOptionPane.showMessageDialog(null, "Musi byc dokładnie 5 sensorów w pliku dla trybu debug");
            throw new AppException("number of sensors in file not equal with 5");
        }
        List<Poi> poiList=new ArrayList<>();
        poi(poiList,data.getWariant());
        data.setListOfPoi(poiList);
        visualisation=new Wyswietlanie(data.getListOfSensors(),poiList,"");
        visualisation.stepButton.setVisible(false);

    }

    public void setCommonSettingsView(PobranieDanych frame) {

       commonSettingsView=frame;
    }
    private double computeCoverRate() {
        int coveredPois=0;
        for(Poi poi:data.getListOfPoi())
        {
            if(poi.coveringSensorsList.stream().anyMatch(x->x.stan==1))
            {
                coveredPois++;
            }
        }
        return ((double) coveredPois)/data.getListOfPoi().size();

    }

    public  void endManualDeploy(ActionEvent actionEvent) {

        data.setListOfSensors(manual.getListOfSensors());

        manual.setVisible(false);
        manual.dispose();
        showLaSettings();
    }
}
