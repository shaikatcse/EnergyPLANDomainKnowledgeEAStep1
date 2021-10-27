package reet.fbk.eu.jmetal.stoppingCriteria;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

class Solution {
	double objectives[];

	Solution() {
		objectives = new double[30];
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
		ArrayList<Solution> Sol1, Sol2, Sol3, Sol4;
		String directoryName="C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/NSGAIISC/ZDT1/run0/";
		   String fileName = "VAR";
		 for(int i=2;i<=300;i++){
			// Sol4 = readFront(directoryName+fileName+(i-3));
			 //Sol3 = readFront(directoryName+fileName+(i-2));
			 
		
			 Sol2 = readFront(directoryName+fileName+(i-1)*1);
			 Sol1 = readFront(directoryName+fileName+(i)*1);
			 
			  //ArrayList<Solution> norSol4 = normalizedFront(Sol4, Sol1);
			 //ArrayList<Solution> norSol3 = normalizedFront(Sol3, Sol1);
			// ArrayList<Solution> norSol2 = normalizedFront(Sol2, Sol1);
			// ArrayList<Solution> norSol1 = normalizedFront(Sol1, Sol1);
			// printFronts(norSol1, 3);
			// System.out.println();
		//	 norSol1 = buildNewFront(norSol1, 2);
			// System.out.println();
			 
			 normalizedFront(Sol2, Sol1);
			//0 -> min value, 1->Xpositition, 2->YPosition
			 double  posAndDis[] = calculateHausdroffDistanceForPosition(Sol1, Sol2);
			 System.out.print(i + " " + posAndDis[0]+ " "+ posAndDis[1] + " " + posAndDis[2]+" ");
			// System.out.print(i+" "+calculateHausdroffDistance(Sol1, Sol2));
			 
			if(posAndDis[3] == 0){
			 System.out.print(Sol1.get((int) posAndDis[1]).getObjective(0) + " " + Sol1.get((int) posAndDis[1]).getObjective(1) + " ")  ;
			 System.out.print(Sol2.get((int) posAndDis[2]).getObjective(0) + " " + Sol2.get((int) posAndDis[2]).getObjective(1) + " ")  ;
			}
			else{
				System.out.print(Sol1.get((int) posAndDis[2]).getObjective(0) + " " + Sol1.get((int) posAndDis[2]).getObjective(1) + " ")  ;
				 System.out.print(Sol2.get((int) posAndDis[1]).getObjective(0) + " " + Sol2.get((int) posAndDis[1]).getObjective(1) + " ")  ;
			}
			 
			 System.out.println();
		 }
		
		

		// HausdroffDistance hd = new HausdroffDistance(X, Y, Z);
		double[] maximumObjectivesValues = new double[3];
		//maximumObjectivesValues = HausdroffDistance.findMaximumObjectiveValies(
			//	X, Y, Z);
		//X = HausdroffDistance.convertAbsoluteObjectiveValuesToRelativeValues(X,
			//	maximumObjectivesValues);
		//Y = HausdroffDistance.convertAbsoluteObjectiveValuesToRelativeValues(Y,
			//	maximumObjectivesValues);
		//Z = HausdroffDistance.convertAbsoluteObjectiveValuesToRelativeValues(Z,
				//maximumObjectivesValues);

		
	//	System.out.println("(Z,X)"+HausdroffDistance.calculateHausdroffDistance(norZ, norX));
	///	System.out.println("(Z,Y)"+HausdroffDistance.calculateHausdroffDistance(norZ, norY));
		
		//System.out.println("(X,Y)"+HausdroffDistance.calculateHausdroffDistance(X, Y));
		
		//System.out.println("(X,Z)"+HausdroffDistance.calculateHausdroffDistance(X, Z));
		//System.out.println("(Y,Z)"+HausdroffDistance.calculateHausdroffDistance(Y, Z));
		
	//	System.out.println("Average (Z,X)"+HausdroffDistance.calculateHausdroffDistance(norZ, norX)/Math.pow(norX.size(),1/2.0));
		//System.out.println("Average (Z,Y)"+HausdroffDistance.calculateHausdroffDistance(norZ, norY)/Math.pow(norY.size(),1/2.0));
		
		//System.out.println(HausdroffDistance.calculateHausdroffDistance(Z, Z));

	System.out.println("HD");
		for(int i=2;i<=500;i++){
			 Sol2 = readFront(directoryName+fileName+(i));
			 Sol1 = readFront(directoryName+fileName+(i-1));
			 System.out.println(i+ " "+ calculateHausdroffDistance(Sol2, Sol1));
		}
	}
	
