package sample;

import java.io.IOException;

import core.NN;
import tool.MNISTReader;

public class mnist {
	public static void main(String[] args) throws IOException {
		MNISTReader reader = new MNISTReader();
		
		int[] neurons = { 784, 4, 10 };
		double[][] input = reader.getInput();
		double[][] expectedOutput = reader.getExpectedOutput();
		int epoch = 2;

		NN nn = new NN(neurons, input, expectedOutput, false);
		nn.train(epoch);
		nn.test(input);
	}
}
