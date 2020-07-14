package lab4.La;

import lab4.Dane;
import lab4.La.strategies.*;

import lab4.Node.Poi;
import lab4.Node.Sensor;
import lab4.Statistics;
import lab4.Utils.Utils;
import lab4.debug.DebugV2;


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


            sensor.setK(random.nextInt(maxK));
        }

    }

    private void setProbabilisticState(double probSensorOn, Random random, Sensor sensor) {
        if(random.nextDouble()>probSensorOn)
            sensor.setStan(0);
        else
            sensor.setStan(1);
    }

    private void setProbabilisticReadyToShare(double probReadyToShare, Random random, Sensor sensor) {
        if(random.nextDouble()>probReadyToShare) {
            sensor.setReadyToShare(false);
            sensor.setNextRTS(false);
        }
        else
            {
                sensor.setReadyToShare(true);
                sensor.setNextRTS(true);
            }
    }

    public void chooseRandomStrategy(Random random, LaData laData) {

        for(Sensor sensor:sensorsList)
        {
            sensor.setLastStrategySelectedByEps(true);
            sensor.setNextStrategySelectedByEps(true);

            Strategy strategy;
            double x=random.nextDouble();
            double threshold=laData.allCProb;

            if(x<threshold)
            {
                strategy=new AllCStrategy();

                sensor.setLastUsedStrategy(strategy);
                continue;

            }
            threshold+=laData.KCProb;
            if(x<threshold)
            {
                strategy=new KCStrategy();

                sensor.setLastUsedStrategy(strategy);
                continue;
            }
            threshold+=laData.KDCProb;
            if(x<threshold)
            {
                strategy=new KDCStrategy();

                sensor.setLastUsedStrategy(strategy);
                continue;
            }
            threshold+=laData.allDProb;
            if(x<threshold)
            {
                strategy=new AllDStrategy();
                sensor.setLastUsedStrategy(strategy);
                continue;
            }

            strategy=new KDStrategy();
            sensor.setLastUsedStrategy(strategy);



        }
        for(var sensor:sensorsList)
        {
            if(sensor.getLastStrategy()==null)
            {
                System.out.println("r");
            }
        }

    }

    public void discontReward(Dane data) {
//        LocalDateTime start= LocalDateTime.now();
        int counter=0;
        int counter2=0;
        for(Sensor sensor: this.sensorsList)
        {
            counter++;
            sensor.discontReward(data,sensorsList);
            sensor.setRewardBeforeRTS(sensor.getLastReward());
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
            if(sensor.isReadyToShare())
            {
                counter2++;
                sensor.computeRewardRTS();
            }

        }
    }

    private void discountRTS() {
        for(var sensor:sensorsList)
        {   if(sensor.isReadyToShare())
                sensor.discountRTS();
        }
    }

    public void setBestStrategyFormMemory(double epslion, Random random, LaData laData) {

        for(Sensor sensor: this.sensorsList)
        {
            if(epslion<random.nextDouble()) {
                sensor.setLastUsedStrategy(sensor.getBestRecordFromMemory(random).getStrategy());
                sensor.setNextStrategySelectedByEps(false);
                sensor.setLastStrategySelectedByEps(false);

            }
            else {
                sensor.setLastUsedStrategy(sensor.getRandomStrategy(random, laData.allCProb, laData.allDProb, laData.KCProb, laData.KDCProb, laData.KDProb, laData.maxK));
                sensor.setNextStrategySelectedByEps(true);
                sensor.setLastStrategySelectedByEps(true);
                if(sensor.getLastStrategy().getName().contains("K"))
                    sensor.setK(random.nextInt(laData.maxK));
            }
        }
        for(var sensor:sensorsList)
        {
            if(sensor.getLastStrategy()==null)
            {
                System.out.println("r");
            }
        }

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


    public void eraseBattery(double batteryUsageInOneMeomentOfTime) {
        for(Sensor sensor:sensorsList)
        {   if(sensor.getStan()==1)
                sensor.eraseBattery(batteryUsageInOneMeomentOfTime);
        }
    }

    public void makeStrategyUSwap(boolean isRTSPlusStrategy, boolean isEvolutionary, Random random, Statistics statistics, Dane data, DebugV2 debugV2) {

        int numberOfStrategyChangedEventsInIteration=0;
        List<Sensor>neighbourToCopyList=new ArrayList<>();
        for(Sensor sensor:sensorsList)
        {
            Sensor neighborToCopyStrategy=null;
            if(isEvolutionary) {
                neighborToCopyStrategy = sensor.getNeighborToCopyEvolutionary(random, data);

                if(neighborToCopyStrategy==null)
                {
                    neighborToCopyStrategy=sensor;
                }
            }
            else {
                neighborToCopyStrategy = sensor.getNeighborWithBestSumU();
                data.setEpsValue(0);
                if(neighborToCopyStrategy==null)
                {
                    neighborToCopyStrategy=sensor;
                }
            }
            neighbourToCopyList.add(neighborToCopyStrategy);




        }
        if(data.laData.isEvolutionaryStrategyChange)
        {
            debugV2.addSixEvolutionary(sensorsList);
        }
        else{
            debugV2.addSix(sensorsList);
        }
//        resetSumU();
        int counter=-1;
        for(var sensor:sensorsList)
        {
            counter++;
            if (neighbourToCopyList.get(counter)==null)
            {
//                sensor.setLastUsedStrategy(sensor.getBestRecordFromMemory(random).getStrategy());
                continue;
            }
            if(neighbourToCopyList.get(counter)!=sensor)
            {
                numberOfStrategyChangedEventsInIteration++;
                sensor.setHasStrategyChanged(true);
            }
            else
            {
                sensor.setHasStrategyChanged(false);
            }


            sensor.setNextK(neighbourToCopyList.get(counter).getK());
            sensor.setNextRTS(neighbourToCopyList.get(counter).isReadyToShare());
            if(isRTSPlusStrategy) {

//                var bestRecord=neighbourToCopyList.get(counter).getBestRecordFromMemory(random);
//                sensor.setNextState(neighborToCopyStrategy.getStan());
                sensor.setNextStrategy(neighbourToCopyList.get(counter).getLastStrategy());

            }
            else
            {
                sensor.setNextStrategy(sensor.getLastStrategy());

            }
        }


        statistics.getStrategyChanged().get(statistics.getStrategyChanged().size()-1).add(((double)numberOfStrategyChangedEventsInIteration)/sensorsList.size());

        //set k and RTS values
        for(Sensor sensor : sensorsList)
        {
            sensor.setK(sensor.getNextK());
            sensor.setReadyToShare(sensor.isNextRTS());
            sensor.setLastUsedStrategy(sensor.getNextStrategy());
        }


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

    /**
     * choose next state and then set it
     */
    public void useSelectedStrategy() {
        for (var sensor : sensorsList) {
            sensor.useSelectedStrategy();
            sensor.setLastStrategySelectedByEps(sensor.nextStrategySelectedByEps);
        }

        for (var sensor : sensorsList) {
            sensor.setStan(sensor.getNextState());
        }

    }


    public boolean isSensorsAllAreAlive() {

        for(var sensor:sensorsList)
        {
            if(sensor.getBateriaPojemnosc()<=0)
            {
                return false;
            }
        }
        return true;
    }

    public void resetSumU() {
        for(var sensor:sensorsList)
        {
            sensor.resetSumU();
        }
    }

    public void setRandomSensorsStates(double probSensorOn,Random random) {

        for (Sensor sensor: sensorsList) {
            setProbabilisticState(probSensorOn, random, sensor);
        }
    }

    public String getStateString() {

        String stateString="";
        for(var sensor:sensorsList)
        {
            stateString+=sensor.stan;
        }
        return stateString;
    }
}
