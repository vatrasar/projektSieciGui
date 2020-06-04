package UI;

import lab4.La.strategies.Strategy;
import lab4.Statistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.*;
import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.*;


public class ResultsPresentationView {
    public JPanel mainPanel;
    public JSpinner spinRunNumber;
    public JButton btnMeanrewardChart;
    private JPanel dashbord;
    private JPanel chartPanel;
    private JButton symulacjaButton;

    public JButton btnActiveSensorsCharts;
    public JButton btnStrategiesCharts;
    public JButton btnDebug;
    public JComboBox comboStrategies;
    public JButton btnKStrategyChart;
    public JButton btnRTSUsageChart;
    public JButton btnCoveredPoiChart;
    public JButton btnSensorsReward;

    private void createUIComponents() {

        chartPanel = createChartPanel();
        comboStrategies=new JComboBox();
        spinRunNumber=new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        initComboStrategies();


    }

    private void initComboStrategies() {
//        comboStrategies.addItem("AllD");
//        comboStrategies.addItem("ALLC");
        comboStrategies.addItem("KC");
        comboStrategies.addItem("KDC");
        comboStrategies.addItem("KD");

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
        int val=(int)spinRunNumber.getValue();
        List<Double>bestRewardsForEachItereationOfRun=statistics.getBestRewardsForEachItereationOfRun((int)spinRunNumber.getValue());


        DefaultXYDataset dataset = new DefaultXYDataset();

        createSeries(series1, bestRewardsForEachItereationOfRun, dataset);

        String chartTitle = "Średnia nagroda dla każdej iteracji";
        String xLabel = "Numer iteracji";
        String yLabel = "nagroda";
        int tick= getTick(bestRewardsForEachItereationOfRun);

        JFreeChart chart = createChart(dataset, chartTitle, xLabel, yLabel, tick);
        chart.getXYPlot().getDomainAxis().setLabel(xLabel);
        ChartPanel pan=(ChartPanel)chartPanel;
        pan.setChart(chart);

        exportChartToSVG(chart,"MeanRewardChart");
    }

