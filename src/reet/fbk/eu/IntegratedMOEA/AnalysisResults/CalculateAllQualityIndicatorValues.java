package reet.fbk.eu.IntegratedMOEA.AnalysisResults;


import java.io.FilenameFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import reet.fbk.eu.IntegratedMOEA.EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.*;

public class CalculateAllQualityIndicatorValues {

	SolutionSet trueParetoFront_;
	double trueParetoFrontHypervolume_;
	Problem problem_;
	jmetal.qualityIndicator.util.MetricsUtil utilities_;

	int noOfFiles;
	
	File fs[];
	
	File fileHV, fileGD, fileIGD, fileSpread, fileEpsilon, fileGenSpread;
	FileWriter fwHV, fwGD, fwIGD, fwSpread, fwEpsilon, fwGenSpread;;
	BufferedWriter bwHV, bwGD, bwIGD, bwSpread, bwEpsilon, bwGenSpread;;

	/**
	 * Constructor
	 * 
	 * @param problem
	 *            The problem
	 * @param paretoFrontFile
	 *            Pareto front file
	 */
	public CalculateAllQualityIndicatorValues(Problem problem,
			String folderName, String paretoFrontFile) {
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
		
		
		fileHV = new File(folderName+"\\HV");
		fileGD = new File(folderName+"\\GD");
		fileIGD = new File(folderName+"\\IGD");
		fileSpread = new File(folderName+"\\Spread");
		fileEpsilon = new File(folderName+"\\Epsilon");
		fileGenSpread = new File(folderName+"\\GenSpread");
	
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
		

	} // Constructor

	public void doCalcutation() throws IOException {
		double HV, IGD, GD, Spread, Epsilon, GS;
		for (int i = 0; i < noOfFiles; i++) {

			SolutionSet solutionParetoFront = utilities_
					.readNonDominatedSolutionSet(fs[i].getPath());

			System.out.println(i + " " + fs[i].getName());
			HV=new Hypervolume().hypervolume(
					solutionParetoFront.writeObjectivesToMatrix(),
					trueParetoFront_.writeObjectivesToMatrix(),
					problem_.getNumberOfObjectives());
			System.out.println("HyperVolume: "+HV); 
			bwHV.write(HV + "\n");

			IGD=new InvertedGenerationalDistance()
			.invertedGenerationalDistance(solutionParetoFront
					.writeObjectivesToMatrix(),
					trueParetoFront_.writeObjectivesToMatrix(),
					problem_.getNumberOfObjectives());
			System.out.println("InvertedGenerationalDistance: "
					+ IGD);
			bwIGD.write(IGD+"\n");
			
			GD=new GenerationalDistance().generationalDistance(
					solutionParetoFront.writeObjectivesToMatrix(),
					trueParetoFront_.writeObjectivesToMatrix(),
					problem_.getNumberOfObjectives());
			System.out.println("GenerationalDistance: "+GD);
			bwGD.write(GD+"\n");
					
			Spread = new Spread().spread(
					solutionParetoFront.writeObjectivesToMatrix(),
					trueParetoFront_.writeObjectivesToMatrix(),
					problem_.getNumberOfObjectives());
			System.out.println("Spread: "+Spread);
			bwSpread.write(Spread+"\n");


			Epsilon = new Epsilon().epsilon(
					solutionParetoFront.writeObjectivesToMatrix(),
					trueParetoFront_.writeObjectivesToMatrix(),
					problem_.getNumberOfObjectives());
			System.out.println("Epsilon: "+Epsilon);
			bwEpsilon.write(Epsilon+"\n");
					
			GS=new GeneralizedSpread().generalizedSpread(
					solutionParetoFront.writeObjectivesToMatrix(),
					trueParetoFront_.writeObjectivesToMatrix(),
					problem_.getNumberOfObjectives());
			System.out.println("Generalized Spread: "+GS);
			bwGenSpread.write(GS+"\n");
			
		
			System.out.println();
		}
		bwHV.close();
		bwIGD.close();
		bwGD.close();
		bwEpsilon.close();
		bwSpread.close();
		bwGenSpread.close();
	}

	/**
	 * @param args
	 *            [0] absolute path of a folder that contain all the FUN files
	 *            for different runs
	 * @param args
	 *            [1] absolute path of the true Pareto front
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Problem problem = new EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution("Real");
		CalculateAllQualityIndicatorValues calqI = new CalculateAllQualityIndicatorValues(
				problem, args[0], args[1]);
		try {
			calqI.doCalcutation();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
