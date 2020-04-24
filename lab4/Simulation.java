package lab4;

import java.util.List;

public class Simulation  {

    Dane dane;

    public Simulation(Dane dane,Wyswietlanie visualization) {
        this.dane = dane;
    }

    void run()
    {
        for(List<Sensor> setOfSensors : dane.getListsOfSensorsForEachSecond())
        {
            this.dane.getListOfSensors().forEach((sensor)->sensor.stan=0);
            setOfSensors.forEach((sensor)->sensor.stan=1);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
