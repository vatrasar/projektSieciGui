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
        this.poisList = new ArrayList(poisList);
        this.sensorsList =new ArrayList<>(sensorsList);
//        Utils.connectSensorsWithPoi(this.poisList,this.sensorsList,sensingRange);
//        for(Sensor sensor:this.sensorsList)
//        {
//            sensor.connectWithNeighbors();
//        }

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
            sensor.setReadyToShare(true);
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

                sensor.useStrategy(strategy, random.nextInt(laData.maxK),random.nextBoolean());
                continue;

            }
            threshold+=laData.KCProb;
            if(x<threshold)
            {
                strategy=new KCStrategy();

                sensor.useStrategy(strategy,random.nextInt(laData.maxK),random.nextBoolean());
                continue;
            }
            threshold+=laData.KDCProb;
            if(x<threshold)
            {
                strategy=new KDCStrategy();

                sensor.useStrategy(strategy, random.nextInt(laData.maxK),random.nextBoolean());
                continue;
            }
            threshold+=laData.allDProb;
            if(x<threshold)
            {
                strategy=new AllDStrategy();
                sensor.useStrategy(strategy, random.nextInt(laData.maxK),random.nextBoolean());
                continue;
            }

            strategy=new KDStrategy();
            sensor.useStrategy(strategy, random.nextInt(laData.maxK),random.nextBoolean());



        }
        setNextState();
    }

    public void discontReward(Dane data) {
//        LocalDateTime start= LocalDateTime.now();
        int counter=0;
        int counter2=0;
        for(Sensor sensor: this.sensorsList)
        {
            counter++;
            sensor.discontReward(data,sensorsList);
        }
//        LocalDateTime end=LocalDateTime.now();
//        System.out.println("czas:"+(end.get-start.getNano()));
        if(data.laData.isRTS)
        {
            computeRTS(counter2);
            discountRTS();
        }
//        System.out.println("RTS:"+counter2);

    }

    private void computeRTS(int counter2) {
        for(Sensor sensor: sensorsList)
        {
            counter2++;
            sensor.computeRewardRTS();
        }
    }

    private void discountRTS() {
        for(var sensor:sensorsList)
        {
            sensor.discountRTS();
        }
    }

    public void setSensorsStatesAccordingToBestStrategyInMemory(double epslion,Random random,LaData laData) {

        for(Sensor sensor: this.sensorsList)
        {
            if(epslion<random.nextDouble())
                sensor.useBestStrategy();
            else
                sensor.useRandomStrategy(random,laData.allCProb,laData.allDProb,laData.KCProb,laData.KDCProb,laData.KDProb,laData.maxK);
        }
        setNextState();
    }

    private void setNextState() {
        for (Sensor sensor : sensorsList) {
            sensor.setStan(sensor.getNextState());
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

    public void makeStrategyUSwap(boolean isRTSPlusStrategy,boolean isEvolutionary,Random random) {
        for(Sensor sensor:sensorsList)
        {
            Sensor neighborToCopyStrategy=null;
            if(isEvolutionary)
                neighborToCopyStrategy=sensor.getNeighborToCopyEvolutionary(random);
            else
                neighborToCopyStrategy=sensor.getNeighborWithBestSumU();
            if (neighborToCopyStrategy==null)
            {
                sensor.useBestStrategy();
                continue;
            }
            sensor.setReadyToShare(neighborToCopyStrategy.isReadyToShare());
            sensor.setK(neighborToCopyStrategy.getK());
            if(isRTSPlusStrategy) {
                var bestRecord=neighborToCopyStrategy.getBestRecordFromMemory();
                sensor.useStrategy(bestRecord.getStrategy(),bestRecord.getK(),bestRecord.isRTS);
            }
            else
                sensor.useBestStrategyWithNeighbourRTS(neighborToCopyStrategy.isReadyToShare());
            sensor.sum_u=0;
        }
        setNextState();

    }

    public void setSensorsStatesAccordingToList(List<Sensor> bestSolutionForCurrentState) {
        sensorsList.forEach(x->x.setStan(0));
        bestSolutionForCurrentState.forEach(x->x.setStan(1));
    }

    public List<Sensor> getSoulution() {
       return sensorsList.stream().filter(x->x.getStan()==1).collect(Collectors.toList());
    }

    public void removeDeadSensors(int sensingRange) {
       List<Sensor>toRemove=new ArrayList<>();
        for(var sensor:sensorsList)
        {
            if(sensor.getBateriaPojemnosc()<=0)
            {
                for(var neighbour:sensor.neighborSensors)
                {
                    neighbour.neighborSensors.remove(sensor);
                }
                for(var poi:poisList)
                {
                    poi.coveringSensorsList.remove(sensor);
                }
                toRemove.add(sensor);


            }

        }
        sensorsList.removeAll(toRemove);

//        sensorsList=sensorsList.stream().filter(x->x.getBateriaPojemnosc()!=0).collect(Collectors.toList());
//        reconnect(sensingRange);
    }

    public void reconnectPoiWithSensors(int sensingRange) {
//        for(var sensor:sensorsList)
//        {
//            sensor.clearNeighbours();
//
//        }
        for(var poi:poisList)
        {
            poi.clearCoveringSensorsList();
        }
        Utils.connectSensorsWithPoi(this.poisList,this.sensorsList,sensingRange);
//        for(Sensor sensor:this.sensorsList)
//        {
//            sensor.connectWithNeighbors();
//        }
    }

    public void offAllSensors() {
        sensorsList.forEach(x->x.setStan(0));
    }


    public List<Double> getLocalCoverageRateForEachSensor() {
        List<Double>localCoverageRateForEachSensor=new ArrayList<>();
        for(var sensor:sensorsList)
        {
            localCoverageRateForEachSensor.add(sensor.getCurrentLocalCoverageRate());
        }
        return localCoverageRateForEachSensor;
    }
}
