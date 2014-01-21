package reet.fbk.eu.OprimizeEnergyPLAN.jmetal.operators.crossover.RE;

//SinglePointCrossover.java
//
//Author:
//   Antonio J. Nebro <antonio@lcc.uma.es>
//   Juan J. Durillo <durillo@lcc.uma.es>
//
//Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU Lesser General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

import jmetal.core.Solution;
import jmetal.core.SolutionType;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.encodings.variable.Real;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.wrapper.XReal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import reet.fbk.eu.OprimizeEnergyPLAN.jmetal.encodings.solutionType.BinaryIntSolutionType;
import reet.fbk.eu.OprimizeEnergyPLAN.jmetal.encodings.variable.BinaryInt;
import jmetal.operators.crossover.*;
import reet.fbk.eu.OprimizeEnergyPLAN.jmetal.operators.crossover.RE.SinglePointCrossoverForRes;
import reet.fbk.eu.OprimizeEnergyPLAN.jmetal.operators.crossover.DKRE.SinglePointCrossoverFavorMaximizationOfPP;
import reet.fbk.eu.OprimizeEnergyPLAN.jmetal.operators.crossover.DKRE.SinglePointCrossoverFavorMaximizationOfRes;
import reet.fbk.eu.OprimizeEnergyPLAN.jmetal.encodings.solutionType.BinaryIntAndRealSolutionType;

/**
 * This class allows to apply a Single Point crossover operator using two parent
 * solutions.
 */
/*
 * this class is the main class for the crossover. This class makes three
 * different instance of crossover (single-point crossover, Single point
 * crossover favor RE and Single point crossover favor PP) Accoring to the
 * probability one of the above crossover will be called. 50 percencent of the
 * times normal single point crossover will be called. Another 25% times
 * favoring RE and favoring PP will be called.
 */
public class SinglePointAndSBXCrossoverForRes extends Crossover {

	private static final double EPS = 1.0e-14;

	private static final double ETA_C_DEFAULT_ = 20.0;
	private double distributionIndex_ = ETA_C_DEFAULT_;

	private Double SBXCrossoverProbability_ = null;
	private Double singlePointCrossoverProbability_ = null;
	
	/**
	 * Valid solution types to apply this operator
	 */
	private static final List VALID_TYPES = Arrays
			.asList(BinaryIntAndRealSolutionType.class);

	private SinglePointCrossoverForRes singlePointCrossoverForRes;
	

	/**
	 * Constructor Creates a new instance of the single point crossover operator
	 */
	public SinglePointAndSBXCrossoverForRes(HashMap<String, Object> parameters) {
		super(parameters);
		singlePointCrossoverForRes = new SinglePointCrossoverForRes(
				parameters);

		if (parameters.get("SBXCrossoverProbability") != null)
			SBXCrossoverProbability_ = (Double) parameters
					.get("SBXCrossoverProbability");

		if (parameters.get("singlePointCrossoverProbability") != null)
			singlePointCrossoverProbability_ = (Double) parameters
					.get("singlePointCrossoverProbability");
	} // SinglePointCrossover

	/**
	 * Constructor Creates a new instance of the single point crossover operator
	 */
	// public SinglePointCrossover(Properties properties) {
	// this();
	// } // SinglePointCrossover

