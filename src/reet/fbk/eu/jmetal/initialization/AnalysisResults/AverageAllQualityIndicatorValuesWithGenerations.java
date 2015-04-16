package reet.fbk.eu.jmetal.initialization.AnalysisResults;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import reet.fbk.eu.OptimizeEnergyPLANWithAccuracy.problem.EnergyPLANProblemStep1;
import reet.fbk.eu.jmetal.initialization.EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.*;

public class AverageAllQualityIndicatorValuesWithGenerations {

	int noOfFiles;

	File fs[];

	File fileAvg;
	FileWriter fwAvg;
	BufferedWriter bwAvg;

	/**
	 * Constructor
	 * 
	 * @param problem
	 *            The problem
	 * @param paretoFrontFile
	 *            Pareto front file
	 */
	public AverageAllQualityIndicatorValuesWithGenerations(String folderName,
			String indicators) {

		fs = new File(folderName + "\\" + indicators).listFiles();

		noOfFiles = fs.length;

		fileAvg = new File(folderName + "\\" + indicators + "\\Avg");

		try {
			if (!fileAvg.exists()) {
				fileAvg.createNewFile();

			}

			fwAvg = new FileWriter(fileAvg.getAbsoluteFile());

			bwAvg = new BufferedWriter(fwAvg);

		} catch (IOException e) {
			e.printStackTrace();
		}

	} // Constructor

	double[] readFile(String path) {
		double data[] = new double[70];
		try {
			/* Open the file */
			FileInputStream fis = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			int i = 0;
			String aux = br.readLine();
			while (aux != null) {
				String str[] = aux.split(" ");
				data[i++] = Double.parseDouble(str[1]);
				aux = br.readLine();
			}
			br.close();

			return data;
		} catch (Exception e) {
			System.out
					.println("jmetal.qualityIndicator.util.readNonDominatedSolutionSet: "
							+ path);
			e.printStackTrace();
		}
		return null;

	}

	public void doAverageCalcutation() throws IOException {
		double[][] allData = new double[70][fs.length];

		for (int i = 0; i < noOfFiles; i++) {
			double data[] = readFile(fs[i].getPath());
			
			for (int j = 0; j<data.length;j++){
				allData[j][i] = data[j];
			}
		}
		
		for (int i = 0; i < 70; i++) {
			double sum=0.0;
			for(int j=0;j<noOfFiles;j++){
				sum+=allData[i][j];
			}
			double avg = sum/noOfFiles;
			bwAvg.write((i+1)+ " " +avg+"\n");
		}
		
		bwAvg.close();
		
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

		AverageAllQualityIndicatorValuesWithGenerations calGD = new AverageAllQualityIndicatorValuesWithGenerations(
				 args[0], "GD");
		AverageAllQualityIndicatorValuesWithGenerations calIGD = new AverageAllQualityIndicatorValuesWithGenerations(
				 args[0], "IGD");
		AverageAllQualityIndicatorValuesWithGenerations calHV = new AverageAllQualityIndicatorValuesWithGenerations(
				 args[0], "HV");
		AverageAllQualityIndicatorValuesWithGenerations calEps = new AverageAllQualityIndicatorValuesWithGenerations(
				 args[0], "Epsilon");
		AverageAllQualityIndicatorValuesWithGenerations calSpd = new AverageAllQualityIndicatorValuesWithGenerations(
				 args[0], "Spread");
		
		
		try {
			calEps.doAverageCalcutation();
			calGD.doAverageCalcutation();
			calIGD.doAverageCalcutation();
			calHV.doAverageCalcutation();
			calSpd.doAverageCalcutation();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
