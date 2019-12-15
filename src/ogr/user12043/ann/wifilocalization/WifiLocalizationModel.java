package ogr.user12043.ann.wifilocalization;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.learning.error.MeanAbsoluteError;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 7.12.2019 - 23:01
 * part of bulanikOdev2
 *
 * @author user12043
 */
class WifiLocalizationModel {
    List<Double> errors;
    private MomentumBackpropagation momentumBackpropagation;

    WifiLocalizationModel() {
        momentumBackpropagation = new MomentumBackpropagation();
        momentumBackpropagation.setMomentum(Constants.MOMENTUM);
        momentumBackpropagation.setLearningRate(Constants.LEARNING_RATE);
        momentumBackpropagation.setErrorFunction(new MeanSquaredError());
        momentumBackpropagation.setMaxIterations(Constants.MAX_ITERATIONS);
        momentumBackpropagation.setMaxError(Constants.MAX_ERROR);
        errors = new ArrayList<>();
    }

    void train() {
        Utils.loadDataSet();

        System.out.println("TRAIN START");
        MultiLayerPerceptron ann = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, Constants.INPUTS_NUMBER, Constants.HIDDEN_LAYERS_NUMBER, Constants.OUTPUTS_NUMBER);
        ann.setLearningRule(momentumBackpropagation);

        // produces NullPointerException
        /*IntStream.range(0, Constants.MAX_ITERATIONS).forEach(i -> {
            ann.getLearningRule().doOneLearningIteration(Data.getInstance().getTrainDataSet());
            errors.add(i, (i == 0) ? 1d : ann.getLearningRule().getPreviousEpochError());
        });*/

        // The following method
//        ann.addListener(event -> {
//            if (event.getEventType().compareTo(NeuralNetworkEvent.Type.CALCULATED) == 0) {
//                int current = ann.getLearningRule().getCurrentIteration();
//                errors[current] = ann.getLearningRule().getPreviousEpochError();
//                System.out.println("Current: " + current + " " + Data.getInstance().getTrainDataSet().get(current));
//            }
//        });

        ann.getLearningRule().addListener(event -> {
            if (event.getEventType().compareTo(LearningEvent.EPOCH_ENDED) == 0) {
                int current = ann.getLearningRule().getCurrentIteration();
                errors.add(current - 1, ann.getLearningRule().getTotalNetworkError());
//                System.out.println(ann.getLearningRule().getTotalNetworkError());
            }
        });

        ann.learn(Utils.getTrainDataSet());
        ann.save(Constants.RESULT_FILE_NAME);
        Utils.writeErrorLog(errors);
        System.out.println("TRAIN END");
        System.out.println("Son hata = " + ann.getLearningRule().getTotalNetworkError());
    }

    void test() {
        File file = new File(Constants.RESULT_FILE_NAME);
        if (!file.exists()) {
            System.err.println("Train first!");
            return;
        }

        int totalErrorResults = 0;

        //noinspection rawtypes
        NeuralNetwork network = NeuralNetwork.createFromFile(Constants.RESULT_FILE_NAME);
        MeanAbsoluteError meanAbsoluteError = new MeanAbsoluteError();

        for (DataSetRow row : Utils.getTestDataSet()) {
            network.setInput(row.getInput());
            network.calculate();
            int output = Utils.getOutput(Utils.round(network.getOutput()));
            int desired = Utils.getOutput(row.getDesiredOutput());
            if (output != desired) {
//                System.out.println(output + ", " + desired);
                totalErrorResults++;
            }
            meanAbsoluteError.addPatternError(network.getOutput(), row.getDesiredOutput());
//            testErrorSum += meanAbsoluteError.getTotalError();
        }
        System.out.println("Toplam hatalı kayıt: " + totalErrorResults);
        System.out.println("Ortalama hata: " + meanAbsoluteError.getTotalError() / Utils.getTestDataSet().size());
    }

    int testSingle(double[] inputs) {
        File file = new File(Constants.RESULT_FILE_NAME);
        if (!file.exists()) {
            System.err.println("Train first!");
            return 0;
        }

        //noinspection rawtypes
        NeuralNetwork network = NeuralNetwork.createFromFile(Constants.RESULT_FILE_NAME);
        network.setInput(inputs);
        network.calculate();
        return Utils.getOutput(Utils.round(network.getOutput()));
    }
}
