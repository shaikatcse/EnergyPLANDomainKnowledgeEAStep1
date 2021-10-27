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
	
	public void run(int populationSize, int maxEvaluations) throws SecurityException, IOException, ClassNotFoundException, JMException{
		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAII_main.log");
		logger_.addHandler(fileHandler_);

		//seed for nsgaii -> matched with first 10 runs
		//long seed [] = {545782, 455875, 547945, 458478, 981354, 652262, 562366, 365652, 456545, 549235 };
		
		//2nd seeds for NSGAII -> these seeds are not matched with NSGAII normal run (reet.fbk.eu.jmetal.initialization.AalborgProblemWithNormalInitilizationMain)
		/*long seed[]={
				146021,
				173857,
				224333,
				242605,
				501371,
				540548,
				573393,
				620498,
				770243,
				890691
	
		};*/
		
		//3rd seeds for NGSAII -> these seeds are not matched with NSGAII normal run (reet.fbk.eu.jmetal.initialization.AalborgProblemWithNormalInitilizationMain)
		/*long seed[]={
			145956,
			173786,
			215005,
			242173,
			339333,
			513474,
			540334,
			758588,
			794993,
			860075

		};*/
		
		//seed for 11 to 30 runs (matched with with NSGAII normal run (reet.fbk.eu.jmetal.initialization.AalborgProblemWithNormalInitilizationMain) 
		/*long seed [] = {
				161395,
				170339,
				259029,
				276644,
				288470,
				309259,
				353882,
				370995,
				468245,
				515856,
				626495,
				668763,
				683991,
				698156,
				714786,
				756252,
				930462,
				989776,
				992177,
				999921
		};*/
	
//seed for 1 to 30 runs (matched with with NSGAII normal run (reet.fbk.eu.jmetal.initialization.AalborgProblemWithNormalInitilizationMain) 
long seed [] = {	
	/*	161395,
		170339,
		259029,
		276644,
		288470,
		309259,
		353882,
		365652,
		370995,
		455875,

		456545,
		458478,
		468245,
		515856,*/
		545782,
		547945,
		549235,
		562366,
		626495,
		652262,
		
		668763,
		683991,
		698156,
		714786,
		756252,
		930462,
		981354,
		989776,
		992177,
		999921
};

		int numberOfRun=30;
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
			algorithm.setInputParameter("populationSize", populationSize);
			algorithm.setInputParameter("maxEvaluations", maxEvaluations);
			
			
			// Mutation and Crossover for Real codification
			parameters = new HashMap();
			parameters.put("probability", 0.9);
			parameters.put("distributionIndex", 10.0);
			crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover",
					parameters);

			parameters = new HashMap();
			parameters.put("probability", 0.1);
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
	
	public void run(int populationSize, int maxEvaluations) throws SecurityException, IOException, ClassNotFoundException, JMException{
		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAII_main.log");
		logger_.addHandler(fileHandler_);

		//seed for spea2 ->  these seeds are matched with SPEA2 normal run (reet.fbk.eu.jmetal.initialization.AalborgProblemWithNormalInitilizationMain)
		//long seed [] = {154568, 148456, 447514, 458475, 274587, 712584, 975572, 585464, 467542, 686544 };
		
		//2nd seeds for spea2 -> these seeds are not matched with SPEA2 normal run (reet.fbk.eu.jmetal.initialization.AalborgProblemWithNormalInitilizationMain)
		/*long seed [] ={
				130107,
				435086,
				795409,
				827955,
				858182,
				899819,
				910053,
				976258,
				986047,
				997488
	
		};*/
		
		//3rd seeds for spea2 -> these seeds are not matched with SPEA2 normal run (reet.fbk.eu.jmetal.initialization.AalborgProblemWithNormalInitilizationMain)
		/*long seed[]={
			202624,
			271732,
			364672,
			375391,
			412546,
			496312,
			594667,
			607708,
			728589,
			782118
		};*/
		
		//seeds for run 11 to run 30 -> (matched with with SPEA2 normal run (reet.fbk.eu.jmetal.initialization.AalborgProblemWithNormalInitilizationMain)
		/*long seed [] = {
				126113,
				212139,
				289982,
				368817,
				387808,
				418353,
				430544,
				434057,
				471922,
				500024,
				596970,
				622980,
				632376,
				784248,
				808035,
				833359,
				879899,
				888346,
				953022,
				983349


				};		*/
//seeds for run 1 to run 30 -> (matched with with SPEA2 normal run (reet.fbk.eu.jmetal.initialization.AalborgProblemWithNormalInitilizationMain)		
	long seed [] = {
		
		/*126113,
		148456,
		154568,
		212139,
		274587,
		289982,
		368817,
		387808,
		418353,
		430544,
		
		434057,
		447514,
		458475,
		467542,*/
		471922,
		500024,
		585464,
		596970,
		622980,
		632376,
		
		686544,
		712584,
		784248,
		808035,
		833359,
		879899,
		888346,
		953022,
		975572,
		983349
	};

		int numberOfRun=30;
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
			algorithm.setInputParameter("populationSize", populationSize);
			algorithm.setInputParameter("maxEvaluations", maxEvaluations);
			 algorithm.setInputParameter("archiveSize", populationSize);
			
			// Mutation and Crossover for Real codification
			parameters = new HashMap();
			parameters.put("probability", 0.9);
			parameters.put("distributionIndex", 10.0);
			crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover",
					parameters);

			parameters = new HashMap();
			parameters.put("probability", 0.1);
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
			nsgaii.run(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		}else if(args[0].equals("SPEA2")){
			SPEA2_Nor_Run spea2 = new SPEA2_Nor_Run();
			spea2.run(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		}
	}
}