    private int getTick(List<Double> bestRewardsForEachItereationOfRun) {
        return 3*(1+bestRewardsForEachItereationOfRun.size()/100);
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

    public void showActiveSensorsChart(Statistics statistics) {
        String series1 = "Aktywne Sensory";
        List<Double>procentOfActiveSensorsForEachItereationOfRun=statistics.getProcetOfActiveSensors((int)spinRunNumber.getValue());


        DefaultXYDataset dataset = new DefaultXYDataset();

        createSeries(series1, procentOfActiveSensorsForEachItereationOfRun, dataset);

        String chartTitle = "Procentowy udział aktywnych sensorów";
        String xLabel = "Numer iteracji";
        String yLabel = "Aktywne Sensory[%]";
        int tick= getTick(procentOfActiveSensorsForEachItereationOfRun);

        JFreeChart chart = createChart(dataset, chartTitle, xLabel, yLabel, tick);
        chart.getXYPlot().getDomainAxis().setLabel(xLabel);
        ChartPanel pan=(ChartPanel)chartPanel;
        pan.setChart(chart);
        exportChartToSVG(chart,"ActiveSensorsChart");
    }

    public void strategiesChart(Statistics statistics) {
        String series1 = "Udział poszczególnych strategii";
        Map<String,List<Double>> procentOfStrategiesForEachItereationOfRun=statistics.getProcetOfStrategies((int)spinRunNumber.getValue());


        DefaultXYDataset dataset = new DefaultXYDataset();
        int tick=2;
        for(var pair:procentOfStrategiesForEachItereationOfRun.entrySet())
        {
            series1=pair.getKey();
            createSeries(series1, pair.getValue(), dataset);
            tick= getTick(pair.getValue());
        }

        String chartTitle = "Procentowy udział strategii";
        String xLabel = "Numer iteracji";
        String yLabel = "Udział strategii[%]";


        JFreeChart chart = createChart(dataset, chartTitle, xLabel, yLabel, tick);
        chart.getXYPlot().getDomainAxis().setLabel(xLabel);
        ChartPanel pan=(ChartPanel)chartPanel;
        pan.setChart(chart);
        exportChartToSVG(chart,"StrategiesUsageChart");
    }

    public void strategiesKChart(Statistics statistics) {
        String series1 = "Udział poszczególnych strategii";
        Map<Integer, List<Double>> procentOfUsageOfEachKInStartegy=statistics.getProcentOfUsageOfEachKInStartegy((int)spinRunNumber.getValue(),(String) comboStrategies.getSelectedItem());


        DefaultXYDataset dataset = new DefaultXYDataset();
        int tick=2;
        for(var pair:procentOfUsageOfEachKInStartegy.entrySet())
        {
            series1=""+pair.getKey();
            createSeries(series1, pair.getValue(), dataset);
            tick= getTick(pair.getValue());
        }

        String chartTitle = "Procentowy udział k w strategii";
        String xLabel = "Numer iteracji";
        String yLabel = "Udział strategii[%]";


        JFreeChart chart = createChart(dataset, chartTitle, xLabel, yLabel, tick);
        chart.getXYPlot().getDomainAxis().setLabel(xLabel);
        ChartPanel pan=(ChartPanel)chartPanel;
        pan.setChart(chart);
        exportChartToSVG(chart,"KIn"+(String) comboStrategies.getSelectedItem()+"chart");
    }

    public void setRTSUsageChart(Statistics statistics) {

        List<Double> porcentOfRTSUsage=statistics.getProcentOfRTSUsageInRun((int)spinRunNumber.getValue());



        String series1 = "Użycie RTS";




        DefaultXYDataset dataset = new DefaultXYDataset();

        createSeries(series1, porcentOfRTSUsage, dataset);

        String chartTitle = "Procent sensorów z aktywnym tagiem RTS";
        String xLabel = "Numer iteracji";
        String yLabel = "Procent";
        int tick= getTick(porcentOfRTSUsage);

        JFreeChart chart = createChart(dataset, chartTitle, xLabel, yLabel, tick);
        chart.getXYPlot().getDomainAxis().setLabel(xLabel);
        ChartPanel pan=(ChartPanel)chartPanel;
        pan.setChart(chart);
        exportChartToSVG(chart,"RTSChart");

    }

    private void exportChartToSVG(JFreeChart chart,String chartName) {
        SVGGraphics2D g2 = new SVGGraphics2D(600, 400);
        Rectangle r = new Rectangle(0, 0, 600, 400);
        chart.draw(g2, r);
        File f = new File(chartName+".svg");
        try {
            SVGUtils.writeToSVG(f, g2.getSVGElement());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCoveredPoiChart(Statistics statistics) {
        List<Double> porcentOfCoveredPoi=statistics.getProcentOfCoveredPoi((int)spinRunNumber.getValue());



        String series1 = "Pokrycie POI";




        DefaultXYDataset dataset = new DefaultXYDataset();

        createSeries(series1, porcentOfCoveredPoi, dataset);

        String chartTitle = "Procent pokrytych poi";
        String xLabel = "Numer iteracji";
        String yLabel = "Procent";
        int tick= getTick(porcentOfCoveredPoi);

        JFreeChart chart = createChart(dataset, chartTitle, xLabel, yLabel, tick);
        chart.getXYPlot().getDomainAxis().setLabel(xLabel);
        ChartPanel pan=(ChartPanel)chartPanel;
//        pan.setHorizontalAxisTrace(true);
//        pan.setMouseWheelEnabled(true);
        pan.setChart(chart);

        exportChartToSVG(chart,"POICovered");
    }

    public void setRewardSensorsChart(Statistics statistics) {

        String series1 = "Nagrody sensorów";
        Map<Integer, List<Double>> rewardsForSensors=statistics.getDataForSensorRewardChart((int)spinRunNumber.getValue());


        DefaultXYDataset dataset = new DefaultXYDataset();
        int tick=2;
        for(var pair:rewardsForSensors.entrySet())
        {
            series1="S"+pair.getKey();
            createSeries(series1, pair.getValue(), dataset);
            tick= getTick(pair.getValue());
        }

        String chartTitle = "Nagrody poszczególnych sensorów";
        String xLabel = "Numer iteracji";
        String yLabel = "Wartośc nagrody";


        JFreeChart chart = createChart(dataset, chartTitle, xLabel, yLabel, tick);
        chart.getXYPlot().getDomainAxis().setLabel(xLabel);
        ChartPanel pan=(ChartPanel)chartPanel;
        pan.setChart(chart);
        exportChartToSVG(chart,"NagrodySensorowChart");

    }
}