	public static void printFronts(ArrayList<Solution> front, int numberOfObjectives){
		for(int i=0;i<front.size();i++){
			for(int j=0;j<numberOfObjectives;j++){
				System.out.print(front.get(i).getObjective(j)+" ");
			}
			System.out.println();
		}
	}
	
	public static ArrayList<Solution> buildNewFront (ArrayList<Solution> front, int numberOfObjectives ){
		double [] maximumValue;
		//GenerationalDistance gd = new GenerationalDistance();
		maximumValue = findMaximumObjectiveValies(front, numberOfObjectives);
		double scaleFactor = 1.5;
		for(int i=0;i<front.size();i++){
			for(int j=0; j<numberOfObjectives;j++){
				//front[i][j]=(front[i][j]-maximumValue[j])*scaleFactor+maximumValue[j];
				double value = (front.get(i).getObjective(j)-maximumValue[j])*scaleFactor+maximumValue[j];
				front.get(i).setObjective(value, j);
			}
			//front[i][0]=(front[i][0]-maximumValue[0])*scaleFactor+maximumValue[0];
			//front[i][1]=(front[i][1]-maximumValue[1])*scaleFactor+maximumValue[1];
		}
		/*for(int i=0;i<front.size();i++){
			for(int j=0; j<numberOfObjectives;j++){
				System.out.print(front.get(i).getObjective(j)+" ");
			}
			System.out.println("");
		}*/
		return front;
	}
	
