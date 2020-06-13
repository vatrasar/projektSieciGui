package lab4;



import lab4.Node.Sensor;

import java.util.*;
import java.util.stream.Collectors;

public class Statistics {
    List<List<List<Sensor>>>runsStateList;//list of runs. each run contains from list of sensors states in each iteration
    public List<Double>procentOfAliveSensorsAfterEachRun;
    List<List<Double>> procentOfCoveredPoi;
    List<List<Double>> strategyChanged;
    List<List<List<Double>>>localCoveredPoisRate;
    List<List<Sensor>>resultSchedule;


    public Statistics() {
        resultSchedule=new ArrayList<>();
        runsStateList=new ArrayList<>();
        procentOfAliveSensorsAfterEachRun=new ArrayList<>();
        procentOfCoveredPoi=new ArrayList<>();
        localCoveredPoisRate=new ArrayList<>();
        strategyChanged=new ArrayList<>();
    }


    public List<List<Double>> getStrategyChanged() {
        return strategyChanged;
    }

    public void setStrategyChanged(List<List<Double>> strategyChanged) {
        this.strategyChanged = strategyChanged;
    }

    public List<List<Double>> getProcentOfCoveredPoi() {
        return procentOfCoveredPoi;
    }

    public void setProcentOfCoveredPoi(List<List<Double>> procentOfCoveredPoi) {
        this.procentOfCoveredPoi = procentOfCoveredPoi;
    }

    public List<List<List<Sensor>>> getRunsStateList() {
        return runsStateList;
    }

    public void setRunsStateList(List<List<List<Sensor>>> runsStateList) {
        this.runsStateList = runsStateList;
    }

    public List<Double> getMeanRewardsForEachItereationOfRun(int runNumber) {
        List<List<Sensor>>runIterations= runsStateList.get(runNumber-1);
        List<Double> meanRewardFroEachIteration=new ArrayList<>();
        for(var iteration:runIterations)
        {
            List<Sensor>iterMoreThenZero=iteration.stream().filter(x->x.getLastReward()==1).collect(Collectors.toList());
            List<Sensor>iterNotOne=iteration.stream().filter(x->x.getLastReward()!=1).collect(Collectors.toList());
            double sum = getRewardSumForIteration(iteration);
            meanRewardFroEachIteration.add(sum/iteration.size());

        }

        return meanRewardFroEachIteration;

    }

    private double getRewardSumForIteration(List<Sensor> iteration) {
        double sum=0;
        for(var sensor:iteration)
        {
            sum+=sensor.getLastReward();
        }
        return sum;
    }

    public List<Double> getProcetOfActiveSensors(int runNumber) {
        List<List<Sensor>>runIterations= runsStateList.get(runNumber-1);
        List<Double> procentOfActiveSensors=new ArrayList<>();

        for(var iteration:runIterations)
        {
            int numOfActive=0;
            for(var sensor:iteration)
            {
                if(sensor.getStan()==1)
                {
                    numOfActive++;
                }
            }
            procentOfActiveSensors.add(((double)numOfActive)/iteration.size());

        }
        return procentOfActiveSensors;

    }

    /**
     * @param
     * @return returns pair String: strategy name,List<Double> procentage contriubtion of strategy in each iteration
     */
    public Map<String,List<Double>> getProcetOfStrategies(int runNumber) {
        List<List<Sensor>>runIterations= runsStateList.get(runNumber-1);
        HashMap<String,List<Double>>contributionsOfStrategies=new HashMap<>();
        contributionsOfStrategies.put("ALLC",new ArrayList<>());
        contributionsOfStrategies.put("KC",new ArrayList<>());
        contributionsOfStrategies.put("KDC",new ArrayList<>());
        contributionsOfStrategies.put("KD",new ArrayList<>());
        contributionsOfStrategies.put("AllD",new ArrayList<>());
        computeUsageOfStrategies(runIterations, contributionsOfStrategies);
        return  contributionsOfStrategies;
    }

    private void computeUsageOfStrategies(List<List<Sensor>> runIterations, HashMap<String, List<Double>> contributionsOfStrategies) {
        for(var iteration:runIterations)
        {



            HashMap<String,Double>contributionsOfStrategiesInIteration=computeStrategyUsageForOneIteration(contributionsOfStrategies, iteration);
            for(var pair:contributionsOfStrategiesInIteration.entrySet())
            {
                List<Double> procentageUsageOfStrategy=contributionsOfStrategies.get(pair.getKey());
                procentageUsageOfStrategy.add(pair.getValue());
            }

        }
    }

    private HashMap<String, Double> computeStrategyUsageForOneIteration(HashMap<String, List<Double>> contributionsOfStrategies, List<Sensor> iteration) {
        HashMap<String,Double>contributionsOfStrategiesInIteration=new HashMap<>();
        contributionsOfStrategiesInIteration.put("ALLC",0.0);
        contributionsOfStrategiesInIteration.put("KC",0.0);
        contributionsOfStrategiesInIteration.put("KDC",0.0);
        contributionsOfStrategiesInIteration.put("KD",0.0);
        contributionsOfStrategiesInIteration.put("AllD",0.0);
        for(var sensor:iteration)
        {
            var result=contributionsOfStrategiesInIteration.get(sensor.getLastStrategy().getName())+1;

            contributionsOfStrategiesInIteration.put(sensor.getLastStrategy().getName(),result);

        }
        for(var pair:contributionsOfStrategiesInIteration.entrySet())
        {
            pair.setValue(pair.getValue()/iteration.size());
        }



        return contributionsOfStrategiesInIteration;
    }

