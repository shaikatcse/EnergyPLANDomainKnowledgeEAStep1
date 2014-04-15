package reet.fbk.eu.jmetal.operators.mutation.Real;

/*this class simplly implements mutation that favoring RE based on
 * Normal distribution
 * 
 * 
 */

import jmetal.core.Solution;
import jmetal.core.SolutionType;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import reet.fbk.eu.jmetal.encodings.solutionType.BinaryIntAndRealSolutionType;
import reet.fbk.eu.jmetal.encodings.solutionType.BinaryIntSolutionType;
import reet.fbk.eu.jmetal.encodings.variable.BinaryInt;
import jmetal.operators.mutation.*;

import org.apache.commons.math3.distribution.NormalDistribution;

public class DKRealMutationFavorConventionalPP extends Mutation {

	/**
	 * The array will keep track on which gene the mutation will be applied true
	 * means apply the mutation to increase and false means apply mutation to
	 * decrease and null means do nothing {Off-Shore wind, on-shore wind, Solar,
	 * PP, coal, oil, ngas}
	 */

	private static Boolean favorGenes[] = new Boolean[] { false, false, false,
			null, true, null, false };

	private Double bitFlipMutationProbability_ = null;

	/**
	 * Valid solution types to apply this operator
	 */
	private static final List VALID_TYPES = Arrays
			.asList(RealSolutionType.class);

	/**
	 * Constructor Creates a new instance of the Bit Flip mutation operator
	 */
	public DKRealMutationFavorConventionalPP(HashMap<String, Object> parameters) {
		super(parameters);
		/*
		 * if (parameters.get("bitFlipMutationProbability") != null)
		 * bitFlipMutationProbability_ = (Double) parameters
		 * .get("bitFlipMutationProbability");
		 */
	}

	public static void main(String args[]) {
		NormalDistribution nd = new NormalDistribution(0, 1);
		System.out.println(nd.density(0.2));
	}

	/**
	 * Perform the mutation operation
	 * 
	 * @param probability
	 *            Mutation probability
	 * @param solution
	 *            The solution to mutate
	 * @throws JMException
	 */
	public void doMutation(double probability, Solution solution)
			throws JMException {
		try {
			if (solution.getType().getClass() == RealSolutionType.class) {

				for (int i = 0; i < solution.getDecisionVariables().length; i++) {
					// do mutation on all variables

					// check if the probability is greater than a given
					// probability

					if (PseudoRandom.randDouble() < probability) {
						try {
							if (favorGenes[i] == true) {

								double indvValue = solution
										.getDecisionVariables()[i].getValue();
								double newIndvValue = indvValue;
								
								double distLowerBound = indvValue;
								double distUpperBound = solution
										.getDecisionVariables()[i]
										.getUpperBound();
								if((distUpperBound - indvValue)>0.0){
									
								NormalDistribution nd = new NormalDistribution(
										indvValue,
										(distUpperBound - indvValue) / 3);

								double rand = PseudoRandom.randDouble();

								newIndvValue = nd
										.inverseCumulativeProbability(nd
												.cumulativeProbability(distLowerBound)
												+ rand
												* (nd.cumulativeProbability(distUpperBound) - nd
														.cumulativeProbability(distLowerBound)));
								}
								solution.getDecisionVariables()[i]
										.setValue(newIndvValue);
							}

							else {
								double indvValue = solution
										.getDecisionVariables()[i].getValue();
								
								double newIndvValue = indvValue;
								
								double distLowerBound = solution
										.getDecisionVariables()[i]
										.getLowerBound();
								double distUpperBound = indvValue;
								if ((indvValue - distLowerBound) > 0) {
									NormalDistribution nd = new NormalDistribution(
											indvValue,
											(indvValue - distLowerBound) / 3);

									double rand = PseudoRandom.randDouble();

									newIndvValue = nd
											.inverseCumulativeProbability(nd
													.cumulativeProbability(distLowerBound)
													+ rand
													* (nd.cumulativeProbability(distUpperBound) - nd
															.cumulativeProbability(distLowerBound)));
								}
								solution.getDecisionVariables()[i]
										.setValue(newIndvValue);
							}
						} catch (NullPointerException e) {
							continue;
						}

					} // if
				}
			}// else
		} catch (ClassCastException e1) {
			Configuration.logger_.severe("BitFlipMutation.doMutation: "
					+ "ClassCastException error" + e1.getMessage());
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".doMutation()");
		}
	} // doMutation

	/**
	 * Executes the operation
	 * 
	 * @param object
	 *            An object containing a solution to mutate
	 * @return An object containing the mutated solution
	 * @throws JMException
	 */
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution) object;

		if (!VALID_TYPES.contains(solution.getType().getClass())) {
			Configuration.logger_
					.severe("BitFlipMutation.execute: the solution "
							+ "is not of the right type. The type should be 'Binary', "
							+ "'BinaryReal ' or 'Int', but "
							+ solution.getType() + " is obtained");

			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if

		doMutation(bitFlipMutationProbability_, solution);
		return solution;
	} // execute
}