package lab4.La;

import lab4.La.strategies.Strategy;

public class HistoryItem {
    double reward;
    Strategy strategy;
    static int globalIndex;
    int index;

    public HistoryItem(double reward, Strategy strategy) {
        this.reward = reward;
        this.strategy = strategy;

        index=globalIndex+1;
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
