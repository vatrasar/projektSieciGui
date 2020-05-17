package lab4.La;

import lab4.Dane;
import lab4.La.strategies.*;

import lab4.Node.Poi;
import lab4.Node.Sensor;
import lab4.Utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Environment {
    public List<Poi>poisList;
    public List<Sensor>sensorsList;

    public Environment(List<Poi> poisList, List<Sensor> sensorsList, int sensingRange) {
        this.poisList = Utils.cloneList(poisList);
        this.sensorsList =Utils.cloneList(sensorsList);
        Utils.connectSensorsWithPoi(this.poisList,this.sensorsList,sensingRange);
        for(Sensor sensor:this.sensorsList)
        {
            sensor.connectWithNeighbors();
        }

    }
    public Environment(Environment parentEnvironment) {
        this.poisList = new ArrayList<>(parentEnvironment.poisList);
        this.sensorsList = new ArrayList<>(parentEnvironment.sensorsList);



    }

    public void resetSumC() {
        sensorsList.forEach(sensor->{sensor.sum_u=0;});
    }


    /**
     *
     * set random values for
     * @param
     * @param probSensorOn
     * @param maxK
     * @param probReadyToShare
     */
    public void setRandomSensorsStatesKAndReadyToShare(Random random, double probSensorOn, int maxK, double probReadyToShare) {

        for (Sensor sensor: sensorsList) {

            setProbabilisticState(probSensorOn, random, sensor);

            setProbabilisticReadyToShare(probReadyToShare, random, sensor);


            sensor.k=random.nextInt(maxK);
        }

    }

    private void setProbabilisticState(double probSensorOn, Random random, Sensor sensor) {
        if(random.nextDouble()>probSensorOn)
            sensor.setStan(0);
        else
            sensor.setStan(1);
    }

    private void setProbabilisticReadyToShare(double probReadyToShare, Random random, Sensor sensor) {
        if(random.nextDouble()>probReadyToShare)
        {
            sensor.setReadyToShare(false);
        }
        else
            sensor.setReadyToShare(false);
    }

    public void setNewStateAccordingToRandomStrategy(Random random, LaData laData) {

        for(Sensor sensor:sensorsList)
        {
            Strategy strategy;
            double x=random.nextDouble();
            double threshold=laData.allCProb;

            if(x<threshold)
            {
                strategy=new AllCStrategy();

                sensor.useStrategy(strategy);
                continue;

            }
            threshold+=laData.KCProb;
            if(x<threshold)
            {
                strategy=new KCStrategy();

                sensor.useStrategy(strategy);
                continue;
            }
            threshold+=laData.KDCProb;
            if(x<threshold)
            {
                strategy=new KDCStrategy();

                sensor.useStrategy(strategy);
                continue;
            }


            strategy=new KDStrategy();
            sensor.useStrategy(strategy);


        }
    }

    public void discontReward(Dane data) {

        for(Sensor sensor: this.sensorsList)
        {
            sensor.discontReward(data,sensorsList);
        }
        for(Sensor sensor: sensorsList)
        {
            sensor.discontRewardRTS();
        }
    }

    public void setSensorsStatesAccordingToBestStrategyInMemory(double epslion,Random random) {

        for(Sensor sensor: this.sensorsList)
        {
            if(epslion<random.nextDouble())
                sensor.useBestStrategy();
            else
                sensor.useRandomStrategy(random);
        }
    }

    public double getCoverageRate() {
        int coveredPois=0;
        for(Poi poi:poisList)
        {
            if(poi.coveringSensorsList.stream().anyMatch(x->x.getStan()==1))
            {
                coveredPois++;
            }
        }
        return ((double) coveredPois)/poisList.size();

    }


    public void eraseBattery() {
        for(Sensor sensor:sensorsList)
        {   if(sensor.getStan()==1)
                sensor.eraseBattery();
        }
    }

    public void makeStrategyUSwap(boolean isRTSPlusStrategy) {
        for(Sensor sensor:sensorsList)
        {
            Sensor bestNeighbor=sensor.getNeighborWithBestSumU();
            if (bestNeighbor==null)
            {
                sensor.useBestStrategy();
                continue;
            }
            sensor.setReadyToShare(bestNeighbor.isReadyToShare());
            sensor.setK(bestNeighbor.getK());
            if(isRTSPlusStrategy)
                sensor.useStrategy(bestNeighbor.getBestRecordFromMemory().getStrategy());
            else
                sensor.useBestStrategy();
        }

    }

    public void setSensorsStatesAccordingToList(List<Sensor> bestSolutionForCurrentState) {
        sensorsList.forEach(x->x.setStan(0));
        bestSolutionForCurrentState.forEach(x->x.setStan(1));
    }

    public List<Sensor> getSoulution(List<Sensor> listOfSensors) {
        List<Sensor>solution=new ArrayList<>();
        for(int i=0;i<sensorsList.size();i++)
        {
            if(sensorsList.get(i).getStan()==1)
            {
                solution.add(listOfSensors.get(i));
            }
        }
        return solution;
    }

    public void removeDeadSensors() {
        sensorsList=sensorsList.stream().filter(x->x.getBateriaPojemnosc()!=0).collect(Collectors.toList());
    }

    public void offAllSensors() {
        sensorsList.forEach(x->x.setStan(0));
    }
}
