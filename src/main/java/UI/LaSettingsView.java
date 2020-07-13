package UI;

import lab4.Dane;
import lab4.La.LaData;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LaSettingsView extends JFrame {
    public JPanel mainPanel;

    private JPanel navigationPanel;
    public JButton btnSimulation;
    private JPanel commonParametresPanel;
    private JSpinner spinMaxIterationsNumber;
    private JLabel maks;
    private JSpinner spinMaxRunsNumber;
    private JCheckBox checkIsScheduleSearch;
    private JCheckBox checkIsStopCondition;
    private JSpinner spinDeltaStop;
    private JSpinner spinU;
    private JPanel initPropertiesPanel;
    private JSpinner spinAllC;
    private JSpinner spinKC;
    private JSpinner spinKD;
    private JSpinner spinKDC;
    private JSpinner spinMaxK;
    private JSpinner spinProbSensorOn;
    private JPanel laParametersPanel;
    private JSpinner spinH;
    private JSpinner spinEpslion;
    private JRadioButton radioEvolutionary;
    private JRadioButton radioAdoptToBest;
    private JRadioButton tagRTSRadioButton;
    private JRadioButton radioChanngeStrategyTypeRTSPlusStrategy;
    private JSpinner spinPReadyToShare;
    public JButton btnDebug;
    private JCheckBox checkIsRTS;
    public JSpinner spinAllD;
    public JRadioButton radioStrategyCompetition;
    public JButton btnDebugV3;
    private JSpinner spinRunNumber;
    ButtonGroup strategyChangeTypeButtonGroup;
    ButtonGroup rangeOfstrategyChangeButtonGroup;


    public LaData getLaData() {

        LaData laData=new LaData((int)spinMaxIterationsNumber.getValue(),(int)spinMaxRunsNumber.getValue(),checkIsScheduleSearch.isSelected(),false,0, (int)spinU.getValue(),(double)spinAllC.getValue(),(double)spinAllD.getValue(),(double)spinKC.getValue(),(double)spinKD.getValue(),(double)spinKDC.getValue(),(int)spinMaxK.getValue(),(double)spinProbSensorOn.getValue(),(int)spinH.getValue(),(double)spinEpslion.getValue(),radioEvolutionary.isSelected(),radioChanngeStrategyTypeRTSPlusStrategy.isSelected(),(double)spinPReadyToShare.getValue(),checkIsRTS.isSelected(),radioStrategyCompetition.isSelected(),(int)spinRunNumber.getValue());
        return laData;
    }

    private void createUIComponents() {

        createSpinners();

    }

    private void createSpinners() {
        spinAllC=new JSpinner(new SpinnerNumberModel(0.0,0 ,1.0,0.1));
        spinKC=new JSpinner(new SpinnerNumberModel(0.0,0 ,1.0,0.1));
        spinKD=new JSpinner(new SpinnerNumberModel(0.0,0 ,1.0,0.1));
        spinKDC=new JSpinner(new SpinnerNumberModel(0.0,0 ,1.0,0.1));
        spinEpslion=new JSpinner(new SpinnerNumberModel(0.0,0 ,1.0,0.1));
        spinProbSensorOn=new JSpinner(new SpinnerNumberModel(0.0,0 ,1.0,0.1));
        spinPReadyToShare=new JSpinner(new SpinnerNumberModel(0.0,0 ,1.0,0.1));
        spinMaxIterationsNumber=new JSpinner(new SpinnerNumberModel(0,0 ,10000,1));
        spinMaxRunsNumber=new JSpinner(new SpinnerNumberModel(0,0 ,10000,1));
        spinH=new JSpinner(new SpinnerNumberModel(0,0 ,10000,1));
        spinDeltaStop=new JSpinner(new SpinnerNumberModel(0,0 ,10000,1));
        spinU=new JSpinner(new SpinnerNumberModel(0,0 ,10000,1));
        spinMaxK=new JSpinner(new SpinnerNumberModel(0,0 ,10000,1));
        spinAllD=new JSpinner(new SpinnerNumberModel(0.0,0 ,1,0.05));
        spinRunNumber=new JSpinner(new SpinnerNumberModel(1,0 ,10000,1));
    }

    public LaSettingsView() throws HeadlessException {


    }
    public void init()
    {
        formRadioButtonGroups();
        radioAdoptToBest.setSelected(true);
        radioChanngeStrategyTypeRTSPlusStrategy.setSelected(true);
        spinAllC.setValue(0.2);
        spinKDC.setValue(0.2);
        spinKC.setValue(0.2);
        spinKD.setValue(0.2);
        spinAllD.setValue(0.2);
        spinMaxK.setValue(3);
        spinProbSensorOn.setValue(0.5);
        spinMaxRunsNumber.setValue(100);
        spinMaxIterationsNumber.setValue(20);
        spinH.setValue(4);
        spinEpslion.setValue(0.3);
        spinPReadyToShare.setValue(0.4);
        spinU.setValue(2);
    }
    private void formRadioButtonGroups() {
        strategyChangeTypeButtonGroup=new ButtonGroup();
        rangeOfstrategyChangeButtonGroup=new ButtonGroup();
        strategyChangeTypeButtonGroup.add(radioEvolutionary);
        strategyChangeTypeButtonGroup.add(radioAdoptToBest);
        rangeOfstrategyChangeButtonGroup.add(radioChanngeStrategyTypeRTSPlusStrategy);
        rangeOfstrategyChangeButtonGroup.add(tagRTSRadioButton);
        rangeOfstrategyChangeButtonGroup.add(radioStrategyCompetition);
    }
}
