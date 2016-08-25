package core;

public class Connection {
	
	private double weight = 0;
	private double prevDeltaWeight = 0;
	private double deltaWeight = 0;
	private Neuron leftNeuron, rightNeuron;
	private int id;
	
	public static int counter = 0;
	
	public Connection(Neuron leftNeuron, Neuron rightNeuron){
		this.leftNeuron = leftNeuron;
		this.rightNeuron = rightNeuron;
		counter++;
		id = counter;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getPrevDeltaWeight() {
		return prevDeltaWeight;
	}

	public void setPrevDeltaWeight(double prevDeltaWeight) {
		this.prevDeltaWeight = prevDeltaWeight;
	}

	public double getDeltaWeight() {
		return deltaWeight;
	}

	public void setDeltaWeight(double deltaWeight) {
		this.prevDeltaWeight = this.deltaWeight;
		this.deltaWeight = deltaWeight;
	}

	public Neuron getLeftNeuron() {
		return leftNeuron;
	}

	public void setLeftNeuron(Neuron leftNeuron) {
		this.leftNeuron = leftNeuron;
	}

	public Neuron getRightNeuron() {
		return rightNeuron;
	}

	public void setRightNeuron(Neuron rightNeuron) {
		this.rightNeuron = rightNeuron;
	}

	public int getId() {
		return id;
	}
}
