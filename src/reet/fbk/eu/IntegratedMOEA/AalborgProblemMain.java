package reet.fbk.eu.IntegratedMOEA;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import reet.fbk.eu.jmetal.metaheuristics.nsgaII.NSGAIIForDKandSCandSI;
import reet.fbk.eu.jmetal.metaheuristics.spea2.SPEA2ForDKandSCandSI;
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

/**
 * The class is main class where the class can execute algorithm from the
 * command line argument. The class only run the algorithm with smart
 * initilization technique.
 * 
 */
class Integrated_NSGAII_Run {
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object

	Problem problem; // The problem to solve
	Algorithm algorithm; // The algorithm to use
	Operator crossover; // Crossover operator
	Operator mutation; // Mutation operator
	Operator selection; // Selection operator

	HashMap parameters; // Operator parameters

	QualityIndicator indicators; // Object to get quality indicators

	public void run(int populationSize, int maxEvaluations)
			throws SecurityException, IOException, JMException,
			ClassNotFoundException {
		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAII_main.log");
		logger_.addHandler(fileHandler_);

		int numberOfRun = 12;

		/*
		 * File f1= new
		 * File("InitializationResults/InitIndividualWithSI/InitIndv_seed_844545"
		 * ); File f2= new
		 * File("InitializationResults/InitIndividualWithSI/InitIndv_seed_752453"
		 * ); File f3= new
		 * File("InitializationResults/InitIndividualWithSI/InitIndv_seed_714451"
		 * ); File f4= new
		 * File("InitializationResults/InitIndividualWithSI/InitIndv_seed_565656"
		 * ); File f5= new
		 * File("InitializationResults/InitIndividualWithSI/InitIndv_seed_545454"
		 * );
		 * 
		 * File[] listOfFiles={f1,f2,f3,f4,f5};
		 */

		/*
		 * File folder = new File("InitializationResults/InitIndividualWithSI");
		 * File[] listOfFiles = folder.listFiles(new FilenameFilter() {
		 * 
		 * @Override public boolean accept(File dir, String name) { return
		 * name.startsWith("Init"); } });
		 */

		File[] listOfFiles = new File[numberOfRun];

		/*
		 * 2nd initial seeds long initSeed[] = { 144759, 271439, 445964, 494817,
		 * 530563, 724859, 746153, 747584, 866309, 938562};
		 */
		// 3rd initial seed
		/*
		 * long initSeed[] ={ 172480, 242648, 268602, 272313, 412455, 441996,
		 * 633160, 814562, 822805, 843288 };
		 */

		long seed[] = {/* 131528, 194125, 199140, 286828, 287192, 303839, 304408,
				325235, 337877, 347354,

				376366, 449029, 472366, 507361, 526317, 570490, 571961, 585378,*/
				607688, 664876,

				684002, 692766, 749363, 853504, 860186, 892574, 925410, 931203,
				962803, 991907

		};

		long initSeed[] = { /*125742, 144759, 172480, 242648, 245454, 268602,
				271439, 272313, 412455, 441996,

				445964, 447551, 455411, 455885, 494817, 530563, 545454, 565656,*/
				633160, 714451,

				724859, 746153, 747584, 752453, 814562, 822805, 843288, 844545,
				866309, 938562

		};

		for (int i = 0; i < numberOfRun; i++) {
			listOfFiles[i] = new File(
					"InitializationResults/InitIndividualWithSI/WithoutRM/InitIndv_seed_"
							+ initSeed[i]);

		}

		for (int i = 0; i < numberOfRun; i++) {

			PseudoRandom.setRandomGenerator(new RandomGenerator(seed[i]));

			indicators = null;

			problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution(
					"Real");
			// algorithm = new NSGAIITrackIndicators(problem, seed[i],
			// "SBX_DKMutation");

			Boolean favorGenesforRE[] = { true, true, true, true, true };
			Boolean favorGenesforConventionalPP[] = { false, false, false,
					false, false };
			parameters = new HashMap();
			parameters.put("favorGenesforRE", favorGenesforRE);
			parameters.put("favorGenesForCon", favorGenesforConventionalPP);
			parameters.put("InitialPopulationFile",
					listOfFiles[i].getAbsolutePath());

			algorithm = new NSGAIIForDKandSCandSI(problem, parameters);

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
			parameters.put("favorGenesforRE", favorGenesforRE);
			parameters.put("favorGenesForConventioanlPP",
					favorGenesforConventionalPP);
			parameters.put(
					"maximum generation",
					(int) algorithm.getInputParameter("maxEvaluations")
							/ (int) algorithm
									.getInputParameter("populationSize") - 1);

			mutation = MutationFactory.getMutationOperator(
					"GeneralModifiedPolynomialMutationForEnergySystems",
					parameters);

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

			int stopGenCriteria1 = ((Integer) algorithm
					.getOutputParameter("stopGenCriteria1")).intValue();

			writeStoppingGenerationInFile(listOfFiles[i].getName()
					.substring(14), stopGenCriteria1,
					"IntegratedMOEAResults\\NSGAII_Com\\Criteria1");
			SolutionSet stoppingPopulationCriteria1 = ((SolutionSet) algorithm
					.getOutputParameter("stoppingPopulationCriteria1"));

			stoppingPopulationCriteria1
					.printVariablesToFile("IntegratedMOEAResults\\NSGAII_Com\\Criteria1\\VAR_Init_"
							+ listOfFiles[i].getName().substring(14)
							+ "_seed_"
							+ seed[i]);
			logger_.info("Objectives values have been writen to file FUN");
			stoppingPopulationCriteria1
					.printObjectivesToFile("IntegratedMOEAResults\\NSGAII_Com\\Criteria1\\FUN_Init_"
							+ listOfFiles[i].getName().substring(14)
							+ "_seed_"
							+ seed[i]);

			//criteria 2
			int stopGenCriteria2 = ((Integer) algorithm
					.getOutputParameter("stopGenCriteria2")).intValue();

			writeStoppingGenerationInFile(listOfFiles[i].getName()
					.substring(14), stopGenCriteria2,
					"IntegratedMOEAResults\\NSGAII_Com\\Criteria2");
			SolutionSet stoppingPopulationCriteria2 = ((SolutionSet) algorithm
					.getOutputParameter("stoppingPopulationCriteria2"));

			stoppingPopulationCriteria2
					.printVariablesToFile("IntegratedMOEAResults\\NSGAII_Com\\Criteria2\\VAR_Init_"
							+ listOfFiles[i].getName().substring(14)
							+ "_seed_"
							+ seed[i]);
			logger_.info("Objectives values have been writen to file FUN");
			stoppingPopulationCriteria2
					.printObjectivesToFile("IntegratedMOEAResults\\NSGAII_Com\\Criteria2\\FUN_Init_"
							+ listOfFiles[i].getName().substring(14)
							+ "_seed_"
							+ seed[i]);

			// Result messages
			logger_.info("Total execution time: " + estimatedTime + "ms");
			logger_.info("Variables values have been writen to file VAR");
			population
					.printVariablesToFile("IntegratedMOEAResults\\NSGAII_Com\\AllEvoCom\\VAR_Init_"
							+ listOfFiles[i].getName().substring(14)
							+ "_seed_"
							+ seed[i]);
			logger_.info("Objectives values have been writen to file FUN");
			population
					.printObjectivesToFile("IntegratedMOEAResults\\NSGAII_Com\\AllEvoCom\\FUN_Init_"
							+ listOfFiles[i].getName().substring(14)
							+ "_seed_"
							+ seed[i]);
		}

	}

