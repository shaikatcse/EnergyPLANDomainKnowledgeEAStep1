package reet.fbk.eu.jmetal.initialization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.StringTokenizer;

import reet.fbk.eu.OptimizeEnergyPLANWithStep.EnergyPLANProblemWithStep;
import sun.util.locale.StringTokenIterator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.problems.ZDT.ZDT1;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import Jama.Matrix;

/*
 * This class is responsible for doing Domain-knowledge initialization
 */
public class DKInitializationInJAVA {

	ArrayList<Solution> initialSolutions;
	Problem problem_;
	Boolean REFavorGenes[], ConFavorGene[], LFCFavorGenes[];

	Random rm;
	
	int populationSize, numberOfIndevPerCombination, numberOfRandomSolutions;
	double theta;
	int maxDistributionIndex;

	public DKInitializationInJAVA(Problem problem_, Boolean REFavorGenes[],
			Boolean ConFavorGene[], Boolean LFCFavorGenes[],
			int populationSize, double theta, int maxDistributionIndex,
			int numberOfIndevPerCombination, int numberOfRandomSolutions)  {
		this.REFavorGenes = REFavorGenes;
		this.ConFavorGene = ConFavorGene;
		this.LFCFavorGenes = LFCFavorGenes;
		this.populationSize = populationSize;
		this.theta = theta;
		this.maxDistributionIndex = maxDistributionIndex;
		this.numberOfIndevPerCombination = numberOfIndevPerCombination;
		this.numberOfRandomSolutions = numberOfRandomSolutions;

		this.problem_ = problem_;
		rm = new Random();
		initialSolutions = new ArrayList<Solution>();

	}

	public DKInitializationInJAVA(Problem problem_, Boolean REFavorGenes[],
			Boolean ConFavorGene[], int populationSize, double theta,
			int maxDistributionIndex, int numberOfIndevPerCombination,
			int numberOfRandomSolutions)
			{
		this.REFavorGenes = REFavorGenes;
		this.ConFavorGene = ConFavorGene;
		this.populationSize = populationSize;
		this.theta = theta;
		this.maxDistributionIndex = maxDistributionIndex;
		this.numberOfIndevPerCombination = numberOfIndevPerCombination;
		this.numberOfRandomSolutions = numberOfRandomSolutions;
		this.problem_ = problem_;
		rm = new Random();
		initialSolutions =  new ArrayList<Solution>();
	}

	
	public void generateInitialSolutions() {

	}

	public void generateInitialSolutionFavorRE() throws ClassNotFoundException,
			JMException {

		for (int no = 0; no < combinationsArray.size(); no++) {
			Integer[] aCombination = combinationsArray.get(no);
			aCombination = TransformArray(aCombination);
			for (int z = 0; z < numberOfIndevPerCombination; z++) {
				Solution sol = new Solution(problem_);
				for (int i = 0; i < problem_.getNumberOfVariables(); i++) {
					try {
						if (REFavorGenes[i] == true) {
							sol.getDecisionVariables()[i]
									.setValue(createGeneWithIncreasedCapacity(
											aCombination[i],
											problem_.getLowerLimit(i),
											problem_.getUpperLimit(i)));
						} else if (REFavorGenes[i] == false) {
							sol.getDecisionVariables()[i]
									.setValue(createGeneWithDecreasedCapacity(
											aCombination[i],
											problem_.getLowerLimit(i),
											problem_.getUpperLimit(i)));
						}
					} catch (NullPointerException e) {

						sol.getDecisionVariables()[i]
								.setValue(createGeneWithoutAnything(
										problem_.getLowerLimit(i),
										problem_.getUpperLimit(i)));
					}
				}
				initialSolutions.add(sol);
			}

		}
	}

	public void generateInitialSolutionFavorCon()
			throws ClassNotFoundException, JMException {

		for (int no = 0; no < combinationsArray.size(); no++) {
			Integer[] aCombination = combinationsArray.get(no);
			aCombination = TransformArray(aCombination);
			for (int z = 0; z < numberOfIndevPerCombination; z++) {
				Solution sol = new Solution(problem_);
				for (int i = 0; i < problem_.getNumberOfVariables(); i++) {
					try {
						if (ConFavorGene[i] == true) {
							sol.getDecisionVariables()[i]
									.setValue(createGeneWithIncreasedCapacity(
											aCombination[i],
											problem_.getLowerLimit(i),
											problem_.getUpperLimit(i)));
						} else if (ConFavorGene[i] == false) {
							sol.getDecisionVariables()[i]
									.setValue(createGeneWithDecreasedCapacity(
											aCombination[i],
											problem_.getLowerLimit(i),
											problem_.getUpperLimit(i)));
						}
					} catch (NullPointerException e) {
						sol.getDecisionVariables()[i]
								.setValue(createGeneWithoutAnything(
										problem_.getLowerLimit(i),
										problem_.getUpperLimit(i)));
					}
				}
				initialSolutions.add(sol);
			}
		}
	}

