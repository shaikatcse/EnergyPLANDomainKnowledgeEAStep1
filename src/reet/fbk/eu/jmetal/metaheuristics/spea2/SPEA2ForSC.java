//  SPEA2.java
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

package reet.fbk.eu.jmetal.metaheuristics.spea2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import reet.fbk.eu.OptimizeEnergyPLANWithAccuracy.util.RepairSolution;
import jmetal.core.*;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.Spea2Fitness;

/**
 * This class representing the SPEA2 algorithm
 */
public class SPEA2ForSC extends Algorithm {

	/**
	 * Defines the number of tournaments for creating the mating pool
	 */
	public static final int TOURNAMENTS_ROUNDS = 1;

	
	File fileHV, fileGD, fileIGD, fileSpread, fileEpsilon, fileGenSpread;
	FileWriter fwHV, fwGD, fwIGD, fwSpread, fwEpsilon, fwGenSpread;
	BufferedWriter bwHV, bwGD, bwIGD, bwSpread, bwEpsilon, bwGenSpread;
	
	String algoName;

	/**
	 * Constructor. Create a new SPEA2 instance
	 * 
	 * @param problem
	 *            Problem to solve
	 */
	public SPEA2ForSC(Problem problem) {
		super(problem);
		//repairSolution = new RepairSolution();
		algoName="SPEA2SC";
	} // Spea2