	Solution[] doSBXCrossover(double realProbability, Solution parent1,
			Solution parent2) throws JMException {

		Solution[] offSpring = new Solution[2];

		offSpring[0] = new Solution(parent1);
		offSpring[1] = new Solution(parent2);

		// SBX crossover
		double rand;
		double y1, y2, yL, yu;
		double c1, c2;
		double alpha, beta, betaq;
		double valueX1, valueX2;
		/*
		 * XReal x1 = new XReal(parent1); XReal x2 = new XReal(parent2); XReal
		 * offs1 = new XReal(offSpring[0]); XReal offs2 = new
		 * XReal(offSpring[1]);
		 */

		int numberOfVariables = parent1.getDecisionVariables().length;

		if (PseudoRandom.randDouble() <= realProbability) {
			for (int i = 0; i < numberOfVariables; i++) {
				if (parent1.getDecisionVariables()[i].getClass() == Real.class) {
					valueX1 = parent1.getDecisionVariables()[i].getValue();
					valueX2 = parent2.getDecisionVariables()[i].getValue();
					if (PseudoRandom.randDouble() <= 0.5) {
						if (java.lang.Math.abs(valueX1 - valueX2) > EPS) {

							if (valueX1 < valueX2) {
								y1 = valueX1;
								y2 = valueX2;
							} else {
								y1 = valueX2;
								y2 = valueX1;
							} // if

							yL = parent1.getDecisionVariables()[i]
									.getLowerBound();
							yu = parent1.getDecisionVariables()[i]
									.getUpperBound();
							rand = PseudoRandom.randDouble();
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
							} // if

							c1 = 0.5 * ((y1 + y2) - betaq * (y2 - y1));
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

							c2 = 0.5 * ((y1 + y2) + betaq * (y2 - y1));

							if (c1 < yL)
								c1 = yL;

							if (c2 < yL)
								c2 = yL;

							if (c1 > yu)
								c1 = yu;

							if (c2 > yu)
								c2 = yu;

							if (PseudoRandom.randDouble() <= 0.5) {
								// offs1.setValue(i, c2);
								offSpring[0].getDecisionVariables()[i]
										.setValue(c2);
								// offs2.setValue(i, c1);
								offSpring[1].getDecisionVariables()[i]
										.setValue(c1);
							} else {
								// offs1.setValue(i, c1);
								offSpring[0].getDecisionVariables()[i]
										.setValue(c1);
								// offs2.setValue(i, c2);
								offSpring[1].getDecisionVariables()[i]
										.setValue(c2);
							} // if
						} // if
						else {
							// offs1.setValue(i, valueX1);
							offSpring[0].getDecisionVariables()[i]
									.setValue(valueX1);
							// offs2.setValue(i, valueX2);
							offSpring[1].getDecisionVariables()[i]
									.setValue(valueX2);

						} // if
					} // if
					else {
						// offs1.setValue(i, valueX2);
						offSpring[0].getDecisionVariables()[i]
								.setValue(valueX2);

						// offs2.setValue(i, valueX1);
						offSpring[1].getDecisionVariables()[i]
								.setValue(valueX1);

					} // else
				} // for
			} // if
		}
		return offSpring;
	}

	/**
	 * Perform the crossover operation.
	 * 
	 * @param probability
	 *            Crossover probability
	 * @param parent1
	 *            The first parent
	 * @param parent2
	 *            The second parent
	 * @return An array containig the two offsprings
	 * @throws JMException
	 */
	public Solution[] doCrossover( Solution parent1,
			Solution parent2) throws JMException {

		Solution[] offSpring;
		// do generalized single-point crossover on parents
		Solution[] binaryIntOffSpring;
		binaryIntOffSpring = singlePointCrossoverForRes.doCrossover(
				singlePointCrossoverProbability_, parent1, parent2);

		// do SBX crossover on real part on parents
		offSpring = doSBXCrossover(SBXCrossoverProbability_,
				binaryIntOffSpring[0], binaryIntOffSpring[1]);

		return offSpring;
	}// doCrossover

	/**
	 * Executes the operation
	 * 
	 * @param object
	 *            An object containing an array of two solutions
	 * @return An object containing an array with the offSprings
	 * @throws JMException
	 */
	public Object execute(Object object) throws JMException {
		Solution[] parents = (Solution[]) object;

		if (!(VALID_TYPES.contains(parents[0].getType().getClass()) && VALID_TYPES
				.contains(parents[1].getType().getClass()))) {

			Configuration.logger_
					.severe("SinglePointCrossover.execute: the solutions "
							+ "are not of the right type. The type should be 'Binary' or 'Int', but "
							+ parents[0].getType() + " and "
							+ parents[1].getType() + " are obtained");

			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if

		if (parents.length < 2) {
			Configuration.logger_
					.severe("SinglePointCrossover.execute: operator "
							+ "needs two parents");
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		}

		Solution[] offSpring;
		// offSpring = doCrossover(crossoverProbability_, parents[0],
		// parents[1]);

		offSpring = doCrossover( parents[0], parents[1]);

		// -> Update the offSpring solutions
		for (int i = 0; i < offSpring.length; i++) {
			offSpring[i].setCrowdingDistance(0.0);
			offSpring[i].setRank(0);
		}
		return offSpring;
	} // execute
} // SinglePointCrossover
