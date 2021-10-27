package reet.fbk.eu.jmetal.stoppingCriteria;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import jmetal.core.Problem;
import jmetal.problems.DTLZ.DTLZ1;
import jmetal.problems.ZDT.ZDT1;
import jmetal.problems.ZDT.ZDT2;
import jmetal.problems.ZDT.ZDT3;
import jmetal.problems.ZDT.ZDT4;
import jmetal.problems.ZDT.ZDT6;
import jmetal.qualityIndicator.Epsilon;
import jmetal.qualityIndicator.Hypervolume;

public class GenerationEpsilon {
	
	double [] array;
	double [] sum;
	int numberOfGenerations;
	double trueHV;
	Epsilon qualityIndicator; 
	Problem problem;
	String path;
	double [][] trueFront={};
	
	public GenerationEpsilon(Problem problem){
		qualityIndicator = new Epsilon();		 
		this.problem = problem;
		
		if(problem.getName().startsWith("ZDT")){
			    trueFront     = qualityIndicator.utils_.readFront("paretoFronts/"+problem.getName()+".pf");
			   }else if(problem.getName().startsWith("DTL")){
				   trueFront     = qualityIndicator.utils_.readFront("paretoFronts/"+problem.getName()+".3D.pf");
			   }
		 
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		Problem problem = new ZDT6("Real");
		GenerationHypervolume gHV= new GenerationHypervolume("C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/NSGAIISC/ZDT6", problem);
		
		/*MultiMap map = new MultiValueMap();
		
		Problem problem = new ZDT6("Real");
		gHV.calculateHyperVolume(problem, map);
		
		
		for(int i=1;i<=500;i++){
			String str=map.get(i).toString().replaceAll("\\[|\\]","");
			System.out.println(str);
		}*/
		gHV.calculateAverageHVOfAllGenerations();
	   
	}
	
	double calculate_ithGenerationEpsilon(String path, int i){
		double [][] solutionFront = qualityIndicator.utils_.readFront(path+"/FUN"+i);
		 double value = qualityIndicator.epsilon(solutionFront,
                 trueFront, problem.getNumberOfObjectives());
		 return value;
	}
	

}
