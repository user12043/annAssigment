package ogr.user12043.ann.wifilocalization;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created on 7.12.2019 - 18:46
 * part of bulanikOdev2
 *
 * @author user12043
 */
public class Main {
    public static void main(String[] args) throws IOException {
        WifiLocalizationModel model = new WifiLocalizationModel();
        int selectedOption;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            try {
                printTheOptions();
                selectedOption = scanner.nextInt();
                switch (selectedOption) {
                    case 1:
                        model.train();
                        break;
                    case 2:
                        model.test();
                        break;
                    case 3:
                        Utils.updateMinMax();
                        double[] inputs = getInputs(scanner);
                        // max min
                        inputs = Utils.normalize(inputs);

                        int result = model.testSingle(inputs);
                        System.out.println("Sonuç: Oda " + result);
                        break;
                    case 4:
                        System.exit(0);
                        break;
                    default:
                        System.err.println("Invalid input!");
                }
                //noinspection ResultOfMethodCallIgnored
                System.in.read();
            } catch (InputMismatchException e) {
                System.err.println("Hatalı giriş!");
            }
        }
    }

    private static void printTheOptions() {
        System.out.print(
                "==================\n" +
                        "1- Ağı Eğit\n" +
                        "2- Ağı Test Et\n" +
                        "3- Tek Veri ile Test Et\n" +
                        "4- Çıkış\n" +
                        "Seç....: "
        );
    }

    private static double[] getInputs(Scanner scanner) {
        double[] inputs = new double[Constants.INPUTS_NUMBER];
        for (int i = 0; i < inputs.length; i++) {
            double input = 0;
            boolean valid = false;
            while (!valid) {
                System.out.print("Access Point " + (i + 1) + " (" + Constants.MIN_SIGNAL_STRENGTH + ", " + Constants.MAX_SIGNAL_STRENGTH + "): ");
                input = scanner.nextDouble();
                valid = input <= Constants.MAX_SIGNAL_STRENGTH && input > Constants.MIN_SIGNAL_STRENGTH;
                if (!valid) {
                    System.err.println("\nSinyal gücü değeri " + Constants.MIN_SIGNAL_STRENGTH + " ve " + Constants.MAX_SIGNAL_STRENGTH + " arasında olmalıdır!");
                }
            }
            inputs[i] = input;
        }

        return inputs;
    }
}
