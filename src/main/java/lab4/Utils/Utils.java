package lab4.Utils;

import lab4.Dane;
import lab4.Node.Node;
import lab4.Node.Poi;
import lab4.Node.Sensor;


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


    public static void connectSensorsWithPoi(List<Poi> poiList, List<Sensor>sensorList, int sensingRange)
    {
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
        return String.format("%.2f",number);

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
    public static void addExperimentData(Dane dane, List<String[]> linesList)
    {
        linesList.add(new String[]{"#Experiment settings"});
        linesList.add(new String[]{"#Number of sensors",dane.listOfSensors.size()+""});
        linesList.add(new String[]{"#Battery usage in 1 moment of time",dane.getPojemnoscBaterii()+""});
        linesList.add(new String[]{"#Sensor range",dane.getPromien()+""});
        linesList.add(new String[]{"#Q",dane.getQ()+""});
        linesList.add(new String[]{"#C",dane.getC()+""});
        linesList.add(new String[]{"#Con",dane.getC_on()+""});
        linesList.add(new String[]{"#Coff+",dane.getC_offPlus()+""});
        linesList.add(new String[]{"#C1",dane.getC1()+""});
        linesList.add(new String[]{"#C2",dane.getC2()+""});
        linesList.add(new String[]{"#C3",dane.getC3()+""});
        linesList.add(new String[]{"#C4",dane.getC4()+""});
        linesList.add(new String[]{"#delta",dane.getDelta2()+""});
        linesList.add(new String[]{"#Sensor placement",dane.getlocationCreationTypeName()+""});
        linesList.add(new String[]{"#Poi placement",dane.getListOfPoi().size()+""});
        linesList.add(new String[]{"#Max_num_of_iter",dane.laData.maxIterationsNumber+""});
        linesList.add(new String[]{"#Max_num_of_runs",dane.laData.maxRunsNumber+""});
        linesList.add(new String[]{"#U",dane.laData.u+""});
        linesList.add(new String[]{"#all-c",dane.laData.allCProb+""});
        linesList.add(new String[]{"#all-d",dane.laData.allDProb+""});
        linesList.add(new String[]{"#K-c",dane.laData.KCProb+""});
        linesList.add(new String[]{"#K-d",dane.laData.KDProb+""});
        linesList.add(new String[]{"#K-dc",dane.laData.KDCProb+""});
        linesList.add(new String[]{"#p_init_ON",dane.laData.probSensorOn+""});
        linesList.add(new String[]{"#H",dane.laData.h+""});
        linesList.add(new String[]{"#epsylon",dane.laData.epslion+""});
        linesList.add(new String[]{"#strategy change type",dane.laData.isEvolutionaryStrategyChange?"Evolutionary":"adopt to the Best"});
        linesList.add(new String[]{"#strategy change range",dane.laData.getStrategyChangeRangeRangeName()});
        linesList.add(new String[]{"#Ready to share income",dane.laData.isRTS?"True":"False"});
        linesList.add(new String[]{"#Ready to share init",dane.laData.probReadyToShare+""});


    }
    public static String[] convertForArray(String s) {
        String[]array=new String[1];
        array[0]=s;
        return array;
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
