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
    private JButton btnDebug;
    ButtonGroup strategyChangeTypeButtonGroup;
    ButtonGroup rangeOfstrategyChangeButtonGroup;


    public LaData getLaData() {

        LaData laData=new LaData((int)spinMaxIterationsNumber.getValue(),(int)spinMaxRunsNumber.getValue(),checkIsScheduleSearch.isSelected(), checkIsStopCondition.isSelected(),(int)spinDeltaStop.getValue(), (int)spinU.getValue(),(double)spinAllC.getValue(),(double)spinKC.getValue(),(double)spinKD.getValue(),(double)spinKDC.getValue(),(int)spinMaxK.getValue(),(double)spinProbSensorOn.getValue(),(int)spinH.getValue(),(double)spinEpslion.getValue(),radioEvolutionary.isSelected(),radioChanngeStrategyTypeRTSPlusStrategy.isSelected(),(double)spinPReadyToShare.getValue());
        return laData;
    }

    private void createUIComponents() {

        spinAllC=new JSpinner(new SpinnerNumberModel(0.0,0 ,1.0,0.1));
        spinKC=new JSpinner(new SpinnerNumberModel(0.0,0 ,1.0,0.1));
        spinKD=new JSpinner(new SpinnerNumberModel(0.0,0 ,1.0,0.1));
        spinKDC=new JSpinner(new SpinnerNumberModel(0.0,0 ,1.0,0.1));
        spinEpslion=new JSpinner(new SpinnerNumberModel(0.0,0 ,1.0,0.1));
        spinProbSensorOn=new JSpinner(new SpinnerNumberModel(0.0,0 ,1.0,0.1));
        spinPReadyToShare=new JSpinner(new SpinnerNumberModel(0.0,0 ,1.0,0.1));

    }

    public LaSettingsView() throws HeadlessException {
        formRadioButtonGroups();

    }

    private void formRadioButtonGroups() {
        strategyChangeTypeButtonGroup=new ButtonGroup();
        rangeOfstrategyChangeButtonGroup=new ButtonGroup();
        strategyChangeTypeButtonGroup.add(radioEvolutionary);
        strategyChangeTypeButtonGroup.add(radioAdoptToBest);
        rangeOfstrategyChangeButtonGroup.add(radioChanngeStrategyTypeRTSPlusStrategy);
        rangeOfstrategyChangeButtonGroup.add(tagRTSRadioButton);
    }
}
