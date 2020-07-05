package lab4.debug;

import lab4.Dane;
import lab4.La.Environment;
import lab4.Node.Sensor;
import lab4.Utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DebugSingSol {
    Dane data;
    public DebugSingSol(Dane dane) {
        data=dane;
    }

    public void makeDebug()
    {
        Debug.buildNetwork(data);
        Environment environment=new Environment(data.listOfPoi,data.listOfSensors,data.getPromien());
        List<List<Sensor>>allPossibleStrategies=getStrategy(data.listOfSensors);
        int i =0;
        List<String[]>csvFileContent=new ArrayList<>();
        Utils.addExperimentData(data,csvFileContent,false);
        csvFileContent.add(Debug.getHeaderOfReward1File(data.listOfSensors.size()));
        for(var strategy:allPossibleStrategies)
        {

            environment.offAllSensors();
            environment.setSensorsStatesAccordingToList(strategy);
            ArrayList<String>line=new ArrayList<>();
            line.add(""+Utils.convertBinaryForLong(environment.getStateString()));

            Debug.addSensorStates(environment, line);
            Debug.addCoverageToCSV(environment, line);
            String[] myArray = new String[line.size()];
            line.toArray(myArray);
            csvFileContent.add(myArray);
            i++;

        }
        Debug.saveLinesToFile(csvFileContent,"debug/debugSingSolReward1.csv");


//reward2
        allPossibleStrategies=getStrategy(data.listOfSensors);
        i =0;
        csvFileContent=new ArrayList<>();
        Utils.addExperimentData(data,csvFileContent,false);
        csvFileContent.add(Debug.getHeaderOfReward2File(data.listOfSensors.size()));
        for(var strategy:allPossibleStrategies)
        {

            environment.offAllSensors();
            environment.setSensorsStatesAccordingToList(strategy);
            ArrayList<String>line=new ArrayList<>();
            line.add(""+Utils.convertBinaryForLong(environment.getStateString()));
            line.add(String.format("%.2f",environment.getCoverageRate()));

            Debug.addMStarToLine(environment, line);
            Debug.addRevardValueToLine(environment, line,data);
            String[] myArray = new String[line.size()];
            line.toArray(myArray);
            csvFileContent.add(myArray);
            i++;


        }
        Debug.saveLinesToFile(csvFileContent,"debug/debugSingSolReward2.csv");


    }

    private List<List<Sensor>> getStrategy(List<Sensor> environment) {
        List<List<Sensor>>listOfStrategues=new ArrayList<>();
        listOfStrategues.add(environment.stream().filter(sensor -> sensor.getStan()==1).collect(Collectors.toList()));
        return listOfStrategues;
    }
}
