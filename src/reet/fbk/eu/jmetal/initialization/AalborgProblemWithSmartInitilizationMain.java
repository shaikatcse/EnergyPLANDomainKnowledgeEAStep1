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
import reet.fbk.eu.jmetal.initialization.metaheuristics.spea2.SPEA2ForSI;
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
 * argument. The class only run the algorithm with smart initilization technique. 
 * 
 */
class NSGAII_SI_Run{
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object
	
	Problem problem; // The problem to solve
	Algorithm algorithm; // The algorithm to use
	Operator crossover; // Crossover operator
	Operator mutation; // Mutation operator
	Operator selection; // Selection operator

	HashMap parameters; // Operator parameters

	QualityIndicator indicators; // Object to get quality indicators
	
	public void run(int populationSize, int maxEvaluations) throws SecurityException, IOException, JMException, ClassNotFoundException{
		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAII_main.log");
		logger_.addHandler(fileHandler_);
		
		//seed for NSGAII
		long seed [] = {343434, 551254, 145845, 555541, 551641,625882,985312,458745, 228424, 7811554 };
		
		int numberOfRun=10;
		
		File folder = new File("InitializationResults/InitIndividualWithSI");
		File[] listOfFiles = folder.listFiles(new FilenameFilter() {
		    @Override
		    public boolean accept(File dir, String name) {
		        return name.startsWith("Init");
		    }
		});
		
		for (int i = 0; i < numberOfRun; i++) {
			
			
			PseudoRandom.setRandomGenerator(new RandomGenerator(seed[i]));
			
			indicators = null;

			problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution("Real");
			//algorithm = new NSGAIITrackIndicators(problem, seed[i], "SBX_DKMutation");
			
			Boolean favorGenesforRE[] ={true, true, true, true, true};
			Boolean favorGenesforConventionalPP[] ={false, false, false, false, false};
			parameters = new HashMap();
			parameters.put("favorGenesforRE", favorGenesforRE);
			parameters.put("favorGenesForCon", favorGenesforConventionalPP);
			parameters.put("InitialPopulationFile", listOfFiles[i].getAbsolutePath());
			
			algorithm = new NSGAIIForSI(problem, parameters);
			
						
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
			population.printVariablesToFile("InitializationResults\\NSGAIIWithoutTrackWithSI\\VAR_Init_"+listOfFiles[i].getName().substring(14)+"_seed_"+seed[i]);
			logger_.info("Objectives values have been writen to file FUN");
			population.printObjectivesToFile("InitializationResults\\NSGAIIWithoutTrackWithSI\\FUN_Init_"+listOfFiles[i].getName().substring(14)+"_seed_"+seed[i]);
		}
		
	}
}

class SPEA2_SI_Run{
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object
	
	Problem problem; // The problem to solve
	Algorithm algorithm; // The algorithm to use
	Operator crossover; // Crossover operator
	Operator mutation; // Mutation operator
	Operator selection; // Selection operator

	HashMap parameters; // Operator parameters

	QualityIndicator indicators; // Object to get quality indicators
	
	public void run(int populationSize, int maxEvaluations) throws SecurityException, IOException, JMException, ClassNotFoundException{
		
		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAII_main.log");
		logger_.addHandler(fileHandler_);
	
		//seed for spea2
		long seed [] = {857578, 647647, 647848, 891747, 957363, 538947, 425374, 637384, 125386, 243858 };
		int numberOfRun=10;
		
		File folder = new File("InitializationResults/InitIndividualWithSI");
		File[] listOfFiles = folder.listFiles(new FilenameFilter() {
		    @Override
		    public boolean accept(File dir, String name) {
		        return name.startsWith("Init");
		    }
		});
		
		for (int i = 0; i < numberOfRun; i++) {
			
			
			PseudoRandom.setRandomGenerator(new RandomGenerator(seed[i]));
			
			indicators = null;

			problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution("Real");
			//algorithm = new NSGAIITrackIndicators(problem, seed[i], "SBX_DKMutation");
			
			Boolean favorGenesforRE[] ={true, true, true, true, true};
			Boolean favorGenesforConventionalPP[] ={false, false, false, false, false};
			parameters = new HashMap();
			parameters.put("favorGenesforRE", favorGenesforRE);
			parameters.put("favorGenesForCon", favorGenesforConventionalPP);
			parameters.put("InitialPopulationFile", listOfFiles[i].getAbsolutePath());
			
			algorithm = new SPEA2ForSI(problem, parameters);
			
			// algorithm = new ssNSGAII(problem);

			//indicators = new QualityIndicator(problem, "C:\\Users\\Nusrat\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\Results\\truePf\\mergefun.pf") ;
			
			
			// Algorithm parameters
			algorithm.setInputParameter("populationSize", populationSize);
			algorithm.setInputParameter("maxEvaluations", maxEvaluations);
			//for spea2
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
			population.printVariablesToFile("InitializationResults\\SPEA2WithoutTrackWithSI\\VAR_Init_"+listOfFiles[i].getName().substring(14)+"_seed_"+seed[i]);
			logger_.info("Objectives values have been writen to file FUN");
			population.printObjectivesToFile("InitializationResults\\SPEA2WithoutTrackWithSI\\FUN_Init_"+listOfFiles[i].getName().substring(14)+"_seed_"+seed[i]);
		}	
	}
}

public class AalborgProblemWithSmartInitilizationMain {


	public static void main(String[] args) throws JMException,
			SecurityException, IOException, ClassNotFoundException {
		
		if(args[0].equals("NSGAII_SI")){
			NSGAII_SI_Run nsgaii_si = new NSGAII_SI_Run();
			nsgaii_si.run(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		}else if(args[0].equals("SPEA2_SI")){
			SPEA2_SI_Run spea2_si = new SPEA2_SI_Run();
			spea2_si.run(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		}
	}
}
