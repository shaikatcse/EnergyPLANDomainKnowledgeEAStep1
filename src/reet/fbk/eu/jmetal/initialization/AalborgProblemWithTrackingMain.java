package reet.fbk.eu.jmetal.initialization;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import reet.fbk.eu.jmetal.initialization.metaheuristics.nsgaII.NSGAIIForSI;
import reet.fbk.eu.jmetal.initialization.metaheuristics.nsgaII.NSGAIITrackIndicators;
import reet.fbk.eu.jmetal.initialization.metaheuristics.spea2.SPEA2ForSI;
import reet.fbk.eu.jmetal.initialization.metaheuristics.spea2.SPEA2TrackIndicators;
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
 * argument. The class run different algorithm to track different indicators for each
 * generation/.  
 * 
 */

class NSGAII_RunWithTracking {

	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object
	
	long NSGAII_seeds[] = { 545782, 455875, 547945, 458478, 981354, 652262,
			562366, 365652, 456545, 549235 };

	Problem problem; // The problem to solve
	Algorithm algorithm; // The algorithm to use
	Operator crossover; // Crossover operator
	Operator mutation; // Mutation operator
	Operator selection; // Selection operator

	HashMap parameters; // Operator parameters

	QualityIndicator indicators; // Object to get quality indicators

	String path = "InitializationResults\\WithTrack\\NSGAII";

