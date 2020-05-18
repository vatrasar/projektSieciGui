package lab4;



import lab4.Node.Sensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Statistics {
    List<List<List<Sensor>>>runsStateList;//list of runs. each run contains from list of sensors states in each iteration

    public Statistics() {
        runsStateList=new ArrayList<>();
    }

    public List<List<List<Sensor>>> getRunsStateList() {
        return runsStateList;
    }

    public void setRunsStateList(List<List<List<Sensor>>> runsStateList) {
        this.runsStateList = runsStateList;
    }

    public List<Double> getBestRewardsForEachItereationOfRun(int runNumber) {
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
        for(var sensor:iteration)
        {
            contributionsOfStrategiesInIteration.put(sensor.getLastStrategy().getName(),(contributionsOfStrategiesInIteration.get(sensor.getLastStrategy().getName())+1));

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

        for(var iteration:runIterations)
        {
            List<Sensor>sensorsWithSelectedStrategy=iteration.stream().filter(x->x.getLastStrategy().getName()==strategyName).collect(Collectors.toList());
            Map<Integer, Double>kUsageInIteration=new HashMap<>();
            for(var sensor:sensorsWithSelectedStrategy)
            {
                kUsageInIteration.putIfAbsent(sensor.getK(),0.0);
                kUsageInIteration.put(sensor.getK(),kUsageInIteration.get(sensor.getK())+1);
            }
            for(var entry:kUsageInIteration.entrySet())
            {
                entry.setValue(entry.getValue()/sensorsWithSelectedStrategy.size());
                result.putIfAbsent(entry.getKey(),new ArrayList<>());
                result.get(entry.getKey()).add(entry.getValue());
            }



        }
        return result;
    }
}
