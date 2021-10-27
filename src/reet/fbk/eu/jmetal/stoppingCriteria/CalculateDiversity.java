package reet.fbk.eu.jmetal.stoppingCriteria;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;

import jmetal.core.Problem;
import jmetal.core.Solution;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

class SolutionVariables {
	double variable[] ;

	public SolutionVariables(int numberOfVariables) {
		variable = new double[30] ;

		// TODO Auto-generated constructor stub
	}

	void setVariable(double value, int position) {
		variable[position] = value;
	}

	double getVariable(int position) {
		return variable[position];
	}
}

public class CalculateDiversity {
	
	double [] array;
	double [] sum;
	int numberOfGenerations;
	
	List<SolutionVariables> solutionList;

	public CalculateDiversity(String path) {
		solutionList = new ArrayList<SolutionVariables>();

		int totalNumberOfFiles = new File(path+"/run0").listFiles().length;
		array = new double[totalNumberOfFiles/2];
		sum = new double[totalNumberOfFiles/2];
		numberOfGenerations = totalNumberOfFiles/2;
		
		// TODO Auto-generated constructor stub
	}
	public CalculateDiversity(){
		solutionList = new ArrayList<SolutionVariables>();

	}
	
	public static void main(String[] args){
		CalculateDiversity cd = new CalculateDiversity("C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/NSGAIISC/ZDT1");
		//cd.calculateAverageOfDiversityOfAllGenerations("C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/NSGAIISC/ZDT6",10);
		cd.calculateDiversityOfAllGenerations("C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/NSGAIISC/ZDT1/run1", 10);
		cd.printDiversity();
		
		//MultiMap map = new MultiValueMap();
		//map = cd.diversityStatisticalTest(10, map);
		
		/*//cd.solutionList.clear();
		String output="";
		double [] array = new double[200];
		for(int i=1;i<200;i++){
			cd.solutionList.clear();;
			cd.readVARFiles("generationVar\\VAR"+i);
			output=output+","+cd.calculateDiversity();
			array[i-1]=cd.calculateDiversity();
			
		}
		KolmogorovSmirnovTest kst =  new KolmogorovSmirnovTest();
		for(int i=30;i<200;i++){
			double []x = Arrays.copyOfRange(array, (i-30), i);
			double []y=Arrays.copyOfRange(array, i-5, i);
			//System.out.println(Arrays.toString(x));
			//System.out.println(Arrays.toString(y));
			double pValue=kst.kolmogorovSmirnovTest(x, y);
			System.out.println(i+" "+pValue);
		}
		//System.out.println(output);*/
	/*	cd.printDiversity();
		
		for(int i=2;i<31;i++){
			System.out.println(i+ " "+ map.get(i).toString());
		}*/
	}

	double calculateDiversityOf_ithGeneration(String path, int i, int numberOfVariables){
		solutionList.clear();
		readVARFiles(path+"/VAR"+(i), numberOfVariables);
		return calculateDiversity(numberOfVariables);
		
	}
	
	double calculateDiversityOf_ithGeneration(double [][] solFrontDS,  int numberOfVariables){
		solutionList.clear();
		for(int i=0;i<solFrontDS.length;i++){
			SolutionVariables sv = new SolutionVariables(numberOfVariables) ;
			for(int j = 0;j<numberOfVariables ; j++){
				sv.setVariable(solFrontDS[i][j], j);
			}
			solutionList.add(sv);
			
		}
		return calculateDiversity(numberOfVariables);
	}
	
	void calculateDiversityOfAllGenerations(String path,int numberOfVariables){
		
		for(int i=0;i<array.length;i++){
			
			array[i]=calculateDiversityOf_ithGeneration(path, i, numberOfVariables);
			
		}
		
	}
	
	void calculateAverageOfDiversityOfAllGenerations(String path, int numberOfVariables){
		File file = new File(path); 
		String[] directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		
		for(int i=0;i<directories.length;i++){
			calculateDiversityOfAllGenerations(path+"/"+directories[i], numberOfVariables);
			for(int j=0;j<numberOfGenerations;j++){
				sum[j]+=array[j];
			}
		}
		for(int i=0;i<numberOfGenerations;i++){
			System.out.println((i+1)+ " "+sum[i]/directories.length);
		}
	}

	void readVARFiles(String fileName, int numberOfVariables) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;

			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				StringTokenizer st = new StringTokenizer(line);
				int i = 0;
				SolutionVariables sv = new SolutionVariables(numberOfVariables) ;
				while (st.hasMoreTokens()) {
					sv.setVariable(Double.parseDouble(st.nextToken()), i);
					i++;
				}
				solutionList.add(sv);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	double calculationXkvar(int variablePosition){
		
		double XkSum=0;
		for(int i =0;i<solutionList.size();i++){
			XkSum+=solutionList.get(i).getVariable(variablePosition);
		}
		return XkSum/solutionList.size();
	}
	double calculateXkSquareVar(int variablePosition){
		double XkSquareSum=0;
		for(int i =0;i<solutionList.size();i++){
			XkSquareSum+=Math.pow(solutionList.get(i).getVariable(variablePosition),2);
		}
		return XkSquareSum/solutionList.size();
	}
	
	double calculateDiversity(int numberOfVariables){
		double total=0;
		for(int i=0;i<numberOfVariables;i++){
			total+=(calculateXkSquareVar(i) -Math.pow(calculationXkvar(i),2));
			
		}
		double diversity = Math.sqrt(total)/numberOfVariables;
		return diversity;
	}
	

	
	public void printDiversity(){
		for(int i=0;i<this.array.length;i++){
			//System.out.println(i+" "+this.array[i]);
			System.out.println(this.array[i]);
		}
			
	}
	
	public MultiMap diversityStatisticalTest(int numberOfVariables, MultiMap map){
		
		
		KolmogorovSmirnovTest kst =  new KolmogorovSmirnovTest();
		//MannWhitneyUTest kst =  new MannWhitneyUTest();
		
		for(int i=2;i<31;i+=1){
			
			double []x = Arrays.copyOfRange(array, i*10-2*10, i*10-10);
			double []y=Arrays.copyOfRange(array, i*10-10, i*10);
			
			//System.out.println(Arrays.toString(x));
			//System.out.println(Arrays.toString(y));
		
						
			//double pValue=kst.kolmogorovSmirnovTest(x, y, false);
			//double dValue = kst.kolmogorovSmirnovStatistic(x, y);
		
			
			
			final double d = kst.kolmogorovSmirnovStatistic(y, x);
			double p=kst.exactP(d, x.length, y.length, false);
			
			//double p =kst.mannWhitneyUTest(x, y);
			
			//String str = p;
			map.put(i, p);
	}
	
		return map;
}
}