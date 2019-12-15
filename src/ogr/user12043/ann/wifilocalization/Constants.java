package ogr.user12043.ann.wifilocalization;

/**
 * Created on 7.12.2019 - 19:05
 * part of bulanikOdev2
 *
 * @author user12043
 */
public interface Constants {
    String DATA_FILE_NAME = "data/wifi_localization.txt";
    String RESULT_FILE_NAME = "data/wifi_localization_result.nnet";
    String TRAIN_ERROR_LOG_FILE_NAME = "data/train_error_log.log";
    int INPUTS_NUMBER = 7;
    int OUTPUTS_NUMBER = 4;
    String DATA_SEPARATOR = "\\s";
    int TEST_DATA_PERCENT = 30;
    int HIDDEN_LAYERS_NUMBER = 5;
    double MOMENTUM = 0.9;
    double LEARNING_RATE = 0.1;
    double MAX_ERROR = 0.0000001;
    int MAX_ITERATIONS = 300;
    int MAX_SIGNAL_STRENGTH = -30;
    int MIN_SIGNAL_STRENGTH = -100;
}
