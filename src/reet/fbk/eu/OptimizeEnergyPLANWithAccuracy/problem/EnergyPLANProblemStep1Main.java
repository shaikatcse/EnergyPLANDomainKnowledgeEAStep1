package reet.fbk.eu.OptimizeEnergyPLANWithAccuracy.problem;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import reet.fbk.eu.OptimizeEnergyPLANWithAccuracy.jmetal.metaheuristics.NSGAIIForDK;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.RandomGenerator;
//import reet.fbk.eu.OprimizeEnergyPLAN.jmetal.operators.mutation.MutationFactory;
import jmetal.operators.mutation.MutationFactory;


import java.util.logging.FileHandler;
import java.util.logging.Logger;

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
		
		long seed [] = {545782, 455875, 547945, 458478, 981354, 652262, 562366, 365652, 456545, 549235 };
		
		int numberOfRun=10;
		for (int i = 0; i < numberOfRun; i++) {
			
			
			PseudoRandom.setRandomGenerator(new RandomGenerator(seed[i]));

			
			indicators = null;

			problem = new EnergyPLANProblemStep1("Real");
			algorithm = new NSGAIIForDK(problem,seed[i], "SBX_Poly");
			// algorithm = new ssNSGAII(problem);

			indicators = new QualityIndicator(problem, "C:\\Users\\mahbub\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\Results\\truePf\\mergefun.pf") ;
			
			// Algorithm parameters
			algorithm.setInputParameter("populationSize", 100);
			algorithm.setInputParameter("maxEvaluations", 5000);

			// Mutation and Crossover for Real codification
			parameters = new HashMap();
			parameters.put("probability", 0.9);
			parameters.put("distributionIndex", 4.0);
			crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover",
					parameters);

			parameters = new HashMap();
			parameters.put("probability", 0.2);
			parameters.put("distributionIndex", 4.0);
			mutation = MutationFactory.getMutationOperator("PolynomialMutation",
						parameters);

			//mutation = MutationFactory.getMutationOperator("GeneralRealMutationForRes",
				//	parameters);

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
			
			
			// Execute the Algorithm
			long initTime = System.currentTimeMillis();
			SolutionSet population = algorithm.execute();
			long estimatedTime = System.currentTimeMillis() - initTime;

			// Result messages
			logger_.info("Total execution time: " + estimatedTime + "ms");
			logger_.info("Variables values have been writen to file VAR");
			population.printVariablesToFile("SBX_Poly\\VAR_SBX_Poly_seed_"+seed[i]);
			logger_.info("Objectives values have been writen to file FUN");
			population.printObjectivesToFile("SBX_Poly\\FUN_SBX_Poly_seed_"+seed[i]);
		}
	}
}
