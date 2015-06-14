package com.alex.kucher;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;

public class FilterApplet extends JApplet {

    private String filePath;
    private JList<String> spFunctions;
    private JList<String> filterFunctions;
    private static final String[] SPECTRAL_PROPERTIES_FUNCTIONS = new String[]{"Hamming", "Von Hann"};
    private static final String[] FILTER_FUNCTIONS = new String[]{"Blackman", "Hamming", "Hann", "Square"};

    public void init() {
        setLayout(new FlowLayout());
        setSize(new Dimension(500, 300));

        final Panel filterFunc = new Panel();
        filterFunc.add(new Label("Filter Functions"));
        filterFunctions = new JList<>(FILTER_FUNCTIONS);
        filterFunctions.setSelectedIndex(0);
        filterFunc.add(filterFunctions);

        filterFunc.add(new Label("Spectral Properties Functions"));
        spFunctions = new JList<>(SPECTRAL_PROPERTIES_FUNCTIONS);
        spFunctions.setSelectedIndex(0);
        filterFunc.add(spFunctions);
        add(filterFunc);

        final Panel p = new Panel();
        p.setLayout(new GridLayout(1, 1));
        final Button file = new Button("Choose File");
        p.add(file);
        add(p);

        final Panel p1 = new Panel();
        p1.setLayout(new GridLayout(3, 2));
        final Button fromFile = new Button("Plot data from file");
        p1.add(fromFile);
        final Button filterFromFile = new Button("Plot data from file apply filter");
        p1.add(filterFromFile);
        final Button fftFromFile = new Button("Plot data from file apply FFT");
        p1.add(fftFromFile);
        final Button fftFilterFromFile = new Button("Plot data from file apply FFT and filter");
        p1.add(fftFilterFromFile);
        add(p1);

        final Panel p2 = new Panel();
        p2.setLayout(new GridLayout(2, 1));
        final Button dspTest = new Button("Spectral Properties of Von Hann & Hamming Windows");
        p2.add(dspTest);
        final Button generatedDSP = new Button("Plot Generated Values for Spectral Properties");
        p2.add(generatedDSP);
        add(p2);

        file.addActionListener(e -> chooseFile());
        fromFile.addActionListener(e -> {
            while(null == filePath) {
                chooseFile();
            }
            final double[] data = Utils.loadData(Paths.get(filePath));
            View.plot("Original Values", data);
        });
        filterFromFile.addActionListener(e -> {
            while(null == filePath) {
                chooseFile();
            }
            final double[] data = Utils.loadData(Paths.get(filePath));
            View.plot("Original Values with Filter", Filter.filter(data, 100, 100, 20, 50, Filter.getWinFunction(getFunctionName())));
        });
        fftFromFile.addActionListener(e -> {
            while(null == filePath) {
                chooseFile();
            }
            final double[] data = Utils.loadData(Paths.get(filePath));
            View.plot("Original Values after Fourier Transform", FourierTransform.fftd(data));
        });
        fftFilterFromFile.addActionListener(e -> {
            while (null == filePath) {
                chooseFile();
            }
            final double[] data = Utils.loadData(Paths.get(filePath));
            View.plot("Fourier Transform with Filter", Filter.filter(FourierTransform.fftd(data), 100, 100, 20, 50, Filter.getWinFunction(getFunctionName())));
        });

        dspTest.addActionListener(e -> View.plot(SpectralProperties.dsp(320, 100, 10, SpectralProperties.getWinFunction(getSPFunctionName()))));
        generatedDSP.addActionListener(e -> View.plot("Generated Spectral Properties", SpectralProperties.generateValues(10, SpectralProperties.getWinFunction(getSPFunctionName()))));
    }

    private String getSPFunctionName() {
        return spFunctions.getSelectedValue();
    }

    private String getFunctionName() {
        return filterFunctions.getSelectedValue();
    }

    private void chooseFile() {
        final JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(FilterApplet.this) == JFileChooser.APPROVE_OPTION) {
            filePath = fc.getSelectedFile().getAbsolutePath();
        }
    }
}