	public void generateRandomInitialSolutionWithoutFovor(
			int numberOfRandomIndividuals) throws ClassNotFoundException,
			JMException {
		for (int no = 0; no < numberOfRandomIndividuals; no++) {
			Solution sol = new Solution(problem_);
			for (int i = 0; i < problem_.getNumberOfVariables(); i++) {
				sol.getDecisionVariables()[i]
						.setValue(createGeneWithoutAnything(
								problem_.getLowerLimit(i),
								problem_.getUpperLimit(i)));
			}
			initialSolutions.add(sol);
		}
	}

	public Integer[] TransformArray(Integer[] aCombination) {
		Integer[] combination = new Integer[REFavorGenes.length];
		int j = 0;
		for (int i = 0; i < REFavorGenes.length; i++) {
			try {
				if (REFavorGenes[i] == true || REFavorGenes[i] == false) {
					combination[i] = aCombination[j++];
				}
			} catch (NullPointerException e) {
				combination[i] = -1;
			}

		}
		return combination;
	}

	/*
	 * 
	 */
	public double createGeneWithIncreasedCapacity(int n, double lowerBound,
			double upperBound) {
		double r = rm.nextDouble();
		double delta = Math.pow(r, (1 / ((double) n + 1)));
		double dcv = lowerBound + (upperBound - lowerBound) * delta;
		return dcv;
	}

	/*
	 * 
	 */
	public double createGeneWithDecreasedCapacity(int n, double lowerBound,
			double upperBound) {
		double r = rm.nextDouble();
		double delta = 1 - Math.pow((1 - r), (1 / ((double) n + 1)));
		double dcv = lowerBound + (upperBound - lowerBound) * delta;
		return dcv;
	}

	public double createGeneWithoutAnything(double lowerBound, double upperBound) {
		double r = rm.nextDouble();
		double delta = r;
		double dcv = lowerBound + (upperBound - lowerBound) * delta;
		return dcv;
	}

	
	public SolutionSet doDKInitialization()
			throws ClassNotFoundException, JMException
			{
		/*
		 * generate all combinations
		 */
		// int maxDistributionIndex = 3;

		initializeLengthAndCounter(maxDistributionIndex, REFavorGenes);
		nestedLoopOperation(counters, length, 0);

		/*
		 * generate two kinds of individuals by favoring RES and conventional
		 * energy
		 */
		generateInitialSolutionFavorRE();
		generateInitialSolutionFavorCon();

		// generate some random solution
		generateRandomInitialSolutionWithoutFovor(numberOfRandomSolutions);

		ArrayList <Solution> temporarySolutionSet = preProcessingIndividuals();

		ArrayList<Solution>  finalSolutionsList = calculateMaximumDiversityInJava(temporarySolutionSet);

		SolutionSet finalSolutionSet = postProcessingIndividuals(finalSolutionsList);
		
		return finalSolutionSet;
	}

	public ArrayList<Solution> preProcessingIndividuals()throws JMException {
		
		ArrayList<Solution> temporarySolutionSet = new ArrayList<Solution>();

		for (int i = 0; i < initialSolutions.size(); i++) {
			Solution s= initialSolutions.get(i);

			for (int j = 0; j < problem_.getNumberOfVariables(); j++) {
				// normalized to 0-1 range
				double oldMax = problem_.getUpperLimit(j);
				double oldMin = problem_.getLowerLimit(j);
				double newMax = 1.0;
				double newMin = 0.0;
				
				double oldRange = (oldMax - oldMin) ; 
				double newRange = (newMax - newMin); //normalized to 0 - 1  
				
				double oldValue = s.getDecisionVariables()[j].getValue();
				
				double newValue = (((oldValue - oldMin) * newRange) / oldRange) + newMin;
						
				s.getDecisionVariables()[j].setValue(newValue);
				
			}
			

			temporarySolutionSet.add(s);
		}
		
		return temporarySolutionSet;

	}
	
