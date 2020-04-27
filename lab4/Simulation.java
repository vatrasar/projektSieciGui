package lab4;

import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Simulation extends Thread {

    private final boolean isDebug;
    Dane data;
    private final Wyswietlanie visualization;

    public Simulation(Dane data,Wyswietlanie visualization,boolean isDebug) {
        this.data = data;
        this.visualization=visualization;
        this.isDebug=isDebug;
    }

    @Override
    public void run() {
        super.run();

        for(List<Sensor> setOfSensors : data.getListsOfSensorsForEachSecond())
        {

            try {

                this.data.getListOfSensors().forEach((sensor)->sensor.stan=0);
                setOfSensors.forEach((sensor)->sensor.stan=1);
                double coverRate=computeCoverRate();
                this.visualization.aktualizacja(coverRate);
                if (isDebug)
                    synchronized (this)
                    {
                        this.wait();
                    }

                else
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
