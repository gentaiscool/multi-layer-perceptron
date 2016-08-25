package sample;

import core.NN;

public class xor {
	public static void main(String[] args) {
		// for current development, only support 1 hidden layer
		int[] neurons = { 2, 4, 1 };
		double[][] input = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };
		double[][] expectedOutput = { { 1 }, { 0 }, { 0 }, { 1 } };
		int epoch = 10000;

		NN nn = new NN(neurons, input, expectedOutput, false);
		nn.train(epoch);

		nn.test(input);
	}
}