	public void run() throws SecurityException, IOException {

		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAII_main.log");
		logger_.addHandler(fileHandler_);
		
		for (int i = 0; i < 10; i++) {

			PseudoRandom
					.setRandomGenerator(new RandomGenerator(NSGAII_seeds[i]));

			problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolutionForThread(
					"Real", path);

			indicators = new QualityIndicator(problem,
					".\\InitializationResults\\ParetoFront\\mergeFUN.pf");
			// algorithm = new NSGAIITrackIndicators(problem, seed[i],
			// "SBX_DKMutation");

			parameters = new HashMap();

			algorithm = new NSGAIITrackIndicators(problem, NSGAII_seeds[i], path);
			// algorithm = new ssNSGAII(problem);

			// indicators = new QualityIndicator(problem,
			// "C:\\Users\\Nusrat\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\Results\\truePf\\mergefun.pf")
			// ;

			// Algorithm parameters
			algorithm.setInputParameter("populationSize", 100);
			algorithm.setInputParameter("maxEvaluations", 7000);
			algorithm.setInputParameter("indicators", indicators);

			try {

				// Mutation and Crossover for Real codification
				parameters = new HashMap();
				parameters.put("probability", 0.9);
				parameters.put("distributionIndex", 10.0);
				crossover = CrossoverFactory.getCrossoverOperator(
						"SBXCrossover", parameters);

				parameters = new HashMap();
				parameters.put("probability", 0.1);
				parameters.put("distributionIndex", 10.0);
				mutation = MutationFactory.getMutationOperator(
						"PolynomialMutation", parameters);

				// mutation =
				// MutationFactory.getMutationOperator("GeneralRealMutationForRes",
				// . parameters);

				// Selection Operator
				parameters = null;
				selection = SelectionFactory.getSelectionOperator(
						"BinaryTournament2", parameters);

				// Add the operators to the algorithm
				algorithm.addOperator("crossover", crossover);
				algorithm.addOperator("mutation", mutation);
				algorithm.addOperator("selection", selection);

		
				// Execute the Algorithm
				long initTime = System.currentTimeMillis();
				SolutionSet population = algorithm.execute();
				long estimatedTime = System.currentTimeMillis() - initTime;

				// Result messages
				logger_.info("Total execution time: " + estimatedTime + "ms");
				logger_.info("Variables values have been writen to file VAR");
				population.printVariablesToFile(path + "\\VAR_seed_"
						+ NSGAII_seeds[i]);
				logger_.info("Objectives values have been writen to file FUN");
				population.printObjectivesToFile(path + "\\FUN_seed_"
						+ NSGAII_seeds[i]);
			} catch (JMException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}

class NSGAII_SI_RunWithTracking  {

	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object
	
	long NSGAII_SI_seeds[] = { 343434, 551254, 145845, 555541, 551641, 625882,
			985312, 458745, 228424, 7811554 };

	Problem problem; // The problem to solve
	Algorithm algorithm; // The algorithm to use
	Operator crossover; // Crossover operator
	Operator mutation; // Mutation operator
	Operator selection; // Selection operator

	HashMap parameters; // Operator parameters

	QualityIndicator indicators; // Object to get quality indicators

	String path = "InitializationResults\\WithTrack\\NSGAII_SI";

	public void run() throws SecurityException, IOException {

		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAII_SI_main.log");
		logger_.addHandler(fileHandler_);
		
		
		File folder = new File("InitializationResults/InitIndividualWithSI");
		File[] listOfFiles = folder.listFiles(new FilenameFilter() {
		    @Override
		    public boolean accept(File dir, String name) {
		        return name.startsWith("Init");
		    }
		});
		
		for (int i = 0; i < NSGAII_SI_seeds.length; i++) {

			PseudoRandom.setRandomGenerator(new RandomGenerator(
					NSGAII_SI_seeds[i]));

			problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolutionForThread(
					"Real", path);

			indicators = new QualityIndicator(problem,
					".\\InitializationResults\\ParetoFront\\mergeFUN.pf");
			// algorithm = new NSGAIITrackIndicators(problem, seed[i],
			// "SBX_DKMutation");

			Boolean favorGenesforRE[] ={true, true, true, true, true};
			Boolean favorGenesforConventionalPP[] ={false, false, false, false, false};
			parameters = new HashMap();
			parameters.put("favorGenesforRE", favorGenesforRE);
			parameters.put("favorGenesForCon", favorGenesforConventionalPP);
			parameters.put("InitialPopulationFile", listOfFiles[i].getAbsolutePath());
			
	
			algorithm = new NSGAIIForSI(problem, NSGAII_SI_seeds[i], path, parameters);
			// algorithm = new ssNSGAII(problem);

			// indicators = new QualityIndicator(problem,
			// "C:\\Users\\Nusrat\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\Results\\truePf\\mergefun.pf")
			// ;

			// Algorithm parameters
			algorithm.setInputParameter("populationSize", 100);
			algorithm.setInputParameter("maxEvaluations", 7000);
			algorithm.setInputParameter("indicators", indicators);

			try {

				// Mutation and Crossover for Real codification
				parameters = new HashMap();
				parameters.put("probability", 0.9);
				parameters.put("distributionIndex", 10.0);
				crossover = CrossoverFactory.getCrossoverOperator(
						"SBXCrossover", parameters);

				parameters = new HashMap();
				parameters.put("probability", 0.1);
				parameters.put("distributionIndex", 10.0);
				mutation = MutationFactory.getMutationOperator(
						"PolynomialMutation", parameters);

				// mutation =
				// MutationFactory.getMutationOperator("GeneralRealMutationForRes",
				// . parameters);

				// Selection Operator
				parameters = null;
				selection = SelectionFactory.getSelectionOperator(
						"BinaryTournament2", parameters);

				// Add the operators to the algorithm
				algorithm.addOperator("crossover", crossover);
				algorithm.addOperator("mutation", mutation);
				algorithm.addOperator("selection", selection);

				
				// Execute the Algorithm
				long initTime = System.currentTimeMillis();
				SolutionSet population = algorithm.execute();
				long estimatedTime = System.currentTimeMillis() - initTime;

				// Result messages
				logger_.info("Total execution time: " + estimatedTime + "ms");
				logger_.info("Variables values have been writen to file VAR");
				population.printVariablesToFile(path + "\\VAR_seed_"
						+ NSGAII_SI_seeds[i]);
				logger_.info("Objectives values have been writen to file FUN");
				population.printObjectivesToFile(path + "\\FUN_seed_"
						+ NSGAII_SI_seeds[i]);
			} catch (JMException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}

class SPEA2_RunWithTracking {
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object
	
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

	Problem problem; // The problem to solve
	Algorithm algorithm; // The algorithm to use
	Operator crossover; // Crossover operator
	Operator mutation; // Mutation operator
	Operator selection; // Selection operator

	
	HashMap parameters; // Operator parameters

	QualityIndicator indicators; // Object to get quality indicators

	String path = "InitializationResults\\WithTrack\\SPEA2";

	public void run(int populationSize, int maxEvaluations) throws SecurityException, IOException, ClassNotFoundException, JMException{
		
		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("SPEA2_main.log");
		logger_.addHandler(fileHandler_);
		
		int numberOfRun = 30;
		for (int i = 0; i < numberOfRun; i++) {

			PseudoRandom
					.setRandomGenerator(new RandomGenerator(seed[i]));

			problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution("Real");
			
			indicators = new QualityIndicator(problem,
					".\\InitializationResults\\ParetoFront\\WithoutRM\\With_mutation_pr_0.1\\mergeFUN.pf");
			// algorithm = new NSGAIITrackIndicators(problem, seed[i],
			// "SBX_DKMutation");

			parameters = new HashMap();

			algorithm = new SPEA2TrackIndicators(problem, seed[i], path);

			// Algorithm parameters
			// Algorithm parameters
			algorithm.setInputParameter("populationSize", populationSize);
			algorithm.setInputParameter("maxEvaluations", maxEvaluations);
			 algorithm.setInputParameter("archiveSize", populationSize);
			 algorithm.setInputParameter("indicators", indicators);

			try {

				// Mutation and Crossover for Real codification
				parameters = new HashMap();
				parameters.put("probability", 0.9);
				parameters.put("distributionIndex", 10.0);
				crossover = CrossoverFactory.getCrossoverOperator(
						"SBXCrossover", parameters);

				parameters = new HashMap();
				parameters.put("probability", 0.1);
				parameters.put("distributionIndex", 10.0);
				mutation = MutationFactory.getMutationOperator(
						"PolynomialMutation", parameters);

				// mutation =
				// MutationFactory.getMutationOperator("GeneralRealMutationForRes",
				// . parameters);

				// Selection Operator
				parameters = null;
				selection = SelectionFactory.getSelectionOperator(
						"BinaryTournament2", parameters);

				// Add the operators to the algorithm
				algorithm.addOperator("crossover", crossover);
				algorithm.addOperator("mutation", mutation);
				algorithm.addOperator("selection", selection);

				
				// Execute the Algorithm
				long initTime = System.currentTimeMillis();
				SolutionSet population = algorithm.execute();
				long estimatedTime = System.currentTimeMillis() - initTime;

				// Result messages
				logger_.info("Total execution time: " + estimatedTime + "ms");
				logger_.info("Variables values have been writen to file VAR");
				population.printVariablesToFile(path + "\\VAR_seed_"
						+ seed[i]);
				logger_.info("Objectives values have been writen to file FUN");
				population.printObjectivesToFile(path + "\\FUN_seed_"
						+ seed[i]);
			} catch (JMException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}

class SPEA2_SI_RunWithTracking  {
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object
	
	Problem problem; // The problem to solve
	Algorithm algorithm; // The algorithm to use
	Operator crossover; // Crossover operator
	Operator mutation; // Mutation operator
	Operator selection; // Selection operator

	HashMap parameters; // Operator parameters

	QualityIndicator indicators; // Object to get quality indicators

	String path = "InitializationResults\\WithTrack\\SPEA2_SI";
	
	
	public void run(int populationSize, int maxEvaluations) throws SecurityException, IOException, JMException, ClassNotFoundException{
		
		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("SPEA2_SI_main.log");
		logger_.addHandler(fileHandler_);
		
		
	int numberOfRun=30;
	
	long seed[]={
			125386,
			145295,
			158570,
			207621,
			227297,
			243858,
			354528,
			356090,
			378578,
			384873,
			
			425374,
			437331,
			461628,
			538947,
			544774,
			631545,
			637384,
			647647,
			647848,
			712308,
			
			733779,
			764889,
			857578,
			866208,
			867882,
			878532,
			891747,
			917083,
			919749,
			957363

	};
	
	/*File folder = new File("InitializationResults/InitIndividualWithSI");
		File[] listOfFiles = folder.listFiles(new FilenameFilter() {
		    @Override
		    public boolean accept(File dir, String name) {
		        return name.startsWith("Init");
		    }
		});*/
		
		File[] listOfFiles = new File[numberOfRun] ;
		
		
		
		/*long initSeed[] = { 144759,
				271439,
				445964,
				494817,
				530563,
				724859,
				746153,
				747584,
				866309,
				938562};
*/
		/*long initSeed[] = {
				172480,
				242648,
				268602,
				272313,
				412455,
				441996,
				633160,
				814562,
				822805,
				843288
	
		};*/

		long initSeed[]={
				125742,
				144759,
				172480,
				242648,
				245454,
				268602,
				271439,
				272313,
				412455,
				441996,
				
				445964,
				447551,
				455411,
				455885,
				494817,
				530563,
				545454,
				565656,
				633160,
				714451,
				
				724859,
				746153,
				747584,
				752453,
				814562,
				822805,
				843288,
				844545,
				866309,
				938562
		
		};

		
		for(int i=0;i<numberOfRun;i++){
			listOfFiles[i] = new File("InitializationResults/InitIndividualWithSI/WithoutRM/InitIndv_seed_"+initSeed[i]);
			
		}

	
				
		for (int i = 0; i < numberOfRun; i++) {

			PseudoRandom.setRandomGenerator(new RandomGenerator(seed[i]));

			problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution("Real");
			indicators = new QualityIndicator(problem,
					".\\InitializationResults\\ParetoFront\\WithoutRM\\With_mutation_pr_0.1\\mergeFUN.pf");
			
			// algorithm = new NSGAIITrackIndicators(problem, seed[i],
			// "SBX_DKMutation");

			Boolean favorGenesforRE[] ={true, true, true, true, true};
			Boolean favorGenesforConventionalPP[] ={false, false, false, false, false};
			parameters = new HashMap();
			parameters.put("favorGenesforRE", favorGenesforRE);
			parameters.put("favorGenesForCon", favorGenesforConventionalPP);
			parameters.put("InitialPopulationFile", listOfFiles[i].getAbsolutePath());
			
			algorithm = new SPEA2ForSI(problem, seed[i], path, parameters);

			// Algorithm parameters
			// Algorithm parameters
			algorithm.setInputParameter("populationSize", populationSize);
			algorithm.setInputParameter("maxEvaluations", maxEvaluations);
			//for spea2
			algorithm.setInputParameter("archiveSize", populationSize);
			algorithm.setInputParameter("indicators", indicators);

			try {

				// Mutation and Crossover for Real codification
				parameters = new HashMap();
				parameters.put("probability", 0.9);
				parameters.put("distributionIndex", 10.0);
				crossover = CrossoverFactory.getCrossoverOperator(
						"SBXCrossover", parameters);

				parameters = new HashMap();
				parameters.put("probability", 0.1);
				parameters.put("distributionIndex", 10.0);
				mutation = MutationFactory.getMutationOperator(
						"PolynomialMutation", parameters);

				// mutation =
				// MutationFactory.getMutationOperator("GeneralRealMutationForRes",
				// . parameters);

				// Selection Operator
				parameters = null;
				selection = SelectionFactory.getSelectionOperator(
						"BinaryTournament2", parameters);

				// Add the operators to the algorithm
				algorithm.addOperator("crossover", crossover);
				algorithm.addOperator("mutation", mutation);
				algorithm.addOperator("selection", selection);

				
				// Execute the Algorithm
				long initTime = System.currentTimeMillis();
				SolutionSet population = algorithm.execute();
				long estimatedTime = System.currentTimeMillis() - initTime;

				// Result messages
				logger_.info("Total execution time: " + estimatedTime + "ms");
				logger_.info("Variables values have been writen to file VAR");
				population.printVariablesToFile(path + "\\VAR_seed_"
						+ seed[i]);
				logger_.info("Objectives values have been writen to file FUN");
				population.printObjectivesToFile(path + "\\FUN_seed_"
						+ seed[i]);
			} catch (JMException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}


public class AalborgProblemWithTrackingMain {

	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object

	public static void main(String[] args) throws JMException,
			SecurityException, IOException, ClassNotFoundException {

	/*	logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAII_main.log");
		logger_.addHandler(fileHandler_);

		Problem problem; // The problem to solve
		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		Operator selection; // Selection operator

		HashMap parameters; // Operator parameters

		QualityIndicator indicators; // Object to get quality indicators

		long SPEA2_seeds[] = { 154568, 148456, 447514, 458475, 274587, 712584,
				975572, 585464, 467542, 686544 };
		long SPEA2_SI_seeds[] = { 857578, 647647, 647848, 891747, 957363,
				538947, 425374, 637384, 125386, 243858 };

		int numberOfRun = 10;
		for (int i = 0; i < numberOfRun; i++) {

			PseudoRandom.setRandomGenerator(new RandomGenerator(SPEA2_seeds[i]));

			indicators = null;

			problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolutionForThread(
					"Real", "InitializationResults\\WithTrack\\SPEA2");
			// algorithm = new NSGAIITrackIndicators(problem, seed[i],
			// "SBX_DKMutation");

			parameters = new HashMap();

			algorithm = new SPEA2(problem);
			// algorithm = new ssNSGAII(problem);

			// indicators = new QualityIndicator(problem,
			// "C:\\Users\\Nusrat\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\Results\\truePf\\mergefun.pf")
			// ;

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
			parameters.put("probability", 0.1);
			parameters.put("distributionIndex", 10.0);
			mutation = MutationFactory.getMutationOperator(
					"PolynomialMutation", parameters);

			// mutation =
			// MutationFactory.getMutationOperator("GeneralRealMutationForRes",
			// . parameters);

			// Selection Operator
			parameters = null;
			selection = SelectionFactory.getSelectionOperator(
					"BinaryTournament2", parameters);

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
			population
					.printVariablesToFile("InitializationResults\\SPEA2WithoutTrack\\VAR_seed_"
							+ SPEA2_seeds[i]);
			logger_.info("Objectives values have been writen to file FUN");
			population
					.printObjectivesToFile("InitializationResults\\SPEA2WithoutTrack\\FUN_seed_"
							+ SPEA2_seeds[i]);
		}*/
		
		  
		if(args[0].equals("NSGAII")){
			NSGAII_RunWithTracking nsgaii = new NSGAII_RunWithTracking();
			nsgaii.run();
		}else if(args[0].equals("NSGAII_SI")){
			NSGAII_SI_RunWithTracking nsgaii_si = new NSGAII_SI_RunWithTracking();
			nsgaii_si.run();
		}else if(args[0].equals("SPEA2")){
			SPEA2_RunWithTracking spea2 = new SPEA2_RunWithTracking();
			spea2.run(100, 7000);
		}else if(args[0].equals("SPEA2_SI")){
			SPEA2_SI_RunWithTracking spea2_si = new SPEA2_SI_RunWithTracking();
			spea2_si.run(100,7000);
		} 
			
		  
		   
	}
}
