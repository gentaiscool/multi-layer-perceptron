import java.util.ArrayList;
import java.util.Random;

public class NN {

	private ArrayList<Layer> layers = new ArrayList<>();
	private Random rand = new Random();
	private final double epsilon = 0.00000000001d;
	private final double learningRate = 0.5f;
	private final double momentum = 0.5f;
	private boolean debug = false;

	private Neuron bias = new Neuron();
	private int[] neurons;
	private double[][] input;
	private double[][] expectedOutput;
	private double minError = 10000000000d;

	public static void main(String[] args) {
		// for current development, only support 1 hidden layer
		int[] neurons = { 2, 4, 1 };
		double[][] input = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };
		double[][] expectedOutput = { { 1 }, { 0 }, { 0 }, { 1 } };
		int epoch = 100000;

		NN nn = new NN(neurons, input, expectedOutput, false);
		nn.train(epoch);

		nn.test(input);
	}

	public NN(int[] neurons, double[][] input, double[][] expectedOutput, boolean debug) {
		// constructor
		this.neurons = neurons;
		this.input = input;
		this.expectedOutput = expectedOutput;
		this.debug = debug;

		for (int i = 0; i < neurons.length; i++) {
			Layer layer = new Layer();
			if (i == 0) {
				// input layer
				for (int j = 0; j < neurons[i]; j++) {
					Neuron neuron = new Neuron();
					layer.addNeuron(neuron);
				}
			} else if (i == neurons.length - 1) {
				// output layer
				for (int j = 0; j < neurons[i]; j++) {
					Neuron neuron = new Neuron();
					neuron.addBiasConnection(bias);
					neuron.addInputConnection(layers.get(i - 1).getNeurons());
					layer.addNeuron(neuron);
				}
			} else {
				// hidden layer
				for (int j = 0; j < neurons[i]; j++) {
					Neuron neuron = new Neuron();
					neuron.addBiasConnection(bias);
					neuron.addInputConnection(layers.get(i - 1).getNeurons());
					layer.addNeuron(neuron);
				}
			}
			layers.add(layer);
		}

		// initialise random weight
		for (int i = 1; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			for (int j = 0; j < layer.getNeurons().size(); j++) {
				Neuron neuron = layer.getNeurons().get(j);
				for (int k = 0; k < neuron.getInputConnection().size(); k++) {
					Connection edge = neuron.getInputConnection().get(k);
					edge.setWeight(rand.nextDouble() * 2 - 1);
				}
			}
		}

		// reset counter
		Neuron.counter = 0;
		Connection.counter = 0;
	}

	public void test(double[][] input) {
		// test NN
		System.out.println("> Test started");
		for (int i = 0; i < input.length; i++) {
			setInput(input[i]);

			feedforward();
			double[] output = getOutput();

			String strInput = "";
			String strOutput = "";
			String strExpectedOutput = "";

			for (int j = 0; j < input[i].length; j++) {
				strInput += input[i][j];
				if (j < input[i].length - 1)
					strInput += " ";
			}

			for (int j = 0; j < output.length; j++) {
				strOutput += output[j];
				if (j < output.length - 1)
					strOutput += " ";
			}

			for (int j = 0; j < expectedOutput[i].length; j++) {
				strExpectedOutput += expectedOutput[i][j];
				if (j < expectedOutput[i].length - 1)
					strExpectedOutput += " ";
			}

			System.out.println("Input: " + strInput + ", Actual Output: " + strOutput + " , Expected Output: "
					+ strExpectedOutput);
		}
		System.out.println("Min error: " + minError);
		System.out.println("> Test ended");
	}

	public void train(int epoch) {
		// train NN
		System.out.println("> Train started");
		for (int x = 0; x < epoch; x++) {
			double error = 0d;
			for (int i = 0; i < input.length; i++) {
				// for each sample
				// set input
				setInput(input[i]);

				feedforward();
				double[] output = getOutput();

				error = 0d;

				for (int j = 0; j < expectedOutput[i].length; j++) {
					double err = Math.pow(output[j] - expectedOutput[i][j], 2);
					error += err;
				}
				minError = Math.min(minError, error);

				backpropagation(expectedOutput[i]);
			}
			if (debug)
				System.out.println("Error (" + (x + 1) + ") " + error);
		}
		System.out.println("> Train ended");
	}

	public double[] getOutput() {
		// getting output from the output layer
		double[] outputs = new double[layers.get(layers.size() - 1).getNeurons().size()];
		for (int i = 0; i < layers.get(layers.size() - 1).getNeurons().size(); i++)
			outputs[i] = layers.get(layers.size() - 1).getNeurons().get(i).getOutput();
		return outputs;
	}

	public void setInput(double input[]) {
		// set input
		for (int i = 0; i < layers.get(0).getNeurons().size(); i++) {
			Neuron neuron = layers.get(0).getNeurons().get(i);
			neuron.setOutput(input[i]);
		}
	}

	public double normalize(double x){
		if (x < 0)
			return epsilon;
		else if (x > 1)
			return 1 - epsilon;
		else
			return x;
	}
	
	public void feedforward() {
		// calculate output of each layer from first hidden layer until output
		// layer
		for (int i = 1; i < layers.size(); i++) {
			for (int j = 0; j < layers.get(i).getNeurons().size(); j++) {
				layers.get(i).getNeurons().get(j).calculateOutput();
			}
		}
	}

	public void backpropagation(double[] expectedOutput) {
		// normalize expected output values
		for (int i = 0; i < expectedOutput.length; i++)
			expectedOutput[i] = normalize(expectedOutput[i]);

		// updating weight in output layer
		Layer outputLayer = layers.get(layers.size() - 1);
		for (int i = 0; i < outputLayer.getNeurons().size(); i++) {
			Neuron neuron = outputLayer.getNeurons().get(i);
			ArrayList<Connection> edges = neuron.getInputConnection();
			for (int j = 0; j < edges.size(); j++) {
				Connection edge = edges.get(j);

				double ak = neuron.getOutput();
				double ai = edges.get(j).getLeftNeuron().getOutput();
				double desiredOutput = expectedOutput[i];

				double partialDerivative = -ak * (1 - ak) * ai * (desiredOutput - ak);
				double deltaWeight = -learningRate * partialDerivative;
				double newWeight = edge.getWeight() + deltaWeight;

				edge.setDeltaWeight(deltaWeight);
				edge.setWeight(newWeight + momentum * edge.getPrevDeltaWeight());
			}
		}

		// updating weight in hidden layers
		for (int i = 1; i < layers.size() - 1; i++) {
			Layer layer = layers.get(i);
			for (int j = 0; j < layer.getNeurons().size(); j++) {
				Neuron neuron = layer.getNeurons().get(j);
				ArrayList<Connection> edges = neuron.getInputConnection();
				for (int k = 0; k < edges.size(); k++) {
					Connection edge = edges.get(k);
					double aj = neuron.getOutput();
					double ai = edge.getLeftNeuron().getOutput();
					double sumKoutputs = 0;

					for (int l = 0; l < outputLayer.getNeurons().size(); l++) {
						Neuron outputNeuron = outputLayer.getNeurons().get(l);
						double wjk = outputNeuron.getConnection(neuron.getId()).getWeight();
						double desiredOutput = expectedOutput[l];
						double ak = outputNeuron.getOutput();
						sumKoutputs += -(desiredOutput - ak) * ak * (1 - ak) * wjk;
					}
					double partialDerivative = aj * (1 - aj) * ai * sumKoutputs;
					double deltaWeight = -learningRate * partialDerivative;
					double newWeight = edge.getWeight() + deltaWeight;
					edge.setDeltaWeight(deltaWeight);
					edge.setWeight(newWeight + momentum * edge.getPrevDeltaWeight());
				}
			}
		}
	}
}