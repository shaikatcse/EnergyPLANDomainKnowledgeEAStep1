package reet.fbk.eu.jmetal.operators.mutation.ModifiedPolynomial;

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
import jmetal.util.wrapper.XReal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import reet.fbk.eu.jmetal.encodings.solutionType.BinaryIntAndRealSolutionType;
import reet.fbk.eu.jmetal.encodings.solutionType.BinaryIntSolutionType;
import reet.fbk.eu.jmetal.encodings.variable.BinaryInt;
import jmetal.operators.mutation.*;

import org.apache.commons.math3.distribution.NormalDistribution;

/*
 * This cmutation will favor energy system dependency (ESD) 
 */
public class ModifiedPolynomialMutationFavorESD extends PolynomialMutation {

	/**
	 * The array will keep track on which gene the mutation will be applied true
	 * means apply the mutation to increase and false means apply mutation to
	 * decrease and null means do nothing {Off-Shore wind, on-shore wind, Solar,
	 * PP, coal, oil, ngas}
	 */
	/*This was used for 1st paper
	 * private static Boolean favorGenes[] = new Boolean[] { true, true, true,
			null, false, null, true };
	 */
	// private Double bitFlipMutationProbability_ = null;

	private Boolean  favorGenes[];
	private static final double ETA_M_DEFAULT_ = 20.0;
	private final double eta_m_ = ETA_M_DEFAULT_;

	private Double mutationProbability_ = null;
	private Double distributionIndex_ = eta_m_;

	/**
	 * Valid solution types to apply this operator
	 */
	private static final List VALID_TYPES = Arrays
			.asList(RealSolutionType.class);

	/**
	 * Constructor Creates a new instance of the Bit Flip mutation operator
	 */
	public ModifiedPolynomialMutationFavorESD(HashMap<String, Object> parameters, Boolean favorGenes[]) {
		super(parameters);
		this.favorGenes = favorGenes;
		if (parameters.get("probability") != null)
			mutationProbability_ = (Double) parameters.get("probability");
		if (parameters.get("distributionIndex") != null) {
			distributionIndex_ = (Double) parameters.get("distributionIndex");

		}
	}

	/*
	 * if (parameters.get("bitFlipMutationProbability") != null)
	 * bitFlipMutationProbability_ = (Double) parameters
	 * .get("bitFlipMutationProbability");
	 */

	/**
	 * Perform the mutation operation
	 * 
	 * @param probability
	 *            Mutation probability
	 * @param solution
	 *            The solution to mutate
	 * @throws JMException
	 */
	@SuppressWarnings("null")
	public void doMutation(double probability, Solution solution)
			throws JMException {

		double rnd, deltaU, deltaL, mut_pow, deltaq;
		double y, yl, yu, val, xy;
		XReal x = new XReal(solution);

		try {
			if (solution.getType().getClass() == RealSolutionType.class) {

				for (int i = 0; i < solution.getDecisionVariables().length; i++) {
					// do mutation on all variables

					// check if the probability is greater than a given
					// probability

					if (PseudoRandom.randDouble() < probability) {
						y = x.getValue(i);
						yl = x.getLowerBound(i);
						yu = x.getUpperBound(i);
						deltaL = (y - yl) / (yu - yl);
						deltaU = (yu - y) / (yu - yl);
						rnd = PseudoRandom.randDouble();
						mut_pow = 1.0 / (distributionIndex_ + 1.0);
						try {
							if (favorGenes[i] == true) {

								/*
								 * double indvValue = solution
								 * .getDecisionVariables()[i].getValue(); double
								 * newIndvValue = indvValue; double
								 * distLowerBound = indvValue; double
								 * distUpperBound = solution
								 * .getDecisionVariables()[i] .getUpperBound();
								 * if ((distUpperBound - indvValue) > 0.0) {
								 * NormalDistribution nd = new
								 * NormalDistribution( indvValue,
								 * (distUpperBound - indvValue) / 3);
								 * 
								 * double rand = PseudoRandom.randDouble();
								 * 
								 * newIndvValue = nd
								 * .inverseCumulativeProbability(nd
								 * .cumulativeProbability(distLowerBound) + rand
								 * (nd.cumulativeProbability(distUpperBound) -
								 * nd .cumulativeProbability(distLowerBound)));
								 * }
								 */
								xy = 1.0 - deltaU;
								val = 1
										- rnd
										+ rnd
										* (Math.pow(xy,
												(distributionIndex_ + 1.0)));
								deltaq = 1 - java.lang.Math.pow(val, mut_pow);

								y = y + deltaq * (yu - yl);
								if (y < yl)
									y = yl;
								if (y > yu)
									y = yu;
								x.setValue(i, y);

								// solution.getDecisionVariables()[i]
								// .setValue(newIndvValue);

							} else {
								/*
								 * double indvValue = solution
								 * .getDecisionVariables()[i].getValue(); double
								 * newIndvValue = indvValue; double
								 * distLowerBound = solution
								 * .getDecisionVariables()[i] .getLowerBound();
								 * double distUpperBound = indvValue; if
								 * ((indvValue - distLowerBound) > 0.0) {
								 * NormalDistribution nd = new
								 * NormalDistribution( indvValue, (indvValue -
								 * distLowerBound) / 3);
								 * 
								 * double rand = PseudoRandom.randDouble();
								 * 
								 * newIndvValue = nd
								 * .inverseCumulativeProbability(nd
								 * .cumulativeProbability(distLowerBound) + rand
								 * (nd.cumulativeProbability(distUpperBound) -
								 * nd .cumulativeProbability(distLowerBound)));
								 * } solution.getDecisionVariables()[i]
								 * .setValue(newIndvValue);
								 */

								xy = 1.0 - deltaL;
								val = rnd
										+ (1 - rnd)
										* (Math.pow(xy,
												(distributionIndex_ + 1.0)));
								deltaq = java.lang.Math.pow(val, mut_pow) - 1;

								y = y + deltaq * (yu - yl);
								if (y < yl)
									y = yl;
								if (y > yu)
									y = yu;
								x.setValue(i, y);

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

		doMutation(mutationProbability_, solution);
		return solution;
	} // execute
}