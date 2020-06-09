package lab4.La.strategies;


import lab4.Node.Sensor;

import java.util.List;

public class KDStrategy implements Strategy{
    @Override
    public int decideAboutSensorState(List<Sensor> neighbours, int k) {
        int offNeighboursNumber = getOffNeighboursNumber(neighbours);

        if(offNeighboursNumber<=k)
        {
            return 1;
        }else
            return 0;
    }

    @Override
    public String getName() {
        return "KD";
    }
    private int getOffNeighboursNumber(List<Sensor> neighbours) {
        int offNeighboursNumber=0;
        for(Sensor sensor:neighbours)
        {
            if(sensor.getStan()==0)
            {
                offNeighboursNumber++;
            }
        }
        return offNeighboursNumber;
    }

    public static String getStaticName()
    {
        return "KD";
    }
}
