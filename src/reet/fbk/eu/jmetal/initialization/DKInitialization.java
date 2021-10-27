package reet.fbk.eu.jmetal.initialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import reet.fbk.eu.OptimizeEnergyPLANWithStep.EnergyPLANProblemWithStep;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.problems.ZDT.ZDT1;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import matlabcontrol.*;
import matlabcontrol.extensions.MatlabTypeConverter;
import Jama.Matrix;

/*
 * This class is responsible for doing Domain-knowledge initialization
 */
public class DKInitialization {

	SolutionSet initialSolutions;
	Problem problem_;
	Boolean REFavorGenes[], ConFavorGene[], LFCFavorGenes[];

	Random rm;
	MatlabProxy proxy;

	int populationSize, numberOfIndevPerCombination, numberOfRandomSolutions;
	double theta;
	int maxDistributionIndex;

	public DKInitialization(Problem problem_, Boolean REFavorGenes[],
			Boolean ConFavorGene[], Boolean LFCFavorGenes[],
			int populationSize, double theta, int maxDistributionIndex,
			int numberOfIndevPerCombination, int numberOfRandomSolutions,
			MatlabProxy proxy) throws MatlabInvocationException {
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
		initialSolutions = new SolutionSet(10000);

		this.proxy = proxy;

		proxy.eval("addpath('C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/src/reet/fbk/eu/jmetal/initialization')");
	}

	public DKInitialization(Problem problem_, Boolean REFavorGenes[],
			Boolean ConFavorGene[], int populationSize, double theta,
			int maxDistributionIndex, int numberOfIndevPerCombination,
			int numberOfRandomSolutions, MatlabProxy proxy)
			throws MatlabInvocationException {
		this.REFavorGenes = REFavorGenes;
		this.ConFavorGene = ConFavorGene;
		this.populationSize = populationSize;
		this.theta = theta;
		this.maxDistributionIndex = maxDistributionIndex;
		this.numberOfIndevPerCombination = numberOfIndevPerCombination;
		this.numberOfRandomSolutions = numberOfRandomSolutions;
		this.problem_ = problem_;
		rm = new Random();
		initialSolutions = new SolutionSet(10000);
		this.proxy = proxy;
		proxy.eval("addpath('C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/src/reet/fbk/eu/jmetal/initialization')");
	}

