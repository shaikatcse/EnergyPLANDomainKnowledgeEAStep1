package reet.fbk.eu.jmetal.initialization;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import reet.fbk.eu.OptimizeEnergyPLANAalborg.problem.EnergyPLANProblemAalborg;
import reet.fbk.eu.OptimizeEnergyPLANAalborg.problem.EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution;
import reet.fbk.eu.jmetal.metaheuristics.nsgaII.NSGAIIForDK;
import reet.fbk.eu.jmetal.metaheuristics.nsgaII.NSGAIITrackIndicators;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.RandomGenerator;
//import reet.fbk.eu.jmetal.operators.mutation.MutationFactory;
import jmetal.operators.mutation.MutationFactory;






import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class AalborgProblemWithNormalInitilizationMain {

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
		
		long seed [] = {458625, 412568, 125445, 144412, 255112, 144554, 455112, 645524, 885545, 584125 };
		
		int numberOfRun=10;
		for (int i = 0; i < numberOfRun; i++) {
			
			
			PseudoRandom.setRandomGenerator(new RandomGenerator(seed[i]));
			
			indicators = null;

			problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution("Real");
			//algorithm = new NSGAIITrackIndicators(problem, seed[i], "SBX_DKMutation");
			algorithm = new NSGAIITrackIndicators(problem);
			// algorithm = new ssNSGAII(problem);

			//indicators = new QualityIndicator(problem, "C:\\Users\\Nusrat\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\Results\\truePf\\mergefun.pf") ;
			
			
			// Algorithm parameters
			algorithm.setInputParameter("populationSize", 10);
			algorithm.setInputParameter("maxEvaluations", 50);

			// Mutation and Crossover for Real codification
			parameters = new HashMap();
			parameters.put("probability", 0.9);
			parameters.put("distributionIndex", 10.0);
			crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover",
					parameters);

			parameters = new HashMap();
			parameters.put("probability", 0.1);
			parameters.put("distributionIndex", 10.0);
			mutation = MutationFactory.getMutationOperator("PolynomialMutation",
						parameters);

			//mutation = MutationFactory.getMutationOperator("GeneralRealMutationForRes",
				//.	parameters);

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
			population.printVariablesToFile("InitializationResults\\NSGAIIWithoutTrack\\VAR_SBX_DKMutation_seed_"+seed[i]);
			logger_.info("Objectives values have been writen to file FUN");
			population.printObjectivesToFile("InitializationResults\\NSGAIIWithoutTrack\\FUN_SBX_DKMutation_seed_"+seed[i]);
		}
	}
}
