package reet.fbk.eu.jmetal.metaheuristics.moead;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateWeight {

	static int numberOfObjectives = 4;

	public GenerateWeight() {
		// TODO Auto-generated constructor stub
	}

	public static int findPositionOfMaxDistance(List<double[]> w1,
			List<double[]> w) {
		/*
		 * for testing
		 */
		/*for(int i=0;i<w.size();i++)
			System.out.println(w.get(i)[0]+ " "+ w.get(i)[1]);
		for(int i=0;i<w1.size();i++)
			System.out.println(w1.get(i)[0]+ " "+ w1.get(i)[1]);*/
		
		
		int position = -1;
		double max = Double.MIN_VALUE;
		// double totalDistance = 0.0;

		for (int z = 0; z < w1.size(); z++) {

			double min_dis = Double.MAX_VALUE;

			for (int i = 0; i < w.size(); i++) {
				//distance with W
				double distance = 0.0;
				for (int j = 0; j < numberOfObjectives; j++) {
					distance += Math.pow((w1.get(z)[j] - w.get(i)[j]), 2.0);
				}
				distance = Math.sqrt(distance);
				//distance calculation end
				//find minimum distance from a vector (v) of W1 to W 
				if (distance <= min_dis) {
					min_dis = distance;
				}
				
				//this is if we want to add all the distances
				// totalDistance = totalDistance + distance;
			}
			if (min_dis >= max) {
				position = z;
				max = min_dis;
			}
			//this is if we want to use totalDistance
			/*
			 * if(totalDistance>=max){ position = z; max=totalDistance; }
			 */

		}

		return position;
	}

	public static void main(String[] args) throws FileNotFoundException,
			UnsupportedEncodingException {
		// TODO Auto-generated method stub
		int populationSize = 200;
		List<double[]> W1 = new ArrayList<double[]>(500);
		List<double[]> W = new ArrayList<double[]>(200);

		Random rm = new Random();
		for (int i = 0; i < 5000; i++) {
			double a[] = new double[numberOfObjectives];
			double sum=0.0;
			for (int j = 0; j < numberOfObjectives; j++) {
				a[j] = rm.nextDouble(); 
				sum+=a[j];
			}
			//normalized weight vector (W)
			//sum of all the weights should be 1
			for (int j = 0; j < numberOfObjectives; j++) {
				a[j] = a[j]/sum; 
			}
			W1.add(a);

		}
		
		
		// initialize W
		for (int i = 0; i < numberOfObjectives; i++) {
			double a[] = new double[numberOfObjectives];
			W.add(a);
			for (int j = 0; j < numberOfObjectives; j++) {
				if (i == j)
					W.get(i)[j] = 1.0;
				else
					W.get(i)[j] = 0.0;
			}
		}

		// generate 200 weight vectors
		for (int i = 0; i < populationSize - numberOfObjectives; i++) {
			// find the position which one is removed from W1 and added to W
			int position = findPositionOfMaxDistance(W1, W);
			double[] a = W1.get(position);
			W1.remove(position);
			W.add(a);

		}

		//write to a file
		PrintWriter writer = new PrintWriter("weight", "UTF-8");
		for (int i = 0; i < W.size(); i++) {
			for (int j = 0; j < numberOfObjectives; j++) {
				writer.print("" + W.get(i)[j] + " ");
			}
			writer.println("");
		}
		writer.close();
	}

}