	public ArrayList<Solution> calculateMaximumDiversityInJava(ArrayList<Solution> temp) throws JMException{
		ArrayList<Solution> S= new ArrayList<Solution>();
		int index=rm.nextInt(temp.size());
		Solution randomSolution = temp.get(index);
		//Add to S
		S.add(randomSolution);
		//remove from temp
		temp.remove(index);
		for(int i=1;i<populationSize;i++){
			Matrix A = new Matrix(i,i);
			A=constructA(A,S);
			Matrix AInv=A.inverse();
			double maxContribution = Double.MIN_VALUE;
			int trackIndexToRemove=-1;
			
			for(int j=0;j<temp.size();j++){
				Matrix b = new Matrix(S.size(), 1);
				b=calculateDistanceFromSToTempJ(S, temp.get(j));
				Matrix d = AInv.times(b);
				double alpha = sumOfAllElementOfAMatrix(d);
				Matrix k;
				Matrix tmp = new Matrix(1,1); tmp.set(0,0, 1.0); 
				k=tmp.minus (b.transpose().times(d));
			
				double contribution = (alpha-1)*(alpha-1)/k.get(0, 0);
				if(contribution>maxContribution){
					maxContribution=contribution;
					trackIndexToRemove=j;
				}
				
			}
			System.out.println("i="+i +" index: "+trackIndexToRemove+" contribution: "+maxContribution);
			S.add(temp.get(trackIndexToRemove));
			temp.remove(trackIndexToRemove);
			
		}
		
		return S;
	}

	SolutionSet postProcessingIndividuals(ArrayList<Solution> S) throws JMException{

		SolutionSet solutionSetToReturn = new SolutionSet(populationSize);
		for(int i=0;i<populationSize;i++){
			solutionSetToReturn.add(S.get(i));
		}
		
		for (int j = 0; j < populationSize; j++) {
			for (int i = 0; i < problem_.getNumberOfVariables(); i++) {
				// get back to original limit
				double oldMax = problem_.getUpperLimit(i);
				double oldMin = problem_.getLowerLimit(i);
				double newMax = 1.0;
				double newMin = 0.0;
				
				double oldRange = (oldMax - oldMin) ; 
				double newRange = (newMax - newMin); //normalized to 0 - 1  
				
				double newValue = solutionSetToReturn.get(j).getDecisionVariables()[i].getValue();
				double oldValue = oldMin + ((newValue - newMin) * oldRange)/newRange;
				
				solutionSetToReturn.get(j).getDecisionVariables()[i].setValue(oldValue);
			}
			
		}
		
		return solutionSetToReturn;

	}
	
	double sumOfAllElementOfAMatrix(Matrix A){
		double sum=0.0;
		for(int i=0;i<A.getRowDimension();i++){
			for(int j=0; j<A.getColumnDimension();j++){
				sum+=A.get(i, j);
			}
		}
		return sum;
	}
	
	Matrix calculateDistanceFromSToTempJ(ArrayList<Solution> S, Solution TempJ) throws JMException{
		Matrix b=new Matrix(S.size(), 1);
		Distance d=new Distance();
		for(int i=0;i<S.size();i++){
			double dis = d.distanceBetweenSolutions(S.get(i), TempJ);
			double normalizedDistance = Math.exp(-theta * dis); 
			b.set(i,0,normalizedDistance);
		}
		return b;
	}

	Matrix constructA(Matrix A, ArrayList<Solution> S) throws JMException{
		Distance d=new Distance();
		for(int i=0;i<S.size();i++){
			for(int j=0;j<S.size();j++){
				double dis = d.distanceBetweenSolutions(S.get(i), S.get(j));
				double normalizedDistance = Math.exp(-theta * dis); 
				A.set(i,j,normalizedDistance);
			}
		}
		return A;
	}
	
	

	/*
	 * 486 code for generate all combinations
	 */

	int[] length;
	int[] counters;

	ArrayList<Integer[]> combinationsArray = new ArrayList<Integer[]>();

	/*
	 * n: is the maximum value of the combinations
	 */
	void initializeLengthAndCounter(int n, Boolean[] favorGenes) {
		combinationsArray.clear();

		int combinationSize = 0;
		for (int i = 0; i < favorGenes.length; i++) {
			try {
				if (favorGenes[i] == true || favorGenes[i] == false)
					combinationSize++;
			} catch (NullPointerException e) {
				continue;
			}
		}

		length = new int[combinationSize];
		counters = new int[combinationSize];

		for (int i = 0; i < combinationSize; i++) {
			length[i] = n;
			counters[i] = 0;
		}

	}

