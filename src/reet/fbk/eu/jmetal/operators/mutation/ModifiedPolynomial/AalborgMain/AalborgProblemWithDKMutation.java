package reet.fbk.eu.jmetal.operators.mutation.ModifiedPolynomial.AalborgMain;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.metaheuristics.spea2.SPEA2;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.RandomGenerator;
//import reet.fbk.eu.jmetal.operators.mutation.MutationFactory;
import reet.fbk.eu.jmetal.operators.mutation.MutationFactory;

import java.util.logging.Logger;

import reet.fbk.eu.jmetal.initialization.EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution;
import reet.fbk.eu.jmetal.metaheuristics.nsgaII.NSGAIIForDK;
import reet.fbk.eu.jmetal.metaheuristics.spea2.SPEA2ForDK;

/**
 *  This class is main class to run Aalborg problem with domain-knowledge mutation
 * 
 */
class NSGAII_Nor_Run{
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object

	
	Problem problem; // The problem to solve
	Algorithm algorithm; // The algorithm to use
	Operator crossover; // Crossover operator
	Operator mutation; // Mutation operator
	Operator selection; // Selection operator

	HashMap parameters; // Operator parameters

	QualityIndicator indicators; // Object to get quality indicators
	
	public void run() throws SecurityException, IOException, ClassNotFoundException, JMException{
		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAII_main.log");
		logger_.addHandler(fileHandler_);

		//seed for nsgaii
		long seed [] = {545782, 455875, 547945, 458478, 981354, 652262, 562366, 365652, 456545, 549235 };
		
		int numberOfRun=10;
		for (int i = 0; i < numberOfRun; i++) {
			
			
			PseudoRandom.setRandomGenerator(new RandomGenerator(seed[i]));
			
			indicators = null;

			problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution("Real");
			//algorithm = new NSGAIITrackIndicators(problem, seed[i], "SBX_DKMutation");
			
			parameters = new HashMap();
						
			algorithm = new NSGAIIForDK(problem);
			// algorithm = new ssNSGAII(problem);

			//indicators = new QualityIndicator(problem, "C:\\Users\\Nusrat\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\Results\\truePf\\mergefun.pf") ;
			
			
			// Algorithm parameters
			algorithm.setInputParameter("populationSize", 100);
			algorithm.setInputParameter("maxEvaluations", 7000);
			
			
			// Mutation and Crossover for Real codification
			parameters = new HashMap();
			parameters.put("probability", 0.9);
			parameters.put("distributionIndex", 10.0);
			crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover",
					parameters);

			parameters = new HashMap();
			parameters.put("probability", 0.2);
			parameters.put("distributionIndex", 10.0);
			Boolean favorGenesforRE[] ={true, true, true, true, true};
			Boolean favorGenesforConventionalPP[] ={false, false, false, false, false};
			parameters.put("favorGenesforRE", favorGenesforRE);
			parameters.put("favorGenesForConventioanlPP", favorGenesforConventionalPP);
			parameters.put("maximum generation", (int) algorithm.getInputParameter("maxEvaluations")/(int) algorithm.getInputParameter("populationSize")-1);
			
			mutation = MutationFactory.getMutationOperator(
					"GeneralModifiedPolynomialMutationForEnergySystems", parameters);

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
			population.printVariablesToFile("mutationResults\\AalborgProblem\\NSGAIIWithDKMutation\\VAR_seed_"+seed[i]);
			logger_.info("Objectives values have been writen to file FUN");
			population.printObjectivesToFile("mutationResults\\AalborgProblem\\NSGAIIWithDKMutation\\FUN_seed_"+seed[i]);
		}
	
	}
}

class SPEA2_Nor_Run{
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object

	
	Problem problem; // The problem to solve
	Algorithm algorithm; // The algorithm to use
	Operator crossover; // Crossover operator
	Operator mutation; // Mutation operator
	Operator selection; // Selection operator

	HashMap parameters; // Operator parameters

	QualityIndicator indicators; // Object to get quality indicators
	
	public void run() throws SecurityException, IOException, ClassNotFoundException, JMException{
		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAII_main.log");
		logger_.addHandler(fileHandler_);

		//seed for spea2
		long seed [] = {154568, 148456, 447514, 458475, 274587, 712584, 975572, 585464, 467542, 686544 };
		
		int numberOfRun=10;
		for (int i = 0; i < numberOfRun; i++) {
			
			
			PseudoRandom.setRandomGenerator(new RandomGenerator(seed[i]));
			
			indicators = null;

			problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution("Real");
			//algorithm = new NSGAIITrackIndicators(problem, seed[i], "SBX_DKMutation");
			
			parameters = new HashMap();
						
			algorithm = new SPEA2ForDK(problem);
			// algorithm = new ssNSGAII(problem);

			//indicators = new QualityIndicator(problem, "C:\\Users\\Nusrat\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\Results\\truePf\\mergefun.pf") ;
			
			
			// Algorithm parameters
			algorithm.setInputParameter("populationSize", 100);
			algorithm.setInputParameter("maxEvaluations", 7000);
			 algorithm.setInputParameter("archiveSize", 100);
			
			// Mutation and Crossover for Real codification
			parameters = new HashMap();
			parameters.put("probability", 0.9);
			parameters.put("distributionIndex", 10.0);
			crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover",
					parameters);

			parameters = new HashMap();
			parameters.put("probability", 0.2);
			parameters.put("distributionIndex", 10.0);
			Boolean favorGenesforRE[] ={true, true, true, true, true};
			Boolean favorGenesforConventionalPP[] ={false, false, false, false, false};
			parameters.put("favorGenesforRE", favorGenesforRE);
			parameters.put("favorGenesForConventioanlPP", favorGenesforConventionalPP);
			parameters.put("maximum generation", (int) algorithm.getInputParameter("maxEvaluations")/(int) algorithm.getInputParameter("populationSize")-1);
			
			mutation = MutationFactory.getMutationOperator(
					"GeneralModifiedPolynomialMutationForEnergySystems", parameters);

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
			population.printVariablesToFile("mutationResults\\AalborgProblem\\SPEA2WithDKMutation\\VAR_seed_"+seed[i]);
			logger_.info("Objectives values have been writen to file FUN");
			population.printObjectivesToFile("mutationResults\\AalborgProblem\\SPEA2WithDKMutation\\FUN_seed_"+seed[i]);
		}
	
	}
}


public class AalborgProblemWithDKMutation {

	public static void main(String args[]) throws SecurityException, ClassNotFoundException, IOException, JMException{
		if(args[0].equals("NSGAII")){
			NSGAII_Nor_Run nsgaii = new NSGAII_Nor_Run();
			nsgaii.run();
		}else if(args[0].equals("SPEA2")){
			SPEA2_Nor_Run spea2 = new SPEA2_Nor_Run();
			spea2.run();
		}
	}
}
