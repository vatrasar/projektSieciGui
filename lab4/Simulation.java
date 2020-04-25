package lab4;

import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Simulation extends Thread {

    Dane data;
    private final Wyswietlanie visualization;

    public Simulation(Dane data,Wyswietlanie visualization) {
        this.data = data;
        this.visualization=visualization;
    }
    public void connectSensorsWithPoi()
    {
        for(Poi poi:data.getListOfPoi())
        {
            for(Sensor sensor:data.getListOfSensors())
            {
                if(Utils.computeDistance(sensor,poi)<=data.getPromien())
                {
                    poi.coveringSensorsList.add(sensor);
                }
            }
        }

    }
    @Override
    public void run() {
        super.run();
        connectSensorsWithPoi();
        for(List<Sensor> setOfSensors : data.getListsOfSensorsForEachSecond())
        {

            try {

                this.data.getListOfSensors().forEach((sensor)->sensor.stan=0);
                setOfSensors.forEach((sensor)->sensor.stan=1);
                double coverRate=computeCoverRate();
                this.visualization.aktualizacja(coverRate);
                Thread.sleep(1000);



            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.visualization.setVisible(false);
        this.visualization.dispose();
    }

    private double computeCoverRate() {
        int coveredPois=0;
        for(Poi poi:data.getListOfPoi())
        {
            if(poi.coveringSensorsList.stream().anyMatch(x->x.stan==1))
            {
                coveredPois++;
            }
        }
        return ((double) coveredPois)/data.getListOfPoi().size();

    }


}