	public DKInitialization(Problem problem_, Boolean REFavorGenes[],
			Boolean ConFavorGene[], int populationSize, double theta,
			int maxDistributionIndex, int numberOfIndevPerCombination,
			int numberOfRandomSolutions){
		this.REFavorGenes = REFavorGenes;
		this.ConFavorGene = ConFavorGene;
		this.populationSize = populationSize;
		this.theta = theta;
		this.maxDistributionIndex = maxDistributionIndex;
		this.numberOfIndevPerCombination = numberOfIndevPerCombination;
		this.numberOfRandomSolutions = numberOfRandomSolutions;
		this.problem_ = problem_;
		rm = new Random();
		initialSolutions = new SolutionSet(10000);
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

	public SolutionSet doDKInitialization() throws ClassNotFoundException,
			JMException, MatlabInvocationException, MatlabConnectionException {

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

		sendPopulationToMatlab();
		double[][] population = runMatlabCode();
		/*
		 * Exit Matlab
		 */
		proxy.exit();

		/*
		 * convert double into population set
		 */

		SolutionSet finalPopulation = new SolutionSet(populationSize);
		for (int j = 0; j < populationSize; j++) {
			Solution s = new Solution(problem_);
			for (int i = 0; i < problem_.getNumberOfVariables(); i++) {
				s.getDecisionVariables()[i].setValue(population[j][i] * 1500.0);
			}
			finalPopulation.add(s);
		}

		return finalPopulation;
	}

	public SolutionSet doDKInitializationInJava()
			throws ClassNotFoundException, JMException,
			MatlabInvocationException, MatlabConnectionException {
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

		SolutionSet temporarySolutionSet = preProcessingIndividuals();

		SolutionSet finalSolutions = calculateMaximumDiversityInJava(initialSolutions);

		return finalSolutions;
	}

	public SolutionSet preProcessingIndividuals()throws JMException {
		
		SolutionSet temporarySolutionSet = new SolutionSet(initialSolutions.size());

		for (int i = 0; i < initialSolutions.size(); i++) {
			Solution s= initialSolutions.get(i);

			for (int j = 0; j < problem_.getNumberOfVariables(); j++) {
				// diveded by 1500, maximum upper limit of all the decision
				// variables
						s.getDecisionVariables()[j].setValue(s.getDecisionVariables()[j].getValue()/1500.0);
			}
			

			temporarySolutionSet.add(s);
		}
		
		return temporarySolutionSet;

	}
	
	public SolutionSet calculateMaximumDiversityInJava(SolutionSet temp) throws JMException{
		ArrayList<Solution> S= new ArrayList<Solution>();
		int index=rm.nextInt()%temp.size();
		Solution randomSolution = temp.get(index);
		//Add to S
		S.add(randomSolution);
		//remove from temp
		temp.remove(index);
		for(int i=1;i<populationSize;i++){
			Matrix AInv = new Matrix(i,i);
			AInv=calculateAInverse(AInv,S);
			double maxContribution = Double.MIN_VALUE;
			int trackIndexToRemove=-1;
			for(int j=0;j<temp.size();j++){
				Matrix b = new Matrix(S.size(), 1);
				b=calculateDistanceFromSToTempJ(S, temp.get(j));
				Matrix d = AInv.times(b);
				double alpha = sumOfAllElementOfAMatrix(d);
				Matrix k=(b.transpose().times(d)).times(-1);
				double contribution = (alpha-1)*(alpha-0)/k.get(0, 0);
				if(contribution>maxContribution){
					maxContribution=contribution;
					trackIndexToRemove=j;
				}
				
			}
			S.add(temp.get(trackIndexToRemove));
			temp.remove(trackIndexToRemove);
			
		}
		
		SolutionSet solutionSetToReturn = new SolutionSet(populationSize);
		for(int i=0;i<populationSize;i++){
			solutionSetToReturn.add(S.get(i));
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
			b.set(0,i,normalizedDistance);
		}
		return b;
	}

	Matrix calculateAInverse(Matrix A, ArrayList<Solution> S) throws JMException{
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
	
	public void sendPopulationToMatlab() throws MatlabInvocationException,
			JMException {
		proxy.eval("clear all");
		String aMatrix;

		aMatrix = "[";
		for (int i = 0; i < initialSolutions.size(); i++) {

			for (int j = 0; j < problem_.getNumberOfVariables(); j++) {
				// diveded by 1500, maximum upper limit of all the decision
				// variables
				aMatrix = aMatrix
						+ initialSolutions.get(i).getDecisionVariables()[j]
								.getValue() / 1500.0 + " ";
			}
			aMatrix = aMatrix + ";";

		}
		aMatrix = aMatrix + "]";
		proxy.eval("indvMatrix=" + aMatrix + ";");

	}

	public double[][] runMatlabCode() throws MatlabInvocationException {
		proxy.eval("numberOfFinalIndv=" + populationSize + ";");
		proxy.eval("theta=" + theta + ";");

		proxy.eval("findIndvMaxDiversity");
		double[][] population = new double[100][problem_.getNumberOfVariables()];
		MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
		population = processor.getNumericArray("indMatrixTemp")
				.getRealArray2D();
		return population;

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
		System.out.println(Arrays.toString(counterAsString));
		combinationsArray.add(counterAsString);
	}

	/*
	 * generate all combination: End
	 */

	/*
	 * test purpose
	 */

	public static void main(String args[]) throws ClassNotFoundException,
			JMException, MatlabConnectionException, MatlabInvocationException {
		Problem problem = new EnergyPLANProblemWithStep("Real");
		Boolean REFavorGenes[] = new Boolean[] { true, true, true, null, false,
				null, true };
		Boolean ConFavorGenes[] = new Boolean[] { false, false, false, null,
				true, null, false };

		

		
		MatlabProxyFactory factory;
		MatlabProxy proxy;

		factory = new MatlabProxyFactory();
		proxy = factory.getProxy();
		
		
		DKInitialization dkini = new DKInitialization(problem, REFavorGenes,
				ConFavorGenes, 100, 6.0, 4, 3, 0, proxy);
		

		
		@SuppressWarnings("unused")
		long initTime = System.currentTimeMillis();
		SolutionSet p = dkini.doDKInitialization();
		long estimatedTime = System.currentTimeMillis() - initTime;
		System.out.println("Total execution time: " + estimatedTime + "ms");
	}

}
