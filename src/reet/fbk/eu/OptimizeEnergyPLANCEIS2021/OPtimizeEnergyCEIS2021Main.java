package reet.fbk.eu.OptimizeEnergyPLANCEIS2021;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.FileHandler;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.PolynomialMutation;
import jmetal.operators.selection.SelectionFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.RandomGenerator;
import reet.fbk.eu.jmetal.operators.mutation.MutationFactory;
import reet.fbk.eu.OptimizeEnergyPLANCEIS2021.Problem.EnergyPLANProblemCEIS2021V1Spooling;

import reet.fbk.eu.OptimizeEnergyPLANSRI.Problem.EnergyPLANProblemSRIV4Spooling;
import reet.fbk.eu.OptimizeEnergyPLANCEIS2021.metaheuristics.nsgaii.NSGAIISpooling;
import reet.fbk.eu.OptimizeEnergyPLANCEIS2021.util.WriteDataToFile;
//import reet.fbk.eu.jmetal.operators.mutation.MutationFactory;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.apache.commons.collections.MultiMap;

public class OPtimizeEnergyCEIS2021Main {

	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object
	
	public static int numberOfVariables;

	public static void main(String[] args) throws JMException, SecurityException, IOException, ClassNotFoundException {

		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAII_main.log");
		// fileHandler_ = new FileHandler("SPEA2.log");
		logger_.addHandler(fileHandler_);
	
		Problem problem; // The problem to solve
		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		Operator selection; // Selection operator

		HashMap parameters; // Operator parameters

		QualityIndicator indicators; // Object to get quality indicators

		// seed for NSGAii
		// long seed [] = {545782, 455875, 547945, 458478, 981354, 652262,
		// 562366, 365652, 456545, 549235 };
		// seed for spea2
		// long seed[]={102354,986587,456987,159753,
		// 216557,589632,471259,523486,4158963,745896};

		String[] simulatedYear = { "2030" };

		for (int year = 0; year < simulatedYear.length; year++) {
			int numberOfRun = 1;

			String folder = "C:\\Users\\shaik\\Desktop\\SRIResults\\" + "\\" + simulatedYear[year];
			File file = new File(folder);
			file.mkdir();

			for (int i = 0; i < numberOfRun; i++) {

				File filetmp = new File(folder);
				File fileAllSolutions = new File(filetmp + "\\allSolutionsParameters_"
						+ "run"+i + ".txt");
				File fileParetoFrontSolutions = new File(filetmp + "\\ParetoFrontSolutionsParameters_"
						+ "run"+i + ".txt");
				
				// PseudoRandom.setRandomGenerator(new
				// RandomGenerator(seed[i]));

				indicators = null;

				// problems for 2 objectives
				problem = new EnergyPLANProblemCEIS2021V1Spooling("Real", simulatedYear[year], folder);
				
				numberOfVariables = problem.getNumberOfVariables();
				
				parameters = new HashMap();

				algorithm = new NSGAIISpooling(problem);
				algorithm.setInputParameter("folder", folder);

				// Algorithm parameters
				int populationSize = 10;
				int maxEvaluations = 50;
				algorithm.setInputParameter("populationSize", populationSize);
				algorithm.setInputParameter("maxEvaluations", maxEvaluations);

				// for spea2
				// algorithm.setInputParameter("archiveSize",150);

				// Mutation and Crossover for Real codification
				parameters = new HashMap();
				parameters.put("probability", 0.9);
				parameters.put("distributionIndex", 10.0);
				crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover", parameters);

				parameters = new HashMap();
				parameters.put("probability", 1.0 / problem.getNumberOfVariables());
				parameters.put("distributionIndex", 10.0);

				mutation = new PolynomialMutation(parameters);

				// Selection Operator
				parameters = null;
				selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);

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

			/*	ExtractEnergyPLANParametersTrentoProvinceV10 ex = new ExtractEnergyPLANParametersTrentoProvinceV10(
						simulatedYear[year]);
				for (int z = 0; z < population.size(); z++) {
					Solution solution = population.get(z);
					
					if (solution.getOverallConstraintViolation() >= 0) {
						String line = "";
						for (int j = 0; j < solution.numberOfVariables(); j++) {
							line = line + " " + solution.getDecisionVariables()[i];
						}
						MultiMap energyplanmMapForSimulation;
						
						energyplanmMapForSimulation = ex.simulateEnergyPLAN(line);
						try {
							ex.WriteEnergyPLANParametersToFile(energyplanmMapForSimulation, folder);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}*/
				
				//write all solutions
				HashMap hm = ((EnergyPLANProblemCEIS2021V1Spooling)problem).getTheList();
				WriteDataToFile.init();
				for (Object value : hm.values()) {
					MultiMap map = (MultiMap) value;
					
					WriteDataToFile.WriteEnergyPLANParametersToFile(map, fileAllSolutions);
				}
				
				for(int j=0;j<population.size();j++) {
					Solution s = population.get(j);
					ArrayList<String> str = solutonToString(s);
					MultiMap map = (MultiMap) hm.get(str);
					WriteDataToFile.WriteEnergyPLANParametersToFile(map, fileParetoFrontSolutions);
				}
				
				
				// Result messages
				logger_.info("Total execution time: " + estimatedTime + "ms");
				logger_.info("Variables values have been writen to file VAR");
				population.printFeasibleVAR(folder + "\\VAR" );
				// population.printFeasibleVAR("VAR");
				logger_.info("Objectives values have been writen to file FUN");
				population.printFeasibleFUN(folder + "\\FUN" );
				// population.printFeasibleFUN("FUN");
			}
			// mergeFUNFiles(folder);

			// ExtrectParetoFrontAndDecisionVariables epf = new
			// ExtrectParetoFrontAndDecisionVariables(
			// folder+"\\mergeFUN", folder+"\\mergeVAR", 2, 11);
			// epf.writeParetoFront();

			// ExtractEnergyPLANParametersVdN2DWithElecTransport ob = new
			// ExtractEnergyPLANParametersVdN2DWithElecTransport(simulatedYear[year],
			// simulatedScenario[scenario]);
			// ob.extractAllEnergyPLANParametersFromAVARFile(folder+"\\mergeVAR.pf");

		}
	}

	
	public static void mergeFUNFiles(String folder) {

		FilenameFilter FUNFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith("FUN");
			}
		};

		FilenameFilter VARFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith("VAR");
			}
		};

		File f = new File(folder); // current directory
		File[] FUNfiles = f.listFiles(FUNFilter);
		File[] VARfiles = f.listFiles(VARFilter);

		try {
			File mergeFUNfile = new File(folder + "\\mergeFUN");
			BufferedWriter mergeFUNbw = new BufferedWriter(new FileWriter(mergeFUNfile.getAbsoluteFile()));

			File mergeVARfile = new File(folder + "\\mergeVAR");
			BufferedWriter mergeVARbw = new BufferedWriter(new FileWriter(mergeVARfile.getAbsoluteFile()));

			if (!mergeFUNfile.exists()) {
				mergeFUNfile.createNewFile();
			}

			if (!mergeFUNfile.exists()) {
				mergeFUNfile.createNewFile();

			}

			for (int i = 0; i < FUNfiles.length; i++) {

				BufferedReader mergeFUNbr = new BufferedReader(new FileReader(FUNfiles[i].getAbsoluteFile()));
				String line;
				while ((line = mergeFUNbr.readLine()) != null) {
					mergeFUNbw.write(line);
					mergeFUNbw.write("\n");
				}
				mergeFUNbr.close();

			}
			mergeFUNbw.close();

			for (int i = 0; i < VARfiles.length; i++) {

				BufferedReader mergeVARbr = new BufferedReader(new FileReader(VARfiles[i].getAbsoluteFile()));
				String line;
				while ((line = mergeVARbr.readLine()) != null) {
					mergeVARbw.write(line);
					mergeVARbw.write("\n");
				}
				mergeVARbr.close();

			}
			mergeVARbw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	 static ArrayList<String> solutonToString(Solution s) {
			ArrayList<String> str = new ArrayList();
			for(int i=0;i<numberOfVariables;i++) {
				try {
					str.add(s.getDecisionVariables()[i].getValue()+"");
				} catch (JMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return str;
		}

}
