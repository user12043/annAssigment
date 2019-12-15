package ogr.user12043.ann.wifilocalization;

import org.neuroph.core.data.DataSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created on 8.12.2019 - 08:54
 * part of bulanikOdev2
 *
 * @author user12043
 */
class Utils {
    private static double[] minimums;
    private static double[] maximums;
    private static List<Integer> testIndices;
    private static DataSet trainDataSet;
    private static DataSet testDataSet;

    private static double normalize(double max, double min, double val) {
        return (val - min) / (max - min);
    }

    static double[] normalize(double[] inputs) {
        double[] normalized = new double[Constants.INPUTS_NUMBER];
        for (int i = 0; i < inputs.length; i++) {
            normalized[i] = normalize(maximums[i], minimums[i], inputs[i]);
        }
        return normalized;
    }

    /**
     * Set max and min of wifi signal strength (-30 to -100)
     */
    static void updateMinMax() {
        maximums = new double[Constants.INPUTS_NUMBER];
        maximums = Arrays.stream(maximums).map(v -> Constants.MAX_SIGNAL_STRENGTH).toArray();
        minimums = new double[Constants.INPUTS_NUMBER];
        minimums = Arrays.stream(minimums).map(v -> Constants.MIN_SIGNAL_STRENGTH).toArray();
    }

    private static void updateTestIndices(int recordsSize) {
        int amountOfTestData = recordsSize * Constants.TEST_DATA_PERCENT / 100;

        // create ordered number list
        if (testIndices == null) {
            testIndices = new ArrayList<>();
            for (int i = 0; i < amountOfTestData; i++) {
                testIndices.add(i, i);
            }
        }
        Collections.shuffle(testIndices);
    }

    static void loadDataSet() {
        try {
            trainDataSet = new DataSet(Constants.INPUTS_NUMBER, Constants.OUTPUTS_NUMBER);
            testDataSet = new DataSet(Constants.INPUTS_NUMBER, Constants.OUTPUTS_NUMBER);

            updateMinMax();

            BufferedReader reader = new BufferedReader(new FileReader(Constants.DATA_FILE_NAME));

            List<String> collect = reader.lines().collect(Collectors.toList());

            // the dataset file contains same number of records for each output value (1, 2, 3, 4)
            int recordPerOutput = collect.size() / Constants.OUTPUTS_NUMBER; // equal to 500
            updateTestIndices(recordPerOutput);

            IntStream.range(0, Constants.OUTPUTS_NUMBER).forEach(currentOutput -> IntStream.range(0, recordPerOutput).forEach(currentLine -> {
                List<String> columns = Arrays.asList(collect.get(currentLine + (recordPerOutput * currentOutput)).split(Constants.DATA_SEPARATOR));

                // check number of columns
                if (columns.size() != Constants.INPUTS_NUMBER + Constants.OUTPUTS_NUMBER) {
                    throw new InputMismatchException("Invalid data format! At line: " + currentLine + (currentOutput * recordPerOutput));
                }

                // inputs
                double[] inputs = columns.subList(0, Constants.INPUTS_NUMBER).stream().mapToDouble(Double::parseDouble).toArray();
                for (int a = 0; a < inputs.length; a++) {
                    inputs[a] = normalize(maximums[a], minimums[a], inputs[a]);
                }

                // outputs
                double[] outputs = columns.subList(Constants.INPUTS_NUMBER, columns.size()).stream().mapToDouble(Double::parseDouble).toArray();

                // add
                if (!testIndices.contains(currentLine)) {
                    trainDataSet.add(inputs, outputs);
                } else {
                    testDataSet.add(inputs, outputs);
                }
            }));
            reader.close();
        } catch (IOException e) {
            System.err.println("An error occurred while loading dataset:");
            e.printStackTrace();
        }
    }

    static int getOutput(double[] output) {
        for (int i = 0; i < output.length; i++) {
            if (output[i] == 1) {
                return i + 1;
            }
        }
//        System.out.println("-1 for " + Arrays.toString(output));
        return -1;
    }

    static double[] round(double[] doubles) {
        return Arrays.stream(doubles).map(Math::round).toArray();
    }

    public static DataSet getTrainDataSet() {
        return trainDataSet;
    }

    public static DataSet getTestDataSet() {
        if (testDataSet == null) {
            loadDataSet();
        }
        return testDataSet;
    }

    static void writeErrorLog(List<Double> errors) {
        try {
            FileWriter writer = new FileWriter(Constants.TRAIN_ERROR_LOG_FILE_NAME, false);
            for (Double error : errors) {
                writer.write(error + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
