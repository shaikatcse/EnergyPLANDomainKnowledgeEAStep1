package reet.fbk.eu.jmetal.operators.crossover.DKRE;

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
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import reet.fbk.eu.jmetal.encodings.solutionType.BinaryIntAndRealSolutionType;
import reet.fbk.eu.jmetal.encodings.solutionType.BinaryIntSolutionType;
import reet.fbk.eu.jmetal.encodings.variable.BinaryInt;
import jmetal.operators.crossover.*;

/**
 * This class allows to apply a Single Point crossover operator using two parent
 * solutions.
 */
public class SinglePointCrossoverFavorMaximizationOfPP extends Crossover {
	/**
	 * Valid solution types to apply this operator
	 */
	private static final List VALID_TYPES = Arrays
			.asList(BinaryIntAndRealSolutionType.class, BinaryIntSolutionType.class);

	private Double singlePointCrossoverProbability_ = null;

	/**
	 * this variable store the status of the genes that need to be maximixed or
	 * minimized true means maximize, false means minimize
	 */

	private static boolean favorGenes[] = new boolean[] { false, false, false,
			true };

	/**
	 * Constructor Creates a new instance of the single point crossover operator
	 */
	public SinglePointCrossoverFavorMaximizationOfPP(
			HashMap<String, Object> parameters) {
		super(parameters);
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
	public Solution[] doCrossover(double probability, Solution parent1,
			Solution parent2) throws JMException {
		Solution[] offSpring = new Solution[2];
		offSpring[0] = new Solution(parent1);
		offSpring[1] = new Solution(parent2);
		try {
			if (PseudoRandom.randDouble() < probability) {
				if ((parent1.getType().getClass() == BinaryIntAndRealSolutionType.class))

				{

					// checks how many BinaryInt and Real variable are in the
					// solution parent

					SolutionType type_ = parent1.getType();
					int numberOfBinaryIntVariable = ((BinaryIntAndRealSolutionType) type_)
							.getintVariables();
					int numberOfRealVariable = ((BinaryIntAndRealSolutionType) type_)
							.getRealVariables();

					// each variable will be checked with favorGenes array
					// this crossover will be only apply to BinaryInt part
					for (int i = 0; i < parent1.getDecisionVariables().length; i++) {
						if (parent1.getDecisionVariables()[i].getClass() == BinaryInt.class) {
							if (favorGenes[i] == true) {
								BinaryInt off[] = doMaximizeCrossoverInGene(
										(BinaryInt) parent1
												.getDecisionVariables()[i],
										(BinaryInt) parent2
												.getDecisionVariables()[i]);

								offSpring[0].getDecisionVariables()[i] = off[0]
										.deepCopy();

								offSpring[1].getDecisionVariables()[i] = off[1]
										.deepCopy();

							} else {
								// usual crossover
								BinaryInt off[] = doUsualCrossoverInGene(
										(BinaryInt) parent1
												.getDecisionVariables()[i],
										(BinaryInt) parent2
												.getDecisionVariables()[i]);

								offSpring[0].getDecisionVariables()[i] = off[0]
										.deepCopy();
								offSpring[1].getDecisionVariables()[i] = off[1]
										.deepCopy();
							}
						}
					}
					// decode the result
					// 7. Decode the results
					for (int i = 0; i < offSpring[0].getDecisionVariables().length; i++) {
						if (parent1.getDecisionVariables()[i].getClass() == BinaryInt.class) {
							((BinaryInt) offSpring[0].getDecisionVariables()[i])
									.decode();
							if (((BinaryInt) offSpring[0]
									.getDecisionVariables()[i]).getValue() > ((BinaryInt) offSpring[0]
									.getDecisionVariables()[i]).getUpperBound()) {
								((BinaryInt) offSpring[0]
										.getDecisionVariables()[i])
										.setValue(((BinaryInt) offSpring[0]
												.getDecisionVariables()[i]).getUpperBound());
							} else if (((BinaryInt) offSpring[0]
									.getDecisionVariables()[i]).getValue() < ((BinaryInt) offSpring[0]
									.getDecisionVariables()[i]).getLowerBound()) {
								((BinaryInt) offSpring[0]
										.getDecisionVariables()[i])
										.setValue(((BinaryInt) offSpring[0]
												.getDecisionVariables()[i]).getLowerBound());

							}
							((BinaryInt) offSpring[1].getDecisionVariables()[i])
									.decode();
							if (((BinaryInt) offSpring[1]
									.getDecisionVariables()[i]).getValue() > ((BinaryInt) offSpring[1]
									.getDecisionVariables()[i]).getUpperBound()) {
								((BinaryInt) offSpring[1]
										.getDecisionVariables()[i])
										.setValue(((BinaryInt) offSpring[1]
												.getDecisionVariables()[i]).getUpperBound());
							} else if (((BinaryInt) offSpring[1]
									.getDecisionVariables()[i]).getValue() < ((BinaryInt) offSpring[1]
									.getDecisionVariables()[i]).getLowerBound()) {
								((BinaryInt) offSpring[1]
										.getDecisionVariables()[i])
										.setValue(((BinaryInt) offSpring[1]
												.getDecisionVariables()[i]).getLowerBound());

							}
						}
					}
				} // Binary or BinaryReal
			}
		} catch (ClassCastException e1) {
			Configuration.logger_
					.severe("SinglePointCrossover.doCrossover: Cannot perfom "
							+ "SinglePointCrossover");
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".doCrossover()");
		}
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
		offSpring = doCrossover(singlePointCrossoverProbability_, parents[0],
				parents[1]);

		// -> Update the offSpring solutions
		for (int i = 0; i < offSpring.length; i++) {
			offSpring[i].setCrowdingDistance(0.0);
			offSpring[i].setRank(0);
		}
		return offSpring;
	} // execute

