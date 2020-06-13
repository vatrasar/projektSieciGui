package lab4.debug;

import com.opencsv.CSVWriter;
import lab4.Node.Sensor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DebugV2 {
    List<String[]>lines;

    public DebugV2() {
        this.lines = new ArrayList<>();
    }
    public void addFirst(List<Sensor>sensorList)
    {

        String[]headerLine=new String[1];
        headerLine[0]="#Debug part1";
        lines.add(headerLine);
        for(int sensorCounter=0;sensorCounter<sensorList.size();sensorCounter++)
        {
            String[]firstLine=new String[1];
            String[]secoundLine=new String[2];
            String[]header=new String[4];
            header[0]="curr_strat";
            header[1]="RTS";
            header[2]="m_i^RTS";
            header[3]="k";
            String[]commonInfo=new String[4];
            commonInfo[0]=sensorList.get(sensorCounter).getLastStrategy().getName();
            commonInfo[1]=sensorList.get(sensorCounter).isReadyToShare()?"1":"0";
            commonInfo[2]=sensorList.get(sensorCounter).getNumberOfRTSNeighbours()+"";
            commonInfo[3]=sensorList.get(sensorCounter).getK()+"";
            firstLine[0]="Sensor "+(sensorCounter+1);
            secoundLine[0]="Strategia";
            secoundLine[1]="Nagroda";
            lines.add(firstLine);
            lines.add(header);
            lines.add(commonInfo);
            lines.add(secoundLine);

            for(var memoryItem:sensorList.get(sensorCounter).memory)
            {
                String[]itemLine=new String[2];
                itemLine[0]=memoryItem.getStrategy().getName();
                itemLine[1]=memoryItem.getReward()+"";
                lines.add(itemLine);
            }


        }


    }


    public void addSecound(List<Sensor>sensorList,int iterNumber)
    {

        String[]iterationLine=new String[2];
        iterationLine[0]="###iteration";
        iterationLine[1]=iterNumber+"";
        String[]headerLine=new String[1];
        headerLine[0]="#Debug part 2";
        lines.add(iterationLine);
        lines.add(headerLine);

        for(int sensorCounter=0;sensorCounter<sensorList.size();sensorCounter++)
        {
            String[]firstLine=new String[1];
            String[]secoundLine=new String[2];
            String[]header=new String[4];
            header[0]="curr_strat";
            header[1]="RTS";
            header[2]="m_i^RTS";
            header[3]="k";
            String[]commonInfo=new String[4];
            commonInfo[0]=sensorList.get(sensorCounter).getLastStrategy().getName();
            commonInfo[1]=sensorList.get(sensorCounter).isReadyToShare()?"1":"0";
            commonInfo[2]=sensorList.get(sensorCounter).getNumberOfRTSNeighbours()+"";
            commonInfo[3]=sensorList.get(sensorCounter).getK()+"";
            firstLine[0]="Sensor "+(sensorCounter+1);
            secoundLine[0]="Strategia";
            secoundLine[1]="Nagroda";
            lines.add(firstLine);
            lines.add(header);
            lines.add(commonInfo);
            lines.add(secoundLine);

            for(var memoryItem:sensorList.get(sensorCounter).memory)
            {
                String[]itemLine=new String[2];
                itemLine[0]=memoryItem.getStrategy().getName();
                itemLine[1]=memoryItem.getReward()+"";
                lines.add(itemLine);
            }


        }


    }
    public void saveLinesToFile(String fileName) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(fileName));
            writer.writeAll(this.lines);
            writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
