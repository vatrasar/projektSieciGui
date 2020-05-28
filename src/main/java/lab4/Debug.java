package lab4;

import com.opencsv.CSVWriter;
import lab4.La.Environment;
import lab4.Node.Sensor;
import lab4.Utils.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Debug {


    public static void buildNetwork(Dane data) {
        Utils.connectSensorsWithPoi(data.getListOfPoi(),data.getListOfSensors(),data.getPromien());
        for(Sensor sensor:data.getListOfSensors())
        {
            sensor.connectWithNeighbors();
        }
    }

    public static void checkAllStatesReward(Dane data) {

        Environment environment=new Environment(data.listOfPoi,data.listOfSensors,data.getPromien());
        List<List<Sensor>>allPossibleStrategies=getAllPossiblgeStrategies(environment);
        int i =0;
        List<String[]>csvFileContent=new ArrayList<>();
        csvFileContent.add(getHeaderOfReward1File());
        for(var strategy:allPossibleStrategies)
        {
            i++;
            environment.offAllSensors();
            environment.setSensorsStatesAccordingToList(strategy);
            ArrayList<String>line=new ArrayList<>();
            line.add(""+i);

            addSensorStates(environment, line);
            addCoverageToCSV(environment, line);
            String[] myArray = new String[line.size()];
            line.toArray(myArray);
            csvFileContent.add(myArray);


        }
        saveLinesToFile(csvFileContent);

    }

    private static String[] getHeaderOfReward1File() {
        String[]header=new String[11];
        header[0]="s";
        for(int i =0;i<6;i++)
        {
            header[i+1]=""+(i+1);
        }
        for(int i =6;i<11;i++)
        {
            header[i]="q"+(i-5);
        }
        return header;
    }

    private static void saveLinesToFile(List<String[]> csvFileContent) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter("reward1.csv"));
            writer.writeAll(csvFileContent);
            writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addCoverageToCSV(Environment environment, List<String> line) {
        for(var sensor:environment.sensorsList)
        {

            line.add(String.format("%s (%.2f)",sensor.getPoiCoverageString(),sensor.getCurrentLocalCoverageRate()));
        }
    }

    private static void addSensorStates(Environment environment, List<String> line) {
        for(var sensor:environment.sensorsList)
        {
            line.add(sensor.getStan()+"");
        }
    }


    private static List<List<Sensor>> getAllPossiblgeStrategies(Environment environment) {
        int statesNumber=(int)Math.pow(2.0,(double) environment.sensorsList.size());
        List<List<Sensor>>solutionsList=new ArrayList<>();
        for(int i=0;i<statesNumber;i++)
        {
            for(var sensor:environment.sensorsList)
            {
                if(sensor.getStan()==1)
                {
                    sensor.setStan(0);
                }
                else {
                    sensor.setStan(1);
                    break;
                }
            }
            solutionsList.add(environment.getSoulution(environment.sensorsList));

        }
        return solutionsList;
    }
}
