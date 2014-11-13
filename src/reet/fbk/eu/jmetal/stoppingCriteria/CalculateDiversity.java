package reet.fbk.eu.jmetal.stoppingCriteria;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

import jmetal.core.Solution;

class SolutionVariables {
	double variable[] = new double[3] ;

	public SolutionVariables() {

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

	List<SolutionVariables> solutionList;

	public CalculateDiversity() {
		solutionList = new ArrayList<SolutionVariables>();

		// TODO Auto-generated constructor stub
	}

	void readVARFiles(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;

			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				StringTokenizer st = new StringTokenizer(line);
				int i = 0;
				SolutionVariables sv = new SolutionVariables() ;
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
	
	double calculateDiversity(){
		final int numberOfVariables=3;
		double total=0;
		for(int i=0;i<numberOfVariables;i++){
			total+=(calculateXkSquareVar(i) -Math.pow(calculationXkvar(i),2));
			
		}
		double diversity = Math.sqrt(total)/numberOfVariables;
		return diversity;
	}
	public static void main(String[] args){
		CalculateDiversity cd = new CalculateDiversity();
		//cd.solutionList.clear();
		String output="";
		double [] array = new double[100];
		for(int i=1;i<100;i++){
			cd.solutionList.clear();;
			cd.readVARFiles("generationVar\\VAR"+i);
			output=output+","+cd.calculateDiversity();
			array[i-1]=cd.calculateDiversity();
			
		}
		KolmogorovSmirnovTest kst =  new KolmogorovSmirnovTest();
		for(int i=30;i<100;i++){
			double []x = Arrays.copyOfRange(array, 0/*(i-30)*/, i);
			double []y=Arrays.copyOfRange(array, i, i+5);
			//System.out.println(Arrays.toString(x));
			//System.out.println(Arrays.toString(y));
			double pValue=kst.kolmogorovSmirnovTest(x, y);
			System.out.println(pValue);
		}
		//System.out.println(output);
	}
}
