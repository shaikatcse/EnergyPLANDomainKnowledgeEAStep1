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
import reet.fbk.eu.jmetal.stoppingCriteria.StopMOEA;
import jmetal.core.*;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.Spea2Fitness;
import jmetal.util.wrapper.XReal;

/**
 * This class representing the SPEA2 algorithm
 */
public class SPEA2ForDKandSCandSI extends Algorithm {

	/**
	 * Defines the number of tournaments for creating the mating pool
	 */
	public static final int TOURNAMENTS_ROUNDS = 1;

	//RepairSolution repairSolution;

	File fileHV, fileGD, fileIGD, fileSpread, fileEpsilon, fileGenSpread;
	FileWriter fwHV, fwGD, fwIGD, fwSpread, fwEpsilon, fwGenSpread;
	BufferedWriter bwHV, bwGD, bwIGD, bwSpread, bwEpsilon, bwGenSpread;
	
	String initialPopulationFile;
	
	/* Genes DK information */
	private Boolean favorGenesForRE[], favorGenesForCon[], favorGenesForLFC[];
	
	
	/**
	 * Constructor. Create a new SPEA2 instance
	 * 
	 * @param problem
	 *            Problem to solve
	 */
	
	StopMOEA stopMOEACriteria1, stopMOEACriteria2 ;
	
	public SPEA2ForDKandSCandSI(Problem problem, HashMap<String, Object> parameters) throws JMException {
		super(problem);
		//nGenLT=20, nGenUnCh=5, alpha=0.05
		stopMOEACriteria1 = new StopMOEA(20, 5, 0.05);
		//nGenLT=15, nGenUnCh=7, alpha=0.05
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
	} // Spea2

	public SPEA2ForDKandSCandSI(Problem problem, long seed, String folderName,HashMap<String, Object> parameters) {
		super(problem);
		//nGenLT=20, nGenUnCh=5, alpha=0.05
		stopMOEACriteria1 = new StopMOEA(20, 5, 0.05);
		//nGenLT=20, nGenUnCh=10, alpha=0.05
		stopMOEACriteria2 = new StopMOEA(20, 10, 0.05);
		
		if (parameters.get("InitialPopulationFile") != null)
			initialPopulationFile = (String) parameters
					.get("InitialPopulationFile");
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
		/*Solution newSolution;
		for (int i = 0; i < populationSize; i++) {
			newSolution = new Solution(problem_);
			
		//	repairSolution.doRepair(newSolution);
			
			problem_.evaluate(newSolution);
			problem_.evaluateConstraints(newSolution);
			evaluations++;
			solutionSet.add(newSolution);
		}*/
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
					solutionSet.add(solution);

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
				problem_.evaluate(solutionSet.get(i));
				problem_.evaluateConstraints(solutionSet.get(i));
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

				solutionSet = dkini.doDKInitialization();
			} catch (MatlabConnectionException e) {
				throw new JMException("Matlab connection problem");
			} catch (MatlabInvocationException e) {
				throw new JMException("Matlab function invocation problem");
			}

			// evaluate each of the individuals
			for (int i = 0; i < populationSize; i++) {
				problem_.evaluate(solutionSet.get(i));
				problem_.evaluateConstraints(solutionSet.get(i));
				evaluations++;
			}
		}
		
		//added by shahriar
		boolean isStoppingCriteria1Activated=false;
		boolean isStoppingCriteria2Activated=false;

		while (evaluations < maxEvaluations) {
				
			SolutionSet union = ((SolutionSet) solutionSet).union(archive);
			Spea2Fitness spea = new Spea2Fitness(union);
			spea.fitnessAssign();
			archive = spea.environmentalSelection(archiveSize);
			
			Ranking generationRanking = new Ranking(archive);
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
			
			
			// Create a new offspringPopulation
			offSpringSolutionSet = new SolutionSet(populationSize);
			Solution[] parents = new Solution[2];
			while (offSpringSolutionSet.size() < populationSize) {
				int j = 0;
				do {
					j++;
					parents[0] = (Solution) selectionOperator.execute(archive);
				} while (j < SPEA2ForDKandSCandSI.TOURNAMENTS_ROUNDS); // do-while
				int k = 0;
				do {
					k++;
					parents[1] = (Solution) selectionOperator.execute(archive);
				} while (k < SPEA2ForDKandSCandSI.TOURNAMENTS_ROUNDS); // do-while

				// make the crossover
				Solution[] offSpring = (Solution[]) crossoverOperator
						.execute(parents);
				
				//repairSolution.doRepair(offSpring[0]);
				//repairSolution.doRepair(offSpring[1]);
				
				mutationOperator.setParameter("current generation", (int) evaluations / populationSize);
				mutationOperator.execute(offSpring[0]);
				
				//repairSolution.doRepair(offSpring[0]);
				
				problem_.evaluate(offSpring[0]);
				problem_.evaluateConstraints(offSpring[0]);
				offSpringSolutionSet.add(offSpring[0]);
				evaluations++;
			} // while
				// End Create a offSpring solutionSet
			solutionSet = offSpringSolutionSet;
		} // while
		
		Ranking ranking = new Ranking(archive);
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
} // SPEA2
