package lab4.Utils;

import lab4.Node.Node;
import lab4.Node.Poi;
import lab4.Node.Sensor;


import java.util.ArrayList;
import java.util.List;

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

    public static String[] convertForArray(String s) {
        String[]array=new String[1];
        array[0]=s;
        return array;
    }
}
