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
import reet.fbk.eu.jmetal.metaheuristics.nsgaII.NSGAIIForDK;
import reet.fbk.eu.jmetal.metaheuristics.nsgaII.NSGAIIForSI;
import reet.fbk.eu.jmetal.metaheuristics.nsgaII.NSGAIITrackIndicators;
import reet.fbk.eu.jmetal.metaheuristics.spea2.SPEA2TrackIndicators;
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

class NSGAII_Runnable implements Runnable {

	public static Logger logger_; // Logger object
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

	public void run() {

		for (int i = 0; i < NSGAII_seeds.length; i++) {

			PseudoRandom
					.setRandomGenerator(new RandomGenerator(NSGAII_seeds[i]));

			problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolutionForThread(
					"Real", path);

			indicators = new QualityIndicator(problem,
					".\\InitializationResults\\ParetoFront\\mergeFUN.pf");
			// algorithm = new NSGAIITrackIndicators(problem, seed[i],
			// "SBX_DKMutation");

			parameters = new HashMap();

			algorithm = new NSGAIITrackIndicators(problem, NSGAII_seeds[i],
					path);
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

				// Add the indicator object to the algorithm
				algorithm.setInputParameter("indicators", indicators);

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

class NSGAII_SI_Runnable implements Runnable {

	public static Logger logger_; // Logger object
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

	public void run() {

		
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

	
			parameters = new HashMap();
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

				// Add the indicator object to the algorithm
				algorithm.setInputParameter("indicators", indicators);

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

class SPEA2_Runnable implements Runnable {
	public static Logger logger_; // Logger object
	long SPEA2_seeds[] = { 154568, 148456, 447514, 458475, 274587, 712584,
			975572, 585464, 467542, 686544 };

	Problem problem; // The problem to solve
	Algorithm algorithm; // The algorithm to use
	Operator crossover; // Crossover operator
	Operator mutation; // Mutation operator
	Operator selection; // Selection operator

	HashMap parameters; // Operator parameters

	QualityIndicator indicators; // Object to get quality indicators

	String path = "InitializationResults\\WithTrack\\SPEA2";

	public void run() {
		int numberOfRun = 10;
		for (int i = 0; i < numberOfRun; i++) {

			PseudoRandom
					.setRandomGenerator(new RandomGenerator(SPEA2_seeds[i]));

			indicators = null;

			problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution(
					"Real");
			// algorithm = new NSGAIITrackIndicators(problem, seed[i],
			// "SBX_DKMutation");

			parameters = new HashMap();

			algorithm = new SPEA2TrackIndicators(problem);

			// Algorithm parameters
			algorithm.setInputParameter("populationSize", 100);
			algorithm.setInputParameter("maxEvaluations", 7000);
			algorithm.setInputParameter("archiveSize", 100);

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

				// Add the indicator object to the algorithm
				algorithm.setInputParameter("indicators", indicators);

				// Execute the Algorithm
				long initTime = System.currentTimeMillis();
				SolutionSet population = algorithm.execute();
				long estimatedTime = System.currentTimeMillis() - initTime;

				// Result messages
				logger_.info("Total execution time: " + estimatedTime + "ms");
				logger_.info("Variables values have been writen to file VAR");
				population.printVariablesToFile(path + "\\VAR_seed_"
						+ SPEA2_seeds[i]);
				logger_.info("Objectives values have been writen to file FUN");
				population.printObjectivesToFile(path + "\\FUN_seed_"
						+ SPEA2_seeds[i]);
			} catch (JMException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}

class SPEA2_SI_Runnable implements Runnable {
	public static Logger logger_; // Logger object
	long SPEA2_SI_seeds[] = { 857578, 647647, 647848, 891747, 957363,
			538947, 425374, 637384, 125386, 243858 };

	Problem problem; // The problem to solve
	Algorithm algorithm; // The algorithm to use
	Operator crossover; // Crossover operator
	Operator mutation; // Mutation operator
	Operator selection; // Selection operator

	HashMap parameters; // Operator parameters

	QualityIndicator indicators; // Object to get quality indicators

	String path = "InitializationResults\\WithTrack\\SPEA2_SI";

	public void run() {
		int numberOfRun = 10;
		for (int i = 0; i < numberOfRun; i++) {

			PseudoRandom
					.setRandomGenerator(new RandomGenerator(SPEA2_SI_seeds[i]));

			indicators = null;

			problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution(
					"Real");
			// algorithm = new NSGAIITrackIndicators(problem, seed[i],
			// "SBX_DKMutation");

			parameters = new HashMap();

			algorithm = new SPEA2TrackIndicators(problem);

			// Algorithm parameters
			algorithm.setInputParameter("populationSize", 100);
			algorithm.setInputParameter("maxEvaluations", 7000);
			algorithm.setInputParameter("archiveSize", 100);

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

				// Add the indicator object to the algorithm
				algorithm.setInputParameter("indicators", indicators);

				// Execute the Algorithm
				long initTime = System.currentTimeMillis();
				SolutionSet population = algorithm.execute();
				long estimatedTime = System.currentTimeMillis() - initTime;

				// Result messages
				logger_.info("Total execution time: " + estimatedTime + "ms");
				logger_.info("Variables values have been writen to file VAR");
				population.printVariablesToFile(path + "\\VAR_seed_"
						+ SPEA2_SI_seeds[i]);
				logger_.info("Objectives values have been writen to file FUN");
				population.printObjectivesToFile(path + "\\FUN_seed_"
						+ SPEA2_SI_seeds[i]);
			} catch (JMException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}


public class AalborgProblemWithThreadsMain {

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
		
		  Thread t_nsgaii = new Thread(new NSGAII_Runnable());
		  t_nsgaii.start();
		  
		  Thread t_nsgaii_si = new Thread(new NSGAII_SI_Runnable());
		  t_nsgaii_si.start();
		  
		   
	}
}
