package lab4.La;

import UI.Controller;
import UI.ProgressView;
import lab4.Dane;

import lab4.debug.Debug;
import lab4.Node.Sensor;
import lab4.Statistics;
import lab4.Utils.Utils;
import lab4.debug.DebugV2;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class LaAlgorithm extends Thread {
    private final JProgressBar progres;
    private final JLabel labProgresInfo;
    private final Controller controller;

    Dane data;
    Random random;
    Statistics statistics;



    @Override
    public void run() {

        data.setListsOfSensorsForEachSecond(getShedule());

        controller.showChartView();

    }


    public LaAlgorithm(Dane data, Statistics statistics, ProgressView progressView, Controller controller) {
        this.statistics=statistics;
        this.data=data;
        this.random=new Random(data.randomSeed);
        this.progres=progressView.progressBar1;
        this.labProgresInfo=progressView.labInfo;
        this.controller=controller;
    }

    public List<List<Sensor>>getShedule()
    {
        Environment environment=new Environment(data.getListOfPoi(),data.getListOfSensors(),data.getPromien());
        List<List<Sensor>>result=new ArrayList<>();
        Environment environmentNoDeadSensors=new Environment(environment);
        for(int i=1;i<=data.laData.maxRunsNumber;i++)
        {



            environmentNoDeadSensors.removeDeadSensors(data.getPromien());
            statistics.getProcentOfAliveSensorsAfterEachRun().add(environmentNoDeadSensors.sensorsList.size()/(double)environment.sensorsList.size());
            Environment resultEnvironment=getBestSolutionForCurrentState(environmentNoDeadSensors,data.getListOfSensors(),progres,labProgresInfo,i);


            if(resultEnvironment!=null)//null mean that target coverage ratio hasn't been achieved
            {
                result.add(environment.getSoulution());
            }else {
                break;
            }

            environmentNoDeadSensors.eraseBattery();



        }
        Debug.produceDebugFilesAfertGettingSolution(statistics,environment);
        environment.reconnectPoiWithSensors(data.getPromien());

        return result;
    }

//    private void onlySolutionSensorsRemainOn(Environment environment, Environment resultEnvironment) {
//        List<Sensor> solution=resultEnvironment.getSoulution(resultEnvironment.sensorsList);
//        environment.offAllSensors();
//        environment.setSensorsStatesAccordingToList(solution);
//    }

    /**
     * @return null mean that target coverage ratio hasn't been achieved
     * @param environment
     * @param listOfSensors
     * @param progres
     * @param labProgresInfo
     */
    private Environment getBestSolutionForCurrentState(Environment environment, List<Sensor> listOfSensors, JProgressBar progres, JLabel labProgresInfo,int solutionNumber) {

        DebugV2 debugV2=new DebugV2();
        int C_u=0;
        File file=new File("./debug");
        file.mkdir();
        environment.resetSumC();
        labProgresInfo.setText("Inicjalizacja pamięci");
        initMemory(environment,progres);
        labProgresInfo.setText("Obliczanie rozwiązania "+solutionNumber);
//        environment.setRandomSensorsStatesKAndReadyToShare(random,data.laData.probSensorOn,data.laData.maxK,data.laData.probReadyToShare);
        List<List<Sensor>>runStatistics=new ArrayList<>();
        List<Double>procentOfCoveredPoi=new ArrayList<>();
        List<List<Double>>localCoveragerateForEachSensor=new ArrayList<>();
        statistics.prepareBeforeNewRun();
        //init localCoverageRateForEachSensor
//        for(int i=0;i<environment.sensorsList.size();i++)
//        {
//            localCoveragerateForEachSensor.add(new ArrayList<>());
//        }

        debugV2.addFirst(environment.sensorsList);


        List<Sensor>on=environment.sensorsList.stream().filter(x->x.getStan()==1).collect(Collectors.toList());
        List<Sensor>off=environment.sensorsList.stream().filter(x->x.getStan()==0).collect(Collectors.toList());
        List<Sensor>isReadyToShare=environment.sensorsList.stream().filter(Sensor::isReadyToShare).collect(Collectors.toList());
        for(int i=0;i<data.laData.maxIterationsNumber;i++)
        {

            debugV2.addSecound(environment.sensorsList,i+1);
            environment.discontReward(data);
            debugV2.addThird(environment.sensorsList,i);
            debugV2.addFourth(environment.sensorsList,1);

            int progresValue=(int)((i/(double)data.laData.maxIterationsNumber)*100);
            progres.setValue(progresValue);
            C_u++;
            if(C_u==data.laData.u && !data.laData.isStrategyCompetition()) {
                environment.setBestStrategyFormMemory(data.laData.epslion, random, data.laData);
                debugV2.addSix(environment.sensorsList);
                environment.makeStrategyUSwap(data.laData.isRTSPlusStrategy,data.laData.isEvolutionaryStrategyChange,random,statistics);
                C_u=0;
                debugV2.addSeven(environment.sensorsList);
            }
            else {
                statistics.getStrategyChanged().get(statistics.getStrategyChanged().size()-1).add(0.0);
                environment.setBestStrategyFormMemory(data.laData.epslion, random, data.laData);
            }


            runStatistics.add(Utils.cloneList(environment.sensorsList));
            procentOfCoveredPoi.add(environment.getCoverageRate());
            localCoveragerateForEachSensor.add(environment.getLocalCoverageRateForEachSensor());
            environment.useSelectedStrategy();

            debugV2.addFifth(listOfSensors);



        }
        debugV2.saveLinesToFile("./debug/debug"+solutionNumber+".csv");
        List<Sensor>hasBattery=environment.sensorsList.stream().filter(x->x.getBateriaPojemnosc()>0).collect(Collectors.toList());
        List<Sensor>underQ=environment.sensorsList.stream().filter(x->x.getCurrentLocalCoverageRate()<data.getQ()).collect(Collectors.toList());
        List<Sensor>upperQ=environment.sensorsList.stream().filter(x->x.getCurrentLocalCoverageRate()>=data.getQ()).collect(Collectors.toList());
        isReadyToShare=environment.sensorsList.stream().filter(Sensor::isReadyToShare).collect(Collectors.toList());
        double rate=environment.getCoverageRate();
        statistics.getRunsStateList().add(runStatistics);
        statistics.getProcentOfCoveredPoi().add(procentOfCoveredPoi);
        statistics.getLocalCoveredPoisRate().add(localCoveragerateForEachSensor);
//        System.out.println(rate);
        statistics.getResultShedule().add(environment.getSoulution());
        if(environment.getCoverageRate()<data.getQ())
        {
            return null;
        }
        return environment;
    }



    public void initMemory(Environment environment,JProgressBar progress) {
//        Environment temp_environment=new Environment(environment,data.getPromien());
        environment.setRandomSensorsStatesKAndReadyToShare(random,data.laData.probSensorOn,data.laData.maxK, data.laData.probReadyToShare);
        for(int i=0;i<data.laData.h;i++)
        {
            progress.setValue((int)((i/(double)data.laData.h)*100));
            environment.chooseRandomStrategy(random,data.laData);
            environment.useSelectedStrategy();
            environment.discontReward(data);


        }

    }


}
