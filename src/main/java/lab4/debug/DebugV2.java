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


    public void addThird(List<Sensor>sensorList,int iterNumber)
    {

        String[]iterationLine=new String[2];

        String[]headerLine=new String[1];
        headerLine[0]="#Debug part 3";
        lines.add(headerLine);

        for(int sensorCounter=0;sensorCounter<sensorList.size();sensorCounter++)
        {
            String[]firstLine=new String[1];

            String[]header=new String[4];
            header[0]="rev_i";
            header[1]="rev_to_Send";
            header[2]="rev_sh";
            header[3]="sum";
            String[]dataLine=new String[4];

            firstLine[0]="Sensor "+(sensorCounter+1);

            lines.add(firstLine);
            lines.add(header);
            dataLine[0]=sensorList.get(sensorCounter).getLastReward()+"";
            dataLine[1]=sensorList.get(sensorCounter).getRevToSend()+"";
            dataLine[2]=sensorList.get(sensorCounter).getRevToSend()+"";
            dataLine[3]=sensorList.get(sensorCounter).sum_u+"";

            lines.add(dataLine);

        }


    }


    public void addFourth(List<Sensor>sensorList,int c_a)
    {

        String[]iterationLine=new String[2];

        String[]headerLine=new String[1];
        headerLine[0]="#Debug part 4";
        lines.add(headerLine);
        String[]firstLine=new String[2];

        firstLine[0]="Sum";
        lines.add(firstLine);
        double SUM=0.0;
        for (var sensor:sensorList)
        {
            SUM+=sensor.sum_u;
        }
        SUM/=sensorList.size()*c_a;
        firstLine[1]=SUM+"";


    }


    public void addFifth(List<Sensor>sensorList)
    {



        String[]headerLine=new String[1];
        headerLine[0]="#Debug part 5";
        lines.add(headerLine);
        String[]firstLine=new String[3];
        firstLine[0]="Sensor state";
        firstLine[1]="State";
        firstLine[2]="Strategy Name";
        lines.add(firstLine);
        for (var sensor:sensorList)
        {
            String[]dataLine=new String[2];
            dataLine[0]=sensor.getStan()+"";
            dataLine[1]=sensor.getLastStrategy().getName();
            lines.add(dataLine);
        }




    }


    public void addSix(List<Sensor>sensorList)
    {



        String[]headerLine=new String[1];
        headerLine[0]="#Debug part 6";
        lines.add(headerLine);


        int sensorCounter=0;
        for (var sensor:sensorList)
        {
            sensorCounter++;
            String[]firstLine=new String[1];
            firstLine[0]="Sensor "+sensorCounter;
            String[]secoundLine=new String[5];
            secoundLine[0]="id";
            secoundLine[1]="current_strategy";
            secoundLine[2]="RTS tag";
            secoundLine[3]="mi^RTS";
            secoundLine[4]="K";
            lines.add(firstLine);
            lines.add(secoundLine);
            Sensor bestNeighbour=sensor.getNeighborWithBestSumU();
            String[]dataLine=new String[5];
            if(bestNeighbour==sensor)
            {
                dataLine[0]="-";
                dataLine[1]="-";
                dataLine[2]="-";
                dataLine[3]="-";
                dataLine[4]="-";
            }
           else {
                dataLine[0]=bestNeighbour.getIdentyfikator()+"";
                dataLine[1]=bestNeighbour.getLastStrategy().getName()+"";
                dataLine[2]=bestNeighbour.isReadyToShare()?"1":"0";
                dataLine[3]=""+bestNeighbour.getNumberOfRTSNeighbours();
                dataLine[4]=""+bestNeighbour.getK();

            }

            lines.add(dataLine);
        }




    }
    public void addSeven(List<Sensor>sensorList)
    {



        String[]headerLine=new String[1];
        headerLine[0]="#Debug part 7";
        lines.add(headerLine);


        int sensorCounter=0;
        for (var sensor:sensorList)
        {
            sensorCounter++;
            String[]firstLine=new String[1];
            firstLine[0]="Sensor "+sensorCounter;
            String[]secoundLine=new String[5];

            secoundLine[1]="current_strategy";
            secoundLine[2]="RTS tag";
            secoundLine[3]="mi^RTS";
            secoundLine[4]="K";
            lines.add(firstLine);
            lines.add(secoundLine);

            String[]dataLine=new String[5];

            dataLine[0]=sensor.getIdentyfikator()+"";
            dataLine[1]=sensor.getLastStrategy().getName()+"";
            dataLine[2]=sensor.isReadyToShare()?"1":"0";
            dataLine[3]=""+sensor.getNumberOfRTSNeighbours();
            dataLine[4]=""+sensor.getK();



            lines.add(dataLine);
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
