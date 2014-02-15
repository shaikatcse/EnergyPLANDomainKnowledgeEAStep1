package reet.fbk.eu.qualityIndicator;

import java.io.File;
import java.io.FilenameFilter;

import reet.fbk.eu.OptimizeEnergyPLANWithAccuracy.problem.EnergyPLANProblemStep1;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.*;

public class CalculateAllQualityIndicatorValue {

	SolutionSet trueParetoFront_;
	double trueParetoFrontHypervolume_;
	Problem problem_;
	jmetal.qualityIndicator.util.MetricsUtil utilities_;

	int noOfFiles;
	File fs[];

	/**
	 * Constructor
	 * 
	 * @param problem
	 *            The problem
	 * @param paretoFrontFile
	 *            Pareto front file
	 */
	public CalculateAllQualityIndicatorValue(Problem problem,
			String paretoFrontFile, String folderName) {
		problem_ = problem;
		utilities_ = new jmetal.qualityIndicator.util.MetricsUtil();
		trueParetoFront_ = utilities_
				.readNonDominatedSolutionSet(paretoFrontFile);

				fs = new File(folderName).listFiles(new FilenameFilter() {
			public boolean accept(File directory, String fileName) {
				return fileName.startsWith("FUN");
			}
		});
				noOfFiles = fs.length;
				
				

	} // Constructor

	public void doCalcutation() {
		for(int i=0;i<noOfFiles;i++){
			
			SolutionSet solutionParetoFront = utilities_
					.readNonDominatedSolutionSet(fs[i].getPath());
			
			System.out.println(i+ " " +fs[i].getName());
			/*System.out.println( "HyperVolume: "+new Hypervolume().hypervolume(solutionParetoFront.writeObjectivesToMatrix(),
                    trueParetoFront_.writeObjectivesToMatrix(),
                    problem_.getNumberOfObjectives()));
			
			System.out.println( "InvertedGenerationalDistance: "+new InvertedGenerationalDistance().invertedGenerationalDistance(solutionParetoFront.writeObjectivesToMatrix(),
                    trueParetoFront_.writeObjectivesToMatrix(),
                    problem_.getNumberOfObjectives()));
			
			System.out.println( "GenerationalDistance: "+new GenerationalDistance().generationalDistance(solutionParetoFront.writeObjectivesToMatrix(),
                    trueParetoFront_.writeObjectivesToMatrix(),
                    problem_.getNumberOfObjectives()));
			
			System.out.println( "Spread: "+ new Spread().spread(solutionParetoFront.writeObjectivesToMatrix(),
                    trueParetoFront_.writeObjectivesToMatrix(),
                    problem_.getNumberOfObjectives()));
			
			System.out.println( "Spread: "+ new Epsilon().epsilon(solutionParetoFront.writeObjectivesToMatrix(),
                    trueParetoFront_.writeObjectivesToMatrix(),
                    problem_.getNumberOfObjectives()));*/
			
			System.out.println( "Spread: "+ new GeneralizedSpread().generalizedSpread(solutionParetoFront.writeObjectivesToMatrix(),
                    trueParetoFront_.writeObjectivesToMatrix(),
                    problem_.getNumberOfObjectives()));
			
			
			
			System.out.println();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Problem problem = new EnergyPLANProblemStep1("Real");
		CalculateAllQualityIndicatorValue calqI = new CalculateAllQualityIndicatorValue(problem, "C:\\Users\\Nusrat\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\truePf\\mergefun.pf", "C:\\Users\\Nusrat\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\truePf\\");
		calqI.doCalcutation();
	}
}
