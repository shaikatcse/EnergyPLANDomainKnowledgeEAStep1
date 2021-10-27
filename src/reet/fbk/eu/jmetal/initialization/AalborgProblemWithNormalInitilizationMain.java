package reet.fbk.eu.jmetal.initialization;

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
import jmetal.operators.mutation.MutationFactory;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * The class is main class where the class can execute algorithm from the command line
 * argument. The class only run the algorithm with normal initilization (random) technique. 
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

		//seed for nsgaii
		//long seed [] = {545782, 455875, 547945, 458478, 981354, 652262, 562366, 365652, 456545, 549235 };
		
		//2nd seed for nsgaii
		/*long seed [] = {
				161395,
				276644,
				309259,
				370995,
				468245,
				515856,
				668763,
				698156,
				756252,
				930462

				
		};*/
		
		//3rd seed for nsgaii
		/*long seed [] = {
				170339,
				259029,
				288470,
				353882,
				626495,
				683991,
				714786,
				989776,
				992177,
				999921
			
		};*/
		
		//all sedd merged togather		
		long seed[] = 
			{
				161395,
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
				515856,
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
						
			algorithm = new NSGAII(problem);
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
			parameters.put("probability", 0.2);
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
			population.printVariablesToFile("InitializationResults\\NSGAIIWithoutTrack\\VAR_seed_"+seed[i]);
			logger_.info("Objectives values have been writen to file FUN");
			population.printObjectivesToFile("InitializationResults\\NSGAIIWithoutTrack\\FUN_seed_"+seed[i]);
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

		//seed for spea2
		//long seed [] = {154568, 148456, 447514, 458475, 274587, 712584, 975572, 585464, 467542, 686544 };
		
		//2nd seed for spea2
		/*long seed [] = {
				212139,
				368817,
				418353,
				471922,
				500024,
				596970,
				784248,
				808035,
				953022,
				983349

		};*/
		
		//3rd seed for spea2
		/*long seed [] = {
				126113,
				289982,
				387808,
				430544,
				434057,
				622980,
				632376,
				833359,
				879899,
				888346

		};*/
		
		long seed[]={
				126113,
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
				467542,
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
						
			algorithm = new SPEA2(problem);
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
			parameters.put("probability", 0.2);
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
			population.printVariablesToFile("InitializationResults\\SPEA2WithoutTrack\\VAR_seed_"+seed[i]);
			logger_.info("Objectives values have been writen to file FUN");
			population.printObjectivesToFile("InitializationResults\\SPEA2WithoutTrack\\FUN_seed_"+seed[i]);
		}
	
	}
}


public class AalborgProblemWithNormalInitilizationMain {

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
