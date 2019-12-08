package ogr.user12043.ann.wifilocalization;

import org.neuroph.core.data.DataSet;

/**
 * Created on 7.12.2019 - 18:46
 * part of bulanikOdev2
 *
 * @author user12043
 */
public class Main {
    public static void main(String[] args) {
        DataSet trainDataSet = Data.getInstance().getTrainDataSet();
        DataSet testDataSet = Data.getInstance().getTestDataSet();
    }
}
