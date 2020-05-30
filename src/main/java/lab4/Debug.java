package lab4;

import com.opencsv.CSVWriter;
import lab4.La.Environment;
import lab4.Node.Sensor;
import lab4.Utils.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Debug {


    public static void buildNetwork(Dane data) {
        Utils.connectSensorsWithPoi(data.getListOfPoi(),data.getListOfSensors(),data.getPromien());
        for(Sensor sensor:data.getListOfSensors())
        {
            sensor.connectWithNeighbors();
        }
    }

    public static void createReward1File(Dane data) {

        Environment environment=new Environment(data.listOfPoi,data.listOfSensors,data.getPromien());
        List<List<Sensor>>allPossibleStrategies=getAllPossiblgeStrategies(environment);
        int i =0;
        List<String[]>csvFileContent=new ArrayList<>();
        csvFileContent.add(getHeaderOfReward1File());
        for(var strategy:allPossibleStrategies)
        {

            environment.offAllSensors();
            environment.setSensorsStatesAccordingToList(strategy);
            ArrayList<String>line=new ArrayList<>();
            line.add(""+i);

            addSensorStates(environment, line);
            addCoverageToCSV(environment, line);
            String[] myArray = new String[line.size()];
            line.toArray(myArray);
            csvFileContent.add(myArray);
            i++;

        }
        saveLinesToFile(csvFileContent,"reward1.csv");

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

    private static void saveLinesToFile(List<String[]> csvFileContent,String fileName) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(fileName));
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
//        Collections.reverse(environment.sensorsList);
        for(var sensor:environment.sensorsList)
        {
            line.add(sensor.getStan()+"");
        }
//        Collections.reverse(environment.sensorsList);
    }


    private static List<List<Sensor>> getAllPossiblgeStrategies(Environment environment) {
        int statesNumber=(int)Math.pow(2.0,(double) environment.sensorsList.size());
        List<List<Sensor>>solutionsList=new ArrayList<>();
        Collections.reverse(environment.sensorsList);
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
            solutionsList.add(environment.getSoulution());

        }
        Collections.reverse(environment.sensorsList);
        return solutionsList;
    }

    public static void createReward2File(Dane data) {
        Environment environment=new Environment(data.listOfPoi,data.listOfSensors,data.getPromien());
        List<List<Sensor>>allPossibleStrategies=getAllPossiblgeStrategies(environment);
        int i =0;
        List<String[]>csvFileContent=new ArrayList<>();
        csvFileContent.add(getHeaderOfReward2File());
        for(var strategy:allPossibleStrategies)
        {

            environment.offAllSensors();
            environment.setSensorsStatesAccordingToList(strategy);
            ArrayList<String>line=new ArrayList<>();
            line.add(""+i);
            line.add(String.format("%.2f",environment.getCoverageRate()));
            if(i==27)
            {
                System.out.println("jest 27");
            }
            addMStarToLine(environment, line);
            addRevardValueToLine(environment, line,data);
            String[] myArray = new String[line.size()];
            line.toArray(myArray);
            csvFileContent.add(myArray);
            i++;


        }
        saveLinesToFile(csvFileContent,"reward2.csv");
    }

    private static void addRevardValueToLine(Environment environment, ArrayList<String> line,Dane data) {
        boolean isNashPoint=true;
        double rewardSum=0;
        for(var sensor:environment.sensorsList)
        {
            double currentReward=sensor.computeReword(data,environment.sensorsList);

            sensor.switchState();
            double rewardForOther=sensor.computeReword(data,environment.sensorsList);
            sensor.switchState();

            if(rewardForOther>currentReward)
            {
                isNashPoint=false;
            }
            line.add(String.format("%.2f",sensor.computeReword(data,environment.sensorsList)));
            rewardSum+=currentReward;

        }
        line.add(String.format("%.2f",rewardSum/environment.sensorsList.size()));
        line.add(isNashPoint+"");

    }

    private static void addMStarToLine(Environment environment, ArrayList<String> line) {
        for(var sensor:environment.sensorsList)
        {
            if(sensor.getStan()==1)
                line.add(String.format("%.2f",sensor.getMStar()));
            else
                line.add("-");
        }
    }

    private static String[] getHeaderOfReward2File() {

        String[]header=new String[15];
        header[0]="s";
        header[1]="q_cur";
        for(int i =0;i<5;i++)
        {
            header[i+2]="m"+(i+1);
        }
        for(int i =0;i<5;i++)
        {
            header[i+7]="rev"+(i+1);
        }
        header[12]="reward mean";
        header[13]="Is nash point";
        return header;
    }
}
