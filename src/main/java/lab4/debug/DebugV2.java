package lab4.debug;

import com.opencsv.CSVWriter;
import lab4.Dane;
import lab4.Node.Sensor;
import lab4.Utils.Utils;

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
        addSensorsStatesToLines(sensorList);


        for(int sensorCounter=0;sensorCounter<sensorList.size();sensorCounter++)
        {
            String[]firstLine=new String[1];
            String[]secoundLine=new String[2];
            String[]header=new String[5];
            header[0]="curr_strat";
            header[1]="is strategy selected by eps";
            header[2]="RTS";
            header[3]="m_i^RTS";
            header[4]="k";
            String[]commonInfo=new String[5];
            commonInfo[0]=sensorList.get(sensorCounter).getLastStrategy().getName();
            commonInfo[1]=sensorList.get(sensorCounter).isLastStrategySelectedByEps()?"True":"False";
            commonInfo[2]=sensorList.get(sensorCounter).isReadyToShare()?"1":"0";
            commonInfo[3]=sensorList.get(sensorCounter).getNumberOfRTSNeighbours()+"";
            commonInfo[4]=sensorList.get(sensorCounter).getK()+"";

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
                itemLine[1]= Utils.stringFormater(memoryItem.getReward())+"";
                lines.add(itemLine);
            }
            //id of neighbours
            String[]thirdLine=new String[1];

            thirdLine[0]="Neighbours list";
            lines.add(thirdLine);
            String[]fourthLine=new String[1];
            fourthLine[0]="id";

            lines.add(fourthLine);


            for(var neighbour:sensorList.get(sensorCounter).neighborSensors)
            {
                String[]neighbourInfo=new String[1];
                neighbourInfo[0]=neighbour.getIdentyfikator()+"";
                lines.add(neighbourInfo);
            }



        }


    }

    private void addSensorsStatesToLines(List<Sensor> sensorList) {
        lines.add(Utils.convertForArray("##Sensors states"));
        lines.add(new String[]{"id","state"});
        for(var sensor:sensorList)
        {
            lines.add(new String[]{sensor.getIdentyfikator()+"",sensor.getStan()+""});
        }
    }


    public void addSecound(List<Sensor>sensorList,int iterNumber)
    {

        String[]iterationLine=new String[2];
        iterationLine[0]="###iteration";
        iterationLine[1]=(iterNumber+1)+"";
        String[]headerLine=new String[1];
        headerLine[0]="#Debug part 2";
        lines.add(iterationLine);
        lines.add(headerLine);
        addSensorsStatesToLines(sensorList);
        for(int sensorCounter=0;sensorCounter<sensorList.size();sensorCounter++)
        {
            String[]firstLine=new String[1];
            String[]secoundLine=new String[2];

            String[]header=new String[5];
            header[0]="curr_strat";
            header[1]="is strategy selected by eps";
            header[2]="RTS";
            header[3]="m_i^RTS";
            header[4]="k";
            String[]commonInfo=new String[5];
            commonInfo[0]=sensorList.get(sensorCounter).getLastStrategy().getName();
            commonInfo[1]=sensorList.get(sensorCounter).isLastStrategySelectedByEps()?"True":"False";
            commonInfo[2]=sensorList.get(sensorCounter).isReadyToShare()?"1":"0";
            commonInfo[3]=sensorList.get(sensorCounter).getNumberOfRTSNeighbours()+"";
            commonInfo[4]=sensorList.get(sensorCounter).getK()+"";
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
                itemLine[1]=Utils.stringFormater(memoryItem.getReward())+"";
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
            dataLine[0]=Utils.stringFormater(sensorList.get(sensorCounter).getLastReward())+"";
            dataLine[1]=Utils.stringFormater(sensorList.get(sensorCounter).getRevToSend())+"";
            dataLine[2]=Utils.stringFormater(sensorList.get(sensorCounter).getRevToSend())+"";
            dataLine[3]=Utils.stringFormater(sensorList.get(sensorCounter).sum_u)+"";

            lines.add(dataLine);

        }


    }


    public void addFourth(List<Sensor> sensorList, int c_a, int cu)
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
        SUM/=sensorList.size()*cu;
        firstLine[1]=Utils.stringFormater(SUM)+"";


    }


    public void addFifth(List<Sensor>sensorList)
    {



        String[]headerLine=new String[1];
        headerLine[0]="#Debug part 5";
        lines.add(headerLine);
        String[]firstLine=new String[5];
        firstLine[0]="Sensor Number";
        firstLine[1]="State";
        firstLine[2]="Strategy Name";
        firstLine[3]="is strategy selected by eps";
        firstLine[4]="k";
        lines.add(firstLine);
        for (var sensor:sensorList)
        {
            String[]dataLine=new String[5];
            dataLine[0]="Sensor"+sensor.getIdentyfikator();
            dataLine[1]=sensor.getStan()+"";
            dataLine[2]=sensor.getLastStrategy().getName();
            dataLine[3]=sensor.isLastStrategySelectedByEps()?"True":"False";
            dataLine[4]=sensor.getK()+"";
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
            if(bestNeighbour==sensor || bestNeighbour==null)
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
    public void addSeven(List<Sensor> sensorList, Double strategyChanged)
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
            String[]secoundLine=new String[6];
            secoundLine[0]="State";
            secoundLine[1]="current_strategy";
            secoundLine[2]="is strategy selected by eps";
            secoundLine[3]="RTS tag";
            secoundLine[4]="mi^RTS";
            secoundLine[5]="K";
            lines.add(firstLine);
            lines.add(secoundLine);

            String[]dataLine=new String[6];

            dataLine[0]=sensor.getStan()+"";
            dataLine[1]=sensor.getLastStrategy().getName()+"";
            dataLine[2]=sensor.isLastStrategySelectedByEps()?"True":"False";
            dataLine[3]=sensor.isReadyToShare()?"1":"0";
            dataLine[4]=""+sensor.getNumberOfRTSNeighbours();
            dataLine[5]=""+sensor.getK();



            lines.add(dataLine);
        }
        String[]strategyChangedLine=new String[2];
        strategyChangedLine[0]="Procent of strategy changed";
        strategyChangedLine[1]=Utils.stringFormater(strategyChanged)+"";
        lines.add(strategyChangedLine);




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

    public List<String[]> getLines() {
        return lines;
    }
}
