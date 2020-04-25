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

    @Override
    public void run() {
        super.run();
        for(List<Sensor> setOfSensors : data.getListsOfSensorsForEachSecond())
        {
            this.data.getListOfSensors().forEach((sensor)->sensor.stan=0);
            setOfSensors.forEach((sensor)->sensor.stan=1);
            try {

                this.visualization.aktualizacja();
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.visualization.setVisible(false);
        this.visualization.dispose();
    }



}
