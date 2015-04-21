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

package reet.fbk.eu.jmetal.initialization.metaheuristics.spea2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import reet.fbk.eu.OptimizeEnergyPLANWithAccuracy.util.RepairSolution;
import reet.fbk.eu.jmetal.initialization.DKInitialization;
import jmetal.core.*;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.Spea2Fitness;
import jmetal.util.wrapper.XReal;

/**
 * This modified version is only required to track the different indicators for typical SPEA2.
 * And this version is used for comparing with smart initilization.
 */
public class SPEA2TrackIndicators extends Algorithm {

	/**
	 * Defines the number of tournaments for creating the mating pool
	 */
	public static final int TOURNAMENTS_ROUNDS = 1;

	// RepairSolution repairSolution;

	File fileHV, fileGD, fileIGD, fileSpread, fileEpsilon, fileGenSpread;
	FileWriter fwHV, fwGD, fwIGD, fwSpread, fwEpsilon, fwGenSpread;
	BufferedWriter bwHV, bwGD, bwIGD, bwSpread, bwEpsilon, bwGenSpread;

	String initialPopulationFile;

	private Boolean favorGenesForRE[], favorGenesForCon[], favorGenesForLFC[];

	/**
	 * Constructor. Create a new SPEA2 instance
	 * 
	 * @param problem
	 *            Problem to solve
	 */
	public SPEA2TrackIndicators(Problem problem) {
		super(problem);
		// repairSolution = new RepairSolution();
	} // Spea2

	public SPEA2TrackIndicators(Problem problem, HashMap<String, Object> parameters)
			throws JMException {
		super(problem);

		if (parameters.get("favorGenesforRE") != null
				&& parameters.get("favorGenesForCon") != null) {
			favorGenesForRE = (Boolean[]) parameters.get("favorGenesforRE");
			favorGenesForCon = (Boolean[]) parameters.get("favorGenesForCon");
		} else
			throw new JMException(
					"favorGenesforRE or favorGenesForConventioanlPP parameter missing");

		if (parameters.get("favorGenesForLFC") != null)
			favorGenesForLFC = (Boolean[]) parameters.get("favorGenesForLFC");
		// repairSolution = new RepairSolution();

		if (parameters.get("InitialPopulationFile") != null)
			initialPopulationFile = (String) parameters
					.get("InitialPopulationFile");

	}

