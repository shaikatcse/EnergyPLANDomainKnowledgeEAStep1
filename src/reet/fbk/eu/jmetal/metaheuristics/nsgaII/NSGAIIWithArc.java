//  NSGAII.java
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

package reet.fbk.eu.jmetal.metaheuristics.nsgaII;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jmetal.core.*;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.comparators.CrowdingComparator;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
/**
 * This is a modified implementation of NSGA-II to deal with a additional
 * archive that save diverse individuals (in decision space) that are
 * \epsilon-dominance in objective space
 */

public class NSGAIIWithArc extends Algorithm {
	/**
	 * Constructor
	 * 
	 * @param problem
	 *            Problem to solve
	 */
	public NSGAIIWithArc(Problem problem) {
		super(problem);
	} // NSGAII

	/**
	 * Runs the NSGA-II algorithm.
	 * 
	 * @return a <code>SolutionSet</code> that is a set of non dominated
	 *         solutions as a result of the algorithm execution
	 * @throws JMException
	 */
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		int populationSize;
		int maxEvaluations;
		int evaluations;

		QualityIndicator indicators; // QualityIndicator object
		int requiredEvaluations; // Use in the example of use of the
		// indicators object (see below)

		SolutionSet population;
		SolutionSet offspringPopulation;
		SolutionSet union;
		// define archive: modified by shahriar
		SolutionSet archive;

		Operator mutationOperator;
		Operator crossoverOperator;
		Operator selectionOperator;

		Distance distance = new Distance();

		// Read the parameters
		populationSize = ((Integer) getInputParameter("populationSize"))
				.intValue();
		maxEvaluations = ((Integer) getInputParameter("maxEvaluations"))
				.intValue();
		indicators = (QualityIndicator) getInputParameter("indicators");

		// Initialize the variables
		population = new SolutionSet(populationSize);
		evaluations = 0;

		// define archive
		archive = new SolutionSet(populationSize);

		requiredEvaluations = 0;

		// Read the operators
		mutationOperator = operators_.get("mutation");
		crossoverOperator = operators_.get("crossover");
		selectionOperator = operators_.get("selection");

		// Create the initial solutionSet
		Solution newSolution;
		for (int i = 0; i < populationSize; i++) {
			newSolution = new Solution(problem_);
			problem_.evaluate(newSolution);
			problem_.evaluateConstraints(newSolution);
			evaluations++;
			population.add(newSolution);
		} // for

		// Generations
		while (evaluations < maxEvaluations) {

			// Create the offSpring solutionSet
			offspringPopulation = new SolutionSet(populationSize);
			Solution[] parents = new Solution[2];
			for (int i = 0; i < (populationSize / 2); i++) {
				if (evaluations < maxEvaluations) {
					// obtain parents
					parents[0] = (Solution) selectionOperator
							.execute(population);
					parents[1] = (Solution) selectionOperator
							.execute(population);
					Solution[] offSpring = (Solution[]) crossoverOperator
							.execute(parents);
					mutationOperator.execute(offSpring[0]);
					mutationOperator.execute(offSpring[1]);
					problem_.evaluate(offSpring[0]);
					problem_.evaluateConstraints(offSpring[0]);
					problem_.evaluate(offSpring[1]);
					problem_.evaluateConstraints(offSpring[1]);
					offspringPopulation.add(offSpring[0]);
					offspringPopulation.add(offSpring[1]);
					evaluations += 2;
				} // if
			} // for

			// Create the solutionSet union of solutionSet and offSpring
			union = ((SolutionSet) population).union(offspringPopulation);

			// Ranking the union
			Ranking ranking = new Ranking(union);

			int remain = populationSize;
			int index = 0;
			SolutionSet front = null;
			population.clear();

			// Obtain the next front
			front = ranking.getSubfront(index);

			while ((remain > 0) && (remain >= front.size())) {
				// Assign crowding distance to individuals
				distance.crowdingDistanceAssignment(front,
						problem_.getNumberOfObjectives());
				// Add the individuals of this front
				for (int k = 0; k < front.size(); k++) {
					population.add(front.get(k));
				} // for

				// Decrement remain
				remain = remain - front.size();

				// Obtain the next front
				index++;
				if (remain > 0) {
					front = ranking.getSubfront(index);
				} // if
			} // while

			// Remain is less than front(index).size, insert only the best one
			if (remain > 0) { // front contains individuals to insert
				distance.crowdingDistanceAssignment(front,
						problem_.getNumberOfObjectives());
				front.sort(new CrowdingComparator());
				for (int k = 0; k < remain; k++) {
					population.add(front.get(k));
				} // for

				remain = 0;
			} // if

			// This piece of code shows how to use the indicator object into the
			// code
			// of NSGA-II. In particular, it finds the number of evaluations
			// required
			// by the algorithm to obtain a Pareto front with a hypervolume
			// higher
			// than the hypervolume of the true Pareto front.
			if ((indicators != null) && (requiredEvaluations == 0)) {
				double HV = indicators.getHypervolume(population);
				if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
					requiredEvaluations = evaluations;
				} // if
			} // if

