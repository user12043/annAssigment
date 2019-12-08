package ogr.user12043.ann.wifilocalization;

import org.neuroph.core.data.DataSet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;

/**
 * Created on 8.12.2019 - 08:54
 * part of bulanikOdev2
 *
 * @author user12043
 */
class Utils {
    private static double[] minimums;
    private static double[] maximums;

    private static double minMax(double max, double min, double val) {
        return (val - min) / (max - min);
    }

    /**
     * Reads all data and calculate minimum and maximum of each input
     */
    private static void updateMinMax() throws FileNotFoundException {
        maximums = new double[Constants.INPUTS_NUMBER];
        maximums = Arrays.stream(maximums).map(v -> Double.MIN_VALUE).toArray();
        minimums = new double[Constants.INPUTS_NUMBER];
        minimums = Arrays.stream(minimums).map(v -> Double.MAX_VALUE).toArray();

        BufferedReader reader = new BufferedReader(new FileReader(Constants.DATA_FILE_NAME));
        reader.lines()
                // iterate over all lines as list
                .map(line -> Arrays.asList(line.split(Constants.DATA_SEPARATOR)))
                // get inputs array
                .map(columnsString -> columnsString.subList(0, Constants.INPUTS_NUMBER)
                        .stream().mapToDouble(Double::parseDouble).toArray())
                // do max min process
                .forEach(inputs -> {
                    for (int i = 0; i < inputs.length; i++) {
                        if (inputs[i] > maximums[i]) {
                            maximums[i] = inputs[i];
                        }

                        if (inputs[i] < minimums[i]) {
                            minimums[i] = inputs[i];
                        }
                    }
                });
    }

    static DataSet loadDataSet(String fileName) throws IOException {
        updateMinMax();

        DataSet dataSet = new DataSet(Constants.INPUTS_NUMBER, Constants.OUTPUTS_NUMBER);
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        reader.lines().map(line -> Arrays.asList(line.split(Constants.DATA_SEPARATOR))).forEach(columnsString -> {
            if (columnsString.size() != Constants.INPUTS_NUMBER + Constants.OUTPUTS_NUMBER) {
                throw new InputMismatchException("Invalid data format!");
            }

            // inputs
            double[] inputs = columnsString.subList(0, Constants.INPUTS_NUMBER).stream().mapToDouble(Double::parseDouble).toArray();
            for (int i = 0; i < inputs.length; i++) {
                inputs[i] = minMax(maximums[i], minimums[i], inputs[i]);
            }

            //outputs
            double[] outputs = columnsString.subList(Constants.INPUTS_NUMBER, columnsString.size()).stream().mapToDouble(Double::parseDouble).toArray();

            // add values
            dataSet.add(inputs, outputs);
        });
        reader.close();
        return dataSet;
    }

    static int getOutput(double[] output) {
        for (int i = 0; i < output.length; i++) {
            if (output[i] == 1) {
                return i + 1;
            }
        }
        return -1;
    }

    static double[] round(double[] doubles) {
        return Arrays.stream(doubles).map(Math::round).toArray();
    }
}
