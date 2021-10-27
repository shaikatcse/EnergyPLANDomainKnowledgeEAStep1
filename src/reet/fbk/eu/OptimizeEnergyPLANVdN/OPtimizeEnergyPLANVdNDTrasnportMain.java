package reet.fbk.eu.OptimizeEnergyPLANVdN;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.logging.FileHandler;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.RandomGenerator;
import reet.fbk.eu.jmetal.operators.mutation.MutationFactory;
import reet.fbk.eu.OptimizeEnergyPLANAalborg.problem.EnergyPLANProblemAalborg;
import reet.fbk.eu.OptimizeEnergyPLANAalborg.problem.EnergyPLANProblemAalborg2Objectives;
import reet.fbk.eu.OptimizeEnergyPLANCIVIS.CEIS.Problem.EnergyPLANProblemCivisCEIS4D;
import reet.fbk.eu.OptimizeEnergyPLANCIVIS.CEIS.Problem.EnergyPLANProblemCivisCEIS4DWithTransport;
import reet.fbk.eu.OptimizeEnergyPLANCIVIS.CEIS.Problem.EnergyPLANProblemCivisCeisWithDH;
import reet.fbk.eu.OptimizeEnergyPLANCIVIS.CEdiS.Problem.EnergyPLANProblemCivisCEdiS;
import reet.fbk.eu.OptimizeEnergyPLANCIVIS.CEdiS.Problem.EnergyPLANProblemCivisCEdiS4D;
import reet.fbk.eu.OptimizeEnergyPLANCIVIS.metaheuristics.NSGAIIForDK;
//import reet.fbk.eu.jmetal.operators.mutation.MutationFactory;

import reet.fbk.eu.OptimizeEnergyPLANVdN.problem.EnergyPLANProblemVdN4DWithTransport;
import reet.fbk.eu.jmetal.metaheuristics.spea2.SPEA2ForDK;
import reet.fbk.eu.jmetal.metaheuristics.spea2.SPEA2ForDKandSCandSIforCEIS;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class OPtimizeEnergyPLANVdNDTrasnportMain {

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

		int numberOfRun = 3;
		for (int i = 0; i < numberOfRun; i++) {

			// PseudoRandom.setRandomGenerator(new RandomGenerator(seed[i]));

			indicators = null;

			//problems for 2 objectives
			problem=new EnergyPLANProblemVdN4DWithTransport("Real");
			
			//problem for 3 objectives
			//problem = new EnergyPLANProblemAalborg("Real");
			
			// decision variables
			// index - 0 -> PV Capacity
			// index - 1 -> oil boiler heat percentage
			// index - 2 -> nGas boiler heat percentage
			// index - 3 -> Biomass boiler heat percentage
			// index - 4 -> Biomass micro chp heat percentage
			// last percentage will go to individual HP percentage
			// index - 5 -> electric car percentage  
			// index - 6 -> FCEV car percentage
			
			//index -7 -> solar thermal percentage in oil boiler
			//index -8 -> solar thermal percentage in nGas boiler
			//index -9 -> solar thermal percentage in biomass boiler
			//index -10 -> solar thermal percentage in biomass CHP
			//index -11 -> solar thermal percentage in HP
				
			//                                       pv     oB    ngb   bB    bCHP   EV     FCEV   oS     nGS    bbS    bCHPs   HPS
			Boolean favorGenesforRE[] =            {true,  false, null, true, true,  true,  true,  true,  true,  true,  true,  true };
			Boolean favorGenesforConventionalPP[] ={false, null,  null, true, false, false, false, false, false, false, false, null };
			Boolean favorGenesforLFC[]=            {false, null,  null, null, true,  true,  true,  false, false, false, false, true};
			Boolean favorGenesforESD[] =           {true,  false, false,true, true,  true,  null,  true,  true,  true,  true,  true };
			
			parameters = new HashMap();
			parameters.put("favorGenesForRE", favorGenesforRE);
			parameters.put("favorGenesForCon", favorGenesforConventionalPP);
			parameters.put("favorGenesForLFC",favorGenesforLFC );
			parameters.put("favorGenesForESD",favorGenesforESD );

			//algorithm = new SPEA2ForDKandSCandSIforCEIS(problem, parameters);
			algorithm = new SPEA2ForDK(problem);
			
			// algorithm = new SPEA2ForDK(problem, seed[i],
			// "SPEA2_SBX_PolynomialMutation");

			// indicators = new QualityIndicator(problem,
			// "C:\\Users\\Nusrat\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\Results\\truePf\\mergefun.pf")
			// ;

			// Algorithm parameters
			
			algorithm.setInputParameter("populationSize", 150);
			algorithm.setInputParameter("maxEvaluations", 11250);
			
			
			// for spea2
			 algorithm.setInputParameter("archiveSize",150);

			// Mutation and Crossover for Real codification
			parameters = new HashMap();
			parameters.put("probability", 0.9);
			parameters.put("distributionIndex", 10.0);
			crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover",
					parameters);

			parameters = new HashMap();
			parameters.put("probability", 1.0 / problem.getNumberOfVariables());
			parameters.put("distributionIndex", 10.0);
			parameters.put("maximum generation", (int) algorithm.getInputParameter("maxEvaluations")/(int) algorithm.getInputParameter("populationSize")-1);
			
			
			
		
			parameters.put("favorGenesforRE", favorGenesforRE);
			parameters.put("favorGenesForConventioanlPP", favorGenesforConventionalPP);
			parameters.put("favorGenesForLFC",favorGenesforLFC );
			parameters.put("favorGenesForESD",favorGenesforESD );

			mutation = MutationFactory.getMutationOperator(
					"GeneralModifiedPolynomialMutationForEnergySystems", parameters);
			
			
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
			
			//int stopGen = ((Integer) algorithm
					//.getOutputParameter("stopGen")).intValue();
			//writeStoppingGenerationInFile(i, stopGen,
				//	"C:\\Users\\mahbub\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\CivisResults\\CEIS\\4Objectives");
			
			// Result messages
			logger_.info("Total execution time: " + estimatedTime + "ms");
			logger_.info("Variables values have been writen to file VAR");
			population.printFeasibleVAR("VdNResults\\VAR" + i);
			//population.printFeasibleVAR("VAR");
			logger_.info("Objectives values have been writen to file FUN");
			population.printFeasibleFUN("VdNResults\\FUN" + i);
			//population.printFeasibleFUN("FUN");
		}
	}
	
	public static void writeStoppingGenerationInFile(int runNo,  int generation,
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
			bw.write(runNo + " " + generation);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			System.out.println("Something wrong StoppingGen file");
			System.exit(0);
		}

	}
}