	public BinaryInt[] doMaximizeCrossoverInGene(BinaryInt gene1,
			BinaryInt gene2) {

		BinaryInt[] offSpring = new BinaryInt[2];
		// 1. Calculate the point to make the crossover
		int crossoverPoint = PseudoRandom.randInt(0,
				gene1.getNumberOfBits() - 1);

		// 2. calculate which one is better until crossover point
		BinaryInt g1 = new BinaryInt(gene1.getNumberOfBits() - 1
				- crossoverPoint, 0, (int) Math.pow(2, gene1.getNumberOfBits()
				- 1 - crossoverPoint));
		BinaryInt g2 = new BinaryInt(gene1.getNumberOfBits() - 1
				- crossoverPoint, 0, (int) Math.pow(2, gene1.getNumberOfBits()
				- 1 - crossoverPoint));

		g1.bits_.clear();
		g2.bits_.clear();

		for (int i = crossoverPoint + 1; i < gene1.getNumberOfBits(); i++) {
			if (gene1.bits_.get(i)) {
				g1.bits_.set(i - crossoverPoint - 1);
			}
			if (gene2.bits_.get(i)) {
				g2.bits_.set(i - crossoverPoint - 1);
			}
		}

		g1.decode();
		g2.decode();

		BinaryInt offSpring1, offSpring2;

		if (g1.getValue() > g2.getValue()) {
			// offspring
			offSpring1 = (BinaryInt) gene1.deepCopy();
			offSpring2 = (BinaryInt) gene2.deepCopy();
		} else {
			offSpring1 = (BinaryInt) gene2.deepCopy();
			offSpring2 = (BinaryInt) gene1.deepCopy();

		}

		// 3. now do a single point crossover by taking 1st part from a
		// better parent

		for (int i = crossoverPoint; i >= 0; i--) {
			offSpring1.bits_.set(i, offSpring2.bits_.get(i));

		}
		// copy to main gene
		offSpring1.decode();
		offSpring[0] = (BinaryInt) offSpring1.deepCopy();

		/*
		 * // 4. the other offspring comes as usual crossoverPoint =
		 * PseudoRandom.randInt(0, gene1.getNumberOfBits() - 1);
		 * 
		 * if (g1.getValue() < g2.getValue()) { // offspring offSpring1 =
		 * (BinaryInt) gene1.deepCopy(); offSpring2 = (BinaryInt)
		 * gene2.deepCopy(); } else { offSpring1 = (BinaryInt) gene2.deepCopy();
		 * offSpring2 = (BinaryInt) gene1.deepCopy();
		 * 
		 * }
		 * 
		 * for (int i = crossoverPoint; i >= 0; i--) { offSpring1.bits_.set(i,
		 * offSpring2.bits_.get(i)); } // copy to main gene offSpring1.decode();
		 * offSpring[1] = (BinaryInt) offSpring1.deepCopy();
		 */

		// now do the same for another crossover point
		
		// 1. Calculate the point to make the crossover
		crossoverPoint = PseudoRandom.randInt(0,
				gene1.getNumberOfBits() - 1);

		// 2. calculate which one is better until crossover point
		BinaryInt ge1 = new BinaryInt(gene1.getNumberOfBits() - 1
				- crossoverPoint, 0, (int) Math.pow(2, gene1.getNumberOfBits()
				- 1 - crossoverPoint));
		BinaryInt ge2 = new BinaryInt(gene1.getNumberOfBits() - 1
				- crossoverPoint, 0, (int) Math.pow(2, gene1.getNumberOfBits()
				- 1 - crossoverPoint));

		ge1.bits_.clear();
		ge2.bits_.clear();

		for (int i = crossoverPoint + 1; i < gene1.getNumberOfBits(); i++) {
			if (gene1.bits_.get(i)) {
				ge1.bits_.set(i - crossoverPoint - 1);
			}
			if (gene2.bits_.get(i)) {
				ge2.bits_.set(i - crossoverPoint - 1);
			}
		}

		ge1.decode();
		ge2.decode();

		BinaryInt offS1, offS2;

		if (ge1.getValue() > ge2.getValue()) {
			// offspring
			offS1 = (BinaryInt) gene1.deepCopy();
			offS2 = (BinaryInt) gene2.deepCopy();
		} else {
			offS1 = (BinaryInt) gene2.deepCopy();
			offS2 = (BinaryInt) gene1.deepCopy();

		}

		// 3. now do a single point crossover by taking 1st part from a
		// better parent

		for (int i = crossoverPoint; i >= 0; i--) {
			offS1.bits_.set(i, offS2.bits_.get(i));

		}
		// copy to main gene
		offS1.decode();
		offSpring[1] = (BinaryInt) offS1.deepCopy();

		// retune
		return offSpring;
	}

	public BinaryInt[] doUsualCrossoverInGene(BinaryInt gene1, BinaryInt gene2) {
		BinaryInt[] offSpring = new BinaryInt[2];
		// 1. Calculate the point to make the crossover
		int crossoverPoint = PseudoRandom.randInt(0,
				gene1.getNumberOfBits() - 1);

		BinaryInt offSpring1, offSpring2;
		offSpring1 = (BinaryInt) gene1.deepCopy();
		offSpring2 = (BinaryInt) gene2.deepCopy();

		for (int i = crossoverPoint; i < offSpring1.getNumberOfBits(); i++) {
			boolean swap = offSpring1.bits_.get(i);
			offSpring1.bits_.set(i, offSpring2.bits_.get(i));
			offSpring2.bits_.set(i, swap);
		}

		offSpring[0] = offSpring1;
		offSpring[1] = offSpring2;
		return offSpring;
	}

} // SinglePointCrossover