	void nestedLoopOperation(int[] counters, int[] length, int level) {
		if (level == counters.length)
			performOperation(counters);
		else {
			for (counters[level] = 0; counters[level] < length[level]; counters[level]++)
				nestedLoopOperation(counters, length, level + 1);
		}
	}

	void performOperation(int[] counters) {
		Integer[] counterAsString = new Integer[counters.length];
		for (int level = 0; level < counters.length; level++) {
			counterAsString[level] = counters[level];
			// if(level < counters.length-1) counterAsString = counterAsString +
			// ",";
		}
		//System.out.println(Arrays.toString(counterAsString));
		combinationsArray.add(counterAsString);
	}
	
	double calculateDiversityOfAPopulation(String fileName) throws JMException{
		
		//1. read from file
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while( (line = br.readLine()) != null){
				Solution s = new Solution(problem_);
				StringTokenizer  st = new StringTokenizer (line);
				int i=0;
				while (st.hasMoreElements()) {
					double value = Double.parseDouble((String) st.nextElement());
					s.getDecisionVariables()[i].setValue(value);
					i++;
				}
				initialSolutions.add(s);
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (JMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//2. preprocessing the data
		ArrayList <Solution> temporarySolutionSet = preProcessingIndividuals();
		
		//3. calculate diversity
		Matrix A = new Matrix(initialSolutions.size(), initialSolutions.size());
		A=constructA(A,temporarySolutionSet);
		Matrix AInv=A.inverse();
		double diversity = sumOfAllElementOfAMatrix(AInv);
		return diversity;
	}
	
	
	public double calculateDiversityOfAPopulation(SolutionSet S) throws JMException{
		
		initialSolutions.clear();
		
		
		//make a copy
		/*SolutionSet TempS = new SolutionSet(S.size());
		for(int i=0;i<S.size();i++){
			TempS.add(S.get(i));
		}*/
		
		for(int i=0;i<S.size();i++){
			Solution s = new Solution(S.get(i));
			initialSolutions.add(s);
		}
			
		
		//2. preprocessing the data
		ArrayList <Solution> temporarySolutionSet = preProcessingIndividuals();
		
		//3. calculate diversity
		Matrix A = new Matrix(initialSolutions.size(), initialSolutions.size());
		A=constructA(A,temporarySolutionSet);
		Matrix AInv=A.inverse();
		double diversity = sumOfAllElementOfAMatrix(AInv);
		return diversity;
	}

	/*
	 * generate all combination: End
	 */

	/*
	 * test purpose
	 */

	public static void main(String args[]) throws ClassNotFoundException,
			JMException {
		/*Problem problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution("Real");
		Boolean REFavorGenes[] = new Boolean[] { true, true, true, null, false,
				null, true };
		Boolean ConFavorGenes[] = new Boolean[] { false, false, false, null,
				true, null, false };

		
	
		DKInitializationInJAVA dkini = new DKInitializationInJAVA(problem, REFavorGenes,
				ConFavorGenes, 100, 6.0, 8, 5, 100);

		@SuppressWarnings("unused")
		long initTime = System.currentTimeMillis();
		SolutionSet p = dkini.doDKInitialization();
		long estimatedTime = System.currentTimeMillis() - initTime;
		System.out.println("Total execution time: " + estimatedTime + "ms");*/
		
		Problem problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution("Real");
		
		Boolean favorGenesforRE[] = { true, true, true, true, true };
		Boolean favorGenesforConventionalPP[] = { false, false, false,
				false, false };
		int populationSize = 100;

		
		/*File folder = new File("C:\\Users\\mahbub\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\InitializationResults\\InitIndividualWithSI\\WithoutRM\\DivMax\\Configuration 3\\");
		File[] listOfFiles = folder.listFiles();

		int i=0;
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        //System.out.println(file.getName());
		        DKInitializationInJAVA dkini = new DKInitializationInJAVA(problem,
						favorGenesforRE, favorGenesforConventionalPP,
						populationSize, 6.0, 5, 10, 0);
		        double diversity = dkini.calculateDiversityOfAPopulation(file.getAbsolutePath());
				System.out.println(i+" "+diversity);
				i++;
		    }
		}*/
		DKInitializationInJAVA dkini = new DKInitializationInJAVA(problem, REFavorGenes,
				favorGenesforConventionalPP, 100, 6.0, 5, 3, 0, proxy);
		@SuppressWarnings("unused")
		long initTime = System.currentTimeMillis();
		SolutionSet p = dkini.doDKInitialization();
		long estimatedTime = System.currentTimeMillis() - initTime;
		System.out.println("Total execution time: " + estimatedTime + "ms");
		
	}

}
