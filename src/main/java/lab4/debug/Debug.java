package lab4.debug;
import com.opencsv.CSVWriter;
import lab4.Dane;
import lab4.La.Environment;
import lab4.La.strategies.KCStrategy;
import lab4.La.strategies.KDCStrategy;
import lab4.La.strategies.KDStrategy;
import lab4.Node.Sensor;
import lab4.Statistics;
import lab4.Utils.Utils;
import lab4.Utils.GnuPlotExporter;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class Debug {


    public static void buildNetwork(Dane data) {
        Utils.connectSensorsWithPoi(data.getListOfPoi(),data.getListOfSensors(),data.getPromien());
        for(Sensor sensor:data.getListOfSensors())
        {
            sensor.connectWithNeighbors();
        }
    }

    public static void createReward1File(Dane data, boolean isOnlyNeshPoints) {

        Environment environment=new Environment(data.listOfPoi,data.listOfSensors,data.getPromien());
        List<List<Sensor>>allPossibleStrategies=getAllPossiblgeStrategies(environment);
        int i =0;
        List<String[]>csvFileContent=new ArrayList<>();
        Utils.addExperimentData(data,csvFileContent,false);
        csvFileContent.add(getHeaderOfReward1File(data.getLiczbaSensorow()));
        for(var strategy:allPossibleStrategies)
        {

            environment.offAllSensors();
            environment.setSensorsStatesAccordingToList(strategy);
            ArrayList<String>line=new ArrayList<>();
            line.add(""+i);
            boolean isNashPoint=addRevardValueToLine(environment, new ArrayList<>(),data);
            addSensorStates(environment, line);
            addCoverageToCSV(environment, line);
            if(isNashPoint || !isOnlyNeshPoints)
            {
                String[] myArray = new String[line.size()];
                line.toArray(myArray);
                csvFileContent.add(myArray);
            }

            i++;

        }
        saveLinesToFile(csvFileContent,"reward1.csv");

    }

    public static String[] getHeaderOfReward1File(int sensorsNmuber) {
        String[]header=new String[1+2*sensorsNmuber];
        int columnCounter=0;
        header[columnCounter]="s";
        columnCounter++;
        for(int i =0;i<sensorsNmuber;i++)
        {
            header[columnCounter]="s"+(i+1);
            columnCounter++;
        }
        for(int i =0;i<sensorsNmuber;i++)
        {
            header[columnCounter]="q"+(i+1);
            columnCounter++;
        }
        return header;
    }

    public static void saveLinesToFile(List<String[]> csvFileContent, String fileName) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(fileName));
            writer.writeAll(csvFileContent);
            writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addCoverageToCSV(Environment environment, List<String> line) {
        for(var sensor:environment.sensorsList)
        {

            line.add(String.format("%s (%.2f)",sensor.getPoiCoverageString(),sensor.getCurrentLocalCoverageRate()));
        }
    }

    public static void addSensorStates(Environment environment, List<String> line) {
//        Collections.reverse(environment.sensorsList);
        for(var sensor:environment.sensorsList)
        {
            line.add(sensor.getStan()+"");
        }
//        Collections.reverse(environment.sensorsList);
    }


    private static List<List<Sensor>> getAllPossiblgeStrategies(Environment environment) {
//        BigInteger bigStatesNumber=new BigInteger("2").pow(environment.sensorsList.size());

        double statesNumber=(int)Math.pow(2.0,(double) environment.sensorsList.size());
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

    public static void createReward2File(Dane data, boolean isOnlyNeshPoints) {
        Environment environment=new Environment(data.listOfPoi,data.listOfSensors,data.getPromien());
        List<List<Sensor>>allPossibleStrategies=getAllPossiblgeStrategies(environment);
        int i =0;
        List<String[]>csvFileContent=new ArrayList<>();
        Utils.addExperimentData(data,csvFileContent,false);
        csvFileContent.add(getHeaderOfReward2File(data.getLiczbaSensorow()));
        for(var strategy:allPossibleStrategies)
        {

            environment.offAllSensors();
            environment.setSensorsStatesAccordingToList(strategy);
            ArrayList<String>line=new ArrayList<>();
            line.add(""+i);
            line.add(Utils.stringFormater(environment.getCoverageRate()));

            addMStarToLine(environment, line);
            boolean isNashPoint=addRevardValueToLine(environment, line,data);

            if(isNashPoint || !isOnlyNeshPoints) {
                String[] myArray = new String[line.size()];

                line.toArray(myArray);
                csvFileContent.add(myArray);
            }
            i++;


        }
        saveLinesToFile(csvFileContent,"reward2.csv");
    }

    /**
     * @param environment
     * @param line
     * @param data
     * @return isNeshPoint
     */
    public static boolean addRevardValueToLine(Environment environment, ArrayList<String> line, Dane data) {
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
            line.add(Utils.stringFormater(sensor.computeReword(data,environment.sensorsList)));
            rewardSum+=currentReward;

        }
        line.add(Utils.stringFormater(rewardSum/environment.sensorsList.size()));
        line.add(isNashPoint+"");
        return isNashPoint;

    }

    public static void addMStarToLine(Environment environment, ArrayList<String> line) {
        for(var sensor:environment.sensorsList)
        {
            if(sensor.getStan()==1)
                line.add(Utils.stringFormater(sensor.getMStar()));
            else
                line.add("-");
        }
    }

    public static String[] getHeaderOfReward2File(int sensorNumber) {

        String[]header=new String[4+2*sensorNumber];
        int columnCounter=0;
        header[columnCounter]="#s";
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


    public static void produceDebugFilesAfertGettingSolution(Statistics statistics, Environment environment, Dane data, List<List<Sensor>> result, int runsCounter)
    {
        File file = new File("./debug");

        boolean dirCreated = file.mkdir();
        if(data.laData.runNumber==1){
            makeLaSolutionFile(statistics,environment,data);

            makeLaResLocals(statistics,data,environment.sensorsList.size()<=8);
            makeLaResults(statistics,environment,0,data,runsCounter);
            makeLaStratFreq(statistics,data);
            makeLaOnOff(statistics,data);

            makeLaNorOper(environment,result,data);
        }else{
            makeLaResults(statistics,environment,0,data,runsCounter);
        }



    }

    private static void makeLaNorOper(Environment environment, List<List<Sensor>> result,Dane data) {
        ArrayList<String[]>lines=new ArrayList<>();
        Utils.addExperimentData(data,lines, true);

        //make header
        int sensorsNumber=environment.sensorsList.size();
       String[]header=new String[sensorsNumber+1];
       header[0]="#t";
       int columncounter=1;
       for(int i=0;i<sensorsNumber;i++)
       {
           header[columncounter]="s"+(i+1);
       }

        //data
        if(result.size()>0) {
            int iterationCounter = 0;
            for (var iteration : result) {
                int columnIndex = 0;
                String[] line = new String[sensorsNumber + 1];
                line[columnIndex] = iterationCounter + "";
                columnIndex++;
//            line[columnIndex]=Utils.getDigitNumberOfSolution(iteration)+"";
                environment.offAllSensors();
                environment.setSensorsStatesAccordingToList(iteration);
                for (var sensor : environment.sensorsList) {
                    line[columnIndex] = sensor.getStan() + "";
                    columnIndex++;
                }
                lines.add(line);
                iterationCounter++;


            }
        }
        GnuPlotExporter.createDataFile(lines,"./results/LA-nor-oper.txt",true);

    }


    private static void makeLaOnOff(Statistics statistics,Dane data) {
        ArrayList<String[]>lines=new ArrayList<>();
        Utils.addExperimentData(data,lines, true);

        //make header
        int sensorsNumber=statistics.getRunsStateList().get(0).get(0).size();
        int iterationsNumber=statistics.getRunsStateList().get(0).size();
        String[]firstLine=new String[sensorsNumber+2];
        int columnCounter=0;
        firstLine[columnCounter]="#iter";
        columnCounter++;

        //create sensors header
        for(int i=0;i<sensorsNumber;i++)
        {
            firstLine[columnCounter]="s"+(i+1);
            columnCounter++;
        }
        firstLine[columnCounter]="s_10";
        columnCounter++;
        lines.add(firstLine);

        int iterationCounter=0;
        //data
        for(var iteration:statistics.getRunsStateList().get(0))
        {
            int columnIndex=0;
            String[] line=new String[sensorsNumber+2];
            line[columnIndex]=iterationCounter+"";
            columnIndex++;

            for(var sensor:iteration)
            {
                line[columnIndex]=sensor.getStan()+"";
                columnIndex++;
            }
            line[columnIndex]=Utils.getDigitNumberOfSolution(iteration)+"";
            columnIndex++;
            lines.add(line);
            iterationCounter++;


        }
        GnuPlotExporter.createDataFile(lines,"./results/La-on-off.txt",true);
    }

    private static void makeLaStratFreq(Statistics statistics, Dane data) {
        ArrayList<String[]>lines=new ArrayList<>();

        Utils.addExperimentData(data,lines, true);
        //make header
        int sensorsNumber=statistics.getRunsStateList().get(0).get(0).size();
        int iterationsNumber=statistics.getRunsStateList().get(0).size();
        String[]firstLine=new String[16];
        firstLine[0]="#iter";
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
        GnuPlotExporter.createDataFile(lines,"./results/LaStratFreq.txt",true);

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


    private static void makeLaResLocals(Statistics statistics,Dane data,boolean toPrint) {

        ArrayList<String[]>lines=new ArrayList<>();
        Utils.addExperimentData(data,lines, true);

        //make header
        int sensorsNumber=statistics.getRunsStateList().get(0).get(0).size();
        String[]firstLine=new String[sensorsNumber*3+3];
        firstLine[0]="#iter";
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
        if(toPrint) {
            int iterationCounter = 0;
            List<Double> coverageRate = statistics.getProcentOfCoveredPoi(1);
            for (var iteration : statistics.getRunsStateList().get(0)) {

                String[] line = new String[sensorsNumber * 3 + 3];

                line[0] = iterationCounter + "";
                int sensorCounter = 0;

                //local coverage
                for (var sensorCoverage : statistics.getLocalCoveredPoisRate().get(0).get(iterationCounter)) {
                    line[sensorCounter + 1] = Utils.stringFormater(sensorCoverage) + "";
                    sensorCounter++;

                }
                //local revards
                for (var sensor : iteration) {
                    line[sensorCounter + 1] = Utils.stringFormater(sensor.getLastReward()) + "";
                    sensorCounter++;

                }
                line[sensorCounter + 1] = Utils.stringFormater(statistics.getMeanRewardsForEachItereationOfRun(1).get(iterationCounter)) + "";
                sensorCounter++;

                //k
                double kSum = 0;
                for (var sensor : iteration) {
                    line[sensorCounter + 1] = sensor.getK() + "";
                    sensorCounter++;
                    kSum += sensor.getK();

                }

                line[sensorCounter + 1] = Utils.stringFormater(kSum / sensorsNumber) + "";


                lines.add(line);
                iterationCounter++;
            }
        }

        GnuPlotExporter.createDataFile(lines,"./results/La-res-local.txt",true);
    }

    private static void makeLaSolutionFile(Statistics statistics, Environment environment, Dane data) {

        List<String[]>linesList=new ArrayList<>();
        int counter=0;
        Utils.addExperimentData(data,linesList, true);
        //header
        for(var run:statistics.getResultShedule())
        {
            counter++;
            int columnsNumber=2+environment.sensorsList.size();
            String[]line=new String[columnsNumber];

            line[0]="#run_num";
            line[1]="q_opt";
            for(int i=0;i<environment.sensorsList.size();i++)
            {
                line[i+2]="s"+counter;
                counter++;
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
        linesList.remove(linesList.size()-1);
        GnuPlotExporter.createDataFile(linesList,"./results/La-found-solution.txt",true);

    }



    private static void makeLaResults(Statistics statistics, Environment environment, int runNumber, Dane data, int runsCounter)
    {
        ArrayList<String[]>lines=new ArrayList<>();
        Utils.addExperimentData(data,lines, true);

        //make header
        int sensorsNumber=statistics.getRunsStateList().get(0).get(0).size();
        String[]firstLine=new String[12];
        firstLine[0]="#iter";
        firstLine[1]="q_curr";
        firstLine[2]="av_rev";

        firstLine[3]="%m";
        firstLine[4]="%strategy";
        firstLine[5]="av_k";
        firstLine[6]="%ALLC";
        firstLine[7]="%KC";
        firstLine[8]="%KDC";
        firstLine[9]="%KD";
        firstLine[10]="%ALLD";
        firstLine[11]="s_10";

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


            line[3]=Utils.stringFormater(statistics.getProcentOfRTSUsageInRun(1).get(iterationCounter))+"";
            line[4]=Utils.stringFormater(statistics.getStrategyChanged().get(runNumber).get(iterationCounter))+"";

            //k
            double kSum=0;
            for(var sensor:iteration)
            {
                kSum+=sensor.getK();
            }
            line[5]=Utils.stringFormater(kSum/sensorsNumber)+"";
            lines.add(line);

            //strategies
            line[6]=Utils.stringFormater(statistics.getProcetOfStrategies(1).get("ALLC").get(iterationCounter))+"";
            line[7]=Utils.stringFormater(statistics.getProcetOfStrategies(1).get("KC").get(iterationCounter))+"";
            line[8]=Utils.stringFormater(statistics.getProcetOfStrategies(1).get("KDC").get(iterationCounter))+"";
            line[9]=Utils.stringFormater(statistics.getProcetOfStrategies(1).get("KD").get(iterationCounter))+"";
            line[10]=Utils.stringFormater(statistics.getProcetOfStrategies(1).get("AllD").get(iterationCounter))+"";
            line[11]=Utils.getDigitNumberOfSolution(iteration)+"";
            iterationCounter++;
        }


        GnuPlotExporter.createDataFile(lines,"./results/La-results"+(runsCounter+1)+".txt",true);
    }


    public static void produceResultAll(List<Statistics> statisticsList,Dane data) {

        ArrayList<String[]>lines=new ArrayList<>();
        Utils.addExperimentData(data,lines, true);

        //make header
        int sensorsNumber=statisticsList.get(0).getRunsStateList().get(0).get(0).size();
        String[]firstLine=new String[21];
        int clumnCounter=0;
        firstLine[clumnCounter]="#iter";
        clumnCounter++;
        firstLine[clumnCounter]="q_curr";
        clumnCounter++;

        firstLine[clumnCounter]="std_q_curr";
        clumnCounter++;
        firstLine[clumnCounter]="av_rev";
        clumnCounter++;
        firstLine[clumnCounter]="std_av_rev";
        clumnCounter++;
        firstLine[clumnCounter]="%m";
        clumnCounter++;
        firstLine[clumnCounter]="std_%m";
        clumnCounter++;


        firstLine[clumnCounter]="%strategy";
        clumnCounter++;

        firstLine[clumnCounter]="std_%strategy";
        clumnCounter++;
        firstLine[clumnCounter]="av_k";
        clumnCounter++;

        firstLine[clumnCounter]="std_av_k";
        clumnCounter++;
        firstLine[clumnCounter]="%ALLC";
        clumnCounter++;
        firstLine[clumnCounter]="std_%ALLC";
        clumnCounter++;

        firstLine[clumnCounter]="%KC";
        clumnCounter++;
        firstLine[clumnCounter]="std_%KC";
        clumnCounter++;
        firstLine[clumnCounter]="%KDC";
        clumnCounter++;
        firstLine[clumnCounter]="std_%KDC";
        clumnCounter++;
        firstLine[clumnCounter]="%KD";
        clumnCounter++;
        firstLine[clumnCounter]="std_%KD";
        clumnCounter++;
        firstLine[clumnCounter]="%ALLD";
        clumnCounter++;
        firstLine[clumnCounter]="std_%ALLD";
        clumnCounter++;


        lines.add(firstLine);

        int iterationCounter=0;
        for(int iteration=0;iteration<statisticsList.get(0).getRunsStateList().get(0).size();iteration++)
        {
            clumnCounter=0;
            String[] line=new String[21];

            line[clumnCounter]=iteration+"";
            clumnCounter++;
            int sensorCounter=0;

            List<Double>valuesForMeanAndStd=new ArrayList<>();
            for(var stac:statisticsList)
            {
                valuesForMeanAndStd.add(stac.getProcentOfCoveredPoi().get(0).get(iteration));
            }
            //coverage
            line[clumnCounter]=Utils.stringFormater(getMean(valuesForMeanAndStd))+"";
            clumnCounter++;
            line[clumnCounter]=Utils.stringFormater(getStd(valuesForMeanAndStd))+"";
            clumnCounter++;

            valuesForMeanAndStd=new ArrayList<>();
            //local revards
            for(var stat:statisticsList)
            {
                double rewardSum=0;
                for(var sensor:stat.getRunsStateList().get(0).get(iteration))
                {
                    rewardSum+=sensor.getLastReward()/statisticsList.get(0).getRunsStateList().get(0).get(iteration).size();

                }
                valuesForMeanAndStd.add(rewardSum);
            }

            line[clumnCounter]=Utils.stringFormater(getMean(valuesForMeanAndStd))+"";
            clumnCounter++;
            line[clumnCounter]=Utils.stringFormater(getStd(valuesForMeanAndStd))+"";
            clumnCounter++;
        //rts
            valuesForMeanAndStd=new ArrayList<>();
            for(var stac:statisticsList)
            {
                valuesForMeanAndStd.add(stac.getProcentOfRTSUsageInRun(1).get(iteration));
            }

            line[clumnCounter]=Utils.stringFormater(getMean(valuesForMeanAndStd))+"";
            clumnCounter++;
            line[clumnCounter]=Utils.stringFormater(getStd(valuesForMeanAndStd))+"";
            clumnCounter++;

            //%strategy
            valuesForMeanAndStd=new ArrayList<>();
            for(var stac:statisticsList)
            {
                valuesForMeanAndStd.add(stac.getStrategyChanged().get(0).get(iterationCounter));
            }
            line[clumnCounter]=Utils.stringFormater(getMean(valuesForMeanAndStd))+"";
            clumnCounter++;
            line[clumnCounter]=Utils.stringFormater(getStd(valuesForMeanAndStd))+"";
            clumnCounter++;
            //k
            valuesForMeanAndStd=new ArrayList<>();
            for(var stat:statisticsList) {

                double kSum = 0;
                for (var sensor : stat.getRunsStateList().get(0).get(iteration)) {
                    kSum += sensor.getK();
                }
                valuesForMeanAndStd.add(kSum/sensorsNumber);
            }
            line[clumnCounter]=Utils.stringFormater(getMean(valuesForMeanAndStd))+"";
            clumnCounter++;
            line[clumnCounter]=Utils.stringFormater(getStd(valuesForMeanAndStd))+"";
            clumnCounter++;


            clumnCounter=addResultAllFileStrategies(statisticsList, clumnCounter, iteration, line, "ALLC");
            clumnCounter=addResultAllFileStrategies(statisticsList, clumnCounter, iteration, line, "KC");
            clumnCounter=addResultAllFileStrategies(statisticsList, clumnCounter, iteration, line, "KDC");
            clumnCounter=addResultAllFileStrategies(statisticsList, clumnCounter, iteration, line, "KD");
            clumnCounter=addResultAllFileStrategies(statisticsList, clumnCounter, iteration, line, "AllD");


            lines.add(line);

        }


        GnuPlotExporter.createDataFile(lines,"./results/La-resultsALL"+".txt",true);


    }

    private static int addResultAllFileStrategies(List<Statistics> statisticsList, Integer clumnCounter, int iterationCounter, String[] line, String strategyName) {
        List<Double> valuesForMeanAndStd;
        valuesForMeanAndStd=new ArrayList<>();
        for(var stat:statisticsList)
        {
            valuesForMeanAndStd.add(stat.getProcetOfStrategies(1).get(strategyName).get(iterationCounter));

        }
        //strategies
        line[clumnCounter]= Utils.stringFormater(getMean(valuesForMeanAndStd))+"";
        clumnCounter++;
        line[clumnCounter]=Utils.stringFormater(getStd(valuesForMeanAndStd))+"";
        clumnCounter++;
        return clumnCounter;
    }

    private static double getStd(List<Double> valuesForMeanAndStd) {
        double mean=getMean(valuesForMeanAndStd);
        double acum=0;
        for(int i=0;i<valuesForMeanAndStd.size();i++)
        {
            double dev=Math.pow(mean-valuesForMeanAndStd.get(i),2);
            acum+=dev;

        }
        acum/=valuesForMeanAndStd.size();
        acum=Math.sqrt(acum);
        return acum;



    }

    private static double getMean( List<Double> valuesList) {

        double mean=0;
        for(var value:valuesList)
        {
            mean+=value;
        }
        return mean/valuesList.size();

    }
}
