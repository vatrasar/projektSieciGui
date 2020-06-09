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
            header[i+2]="m*"+(i+1);
        }
        for(int i =0;i<5;i++)
        {
            header[i+7]="rev"+(i+1);
        }
        header[12]="reward mean";
        header[13]="Is nash point";
        return header;
    }
    public static void produceDebugFilesAfertGettingSolution(Statistics statistics,Environment environment)
    {
        makeLaSolutionFile(statistics,environment);
        makeLaResLocals(statistics);
        makeLaResults(statistics,environment);
    }

    private static void makeLaResLocals(Statistics statistics) {

        ArrayList<String[]>lines=new ArrayList<>();


        //make header
        int sensorsNumber=statistics.getRunsStateList().get(0).get(0).size();
        String[]firstLine=new String[sensorsNumber*3+4];
        firstLine[0]="iter";
        //add qi
        for(int i=0;i<sensorsNumber;i++)
        {
            firstLine[i+1]="q"+(i+1);

        }
        //add si
        for(int i=0;i<sensorsNumber;i++)
        {
            firstLine[i+1+sensorsNumber]="rev"+(i+1);

        }
        firstLine[1+2*sensorsNumber]="revavg";
        //add k usage
        for(int i=0;i<sensorsNumber;i++)
        {
            firstLine[i+2+2*sensorsNumber]="k"+(i+1);

        }
        firstLine[2+3*sensorsNumber]="kavg";


        //add data to file
        lines.add(firstLine);
        int iterationCounter=0;
        List<Double>coverageRate=statistics.getProcentOfCoveredPoi(1);
        for(var iteration:statistics.getRunsStateList().get(0))
        {

            String[] line=new String[sensorsNumber*3+4];
            iterationCounter++;
            line[0]=iterationCounter+"";
            int sensorCounter=0;

            //local coverage
            for(var sensorCoverage:statistics.getLocalCoveredPoisRate().get(0).get(iterationCounter-1))
            {
                line[sensorCounter+1]=sensorCoverage+"";
                sensorCounter++;

            }
            //local revards
            for(var sensor:iteration)
            {
                line[sensorCounter+1]=sensor.getLastReward()+"";
                sensorCounter++;

            }
            line[sensorCounter+1]=statistics.getMeanRewardsForEachItereationOfRun(1).get(iterationCounter-1)+"";
            sensorCounter++;

            //k
            double kSum=0;
            for(var sensor:iteration)
            {
                line[sensorCounter+1]=sensor.getK()+"";
                sensorCounter++;
                kSum+=sensor.getK();

            }

            line[sensorCounter+1]=kSum/sensorsNumber+"";


            lines.add(line);
        }


        saveLinesToFile(lines,"La-res-local.csv");
    }

    private static void makeLaSolutionFile(Statistics statistics,Environment environment) {

        List<String[]>linesList=new ArrayList<>();
        int counter=0;

        //header
        for(var run:statistics.getResultShedule())
        {
            counter++;
            int columnsNumber=2+environment.sensorsList.size();
            String[]line=new String[columnsNumber];

            line[0]="run_num";
            line[1]="q_opt";
            for(int i=0;i<environment.sensorsList.size();i++)
            {
                line[i+2]="s"+counter;
            }
            linesList.add(line);
            break;
        }


        //data
        for(var run:statistics.getResultShedule())
        {
            counter++;
            int columnsNumber=2+environment.sensorsList.size();
            String[]line=new String[columnsNumber];
            environment.setSensorsStatesAccordingToList(run);
            line[0]=counter+" ";
            line[1]=environment.getCoverageRate()+" ";
            for(int i=0;i<environment.sensorsList.size();i++)
            {
                line[i+2]=environment.sensorsList.get(i).getStan()+" ";
            }
            linesList.add(line);

        }
        saveLinesToFile(linesList,"La-found-solution.csv");

    }



    private static void makeLaResults(Statistics statistics,Environment environment)
    {
        ArrayList<String[]>lines=new ArrayList<>();


        //make header
        int sensorsNumber=statistics.getRunsStateList().get(0).get(0).size();
        String[]firstLine=new String[12];
        firstLine[0]="iter";
        firstLine[1]="q_curr";
        firstLine[2]="av_rev";
        firstLine[3]="s_10";
        firstLine[4]="%m";
        firstLine[5]="%strategy";
        firstLine[6]="av_k";
        firstLine[7]="%allC";
        firstLine[8]="%allD";
        firstLine[9]="%KD";
        firstLine[10]="%KC";
        firstLine[11]="%KDC";
        lines.add(firstLine);

        int iterationCounter=0;
        for(var iteration:statistics.getRunsStateList().get(0))
        {

            String[] line=new String[12];
            iterationCounter++;
            line[0]=iterationCounter+"";
            int sensorCounter=0;

            //coverage
            line[1]=statistics.getProcentOfCoveredPoi().get(0).get(iterationCounter-1)+"";
            //local revards
            double rewardSum=0;
            for(var sensor:iteration)
            {
                rewardSum+=sensor.getLastReward();

            }
            line[2]=rewardSum/iteration.size()+"";

            line[3]=Utils.getDigitNumberOfSolution(iteration)+"";
            line[4]=statistics.getProcentOfRTSUsageInRun(1).get(iterationCounter-1)+"";
            line[5]="do omówienia";

            //k
            double kSum=0;
            for(var sensor:iteration)
            {
                kSum+=sensor.getK();
            }
            line[6]=kSum/sensorsNumber+"";
            lines.add(line);

            //strategies
            line[7]=statistics.getProcetOfStrategies(1).get("ALLC").get(iterationCounter-1)+"";
            line[8]=statistics.getProcetOfStrategies(1).get("KC").get(iterationCounter-1)+"";
            line[9]=statistics.getProcetOfStrategies(1).get("KDC").get(iterationCounter-1)+"";
            line[10]=statistics.getProcetOfStrategies(1).get("KD").get(iterationCounter-1)+"";
            line[11]=statistics.getProcetOfStrategies(1).get("AllD").get(iterationCounter-1)+"";


        }


        saveLinesToFile(lines,"La-results.csv");
    }
}