	public SPEA2ForSC(Problem problem, long seed, String folderName) {
		super(problem);
	//	repairSolution = new RepairSolution();
		if(!(new File(folderName+"\\HV").exists()))
			new File(folderName+"\\HV").mkdirs();
		if(!(new File(folderName+"\\GD").exists()))
			new File(folderName+"\\GD").mkdirs();
		if(!(new File(folderName+"\\IGD").exists()))
			new File(folderName+"\\IGD").mkdirs();
		if(!(new File(folderName+"\\Spread").exists()))
			new File(folderName+"\\Spread").mkdirs();
		if(!(new File(folderName+"\\Epsilon").exists()))
			new File(folderName+"\\Epsilon").mkdirs();
		if(!(new File(folderName+"\\GenSpread").exists()))
			new File(folderName+"\\GenSpread").mkdirs();
		
		
		fileHV = new File(folderName + "\\HV\\trackHV_" + seed);
		fileGD = new File(folderName + "\\GD\\trackGD_" + seed);
		fileIGD = new File(folderName + "\\IGD\\trackIGD_" + seed);
		fileSpread = new File(folderName + "\\Spread\\trackSpread_" + seed);
		fileEpsilon = new File(folderName + "\\Epsilon\\trackEpsilon_" + seed);
		fileGenSpread = new File(folderName + "\\GenSpread\\trackGenSpread_" + seed);
		// if file doesnt exists, then create it
		try {
			if (!fileHV.exists()) {
				fileHV.createNewFile();
				fileGD.createNewFile();
				fileIGD.createNewFile();
				fileSpread.createNewFile();
				fileEpsilon.createNewFile();
				fileGenSpread.createNewFile();
			}

			fwHV = new FileWriter(fileHV.getAbsoluteFile());
			fwGD = new FileWriter(fileGD.getAbsoluteFile());
			fwIGD = new FileWriter(fileIGD.getAbsoluteFile());
			fwSpread = new FileWriter(fileSpread.getAbsoluteFile());
			fwEpsilon = new FileWriter(fileEpsilon.getAbsoluteFile());
			fwGenSpread = new FileWriter(fileGenSpread.getAbsoluteFile());

			bwHV = new BufferedWriter(fwHV);
			bwGD = new BufferedWriter(fwGD);
			bwIGD = new BufferedWriter(fwIGD);
			bwSpread = new BufferedWriter(fwSpread);
			bwEpsilon = new BufferedWriter(fwEpsilon);
			bwGenSpread = new BufferedWriter(fwGenSpread);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Runs of the Spea2 algorithm.
	 * 
	 * @return a <code>SolutionSet</code> that is a set of non dominated
	 *         solutions as a result of the algorithm execution
	 * @throws JMException
	 */
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		int populationSize, archiveSize, maxEvaluations, evaluations;
		Operator crossoverOperator, mutationOperator, selectionOperator;
		SolutionSet solutionSet, archive, offSpringSolutionSet;

		// Read the params
		populationSize = ((Integer) getInputParameter("populationSize"))
				.intValue();
		archiveSize = ((Integer) getInputParameter("archiveSize")).intValue();
		maxEvaluations = ((Integer) getInputParameter("maxEvaluations"))
				.intValue();

		// Read the operators
		crossoverOperator = operators_.get("crossover");
		mutationOperator = operators_.get("mutation");
		selectionOperator = operators_.get("selection");

		// Initialize the variables
		solutionSet = new SolutionSet(populationSize);
		archive = new SolutionSet(archiveSize);
		evaluations = 0;

		// -> Create the initial solutionSet
		Solution newSolution;
		for (int i = 0; i < populationSize; i++) {
			newSolution = new Solution(problem_);
			problem_.evaluate(newSolution);
			problem_.evaluateConstraints(newSolution);
			evaluations++;
			solutionSet.add(newSolution);
		}
		// Generations
		int runs=((Integer) getInputParameter("run"))
				.intValue();
		String runDirectory="C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/"+algoName+"/"
		+ problem_.getName()+"/run"+runs;
		File runDir = new File(runDirectory);
		if (!runDir.exists()) {
			System.out.println(runDir.mkdir());
		
		}
				
		while (evaluations < maxEvaluations) {
			SolutionSet union = ((SolutionSet) solutionSet).union(archive);
			Spea2Fitness spea = new Spea2Fitness(union);
			spea.fitnessAssign();
			archive = spea.environmentalSelection(archiveSize);
			
			/*
			 * addes by shahriar to get a 1st ranked population from archive
			 */
			// write FUN and VAR for each generation
			Ranking generationRanking = new Ranking(archive);
			generationRanking.getSubfront(0).printFeasibleFUN(
					runDirectory+"/FUN" + (int) evaluations / populationSize);
			archive.printVariablesToFile(runDirectory+"/VAR"
					+ (int) evaluations / populationSize);
		
			
			// Create a new offspringPopulation
			offSpringSolutionSet = new SolutionSet(populationSize);
			Solution[] parents = new Solution[2];
			while (offSpringSolutionSet.size() < populationSize) {
				int j = 0;
				do {
					j++;
					parents[0] = (Solution) selectionOperator.execute(archive);
				} while (j < SPEA2ForSC.TOURNAMENTS_ROUNDS); // do-while
				int k = 0;
				do {
					k++;
					parents[1] = (Solution) selectionOperator.execute(archive);
				} while (k < SPEA2ForSC.TOURNAMENTS_ROUNDS); // do-while

				// make the crossover
				Solution[] offSpring = (Solution[]) crossoverOperator
						.execute(parents);
												
				mutationOperator.execute(offSpring[0]);
				
				problem_.evaluate(offSpring[0]);
				problem_.evaluateConstraints(offSpring[0]);
				offSpringSolutionSet.add(offSpring[0]);
				evaluations++;
			} // while
				// End Create a offSpring solutionSet
			solutionSet = offSpringSolutionSet;
		} // while
		
		//added by shariar
		SolutionSet union = ((SolutionSet) solutionSet).union(archive);
		Spea2Fitness spea = new Spea2Fitness(union);
		spea.fitnessAssign();
		archive = spea.environmentalSelection(archiveSize);
		//until now
		
		Ranking ranking = new Ranking(archive);
		
		ranking.getSubfront(0).printFeasibleFUN(
				runDirectory+"/FUN" + (int) evaluations / populationSize);
		ranking.getSubfront(0).printVariablesToFile(runDirectory+"/VAR"
				+ (int) evaluations / populationSize);
		
		return ranking.getSubfront(0);
	} // execute
} // SPEA2
