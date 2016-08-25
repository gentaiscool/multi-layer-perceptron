package tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MNISTReader {
	
	public static String TRAIN_FILEPATH = "./dataset/mnist/train.csv";
	public static String TEST_FILEPATH = "./dataset/mnist/test.csv";
	
	private double[][] input = new double[42000][784];
	private double[][] expectedOutput = new double[42000][10];
	
	public static void main(String[] args) throws IOException {
		MNISTReader reader = new MNISTReader();
	}
	
	public MNISTReader() throws IOException {
		File file = new File(TRAIN_FILEPATH);
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String str;
		int count = 0;
		
		//skip the header
		str = reader.readLine();
		while((str=reader.readLine()) != null){
			String[] arr = str.split(",");
			int out = Integer.parseInt(arr[0]);
			for(int i=1; i<arr.length; i++){
				double num = Double.parseDouble(arr[i]);
				input[count][i-1] = num;
			}
			for(int i=0; i<10; i++)
				expectedOutput[count][i] = 0;
			expectedOutput[count][out] = 1;
			count++;
		}
	}

	public double[][] getInput() {
		return input;
	}

	public void setInput(double[][] input) {
		this.input = input;
	}

	public double[][] getExpectedOutput() {
		return expectedOutput;
	}

	public void setExpectedOutput(double[][] expectedOutput) {
		this.expectedOutput = expectedOutput;
	}
	
}
