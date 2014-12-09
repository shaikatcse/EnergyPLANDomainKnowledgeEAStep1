package reet.fbk.eu.jmetal.stoppingCriteria;

import java.io.BufferedReader;
import java.io.FileReader;
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
import jmetal.qualityIndicator.Hypervolume;

public class GenerationHypervolume {

	public MultiMap calculateHyperVolume(Problem problem, MultiMap map){
		 
		String directoryName="C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/StoppingCriteriaAnalysis/DTLZ1/run0/";
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

	public static void main(String[] args) throws ClassNotFoundException {
		
		GenerationHypervolume gHV= new GenerationHypervolume();
		MultiMap map = new MultiValueMap();
		
		Problem problem = new DTLZ1("Real");
		gHV.calculateHyperVolume(problem, map);
		
		
		for(int i=4;i<=500;i++){
			String str=map.get(i).toString().replaceAll("\\[|\\]","");
			System.out.println(str);
		}
	   
	}

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
