import java.util.ArrayList;

public class Layer {
	private ArrayList<Neuron> neurons = new ArrayList();
	
	public Layer() {
	
	}

	public ArrayList<Neuron> getNeurons() {
		return neurons;
	}

	public void setNeurons(ArrayList<Neuron> neurons) {
		this.neurons = neurons;
	}
	
	public void addNeuron(Neuron neuron){
		this.neurons.add(neuron);
	}
}