	public static ArrayList<Solution> readFront (String fileName){
		ArrayList<Solution> Z= new ArrayList<Solution>();

		try{
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = "";
		

		while ((line = br.readLine()) != null) {
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
return Z;
		
	}

	public static double distanc(Solution s1, Solution s2) {
		double distance = 0;
		for (int i = 0; i < s1.objectives.length; i++) {
			distance += Math.pow((s1.getObjective(i) - s2.getObjective(i)), 2);
		}
		return Math.sqrt(distance);
	}

	public static double distance(Solution x, ArrayList<Solution> Y) {
		double min_ = Double.POSITIVE_INFINITY;
		for (int i = 0; i < Y.size(); i++) {
			double tmpDis = distanc(x, Y.get(i));
			if (tmpDis <= min_) {
				min_ = tmpDis;
			}
		}
		return min_;
	}

	public static double distance(ArrayList<Solution> X, ArrayList<Solution> Y) {
		double max_ = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < X.size(); i++) {
			double tmpDis = distance(X.get(i), Y);
			if (tmpDis >= max_) {
				max_ = tmpDis;
			}

		}
		return max_;
	}

	public static double calculateHausdroffDistance(ArrayList<Solution> X,
			ArrayList<Solution> Y) {
		return Math.max(distance(X, Y), distance(Y, X));
	}

	
	public static double [] distanceForPosition(Solution x, ArrayList<Solution> Y) {
		double min = Double.POSITIVE_INFINITY;
		double YPosition=-1;
		for (int i = 0; i < Y.size(); i++) {
			double tmpDis = distanc(x, Y.get(i));
			if (tmpDis <= min) {
				min = tmpDis;
				YPosition=i;
			}
		}
		double mulReturn[] = new double [2];
		//0 -> min value, 1->positition
		mulReturn[0] = min;
		mulReturn[1]=YPosition;
		return mulReturn;
	}

	public static double[] distanceForPosition(ArrayList<Solution> X, ArrayList<Solution> Y) {
		double max = Double.NEGATIVE_INFINITY;
		double XPosition=-1;
		double YPosition=-1;
		
		for (int i = 0; i < X.size(); i++) {
			double tmpDis[] = distanceForPosition(X.get(i), Y);
			//System.out.println(tmpDis[0]);
			if (tmpDis[0] >= max) {
				max = tmpDis[0];
				XPosition = i;
				YPosition = tmpDis[1];
			}

		}
		double mulReturn[] = new double [3];
		//0 -> min value, 1->Xpositition, 2->YPosition
		mulReturn[0] = max;
		mulReturn[1]=XPosition;
		mulReturn[2]=YPosition;
		return mulReturn;
	}
	
	
	public static double [] calculateHausdroffDistanceForPosition(ArrayList<Solution> X,
			ArrayList<Solution> Y) {
		//return Math.max(distance(X, Y), distance(Y, X)); 
		double XtoY[] = distanceForPosition(X, Y);
		double YtoX[] = distanceForPosition(Y, X);
		
		//0 -> min value, 1->Xpositition, 2->YPosition, 3-> 0 means XtoY or 1 means YtoX (if XtoY, then index 1 contains X position, if YtoX, then index 1 contains Y position)
		if(XtoY[0]>YtoX[0]){
			double [] returnValue = new double[4];
			for(int i=0;i<3;i++){
				returnValue[i]=XtoY[i];
			}
			returnValue[3] = 0;
			return returnValue;
			
		}else{
			double [] returnValue = new double[4];
			for(int i=0;i<3;i++){
				returnValue[i]=YtoX[i];
			}
			returnValue[3] = 1;
			return returnValue;
		}
			
		
	}
	
	public static double[] findMaximumObjectiveValies( ArrayList<Solution> X, ArrayList<Solution> Y, int numberOfObjectives) {
		ArrayList<Solution> Z = new ArrayList<>();
		double maximumObjectivesValues[] = new double[numberOfObjectives];
		for (int i = 0; i < maximumObjectivesValues.length; i++) {
			maximumObjectivesValues[i] = Double.MIN_VALUE;
		}
		Z.addAll(X);
		Z.addAll(Y);
		
		for (int i = 0; i < Z.size(); i++) {
			for (int j = 0; j < maximumObjectivesValues.length; j++) {
				if (Z.get(i).getObjective(j) > maximumObjectivesValues[j]) {
					maximumObjectivesValues[j] = Z.get(i)
							.getObjective(j);
				}
			}
		}
		return maximumObjectivesValues;

	}
	
	public static double[] findMinimumObjectiveValies( ArrayList<Solution> X, ArrayList<Solution> Y, int numberOfVariables) {
		
		ArrayList<Solution> Z = new ArrayList<>();
		
		double minimumObjectivesValues[] = new double[numberOfVariables];
		for (int i = 0; i < minimumObjectivesValues.length; i++) {
			minimumObjectivesValues[i] = Double.MAX_VALUE;
		}
		
		Z.addAll(X);
		Z.addAll(Y);
		
		for (int i = 0; i < Z.size(); i++) {
			for (int j = 0; j < minimumObjectivesValues.length; j++) {
				if (Z.get(i).getObjective(j) < minimumObjectivesValues[j]) {
					minimumObjectivesValues[j] = Z.get(i)
							.getObjective(j);
				}
			}
		}
		return minimumObjectivesValues;

	}
	
	public static void normalizedFront(ArrayList<Solution> X, ArrayList<Solution> Z){
		
		
		double [] minimumObjectiveValues = findMinimumObjectiveValies(X, Z, 30);
		double [] maximumObjectiveValues = findMaximumObjectiveValies(X, Z,30);
		
		for(int i=0;i<X.size();i++){
			double newValue=0;
			for(int j=0;j<X.get(i).objectives.length;j++){
				newValue= (X.get(i).getObjective(j) - minimumObjectiveValues[j])/ (maximumObjectiveValues[j]-minimumObjectiveValues[j]);
				X.get(i).setObjective(newValue, j);
			}
			
		}
		
		for(int i=0;i<Z.size();i++){
			double newValue=0;
			for(int j=0;j<Z.get(i).objectives.length;j++){
				newValue= (Z.get(i).getObjective(j) - minimumObjectiveValues[j])/ (maximumObjectiveValues[j]-minimumObjectiveValues[j]);
				Z.get(i).setObjective(newValue, j);
			}
			
		}
		
				
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
