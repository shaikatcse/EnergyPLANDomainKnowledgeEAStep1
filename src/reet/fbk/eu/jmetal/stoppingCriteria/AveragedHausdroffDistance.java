package reet.fbk.eu.jmetal.stoppingCriteria;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class AveragedHausdroffDistance {

	/*
	 * ArrayList<Solution> X,Y,Z;
	 * 
	 * public HausdroffDistance(ArrayList<Solution> X, ArrayList<Solution> Y,
	 * ArrayList<Solution> Z) { this.X=X; this.Y=Y; this.Z=Z; }
	 */

	public static void main(String[] args) {
		
		
		 // STEP 1. Create an instance of Generational Distance
		//jmetal.qualityIndicator.util.MetricsUtil utils_= new jmetal.qualityIndicator.util.MetricsUtil();;
	    
		GenerationalDistance gd = new GenerationalDistance();
	    InvertedGenerationalDistance igd = new InvertedGenerationalDistance();
	    
	    // STEP 2. Read the fronts from the files
	   /* double [][] solutionFrontX = gd.utils_.readFront(args[0]);
	    double [][] solutionFrontY = gd.utils_.readFront(args[1]);
	    double [][] trueFront     = gd.utils_.readFront(args[2]);*/
	    
	    String directoryName="generationFun\\CEIS\\";
	   String fileName = "FUN"; 
	    
	   for(int i=4;i<100;i++){
	    
		   double [][] solutionFront4 = gd.utils_.readFront(directoryName+fileName+(i-3));
		   double [][] solutionFront3 = gd.utils_.readFront(directoryName+fileName+(i-2));
		    
		   double [][] solutionFront2 = gd.utils_.readFront(directoryName+fileName+(i-1));
	    double [][] solutionFront1 = gd.utils_.readFront(directoryName+fileName+(i));
	    double [][] trueFront     = gd.utils_.readFront(directoryName+fileName+(i));
	   //    double [][] trueFront     = gd.utils_.readFront(directoryName+"reference.txt");
	    
	    trueFront = buildNewFront(trueFront);
	    
	    double gdValue1 = gd.generationalDistance( solutionFront1, trueFront,2);
	    double igdValue1 = igd.invertedGenerationalDistance(solutionFront1,trueFront,2);
	    
	    double gdValue2 = gd.generationalDistance( solutionFront2, trueFront,2);
	    double igdValue2 = igd.invertedGenerationalDistance(solutionFront2,trueFront,2);
	    
	    double gdValue3 = gd.generationalDistance( solutionFront3, trueFront,2);
	    double igdValue3 = igd.invertedGenerationalDistance(solutionFront3,trueFront,2);
	    
	    double gdValue4 = gd.generationalDistance( solutionFront4, trueFront,2);
	    double igdValue4 = igd.invertedGenerationalDistance(solutionFront4,trueFront,2);
	    
	    //System.out.println("Average (Z,X)"+Math.max(gdValue, igdValue));
	    
	    
	    
	    System.out.println(i+" "+Math.max(gdValue1, igdValue1)+" "+ Math.max(gdValue2, igdValue2)
	    		+ " " +Math.max(gdValue3, igdValue3)+ " "+Math.max(gdValue4, igdValue4));
	}}

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