	public void writeStoppingGenerationInFile(String seed, int generation,
			String folder) {
		File file;
		FileWriter fw;
		BufferedWriter bw;

		file = new File(folder + "\\StoppingGen");

		try {
			if (!file.exists()) {
				file.createNewFile();

			}

			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			bw.write(seed + " " + generation);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			System.out.println("Something wrong StoppingGen file");
			System.exit(0);
		}

	}
}

class Integrated_SPEA2_Run {
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object

	Problem problem; // The problem to solve
	Algorithm algorithm; // The algorithm to use
	Operator crossover; // Crossover operator
	Operator mutation; // Mutation operator
	Operator selection; // Selection operator

	HashMap parameters; // Operator parameters

	QualityIndicator indicators; // Object to get quality indicators

	public void run(int populationSize, int maxEvaluations)
			throws SecurityException, IOException, JMException,
			ClassNotFoundException {

		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAII_main.log");
		logger_.addHandler(fileHandler_);

		/*
		 * int numberOfRun = 2; File f1= new
		 * File("InitializationResults/InitIndividualWithSI/InitIndv_seed_455885"
		 * ); File f2= new
		 * File("InitializationResults/InitIndividualWithSI/InitIndv_seed_545454"
		 * ); /*File f3= new
		 * File("InitializationResults/InitIndividualWithSI/InitIndv_seed_714451"
		 * ); File f4= new
		 * File("InitializationResults/InitIndividualWithSI/InitIndv_seed_565656"
		 * ); File f5= new
		 * File("InitializationResults/InitIndividualWithSI/InitIndv_seed_545454"
		 * );
		 * 
		 * File[] listOfFiles={f1,f2};
		 */

		/*
		 * File folder = new File("InitializationResults/InitIndividualWithSI");
		 * File[] listOfFiles = folder.listFiles(new FilenameFilter() {
		 * 
		 * @Override public boolean accept(File dir, String name) { return
		 * name.startsWith("Init"); } });
		 */

		int numberOfRun = 2;

		File[] listOfFiles = new File[numberOfRun];

		/*
		 * 2nd initial seed for reading files for smart initialization long
		 * initSeed[] = { 144759, 271439, 445964, 494817, 530563, 724859,
		 * 746153, 747584, 866309, 938562};
		 */

		// 3rd initial seed
		/*
		 * long initSeed[] ={ 172480, 242648, 268602, 272313, 412455, 441996,
		 * 633160, 814562, 822805, 843288 };
		 */

		long seed[] = { /*116023, 122077, 148412, 229494, 234118,*/ 250755, /*289773,*/
				315371/*, 366586, 386609*/

				/*403236, 529557, 542538, 544241, 625137, 629633, 630311, 668240,
				686408, 701282,

				710450, 736124, 758115, 811191, 861135, 889553, 922683, 929630,
				943600, 983919*/

		};

		long initSeed[] = { /*125742,144759, 172480, 242648, 245454,*/ 268602,
				/*271439,*/ 272313/*, 412455, 441996*/

				/*445964, 447551, 455411, 455885, 494817, 530563, 545454, 565656,
				633160, 714451,

				724859, 746153, 747584, 752453, 814562, 822805, 843288, 844545,
				866309, 938562*/

		};

		/*
		 * for(int i=0;i<numberOfRun;i++){ listOfFiles[i] = new File(
		 * "InitializationResults/InitIndividualWithSI/WithoutRM/InitIndv_seed_"
		 * +initSeed[i]);
		 * 
		 * }
		 */

		// to run in reverse order
		for (int i = 0; i < numberOfRun; i++) {
					listOfFiles[i] = new File(
					"InitializationResults/InitIndividualWithSI/WithoutRM/InitIndv_seed_"
							+ initSeed[i]);

		}

		// for (int i = 0; i < numberOfRun; i++) {
		// to run in reverse order
		for (int i = 0; i < numberOfRun; i++) {
			
			PseudoRandom.setRandomGenerator(new RandomGenerator(seed[i]));

			indicators = null;

			problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution(
					"Real");
			// algorithm = new NSGAIITrackIndicators(problem, seed[i],
			// "SBX_DKMutation");

			Boolean favorGenesforRE[] = { true, true, true, true, true };
			Boolean favorGenesforConventionalPP[] = { false, false, false,
					false, false };
			parameters = new HashMap();
			parameters.put("favorGenesforRE", favorGenesforRE);
			parameters.put("favorGenesForCon", favorGenesforConventionalPP);
			parameters.put("InitialPopulationFile",
					listOfFiles[i].getAbsolutePath());

			algorithm = new SPEA2ForDKandSCandSI(problem, parameters);

			// algorithm = new ssNSGAII(problem);

			// indicators = new QualityIndicator(problem,
			// "C:\\Users\\Nusrat\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\Results\\truePf\\mergefun.pf")
			// ;

			// Algorithm parameters
			algorithm.setInputParameter("populationSize", populationSize);
			algorithm.setInputParameter("maxEvaluations", maxEvaluations);
			// for spea2
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
			parameters.put("favorGenesforRE", favorGenesforRE);
			parameters.put("favorGenesForConventioanlPP",
					favorGenesforConventionalPP);
			parameters.put(
					"maximum generation",
					(int) algorithm.getInputParameter("maxEvaluations")
							/ (int) algorithm
									.getInputParameter("populationSize") - 1);

			mutation = MutationFactory.getMutationOperator(
					"GeneralModifiedPolynomialMutationForEnergySystems",
					parameters);
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

			int stopGenCriteria1 = ((Integer) algorithm
					.getOutputParameter("stopGenCriteria1")).intValue();

			writeStoppingGenerationInFile(listOfFiles[i].getName()
					.substring(14), stopGenCriteria1,
					"IntegratedMOEAResults\\SPEA2_Com\\Criteria1");
			SolutionSet stoppingPopulationCriteria1 = ((SolutionSet) algorithm
					.getOutputParameter("stoppingPopulationCriteria1"));

			stoppingPopulationCriteria1
					.printVariablesToFile("IntegratedMOEAResults\\SPEA2_Com\\Criteria1\\VAR_Init_"
							+ listOfFiles[i].getName().substring(14)
							+ "_seed_"
							+ seed[i]);
			logger_.info("Objectives values have been writen to file FUN");
			stoppingPopulationCriteria1
					.printObjectivesToFile("IntegratedMOEAResults\\SPEA2_Com\\Criteria1\\FUN_Init_"
							+ listOfFiles[i].getName().substring(14)
							+ "_seed_"
							+ seed[i]);

			//criteria 2
			int stopGenCriteria2 = ((Integer) algorithm
					.getOutputParameter("stopGenCriteria2")).intValue();

			writeStoppingGenerationInFile(listOfFiles[i].getName()
					.substring(14), stopGenCriteria2,
					"IntegratedMOEAResults\\SPEA2_Com\\Criteria2");
			SolutionSet stoppingPopulationCriteria2 = ((SolutionSet) algorithm
					.getOutputParameter("stoppingPopulationCriteria2"));

			stoppingPopulationCriteria2
					.printVariablesToFile("IntegratedMOEAResults\\SPEA2_Com\\Criteria2\\VAR_Init_"
							+ listOfFiles[i].getName().substring(14)
							+ "_seed_"
							+ seed[i]);
			logger_.info("Objectives values have been writen to file FUN");
			stoppingPopulationCriteria2
					.printObjectivesToFile("IntegratedMOEAResults\\SPEA2_Com\\Criteria2\\FUN_Init_"
							+ listOfFiles[i].getName().substring(14)
							+ "_seed_"
							+ seed[i]);

			// Result messages
			logger_.info("Total execution time: " + estimatedTime + "ms");
			logger_.info("Variables values have been writen to file VAR");
			population
					.printVariablesToFile("IntegratedMOEAResults\\SPEA2_Com\\AllEvoCom\\VAR_Init_"
							+ listOfFiles[i].getName().substring(14)
							+ "_seed_"
							+ seed[i]);
			logger_.info("Objectives values have been writen to file FUN");
			population
					.printObjectivesToFile("IntegratedMOEAResults\\SPEA2_Com\\AllEvoCom\\FUN_Init_"
							+ listOfFiles[i].getName().substring(14)
							+ "_seed_"
							+ seed[i]);
		
		}
	}

	public void writeStoppingGenerationInFile(String seed, int generation,
			String folder) {
		File file;
		FileWriter fw;
		BufferedWriter bw;

		file = new File(folder + "\\StoppingGen");

		try {
			if (!file.exists()) {
				file.createNewFile();

			}

			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			bw.write(seed + " " + generation);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			System.out.println("Something wrong StoppingGen file");
			System.exit(0);
		}

	}
}

public class AalborgProblemMain {

	public static void main(String[] args) throws JMException,
			SecurityException, IOException, ClassNotFoundException {

		if (args[0].equals("NSGAII")) {
			Integrated_NSGAII_Run integrated_nsgaii = new Integrated_NSGAII_Run();
			integrated_nsgaii.run(Integer.parseInt(args[1]),
					Integer.parseInt(args[2]));
		} else if (args[0].equals("SPEA2")) {
			Integrated_SPEA2_Run integrated_spea2 = new Integrated_SPEA2_Run();
			integrated_spea2.run(Integer.parseInt(args[1]),
					Integer.parseInt(args[2]));
		}
	}
}
