package reet.fbk.eu.OptimizeEnergyPLANVdN.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.omg.CORBA.NO_IMPLEMENT;

import jmetal.core.Solution;
import jmetal.util.JMException;
import Jama.Matrix;

/*
 * This class is inspired from diversity maximization. Here, a set of some solution is given, the class returns a set of 'n' elements that maximize diversity of decision space 
 */

public class ExtractDiverseSetOfFinalSolutions {

	int numberOfSolutionsToReturn;
	List<Double[]> Solutions = new ArrayList<Double[]>();
	List<Double[]> SolutionsToReturn = new ArrayList<Double[]>() ;
	List<Double[]> NormalizedSolutions = new ArrayList<Double[]>();
	int numberOfDicicionVariables;
	Double M[][];
	double theta;
	int currentMLength;
	
	ExtractDiverseSetOfFinalSolutions(int numberOfSolutionsToReturn, int numberOfDecisionVariables, double theta){
		this.numberOfSolutionsToReturn = numberOfSolutionsToReturn;
		this.numberOfDicicionVariables=numberOfDecisionVariables;
		this.theta=theta;
		
	}
	
	void readSolutions() {

		try {
			BufferedReader br = new BufferedReader(new FileReader("file.txt"));
			String line;

			while ((line = br.readLine()) != null){
				
				StringTokenizer st = new StringTokenizer(line);
				Double[] aSol = new Double[numberOfDicicionVariables];

				int i = 0;
				while (st.hasMoreTokens()) {
					aSol[i++] = Double.parseDouble(st.nextToken());
				}
				Solutions.add(aSol);
			}
				
			br.close();
			
			currentMLength = Solutions.size();
			SolutionsToReturn = new ArrayList<Double[]>(Solutions);
			//Collections.copy(SolutionsToReturn, Solutions);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	
	public void preProcessingIndividuals() {
		
		
		/*Double upperLimit[] = {39861.0,16762.0,	8980.0,92.0,191.0,17709.0,188.8046948,19.37320592,19.37126363};//;new Double [numberOfDicicionVariables];
		Double lowerLimit[] = {938.0,	1.0,7693.0,	0.0,0.0,690.0,0.043430998,	0.008581329,0.006639043};//new Double [numberOfDicicionVariables];*/
		
		Double upperLimit[] = new Double [numberOfDicicionVariables];
		Double lowerLimit[] = new Double [numberOfDicicionVariables];
		
		for(int i=0;i<numberOfDicicionVariables;i++){
			Double arr[]= new Double[Solutions.size()];
			for (int j = 0; j < Solutions.size(); j++) {
				arr[j]=Solutions.get(j)[i];
			}
			
			double max_ = Double.MIN_VALUE;
			double min_ = Double.MAX_VALUE;
			
			for(int z = 0; z < arr.length; z++) {
			      if(arr[z] > max_) {
			         max_ = arr[z];
			      }
			      if(arr[z] < min_) {
				         min_ = arr[z];
				      }
			}
			
			upperLimit[i]=max_;
			lowerLimit[i]=min_;
		}
		
		for (int i = 0; i < Solutions.size(); i++) {
			Double s[]= new Double[numberOfDicicionVariables];
			for(int z=0;z<Solutions.get(i).length;z++){
				s[z] = Solutions.get(i)[z];
			}
			
			for (int j = 0; j < numberOfDicicionVariables; j++) {
				// normalized to 0-1 range
				double oldMax = upperLimit[j];
				double oldMin = lowerLimit[j];
				double newMax = 1.0;
				double newMin = 0.0;
				
				double oldRange = (oldMax - oldMin) ; 
				double newRange = (newMax - newMin); //normalized to 0 - 1  
				
				double oldValue = s[j];
				
				double newValue = (((oldValue - oldMin) * newRange) / oldRange) + newMin;
						
				s[j] = newValue;
				
			}
			
			NormalizedSolutions.add(s);
		
		}
	}
	
	void calculateDiversity(){
		Matrix A = new Matrix(NormalizedSolutions.size(),NormalizedSolutions.size());
		
		for(int j=Solutions.size();j>=numberOfSolutionsToReturn+1;j--){
			A=constructA();
			int trackRowCol=-1;
			Double contributionDiv = Double.MAX_VALUE;
			Matrix AInv=A.inverse();
			for(int i=0;i<currentMLength;i++){
				double [][] AInvEqu = AInv.getArrayCopy();
				
				//take a column
				double  b []= new double[currentMLength];
				int bi=0;
				for(int z=0;z<currentMLength;z++){
					b[bi++] = AInvEqu[z][i];
				}
				
				double sum = 0.0;
				for (double aVal  : b)
				    sum += aVal;
				//s=sum(b)^2/b(i);
				double s = (sum*sum)/b[i];
				if  (s < contributionDiv){
		           contributionDiv = s;
		           //the row and column number is saved
		           trackRowCol = i;
				}
				
			}
			//Delete the row and column from M.
		    //M(trackRowCol,:)=[];
		    //M(:,trackRowCol)=[];
			currentMLength --;
			removeRowColFromM(trackRowCol);
			SolutionsToReturn.remove(trackRowCol);
			System.out.println(trackRowCol);
		}
	}
	
	void removeRowColFromM(int indexToRemove){
	
		Double destinationarr[][] = new Double[currentMLength][currentMLength];
		 
        int REMOVE_ROW = indexToRemove;
        int REMOVE_COLUMN = indexToRemove;
        int p = 0;
        for( int i = 0; i < M.length; ++i)
        {
            if ( i == REMOVE_ROW)
                continue;


            int q = 0;
            for( int j = 0; j < M.length; ++j)
            {
                if ( j == REMOVE_COLUMN)
                    continue;

                destinationarr[p][q] = M[i][j];
                ++q;
            }

            ++p;
        }
        
        M=destinationarr;
	}
	
	Matrix constructA(){
		Matrix A=new Matrix(currentMLength,currentMLength);
		for(int i=0;i<currentMLength;i++){
			Double [] values = M[i];
			for(int j=0;j<values.length;j++){
				A.set(i,j,values[j]);
			}
		}
		return A;
	}
	void calculateeuclideandistance(){
		M=new Double[Solutions.size()][Solutions.size()];
		for(int i=0 ;i<NormalizedSolutions.size();i++){
			for(int j=0;j<NormalizedSolutions.size();j++){
				Double D = calculateDistance(NormalizedSolutions.get(i), NormalizedSolutions.get(j));
				M[i][j]=Math.exp(-theta*D);
			}
		}
	}
	
	void printNOrmalizedValues(){
		for(int i=0;i<NormalizedSolutions.size();i++){
			Double []vals = NormalizedSolutions.get(i);
			for(int j=0;j<vals.length;j++){
				System.out.print(vals[j]+" ");
			}
			System.out.println();
		}
	}
	

	public double calculateDistance(Double[] array1, Double[] array2)
    {
        double Sum = 0.0;
        for(int i=0;i<array1.length;i++) {
           Sum = Sum + Math.pow((array1[i]-array2[i]),2.0);
        }
        return Math.sqrt(Sum);
    }
	
	void returnIndicesthatToBeKept(){
		for(int i=0;i<SolutionsToReturn.size();i++){
			for(int j=0;j<Solutions.size();j++){
				if(SolutionsToReturn.get(i).equals(Solutions.get(j))){
					System.out.println(j);
				}
			}
		}
	}
	
	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		ExtractDiverseSetOfFinalSolutions ob = new ExtractDiverseSetOfFinalSolutions(10,9,6.0);
		ob.readSolutions();
		ob.preProcessingIndividuals();
		ob.printNOrmalizedValues();
		ob.calculateeuclideandistance();
		ob.calculateDiversity();
		System.out.println("Indices");
		ob.returnIndicesthatToBeKept();
		System.out.println();
	}

}
