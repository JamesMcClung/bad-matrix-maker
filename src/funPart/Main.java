package funPart;

import java.util.Random;

public class Main {
	
	public static boolean doDebug = false;
	private static boolean doOutput = true;
	
	public static void main(String...args) {
//		Network2 network = new Network2("sumTesterBig", .1, ActivationFunction.tanh, 3, 50, 50, 50, 50, 50, 3);
		Network network = Network.deserialize("sumTesterBig");
		
		Random r = new Random();
		
		final int bound = 5;
		final int target = 8;
		final int nTrainings = 10;
		for (int i = 0; i < nTrainings; i++) {
			int a = r.nextInt(bound);
			int b = r.nextInt(bound);
			int c = r.nextInt(bound);
			
			int added = a + b + c >= target ? 1 : 0;
			
			double[] input = {a, b, c};
			double[] output = {added, 0, 0};
			
			network.giveInfo(input);
			network.giveExpected(output);
			
//			network.giveInfo(new double[] {0, 1, 1});
//			network.giveExpected(new double[] {0, 0, 0});
			
			if (doOutput) {
				printArr(input);
				printArr(network.getOutput());
				System.out.println();
			}
		}
		
		network.serialize();
	}
	
	private static void printArr(double[] arr) {
		for (double d : arr)
			System.out.print(d + ", ");
		System.out.println();
	}

}
