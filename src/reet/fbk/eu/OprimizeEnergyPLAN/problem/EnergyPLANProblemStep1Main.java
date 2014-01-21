package reet.fbk.eu.OprimizeEnergyPLAN.problem;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.nsgaII.NSGAII;

import jmetal.operators.selection.SelectionFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.RandomGenerator;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

import reet.fbk.eu.OprimizeEnergyPLAN.jmetal.operators.crossover.CrossoverFactory;
import reet.fbk.eu.OprimizeEnergyPLAN.jmetal.operators.mutation.MutationFactory;

public class EnergyPLANProblemStep1Main {

	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object

	public static void main(String[] args) throws JMException,
			SecurityException, IOException, ClassNotFoundException {

		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAII_main.log");
		logger_.addHandler(fileHandler_);

		Problem problem; // The problem to solve
		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		Operator selection; // Selection operator

		HashMap parameters; // Operator parameters

		QualityIndicator indicators; // Object to get quality indicators

		indicators = null;

		int noOfBinaryIntVariable = 4;
		int noOfRealVariable = 3;

		long seed = 2121979;
		PseudoRandom.setRandomGenerator(new RandomGenerator(seed));

		problem = new EnergyPLANProblemStep1("BinaryIntAndReal",
				noOfBinaryIntVariable, noOfRealVariable);

		indicators = new QualityIndicator(problem, "truePf");

		algorithm = new NSGAII(problem);
		// algorithm = new ssNSGAII(problem);

		// Algorithm parameters
		algorithm.setInputParameter("populationSize", 100);
		algorithm.setInputParameter("maxEvaluations", 500);

		// Mutation and Crossover for Binary codification
		parameters = new HashMap();
		parameters.put("singlePointCrossoverProbability", 0.9);
		parameters.put("SBXCrossoverProbability", 0.9);
		parameters.put("distributionIndex", 20.0);
		crossover = CrossoverFactory.getCrossoverOperator(
				"SinglePointAndSBXCrossoverForRes", parameters);

		parameters = new HashMap();
		parameters.put("bitFlipMutationProbability",
				1 / (double) noOfRealVariable);
		parameters.put("polynomialMutationProbability",
				1 / (double) noOfRealVariable);
		parameters.put("distributionIndex", 20.0);
		mutation = MutationFactory.getMutationOperator(
				"BitFlipAndPolynomialMutationForRes", parameters);

		// Selection Operator
		parameters = null;
		selection = SelectionFactory.getSelectionOperator("BinaryTournament2",
				parameters);

		// Add the operators to the algorithm
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);

		// Add the indicator object to the algorithm
		algorithm.setInputParameter("indicators", indicators);

		int numberOfRun = 1;
		for (int i = 0; i < numberOfRun; i++) {
			// Execute the Algorithm
			long initTime = System.currentTimeMillis();
			SolutionSet population = algorithm.execute();
			long estimatedTime = System.currentTimeMillis() - initTime;

			// Result messages
			logger_.info("Total execution time: " + estimatedTime + "ms");
			logger_.info("Variables values have been writen to file VAR");
			population.printVariablesToFile("run_" + i + "_VAR");
			logger_.info("Objectives values have been writen to file FUN");
			population.printObjectivesToFile("run_" + i + "_FUN");

			if (indicators != null) {
				logger_.info("Quality indicators");
				logger_.info("Hypervolume: "
						+ indicators.getHypervolume(population));
				logger_.info("GD         : " + indicators.getGD(population));
				logger_.info("IGD        : " + indicators.getIGD(population));
				logger_.info("Spread     : " + indicators.getSpread(population));
				logger_.info("Epsilon    : "
						+ indicators.getEpsilon(population));

				int evaluations = ((Integer) algorithm
						.getOutputParameter("evaluations")).intValue();
				logger_.info("Speed      : " + evaluations + " evaluations");
			} // if
		}
	}
}
