package UI;

import lab4.Statistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.*;

import java.util.List;
import javax.swing.*;


public class ResultsPresentationView {
    public JPanel mainPanel;
    private JSpinner spinner1;
    public JButton btnMeanrewardChart;
    private JPanel dashbord;
    private JPanel chartPanel;
    private JButton symulacjaButton;
    private JButton debugButton;

    private void createUIComponents() {

        chartPanel = createChartPanel();


    }

    private JPanel createChartPanel() {
        String chartTitle = "Programming Languages Trends";
        String categoryAxisLabel = "Interest over time";
        String valueAxisLabel = "Popularity";
        CategoryDataset dataset = getDefaultChart();;
        JFreeChart chart = ChartFactory.createLineChart(chartTitle,
                categoryAxisLabel, valueAxisLabel, dataset);

        ;
        return new ChartPanel(chart);
    }

    public void showChart()
    {
        String chartTitle = "Noewy tytuł";
        String categoryAxisLabel = "nie obchodzi mniee";
        String valueAxisLabel = "lol";
        JFreeChart chart = ChartFactory.createLineChart(chartTitle,
                categoryAxisLabel, valueAxisLabel, getDataset());
        ChartPanel pan=(ChartPanel)chartPanel;
        pan.setChart(chart);


    }
    private CategoryDataset getDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String series1 = "Java";
        String series2 = "PHr";
        String series3 = "C++";
        String series4 = "C#";

        dataset.addValue(50.0, series1, "2003");
        dataset.addValue(4.8, series1, "2006");
        dataset.addValue(4.5, series1, "2007");
        dataset.addValue(4.3, series1, "2008");
        dataset.addValue(4.0, series1, "2009");
        dataset.addValue(4.1, series1, "2010");
        dataset.addValue(4.2, series1, "2011");
        dataset.addValue(4.2, series1, "2012");
        dataset.addValue(4.0, series1, "2013");

        dataset.addValue(4.0, series2, "2003");
        dataset.addValue(4.2, series2, "2006");
        dataset.addValue(3.8, series2, "2007");
        dataset.addValue(3.6, series2, "2008");
        dataset.addValue(3.4, series2, "2009");
        dataset.addValue(3.4, series2, "2010");
        dataset.addValue(3.3, series2, "2011");
        dataset.addValue(3.1, series2, "2012");
        dataset.addValue(3.2, series2, "2013");

        dataset.addValue(3.6, series3, "2005");
        dataset.addValue(3.4, series3, "2006");
        dataset.addValue(3.5, series3, "2007");
        dataset.addValue(3.2, series3, "2008");
        dataset.addValue(3.2, series3, "2009");
        dataset.addValue(3.0, series3, "2010");
        dataset.addValue(2.8, series3, "2011");
        dataset.addValue(2.8, series3, "2012");
        dataset.addValue(2.6, series3, "2013");

        dataset.addValue(3.2, series4, "2005");
        dataset.addValue(3.2, series4, "2006");
        dataset.addValue(3.0, series4, "2007");
        dataset.addValue(3.0, series4, "2008");
        dataset.addValue(2.8, series4, "2009");
        dataset.addValue(2.7, series4, "2010");
        dataset.addValue(2.6, series4, "2011");
        dataset.addValue(2.6, series4, "2012");
        dataset.addValue(2.4, series4, "2013");

        return dataset;


    }
    private CategoryDataset getDefaultChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String series1 = "Java";
        String series2 = "PHP";
        String series3 = "C++";
        String series4 = "C#";

        dataset.addValue(5.0, series1, "2005");
        dataset.addValue(4.8, series1, "2006");
        dataset.addValue(4.5, series1, "2007");
        dataset.addValue(4.3, series1, "2008");
        dataset.addValue(4.0, series1, "2009");
        dataset.addValue(4.1, series1, "2010");
        dataset.addValue(4.2, series1, "2011");
        dataset.addValue(4.2, series1, "2012");
        dataset.addValue(4.0, series1, "2013");

        dataset.addValue(4.0, series2, "2005");
        dataset.addValue(4.2, series2, "2006");
        dataset.addValue(3.8, series2, "2007");
        dataset.addValue(3.6, series2, "2008");
        dataset.addValue(3.4, series2, "2009");
        dataset.addValue(3.4, series2, "2010");
        dataset.addValue(3.3, series2, "2011");
        dataset.addValue(3.1, series2, "2012");
        dataset.addValue(3.2, series2, "2013");

        dataset.addValue(3.6, series3, "2005");
        dataset.addValue(3.4, series3, "2006");
        dataset.addValue(3.5, series3, "2007");
        dataset.addValue(3.2, series3, "2008");
        dataset.addValue(3.2, series3, "2009");
        dataset.addValue(3.0, series3, "2010");
        dataset.addValue(2.8, series3, "2011");
        dataset.addValue(2.8, series3, "2012");
        dataset.addValue(2.6, series3, "2013");

        dataset.addValue(3.2, series4, "2005");
        dataset.addValue(3.2, series4, "2006");
        dataset.addValue(3.0, series4, "2007");
        dataset.addValue(3.0, series4, "2008");
        dataset.addValue(2.8, series4, "2009");
        dataset.addValue(2.7, series4, "2010");
        dataset.addValue(2.6, series4, "2011");
        dataset.addValue(2.6, series4, "2012");
        dataset.addValue(2.4, series4, "2013");

        return dataset;
    }

    public void setMeanRewardChart(Statistics statistics) {
        String series1 = "Średnia nagroda";
        List<Double>bestRewardsForEachItereationOfRun=statistics.getBestRewardsForEachItereationOfRun(1);


        DefaultXYDataset dataset = new DefaultXYDataset();

        createSeries(series1, bestRewardsForEachItereationOfRun, dataset);

        String chartTitle = "Średnia nagroda dla każdej iteracji";
        String xLabel = "Numer iteracji";
        String yLabel = "nagroda";
        int tick= getTick(bestRewardsForEachItereationOfRun);

        JFreeChart chart = createChart(dataset, chartTitle, xLabel, yLabel, tick);
        ChartPanel pan=(ChartPanel)chartPanel;
        pan.setChart(chart);
    }

    private int getTick(List<Double> bestRewardsForEachItereationOfRun) {
        return 2*(1+bestRewardsForEachItereationOfRun.size()/100);
    }

    private JFreeChart createChart(DefaultXYDataset dataset, String chartTitle, String xLabel, String yLabel, int tick) {
        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,xLabel,yLabel,dataset);
        // Create an NumberAxis
        NumberAxis xAxis = new NumberAxis();
        xAxis.setTickUnit(new NumberTickUnit(tick));

// Assign it to the chart
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainAxis(xAxis);
        return chart;
    }

    private void createSeries(String series1, List<Double> bestRewardsForEachItereationOfRun, DefaultXYDataset dataset) {
        double[]y=new double[bestRewardsForEachItereationOfRun.size()];
        double[]x=new double[bestRewardsForEachItereationOfRun.size()];
        for(int i=0;i<bestRewardsForEachItereationOfRun.size();i++)
        {
           x[i]=i+1;
           y[i]=bestRewardsForEachItereationOfRun.get(i);
        }
        double[][] dataForChart = new double[][] {x, y};
        dataset.addSeries(series1, dataForChart);
    }
}