package reet.fbk.eu.jmetal.initialization;

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
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.RandomGenerator;
//import reet.fbk.eu.jmetal.operators.mutation.MutationFactory;
import jmetal.operators.mutation.MutationFactory;

import java.util.logging.Logger;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;

public class GenerateInitialIndividual {

	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object

	public static void main(String[] args) throws JMException,
			SecurityException, IOException, ClassNotFoundException {

		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAII_main.log");
		logger_.addHandler(fileHandler_);

		Problem problem; // The problem to solve
		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		Operator selection; // Selection operator

		HashMap parameters; // Operator parameters

		QualityIndicator indicators; // Object to get quality indicators

		/*long seed[] = { 545454, 565656, 455885, 245454, 714451, 752453, 125742,
				455411, 447551, 844545 };*/
		
		//2nd list
		long seed[] = { 144759,
						271439,
						445964,
						494817,
						530563,
						724859,
						746153,
						747584,
						866309,
						938562};

		int numberOfRun = 10;
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
			int populationSize = 100;
			SolutionSet population;

			MatlabProxyFactory factory;
			MatlabProxy proxy;

			factory = new MatlabProxyFactory();
			try {
				proxy = factory.getProxy();

				DKInitialization dkini = new DKInitialization(problem,
						favorGenesforRE, favorGenesforConventionalPP,
						populationSize, 6.0, 3, 4, 200, proxy);

				population = dkini.doDKInitialization();
			} catch (MatlabConnectionException e) {
				throw new JMException("Matlab connection problem");
			} catch (MatlabInvocationException e) {
				throw new JMException("Matlab function invocation problem");
			}

			population
					.printVariablesToFile("InitializationResults/InitIndividualWithSI/InitIndv_seed_"
							+ seed[i]);

		}
	}
}
