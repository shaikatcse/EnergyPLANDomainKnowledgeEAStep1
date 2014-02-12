//  Hypervolume.java
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

package reet.fbk.eu.qualityIndicator;

/**
 * This class implements the hypervolume indicator. The code is the a Java version
 * of the original metric implementation by Eckart Zitzler.
 * It can be used also as a command line program just by typing
 * $java jmetal.qualityIndicator.Hypervolume <solutionFrontFile> <trueFrontFile> <numberOfOjbectives>
 * Reference: E. Zitzler and L. Thiele
 *           Multiobjective Evolutionary Algorithms: A Comparative Case Study 
 *           and the Strength Pareto Approach,
 *           IEEE Transactions on Evolutionary Computation, vol. 3, no. 4, 
 *           pp. 257-271, 1999.
 */
public class MyHypervolume extends jmetal.qualityIndicator.Hypervolume{

 
  
  /**
  * Constructor
  * Creates a new instance of MultiDelta
  */
  public MyHypervolume() {
    super();
  } // Hypervolume
  
   /** 
   * Returns the hypevolume value of the paretoFront. This method call to the
   * calculate hipervolume one
   * @param paretoFront The pareto front
   * @param paretoTrueFront The true pareto front
   * @param numberOfObjectives Number of objectives of the pareto front
   */
  @Override
  public double hypervolume(double [][] paretoFront, 
                            double [][] paretoTrueFront,
                            int numberOfObjectives) {
    
    /**
     * Stores the maximum values of true pareto front.
     */
    double[] maximumValues;
    
    /**
     * Stores the minimum values of the true pareto front.
     */
    double[] minimumValues;
    
    /**
     * Stores the normalized front.
     */
    double [][] normalizedFront;
    
    /**
     * Stores the inverted front. Needed for minimization problems
     */
    double [][] invertedFront;
    
    // STEP 1. Obtain the maximum and minimum values of the Pareto front
    maximumValues = new double[] {30,50000};
    minimumValues = new double[] {0,0};
    
    // STEP 2. Get the normalized front
    normalizedFront = utils_.getNormalizedFront(paretoFront,
                                                maximumValues,
                                                minimumValues);
    
    // STEP 3. Inverse the pareto front. This is needed because of the original
    //metric by Zitzler is for maximization problems
    invertedFront = utils_.invertedFront(normalizedFront);
    
    // STEP4. The hypervolumen (control is passed to java version of Zitzler code)
    return this.calculateHypervolume(invertedFront,invertedFront.length,numberOfObjectives);
  }// hypervolume
  
  /**
   * This class can be invoqued from the command line. Three params are required:
   * 1) the name of the file containing the front,  
   * 2) the name of the file containig the true Pareto front
   * 3) the number of objectives
   */
  public static void main(String args[]) {
    if (args.length < 2) {
      System.err.println("Error using Hypervolume. Usage: \n java jmetal.qualityIndicator.Hypervolume " +
                         "<SolutionFrontFile> " +
                         "<TrueFrontFile> " + "<numberOfObjectives>");
      System.exit(1);
    }
    
    //Create a new instance of the metric
    MyHypervolume qualityIndicator = new MyHypervolume();
    //Read the front from the files
    double [][] solutionFront = qualityIndicator.utils_.readFront(args[0]);
    double [][] trueFront     = qualityIndicator.utils_.readFront(args[1]);
    
    //Obtain delta value
    double value = qualityIndicator.hypervolume(solutionFront,
                                  trueFront,
            new Integer(args[2]));
    
    System.out.println(value);  
  } // main
} // Hypervolume
