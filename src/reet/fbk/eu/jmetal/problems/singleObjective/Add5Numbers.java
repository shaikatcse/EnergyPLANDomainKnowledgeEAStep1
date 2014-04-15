package reet.fbk.eu.OprimizeEnergyPLAN.jmetal.problems.singleObjective;

//OneMax.java
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
//along with this program.  If not, see <http://www.gnu.org/licenses/>. * OneMax.java

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import reet.fbk.eu.OprimizeEnergyPLAN.jmetal.encodings.solutionType.BinaryIntAndRealSolutionType;
import reet.fbk.eu.OprimizeEnergyPLAN.jmetal.encodings.solutionType.BinaryIntSolutionType;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.JMException;

/**
 * Class representing problem OneMax. The problem consist of maximizing the
 * number of '1's in a binary string.
 */
public class Add5Numbers extends Problem {

	/**
	 * Creates a new OneMax problem instance
	 * 
	 * @param numberOfBits
	 *            Length of the problem
	 */
	public Add5Numbers(String solutionType, Integer numberOfVariables,
			int numberOfInt, int numberOfReal) {

	
		numberOfVariables_ = numberOfVariables;
		numberOfObjectives_ = 2;
		numberOfConstraints_ = 0;
		problemName_ = "Add5Number";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];
		for (int var = 0; var < numberOfVariables_; var++) {
			lowerLimit_[var] = 30;
			upperLimit_[var] = 300;
		}

		if (solutionType.compareTo("BinaryIntAndReal") == 0)
			solutionType_ = new BinaryIntAndRealSolutionType(this, numberOfInt, numberOfReal);
		else {
			System.out.println("OneMax: solution type " + solutionType
					+ " invalid");
			System.exit(-1);
		}

	} // OneMax

	/**
	 * Evaluates a solution
	 * 
	 * @param solution
	 *            The solution to evaluate
	 */
	public void evaluate(Solution solution) throws JMException {
		Variable[] decisionVariables = solution.getDecisionVariables();

		double Addsum = 0;
		double Subsum = 0;
		for (int var = 0; var < numberOfVariables_; var++) {
			Addsum += decisionVariables[var].getValue();
		}
		
		for (int var = 0; var < numberOfVariables_; var++) {
			Subsum -= decisionVariables[var].getValue();
		}
		

		// OneMax is a maximization problem: multiply by -1 to minimize
		//solution.setObjective(0, Math.abs(300 - sum));
		solution.setObjective(0, -1*Addsum);
		solution.setObjective(1, Math.abs(1000-Subsum));
		
	} // evaluate
} // OneMax
