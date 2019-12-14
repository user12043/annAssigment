package ogr.user12043.ann.wifilocalization;

import java.io.IOException;

/**
 * Created on 7.12.2019 - 18:46
 * part of bulanikOdev2
 *
 * @author user12043
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Utils.loadDataSet();
        WifiLocalizationModel model = new WifiLocalizationModel();
        model.train();
        model.test();
    }
}
