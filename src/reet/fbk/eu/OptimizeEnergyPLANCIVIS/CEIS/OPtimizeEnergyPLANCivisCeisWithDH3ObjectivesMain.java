package reet.fbk.eu.OptimizeEnergyPLANCIVIS.CEIS;


import java.io.IOException;
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
import reet.fbk.eu.jmetal.operators.mutation.MutationFactory;


import reet.fbk.eu.OptimizeEnergyPLANCIVIS.CEIS.Problem.EnergyPLANProblemCivisCeisWithDH;
import reet.fbk.eu.OptimizeEnergyPLANCIVIS.metaheuristics.NSGAIIForDK;
//import reet.fbk.eu.jmetal.operators.mutation.MutationFactory;


import reet.fbk.eu.OptimizeEnergyPLANCIVIS.metaheuristics.NSGAIIForDKTest;

import java.util.logging.Logger;

public class OPtimizeEnergyPLANCivisCeisWithDH3ObjectivesMain {

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
			problem=new EnergyPLANProblemCivisCeisWithDH("Real");
			
			//problem for 3 objectives
			//problem = new EnergyPLANProblemAalborg("Real");

			algorithm = new NSGAIIForDK(problem);
			// algorithm = new SPEA2ForDK(problem, seed[i],
			// "SPEA2_SBX_PolynomialMutation");

			// indicators = new QualityIndicator(problem,
			// "C:\\Users\\Nusrat\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\Results\\truePf\\mergefun.pf")
			// ;

			// Algorithm parameters
			algorithm.setInputParameter("populationSize", 100);
			algorithm.setInputParameter("maxEvaluations", 10000);
			// for spea2
			// algorithm.setInputParameter("archiveSize",100);

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
			
			/*
			 * 0-> chp2 1 -> hp2 2 -> pv capacity 
			 */
			
			Boolean favorGenesforRE[] ={true, true, true };
			Boolean favorGenesforConventionalPP[] ={null,null, false };
			Boolean favorGenesforLFC[]={true, true, false};
			
			parameters.put("favorGenesforRE", favorGenesforRE);
			parameters.put("favorGenesForConventioanlPP", favorGenesforConventionalPP);
			parameters.put("favorGenesForLFC",favorGenesforLFC );
							
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

			// Result messages
			logger_.info("Total execution time: " + estimatedTime + "ms");
			logger_.info("Variables values have been writen to file VAR");
			population.printFeasibleVAR("CivisResults\\CEIS\\DH_3Objectives\\VAR" + i);
			//population.printFeasibleVAR("VAR");
			logger_.info("Objectives values have been writen to file FUN");
			population.printFeasibleFUN("CivisResults\\CEIS\\DH_3Objectives\\FUN" + i);
			//population.printFeasibleFUN("FUN");
		}
	}
}
