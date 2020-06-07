package lab4.La;

import lab4.La.strategies.Strategy;
import lab4.Node.Node;
import lab4.Utils.ToClone;

public class HistoryItem implements ToClone {
    double reward;
    Strategy strategy;
    static int globalIndex;
    int index;
//    int k;
//    boolean isRTS;

    public HistoryItem(double reward, Strategy strategy) {
        this.reward = reward;
        this.strategy = strategy;
//        this.k=k;
//        this.isRTS=isRTS;
        index=globalIndex+1;
    }

    @Override
    public ToClone clone() {
        HistoryItem toClone=new HistoryItem(reward,strategy);
        return toClone;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }


}
