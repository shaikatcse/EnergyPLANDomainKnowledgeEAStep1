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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Parameter;

import reet.fbk.eu.OptimizeEnergyPLANWithAccuracy.util.RepairDVGene;
import reet.fbk.eu.OptimizeEnergyPLANWithAccuracy.util.RepairFuelGene;
import reet.fbk.eu.OptimizeEnergyPLANWithAccuracy.util.RepairSolution;
import reet.fbk.eu.jmetal.stoppingCriteria.AveragedHausdroffDistance;
import reet.fbk.eu.jmetal.stoppingCriteria.CalculateDiversity;
import reet.fbk.eu.jmetal.stoppingCriteria.GenerationHypervolume;
import reet.fbk.eu.qualityIndicator.CalculateAllQualityIndicatorValue;
import jmetal.core.*;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.comparators.CrowdingComparator;

/**
 * Implementation of NSGA-II. This implementation of NSGA-II makes use of a
 * QualityIndicator object to obtained the convergence speed of the algorithm.
 * This version is used in the paper: A.J. Nebro, J.J. Durillo, C.A. Coello
 * Coello, F. Luna, E. Alba
 * "A Study of Convergence Speed in Multi-Objective Metaheuristics." To be
 * presented in: PPSN'08. Dortmund. September 2008.
 */

/*
 * Print each generation of Pareto-front and population in desision space
 */

public class NSGAIIForSC extends NSGAII {

	RepairSolution repairSolution;

	File fileHV, fileGD, fileIGD, fileSpread, fileEpsilon, fileGenSpread;
	FileWriter fwHV, fwGD, fwIGD, fwSpread, fwEpsilon, fwGenSpread;;
	BufferedWriter bwHV, bwGD, bwIGD, bwSpread, bwEpsilon, bwGenSpread;;

	String algoName;
	/**
	 * Constructor
	 * 
	 * @param problem
	 *            Problem to solve
	 */

	public NSGAIIForSC(Problem problem) {
		super(problem);
		// repairSolution = new RepairSolution();
		algoName="NSGAIISC";

	} // NSGAII

	public NSGAIIForSC(Problem problem, long seed, String folderName) {
		super(problem);
		repairSolution = new RepairSolution();
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

		// read the bas directory
		String baseDirectory = ((String) getInputParameter("baseDirectory"));
		//String paretoFrontFile = ((String)("paretoFrontFile_"));
		
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
		Solution newSolution;
		for (int i = 0; i < populationSize; i++) {
			newSolution = new Solution(problem_);

			// repairSolution.doRepair(newSolution);

			problem_.evaluate(newSolution);
			problem_.evaluateConstraints(newSolution);
			evaluations++;
			population.add(newSolution);
		} // for

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

			// write FUN and VAR for each generation
			Ranking generationRanking = new Ranking(population);
			generationRanking.getSubfront(0).printFeasibleFUN(
					runDirectory+"\\FUN" + (int) evaluations / populationSize);
			population.printVariablesToFile(runDirectory+"\\VAR"
					+ (int) evaluations / populationSize);

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

		
		ranking.getSubfront(0).printFeasibleFUN(
				runDirectory+"\\FUN" + (int) evaluations / populationSize);
		population.printVariablesToFile(runDirectory+"\\VAR"
				+ (int) evaluations / populationSize);
		
		// stopping criteria analysis
	/*	MultiMap map = new MultiValueMap(); 
		AveragedHausdroffDistance averageHD = new AveragedHausdroffDistance();
		CalculateDiversity calDV = new CalculateDiversity();
		GenerationHypervolume hv = new GenerationHypervolume();
		map = averageHD.calcualteAverageHausdroffDistance(problem_.getNumberOfObjectives(), map);
		map=calDV.diversityStatisticalTest(problem_.getNumberOfVariables(), map);
		map = hv.calculateHyperVolume(problem_, map);
		
		String fileName = "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1"
				+ "/StoppingCriteriaStudies/data/StoppingCriteriaAnalysis/"
				+ problem_.getName() + "/HausdroffDiversity"+"."+problem_.getName();
		File f = new File(fileName);

		try {
			FileWriter fw;
			if (f.exists() && !f.isDirectory()) {

				fw = new FileWriter(f, true);
							
			} else {
				f.createNewFile();
				 fw = new FileWriter(f, false);
			}
			
			final double distaceConstant =0.0005;
			final double pValueConstant=0.2;
			double [] parameters = new double[8];
			Boolean track=new Boolean(false);
			
			for(int i=1;i<=30;i++){
				StringTokenizer st = new StringTokenizer(map.get(i).toString(), "[],");
				String aLine=i+" ";
								
				int j=0;
				if(!track)
					parameters[j++]=(int) i;
				
				while(st.hasMoreTokens()){
					String str=st.nextToken();
					aLine+=str;
					if(!track)
						parameters[j++]=Double.parseDouble(str);
				}
				fw.write(aLine+"\n");
				if(parameters[1]<distaceConstant && parameters[2]>pValueConstant)
					track=true;
				
			}
			fw.write("cut-off point\n");
			fw.write(parameters[0]+" "+parameters[1]+" "+parameters[2]+" "+parameters[3]+"\n\n" );
			fw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		
		
		
		return ranking.getSubfront(0);
	} // execute
} // NSGA-II
