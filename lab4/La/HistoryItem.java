package lab4.La;

import lab4.La.strategies.Strategy;

public class HistoryItem {
    double reward;
    Strategy strategy;

    public HistoryItem(double reward, Strategy strategy) {
        this.reward = reward;
        this.strategy = strategy;
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
