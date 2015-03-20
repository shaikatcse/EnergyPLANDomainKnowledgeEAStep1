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
import reet.fbk.eu.OptimizeEnergyPLANWithAccuracy.util.RepairDVGene;
import reet.fbk.eu.OptimizeEnergyPLANWithAccuracy.util.RepairFuelGene;
import reet.fbk.eu.OptimizeEnergyPLANWithAccuracy.util.RepairSolution;
import reet.fbk.eu.jmetal.initialization.DKInitialization;
import sun.util.locale.StringTokenIterator;
import jmetal.core.*;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.comparators.CrowdingComparator;
import jmetal.util.wrapper.XReal;

/**
 * Implementation of NSGA-II. This implementation of NSGA-II makes use of a
 * QualityIndicator object to obtained the convergence speed of the algorithm.
 * This version is used in the paper: A.J. Nebro, J.J. Durillo, C.A. Coello
 * Coello, F. Luna, E. Alba
 * "A Study of Convergence Speed in Multi-Objective Metaheuristics." To be
 * presented in: PPSN'08. Dortmund. September 2008.
 */

/**
 * NSGA-II with smart initialization
 * 
 * @author mahbub
 * 
 */
public class NSGAIIForSI extends NSGAII {

	File fileHV, fileGD, fileIGD, fileSpread, fileEpsilon, fileGenSpread;
	FileWriter fwHV, fwGD, fwIGD, fwSpread, fwEpsilon, fwGenSpread;
	BufferedWriter bwHV, bwGD, bwIGD, bwSpread, bwEpsilon, bwGenSpread;

	String initialPopulationFile;

	/* Genes information */
	private Boolean favorGenesForRE[], favorGenesForCon[], favorGenesForLFC[];

	/**
	 * Constructor
	 * 
	 * @param problem
	 *            Problem to solve
	 * @throws JMException
	 */

	public NSGAIIForSI(Problem problem, HashMap<String, Object> parameters)
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

	} // NSGAII
	
	public NSGAIIForSI(Problem problem){
		super(problem);
	}
	

	public NSGAIIForSI(Problem problem, long seed, String folderName,HashMap<String, Object> parameters ) {
		super(problem);
		
		if (parameters.get("InitialPopulationFile") != null)
			initialPopulationFile = (String) parameters
					.get("InitialPopulationFile");
		
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
	 * Runs the NSGA-II algorithm.
	 * 
	 * @return a <code>SolutionSet</code> that is a set of non dominated
	 *         solutions as a result of the algorithm execution
	 * @throws JMException
	 * @throws MatlabInvocationException
	 */
	@Override
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

		requiredEvaluations = 0;

		// Read the operators
		mutationOperator = operators_.get("mutation");
		crossoverOperator = operators_.get("crossover");
		selectionOperator = operators_.get("selection");

		// Create the initial solutionSet
		/*
		 * Solution newSolution; for (int i = 0; i < populationSize; i++) {
		 * newSolution = new Solution(problem_);
		 * 
		 * // repairSolution.doRepair(newSolution);
		 * 
		 * problem_.evaluate(newSolution);
		 * problem_.evaluateConstraints(newSolution); evaluations++;
		 * population.add(newSolution); } // for
		 */

		// New Initialization (Author: shaikat)

		if (initialPopulationFile != null) {
			BufferedReader br = null;

			try {

				String sCurrentLine;

				br = new BufferedReader(new FileReader(initialPopulationFile));

				while ((sCurrentLine = br.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(sCurrentLine);
					int j = 0;
					Solution solution = new Solution(problem_);
					XReal xreal = new XReal(solution);
					while (st.hasMoreElements()) {
						xreal.setValue(j, Double.parseDouble(st.nextToken()));
						j++;
					}
					population.add(solution);

				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			for (int i = 0; i < populationSize; i++) {
				problem_.evaluate(population.get(i));
				problem_.evaluateConstraints(population.get(i));
				evaluations++;
			}

		} else {

			MatlabProxyFactory factory;
			MatlabProxy proxy;

			factory = new MatlabProxyFactory();
			try {
				proxy = factory.getProxy();

				DKInitialization dkini = new DKInitialization(problem_,
						favorGenesForRE, favorGenesForCon, populationSize, 6.0,
						4, 3, proxy);

				population = dkini.doDKInitialization();
			} catch (MatlabConnectionException e) {
				throw new JMException("Matlab connection problem");
			} catch (MatlabInvocationException e) {
				throw new JMException("Matlab function invocation problem");
			}

			// evaluate each of the individuals
			for (int i = 0; i < populationSize; i++) {
				problem_.evaluate(population.get(i));
				problem_.evaluateConstraints(population.get(i));
				evaluations++;
			}
		}
		/*
		 * if (indicators != null) { int genNo = (int) evaluations /
		 * populationSize; double hyperVolume =
		 * indicators.getHypervolume(population); double gd =
		 * indicators.getGD(population); double igd =
		 * indicators.getIGD(population); double spread =
		 * indicators.getSpread(population); double epsilon =
		 * indicators.getEpsilon(population); double genSpread =
		 * indicators.getGeneralizedSpread(population);
		 * 
		 * try { bwHV.write(genNo + " " + hyperVolume + "\n"); bwGD.write(genNo
		 * + " " + gd + "\n"); bwIGD.write(genNo + " " + igd + "\n");
		 * bwSpread.write(genNo + " " + spread + "\n"); bwEpsilon.write(genNo +
		 * " " + epsilon + "\n"); bwGenSpread.write(genNo + " " + genSpread +
		 * "\n");
		 * 
		 * } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } }
		 */
		
		
		Ranking generationRanking = new Ranking(population);
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

					// repairSolution.doRepair(offSpring[0]);
					// repairSolution.doRepair(offSpring[1]);

					mutationOperator.setParameter("current generation",
							(int) evaluations / populationSize);
					mutationOperator.execute(offSpring[0]);
					mutationOperator.execute(offSpring[1]);

					// repairSolution.doRepair(offSpring[0]);
					// repairSolution.doRepair(offSpring[1]);

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
				if (HV >= (0.90 * indicators.getTrueParetoFrontHypervolume())) {
					requiredEvaluations = evaluations;
				} // if
			} // if
			
			Ranking generateRanking = new Ranking(population);
			
			if (indicators != null) {
				int genNo = (int) evaluations / populationSize;
				double hyperVolume = indicators.getHypervolume(generateRanking.getSubfront(0));
				double gd = indicators.getGD(generateRanking.getSubfront(0));
				double igd = indicators.getIGD(generateRanking.getSubfront(0));
				double spread = indicators.getSpread(generateRanking.getSubfront(0));
				double epsilon = indicators.getEpsilon(generateRanking.getSubfront(0));
				double genSpread = indicators.getGeneralizedSpread(generateRanking.getSubfront(0));

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
			// added by shaikat to track the run
			if ((evaluations % populationSize) == 0) {
				System.out.println(evaluations + ": "
						+ population.get(0).getObjective(0));
			} //

		} // while

		// Return as output parameter the required evaluations
		setOutputParameter("evaluations", requiredEvaluations);

		// Return the first non-dominated front
		Ranking ranking = new Ranking(population);
		ranking.getSubfront(0).printFeasibleFUN("FUN_NSGAII");
		if (indicators != null) {
			try {
				
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
} // NSGA-II
