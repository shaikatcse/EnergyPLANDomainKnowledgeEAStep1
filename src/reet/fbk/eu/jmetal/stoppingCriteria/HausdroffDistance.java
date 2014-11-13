package reet.fbk.eu.jmetal.stoppingCriteria;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

class Solution {
	double objectives[];

	Solution() {
		objectives = new double[2];
	}

	Solution(double ob[]) {
		this.objectives = ob;

	}

	double getObjective(int position) {
		return objectives[position];
	}

	void setObjective(double value, int postion) {
		objectives[postion] = value;
	}

}

public class HausdroffDistance {

	/*
	 * ArrayList<Solution> X,Y,Z;
	 * 
	 * public HausdroffDistance(ArrayList<Solution> X, ArrayList<Solution> Y,
	 * ArrayList<Solution> Z) { this.X=X; this.Y=Y; this.Z=Z; }
	 */

	public static void main(String[] args) {
		ArrayList<Solution> X, Y, Z;
		X = new ArrayList<Solution>();
		Y = new ArrayList<Solution>();
		Z = new ArrayList<Solution>();

		try {
			BufferedReader brX = new BufferedReader(new FileReader(args[0]));
			String line;

			while ((line = brX.readLine()) != null) {
				// System.out.println(line);
				StringTokenizer st = new StringTokenizer(line);
				int i = 0;
				Solution solution = new Solution();
				while (st.hasMoreTokens()) {
					solution.setObjective(Double.parseDouble(st.nextToken()), i);
					i++;
				}
				X.add(solution);
			}

			BufferedReader brY = new BufferedReader(new FileReader(args[1]));
			line = "";

			while ((line = brY.readLine()) != null) {
				// System.out.println(line);
				StringTokenizer st = new StringTokenizer(line);
				int i = 0;
				Solution solution = new Solution();
				while (st.hasMoreTokens()) {
					solution.setObjective(Double.parseDouble(st.nextToken()), i);
					i++;
				}
				Y.add(solution);
			}

			BufferedReader brZ = new BufferedReader(new FileReader(args[2]));
			line = "";

			while ((line = brZ.readLine()) != null) {
				// System.out.println(line);
				StringTokenizer st = new StringTokenizer(line);
				int i = 0;
				Solution solution = new Solution();
				while (st.hasMoreTokens()) {
					solution.setObjective(Double.parseDouble(st.nextToken()), i);
					i++;
				}
				Z.add(solution);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		// HausdroffDistance hd = new HausdroffDistance(X, Y, Z);
		double[] maximumObjectivesValues = new double[2];
		maximumObjectivesValues = HausdroffDistance.findMaximumObjectiveValies(
				X, Y, Z);
		//X = HausdroffDistance.convertAbsoluteObjectiveValuesToRelativeValues(X,
			//	maximumObjectivesValues);
		//Y = HausdroffDistance.convertAbsoluteObjectiveValuesToRelativeValues(Y,
			//	maximumObjectivesValues);
		//Z = HausdroffDistance.convertAbsoluteObjectiveValuesToRelativeValues(Z,
				//maximumObjectivesValues);

		System.out.println("(Z,X)"+HausdroffDistance.calculateHausdroffDistance(Z, X));
		System.out.println("(Z,Y)"+HausdroffDistance.calculateHausdroffDistance(Z, Y));
		
		System.out.println("Average (Z,X)"+HausdroffDistance.calculateHausdroffDistance(Z, X)/X.size());
		System.out.println("Average (Z,Y)"+HausdroffDistance.calculateHausdroffDistance(Z, Y)/Y.size());
		
		System.out.println(HausdroffDistance.calculateHausdroffDistance(Z, Z));

	}

	public static double distanc(Solution s1, Solution s2) {
		double distance = 0;
		for (int i = 0; i < s1.objectives.length; i++) {
			distance += Math.pow((s1.getObjective(i) - s2.getObjective(i)), 2);
		}
		return Math.sqrt(distance);
	}

	public static double distance(Solution x, ArrayList<Solution> Y) {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < Y.size(); i++) {
			double tmpDis = distanc(x, Y.get(i));
			if (tmpDis < min) {
				min = tmpDis;
			}
		}
		return min;
	}

	public static double distance(ArrayList<Solution> X, ArrayList<Solution> Y) {
		double max = Double.MIN_VALUE;
		for (int i = 0; i < X.size(); i++) {
			double tmpDis = distance(X.get(i), Y);
			if (tmpDis > max) {
				max = tmpDis;
			}

		}
		return max;
	}

	public static double calculateHausdroffDistance(ArrayList<Solution> X,
			ArrayList<Solution> Y) {
		return Math.max(distance(X, Y), distance(Y, X));
	}

	public static double[] findMaximumObjectiveValies(ArrayList<Solution> X,
			ArrayList<Solution> Y, ArrayList<Solution> Z) {
		ArrayList<Solution> mergeArrarlist = new ArrayList<Solution>(X);
		mergeArrarlist.addAll(Y);
		mergeArrarlist.addAll(Z);

		double maximumObjectivesValues[] = new double[2];
		for (int i = 0; i < maximumObjectivesValues.length; i++) {
			maximumObjectivesValues[i] = Double.MIN_VALUE;
		}
		for (int i = 0; i < mergeArrarlist.size(); i++) {
			for (int j = 0; j < maximumObjectivesValues.length; j++) {
				if (mergeArrarlist.get(i).getObjective(j) > maximumObjectivesValues[j]) {
					maximumObjectivesValues[j] = mergeArrarlist.get(i)
							.getObjective(j);
				}
			}
		}
		return maximumObjectivesValues;

	}

	public static ArrayList<Solution> convertAbsoluteObjectiveValuesToRelativeValues(
			ArrayList<Solution> X, double[] maximumObjectivesValues) {
		for (int i = 0; i < X.size(); i++) {
			for (int j = 0; j < X.get(i).objectives.length; j++) {
				X.get(i).setObjective(
						X.get(i).getObjective(j) / maximumObjectivesValues[j],
						j);
			}
		}
		return X;

	}

}
