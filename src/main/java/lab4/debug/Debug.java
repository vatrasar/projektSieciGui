package lab4.debug;

import com.opencsv.CSVWriter;
import lab4.Dane;
import lab4.La.Environment;
import lab4.La.strategies.AllCStrategy;
import lab4.La.strategies.KCStrategy;
import lab4.La.strategies.KDCStrategy;
import lab4.La.strategies.KDStrategy;
import lab4.Node.Sensor;
import lab4.Statistics;
import lab4.Utils.Utils;
import lab4.Utils.GnuPlotExporter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        csvFileContent.add(getHeaderOfReward1File(data.getLiczbaSensorow()));
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

    private static String[] getHeaderOfReward1File(int sensorsNmuber) {
        String[]header=new String[1+2*sensorsNmuber];
        int columnCounter=0;
        header[columnCounter]="s";
        columnCounter++;
        for(int i =0;i<sensorsNmuber;i++)
        {
            header[columnCounter]=""+(i+1);
            columnCounter++;
        }
        for(int i =0;i<sensorsNmuber;i++)
        {
            header[columnCounter]="q"+(i+1);
            columnCounter++;
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
        int operation=0;
        for(int i=0;i<statesNumber;i++)
        {
            for(var sensor:environment.sensorsList)
            {
                operation++;
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
        csvFileContent.add(getHeaderOfReward2File(data.getLiczbaSensorow()));
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

    private static String[] getHeaderOfReward2File(int sensorNumber) {

        String[]header=new String[4+2*sensorNumber];
        int columnCounter=0;
        header[columnCounter]="s";
        columnCounter++;
        header[columnCounter]="q_cur";
        columnCounter++;

        for(int i =0;i<sensorNumber;i++)
        {
            header[columnCounter]="m*"+(i+1);
            columnCounter++;
        }
        for(int i =0;i<sensorNumber;i++)
        {
            header[columnCounter]="rev"+(i+1);
            columnCounter++;
        }
        header[columnCounter]="reward mean";
        columnCounter++;
        header[columnCounter]="Is nash point";
        columnCounter++;
        return header;
    }
    public static void produceDebugFilesAfertGettingSolution(Statistics statistics, Environment environment)
    {
        File file = new File("./debug");

        boolean dirCreated = file.mkdir();
        makeLaSolutionFile(statistics,environment);
        makeLaResLocals(statistics);
        makeLaResults(statistics,environment,0);
        makeLaStratFreq(statistics);
        makeLaOnOff(statistics);


    }



    private static void makeLaOnOff(Statistics statistics) {
        ArrayList<String[]>lines=new ArrayList<>();


        //make header
        int sensorsNumber=statistics.getRunsStateList().get(0).get(0).size();
        int iterationsNumber=statistics.getRunsStateList().get(0).size();
        String[]firstLine=new String[sensorsNumber+2];
        int columnCounter=0;
        firstLine[columnCounter]="iter";
        columnCounter++;
        firstLine[columnCounter]="s_10";
        columnCounter++;
        //create sensors header
        for(int i=0;i<sensorsNumber;i++)
        {
            firstLine[columnCounter]="s"+(i+1);
            columnCounter++;
        }

        lines.add(firstLine);

        int iterationCounter=0;
        //data
        for(var iteration:statistics.getRunsStateList().get(0))
        {
            int columnIndex=0;
            String[] line=new String[sensorsNumber+2];
            line[columnIndex]=iterationCounter+"";
            columnIndex++;
            line[columnIndex]=Utils.getDigitNumberOfSolution(iteration)+"";
            columnIndex++;
            for(var sensor:iteration)
            {
                line[columnIndex]=sensor.getStan()+"";
                columnIndex++;
            }
            lines.add(line);
            iterationCounter++;


        }
        GnuPlotExporter.createDataFile(lines,"./debug/La-on-off.txt");
    }

    private static void makeLaStratFreq(Statistics statistics) {
        ArrayList<String[]>lines=new ArrayList<>();


        //make header
        int sensorsNumber=statistics.getRunsStateList().get(0).get(0).size();
        int iterationsNumber=statistics.getRunsStateList().get(0).size();
        String[]firstLine=new String[16];
        firstLine[0]="iter";
        firstLine[1]="%0D";
        firstLine[2]="%1D";
        firstLine[3]="2D";
        firstLine[4]="%3D";
        firstLine[5]="%othD";
        firstLine[6]="%0C";
        firstLine[7]="%1C";
        firstLine[8]="%2C";
        firstLine[9]="%3C";
        firstLine[10]="%othC";
        firstLine[11]="%0DC";
        firstLine[12]="%1DC";
        firstLine[13]="%2DC";
        firstLine[14]="%3DC";
        firstLine[15]="%othDC";

        lines.add(firstLine);


        //make rows
        Map<Integer,List<Double>>procentOfUsageOfEachKInKD=statistics.getProcentOfUsageOfEachKInStartegy(1, KDStrategy.getStaticName());
        Map<Integer,List<Double>>procentOfUsageOfEachKInKDC=statistics.getProcentOfUsageOfEachKInStartegy(1, KDCStrategy.getStaticName());
        Map<Integer,List<Double>>procentOfUsageOfEachKInKC=statistics.getProcentOfUsageOfEachKInStartegy(1, KCStrategy.getStaticName());



        for(int iterationCounter=0;iterationCounter<iterationsNumber;iterationCounter++)
        {

            String[] line=new String[16];

            line[0]=(iterationCounter)+"";
            double D0=getKUsageInIteration(procentOfUsageOfEachKInKD,0,iterationCounter);
            double D1=getKUsageInIteration(procentOfUsageOfEachKInKD,1,iterationCounter);
            double D2=getKUsageInIteration(procentOfUsageOfEachKInKD,2,iterationCounter);
            double D3=getKUsageInIteration(procentOfUsageOfEachKInKD,3,iterationCounter);
            double otherD=getOtherKUsageInIteration(procentOfUsageOfEachKInKD,3,iterationCounter);

            double C0=getKUsageInIteration(procentOfUsageOfEachKInKC,0,iterationCounter);
            double C1=getKUsageInIteration(procentOfUsageOfEachKInKC,1,iterationCounter);
            double C2=getKUsageInIteration(procentOfUsageOfEachKInKC,2,iterationCounter);
            double C3=getKUsageInIteration(procentOfUsageOfEachKInKC,3,iterationCounter);
            double otherC=getOtherKUsageInIteration(procentOfUsageOfEachKInKC,3,iterationCounter);

            double DC0=getKUsageInIteration(procentOfUsageOfEachKInKDC,0,iterationCounter);
            double DC1=getKUsageInIteration(procentOfUsageOfEachKInKDC,1,iterationCounter);
            double DC2=getKUsageInIteration(procentOfUsageOfEachKInKDC,2,iterationCounter);
            double DC3=getKUsageInIteration(procentOfUsageOfEachKInKDC,3,iterationCounter);
            double otherDC=getOtherKUsageInIteration(procentOfUsageOfEachKInKDC,3,iterationCounter);

            line[1]=Utils.stringFormater(D0)+"";
            line[2]=Utils.stringFormater(D1)+"";
            line[3]=Utils.stringFormater(D2)+"";
            line[4]=Utils.stringFormater(D3)+"";
            line[5]=Utils.stringFormater(otherD)+"";

            line[6]=Utils.stringFormater(C0)+"";
            line[7]=Utils.stringFormater(C1)+"";
            line[8]=Utils.stringFormater(C2)+"";
            line[9]=Utils.stringFormater(C3)+"";
            line[10]=Utils.stringFormater(otherC)+"";


            line[11]=Utils.stringFormater(DC0)+"";
            line[12]=Utils.stringFormater(DC1)+"";
            line[13]=Utils.stringFormater(DC2)+"";
            line[14]=Utils.stringFormater(DC3)+"";
            line[15]=Utils.stringFormater(otherDC)+"";
            lines.add(line);

        }
        GnuPlotExporter.createDataFile(lines,"./debug/LaStratFreq.txt");

    }

    private static double getOtherKUsageInIteration(Map<Integer, List<Double>> procentOfUsageOfEachKInKD, int kBigerThen, int iterationCounter) {
        double otherUsgeSum=0.0;
        for(var entry:procentOfUsageOfEachKInKD.entrySet())
        {

            if(entry.getKey()>kBigerThen)
            {
                otherUsgeSum+=entry.getValue().get(iterationCounter);
            }
        }
        return otherUsgeSum;
    }

    private static double getKUsageInIteration(Map<Integer, List<Double>> procentOfUsageOfEachKInKC, int k, int iterationCounter) {
        double result=0.0;
        try {
            result=procentOfUsageOfEachKInKC.get(k).get(iterationCounter);

        }catch (NullPointerException e)
        {
            result=0;
        }
        return result;
    }


    private static void makeLaResLocals(Statistics statistics) {

        ArrayList<String[]>lines=new ArrayList<>();


        //make header
        int sensorsNumber=statistics.getRunsStateList().get(0).get(0).size();
        String[]firstLine=new String[sensorsNumber*3+3];
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

            String[] line=new String[sensorsNumber*3+3];

            line[0]=iterationCounter+"";
            int sensorCounter=0;

            //local coverage
            for(var sensorCoverage:statistics.getLocalCoveredPoisRate().get(0).get(iterationCounter))
            {
                line[sensorCounter+1]=Utils.stringFormater(sensorCoverage)+"";
                sensorCounter++;

            }
            //local revards
            for(var sensor:iteration)
            {
                line[sensorCounter+1]=Utils.stringFormater(sensor.getLastReward())+"";
                sensorCounter++;

            }
            line[sensorCounter+1]=Utils.stringFormater(statistics.getMeanRewardsForEachItereationOfRun(1).get(iterationCounter))+"";
            sensorCounter++;

            //k
            double kSum=0;
            for(var sensor:iteration)
            {
                line[sensorCounter+1]=sensor.getK()+"";
                sensorCounter++;
                kSum+=sensor.getK();

            }

            line[sensorCounter+1]=Utils.stringFormater(kSum/sensorsNumber)+"";


            lines.add(line);
            iterationCounter++;
        }


        GnuPlotExporter.createDataFile(lines,"./debug/La-res-local.txt");
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
        int runCounter=0;
        for(var run:statistics.getResultShedule())
        {

            int columnsNumber=2+environment.sensorsList.size();
            String[]line=new String[columnsNumber];
            environment.setSensorsStatesAccordingToList(run);
            line[0]=runCounter+" ";
            line[1]=Utils.stringFormater(environment.getCoverageRate())+" ";
            for(int i=0;i<environment.sensorsList.size();i++)
            {
                line[i+2]=environment.sensorsList.get(i).getStan()+" ";
            }
            linesList.add(line);
            runCounter++;
        }
        GnuPlotExporter.createDataFile(linesList,"./debug/La-found-solution.txt");

    }



    private static void makeLaResults(Statistics statistics,Environment environment,int runNumber)
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
        firstLine[7]="%ALLC";
        firstLine[8]="%KC";
        firstLine[9]="%KDC";
        firstLine[10]="%KD";
        firstLine[11]="%ALLD";
        lines.add(firstLine);

        int iterationCounter=0;
        for(var iteration:statistics.getRunsStateList().get(0))
        {

            String[] line=new String[12];

            line[0]=iterationCounter+"";
            int sensorCounter=0;

            //coverage
            line[1]=Utils.stringFormater(statistics.getProcentOfCoveredPoi().get(0).get(iterationCounter))+"";
            //local revards
            double rewardSum=0;
            for(var sensor:iteration)
            {
                rewardSum+=sensor.getLastReward();

            }
            line[2]=Utils.stringFormater(rewardSum/iteration.size())+"";

            line[3]=Utils.getDigitNumberOfSolution(iteration)+"";
            line[4]=Utils.stringFormater(statistics.getProcentOfRTSUsageInRun(1).get(iterationCounter))+"";
            line[5]=Utils.stringFormater(statistics.getStrategyChanged().get(runNumber).get(iterationCounter))+"";

            //k
            double kSum=0;
            for(var sensor:iteration)
            {
                kSum+=sensor.getK();
            }
            line[6]=Utils.stringFormater(kSum/sensorsNumber)+"";
            lines.add(line);

            //strategies
            line[7]=Utils.stringFormater(statistics.getProcetOfStrategies(1).get("ALLC").get(iterationCounter))+"";
            line[8]=Utils.stringFormater(statistics.getProcetOfStrategies(1).get("KC").get(iterationCounter))+"";
            line[9]=Utils.stringFormater(statistics.getProcetOfStrategies(1).get("KDC").get(iterationCounter))+"";
            line[10]=Utils.stringFormater(statistics.getProcetOfStrategies(1).get("KD").get(iterationCounter))+"";
            line[11]=Utils.stringFormater(statistics.getProcetOfStrategies(1).get("AllD").get(iterationCounter))+"";

            iterationCounter++;
        }


        GnuPlotExporter.createDataFile(lines,"./debug/La-results.txt");
    }
}
