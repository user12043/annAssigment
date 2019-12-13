package ogr.user12043.ann.wifilocalization;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.learning.error.MeanAbsoluteError;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

import java.io.File;

/**
 * Created on 7.12.2019 - 23:01
 * part of bulanikOdev2
 *
 * @author user12043
 */
class WifiLocalizationModel {
    double[] errors;
    private MomentumBackpropagation momentumBackpropagation;

    WifiLocalizationModel() {
        momentumBackpropagation = new MomentumBackpropagation();
        momentumBackpropagation.setMomentum(Constants.MOMENTUM);
        momentumBackpropagation.setLearningRate(Constants.LEARNING_RATE);
        momentumBackpropagation.setErrorFunction(new MeanAbsoluteError());
        momentumBackpropagation.setMaxIterations(Constants.MAX_ITERATIONS);
        momentumBackpropagation.setMaxError(Constants.MAX_ERROR);
        errors = new double[Constants.MAX_ITERATIONS];
    }

    void train() {
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
                errors[current - 1] = ann.getLearningRule().getPreviousEpochError();
                System.out.println(ann.getLearningRule().getPreviousEpochError());
            }
        });

        ann.learn(Data.getInstance().getTrainDataSet());

        ann.save(Constants.RESULT_FILE_NAME);
        System.out.println("TRAIN END");
    }

    void test() {
        File file = new File(Constants.RESULT_FILE_NAME);
        if (!file.exists()) {
            System.err.println("Train first!");
            return;
        }
        //noinspection rawtypes
        NeuralNetwork network = NeuralNetwork.createFromFile(Constants.RESULT_FILE_NAME);
        for (DataSetRow row : Data.getInstance().getTestDataSet()) {
            network.setInput(row.getInput());
            network.calculate();
            System.out.println(Utils.getOutput(Utils.round(network.getOutput())) + ", " + Utils.getOutput(row.getDesiredOutput()));
        }
    }
}
