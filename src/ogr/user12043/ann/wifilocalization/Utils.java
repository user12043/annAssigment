package ogr.user12043.ann.wifilocalization;

import org.neuroph.core.data.DataSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

/**
 * Created on 8.12.2019 - 08:54
 * part of bulanikOdev2
 *
 * @author user12043
 */
class Utils {
    static DataSet loadDataSet(String fileName) throws IOException {
        DataSet dataSet = new DataSet(Constants.INPUTS_NUMBER, Constants.OUTPUTS_NUMBER);
        BufferedReader readerTrain = new BufferedReader(new FileReader(fileName));
        readerTrain.lines().forEach(line -> {
            List<String> columnsString = Arrays.asList(line.split(Constants.DATA_SEPARATOR));
            if (columnsString.size() != Constants.INPUTS_NUMBER + Constants.OUTPUTS_NUMBER) {
                throw new InputMismatchException("Invalid data format!");
            }
            double[] inputs = columnsString.subList(0, Constants.INPUTS_NUMBER).stream().mapToDouble(Double::parseDouble).toArray();
            double[] outputs = columnsString.subList(Constants.INPUTS_NUMBER, columnsString.size()).stream().mapToDouble(Double::parseDouble).toArray();
            dataSet.add(inputs, outputs);
        });
        return dataSet;
    }
}
