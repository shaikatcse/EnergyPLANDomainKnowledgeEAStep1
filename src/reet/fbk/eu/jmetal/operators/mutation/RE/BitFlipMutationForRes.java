//  BitFlipMutation.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package reet.fbk.eu.jmetal.operators.mutation.RE;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.operators.mutation.Mutation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import reet.fbk.eu.jmetal.encodings.solutionType.BinaryIntAndRealSolutionType;
import reet.fbk.eu.jmetal.encodings.variable.BinaryInt;

/**
 * This class implements a bit flip mutation operator. NOTE: the operator is
 * applied to binary or integer solutions, considering the whole solution as a
 * single encodings.variable.
 */
public class BitFlipMutationForRes extends Mutation {
	/**
	 * Valid solution types to apply this operator
	 */
	private static final List VALID_TYPES = Arrays
			.asList(BinaryIntAndRealSolutionType.class);

	private Double bitFlipMutationProbability_ = null;

	/**
	 * Constructor Creates a new instance of the Bit Flip mutation operator
	 */
	public BitFlipMutationForRes(HashMap<String, Object> parameters) {
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
				for (int i = 0; i < solution.getDecisionVariables().length; i++) {
					if (solution.getDecisionVariables()[i].getClass() == BinaryInt.class)
						for (int j = 0; j < ((Binary) solution
								.getDecisionVariables()[i]).getNumberOfBits(); j++) {
							if (PseudoRandom.randDouble() < probability) {
								((Binary) solution.getDecisionVariables()[i]).bits_
										.flip(j);
							}
						}
				}
			}

			for (int i = 0; i < solution.getDecisionVariables().length; i++) {
				if (solution.getDecisionVariables()[i].getClass() == BinaryInt.class) {

					((BinaryInt) solution.getDecisionVariables()[i]).decode();
					if (((BinaryInt) solution.getDecisionVariables()[i])
							.getValue() > ((BinaryInt) solution
							.getDecisionVariables()[i]).getUpperBound()) {
						((BinaryInt) solution.getDecisionVariables()[i])
								.setValue(((BinaryInt) solution
										.getDecisionVariables()[i])
										.getUpperBound());
					} else if (((BinaryInt) solution.getDecisionVariables()[i])
							.getValue() < ((BinaryInt) solution
							.getDecisionVariables()[i]).getLowerBound()) {
						((BinaryInt) solution.getDecisionVariables()[i])
								.setValue(((BinaryInt) solution
										.getDecisionVariables()[i])
										.getLowerBound());

					}
				}
			}
		} // if
		catch (ClassCastException e1) {
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
							+ "'BinaryReal' or 'Int', but "
							+ solution.getType() + " is obtained");

			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if

		doMutation(bitFlipMutationProbability_, solution);
		return solution;
	} // execute
} // BitFlipMutation