	public SPEA2TrackIndicators(Problem problem, long seed, String folderName) {
		super(problem);
		// repairSolution = new RepairSolution();
		if (!(new File(folderName + "\\HV").exists()))
			new File(folderName + "\\HV").mkdirs();
		if (!(new File(folderName + "\\GD").exists()))
			new File(folderName + "\\GD").mkdirs();
		if (!(new File(folderName + "\\IGD").exists()))
			new File(folderName + "\\IGD").mkdirs();
		if (!(new File(folderName + "\\Spread").exists()))
			new File(folderName + "\\Spread").mkdirs();
		if (!(new File(folderName + "\\Epsilon").exists()))
			new File(folderName + "\\Epsilon").mkdirs();
		if (!(new File(folderName + "\\GenSpread").exists()))
			new File(folderName + "\\GenSpread").mkdirs();

		fileHV = new File(folderName + "\\HV\\trackHV_" + seed);
		fileGD = new File(folderName + "\\GD\\trackGD_" + seed);
		fileIGD = new File(folderName + "\\IGD\\trackIGD_" + seed);
		fileSpread = new File(folderName + "\\Spread\\trackSpread_" + seed);
		fileEpsilon = new File(folderName + "\\Epsilon\\trackEpsilon_" + seed);
		fileGenSpread = new File(folderName + "\\GenSpread\\trackGenSpread_"
				+ seed);
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
		
		QualityIndicator indicators; // QualityIndicator object

		// Read the params
		populationSize = ((Integer) getInputParameter("populationSize"))
				.intValue();
		archiveSize = ((Integer) getInputParameter("archiveSize")).intValue();
		maxEvaluations = ((Integer) getInputParameter("maxEvaluations"))
				.intValue();

		indicators = (QualityIndicator) getInputParameter("indicators");
		
		// Read the operators
		crossoverOperator = operators_.get("crossover");
		mutationOperator = operators_.get("mutation");
		selectionOperator = operators_.get("selection");

		// Initialize the variables
		solutionSet = new SolutionSet(populationSize);
		archive = new SolutionSet(archiveSize);
		evaluations = 0;

		// -> Create the initial solutionSet
		/*
		 * Solution newSolution; for (int i = 0; i < populationSize; i++) {
		 * newSolution = new Solution(problem_);
		 * 
		 * // repairSolution.doRepair(newSolution);
		 * 
		 * problem_.evaluate(newSolution);
		 * problem_.evaluateConstraints(newSolution); evaluations++;
		 * solutionSet.add(newSolution); }
		 */

		 //-> Create the initial solutionSet
	    Solution newSolution;
	    for (int i = 0; i < populationSize; i++) {
	      newSolution = new Solution(problem_);
	      problem_.evaluate(newSolution);            
	      problem_.evaluateConstraints(newSolution);
	      evaluations++;
	      solutionSet.add(newSolution);
	    }                        

		while (evaluations < maxEvaluations) {
			SolutionSet union = ((SolutionSet) solutionSet).union(archive);
			Spea2Fitness spea = new Spea2Fitness(union);
			spea.fitnessAssign();
			archive = spea.environmentalSelection(archiveSize);
			
			Ranking generationRanking = new Ranking(archive);
			if (indicators != null) {
				int genNo = (int) evaluations / populationSize;
				double hyperVolume = indicators.getHypervolume(generationRanking.getSubfront(0));
				double gd = indicators.getGD(generationRanking.getSubfront(0));
				double igd = indicators.getIGD(generationRanking.getSubfront(0));
				double spread = indicators.getSpread(generationRanking.getSubfront(0));
				double epsilon = indicators.getEpsilon(generationRanking.getSubfront(0));
				double genSpread = indicators.getGeneralizedSpread(generationRanking.getSubfront(0));

				try {
					bwHV.write(genNo + " " + hyperVolume + "\n");
					bwGD.write(genNo + " " + gd + "\n");
					bwIGD.write(genNo + " " + igd + "\n");
					bwSpread.write(genNo + " " + spread + "\n");
					bwEpsilon.write(genNo + " " + epsilon + "\n");
					bwGenSpread.write(genNo + " " + genSpread + "\n");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			// Create a new offspringPopulation
			offSpringSolutionSet = new SolutionSet(populationSize);
			Solution[] parents = new Solution[2];
			while (offSpringSolutionSet.size() < populationSize) {
				int j = 0;
				do {
					j++;
					parents[0] = (Solution) selectionOperator.execute(archive);
				} while (j < SPEA2TrackIndicators.TOURNAMENTS_ROUNDS); // do-while
				int k = 0;
				do {
					k++;
					parents[1] = (Solution) selectionOperator.execute(archive);
				} while (k < SPEA2TrackIndicators.TOURNAMENTS_ROUNDS); // do-while

				// make the crossover
				Solution[] offSpring = (Solution[]) crossoverOperator
						.execute(parents);

				// repairSolution.doRepair(offSpring[0]);
				// repairSolution.doRepair(offSpring[1]);

				mutationOperator.setParameter("current generation",
						(int) evaluations / populationSize);
				mutationOperator.execute(offSpring[0]);

				// repairSolution.doRepair(offSpring[0]);

				problem_.evaluate(offSpring[0]);
				problem_.evaluateConstraints(offSpring[0]);
				offSpringSolutionSet.add(offSpring[0]);
				evaluations++;
			} // while
				// End Create a offSpring solutionSet
			solutionSet = offSpringSolutionSet;
		} // while

		Ranking ranking = new Ranking(archive);
		
		if (indicators != null) {
			int genNo = (int) evaluations / populationSize;
			double hyperVolume = indicators.getHypervolume(ranking.getSubfront(0));
			double gd = indicators.getGD(ranking.getSubfront(0));
			double igd = indicators.getIGD(ranking.getSubfront(0));
			double spread = indicators.getSpread(ranking.getSubfront(0));
			double epsilon = indicators.getEpsilon(ranking.getSubfront(0));
			double genSpread = indicators.getGeneralizedSpread(ranking.getSubfront(0));

			try {
				bwHV.write(genNo + " " + hyperVolume + "\n");
				bwGD.write(genNo + " " + gd + "\n");
				bwIGD.write(genNo + " " + igd + "\n");
				bwSpread.write(genNo + " " + spread + "\n");
				bwEpsilon.write(genNo + " " + epsilon + "\n");
				bwGenSpread.write(genNo + " " + genSpread + "\n");
				
				bwHV.close();
				bwGD.close();
				bwIGD.close();
				bwSpread.close();
				bwEpsilon.close();
				bwGenSpread.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ranking.getSubfront(0);
	} // execute
} // SPEA2
