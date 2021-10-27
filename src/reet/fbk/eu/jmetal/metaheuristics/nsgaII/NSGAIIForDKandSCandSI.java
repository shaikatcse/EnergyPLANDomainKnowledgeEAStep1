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
import reet.fbk.eu.jmetal.stoppingCriteria.StopMOEA;
import jmetal.core.*;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.comparators.CrowdingComparator;
import jmetal.util.wrapper.XReal;

/**
 * A modified NSGA-II that works with Domain knowledge and Stooping criteria
 * @author mahbub
 *
 */
public class NSGAIIForDKandSCandSI extends NSGAII {

	//RepairSolution repairSolution;

	File fileHV, fileGD, fileIGD, fileSpread, fileEpsilon, fileGenSpread;
	FileWriter fwHV, fwGD, fwIGD, fwSpread, fwEpsilon, fwGenSpread;
	BufferedWriter bwHV, bwGD, bwIGD, bwSpread, bwEpsilon, bwGenSpread;

	String initialPopulationFile;
	
	/* Genes DK information */
	private Boolean favorGenesForRE[], favorGenesForCon[], favorGenesForLFC[];
	
	/**
	 * Constructor
	 * 
	 * @param problem
	 *            Problem to solve
	 */
	
	StopMOEA stopMOEACriteria1, stopMOEACriteria2 ;

	public NSGAIIForDKandSCandSI(Problem problem, HashMap<String, Object> parameters) throws JMException {
		super(problem);
		//nGenLT=20, nGenUnCh=5, alpha=0.05
		stopMOEACriteria1 = new StopMOEA(20, 5, 0.05);
		//nGenLT=20, nGenUnCh=10, alpha=0.05
		stopMOEACriteria2 = new StopMOEA(15, 7, 0.05);
		
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

	public NSGAIIForDKandSCandSI(Problem problem, long seed, String folderName ,HashMap<String, Object> parameters) {
		super(problem);
		//nGenLT=20, nGenUnCh=5, alpha=0.05
		stopMOEACriteria1 = new StopMOEA(20, 5, 0.05);
		//nGenLT=20, nGenUnCh=10, alpha=0.05
		stopMOEACriteria2 = new StopMOEA(20, 10, 0.05);
		
		if (parameters.get("InitialPopulationFile") != null)
			initialPopulationFile = (String) parameters
					.get("InitialPopulationFile");
		
		//repairSolution = new RepairSolution();
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
	 * Runs the NSGA-II algorithm.
	 * 
	 * @return a <code>SolutionSet</code> that is a set of non dominated
	 *         solutions as a result of the algorithm execution
	 * @throws JMException
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
		/*Solution newSolution;
		for (int i = 0; i < populationSize; i++) {
			newSolution = new Solution(problem_);

			//repairSolution.doRepair(newSolution);

			problem_.evaluate(newSolution);
			problem_.evaluateConstraints(newSolution);
			evaluations++;
			population.add(newSolution);
		} // for*/

	
		
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
						4, 3, 200, proxy);

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
		
		/*if (indicators != null) {
			int genNo = (int) evaluations / populationSize;
			double hyperVolume = indicators.getHypervolume(population);
			double gd = indicators.getGD(population);
			double igd = indicators.getIGD(population);
			double spread = indicators.getSpread(population);
			double epsilon = indicators.getEpsilon(population);
			double genSpread = indicators.getGeneralizedSpread(population);

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
		}*/
		
		//added by shahriar
		boolean isStoppingCriteria1Activated=false;
		boolean isStoppingCriteria2Activated=false;
		// Generations
		while (evaluations < maxEvaluations ) {

			Ranking generationRanking = new Ranking(population);
			if(!isStoppingCriteria1Activated && stopMOEACriteria1.isStopMOEA((int) evaluations / populationSize, generationRanking.getSubfront(0), problem_.getNumberOfObjectives(), problem_.getNumberOfVariables())){
				System.out.println("Stopping Criteria 1 is Activated: " +(int) evaluations / populationSize);	
				//break;
				isStoppingCriteria1Activated=true;
				setOutputParameter("stoppingPopulationCriteria1", (SolutionSet)generationRanking.getSubfront(0)) ; 
				setOutputParameter("stopGenCriteria1",(int) evaluations/populationSize);
			}
			
			if(!isStoppingCriteria2Activated && stopMOEACriteria2.isStopMOEA((int) evaluations / populationSize, generationRanking.getSubfront(0), problem_.getNumberOfObjectives(), problem_.getNumberOfVariables())){
				System.out.println("Stopping Criteria 2 is Activated: " +(int) evaluations / populationSize);	
				//break;
				isStoppingCriteria2Activated=true;
				setOutputParameter("stoppingPopulationCriteria2", (SolutionSet)generationRanking.getSubfront(0)) ; 
				setOutputParameter("stopGenCriteria2",(int) evaluations/populationSize);
			}
			
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

					//repairSolution.doRepair(offSpring[0]);
				//	repairSolution.doRepair(offSpring[1]);

					mutationOperator.setParameter("current generation", (int) evaluations / populationSize);
					mutationOperator.execute(offSpring[0]);
					mutationOperator.execute(offSpring[1]);

					//repairSolution.doRepair(offSpring[0]);
					//repairSolution.doRepair(offSpring[1]);

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
			if (indicators != null) {
				int genNo = (int) evaluations / populationSize;
				double hyperVolume = indicators.getHypervolume(population);
				double gd = indicators.getGD(population);
				double igd = indicators.getIGD(population);
				double spread = indicators.getSpread(population);
				double epsilon = indicators.getEpsilon(population);
				double genSpread = indicators.getGeneralizedSpread(population);

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
			if ((evaluations % 1000) == 0) {
				System.out.println(evaluations + ": "
						+ population.get(0).getObjective(0));
			} //

		} // while

		
		
		// Return as output parameter the evaluations
		setOutputParameter("evaluations", evaluations);

		// Return the first non-dominated front
		Ranking ranking = new Ranking(population);
		ranking.getSubfront(0).printFeasibleFUN("FUN_NSGAII");
		if (indicators != null) {
			try {
				bwHV.write("Required evolution to reach 90% Hypervolume: "
						+ requiredEvaluations);
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
		
		//added by shahriar
		if(!isStoppingCriteria1Activated){
			setOutputParameter("stoppingPopulationCriteria1", (SolutionSet)ranking.getSubfront(0)) ; 
			setOutputParameter("stopGenCriteria1",(int) evaluations/populationSize);
		}
		if(!isStoppingCriteria2Activated){
			setOutputParameter("stoppingPopulationCriteria2", (SolutionSet)ranking.getSubfront(0)) ; 
			setOutputParameter("stopGenCriteria2",(int) evaluations/populationSize);
		}

		return ranking.getSubfront(0);
	} // execute
} // NSGA-II
