import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {
    private record GraphData(float degrees, float path) {}

    private static void make(int n, float p, XYSeries series) {
        final GilbertGraph graph = new GilbertGraph(n, p);
        graph.generate();
        // System.out.println("Degrees " + n + "/" + p + ": " + graph.getAverageNodeDegree());
        // System.out.println("Avg. Path Length: " + graph.getAveragePathLength(100));

        new Thread(() -> {
            series.add(graph.getAveragePathLength(100), n);
            if (n % 100 == 0) {
                System.out.println("Thread #" + n + " finished");
            }
        }).start();
    }

    public static void main(String[] args) {
        XYSeries series = new XYSeries("Mean path length by constant average node degrees.");
        LogAxis xAxis = new LogAxis("Avg. Path Length");
        xAxis.setBase(2);
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        LogAxis yAxis = new LogAxis("Number of Nodes");
        yAxis.setBase(2);
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // generate graph with p = 1/n in increments of 100 n
        for (int n = 100; n < 2500; n+=10) {
            make(n, 1.0f/n, series);
        }

        XYSeriesCollection sc = new XYSeriesCollection(series);
        // sc.addSeries(series);
        XYDataset average = MovingAverage.createMovingAverage(
                sc, "-MAVG", 3 * 24 * 60 * 60 * 1000L, 0L
        );

        XYPlot plot = new XYPlot(average,
                xAxis, yAxis, new XYLineAndShapeRenderer(true, false));
        JFreeChart chart = new JFreeChart(
                "Mean path length by constant average node degrees.", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        File file = new File("./log_log.png");
        try {
            ChartUtils.saveChartAsPNG(file, chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("LogAxis Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }
}
