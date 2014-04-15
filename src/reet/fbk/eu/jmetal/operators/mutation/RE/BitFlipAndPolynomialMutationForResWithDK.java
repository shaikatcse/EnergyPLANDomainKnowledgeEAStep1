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
import jmetal.encodings.variable.Real;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.wrapper.XReal;
import jmetal.operators.mutation.Mutation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import reet.fbk.eu.jmetal.encodings.solutionType.BinaryIntAndRealSolutionType;
import reet.fbk.eu.jmetal.encodings.variable.BinaryInt;

/**
 * This class implements a bit flip mutation operator.
 * NOTE: the operator is applied to binary or integer solutions, considering the
 * whole solution as a single encodings.variable.
 */
public class BitFlipAndPolynomialMutationForResWithDK extends Mutation {
  /**
   * Valid solution types to apply this operator 
   */
  private static final List VALID_TYPES = Arrays.asList( BinaryIntAndRealSolutionType.class) ;
  
  private static final double ETA_M_DEFAULT_ = 20.0;
	private final double eta_m_=ETA_M_DEFAULT_;
	private double distributionIndex_ = eta_m_;
	
  private Double bitFlipMutationProbability_ = null ;
  private Double polynomialMutationProbability_ = null;
	
  private GeneralBitFlipMutationForRes generalBitFlipMutation; 
  /**
	 * Constructor
	 * Creates a new instance of the Bit Flip mutation operator
	 */
	public BitFlipAndPolynomialMutationForResWithDK(HashMap<String, Object> parameters) {
		super(parameters) ;
  	if (parameters.get("bitFlipMutationProbability") != null)
  		bitFlipMutationProbability_ = (Double) parameters.get("bitFlipMutationProbability") ;  		
  	if (parameters.get("polynomialMutationProbability") != null)
  		polynomialMutationProbability_ = (Double) parameters.get("polynomialMutationProbability") ;  		
  	
  	if (parameters.get("distributionIndex") != null)
  		distributionIndex_ = (Double) parameters.get("distributionIndex") ;  	
  	
  	generalBitFlipMutation= new GeneralBitFlipMutationForRes(parameters);
  	
	} // BitFlipMutation
	
	void doPolynomialMutation(double probability, Solution solution) throws JMException{
		double rnd, delta1, delta2, mut_pow, deltaq;
		double y, yl, yu, val, xy;
		
		//XReal x = new XReal(solution) ;
		
		// Polynomial mutation applied to the array real
		for (int var=0; var < solution.getDecisionVariables().length; var++) {	
			if(solution.getDecisionVariables()[var].getClass() == Real.class){
			if (PseudoRandom.randDouble() <= polynomialMutationProbability_) {
				y      = solution.getDecisionVariables()[var].getValue();
				yl     = solution.getDecisionVariables()[var].getLowerBound();                
				yu     = solution.getDecisionVariables()[var].getUpperBound();
				delta1 = (y-yl)/(yu-yl);
				delta2 = (yu-y)/(yu-yl);
				rnd = PseudoRandom.randDouble();
				mut_pow = 1.0/(eta_m_+1.0);
				if (rnd <= 0.5)
				{
					xy     = 1.0-delta1;
					val    = 2.0*rnd+(1.0-2.0*rnd)*(Math.pow(xy,(distributionIndex_+1.0)));
					deltaq =  java.lang.Math.pow(val,mut_pow) - 1.0;
				}
				else
				{
					xy = 1.0-delta2;
					val = 2.0*(1.0-rnd)+2.0*(rnd-0.5)*(java.lang.Math.pow(xy,(distributionIndex_+1.0)));
					deltaq = 1.0 - (java.lang.Math.pow(val,mut_pow));
				}
				y = y + deltaq*(yu-yl);
				if (y<yl)
					y = yl;
				if (y>yu)
					y = yu;
				//x.setValue(var, y);                           
				solution.getDecisionVariables()[var].setValue(y);
			} // if	
		}
		} // for

	}

	/**
	 * Perform the mutation operation
	 * @param probability Mutation probability
	 * @param solution The solution to mutate
	 * @throws JMException
	 */
	public void doMutation( Solution solution) throws JMException {
		try {
			
				//do general bit flip mutation
				generalBitFlipMutation.doMutation(bitFlipMutationProbability_, solution);
				
				//do polynomial mutation
				doPolynomialMutation(polynomialMutationProbability_,solution);
				
			} // if
			catch (ClassCastException e1) {
			Configuration.logger_.severe("BitFlipMutation.doMutation: " +
					"ClassCastException error" + e1.getMessage());
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".doMutation()");
		}
	} // doMutation

	/**
	 * Executes the operation
	 * @param object An object containing a solution to mutate
	 * @return An object containing the mutated solution
	 * @throws JMException 
	 */
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution) object;

		if (!VALID_TYPES.contains(solution.getType().getClass())) {
			Configuration.logger_.severe("BitFlipMutation.execute: the solution " +
					"is not of the right type. The type should be 'Binary', " +
					"'BinaryReal' or 'Int', but " + solution.getType() + " is obtained");

			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if 

		doMutation( solution);
		return solution;
	} // execute
} // BitFlipMutation
