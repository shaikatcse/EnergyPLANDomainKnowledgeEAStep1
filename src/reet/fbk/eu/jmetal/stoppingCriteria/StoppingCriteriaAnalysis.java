package reet.fbk.eu.jmetal.stoppingCriteria;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

	/*
	 * perform good: static final int nPreGen = 30, noGenCheck = 10; static
	 * final double significanceValue = 0.05;
	 */

	static final int nPreGen = 30, noGenCheck = 10;
	static final double significanceValue = 0.05;

	/*
	 * static final int nPreGen = 30, noGenCheck = 5; static final double
	 * significanceValue = 0.05;
	 */

	static int numberOfPopulation = 100;
	static final Map<String, Integer> defaultNoOfGeneration;
	static {
		defaultNoOfGeneration = new HashMap<String, Integer>();
		defaultNoOfGeneration.put("ZDT1", 200);
		defaultNoOfGeneration.put("ZDT2", 200);
		defaultNoOfGeneration.put("ZDT3", 200);
		defaultNoOfGeneration.put("ZDT4", 200);
		defaultNoOfGeneration.put("ZDT6", 200);
		defaultNoOfGeneration.put("DTLZ2", 300);

	}

	public StoppingCriteriaAnalysis() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) throws ClassNotFoundException {
		Problem problem = new DTLZ2("Real");

		doAnalysis(
				"C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/NSGAIISC/"
						+ problem.getName() + "/", problem);
	}

	public static void doAnalysis(String path, Problem problem) {
		AveragedHausdroffDistance ahd = new AveragedHausdroffDistance();
		CalculateDiversity cdv = new CalculateDiversity();
		GenerationHypervolume HV = new GenerationHypervolume(problem);
		GenerationEpsilon eps = new GenerationEpsilon(problem);

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

		System.out.println(HV.trueHV);

		for (int i = 0; i < directories.length; i++) {
			ahdList.clear();
			dvList.clear();

			pValueDVList.clear();
			pValueHDList.clear();

			String extPath = path + directories[i];
			int totalNumberOfFiles = new File(extPath).listFiles().length;
			double pValueHD = -1.0, pValueDV = -1.0;

			boolean track = false;
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
					// if (!checkSignificanceTwoList(pValueDVList,
					// pValueHDList)) {
					if (!checkSignificanceOneList(pValueDVList)) {
						/*
						 * System.out.println("generation " + j);
						 * System.out.println
						 * ("SC: "+HV.calculate_ithGenerationHypervolume
						 * (extPath, j)+
						 * " percentage: "+HV.calculate_ithGenerationHypervolume
						 * (extPath,
						 * j)/HV.trueHV+" "+eps.calculate_ithGenerationEpsilon
						 * (extPath, j)); System.out.println("Without SC: "+HV.
						 * calculate_ithGenerationHypervolume(extPath,
						 * defaultNoOfGeneration.get(problem.getName()))+ " " +
						 * eps.calculate_ithGenerationEpsilon(extPath,
						 * defaultNoOfGeneration.get(problem.getName()))) ;
						 */

						System.out
								.println(i
										+ " "
										+ j
										+ " "
										+ ((defaultNoOfGeneration.get(problem
												.getName()) - j) * numberOfPopulation)
										+ " "
										+ (HV.calculate_ithGenerationHypervolume(
												extPath, defaultNoOfGeneration
														.get(problem.getName())) - HV
												.calculate_ithGenerationHypervolume(
														extPath, j))
										+ " "
										+ HV.calculate_ithGenerationHypervolume(
												extPath, j)
										/ HV.trueHV
										+ " "
										+ (eps.calculate_ithGenerationEpsilon(
												extPath, defaultNoOfGeneration
														.get(problem.getName())) - eps
												.calculate_ithGenerationEpsilon(
														extPath, j)));

						track = true;

						break;
					}
				}

			}
			if (!track) {
				System.out
						.println(i
								+ " "
								+ 500
								+ " "
								+ ((defaultNoOfGeneration.get(problem.getName()) - 500) * numberOfPopulation)
								+ " "
								+ (HV.calculate_ithGenerationHypervolume(
										extPath, defaultNoOfGeneration
												.get(problem.getName())) - HV
										.calculate_ithGenerationHypervolume(
												extPath, 500))
								+ " "
								+ HV.calculate_ithGenerationHypervolume(
										extPath, 500)
								/ HV.trueHV
								+ " "
								+ (eps.calculate_ithGenerationEpsilon(extPath,
										defaultNoOfGeneration.get(problem
												.getName())) - eps
										.calculate_ithGenerationEpsilon(
												extPath, 500)));
			}

			/*
			 * System.out.println("Hausdroff"); for(int
			 * z=0;z<ahdList.size();z++) System.out.println(ahdList.get(z));
			 * System.out.println("Diversity"); for(int z=0;z<dvList.size();z++)
			 * System.out.println(dvList.get(z));
			 */

		}
	}

	public static boolean checkSignificanceTwoList(List<Double> pValueDVList,
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

	public static boolean checkSignificanceOneList(List<Double> pValue) {
		boolean track = false;
		for (int i = 0; i < pValue.size(); i++) {
			if (pValue.get(i) < significanceValue) {
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
