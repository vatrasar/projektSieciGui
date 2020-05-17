package lab4.La;

import lab4.Sensor;

import java.util.ArrayList;
import java.util.List;

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
            double sum = getRewardSumForIteration(iteration);
            meanRewardFroEachIteration.add(sum/iteration.size());
        }
        return meanRewardFroEachIteration;

    }

    private double getRewardSumForIteration(List<Sensor> iteration) {
        double sum=0;
        for(var sensor:iteration)
        {
            sum=+sensor.getLastReward();
        }
        return sum;
    }
}
