package lab4.La;

public class LaData {
    int maxIterationsNumber;
    int maxRunsNumber;
    boolean isScheduleSearch;
    boolean isStopCondition;
    int deltaStop;
    int u;
    double allCProb;
    double KCProb;
    double KDProb;
    double KDCProb;
    int maxK;
    double probSensorOn;
    public int h;
    double epslion;
    boolean isEvolutionaryStrategyChange;
    boolean isRTSPlusStrategy;
    double probReadyToShare;

    public LaData(int maxIterationsNumber, int maxRunsNumber, boolean isScheduleSearch, boolean isStopCondition, int deltaStop, int u, double allCProb, double KCProb, double KDProb, double KDCProb, int maxK, double probSensorOn, int h, double epslion, boolean isEvolutionaryStrategyChange, boolean isRTSPlusStrategy, double probReadyToShare) {
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
    }
}
