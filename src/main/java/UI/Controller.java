package UI;

import lab4.*;
import lab4.Node.Poi;
import lab4.Node.Sensor;
import lab4.Utils.Utils;
import lab4.debug.Debug;
import lab4.debug.DebugSingSol;
import lab4.debug.DebugV3;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    int activeChartNumber;


    public Controller(LaSettingsView laSettingsView) {
        this.laSettingsView = laSettingsView;

        laSettingsFrame=new JFrame("Ustawienia algorytmu LA");
        this.laSettingsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.laSettingsView.btnSimulation.addActionListener(this);
        this.laSettingsView.btnDebugV3.addActionListener(this::makeDebugV3);
        this.laSettingsView.init();
        resultsPresentationView=new ResultsPresentationView();
        resultsPresentationView.comboStrategies.addActionListener(this::comboStrategieChanged);
        resultsPresentationView.btnMeanrewardChart.addActionListener(this::showMeanRewardChart);
        resultsPresentationView.btnActiveSensorsCharts.addActionListener(this::showActiveSensorsChart);
        resultsPresentationView.btnStrategiesCharts.addActionListener(this::showStrategiesChart);
        resultsPresentationView.btnDebug.addActionListener(this::actionDebug);
        resultsPresentationView.btnKStrategyChart.addActionListener(this::showKStrategiesChart);
        resultsPresentationView.spinRunNumber.addChangeListener(this::runNumberChanged);
        resultsPresentationView.btnRTSUsageChart.addActionListener(this::showRTSUsageChart);
        resultsPresentationView.btnCoveredPoiChart.addActionListener(this::showCoveredPoisRate);
        resultsPresentationView.btnSensorsReward.addActionListener(this::showSensorsRewardChart);
        resultsPresentationView.btnLocalCoverager.addActionListener(this::showLocalCoverageChart);
        resultsPresentationView.btnAlive.addActionListener(this::showAliveChart);

        resultsPresentationView.btnBack.addActionListener(this::showLaSettingsAgain);
        laSettingsView.btnBack.addActionListener(this::backDoBeginig);
        resultsPresentationView.btnQuit.addActionListener(this::quitFromProgram);
//        commonSettingsView.btnDebugSingSol.addActionListener(this);
        updateDahBoard();

//        this.laSettingsView.btnDebug.addActionListener(this::actionDebug);



    }

    private void quitFromProgram(ActionEvent actionEvent) {
        laSettingsFrame.setVisible(false);
        laSettingsFrame.dispose();
        commonSettingsView.setVisible(false);
        commonSettingsView.dispose();
    }

    private void backDoBeginig(ActionEvent actionEvent) {
        laSettingsFrame.setVisible(false);
        commonSettingsView.setVisible(true);
    }

    private void showLaSettingsAgain(ActionEvent actionEvent) {
        if(Math.abs(data.laData.getSumOfStrategiesProb()-1)>0.0001)
        {
            JOptionPane.showMessageDialog(null, "Suma prawdopodobieństw strategii różna od 1!");
            return;
        }
        statistics=new Statistics();


        laSettingsFrame.setContentPane(laSettingsView.mainPanel);
        laSettingsFrame.setSize(new Dimension(800, 1000));
    }

    private void makeDebugV3(ActionEvent actionEvent) {
        DebugV3 debugV3=new DebugV3();
        debugV3.makeDebuV3(data.getLiczbaSensorow()-1,laSettingsView.getLaData().maxK);
    }

    private void showAliveChart(ActionEvent actionEvent) {
        this.activeChartNumber=9;
        resultsPresentationView.setAliveChart(statistics);
    }

    private void showLocalCoverageChart(ActionEvent actionEvent) {
        resultsPresentationView.setLocalCoverage(statistics);
    }

    private void showSensorsRewardChart(ActionEvent actionEvent) {
        resultsPresentationView.setRewardSensorsChart(statistics);
    }

    private void showCoveredPoisRate(ActionEvent actionEvent) {
        this.activeChartNumber=5;
        this.resultsPresentationView.comboStrategies.setEnabled(false);
        resultsPresentationView.setCoveredPoiChart(statistics);
    }

    private void showRTSUsageChart(ActionEvent actionEvent) {
        this.activeChartNumber=4;
        this.resultsPresentationView.comboStrategies.setEnabled(false);
        resultsPresentationView.setRTSUsageChart(statistics);
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
            if(Math.abs(data.laData.getSumOfStrategiesProb()-1)>0.0001)
            {
                JOptionPane.showMessageDialog(null, "Suma prawdopodobieństw strategii różna od 1!");
                return;
            }
            statistics=new Statistics();


            laSettingsFrame.setVisible(false); //you can't see me!
            progressView=new ProgressView();
            laSettingsFrame.setContentPane(progressView.panel1);
//            laSettingsFrame.pack();


            Main.computeSolution(data,statistics,false,this,progressView,false);
            laSettingsFrame.setSize(new Dimension(400,200));
            laSettingsFrame.setVisible(true);
            Utils.saveFileWithSensorNumber(data.getListOfSensors());



        }else if(actionEvent.getSource()==resultsPresentationView.btnRepeat)
        {
            if(Math.abs(data.laData.getSumOfStrategiesProb()-1)>0.0001)
            {
                JOptionPane.showMessageDialog(null, "Suma prawdopodobieństw strategii różna od 1!");
                return;
            }
            statistics=new Statistics();


            laSettingsFrame.setVisible(false); //you can't see me!
            progressView=new ProgressView();
            laSettingsFrame.setContentPane(progressView.panel1);
//            laSettingsFrame.pack();


            Main.computeSolution(data,statistics,false,this,progressView,true);
            laSettingsFrame.setSize(new Dimension(400,200));
            laSettingsFrame.setVisible(true);
        }
        else if(commonSettingsView.btnDebugSingSol==actionEvent.getSource())
        {

            File file=getFileDirectory();
            if(file==null)
            {
                return;
            }
            commonSettingsView.inicjalizacjaDanych();
            data=commonSettingsView.getDane();
            showVisualisation();

            data.setListOfSensors(Utils.loadSensors(data,file));
            DebugSingSol debugSingSol=new DebugSingSol(data);
            debugSingSol.makeDebug();






        }

    }
    public File getFileDirectory()
    {
        File workingDirectory = new File(System.getProperty("user.dir"));

        final JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(workingDirectory);
        int returnVal = fc.showOpenDialog(commonSettingsView);
        File fileDirectory;

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            fileDirectory=fc.getSelectedFile();
            return fileDirectory;
            //This is where a real application would open the file.

        }else
        {
            return null;
        }
    }
    public void showChartView() {
        this.activeChartNumber=0;

//        resultsPresentationView.spinRunNumber.addChangeListener();

        laSettingsFrame.setLocation(700,300);
        laSettingsFrame.setContentPane(resultsPresentationView.mainPanel);
        resultsPresentationView.setMeanRewardChart(statistics,data);
        laSettingsFrame.pack();
        laSettingsFrame.setVisible(true);
    }

    private void runNumberChanged(ChangeEvent changeEvent) {
        if((int)resultsPresentationView.spinRunNumber.getValue()>statistics.getRunsStateList().size())
        {
            resultsPresentationView.spinRunNumber.setValue(statistics.getRunsStateList().size());
        }

        switch (activeChartNumber)
        {
            case 0:
                showMeanRewardChart(new ActionEvent(this,1,""));
                break;
            case 1:
                showActiveSensorsChart(new ActionEvent(this,1,""));
                break;
            case 2:
                showStrategiesChart(new ActionEvent(this,1,""));
                break;
            case 3:
                showKStrategiesChart(new ActionEvent(this,1,""));
                break;
            case 4:
                showRTSUsageChart(new ActionEvent(this,1,""));
                break;
            case 9:
                showAliveChart(new ActionEvent(this,1,""));
        }
    }

    private void showKStrategiesChart(ActionEvent actionEvent) {
        this.activeChartNumber=3;
        resultsPresentationView.strategiesKChart(statistics,data);
        updateDahBoard();

    }
    private void comboStrategieChanged(ActionEvent actionEvent) {
        resultsPresentationView.strategiesKChart(statistics,data);
    }



    private void showStrategiesChart(ActionEvent actionEvent) {
        this.activeChartNumber=2;
        resultsPresentationView.strategiesChart(statistics);
        updateDahBoard();
    }

    public void showActiveSensorsChart(ActionEvent actionEvent)
    {
        this.activeChartNumber=1;
        resultsPresentationView.showActiveSensorsChart(statistics);
        updateDahBoard();
    }
    public void actionDebug(ActionEvent actionEvent) {

        data.laData=laSettingsView.getLaData();
        Main.runExperiment(data,true,data.getListOfPoi());

//        laSettingsFrame.setVisible(false); //you can't see me!
//        laSettingsFrame.dispose();

//

    }


    public void showMeanRewardChart(ActionEvent actionEvent) {


        resultsPresentationView.setMeanRewardChart(statistics,data);
        updateDahBoard();

    }

    private void updateDahBoard() {
        if(activeChartNumber==3)
            resultsPresentationView.comboStrategies.setEnabled(true);
        else
            resultsPresentationView.comboStrategies.setEnabled(false);
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
        try {


            commonSettingsView.inicjalizacjaDanych();
            data = commonSettingsView.getDane();


            showVisualisation();

            int result = JOptionPane.showConfirmDialog((Component) null, "Do you want to show only nehs points?",
                    "alert", JOptionPane.YES_NO_OPTION);

            Debug.buildNetwork(data);
            Debug.createReward1File(data, result == 0 ? true : false);
            Debug.createReward2File(data, result == 0 ? true : false);
            showRewardDebugView();

        }
        catch (Exception e)
        {
            JOptionPane.showConfirmDialog((Component) null, "Error. Blank lines in input file?",
                    "alert", JOptionPane.DEFAULT_OPTION);

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



    private void showVisualisation() {
        if(data.areSensorsFromFile())
        {
            data.setListOfSensors(Main.getSensorsListFormFile(data.getFileWithSensors(),data.getPromien(),data.getPojemnoscBaterii()));
//            JOptionPane.showMessageDialog(null, "Przed użyciem debug musisz podać plik z kordynatami dla 5 sensorów");
//            throw new AppException("No file with sensors selected");
            data.setLiczbaSensorow(data.getListOfSensors().size());
        }
        else {
            data.setListOfSensors(getDefaultDebugSensorList(data.getPromien(),data.getPojemnoscBaterii()));
        }


//        if(data.getListOfSensors().size()!=5)
//        {
//
//            JOptionPane.showMessageDialog(null, "Musi byc dokładnie 5 sensorów w pliku dla trybu debug");
//            throw new AppException("number of sensors in file not equal with 5");
//        }
        List<Poi> poiList=new ArrayList<>();
        poi(poiList,data.getWariant());
        data.setListOfPoi(poiList);
//        visualisation=new Wyswietlanie(data.getListOfSensors(),poiList,"");
//        visualisation.stepButton.setVisible(false);

    }

    private List<Sensor> getDefaultDebugSensorList(int promien, int pojemnoscBaterii) {
        List<Sensor>sensors=new ArrayList<>();
        sensors.add(new Sensor(30.0,30.0,promien,pojemnoscBaterii));
        sensors.add(new Sensor(70.0,30.0,promien,pojemnoscBaterii));
        sensors.add(new Sensor(50.0,50.0,promien,pojemnoscBaterii));
        sensors.add(new Sensor(30.0,70.0,promien,pojemnoscBaterii));
        sensors.add(new Sensor(70.0,70.0,promien,pojemnoscBaterii));
        return sensors;
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
