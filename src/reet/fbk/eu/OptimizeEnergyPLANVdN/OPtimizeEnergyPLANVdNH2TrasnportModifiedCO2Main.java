package reet.fbk.eu.OptimizeEnergyPLANVdN;

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
import java.util.HashMap;
import java.util.logging.FileHandler;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.RandomGenerator;
import reet.fbk.eu.jmetal.operators.mutation.MutationFactory;
import reet.fbk.eu.OptimizeEnergyPLANCIVIS.metaheuristics.NSGAIIForDK;
//import reet.fbk.eu.jmetal.operators.mutation.MutationFactory;

import reet.fbk.eu.OptimizeEnergyPLANVdN.metaheuristics.NSGAIIForDKandSCandSI;
import reet.fbk.eu.OptimizeEnergyPLANVdN.problem.EnergyPLANProblemVdN2DWithH2VehicleModifiedCO2;
import reet.fbk.eu.OptimizeEnergyPLANVdN.util.ExtractEnergyPLANParametersVdN2DWithElecTransport;
import reet.fbk.eu.OptimizeEnergyPLANVdN.util.ExtractEnergyPLANParametersVdN2DWithH2Transport;
import reet.fbk.eu.jmetal.initialization.DKInitializationInJAVA;
import reet.fbk.eu.jmetal.metaheuristics.spea2.SPEA2ForDK;
import reet.fbk.eu.jmetal.metaheuristics.spea2.SPEA2ForDKandSCandSIforCEIS;
import reet.fbk.eu.util.ExtrectParetoFrontAndDecisionVariables;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class OPtimizeEnergyPLANVdNH2TrasnportModifiedCO2Main {

	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object

	public static void main(String[] args) throws JMException,
			SecurityException, IOException, ClassNotFoundException {

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

		String[] simulatedYear = { "2020", "2030", "2050" };
		String[] simulatedScenario = { "2DS" };

		for (int year = 0; year < simulatedYear.length; year++) {
			for (int scenario = 0; scenario < simulatedScenario.length; scenario++) {
				int numberOfRun = 2;

				String folder = "C:\\Users\\mahbub\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\VdNResults\\ShahriarEleboration\\H2Transport\\"
						+ simulatedScenario[scenario]
						+ "\\"
						+ simulatedYear[year];
				
				for (int i = 0; i < numberOfRun; i++) {

					// PseudoRandom.setRandomGenerator(new
					// RandomGenerator(seed[i]));

					indicators = null;

					// problems for 2 objectives
					problem = new EnergyPLANProblemVdN2DWithH2VehicleModifiedCO2(
							"Real", simulatedYear[year],
							simulatedScenario[scenario]);

					// problem for 3 objectives
					// problem = new EnergyPLANProblemAalborg("Real");

					// decision variables
					// index - 0 -> PV Capacity
					// index - 1 -> oil boiler heat percentage
					// index - 2 -> nGas boiler heat percentage
					// index - 3 -> Biomass boiler heat percentage
					// index - 4 -> Biomass micro chp heat percentage
					// last percentage will go to individual HP percentage
					// index - 5 -> electric car percentage
					// index - 6 -> FCEV car percentage (no FCEV car)

					// index -7 -> solar thermal percentage in oil boiler
					// index -8 -> solar thermal percentage in nGas boiler
					// index -9 -> solar thermal percentage in biomass boiler
					// index -10 -> solar thermal percentage in biomass CHP
					// index -11 -> solar thermal percentage in HP

					Boolean favorGenesforRE[], favorGenesforConventionalPP[];
					if (simulatedYear[year].equals("2020")
							|| simulatedYear[year].equals("2030")) {
																	// pv     oB    ngb   bB   bCHP   FCEV    oS    nGS        bbS    bCHPs   HPS
						favorGenesforRE =             new Boolean[] { true, false, null, true, true, true,   true, true,     true,  true, true };
						favorGenesforConventionalPP = new Boolean[] { false, null, null, true, false, false, false, false,   false, false, null };
					} 
					  else {                                       // pv      oB   ngb    bB    bCHP   FCEV     oS      nGS     bbS    bCHPs HPS
						favorGenesforRE =             new Boolean[] {true,  false, null, true, true,  true,  true,   true,  true,   true,   true };
						favorGenesforConventionalPP = new Boolean[] {null,   null, null, true, false, null,  false,  false, false,  false,  null };

					}
					
					parameters = new HashMap();
					parameters = new HashMap();
					parameters.put("favorGenesforRE", favorGenesforRE);
					parameters.put("favorGenesForCon",
							favorGenesforConventionalPP);

					// algorithm = new SPEA2ForDKandSCandSIforCEIS(problem,
					// parameters);
					algorithm = new NSGAIIForDKandSCandSI(problem, parameters);

					// algorithm = new SPEA2ForDK(problem, seed[i],
					// "SPEA2_SBX_PolynomialMutation");

					// indicators = new QualityIndicator(problem,
					// "C:\\Users\\Nusrat\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\Results\\truePf\\mergefun.pf")
					// ;

					// Algorithm parameters
					int populationSize = 120;
					int maxEvaluations = 12000;
					algorithm.setInputParameter("populationSize",
							populationSize);
					algorithm.setInputParameter("maxEvaluations",
							maxEvaluations);

					// for spea2
					// algorithm.setInputParameter("archiveSize",150);

					// Mutation and Crossover for Real codification
					parameters = new HashMap();
					parameters.put("probability", 0.9);
					parameters.put("distributionIndex", 10.0);
					crossover = CrossoverFactory.getCrossoverOperator(
							"SBXCrossover", parameters);

					parameters = new HashMap();
					parameters.put("probability",
							1.0 / problem.getNumberOfVariables());
					parameters.put("distributionIndex", 10.0);
					parameters
							.put("maximum generation",
									(int) algorithm
											.getInputParameter("maxEvaluations")
											/ (int) algorithm
													.getInputParameter("populationSize")
											- 1);

					parameters.put("favorGenesforRE", favorGenesforRE);
					parameters.put("favorGenesForConventioanlPP",
							favorGenesforConventionalPP);
					// parameters.put("favorGenesForESD",favorGenesforESD );

					mutation = MutationFactory
							.getMutationOperator(
									"GeneralModifiedPolynomialMutationForEnergySystems",
									parameters);

					// parameters.put("maximum generation", (int)
					// algorithm.getInputParameter("maxEvaluations")/(int)
					// algorithm.getInputParameter("populationSize")-1);

					// mutation =
					// MutationFactory.getMutationOperator("GeneralModifiedPolynomialMutationForRes",
					// parameters);

					// Selection Operator
					parameters = null;
					selection = SelectionFactory.getSelectionOperator(
							"BinaryTournament", parameters);

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

					if (((Integer) algorithm.getOutputParameter("stopGen")) == null) {
						writeStoppingGenerationInFile(i, (int) maxEvaluations
								/ populationSize, folder);
					} else {
						int stopGen = ((Integer) algorithm
								.getOutputParameter("stopGen")).intValue();
						writeStoppingGenerationInFile(i, stopGen, folder);
					}

					// Result messages
					logger_.info("Total execution time: " + estimatedTime
							+ "ms");
					logger_.info("Variables values have been writen to file VAR");
					population.printFeasibleVAR(folder + "\\VAR" + i);
					// population.printFeasibleVAR("VAR");
					logger_.info("Objectives values have been writen to file FUN");
					population.printFeasibleFUN(folder + "\\FUN" + i);
					// population.printFeasibleFUN("FUN");
				}
				mergeFUNFiles(folder);
				
				ExtrectParetoFrontAndDecisionVariables epf = new ExtrectParetoFrontAndDecisionVariables(
						folder+"\\mergeFUN", folder+"\\mergeVAR", 2, 11);
				epf.writeParetoFront();
				
				ExtractEnergyPLANParametersVdN2DWithH2Transport ob = new ExtractEnergyPLANParametersVdN2DWithH2Transport(simulatedYear[year],
						simulatedScenario[scenario]);
				ob.extractAllEnergyPLANParametersFromAVARFile(folder+"\\mergeVAR.pf");
			}
		}
	}

	public static void writeStoppingGenerationInFile(int runNo, int generation,
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
			bw.write("Run no: " + runNo + " Stop Generation: " + generation);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			System.out.println("Something wrong StoppingGen file");
			System.exit(0);
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
			BufferedWriter mergeFUNbw = new BufferedWriter(new FileWriter(
					mergeFUNfile.getAbsoluteFile()));

			File mergeVARfile = new File(folder + "\\mergeVAR");
			BufferedWriter mergeVARbw = new BufferedWriter(new FileWriter(
					mergeVARfile.getAbsoluteFile()));

			if (!mergeFUNfile.exists()) {
				mergeFUNfile.createNewFile();
			}

			if (!mergeFUNfile.exists()) {
				mergeFUNfile.createNewFile();

			}

			for (int i = 0; i < FUNfiles.length; i++) {

				BufferedReader mergeFUNbr = new BufferedReader(new FileReader(
						FUNfiles[i].getAbsoluteFile()));
				String line;
				while ((line = mergeFUNbr.readLine()) != null) {
					mergeFUNbw.write(line);
					mergeFUNbw.write("\n");
				}
				mergeFUNbr.close();

			}
			mergeFUNbw.close();

			for (int i = 0; i < VARfiles.length; i++) {

				BufferedReader mergeVARbr = new BufferedReader(new FileReader(
						VARfiles[i].getAbsoluteFile()));
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
}
