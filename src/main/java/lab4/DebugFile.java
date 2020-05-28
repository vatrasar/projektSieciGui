package lab4;

import lab4.Node.Sensor;

import java.util.List;

public class DebugFile {
    public static String generate(Dane data) {
        StringBuilder document=new StringBuilder();
        document.append(generateHeader(data.getListOfSensors())).append("\n");

        for(int i=0;i<data.getListOfSensors().size();i++)
        {
            document.append(generateRow(i,data)).append("\n");
        }
        return document.toString();

    }


    private static String generateRow(int index,Dane data)
    {
        StringBuilder row=new StringBuilder();
        Sensor sensor=data.getListOfSensors().get(index);
        row.append(index+1).append(" ").append(sensor.getStan()).append(" ");
        //states
        for(int i=0;i<data.getListOfSensors().size();i++)
        {
            if(i!= index)
            {
                row.append(data.listOfSensors.get(i).getStan()).append(" ");
            }
        }

        row.append(data.getQ()).append(" ");
        row.append(String.format("%.2f",sensor.getCurrentLocalCoverageRate())).append(" ").append(String.format("%.2f",sensor.computeReword(data,data.listOfSensors))).append(" ");
        return row.toString();
    }

    private static String generateHeader(List<Sensor> listOfSensors) {

        StringBuilder header=new StringBuilder();
        header.append("#s_num ");
        header.append("s_i ");
        for(int i=1;i<listOfSensors.size();i++)
        {
            header.append("s_i-").append(i).append(" ");
        }

        header.append("q q_curr rev");



        return header.toString();
    }
}
