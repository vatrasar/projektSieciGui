package lab4.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GnuPlotExporter {

    /**
     * @param listOfSeriesAndTheirNames key is series name, value is is lus
     * @param path
     */
    public static void exportToGnuplot(Map<String, List<Double>> listOfSeriesAndTheirNames, String path)
    {
        try {
            int ymax=0;
            int ymin=0;
            int iternum=0;
            for(var serie:listOfSeriesAndTheirNames.values())
            {
                iternum=serie.size();
                for(var value:serie)
                {

                    if(value>ymax)
                    {
                        ymax=value.intValue()+1;
                    }
                    if(value<ymin)
                    {
                        ymin=value.intValue()-1;
                    }
                }
            }


            createHeaderFile(listOfSeriesAndTheirNames.keySet(),path,iternum,ymin,ymax,"iterations");


            PrintWriter printWriter=new PrintWriter(path+".txt");
            List<List<Double>>listOfSeries=new ArrayList<>(listOfSeriesAndTheirNames.values());
            if(listOfSeries.size()==0)
                return;



            for(int i=0;i< listOfSeries.get(0).size();i++)
            {
                printWriter.print(i+" ");
                for(var series:listOfSeries)
                {
                    printWriter.print(series.get(i)+" ");
                }
                printWriter.print("\n");
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public static void createDataFile(List<String[]> listOfLines,String filename,boolean doots){
        try {
            PrintWriter printWriter=new PrintWriter(filename);
            printWriter.write("#");
            for(var line:listOfLines)
            {
                if(doots)
                {
                    for(int i=0;i<line.length;i++)
                    {
                       line[i]=line[i].replaceAll(",",".");
                    }
                }
                for(var token:line)
                {
                    printWriter.write(token+" ");
                }
                printWriter.write("\n");
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    private static void createHeaderFile(Set<String> seriesNamesSet, String path,int iterations,int minY,int maxY,String ylabel) {
        try {
            if(seriesNamesSet.size()==0)
                return;
            String firstLineShape="set style data lines\n" +
                    "#set xrange [0:%d]\n" +
                    "#set yrange [%d:%d]\n" +
                    "set xlabel \"iterations\"\n" +
                    "set ylabel \"%s\"\n" +
                    "#set key at 95,80\n" +
                    "#set label \"b=1.4, k=6\" at 5,110\n" +
                    "plot '%s' using 1:%s with lines lc %d lw 3 title \"%s\",\\\n";
            String otherLinesShape="'%s' using 1:%s with lines lc %d lw 3 title \"%s\",\\\n";

            new File(path).getName();
            ArrayList<String>serieNameList=new ArrayList<>(seriesNamesSet);
            String outString=String.format(firstLineShape,iterations,minY,maxY,ylabel,new File(path).getName()+".txt",2,1,serieNameList.get(0));
            PrintWriter printWriter=new PrintWriter(path+".plt");
            printWriter.print(outString);
            for(int i=1;i<serieNameList.size();i++)
            {

                outString=String.format(otherLinesShape,new File(path).getName()+".txt",i+2,i+1,serieNameList.get(i));
                printWriter.print(outString);
            }
            printWriter.close();
//            for(var serie:seriesNamesSet)
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