    public Map<Integer, List<Double>> getProcentOfUsageOfEachKInStartegy(int runNumber, String strategyName) {
        List<List<Sensor>>runIterations= runsStateList.get(runNumber-1);
        Map<Integer, List<Double>> result=new HashMap<>();
        int iterationNumber=0;
        List<Map<Integer, Double>>kUsageInIterationList=new ArrayList<>();
        for(var iteration:runIterations)
        {
            List<Sensor>sensorsWithSelectedStrategy=iteration.stream().filter(x->x.getLastStrategy().getName().equals(strategyName)).collect(Collectors.toList());
            Map<Integer, Double>kUsageInIteration=new HashMap<>();
            for(var sensor:sensorsWithSelectedStrategy)
            {

                kUsageInIteration.putIfAbsent(sensor.getK(),0.0);
                kUsageInIteration.put(sensor.getK(),kUsageInIteration.get(sensor.getK())+1);
            }
            for(var entry:kUsageInIteration.entrySet())
            {
                entry.setValue(entry.getValue()/sensorsWithSelectedStrategy.size());

            }
            kUsageInIterationList.add(kUsageInIteration);


            iterationNumber++;



        }

        initResultmap(result, kUsageInIterationList);

        for(int i=0;i<kUsageInIterationList.size();i++)
        {
            for(var entry:kUsageInIterationList.get(i).entrySet())
            {



                result.get(entry.getKey()).set(i,entry.getValue());


            }
        }



        return result;
    }

    private void initResultmap(Map<Integer, List<Double>> result, List<Map<Integer, Double>> kUsageInIterationList) {
        Set<Integer> allPresetKValues=getAllPresentKValues(kUsageInIterationList);

        for(var k:allPresetKValues)
        {
            result.put(k,new ArrayList<>());
        }
        for(int i=0;i<kUsageInIterationList.size();i++)
        {
            for(var value:result.values())
            {
                value.add(0.0);
            }
        }

    }

    private Set<Integer> getAllPresentKValues(List<Map<Integer, Double>> kUsageInIterationList) {
        HashSet<Integer>kSet=new HashSet<>();
        for(var iterationUsage:kUsageInIterationList)
        {
            kSet.addAll(iterationUsage.keySet());
        }
        return kSet;
    }

    public List<Double> getProcentOfAliveSensorsAfterEachRun() {
        return procentOfAliveSensorsAfterEachRun;
    }

    public void setProcentOfAliveSensorsAfterEachRun(List<Double> procentOfAliveSensorsAfterEachRun) {
        this.procentOfAliveSensorsAfterEachRun = procentOfAliveSensorsAfterEachRun;
    }

    public List<Double> getProcentOfRTSUsageInRun(int runNumber) {
        List<List<Sensor>>runIterations= runsStateList.get(runNumber-1);
        List<Double>result=new ArrayList<>();
        for(var iteration:runIterations)
        {
            int rtsNumber=0;
            for(var sensor:iteration)
            {
                if(sensor.isReadyToShare())
                {
                    rtsNumber++;
                }
            }
            result.add(rtsNumber/(double)iteration.size());

        }
        return result;
    }

    public List<Double> getProcentOfCoveredPoi(int runNumber) {
        return procentOfCoveredPoi.get(runNumber-1);
    }

    public Map<Integer, List<Double>> getDataForSensorRewardChart(int runNumber) {
        List<List<Sensor>>iterationsSensorsList=runsStateList.get(runNumber-1);
        Map<Integer,List<Double>>result=new HashMap<>();
        initResultDaraForSensor(iterationsSensorsList, result);
        for(var iteration:iterationsSensorsList)
        {

            for(int i=0;i<iteration.size();i++)
            {
               Sensor sensor =iteration.get(i);
               Double reward=sensor.getLastReward();
               result.get(i+1).add(reward);
            }
        }
        return result;

    }

    private <T> void initResultDaraForSensor(List<List<T>> iterationsSensorsList, Map<Integer, List<Double>> result) {
        for(int i=1;i<=iterationsSensorsList.get(0).size();i++)
        {
            result.put(i,new ArrayList<>());
        }
    }

    public Map<Integer, List<Double>> getForLocalCoverageChart(int runNumber) {
        List<List<Double>>iterationsSensorsCoverList=localCoveredPoisRate.get(runNumber-1);
        Map<Integer,List<Double>>result=new HashMap<>();
        initResultDaraForSensor(iterationsSensorsCoverList, result);
        for(var iteration:iterationsSensorsCoverList)
        {
            for(int i=0;i<iteration.size();i++)
            {
               result.get(i+1).add(iteration.get(i));
            }

        }
        return result;
    }

    public List<List<List<Double>>> getLocalCoveredPoisRate() {
        return localCoveredPoisRate;
    }

    public List<List<Sensor>> getResultShedule() {
        return resultSchedule;
    }
    public void setResultShedule(List<List<Sensor>> result) {
    }

    public void prepareBeforeNewRun() {

        strategyChanged.add(new ArrayList<>());
    }
}
