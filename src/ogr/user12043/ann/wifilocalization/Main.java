package ogr.user12043.ann.wifilocalization;

/**
 * Created on 7.12.2019 - 18:46
 * part of bulanikOdev2
 *
 * @author user12043
 */
public class Main {
    public static void main(String[] args) {
        WifiLocalizationModel model = new WifiLocalizationModel();
        model.train();
        model.test();
    }
}
