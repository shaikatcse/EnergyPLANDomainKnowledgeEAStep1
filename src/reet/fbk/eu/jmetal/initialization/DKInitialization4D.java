package reet.fbk.eu.jmetal.initialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import reet.fbk.eu.OptimizeEnergyPLANWithStep.EnergyPLANProblemWithStep;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.problems.ZDT.ZDT1;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import matlabcontrol.*;
import matlabcontrol.extensions.MatlabTypeConverter;

/*
 * This class is responsible for doing Domain-knowledge initialization
 */
public class DKInitialization4D {

	SolutionSet initialSolutions;
	Problem problem_;
	Boolean REFavorGenes[], ConFavorGenes[], LFCFavorGenes[], ESDFavorGenes[];

	Random rm;
	MatlabProxy proxy;

	int populationSize, numberOfIndevPerCombination,numberOfRandomSolutions;
	double theta;
	int maxDistributionIndex;

	public DKInitialization4D(Problem problem_, Boolean REFavorGenes[],
			Boolean ConFavorGene[], Boolean LFCFavorGenes[],Boolean ESDFavorGenes[],
			int populationSize, double theta, int maxDistributionIndex,
			int numberOfIndevPerCombination, int numberOfRandomSolutions, MatlabProxy proxy)
			throws MatlabInvocationException {
		this.REFavorGenes = REFavorGenes;
		this.ConFavorGenes = ConFavorGene;
		this.LFCFavorGenes = LFCFavorGenes;
		this.ESDFavorGenes = ESDFavorGenes;
		this.populationSize = populationSize;
		this.theta = theta;
		this.maxDistributionIndex = maxDistributionIndex;
		this.numberOfIndevPerCombination = numberOfIndevPerCombination;
		this.numberOfRandomSolutions=numberOfRandomSolutions;

		this.problem_ = problem_;
		rm = new Random();
		initialSolutions = new SolutionSet(10000);

		this.proxy = proxy;

		proxy.eval("addpath('C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/src/reet/fbk/eu/jmetal/initialization')");
	}

	public DKInitialization4D(Problem problem_, Boolean REFavorGenes[],
			Boolean ConFavorGene[], int populationSize, double theta,
			int maxDistributionIndex, int numberOfIndevPerCombination, int numberOfRandomSolutions, 
			MatlabProxy proxy) throws MatlabInvocationException {
		this.REFavorGenes = REFavorGenes;
		this.ConFavorGenes = ConFavorGene;
		this.populationSize = populationSize;
		this.theta = theta;
		this.maxDistributionIndex = maxDistributionIndex;
		this.numberOfIndevPerCombination = numberOfIndevPerCombination;
		this.numberOfRandomSolutions=numberOfRandomSolutions;
		this.problem_ = problem_;
		rm = new Random();
		initialSolutions = new SolutionSet(10000);
		this.proxy = proxy;
		proxy.eval("addpath('C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/src/reet/fbk/eu/jmetal/initialization')");
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
						if (ConFavorGenes[i] == true) {
							sol.getDecisionVariables()[i]
									.setValue(createGeneWithIncreasedCapacity(
											aCombination[i],
											problem_.getLowerLimit(i),
											problem_.getUpperLimit(i)));
						} else if (ConFavorGenes[i] == false) {
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

	public void generateInitialSolutionFavorLFC()
			throws ClassNotFoundException, JMException {

		for (int no = 0; no < combinationsArray.size(); no++) {
			Integer[] aCombination = combinationsArray.get(no);
			aCombination = TransformArray(aCombination);
			for (int z = 0; z < numberOfIndevPerCombination; z++) {
				Solution sol = new Solution(problem_);
				for (int i = 0; i < problem_.getNumberOfVariables(); i++) {
					try {
						if (LFCFavorGenes[i] == true) {
							sol.getDecisionVariables()[i]
									.setValue(createGeneWithIncreasedCapacity(
											aCombination[i],
											problem_.getLowerLimit(i),
											problem_.getUpperLimit(i)));
						} else if (LFCFavorGenes[i] == false) {
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
	
	public void generateInitialSolutionFavorESD()
			throws ClassNotFoundException, JMException {

		for (int no = 0; no < combinationsArray.size(); no++) {
			Integer[] aCombination = combinationsArray.get(no);
			aCombination = TransformArray(aCombination);
			for (int z = 0; z < numberOfIndevPerCombination; z++) {
				Solution sol = new Solution(problem_);
				for (int i = 0; i < problem_.getNumberOfVariables(); i++) {
					try {
						if (ESDFavorGenes[i] == true) {
							sol.getDecisionVariables()[i]
									.setValue(createGeneWithIncreasedCapacity(
											aCombination[i],
											problem_.getLowerLimit(i),
											problem_.getUpperLimit(i)));
						} else if (ESDFavorGenes[i] == false) {
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
	
	public void generateRandomInitialSolutionWithoutFovor(int numberOfRandomIndividuals)
			throws ClassNotFoundException, JMException {
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

	public SolutionSet doDKInitialization4D() throws ClassNotFoundException,
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
		generateInitialSolutionFavorLFC();
		generateInitialSolutionFavorESD();
		

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
				combinationSize++;
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

		DKInitialization4D dkini = new DKInitialization4D(problem, REFavorGenes,
				ConFavorGenes, 100, 0.00005, 4, 3, proxy);

		@SuppressWarnings("unused")
		SolutionSet p = dkini.doDKInitialization();
	}

}
