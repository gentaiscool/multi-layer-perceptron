package core;
import java.util.ArrayList;
import java.util.HashMap;

public class Neuron {
	
	private ArrayList<Connection> inputConnection = new ArrayList<>();
	private HashMap<Integer, Connection> mapConnection = new HashMap<>();
	private Connection biasConnection;
	private double output = 0d;
	private double bias = 1;
	private int id;
	
	public static int counter = 0;
	
	public Neuron() {
		counter++;
		id = counter;
	}
	
	public double activation(double x){
		return sigmoid(x);
	}
	
	public double sigmoid(double x){
		return 1.0 / (1.0 +  (Math.exp(-x)));
	}
	
	public void calculateOutput(){
		// dot product of weight and input
		output = 0;
		for(int i=0; i<inputConnection.size(); i++){
			output += inputConnection.get(i).getWeight() * inputConnection.get(i).getLeftNeuron().getOutput();
		}
		output += bias * biasConnection.getWeight();
		output = activation(output);
	}
	
	public void addInputConnection(ArrayList<Neuron> neurons){
		for(int i=0; i<neurons.size(); i++){
			Connection con = new Connection(neurons.get(i), this);
			inputConnection.add(con);
			mapConnection.put(neurons.get(i).getId(), con);
		}
    }
	
    public void addBiasConnection(Neuron neuron){
        Connection con = new Connection(neuron,this);
        biasConnection = con;
        inputConnection.add(con);
    }
	
	public Connection getConnection(int neuronIndex){
        return mapConnection.get(neuronIndex);
    }
	
	public ArrayList<Connection> getInputConnection() {
		return inputConnection;
	}

	public void setInputConnection(ArrayList<Connection> inputConnection) {
		this.inputConnection = inputConnection;
	}

	public HashMap<Integer, Connection> getMapConnection() {
		return mapConnection;
	}

	public void setMapConnection(HashMap<Integer, Connection> mapConnection) {
		this.mapConnection = mapConnection;
	}

	public Connection getBiasConnection() {
		return biasConnection;
	}

	public void setBiasConnection(Connection biasConnection) {
		this.biasConnection = biasConnection;
	}

	public double getOutput() {
		return output;
	}

	public void setOutput(double output) {
		this.output = output;
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

	public int getId() {
		return id;
	}
}
