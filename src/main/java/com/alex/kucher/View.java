package com.alex.kucher;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.event.WindowEvent;

public class View extends ApplicationFrame {
    public View(String applicationTitle, String chartTitle, String xTitle, String yTitle, XYDataset dataPoints) {
        super(applicationTitle);
        final JFreeChart lineChart = ChartFactory.createXYLineChart(
                chartTitle,
                xTitle,
                yTitle,
                dataPoints,
                PlotOrientation.VERTICAL,
                false, false, false);

        final ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));
//        final XYPlot plot = lineChart.getXYPlot();
//        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//        renderer.setSeriesPaint(0, Color.RED);
//        renderer.setSeriesStroke(0, new BasicStroke(4.0f));
//        plot.setRenderer(renderer);
        setContentPane(chartPanel);
    }

    public static void plot(final double[] dataPoints) {
        plot("Spectrum Window", "Filter", "Omega", "|W(Omega)|, dB", dataPoints);
    }

    public static void plot(final String applicationTitle, final double[] dataPoints) {
        plot(applicationTitle, null, null, null, dataPoints);
    }

    public static void plot(final String applicationTitle, final String chartTitle, final String xTitle, final String yTitle, final double[] dataPoints) {
        final XYSeries series = new XYSeries("Spectra windows");
        for (int i = 0; i < dataPoints.length; i++) {
            series.add(i, dataPoints[i]);
        }

        final XYSeriesCollection xyDataset = new XYSeriesCollection(series);
        final View chart = new View(applicationTitle, chartTitle, xTitle, yTitle, xyDataset);
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }

    @Override
    public void windowClosing(WindowEvent event) {}
}