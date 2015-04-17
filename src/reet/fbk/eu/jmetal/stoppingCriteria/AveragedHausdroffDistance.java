package reet.fbk.eu.jmetal.stoppingCriteria;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import jmetal.core.Problem;
import jmetal.problems.ZDT.ZDT1;
import jmetal.problems.ZDT.ZDT2;
import jmetal.problems.ZDT.ZDT3;
import jmetal.problems.ZDT.ZDT4;
import jmetal.problems.ZDT.ZDT6;
import jmetal.qualityIndicator.Hypervolume;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class AveragedHausdroffDistance {

	/*
	 * ArrayList<Solution> X,Y,Z;
	 * 
	 * public HausdroffDistance(ArrayList<Solution> X, ArrayList<Solution> Y,
	 * ArrayList<Solution> Z) { this.X=X; this.Y=Y; this.Z=Z; }
	 */
	double [] array;
	double [] sum;
	int numberOfGenerations;
	
	Problem problem;
	String path;
	
	GenerationalDistance gd;
	InvertedGenerationalDistance igd;
	
	AveragedHausdroffDistance(String path, Problem problem){
		int totalNumberOfFiles = new File(path+"/run0").listFiles().length;
		array = new double[totalNumberOfFiles/2];
		sum = new double[totalNumberOfFiles/2];
		numberOfGenerations = totalNumberOfFiles/2;
		this.problem = problem;
		this.path = path;
		gd = new GenerationalDistance();
		igd = new InvertedGenerationalDistance();
	}
	
	public AveragedHausdroffDistance() {
		gd = new GenerationalDistance();
		igd = new InvertedGenerationalDistance();
	}

	public MultiMap calcualteAverageHausdroffDistance(int numberOfObjectives, MultiMap map) {
		GenerationalDistance gd = new GenerationalDistance();
		InvertedGenerationalDistance igd = new InvertedGenerationalDistance();
	    
	    // STEP 2. Read the fronts from the files
	   /* double [][] solutionFrontX = gd.utils_.readFront(args[0]);
	    double [][] solutionFrontY = gd.utils_.readFront(args[1]);
	    double [][] trueFront     = gd.utils_.readFront(args[2]);*/
	    
	    /*String directoryName="generationFun\\";
	   String fileName = "FUN"; 
	    
	   for(int i=4;i<300;i++){
	    
		   double [][] solutionFront4 = gd.utils_.readFront(directoryName+fileName+(i-3));
		   double [][] solutionFront3 = gd.utils_.readFront(directoryName+fileName+(i-2));
		    
		   double [][] solutionFront2 = gd.utils_.readFront(directoryName+fileName+(i-1));
	    double [][] solutionFront1 = gd.utils_.readFront(directoryName+fileName+(i));
	    double [][] trueFront     = gd.utils_.readFront(directoryName+fileName+(i));
	   //    double [][] trueFront     = gd.utils_.readFront(directoryName+"reference.txt");
	    
	    trueFront = buildNewFront(trueFront, problem.getNumberOfObjectives());
	    
	    double gdValue1 = gd.generationalDistance( solutionFront1, trueFront,problem.getNumberOfObjectives());
	    double igdValue1 = igd.invertedGenerationalDistance(solutionFront1,trueFront,problem.getNumberOfObjectives());
	    
	    double gdValue2 = gd.generationalDistance( solutionFront2, trueFront,problem.getNumberOfObjectives());
	    double igdValue2 = igd.invertedGenerationalDistance(solutionFront2,trueFront,problem.getNumberOfObjectives());
	    
	    double gdValue3 = gd.generationalDistance( solutionFront3, trueFront,problem.getNumberOfObjectives());
	    double igdValue3 = igd.invertedGenerationalDistance(solutionFront3,trueFront,problem.getNumberOfObjectives());
	    
	    double gdValue4 = gd.generationalDistance( solutionFront4, trueFront,problem.getNumberOfObjectives());
	    double igdValue4 = igd.invertedGenerationalDistance(solutionFront4,trueFront,problem.getNumberOfObjectives());
	    
	    //System.out.println("Average (Z,X)"+Math.max(gdValue, igdValue));
	    
	  map.put(i,Math.max(gdValue2, igdValue2) - Math.max(gdValue1, igdValue1));
	  map.put(i, Math.max(gdValue3, igdValue3)-Math.max(gdValue2, igdValue2) );
	  map.put(i, Math.max(gdValue4, igdValue4)-Math.max(gdValue3, igdValue3) );
	  
	  //  map.put(i,Math.max(gdValue1, igdValue1)+" "+ Math.max(gdValue2, igdValue2)
	    	//	+ " " +Math.max(gdValue3, igdValue3)+ " "+Math.max(gdValue4, igdValue4) );
	    
	   }
		
	   return map;
	   */
		
		   String directoryName="generationFun\\";
		   String fileName = "FUN"; 
		    
		   double [] averageHD = new double[10];
		   int j=1;
		   for(int i=3;i<=300;i++){
		    
			 //  double [][] solutionFront4 = gd.utils_.readFront(directoryName+fileName+(i-3));
			   double [][] solutionFront3 = gd.utils_.readFront(directoryName+fileName+(i-2));
			    
			   double [][] solutionFront2 = gd.utils_.readFront(directoryName+fileName+(i-1));
		  //  double [][] solutionFront1 = gd.utils_.readFront(directoryName+fileName+(i));
		    double [][] trueFront     = gd.utils_.readFront(directoryName+fileName+(i));
		   //    double [][] trueFront     = gd.utils_.readFront(directoryName+"reference.txt");
		    
		 //  trueFront = buildNewFront(trueFront, 2);
		    
		   // double gdValue1 = gd.generationalDistance( solutionFront1, trueFront,3);
		   // double igdValue1 = igd.invertedGenerationalDistance(solutionFront1,trueFront,3);
		    
		    double gdValue2 = gd.generationalDistance( solutionFront2, trueFront,numberOfObjectives);
		    double igdValue2 = igd.invertedGenerationalDistance(solutionFront2,trueFront,numberOfObjectives);
		    
		    double gdValue3 = gd.generationalDistance( solutionFront3, trueFront,3);
		    double igdValue3 = igd.invertedGenerationalDistance(solutionFront3,trueFront,3);
		    
		    //double gdValue4 = gd.generationalDistance( solutionFront4, trueFront,3);
		    //double igdValue4 = igd.invertedGenerationalDistance(solutionFront4,trueFront,3);
		    
		    //System.out.println("Average (Z,X)"+Math.max(gdValue, igdValue));
		    
		    
		    
		    /*System.out.println(i+" "+Math.max(gdValue1, igdValue1)+" "+ Math.max(gdValue2, igdValue2)
		    		+ " " +Math.max(gdValue3, igdValue3)+ " "+Math.max(gdValue4, igdValue4));*/
		    
		//    System.out.println(i+" "/*+Math.max(gdValue1, igdValue1)+" "*/+ Math.max(gdValue2 , igdValue2));
		    	//	+ " " +(gdValue3 + igdValue3)+ " "+(gdValue4+ igdValue4));
		    
		    /*averageHD[i%10] = Math.max(gdValue2 , igdValue2);
		    if(i%10==0){
		    	StandardDeviation sd = new StandardDeviation();
		    	map.put((int)i/10, sd.evaluate(averageHD));
		    	
		    }*/
		    
		    
		    
		   }
		    
		   
		   return map;
	   
	   
	   
	  
	}
	
	double  calclulate_ithGenerationHD(String path, int i,int numberOfObjectives){
		
		  double [][] solutionFront2 = gd.utils_.readFront(path+"/FUN"+i);
		    double [][] solutionFront1 = gd.utils_.readFront(path+"/FUN"+(i-1));
		    
		    double gdValue = gd.generationalDistance( solutionFront1, solutionFront2,numberOfObjectives);
		    double igdValue = igd.invertedGenerationalDistance( solutionFront1, solutionFront2,numberOfObjectives);
		    return Math.max(gdValue,igdValue);
	}
	
	/*
	 * iSolFront -> ith solution front given in array
	 * iMinus1SolFront -> (i-1)th solution front given in array
	 * numberOfObjectives -> number of objectives
	 */
	double calclulate_ithGenerationAHD(double [][] iSolFront,double [][] iMinus1SolFront, int numberOfObjectives){
		
		double gdValue = gd.generationalDistance(iMinus1SolFront, iSolFront, numberOfObjectives);
	    double igdValue = igd.invertedGenerationalDistance( iMinus1SolFront, iSolFront, numberOfObjectives);
	   if(Double.isNaN(gdValue) || Double.isNaN(igdValue))
		   System.out.println("sds");
	    return Math.max(gdValue,igdValue);
	}
	
	void calculateAllGenerationsHD(String path, int numberOfObjectives){
		for(int i=1;i<numberOfGenerations;i++){
			array[i]=calclulate_ithGenerationHD(path, (i+1), numberOfObjectives);
		}
	}
	
	void calculateAverageHDOfAllGenerations(int numberOfObjectives){
		File file = new File(path); 
		String[] directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		
		for(int i=0;i<directories.length;i++){
			calculateAllGenerationsHD(path+"/"+directories[i], numberOfObjectives);
			for(int j=0;j<numberOfGenerations;j++){
				sum[j]+=array[j];
			}
		}
		for(int i=1;i<numberOfGenerations;i++){
			System.out.println((i+1)+ " "+sum[i]/directories.length);
		}
	}
	
	void printAllGenrationHD(){
		for(int i=1;i<array.length;i++){
			System.out.println(array[i]);
		}
			
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		
		 // STEP 1. Create an instance of Generational Distance
		//jmetal.qualityIndicator.util.MetricsUtil utils_= new jmetal.qualityIndicator.util.MetricsUtil();;
	    
		/*GenerationalDistance gd = new GenerationalDistance();
	    InvertedGenerationalDistance igd = new InvertedGenerationalDistance();
	    
	    // STEP 2. Read the fronts from the files
	   /* double [][] solutionFrontX = gd.utils_.readFront(args[0]);
	    double [][] solutionFrontY = gd.utils_.readFront(args[1]);
	    double [][] trueFront     = gd.utils_.readFront(args[2]);*/
	    
	/*    String directoryName="C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/NSGAIISC/ZDT4/run2/";
	   String fileName = "FUN"; 
	    
	   double [] averageHD = new double[10];
	   for(int i=2;i<=300;i++){
	    
		 //double [][] solutionFront4 = gd.utils_.readFront(directoryName+fileName+(i-3));
		   //double [][] solutionFront3 = gd.utils_.readFront(directoryName+fileName+(i-2));
		    
		   double [][] solutionFront2 = gd.utils_.readFront(directoryName+fileName+(i-1));
	    double [][] solutionFront1 = gd.utils_.readFront(directoryName+fileName+(i));
	   // double [][] trueFront     = gd.utils_.readFront(directoryName+fileName+(i));
	   //    double [][] trueFront     = gd.utils_.readFront(directoryName+"reference.txt");
	    
	   //trueFront = buildNewFront(trueFront, 2);
	    
	    /*double gdValue1 = gd.generationalDistance( solutionFront1, trueFront,2);
	    double igdValue1 = igd.invertedGenerationalDistance(solutionFront1,trueFront,2);
	    
	    double gdValue2 = gd.generationalDistance( solutionFront2, trueFront,2);
	    double igdValue2 = igd.invertedGenerationalDistance(solutionFront2,trueFront,2);
	    
	    double gdValue3 = gd.generationalDistance( solutionFront3, trueFront,2);
	    double igdValue3 = igd.invertedGenerationalDistance(solutionFront3,trueFront,2);
	    
	    double gdValue4 = gd.generationalDistance( solutionFront4, trueFront,2);
	    double igdValue4 = igd.invertedGenerationalDistance(solutionFront4,trueFront,2);*/
	    
	  /*  double gdValue = gd.generationalDistance( solutionFront1, solutionFront2,2);
	    double igdValue = igd.invertedGenerationalDistance( solutionFront1, solutionFront2,2);
	    
	    
	    //System.out.println("Average (Z,X)"+Math.max(gdValue, igdValue));
	    
	    
	    
	    /*System.out.println(i+" "+Math.max(gdValue1, igdValue1)+" "+ Math.max(gdValue2, igdValue2)
	    		+ " " +Math.max(gdValue3, igdValue3)+ " "+Math.max(gdValue4, igdValue4));*/
	    
	//    System.out.println(i+" "/*+Math.max(gdValue1, igdValue1)+" "*/+ Math.max(gdValue2 , igdValue2));
	    	//	+ " " +(gdValue3 + igdValue3)+ " "+(gdValue4+ igdValue4));
	    
	   /* averageHD[i%10] = Math.max(gdValue2 , igdValue2);
	    if(i%10==0){
	    	StandardDeviation sd = new StandardDeviation();
	    	
	    	System.out.println(sd.evaluate(averageHD));
	    }*/
	   // System.out.println(i+" "+Math.max(gdValue1,igdValue1)+" "+Math.max(gdValue2,igdValue2)+" "+Math.max(gdValue3,igdValue3) );
	   /* System.out.println(Math.max(gdValue,igdValue));*/
		
		Problem problem = new ZDT6("Real");
		AveragedHausdroffDistance avghd = new AveragedHausdroffDistance("C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/NSGAIISC/ZDT1", problem);
		//avghd.calculateAverageHDOfAllGenerations(problem.getNumberOfObjectives());
		avghd.calculateAllGenerationsHD("C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/NSGAIISC/ZDT1/run1", 2);
		avghd.printAllGenrationHD();
	}
	   
	   /*double [][] X = gd.utils_.readFront(directoryName+"/Test/X.txt");
	   double [][] Y = gd.utils_.readFront(directoryName+"/Test/Y.txt");
	   double [][] Z = gd.utils_.readFront(directoryName+"/Test/Z.txt");
	   
	   double gdValue1 = gd.generationalDistance( X, Z,2);
	    double igdValue1 = igd.invertedGenerationalDistance(X,Z,2);
	    
	    double gdValue2 = gd.generationalDistance( Y, Z,2);
	    double igdValue2 = igd.invertedGenerationalDistance(Y,Z,2);
	    
	   
	   
	   System.out.println("(X,Z)"+Math.max(gdValue1	, igdValue1)+" "+"(Y,Z)"+Math.max(gdValue2	, igdValue2)+" ");
	   
	   System.out.println("(X,Z)"+(gdValue1	+ igdValue1)+" "+"(Y,Z)"+(gdValue2	+ igdValue2)+" ");*/
	   
	   
		
	

	public static double[][] buildNewFront (double [][] front, int numberOfObjectives ){
		double [] maximumValue;
		GenerationalDistance gd = new GenerationalDistance();
		maximumValue = gd.utils_.getMaximumValues(front, numberOfObjectives);
		double scaleFactor = 1.1;
		for(int i=0;i<front.length;i++){
			for(int j=0; j<numberOfObjectives;j++){
				front[i][j]=(front[i][j]-maximumValue[j])*scaleFactor+maximumValue[j];
			}
			//front[i][0]=(front[i][0]-maximumValue[0])*scaleFactor+maximumValue[0];
			//front[i][1]=(front[i][1]-maximumValue[1])*scaleFactor+maximumValue[1];
		}
		/*for(int i=0;i<front.length;i++){
			for(int j=0; j<numberOfObjectives;j++){
				System.out.print(front[i][j]+" ");
			}
			System.out.println("");
		}*/
		return front;
	}
	
	public static double[][] buildReferenceFront(double [][] fornt, int numberOfObjectives){
		
	}
	
	
}
