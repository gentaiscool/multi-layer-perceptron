package sample;

import java.io.IOException;

import core.NN;
import tool.MNISTReader;

public class mnist {
	public static void main(String[] args) throws IOException {
		MNISTReader reader = new MNISTReader();

		int[] neurons = { 784, 10, 10 };
		double[][] input = reader.getInput();
		double[][] expectedOutput = reader.getExpectedOutput();
		int epoch = 1000;

		NN nn = new NN(neurons, input, expectedOutput, false, 0.00000000001d, 0.2f, 0.4f);
		nn.train(epoch);

		System.out.println("> Result");
		int num_positive = 0, num_negative = 0;
		double[][] result = nn.test(input);
		for (int i = 0; i < result.length; i++) {
			double maxOutput = 0, idxOutput = 0;
			double maxExpectedOutput = 0, idxExpectedOutput = 0;
			for (int j = 0; j < result[i].length; j++) {
				if (maxOutput < result[i][j]) {
					maxOutput = result[i][j];
					idxOutput = j;
				}
			}
			for (int j = 0; j < expectedOutput[i].length; j++) {
				if (maxExpectedOutput < expectedOutput[i][j]) {
					maxExpectedOutput = expectedOutput[i][j];
					idxExpectedOutput = j;
				}
			}
			if (idxOutput == idxExpectedOutput)
				num_positive++;
			else
				num_negative++;
		}
		System.out.println("Positive: " + num_positive + ", Negative: " + num_negative);
	}
}
