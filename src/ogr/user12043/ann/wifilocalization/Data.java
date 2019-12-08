package ogr.user12043.ann.wifilocalization;

import org.neuroph.core.data.DataSet;

import java.io.IOException;

/**
 * Created on 7.12.2019 - 23:01
 * part of bulanikOdev2
 *
 * @author user12043
 */
class Data {
    private static Data data;
    private DataSet trainDataSet;
    private DataSet testDataSet;

    private Data() {
        try {
            loadTrainDataSet();
            loadTestDataSet();
        } catch (IOException e) {
            System.err.println("Could not load the dataset!");
            e.printStackTrace();
        }
    }

    static Data getInstance() {
        if (data == null) {
            data = new Data();
        }
        return data;
    }

    DataSet getTrainDataSet() {
        return trainDataSet;
    }

    DataSet getTestDataSet() {
        return testDataSet;
    }

    private void loadTrainDataSet() throws IOException {
        if (trainDataSet == null) {
            trainDataSet = Utils.loadDataSet(Constants.TRAIN_FILE_NAME);
        }
    }

    private void loadTestDataSet() throws IOException {
        if (testDataSet == null) {
            testDataSet = Utils.loadDataSet(Constants.TEST_FILE_NAME);
        }
    }
}
