package reet.fbk.eu.jmetal.operators.crossover;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.operators.crossover.Crossover;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.wrapper.XReal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * This class allows to apply a SBX crossover operator using two parent
 * solutions.
 */
public class SBXIntegerCrossoverV2 extends Crossover {
	/**
	 * EPS defines the minimum difference allowed between real values
	 */
	private static final double EPS = 1.0e-14;

	private static final double ETA_C_DEFAULT_ = 20.0;
	private Double crossoverProbability_ = 0.9;
	private double distributionIndex_ = ETA_C_DEFAULT_;

	/**
	 * Valid solution types to apply this operator
	 */
	private static final List VALID_TYPES = Arrays.asList(
			RealSolutionType.class, ArrayRealSolutionType.class);

	/**
	 * Constructor Create a new SBX crossover operator whit a default index
	 * given by <code>DEFAULT_INDEX_CROSSOVER</code>
	 */
	public SBXIntegerCrossoverV2(HashMap<String, Object> parameters) {
		super(parameters);

		if (parameters.get("probability") != null)
			crossoverProbability_ = (Double) parameters.get("probability");
		if (parameters.get("distributionIndex") != null)
			distributionIndex_ = (Double) parameters.get("distributionIndex");
	} // SBXCrossover

	/**
	 * Perform the crossover operation.
	 * 
	 * @param probability
	 *            Crossover probability
	 * @param parent1
	 *            The first parent
	 * @param parent2
	 *            The second parent
	 * @return An array containing the two offsprings
	 */
	public Solution[] doCrossover(double probability, Solution parent1,
			Solution parent2) throws JMException {

		Solution[] offSpring = new Solution[2];

		offSpring[0] = new Solution(parent1);
		offSpring[1] = new Solution(parent2);

		int i;
		double rand;
		double y1, y2, yL, yu;
		double c1, c2;
		double alpha, beta, betaq;
		double valueX1, valueX2;
		XReal x1 = new XReal(parent1);
		XReal x2 = new XReal(parent2);
		XReal offs1 = new XReal(offSpring[0]);
		XReal offs2 = new XReal(offSpring[1]);

		// parent1.getDecisionVariables()[0]).getvalue;

		int numberOfVariables = x1.getNumberOfDecisionVariables();

		if (PseudoRandom.randDouble() <= probability) {
			for (i = 0; i < numberOfVariables; i++) {
				valueX1 = parent1.getDecisionVariables()[i].getValue();
				valueX2 = parent2.getDecisionVariables()[i].getValue();
				if (PseudoRandom.randDouble() <= 1.0 /* by shaikat, should be 0.5 */) {
					if (java.lang.Math.abs(valueX1 - valueX2) > EPS) {

						if (valueX1 < valueX2) {
							y1 = valueX1;
							y2 = valueX2;
						} else {
							y1 = valueX2;
							y2 = valueX1;
						} // if

						yL = parent1.getDecisionVariables()[i].getLowerBound();
						yu = parent1.getDecisionVariables()[i].getUpperBound();
						rand = PseudoRandom.randDouble();
						// rand = 0.99999999;
						beta = 1.0 + (2.0 * (y1 - yL) / (y2 - y1));
						alpha = 2.0 - java.lang.Math.pow(beta,
								-(distributionIndex_ + 1.0));

						if (rand <= (1.0 / alpha)) {
							betaq = java.lang.Math.pow((rand * alpha),
									(1.0 / (distributionIndex_ + 1.0)));
						} else {
							betaq = java.lang.Math.pow((1.0 / (2.0 - rand
									* alpha)),
									(1.0 / (distributionIndex_ + 1.0)));
						}

						double betaStep = 2 / (y2 - y1);
						double betaInt;
						if ((y2 - y1) % 2 == 0.0) {
							betaInt = Math.round(betaq / betaStep) * betaStep;
						} else {
							betaInt = Math.floor(betaq / betaStep) * betaStep
									+ betaStep / 2;
						}

						c1 = 0.5 * ((y1 + y2) - betaInt * (y2 - y1));
						
						double temp1 = Math.round(0.5 * ((y1 + y2) - betaq * (y2 - y1)));
						double temp2 = 0.5 * ((y1 + y2) - betaInt * (y2 - y1));
						
						if(Math.abs(temp1 - temp2)>0.005 ){
							System.out.println("First part:");
							System.out.println("SBX "+temp1);
							System.out.println("SBXInt "+temp2);
							System.out.println("rand :" +rand);
							System.out.println("P1 :" + y1);
							System.out.println("P2 :" + y2);
							System.out.println("UB :" + yu);
							System.out.println("LB :" + yL);
						}
						
						
						//System.out.println("SBX : C1: "  +  0.5 * ((y1 + y2) - betaq * (y2 - y1)));
						//System.out.println("SBXInt : C1: "  +  0.5 * ((y1 + y2) - betaInt * (y2 - y1)));
						

						// @SuppressWarnings("unused")
						// int average;

						/*
						 * if (((int) y1 + (int) y2) % 2.0 == 0) { average =
						 * ((int) y1 + (int) y2) / 2; } else { int randForAvg =
						 * PseudoRandom.randInt(0, 1); if (randForAvg == 0)
						 * average = (int) Math .ceil(((int) y1 + (int) y2) /
						 * 2.0); else average = (int) Math .floor(((int) y1 +
						 * (int) y2) / 2.0); }
						 */

						/*
						 * average = ((int) y1 + (int) y2) / 2; double[][]
						 * distribution; distribution =
						 * calculateTheProbabilityDistribution( (int) y1, (int)
						 * y2, beta);
						 * 
						 * int pos = Arrays.binarySearch(distribution[2], rand);
						 * if (pos <= 0) pos = -1
						 * (Arrays.binarySearch(distribution[2], rand) + 1);
						 * 
						 * if (pos >= distribution[2].length) pos = pos - 1;
						 * 
						 * c1 = average + 0.5 (-distribution[0][pos] * (y2 -
						 * y1));
						 */

						/*
						 * if (rand <= (1.0 / alpha)) { betaq =
						 * java.lang.Math.pow((rand * alpha), (1.0 /
						 * (distributionIndex_ + 1.0))); } else { betaq =
						 * java.lang.Math.pow((1.0 / (2.0 - rand alpha)), (1.0 /
						 * (distributionIndex_ + 1.0))); } // if
						 * 
						 * c1 = 0.5 * ((y1 + y2) - betaq * (y2 - y1)); beta =
						 * 1.0 + (2.0 * (yu - y2) / (y2 - y1)); alpha = 2.0 -
						 * java.lang.Math.pow(beta, -(distributionIndex_ +
						 * 1.0));
						 * 
						 * if (rand <= (1.0 / alpha)) { betaq =
						 * java.lang.Math.pow((rand * alpha), (1.0 /
						 * (distributionIndex_ + 1.0))); } else { betaq =
						 * java.lang.Math.pow((1.0 / (2.0 - rand alpha)), (1.0 /
						 * (distributionIndex_ + 1.0))); } // if
						 */

						
						beta = 1.0 + (2.0 * (yu - y2) / (y2 - y1));
						alpha = 2.0 - java.lang.Math.pow(beta,
								-(distributionIndex_ + 1.0));

						if (rand <= (1.0 / alpha)) {
							betaq = java.lang.Math.pow((rand * alpha),
									(1.0 / (distributionIndex_ + 1.0)));
						} else {
							betaq = java.lang.Math.pow((1.0 / (2.0 - rand
									* alpha)),
									(1.0 / (distributionIndex_ + 1.0)));
						} // if

						if ((y2 - y1) % 2 == 0.0) {
							betaInt = Math.round(betaq / betaStep) * betaStep;
						} else {
							betaInt = Math.floor(betaq / betaStep) * betaStep
									+ betaStep / 2;
						}
						c2 = 0.5*((y1+y2)+betaInt*(y2-y1));
						
						temp1 = Math.round(0.5 * ((y1 + y2) + betaq * (y2 - y1)));
						temp2 = 0.5 * ((y1 + y2) + betaInt * (y2 - y1));
						
						if(Math.abs(temp1 - temp2)>0.005 ){
							System.out.println("Second part:");
							System.out.println("SBX "+temp1);
							System.out.println("SBXInt "+temp2);
							System.out.println("rand :" +rand);
							System.out.println("P1 :" + y1);
							System.out.println("P2 :" + y2);
							System.out.println("UB :" + yu);
							System.out.println("LB :" + yL);
						}
						
						//System.out.println("SBX : C2: "  +  0.5 * ((y1 + y2) + betaq * (y2 - y1)));
						//System.out.println("SBXInt :C2: "  +  0.5 * ((y1 + y2) + betaInt * (y2 - y1)));
						
						
						/*distribution = calculateTheProbabilityDistribution(
								(int) y1, (int) y2, beta);

						pos = Arrays.binarySearch(distribution[2], rand);
						if (pos <= 0)
							pos = -1
									* (Arrays.binarySearch(distribution[2],
											rand) + 1);

						if (pos >= distribution[2].length)
							pos = pos - 1;

						c2 = average + 0.5 * (distribution[0][pos] * (y2 - y1));*/
						// c2 = 0.5 * ((y1 + y2) + betaq * (y2 - y1));

						if (c1 < yL)
							c1 = yL;

						if (c2 < yL)
							c2 = yL;

						if (c1 > yu)
							c1 = yu;

						if (c2 > yu)
							c2 = yu;

						if (PseudoRandom.randDouble() <= 0.5) {
							offSpring[0].getDecisionVariables()[i].setValue(c2);
							offSpring[1].getDecisionVariables()[i].setValue(c1);

							/*
							 * setValue(i, c2); offs2.setValue(i, c1);
							 */
						} else {

							offSpring[0].getDecisionVariables()[i].setValue(c1);
							offSpring[1].getDecisionVariables()[i].setValue(c2);

							/*
							 * offs1.setValue(i, c1); offs2.setValue(i, c2);
							 */
						} // if
					} else {

						offSpring[0].getDecisionVariables()[i]
								.setValue(valueX1);
						offSpring[1].getDecisionVariables()[i]
								.setValue(valueX2);
						/*
						 * offs1.setValue(i, valueX1); offs2.setValue(i,
						 * valueX2);
						 */
					} // if
				} else {

					offSpring[0].getDecisionVariables()[i].setValue(valueX2);
					offSpring[1].getDecisionVariables()[i].setValue(valueX1);
					/*
					 * offs1.setValue(i, valueX2); offs2.setValue(i, valueX1);
					 */
				} // if
			} // if
		} // if

		return offSpring;
	} // doCrossover

