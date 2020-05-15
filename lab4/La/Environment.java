package lab4.La;

import lab4.Dane;
import lab4.La.strategies.*;
import lab4.Poi;
import lab4.Sensor;
import lab4.Utils;

import java.util.List;
import java.util.Random;

public class Environment {
    public List<Poi>poisList;
    public List<Sensor>sensorsList;

    public Environment(List<Poi> poisList, List<Sensor> sensorsList,int sensingRange) {
        this.poisList = Utils.cloneList(poisList);
        this.sensorsList =Utils.cloneList(sensorsList);
        Utils.connectSensorsWithPoi(this.poisList,this.sensorsList,sensingRange);

    }
    public Environment(Environment parentEnvironment,int sensingRange) {
        this.poisList = Utils.cloneList(parentEnvironment.poisList);
        this.sensorsList =Utils.cloneList(parentEnvironment.sensorsList);
        Utils.connectSensorsWithPoi(this.poisList,this.sensorsList,sensingRange);


    }

    public void resetSumC() {
        sensorsList.forEach(sensor->{sensor.sum_u=0;});
    }


    /**
     *
     * set random values for
     * @param randomSeed
     * @param probSensorOn
     * @param maxK
     * @param probReadyToShare
     */
    public void setRandomSensorsStatesKAndReadyToShare(long randomSeed, double probSensorOn, int maxK, double probReadyToShare) {
        Random random=new Random(randomSeed);
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

    public void setNewStateAccordingToRandomStrategy(long randomSeed, LaData laData) {
        Random random=new Random(randomSeed);
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
            threshold+=laData.KDProb;
            if(x<threshold)
            {
                strategy=new KDStrategy();

                sensor.useStrategy(strategy);
                continue;
            }


            strategy=new KDCStrategy();
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

    public void setSensorsStatesAccordingToBestStrategyInMemory(double epslion,long randomSeed) {
        Random random=new Random(randomSeed);
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
        {
            sensor.eraseBattery();
        }
    }
}
