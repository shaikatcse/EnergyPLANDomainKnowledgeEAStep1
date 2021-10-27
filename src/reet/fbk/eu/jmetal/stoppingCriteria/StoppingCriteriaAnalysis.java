package reet.fbk.eu.jmetal.stoppingCriteria;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.text.DefaultEditorKit.BeepAction;

import jmetal.core.Problem;
import jmetal.problems.Fonseca;
import jmetal.problems.DTLZ.DTLZ1;
import jmetal.problems.DTLZ.DTLZ2;
import jmetal.problems.DTLZ.DTLZ3;
import jmetal.problems.DTLZ.DTLZ4;
import jmetal.problems.DTLZ.DTLZ5;
import jmetal.problems.DTLZ.DTLZ6;
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
		defaultNoOfGeneration.put("DTLZ3", 300);
		defaultNoOfGeneration.put("DTLZ4", 300);
		defaultNoOfGeneration.put("DTLZ5", 200);
		defaultNoOfGeneration.put("DTLZ6", 300);

	}

	public StoppingCriteriaAnalysis() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * these four variabres are for second analysis
	 */
	static double HVAHD[] = new double[100];
	static double epsAHD[] = new double[100];
	static double HVAHDAndDV[] = new double[100];
	static double epsAHDAndDV[] = new double[100];
	
	/*
	 * These two variables are for the analysis for default number of generations
	 */
	static double HVD[]= new double[100];
	static double epsD[]= new double[100];
	

	
	public static void main(String args[]) throws ClassNotFoundException,
			IOException {
	
		Problem prob1 = new ZDT1("Real");
		Problem prob2 = new ZDT2("Real");
		Problem prob3 = new ZDT3("Real");
		Problem prob4 = new ZDT4("Real");
		Problem prob5 = new DTLZ2("Real");
		Problem prob6 = new DTLZ5("Real");

		Problem  probArr[] = {prob1, prob2, prob3, prob4, prob5, prob6};
		
		for(int i=0;i<probArr.length;i++){
			/*doAnalysisForAHDAndDV(
				"I:/shahriar/StoppingCriteriaStudies/data/SPEA2SC/"
						+ probArr[i].getName() + "/", probArr[i]);*/
			/*doAnalysis(
					"I:/shahriar/StoppingCriteriaStudies/data/SPEA2SC/"
							+ probArr[i].getName() + "/", probArr[i]);*/
		compareOCDHVdAndEps("C:/Users/mahbub/Desktop/shahriar/StoppingCriteriaStudies/data/SPEA2SC/"+ probArr[i].getName() + "/", probArr[i]);
		}
		
		/*doAnalysisForDefaultNoOfGeneration("I:/shahriar/StoppingCriteriaStudies/data/SPEA2SC/"+
				probArr[3].getName() + "/", probArr[3]);*/
		
	}
	
	public static void compareOCDHVdAndEps(String path, Problem problem) throws IOException{
		
		
		List<Double> HVd = new ArrayList<Double>();
		List<Double> Epsd = new ArrayList<Double>();
		
		GenerationHypervolume HV = new GenerationHypervolume(problem);
		GenerationEpsilon eps = new GenerationEpsilon(problem);
		
		File file = new File(path);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		
		
		// open the OCD files
		BufferedReader br = null;
		String sCurrentLine;
		br = new BufferedReader(new FileReader(path+"stopOCD"));
		int i=0;
		//read from the file
		while ((sCurrentLine = br.readLine()) != null) {
			
			String extPath = path + directories[i];
			int stopGen = Integer.parseInt(sCurrentLine);
			
			HVd.add( HV.calculate_ithGenerationHypervolume(extPath, stopGen)-HV.calculate_ithGenerationHypervolume(
					extPath, defaultNoOfGeneration.get(problem.getName()))) ;
			Epsd.add( eps.calculate_ithGenerationEpsilon(extPath,
					defaultNoOfGeneration.get(problem.getName()))
					- eps.calculate_ithGenerationEpsilon(extPath, stopGen));
			
			i++;
		}
 			
		br.close();
 			
		writeIntoFileForOCDCompare(path, problem, HVd, Epsd);
			
	}
	
	public static void writeIntoFileForOCDCompare(String path, Problem problem, List<Double> HVd, List<Double> Epsd)
			throws IOException {
		
		File fileHV = new File(path + "HVDOCD");
		File fileEps = new File(path + "EpsDOCD");

		FileWriter fwHV = new FileWriter(fileHV.getAbsoluteFile());
		BufferedWriter bwHV = new BufferedWriter(fwHV);

		FileWriter fwEps = new FileWriter(fileEps.getAbsoluteFile());
		BufferedWriter bwEps = new BufferedWriter(fwEps);

	
		for(int i=0; i<HVd.size();i++){
			bwHV.write(HVd.get(i) + "\n");
			bwEps.write(Epsd.get(i)+ "\n");
		}
		
		bwHV.close();
		bwEps.close();
	}

	public static void doAnalysis(String path, Problem problem)
			throws IOException {
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

		
		List<Double> HVd = new ArrayList<Double>();
		List<Double> Epsd = new ArrayList<Double>();
		List<Integer> StopGen = new ArrayList<Integer>();
		
		
		
		File file = new File(path);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

		//System.out.println(HV.trueHV);
		System.out.println(problem.getName());
		
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
					if (!checkSignificanceTwoList(pValueDVList, pValueHDList)) {
						// if (!checkSignificanceOneList(pValueDVList)) {
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
										+ HV.calculate_ithGenerationHypervolume(
												extPath, defaultNoOfGeneration
														.get(problem.getName()))
										/ HV.trueHV
										+ " "
										+ (eps.calculate_ithGenerationEpsilon(
												extPath, defaultNoOfGeneration
														.get(problem.getName())) - eps
												.calculate_ithGenerationEpsilon(
														extPath, j)));

						/*HVD[i] = (HV.calculate_ithGenerationHypervolume(
								extPath,
								defaultNoOfGeneration.get(problem.getName())) - HV
								.calculate_ithGenerationHypervolume(extPath, j));
						epsD[i] = eps.calculate_ithGenerationEpsilon(extPath,
								defaultNoOfGeneration.get(problem.getName()))
								- eps.calculate_ithGenerationEpsilon(extPath, j);*/
						
						/*
						 * new formulation to indicate same direction to have better results
						 * here, larger is better
						 */
						HVd.add( HV.calculate_ithGenerationHypervolume(extPath, j)-HV.calculate_ithGenerationHypervolume(
								extPath, defaultNoOfGeneration.get(problem.getName()))) ;
						Epsd.add( eps.calculate_ithGenerationEpsilon(extPath,
								defaultNoOfGeneration.get(problem.getName()))
								- eps.calculate_ithGenerationEpsilon(extPath, j));
						StopGen.add(j);
						
						track = true;

						break;
					}
				}

			}
		/*	for this experiment this section is blocked
		 * if (!track) {
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
								+ +HV.calculate_ithGenerationHypervolume(
										extPath, defaultNoOfGeneration
												.get(problem.getName()))
								/ HV.trueHV
								+ " "
								+ (eps.calculate_ithGenerationEpsilon(extPath,
										defaultNoOfGeneration.get(problem
												.getName())) - eps
										.calculate_ithGenerationEpsilon(
												extPath, 500)));
				HVD[i] = (HV.calculate_ithGenerationHypervolume(extPath,
						defaultNoOfGeneration.get(problem.getName())) - HV
						.calculate_ithGenerationHypervolume(extPath, 500));
				epsD[i] = eps.calculate_ithGenerationEpsilon(extPath,
						defaultNoOfGeneration.get(problem.getName()))
						- eps.calculate_ithGenerationEpsilon(extPath, 500); 

			}*/

			/*
			 * System.out.println("Hausdroff"); for(int
			 * z=0;z<ahdList.size();z++) System.out.println(ahdList.get(z));
			 * System.out.println("Diversity"); for(int z=0;z<dvList.size();z++)
			 * System.out.println(dvList.get(z));
			 */

		}

		writeIntoFile(path, problem, HVd, Epsd, StopGen);
	}

	public static void doAnalysisForDefaultNoOfGeneration(String path, Problem problem)
			throws IOException {
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

		//System.out.println(HV.trueHV);
		System.out.println(problem.getName());
		
		for (int i = 0; i < directories.length; i++) {
			ahdList.clear();
			dvList.clear();

			pValueDVList.clear();
			pValueHDList.clear();

			String extPath = path + directories[i];
			int totalNumberOfFiles = new File(extPath).listFiles().length;
			double pValueHD = -1.0, pValueDV = -1.0;

			boolean track = false;
			for (int j = 1; j <= defaultNoOfGeneration.get(problem.getName()); j++) {
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
					if (!checkSignificanceTwoList(pValueDVList, pValueHDList)) {
						// if (!checkSignificanceOneList(pValueDVList)) {
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
										+  (HV.calculate_ithGenerationHypervolume(
														extPath, j) - HV.calculate_ithGenerationHypervolume(
																extPath,  defaultNoOfGeneration
																.get(problem.getName())) )
										+ " "
										+ HV.calculate_ithGenerationHypervolume(
												extPath, j)
										/ HV.trueHV
										+ " " 
										+ HV.calculate_ithGenerationHypervolume(
												extPath, defaultNoOfGeneration
														.get(problem.getName()))
										/ HV.trueHV
										+ " "
										+ (eps.calculate_ithGenerationEpsilon(
												extPath, defaultNoOfGeneration
														.get(problem.getName())) - eps
												.calculate_ithGenerationEpsilon(
														extPath, j)));

						HVD[i] = (HV.calculate_ithGenerationHypervolume(
								extPath, j) - HV.calculate_ithGenerationHypervolume(
										extPath,  defaultNoOfGeneration
										.get(problem.getName())) );
						epsD[i] = eps.calculate_ithGenerationEpsilon(extPath,
								defaultNoOfGeneration.get(problem.getName()))
								- eps.calculate_ithGenerationEpsilon(extPath, j);

						track = true;

						break;
					}
				}

			}
			if (!track) {
				System.out
						.println(i
								+ " "
								+ defaultNoOfGeneration.get(problem.getName()) 
								+ " "
								+ 0
								+ " "
								+ 0
								+ " "
								+ HV.calculate_ithGenerationHypervolume(
										extPath, defaultNoOfGeneration.get(problem.getName()))
								/ HV.trueHV
								+ " "
								/*+ +HV.calculate_ithGenerationHypervolume(
										extPath, defaultNoOfGeneration
												.get(problem.getName()))
								/ HV.trueHV
								+ " "*/
								+ 0);
				HVD[i] = 0;
				epsD[i] = 0;

			}

			/*
			 * System.out.println("Hausdroff"); for(int
			 * z=0;z<ahdList.size();z++) System.out.println(ahdList.get(z));
			 * System.out.println("Diversity"); for(int z=0;z<dvList.size();z++)
			 * System.out.println(dvList.get(z));
			 */

		}

		writeIntoFileForDefaultNoOfGeneration(path, problem);
	}

	public static void writeIntoFileForDefaultNoOfGeneration(String path, Problem problem)
			throws IOException {
		File file = new File(path);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

		File fileHV = new File(path + "HVDDef");
		File fileEps = new File(path + "EpsDDef");

		FileWriter fwHV = new FileWriter(fileHV.getAbsoluteFile());
		BufferedWriter bwHV = new BufferedWriter(fwHV);

		FileWriter fwEps = new FileWriter(fileEps.getAbsoluteFile());
		BufferedWriter bwEps = new BufferedWriter(fwEps);

		for (int i = 0; i < directories.length; i++) {
			bwHV.write(HVD[i] + "\n");
			bwEps.write(epsD[i] + "\n");
		}

		bwHV.close();
		bwEps.close();
	}


	
	/*
	 * This is the analysis that measure HV and eps for two criteria. 
	 * 1. only  when AHD is used (objective space) 
	 * 2. AHD and DV is used (objective and
	 * decision space)
	 */
	public static void doAnalysisForAHDAndDV(String path, Problem problem)
			throws IOException {

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
		
		List<Double> HVd = new ArrayList<Double>();
		List<Double> Epsd = new ArrayList<Double>();
		List<Integer> StopGen = new ArrayList<Integer>();

		File file = new File(path);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

	//	System.out.println(HV.trueHV);
		System.out.println(problem.getName());

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
					if (!checkSignificanceOneList( pValueDVList)) {
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
										+ HV.calculate_ithGenerationHypervolume(
												extPath, defaultNoOfGeneration
														.get(problem.getName()))
										/ HV.trueHV
										+ " "
										+ (eps.calculate_ithGenerationEpsilon(
												extPath, defaultNoOfGeneration
														.get(problem.getName())) - eps
												.calculate_ithGenerationEpsilon(
														extPath, j)));

						HVd.add( HV.calculate_ithGenerationHypervolume(extPath, j)-HV.calculate_ithGenerationHypervolume(
								extPath, defaultNoOfGeneration.get(problem.getName()))) ;
						Epsd.add( eps.calculate_ithGenerationEpsilon(extPath,
								defaultNoOfGeneration.get(problem.getName()))
								- eps.calculate_ithGenerationEpsilon(extPath, j));
						StopGen.add(j);

						track = true;

						break;
					}
				}

			}

		/*	if (track) {

				ahdList.clear();

				dvList.clear();

				pValueDVList.clear();
				pValueHDList.clear();

				extPath = path + directories[i];
				totalNumberOfFiles = new File(extPath).listFiles().length;
				pValueHD = -1.0;
				pValueDV = -1.0;

				track = false;
				for (int j = 1; j <= totalNumberOfFiles / 2; j++) {
					Double ahdValue = -1.0, dvVAlue = -1.0;
					if (j != 1) {
						ahdValue = ahd.calclulate_ithGenerationHD(extPath, j,
								problem.getNumberOfObjectives());
						ahdList.add(ahdValue);
					}
					dvVAlue = cdv.calculateDiversityOf_ithGeneration(extPath,
							j, problem.getNumberOfVariables());
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

						ahdArray = convertDoubles(ahdList, j - nPreGen - 1,
								j - 2);
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
						if (!checkSignificanceOneList(pValueHDList)) {
							System.out
									.println(i
											+ " "
											+ j
											+ " "
											+ ((defaultNoOfGeneration
													.get(problem.getName()) - j) * numberOfPopulation)
											+ " "
											+ (HV.calculate_ithGenerationHypervolume(
													extPath,
													defaultNoOfGeneration
															.get(problem
																	.getName())) - HV
													.calculate_ithGenerationHypervolume(
															extPath, j))
											+ " "
											+ HV.calculate_ithGenerationHypervolume(
													extPath, j)
											/ HV.trueHV
											+ " "
											+ HV.calculate_ithGenerationHypervolume(
													extPath,
													defaultNoOfGeneration
															.get(problem
																	.getName()))
											/ HV.trueHV
											+ " "
											+ (eps.calculate_ithGenerationEpsilon(
													extPath,
													defaultNoOfGeneration
															.get(problem
																	.getName())) - eps
													.calculate_ithGenerationEpsilon(
															extPath, j)));

							HVAHD[i] = HV.calculate_ithGenerationHypervolume(
									extPath, j);
							epsAHD[i] = eps.calculate_ithGenerationEpsilon(
									extPath, j);

							track = true;

							break;
						}
					}

				}
			}*/
			/*
			 * if (!track) { System.out .println(i + " " + 500 + " " +
			 * ((defaultNoOfGeneration.get(problem.getName()) - 500) *
			 * numberOfPopulation) + " " +
			 * (HV.calculate_ithGenerationHypervolume( extPath,
			 * defaultNoOfGeneration .get(problem.getName())) - HV
			 * .calculate_ithGenerationHypervolume( extPath, 500)) + " " +
			 * HV.calculate_ithGenerationHypervolume( extPath, 500) / HV.trueHV
			 * + " " + + HV.calculate_ithGenerationHypervolume( extPath,
			 * defaultNoOfGeneration .get(problem.getName())) / HV.trueHV +" " +
			 * (eps.calculate_ithGenerationEpsilon(extPath,
			 * defaultNoOfGeneration.get(problem .getName())) - eps
			 * .calculate_ithGenerationEpsilon( extPath, 500)));
			 * 
			 * HVAHDAndDV[i]=HV.calculate_ithGenerationHypervolume(extPath,
			 * defaultNoOfGeneration.get(problem.getName())); epsAHDAndDV[i]=
			 * eps.calculate_ithGenerationEpsilon(extPath,
			 * defaultNoOfGeneration.get(problem.getName()));
			 * 
			 * }
			 */

			/*
			 * second analysis
			 */

			/*
			 * if (!track) { System.out .println(i + " " + 500 + " " +
			 * ((defaultNoOfGeneration.get(problem.getName()) - 500) *
			 * numberOfPopulation) + " " +
			 * (HV.calculate_ithGenerationHypervolume( extPath,
			 * defaultNoOfGeneration .get(problem.getName())) - HV
			 * .calculate_ithGenerationHypervolume( extPath, 500)) + " " +
			 * HV.calculate_ithGenerationHypervolume( extPath, 500) / HV.trueHV
			 * + " " + + HV.calculate_ithGenerationHypervolume( extPath,
			 * defaultNoOfGeneration .get(problem.getName())) / HV.trueHV +" " +
			 * (eps.calculate_ithGenerationEpsilon(extPath,
			 * defaultNoOfGeneration.get(problem .getName())) - eps
			 * .calculate_ithGenerationEpsilon( extPath, 500))); HVAHD[i]=
			 * HV.calculate_ithGenerationHypervolume(extPath, 500); epsAHD[i]=
			 * eps.calculate_ithGenerationEpsilon(extPath, 500);
			 * 
			 * }
			 */

			/*
			 * System.out.println("Hausdroff"); for(int
			 * z=0;z<ahdList.size();z++) System.out.println(ahdList.get(z));
			 * System.out.println("Diversity"); for(int z=0;z<dvList.size();z++)
			 * System.out.println(dvList.get(z));
			 */

		}

		writeIntoFile(path, problem, HVd, Epsd, StopGen);
	}

	public static void writeIntoFile2ndAnalysis(String path, Problem problem)
			throws IOException {
		File file = new File(path);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

		File fileHVAHDAndDV = new File(path + "HVAHDAndDV");
		File fileEpsAHDAndDV = new File(path + "EpsAHDAndDV");

		File fileHVAHD = new File(path + "HVAHD");
		File fileEpsAHD = new File(path + "EpsAHD");

		FileWriter fwHVAHDAndDV = new FileWriter(
				fileHVAHDAndDV.getAbsoluteFile());
		BufferedWriter bwHVAHDAndDV = new BufferedWriter(fwHVAHDAndDV);

		FileWriter fwEpsAHDAndDV = new FileWriter(
				fileEpsAHDAndDV.getAbsoluteFile());
		BufferedWriter bwEpsAHDAndDV = new BufferedWriter(fwEpsAHDAndDV);

		FileWriter fwHVAHD = new FileWriter(fileHVAHD.getAbsoluteFile());
		BufferedWriter bwHVAHD = new BufferedWriter(fwHVAHD);

		FileWriter fwEpsAHD = new FileWriter(fileEpsAHD.getAbsoluteFile());
		BufferedWriter bwEpsAHD = new BufferedWriter(fwEpsAHD);

		for (int i = 0; i < directories.length; i++) {
			bwHVAHDAndDV.write(HVAHDAndDV[i] + "\n");
			bwEpsAHDAndDV.write(epsAHDAndDV[i] + "\n");

			bwHVAHD.write(HVAHD[i] + "\n");
			bwEpsAHD.write(epsAHD[i] + "\n");

		}

		bwHVAHDAndDV.close();
		bwEpsAHDAndDV.close();

		bwHVAHD.close();
		bwEpsAHD.close();

	}

	public static void writeIntoFile(String path, Problem problem, List<Double> HVd, List<Double> Epsd, List<Integer> stopGen)
			throws IOException {
		//File file = new File(path);
	

		File fileHV = new File(path + "HVDDVNew");
		File fileEps = new File(path + "EpsDDVNew");
		File fileStopGen = new File(path + "StGenDVNew");

		FileWriter fwHV = new FileWriter(fileHV.getAbsoluteFile());
		BufferedWriter bwHV = new BufferedWriter(fwHV);
		

		FileWriter fwEps = new FileWriter(fileEps.getAbsoluteFile());
		BufferedWriter bwEps = new BufferedWriter(fwEps);

		FileWriter fwStopGen = new FileWriter(fileStopGen.getAbsoluteFile());
		BufferedWriter bwStopGen = new BufferedWriter(fwStopGen);
		
		
		for(int i=0; i<HVd.size();i++){
			bwHV.write(HVd.get(i) + "\n");
			bwEps.write(Epsd.get(i)+ "\n");
			bwStopGen.write(stopGen.get(i) + "\n");
		}

		bwHV.close();
		bwEps.close();
		bwStopGen.close();
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
