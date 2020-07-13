package lab4.La;

public class LaData {
    public boolean isRTS;
    public double allDProb;
    public  int maxIterationsNumber;
    public int maxRunsNumber;
    public boolean isScheduleSearch;
    public boolean isStopCondition;
    public int deltaStop;
    public int u;
    public double allCProb;
    public double KCProb;
    public double KDProb;
    public double KDCProb;
    public int maxK;
    public double probSensorOn;
    public int h;
    public double epslion;
    public boolean isEvolutionaryStrategyChange;
    public boolean isRTSPlusStrategy;
    public double probReadyToShare;
    public boolean isStrategyCompetition;
    public int runNumber;

    public LaData(int maxIterationsNumber, int maxRunsNumber, boolean isScheduleSearch, boolean isStopCondition, int deltaStop, int u, double allCProb, double allDProb, double KCProb, double KDProb, double KDCProb, int maxK, double probSensorOn, int h, double epslion, boolean isEvolutionaryStrategyChange, boolean isRTSPlusStrategy, double probReadyToShare, boolean isRTS, boolean isStrategyCompetition, int runsNumber) {
        this.maxIterationsNumber = maxIterationsNumber;
        this.maxRunsNumber = maxRunsNumber;
        this.isScheduleSearch = isScheduleSearch;
        this.isStopCondition = isStopCondition;
        this.deltaStop = deltaStop;
        this.u = u;
        this.allCProb = allCProb;
        this.KCProb = KCProb;
        this.KDProb = KDProb;
        this.KDCProb = KDCProb;
        this.maxK = maxK;
        this.probSensorOn = probSensorOn;
        this.h = h;
        this.epslion = epslion;
        this.isEvolutionaryStrategyChange = isEvolutionaryStrategyChange;
        this.isRTSPlusStrategy = isRTSPlusStrategy;
        this.probReadyToShare = probReadyToShare;
        this.isRTS=isRTS;
        this.allDProb=allDProb;
        this.isStrategyCompetition=isStrategyCompetition;
        this.runNumber=runsNumber;
    }

    public int getRunNumber() {
        return runNumber;
    }

    public void setRunNumber(int runNumber) {
        this.runNumber = runNumber;
    }

    public boolean isStrategyCompetition() {
        return isStrategyCompetition;
    }

    public void setStrategyCompetition(boolean strategyCompetition) {
        isStrategyCompetition = strategyCompetition;
    }

    public double getSumOfStrategiesProb() {

        return allCProb+allDProb+KCProb+KDProb+KDCProb;
    }
    public String getStrategyChangeRangeRangeName()
    {
        if(isStrategyCompetition)
        {
            return "no strategy competition";
        }
        else
        {
            if(isRTSPlusStrategy)
            {
                return "RTS+ strategy";
            }
            else
            {
                return "RTS";
            }
        }
    }
}
