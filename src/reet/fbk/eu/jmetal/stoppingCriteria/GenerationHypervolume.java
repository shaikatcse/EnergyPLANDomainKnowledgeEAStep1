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
import jmetal.qualityIndicator.Hypervolume;

public class GenerationHypervolume {
	
	double [] array;
	double [] sum;
	int numberOfGenerations;
	double trueHV;
	Hypervolume qualityIndicator; 
	Problem problem;
	String path;
	double [][] trueFront={};
	
	public GenerationHypervolume(String path, Problem problem) {
		
		int totalNumberOfFiles = new File(path+"/run0").listFiles().length;
		array = new double[totalNumberOfFiles/2];
		sum = new double[totalNumberOfFiles/2];
		numberOfGenerations = totalNumberOfFiles/2;
		this.problem = problem;
		this.path = path;
		
			 
		qualityIndicator = new Hypervolume();		 
		
		if(problem.getName().startsWith("ZDT")){
			    trueFront     = qualityIndicator.utils_.readFront("paretoFronts/"+problem.getName()+".pf");
			   }else if(problem.getName().startsWith("DTL")){
				   trueFront     = qualityIndicator.utils_.readFront("paretoFronts/"+problem.getName()+".3D.pf");
			   }else{
				   trueFront  = qualityIndicator.utils_.readFront("paretoFronts/"+problem.getName()+".pf");
			   }
		 trueHV= qualityIndicator.hypervolume(trueFront, trueFront, problem.getNumberOfObjectives());
	
	}
	
	public GenerationHypervolume(Problem problem){
		qualityIndicator = new Hypervolume();		 
		this.problem = problem;
		
		if(problem.getName().startsWith("ZDT")){
			    trueFront     = qualityIndicator.utils_.readFront("paretoFronts/"+problem.getName()+".pf");
			   }else if(problem.getName().startsWith("DTL")){
				   trueFront     = qualityIndicator.utils_.readFront("paretoFronts/"+problem.getName()+".3D.pf");
			   }
		 trueHV= qualityIndicator.hypervolume(trueFront, trueFront, problem.getNumberOfObjectives());
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
	
	double calculate_ithGenerationHypervolume(String path, int i){
		double [][] solutionFront = qualityIndicator.utils_.readFront(path+"/FUN"+i);
		 double value = qualityIndicator.hypervolume(solutionFront,
                 trueFront, problem.getNumberOfObjectives());
		 return value;
	}
	
	void calculateAllGenerationsHV(String path){
		for(int i=0;i<numberOfGenerations;i++){
			array[i]=calculate_ithGenerationHypervolume(path, (i+1));
		}
	}
	
	void calculateAverageHVOfAllGenerations(){
		File file = new File(path); 
		String[] directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		
		for(int i=0;i<directories.length;i++){
			calculateAllGenerationsHV(path+"/"+directories[i]);
			for(int j=0;j<numberOfGenerations;j++){
				sum[j]+=array[j];
			}
		}
		for(int i=0;i<numberOfGenerations;i++){
			System.out.println((i+1)+ " "+sum[i]/directories.length);
		}
	}

	public MultiMap calculateHyperVolume(Problem problem, MultiMap map){
		 
		String directoryName="C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/StoppingCriteriaAnalysis/ZDT6/run0/";
		   String fileName = "FUN"; 
		   
		   Hypervolume qualityIndicator = new Hypervolume();
		   double [][] trueFront={};
		   if(problem.getName().startsWith("ZDT")){
			    trueFront     = qualityIndicator.utils_.readFront("paretoFronts/"+problem.getName()+".pf");
			   }else if(problem.getName().startsWith("DTL")){
				   trueFront     = qualityIndicator.utils_.readFront("paretoFronts/"+problem.getName()+".3D.pf");
			   }
		   double trueHV= qualityIndicator.hypervolume(trueFront, trueFront, problem.getNumberOfObjectives());
		   
		   for(int i=1;i<=500;i+=1){
		    
			   double [][] solutionFront = qualityIndicator.utils_.readFront(directoryName+fileName+i*1);
			  
			  
			    
			    //Obtain delta value
			    double value = qualityIndicator.hypervolume(solutionFront,
			                                  trueFront, problem.getNumberOfObjectives());
			    
			   // map.put(i, value);
			    //map.put(i, trueHV);
			    
			    map.put(i, value/trueHV);
			    
		}	
		   return map;
	}
	
	/*
	 * ArrayList<Solution> X,Y,Z;
	 * 
	 * public HausdroffDistance(ArrayList<Solution> X, ArrayList<Solution> Y,
	 * ArrayList<Solution> Z) { this.X=X; this.Y=Y; this.Z=Z; }
	 */



	public static double[][] buildNewFront (double [][] front ){
		double [] maximumValue;
		GenerationalDistance gd = new GenerationalDistance();
		maximumValue = gd.utils_.getMaximumValues(front, 2);
		double scaleFactor = 1.5;
		for(int i=0;i<front.length;i++){
			front[i][0]=(front[i][0]-maximumValue[0])*scaleFactor+maximumValue[0];
			front[i][1]=(front[i][1]-maximumValue[1])*scaleFactor+maximumValue[1];
		}
		
		return front;
	}
}