			SolutionSet temporaryArchive = buildTemporaryArchive(union,
					archive, population);
			archive = buildArchive(population, temporaryArchive);

			// added by shaikat to track the run
			if ((evaluations % 50) == 0) {
				System.out.println(evaluations + ": "
						+ population.get(0).getObjective(0));
			} //

		} // while

		//added by shahriar
		//Ranking ranking = new Ranking(population);
		//ranking.getSubfront(0).printFeasibleFUN("FUN_NSGAII");
		archive.printObjectivesToFile("archive");
		
		//end added
		
		// Return as output parameter the required evaluations
		setOutputParameter("evaluations", requiredEvaluations);

		// Return the first non-dominated front
		Ranking ranking = new Ranking(population);
		ranking.getSubfront(0).printFeasibleFUN("FUN_NSGAII");

		return ranking.getSubfront(0);
	} // execute

	SolutionSet buildTemporaryArchive(SolutionSet previousPopulation,
			SolutionSet oldArchive, SolutionSet newPopulation) throws JMException {
		// previousPopulation = population union offspring
		// newPopulation = population after elimination
		SolutionSet union;
		union = previousPopulation.union(oldArchive);
		for (int i = 0; i < newPopulation.size(); i++) {
			Solution a = new Solution(newPopulation.get(i));
			  
			for (int j = 0; j < union.size(); j++) {
				Solution b = new Solution(union.get(j));
				boolean track = true;	
				for(int z=0;z<problem_.getNumberOfVariables();z++){
					if(a.getDecisionVariables()[z].getValue() != b.getDecisionVariables()[z].getValue())
						track = false;
				}
				if (track) {
					union.remove(j);
				}
			}
		}
		return union;
	}

	SolutionSet buildArchive(SolutionSet currentParetoFront,
			SolutionSet temporaryArchive) throws JMException {
		
		SolutionSet archive = new SolutionSet(currentParetoFront.size());
		for(int i=0;i<currentParetoFront.size();i++){
			SolutionSet ss = findNClosetSolutionInObjectiveSpace(currentParetoFront.get(i), temporaryArchive, 10);
			if(ss.size()>0){
				SolutionSet curr = new SolutionSet(1);
				curr.add(currentParetoFront.get(i));
				SolutionSet ssUnion=ss.union(curr);
				SolutionSet normalizedSS = new SolutionSet(ssUnion.size());
				normalizedSS=normalizedDecisionVariables(ssUnion);
				int position = findMaxdistanceDecisionSpace(normalizedSS);
				archive.add(ss.get(position));
			}
		}
		return archive;
	}
	
	
	public int findMaxdistanceDecisionSpace(SolutionSet ss) throws JMException{
			Solution sol = ss.get(ss.size()-1);
			  int returnPosition=-1;
			  double max = Double.MIN_VALUE;
			  for(int i=0;i<ss.size()-1;i++){
				  double dis = distanceDecisionSpace(sol, ss.get(i));
				  if(dis>=max){
					  max=dis;
					  returnPosition = i;
				  }
			  }
			  return returnPosition;
	}
	
	public double distanceDecisionSpace(Solution a, Solution b) throws JMException {

		double distance = 0.0;

		for (int i = 0; i < problem_.getNumberOfVariables(); i++) {
			distance += Math.pow(a.getDecisionVariables()[i].getValue() - b.getDecisionVariables()[i].getValue(), 2.0);
		}
		return Math.sqrt(distance);
	}
	
	
	public SolutionSet normalizedDecisionVariables(SolutionSet solutionSet) throws JMException{
		
		SolutionSet ss = new SolutionSet(solutionSet.size());
		for(int i=0;i<solutionSet.size();i++)
			ss.add(new Solution(solutionSet.get(i)));
		
		Mean mean = new Mean();
		StandardDeviation sd = new StandardDeviation(true/*means sample standardDeviation*/);
		
		for(int i=0;i<problem_.getNumberOfVariables();i++){
			double array[]=new double[ss.size()];		
			for(int j=0;j<ss.size();j++){
				array[j]=ss.get(j).getDecisionVariables()[i].getValue();
			}
			double arrayMean=mean.evaluate(array);
			double arraySD=sd.evaluate(array);
			if(arraySD==0){
				for(int j=0;j<ss.size();j++){
					ss.get(j).getDecisionVariables()[i].setValue(0.0);
				}
			}else{
				for(int j=0;j<ss.size();j++){
					double normaliedvalue = (array[j]- arrayMean)/arraySD;
					ss.get(j).getDecisionVariables()[i].setValue(normaliedvalue);
				}
			}
		}
		return ss;
	}

	public double distance(Solution a, Solution b) {

		double distance = 0.0;

		for (int i = 0; i < problem_.getNumberOfObjectives(); i++) {
			distance += Math.pow(a.getObjective(i) - b.getObjective(i), 2.0);
		}
		return Math.sqrt(distance);
	}

	SolutionSet findNClosetSolutionInObjectiveSpace(Solution solution, SolutionSet solutionSet, int n){
	  
	  class DistanceSolution{
		Solution solution;
		double distance;
		
		DistanceSolution(){
			solution=null;
			distance=0.0;
		}
		
		DistanceSolution(Solution sol, double dis){
			solution=sol;
			distance=dis;
		}
		
		
		void setSolution(Solution sol){
			this.solution=sol;
		}
		
		void setDistance(double distance){
			this.distance=distance;
		}
		
		Solution getSolution(){
			return solution;
		}
		
		double getDistance(){
			return distance;
		}
		
	  };
	  
	  List<DistanceSolution>  DSArray= new ArrayList<DistanceSolution>();
	  
	  for(int i=0;i<solutionSet.size();i++){
		  double dis = distance(solution, solutionSet.get(i));
		  boolean track = epsilonDominance(solution, solutionSet.get(i), 0.1);
		  if(dis>0.0 && track==true){
			  DistanceSolution ds = new DistanceSolution(solutionSet.get(i), dis);
			  DSArray.add(ds);
		  }
	  }
	  
	  if (DSArray.size()< n){
		  SolutionSet ss = new SolutionSet(DSArray.size()); 
		  
		  for(int i=0;i<DSArray.size();i++){
			  ss.add(DSArray.get(i).getSolution());
		  }
		  return ss;
		  
	  }
		  
	  
	  Collections.sort(DSArray, new Comparator<DistanceSolution>() {
	        @Override
	        public int compare(DistanceSolution  ds1, DistanceSolution  ds2)
	        {
	        	double dis1 = ds1.getDistance();
	        	double dis2 = ds2.getDistance();

	    		if (dis1 == dis2)
	    			return 0;
	    		else if (dis1 > dis2)
	    			return 1;
	    		else
	    			return -1;
	    	
	            //return  fruite1.fruitName.compareTo(fruite2.fruitName);
	        }
	        
	        
	    });
	  
	  SolutionSet ss = new SolutionSet(n); 
	  
	  for(int i=0;i<n;i++){
		  ss.add(DSArray.get(i).getSolution());
	  }
	  return ss;
  }

	/*
	 * check the solution is within the range of obj_i*(1-epsilon) true -> means
	 * within the range false -> not within the range
	 */
	boolean epsilonDominance(Solution paretoForntSolution, Solution solution,
			double epsilon) {
		boolean track = true;
		for (int i = 0; i < problem_.getNumberOfObjectives(); i++) {
			if ((paretoForntSolution.getObjective(i) - solution.getObjective(i)) > paretoForntSolution
					.getObjective(i) * (1 - epsilon)) {
				track = false;
				break;
			}

		}
		return track;
	}
} // NSGA-II
