package lab4.La.strategies;

import lab4.Sensor;

import java.util.List;

public interface Strategy {
    public int decideAboutSensorState(List<Sensor>neighbours,int k);
    public String getName();
}