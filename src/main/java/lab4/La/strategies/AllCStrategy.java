package lab4.La.strategies;



import lab4.Node.Sensor;

import java.util.List;

public class AllCStrategy implements Strategy {

    @Override
    public int decideAboutSensorState(List<Sensor> neighbours, int k) {
        return 1;
    }

    @Override
    public String getName() {
        return "ALLC";
    }
}