	public double[][] calculateTheProbabilityDistribution(int p1, int p2,
			double betaMax) {
		// p1 is smaller and p2 is larger
		double distance = p2 - p1;
		double betaStep = 1 / distance * 2.0;
		double betaStepFor1 = 1 / distance;
		int totalColumn;
		if ((int) distance % 2 == 0)
			totalColumn = (int) Math.floor((betaMax / betaStep));
		else
			totalColumn = (int) Math.floor((betaMax / betaStep)) + 1;

		double distribution[][] = new double[3][totalColumn + 1];

		double area = 1 - 0.5 * Math.pow(betaMax, -(distributionIndex_ + 1));

		if ((int) distance % 2 == 0) {
			for (int i = 0; i <= totalColumn; i++)
				distribution[0][i] = i * betaStep;
		} else {
			distribution[0][0] = 0.0;
			for (int i = 0; i < totalColumn; i++)
				distribution[0][i + 1] = (i * betaStep) + betaStepFor1;
		}
		if ((int) distance % 2 == 0) {
			distribution[1][0] = 0.5
					* Math.pow(distribution[0][0] + betaStep / 2,
							(distributionIndex_ + 1)) / area;
		} else {
			distribution[1][0] = 0.5
					* Math.pow(distribution[0][0] + betaStepFor1 / 2,
							(distributionIndex_ + 1)) / area;
		}
		distribution[2][0] = distribution[1][0];

		boolean trackBetaForFirstPart = true;
		if ((int) distance % 2 == 0) {
			for (int i = 1; i < totalColumn; i++) {

				/*
				 * if (i * betaStep <= 1.0 && trackBetaForFirstPart) {
				 * distribution[1][i] = 0.5 (Math.pow(distribution[0][i],
				 * (distributionIndex_ + 1)) - Math.pow( distribution[0][i - 1],
				 * (distributionIndex_ + 1))) / area; distribution[2][i] =
				 * distribution[1][i] + distribution[2][i - 1]; }
				 * 
				 * else if (i * betaStep > 1.0 && !trackBetaForFirstPart) {
				 * distribution[1][i] = 0.5 (-Math.pow(distribution[0][i],
				 * (-distributionIndex_ - 1)) + Math.pow( distribution[0][i -
				 * 1], (-distributionIndex_ - 1))) / area; distribution[2][i] =
				 * distribution[1][i] + distribution[2][i - 1];
				 * 
				 * } else { distribution[1][i] = 0.5 (Math.pow(1,
				 * (distributionIndex_ + 1)) - Math.pow(distribution[0][i - 1],
				 * (distributionIndex_ + 1)) + -Math.pow(distribution[0][i],
				 * (-distributionIndex_ - 1)) + Math.pow( 1,
				 * (-distributionIndex_ - 1))) / area; distribution[2][i] =
				 * distribution[1][i] + distribution[2][i - 1];
				 * trackBetaForFirstPart = false; }
				 */

				double localUb = distribution[0][i] + betaStep / 2;
				double localLb = distribution[0][i] - betaStep / 2;

				if (localLb < 1.0 && localUb <= 1.0) {
					distribution[1][i] = 0.5
							* (Math.pow(localUb, (distributionIndex_ + 1)) - Math
									.pow(localLb, (distributionIndex_ + 1)))
							/ area;
				} else if (localLb >= 1.0 && localUb > 1.0) {
					distribution[1][i] = 0.5
							* (-Math.pow(localUb, (-distributionIndex_ - 1)) + Math
									.pow(localLb, (-distributionIndex_ - 1)))
							/ area;
				} else if (localLb < 1.0 && localUb > 1.0) {
					distribution[1][i] = 0.5
							* (Math.pow(1, (distributionIndex_ + 1))
									- Math.pow(localLb,
											(distributionIndex_ + 1))
									+ -Math.pow(localUb,
											(-distributionIndex_ - 1)) + Math
										.pow(1, (-distributionIndex_ - 1)))
							/ area;
				} else if (localLb - 0.0 <= 0.000000001 && localUb > 0.0) {
					distribution[1][i] = 0.5
							* (1 - Math.pow(localUb, (-distributionIndex_ - 1)))
							/ area;
				}
				distribution[2][i] = distribution[1][i]
						+ distribution[2][i - 1];

			}
		} else {
			// when distance is odd
			for (int i = 1; i < totalColumn; i++) {
				if (i == 1) {
					double localUb = distribution[0][i] + betaStep / 2;
					double localLb = distribution[0][i] - betaStepFor1 / 2;
					distribution[1][i] = 0.5
							* (Math.pow(localUb, (distributionIndex_ + 1)) - Math
									.pow(localLb, (distributionIndex_ + 1)))
							/ area;
					distribution[2][i] = distribution[1][i]
							+ distribution[2][i - 1];
				} else {
					double localUb = distribution[0][i] + betaStep / 2;
					double localLb = distribution[0][i] - betaStep / 2;

					if (localLb < 1.0 && localUb <= 1.0) {
						distribution[1][i] = 0.5
								* (Math.pow(localUb, (distributionIndex_ + 1)) - Math
										.pow(localLb, (distributionIndex_ + 1)))
								/ area;
					} else if (localLb >= 1.0 && localUb > 1.0) {
						distribution[1][i] = 0.5
								* (-Math.pow(localUb, (-distributionIndex_ - 1)) + Math
										.pow(localLb, (-distributionIndex_ - 1)))
								/ area;
					} else if (localLb < 1.0 && localUb > 1.0) {
						distribution[1][i] = 0.5
								* (Math.pow(1, (distributionIndex_ + 1))
										- Math.pow(localLb,
												(distributionIndex_ + 1))
										+ -Math.pow(localUb,
												(-distributionIndex_ - 1)) + Math
											.pow(1, (-distributionIndex_ - 1)))
								/ area;
					} else if (localLb - 0.0 <= 0.000000001 && localUb > 0.0) {
						distribution[1][i] = 0.5
								* (1 - Math.pow(localUb,
										(-distributionIndex_ - 1))) / area;
					}
					distribution[2][i] = distribution[1][i]
							+ distribution[2][i - 1];
				}

			}
		}

		if (betaMax > 1.0) {
			distribution[1][totalColumn] = 0.5
					* (-Math.pow(betaMax, (-distributionIndex_ - 1)) + Math
							.pow(distribution[0][totalColumn] - betaStep / 2,
									(-distributionIndex_ - 1))) / area;
		} else {
			distribution[1][totalColumn] = 0.5
					* (Math.pow(betaMax, (distributionIndex_ + 1)) - Math.pow(
							distribution[0][totalColumn] - betaStep / 2,
							(distributionIndex_ + 1))) / area;
		}
		distribution[2][totalColumn] = distribution[1][totalColumn]
				+ distribution[2][totalColumn - 1];

		return distribution;
	}

	/**
	 * Executes the operation
	 * 
	 * @param object
	 *            An object containing an array of two parents
	 * @return An object containing the offSprings
	 */
	public Object execute(Object object) throws JMException {
		Solution[] parents = (Solution[]) object;

		if (parents.length != 2) {
			Configuration.logger_
					.severe("SBXCrossover.execute: operator needs two "
							+ "parents");
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if

		if (!(VALID_TYPES.contains(parents[0].getType().getClass()) && VALID_TYPES
				.contains(parents[1].getType().getClass()))) {
			Configuration.logger_.severe("SBXCrossover.execute: the solutions "
					+ "type " + parents[0].getType()
					+ " is not allowed with this operator");

			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if

		Solution[] offSpring;
		offSpring = doCrossover(crossoverProbability_, parents[0], parents[1]);

		// for (int i = 0; i < offSpring.length; i++)
		// {
		// offSpring[i].setCrowdingDistance(0.0);
		// offSpring[i].setRank(0);
		// }
		return offSpring;
	} // execute
} // SBXCrossover