package lab4.Utils;

import lab4.Dane;
import lab4.Node.Node;
import lab4.Node.Poi;
import lab4.Node.Sensor;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {


    public static double computeDistance(Node node1, Node node2)
    {
        double distance=Math.sqrt(Math.pow(node1.getX()-node2.getX(),2)+Math.pow(node1.getY()-node2.getY(),2));
        return distance;
    }

    public static long convertBinaryForLong(String binary)
    {
        double sum=0;
        int power=0;
        for(int i=binary.length()-1;i>=0;i--)
        {
            sum+=Integer.parseInt(binary.charAt(i)+"")*Math.pow(2,power);
            power++;
        }
        return (long)sum;
    }
    public static void connectSensorsWithPoi(List<Poi> poiList, List<Sensor>sensorList, int sensingRange)
    {
        for(var poi:poiList)
        {
            poi.clearCoveringSensorsList();
        }
        for(Poi poi:poiList)
        {
            for(Sensor sensor:sensorList)
            {
                if(Utils.computeDistance(sensor,poi)<=sensingRange)
                {
                    poi.coveringSensorsList.add(sensor);
                    sensor.poisInRange.add(poi);
                }
            }
        }
//        System.out.println("pierwsze");
//        for(var poi:poiList)
//        {
//            System.out.println(poi.coveringSensorsList.size());
//        }

    }
    public static String stringFormater(double number)
    {

        return String.format("%.2f",roundTwoPlaces(number));

    }

    public static double roundTwoPlaces(double source)
    {
        return Math.round(source*100d)/100d;
    }

    public static <T extends ToClone> List<T> cloneList(List<T> source)
    {

        List<T>resultList=new ArrayList<T>();
        for (T item:source){

            resultList.add((T)item.clone());
        }
        return resultList;
    }

    public static List<Poi> getCommonPois(Sensor sensor1,Sensor sensor2)
    {
        ArrayList<Poi> commonPois=new ArrayList<Poi>(sensor1.poisInRange);
        commonPois.retainAll(sensor2.poisInRange);
        return commonPois;
    }

    public static int getDigitNumberOfSolution(List<Sensor>sensorsList) {
        double power=0;
        double sum=0;
        for(var sensor:sensorsList)
        {
            sum+=sensor.getStan()* Math.pow(2,power);
            power++;
        }
        return (int)sum;
    }
    public static void addExperimentData(Dane dane, List<String[]> linesList, boolean isLaData)
    {
        linesList.add(new String[]{"#Experiment settings"});
        linesList.add(new String[]{"#Number of sensors",dane.listOfSensors.size()+""});
        linesList.add(new String[]{"#Battery usage in 1 moment of time",dane.getBateria()+""});
        linesList.add(new String[]{"#Battery cappacity",dane.getPojemnoscBaterii()+""});
        linesList.add(new String[]{"#Sensor range",dane.getPromien()+""});
        linesList.add(new String[]{"#Q",Utils.stringFormater(dane.getQ())+""});
        linesList.add(new String[]{"#C",dane.getC()+""});
        linesList.add(new String[]{"#Con",dane.getC_on()+""});
        linesList.add(new String[]{"#Coff+",dane.getC_offPlus()+""});
        linesList.add(new String[]{"#C1",dane.getC1()+""});
        linesList.add(new String[]{"#C2",dane.getC2()+""});
        linesList.add(new String[]{"#C3",dane.getC3()+""});
        linesList.add(new String[]{"#C4",dane.getC4()+""});
        linesList.add(new String[]{"#delta2",dane.getDelta2()+""});
        if(isLaData)
        {
            linesList.add(new String[]{"#seed",dane.getRandomSeed()+""});
        }
        linesList.add(new String[]{"#Multirun",dane.laData.runNumber+""});
        if(dane.areSensorsFromFile())
            linesList.add(Utils.convertForArray("#form file",dane.getFileWithSensors().getName()));
        else
            linesList.add(new String[]{"#Sensor placement",dane.getlocationCreationTypeName()+""});
        linesList.add(new String[]{"#Poi placement",dane.getListOfPoi().size()+""});


        if(isLaData) {
            linesList.add(new String[]{"#Max_num_of_iter", dane.laData.maxIterationsNumber + ""});
            linesList.add(new String[]{"#Max_num_of_runs", dane.laData.maxRunsNumber + ""});
            linesList.add(new String[]{"#U", dane.laData.u + ""});
            linesList.add(new String[]{"#all-c", Utils.stringFormater(dane.laData.allCProb) + ""});
            linesList.add(new String[]{"#all-d", Utils.stringFormater(dane.laData.allDProb) + ""});
            linesList.add(new String[]{"#K-c", Utils.stringFormater(dane.laData.KCProb) + ""});
            linesList.add(new String[]{"#K-d", Utils.stringFormater(dane.laData.KDProb) + ""});
            linesList.add(new String[]{"#K-dc", Utils.stringFormater(dane.laData.KDCProb) + ""});
            linesList.add(new String[]{"#max k", dane.laData.maxK + ""});
            linesList.add(new String[]{"#p_init_ON", dane.laData.probSensorOn + ""});
            linesList.add(new String[]{"#H", dane.laData.h + ""});
            linesList.add(new String[]{"#epsylon", dane.laData.epslion + ""});
            linesList.add(new String[]{"#strategy change type", dane.laData.isEvolutionaryStrategyChange ? "Evolutionary" : "adopt to the Best"});
            linesList.add(new String[]{"#strategy change range", dane.laData.getStrategyChangeRangeRangeName()});
            linesList.add(new String[]{"#Ready to share income", dane.laData.isRTS ? "True" : "False"});
            linesList.add(new String[]{"#Ready to share init", dane.laData.probReadyToShare + ""});
        }


    }





    public static String[] convertForArray(String s) {
        String[]array=new String[1];
        array[0]=s;
        return array;
    }

    public static String[] convertForArray(String s,String value) {
        String[]array=new String[2];
        array[0]=s;
        array[1]=value;
        return array;
    }

    public static List<Sensor> loadSensors(Dane data, File source) {
        try {
            Scanner scanner=new Scanner(source);
            List<Sensor>sensorList=new ArrayList<>();

            //header
            if(scanner.hasNextLine())
            {
                scanner.nextLine();
            }

            //data
            while (scanner.hasNextLine())
            {
                String line=scanner.nextLine();
                String[]lineParts=line.split(" ");
                Sensor newSensor=new Sensor(Double.parseDouble(lineParts[1]),Double.parseDouble(lineParts[2]),data.getPromien(),1);
                newSensor.setStan(Integer.parseInt(lineParts[3]));
                sensorList.add(newSensor);
            }
            return sensorList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void saveFileWithSensorNumber(List<Sensor> listOfSensors) {
        try {

            PrintWriter out=new PrintWriter("SensorsNumber.txt");
            out.println("#id x y state");
            for(Sensor sensor:listOfSensors)
                out.println(sensor.getIdentyfikator()+" "+sensor.getX()+" "+sensor.getY()+" "+0);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


//    public static void convertCommaForDot(String fileName)
//    {
//        Scanner scanner = new Scanner(fileName);
//        List<String>lines=new ArrayList<>();
//        while(scanner.hasNextLine())
//        {
//            String line= scanner.nextLine();
//            line=line.replaceAll(",",".");
//
//            lines.add(line);
//        }
//        scanner.close();
//        try {
//            PrintWriter printWriter=new PrintWriter(fileName);
//            for(var line:lines)
//            {
//                printWriter.write(line+"\n");
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
}
