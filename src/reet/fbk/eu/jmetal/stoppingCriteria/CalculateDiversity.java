package reet.fbk.eu.jmetal.stoppingCriteria;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	
	public static void main(String[] args){
		CalculateDiversity cd = new CalculateDiversity();
		MultiMap map = new MultiValueMap();
		map = cd.diversityStatisticalTest(7, map);
		
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
		cd.printDiversity();
		
		for(int i=2;i<31;i++){
			System.out.println(i+ " "+ map.get(i).toString());
		}
	}

	double [] array = new double[300];
	List<SolutionVariables> solutionList;

	public CalculateDiversity() {
		solutionList = new ArrayList<SolutionVariables>();

		// TODO Auto-generated constructor stub
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
		double [] array = new double[500];
		for(int i=1;i<=500;i++){
			solutionList.clear();
			readVARFiles("C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/StoppingCriteriaAnalysis/DTLZ1/run0/VAR"+i, numberOfVariables);
			array[i-1]=calculateDiversity(numberOfVariables);
			
		}
		this.array=array;
		
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