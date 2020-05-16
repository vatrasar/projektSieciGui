package lab4.La.strategies;

import lab4.Sensor;

import java.util.List;

public class KCStrategy implements Strategy{
    @Override
    public int decideAboutSensorState(List<Sensor> neighbours, int k) {

        int onNeighboursNumber = getOnNeighboursNumber(neighbours);

        if(onNeighboursNumber<=k)
        {
            return 1;
        }else
            return 0;
    }

    private int getOnNeighboursNumber(List<Sensor> neighbours) {
        int onNeighboursNumber=0;
        for(Sensor sensor:neighbours)
        {
            if(sensor.getStan()==1)
            {
                onNeighboursNumber++;
            }
        }
        return onNeighboursNumber;
    }

    @Override
    public String getName() {
        return "KC";
    }
}