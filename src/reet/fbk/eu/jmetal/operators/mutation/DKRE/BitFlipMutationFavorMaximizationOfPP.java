package reet.fbk.eu.jmetal.operators.mutation.DKRE;

//BitFlipMutation.java
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
import jmetal.operators.mutation.*;

/**
 * This class implements a bit flip mutation operator. NOTE: the operator is
 * applied to binary or integer solutions, considering the whole solution as a
 * single encodings.variable.
 */
public class BitFlipMutationFavorMaximizationOfPP extends Mutation {
	/**
	 * Valid solution types to apply this operator
	 */
	private static final List VALID_TYPES = Arrays
			.asList(BinaryIntSolutionType.class);

	private Double bitFlipMutationProbability_ = null;

	private static boolean favorGenes[] = new boolean[] { false, false, false,
			true };

	/**
	 * Constructor Creates a new instance of the Bit Flip mutation operator
	 */
	public BitFlipMutationFavorMaximizationOfPP(
			HashMap<String, Object> parameters) {
		super(parameters);
		if (parameters.get("bitFlipMutationProbability") != null)
			bitFlipMutationProbability_ = (Double) parameters
					.get("bitFlipMutationProbability");
	} // BitFlipMutation

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
			if (solution.getType().getClass() == BinaryIntAndRealSolutionType.class) {

				// check how many BinaryInt and Real variable in the
				// solution Parent
				/*SolutionType type_ = solution.getType();
				int numberOfBinaryIntVariable = ((BinaryIntAndRealSolutionType) type_)
						.getintVariables();
				int numberOfRealVariable = ((BinaryIntAndRealSolutionType) type_)
						.getRealVariables();*/
				
				for (int i = 0; i < solution.getDecisionVariables().length; i++) {
					// do mutation on all variables
					// for (int j = 0; j < ((Binary)
					// solution.getDecisionVariables()[i]).getNumberOfBits();
					// j++) {

					if (solution.getDecisionVariables()[i].getClass() == BinaryInt.class) {

					// check if the probability is greater than a given
					// probability
					
					
					if (PseudoRandom.randDouble() < probability) {

						
						if (favorGenes[i] == true) {
							/*
							 * int position = (int) Math.ceil(Math
							 * .sqrt(PseudoRandom.randInt(0, (int) Math
							 * .pow((((BinaryInt) solution
							 * .getDecisionVariables()[i]) .getNumberOfBits()),
							 * 2)))) + 1;
							 */

							int numberOfBits = (((BinaryInt) solution
									.getDecisionVariables()[i])
									.getNumberOfBits());

							// minimizing
							// 1/log2(x)*10
							// int position= (int) Math.floor( 1/
							// (Math.log(random_number)/Math.log(2)) *10 ) ;

							int position, random_number, attempt = 0;

							// check if in the "position", the bit is 0.
							// otherwise try to get another position
							// and this attempt will do for 50 times, because if
							// all bits are '1', then it will put into infinite
							// loop

							do {
								++attempt;
								random_number = PseudoRandom.randInt(1,
										((int) Math.pow(2, numberOfBits) - 1));

								position = (int) Math.floor(Math
										.log(random_number) / Math.log(2));

							} while (((BinaryInt) solution
									.getDecisionVariables()[i]).bits_
									.get(position) != false
									|| attempt <= 50);

							((BinaryInt) solution.getDecisionVariables()[i]).bits_
									.flip(position);

						} else {
							int position = PseudoRandom.randInt(0,
									((BinaryInt) solution
											.getDecisionVariables()[i])
											.getNumberOfBits() - 1);
							((BinaryInt) solution.getDecisionVariables()[i]).bits_
									.flip(position);

						}
					}
				}
				}

				for (int i = 0; i < solution.getDecisionVariables().length; i++) {

					if (solution.getDecisionVariables()[i].getClass() == BinaryInt.class) {

						((BinaryInt) solution.getDecisionVariables()[i])
								.decode();
						if (((BinaryInt) solution.getDecisionVariables()[i]).getValue() > ((BinaryInt) solution
								.getDecisionVariables()[i]).getUpperBound()) {
							((BinaryInt) solution.getDecisionVariables()[i])
									.setValue(((BinaryInt) solution
											.getDecisionVariables()[i]).getUpperBound());
						} else if (((BinaryInt) solution.getDecisionVariables()[i]).getValue() < ((BinaryInt) solution
								.getDecisionVariables()[i]).getLowerBound()) {
							((BinaryInt) solution.getDecisionVariables()[i])
									.setValue(((BinaryInt) solution
											.getDecisionVariables()[i]).getLowerBound());

						}

					}

					// ((Binary) solution.getDecisionVariables()[i]).decode();
				}
			} // if
				// else
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
} // BitFlipMutation
