package lab4.debug;

import com.opencsv.CSVWriter;
import lab4.La.strategies.KCStrategy;
import lab4.La.strategies.KDCStrategy;
import lab4.La.strategies.KDStrategy;
import lab4.La.strategies.Strategy;
import lab4.Node.Sensor;
import lab4.Utils.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DebugV3 {



    public DebugV3() {

    }
   public void makeDebuV3(int maxNeighboursNumber, int maxk) {

       List<String[]> csvFileContent = new ArrayList<>();
      csvFileContent.addAll(makeHeader(maxNeighboursNumber));
      Sensor sensor=new Sensor(1,1,1,1);
      sensor.neighborSensors=getNeighboursList(maxNeighboursNumber);

      for(int ik=0;ik<maxk;ik++)
      {
          sensor.onAllNeighbours();
          Strategy currentStrategy=new KCStrategy();
          String[] lineKC = getStrategyLine(maxNeighboursNumber, sensor, ik, currentStrategy);
          csvFileContent.add(lineKC);

          sensor.onAllNeighbours();
          currentStrategy=new KDStrategy();
          String[] lineKD = getStrategyLine(maxNeighboursNumber, sensor, ik, currentStrategy);
          csvFileContent.add(lineKD);

          sensor.onAllNeighbours();
          currentStrategy=new KDCStrategy();
          String[] lineKDC = getStrategyLine(maxNeighboursNumber, sensor, ik, currentStrategy);
          csvFileContent.add(lineKDC);

      }


       saveLinesToFile(csvFileContent, "debug/debugv3.csv");
   }

    private String[] getStrategyLine(int maxNeighboursNumber, Sensor sensor, int ik, Strategy currentStrategy) {
        String[] line=new String[maxNeighboursNumber+2];
        int columnConter=0;
        line[columnConter]=currentStrategy.getName()+" "+ik;
        columnConter++;
        for(int i=0;i<maxNeighboursNumber+1;i++)
        {
            line[columnConter]= currentStrategy.decideAboutSensorState(sensor.neighborSensors,ik)+"";
            columnConter++;
            if(i==maxNeighboursNumber)
            {
                break;
            }
            sensor.neighborSensors.get(i).setStan(0);


        }
        return line;
    }

    private List<Sensor> getNeighboursList(int maxNeighboursNumber) {

        List<Sensor>neighbours=new ArrayList<>();
        for(int i=0;i<maxNeighboursNumber;i++)
        {
            neighbours.add(new Sensor(1,1,1,1));
        }
        return neighbours;
    }

    private List<String[]> makeHeader(int maxNeighboursNumber) {
        String[] line=new String[2+maxNeighboursNumber];
        line[0]="num of C (ON)";
        int columnCounter=1;
        for(int i=0;i<maxNeighboursNumber+1;i++){
            line[columnCounter]=(maxNeighboursNumber-i)+"";
            columnCounter++;
        }

        String[] line2=new String[2+maxNeighboursNumber];
        line2[0]="num_of_D(OFF)";
        columnCounter=1;
        for(int i=0;i<maxNeighboursNumber+1;i++){
            line2[columnCounter]=(i)+"";
            columnCounter++;
        }
        List<String[]> result= new ArrayList<>();
        result.add(line);
        result.add(line2);
        return result;

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
}
