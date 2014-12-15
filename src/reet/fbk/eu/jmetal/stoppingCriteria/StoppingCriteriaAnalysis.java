package reet.fbk.eu.jmetal.stoppingCriteria;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jmetal.core.Problem;
import jmetal.problems.Fonseca;
import jmetal.problems.DTLZ.DTLZ1;
import jmetal.problems.DTLZ.DTLZ2;
import jmetal.problems.ZDT.ZDT1;
import jmetal.problems.ZDT.ZDT2;
import jmetal.problems.ZDT.ZDT3;
import jmetal.problems.ZDT.ZDT4;
import jmetal.problems.ZDT.ZDT6;
import jmetal.qualityIndicator.Hypervolume;

public class StoppingCriteriaAnalysis {

	static final int nPreGen = 30, noGenCheck = 5;
	static final double significanceValue = 0.05;

	public StoppingCriteriaAnalysis() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) throws ClassNotFoundException {
		Problem problem = new ZDT4("Real");

		doAnalysis(
				"C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/NSGAIISC/"
						+ problem.getName() + "/", problem);
	}

	public static void doAnalysis(String path, Problem problem) {
		AveragedHausdroffDistance ahd = new AveragedHausdroffDistance();
		CalculateDiversity cdv = new CalculateDiversity();
		GenerationHypervolume HV = new GenerationHypervolume(problem);

		// double ahdArray[] = new double [maxGeneration];
		// double dvArray[] = new double[maxGeneration];
		double ahdArray[];
		double dvArray[];
		List<Double> ahdList = new ArrayList<Double>();
		List<Double> dvList = new ArrayList<Double>();
		List<Double> pValueHDList = new LinkedList<Double>();
		List<Double> pValueDVList = new LinkedList<Double>();

		File file = new File(path);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

		for (int i = 0; i < directories.length; i++) {
			ahdList.clear();
			dvList.clear();

			pValueDVList.clear();
			pValueHDList.clear();

			String extPath = path + directories[i];
			int totalNumberOfFiles = new File(extPath).listFiles().length;
			double pValueHD = -1.0, pValueDV = -1.0;

			for (int j = 1; j <= totalNumberOfFiles / 2; j++) {
				Double ahdValue = -1.0, dvVAlue = -1.0;
				if (j != 1) {
					ahdValue = ahd.calclulate_ithGenerationHD(extPath, j,
							problem.getNumberOfObjectives());
					ahdList.add(ahdValue);
				}
				dvVAlue = cdv.calculateDiversityOf_ithGeneration(extPath, j,
						problem.getNumberOfVariables());
				dvList.add(dvVAlue);

				if (j == nPreGen) {
					ahdArray = convertDoubles(ahdList, 0, j - 2);
					dvArray = convertDoubles(dvList, 0, j - 1);

					pValueHD = TtestOnSignificanceOfLinearTrend
							.doSignificanceTest(ahdArray);
					pValueDV = TtestOnSignificanceOfLinearTrend
							.doSignificanceTest(dvArray);

					pValueHDList.add(pValueHD);
					pValueDVList.add(pValueDV);

				}
				if (j > nPreGen) {

					ahdArray = convertDoubles(ahdList, j - nPreGen - 1, j - 2);
					dvArray = convertDoubles(dvList, j - nPreGen, j - 1);

					pValueHD = TtestOnSignificanceOfLinearTrend
							.doSignificanceTest(ahdArray);
					pValueDV = TtestOnSignificanceOfLinearTrend
							.doSignificanceTest(dvArray);

					pValueHDList.add(pValueHD);
					pValueDVList.add(pValueDV);

					if (pValueDVList.size() > noGenCheck)
						pValueDVList.remove(0);
					if (pValueHDList.size() > noGenCheck)
						pValueHDList.remove(0);

				}
				if (pValueDVList.size() >= noGenCheck) {
					if (!checkSignificance(pValueDVList, pValueHDList)) {
						System.out.println("generation " + j);
						System.out.println("SC: "+HV.calculate_ithGenerationHypervolume(extPath, j)+ " percentage: "+HV.calculate_ithGenerationHypervolume(extPath, j)/HV.trueHV);
						System.out.println("Without SC: "+HV.calculate_ithGenerationHypervolume(extPath, 200));
						break;
					}
				}
				

			}
			/*
			 * System.out.println("Hausdroff"); for(int
			 * z=0;z<ahdList.size();z++) System.out.println(ahdList.get(z));
			 * System.out.println("Diversity"); for(int z=0;z<dvList.size();z++)
			 * System.out.println(dvList.get(z));
			 */

		}
	}

	public static boolean checkSignificance(List<Double> pValueDVList,
			List<Double> pValueHDList) {
		boolean track = false;
		for (int i = 0; i < pValueDVList.size(); i++) {
			if (pValueDVList.get(i) < significanceValue
					|| pValueHDList.get(i) < significanceValue) {
				track = true;
				break;
			}

		}
		return track;
	}

	public static double[] convertDoubles(List<Double> doubles, int start,
			int end) {
		double[] ret = new double[end - start + 1];
		int i = 0;
		for (int j = start; j <= end; j++)
			ret[i++] = doubles.get(j);
		return ret;
	}
}
