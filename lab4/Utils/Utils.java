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

    }

    public static <T extends ToClone> List<T> cloneList(List<T> source)
    {

        List<T>resultList=new ArrayList<T>();
        for (T item:source){

            resultList.add((T)item.clone());
        }
        return resultList;
    }
}
