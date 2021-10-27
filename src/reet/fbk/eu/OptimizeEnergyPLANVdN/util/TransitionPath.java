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

public class TransitionPath {

	List<Double[]> Solutions = new ArrayList<Double[]>();
	List<Double[]> SolutionsToReturn = new ArrayList<Double[]>() ;
	List<Double[]> NormalizedSolutions = new ArrayList<Double[]>();
	int numberOfDicicionVariables;
	int numberOf2020Solutions, numberOf2030Solutions, numberOf2050Solutions;
	
	
	TransitionPath(int numberOf2020Solutions,int numberOf2030Solutions,int numberOf2050Solutions, int numberOfDecisionVariables){
		
		this.numberOf2020Solutions=numberOf2020Solutions;
		this.numberOf2030Solutions=numberOf2030Solutions;
		this.numberOf2050Solutions=numberOf2050Solutions;
		this.numberOfDicicionVariables=numberOfDecisionVariables;
		
		
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
	
	/*
	 * This method will find 3 scenarios (for 2020, 2030, 2050) those have minimum distance from (2020 - 2030) + (2030-2050). The index is returned.
	 */
	public void find3ScnaeiosForTransition(){
		int in1=1, in2=-1, in3=-1;
		double totalDistance=0.0;
		double min_dis = Double.MAX_VALUE;
		for(int i=0;i<numberOf2020Solutions;i++){
			for(int j=numberOf2020Solutions;j<numberOf2020Solutions+numberOf2030Solutions;j++){
				double dis_2020To2030 = calculateDistance(NormalizedSolutions.get(i), NormalizedSolutions.get(j));
				for(int z=numberOf2020Solutions+numberOf2030Solutions;z<numberOf2020Solutions+numberOf2030Solutions+numberOf2050Solutions;z++){
					double dis_2030To2050 = calculateDistance(NormalizedSolutions.get(j), NormalizedSolutions.get(z));
					totalDistance = dis_2020To2030 + dis_2030To2050;
					if(totalDistance< min_dis){
						min_dis=totalDistance;
						in1=i;
						in2=j;
						in3=z;
					}
				}
			}
		}
		System.out.println("2020:"+in1+" 2030:"+in2+" 2050:"+in3);
	}
	
	/*
	 * This method will find 2 scenarios (2030, 2050) for each 2020 scenario 
	 */
	public void find2ScnaeiosForTransition(){
		int in1=1, in2=-1, in3=-1;
		double mem_dis2020to2030=-1.0, mem_dis2030to2050=-1.0, mem_totalDistsance=-1.0;
		double totalDistance=0.0;
		double dis_2020To2030=-1.0, dis_2030To2050=-1.0;
		System.out.println("2020\t2030\t2050\tDis 2020-2030\tDis2030-2050\ttotal dis");
		for(int i=0;i<numberOf2020Solutions;i++){
			double min_dis = Double.MAX_VALUE;
			in1=1; in2=-1; in3=-1;
			for(int j=numberOf2020Solutions;j<numberOf2020Solutions+numberOf2030Solutions;j++){
				 dis_2020To2030 = calculateDistance(NormalizedSolutions.get(i), NormalizedSolutions.get(j));
				for(int z=numberOf2020Solutions+numberOf2030Solutions;z<numberOf2020Solutions+numberOf2030Solutions+numberOf2050Solutions;z++){
					dis_2030To2050 = calculateDistance(NormalizedSolutions.get(j), NormalizedSolutions.get(z));
					totalDistance = dis_2020To2030 + dis_2030To2050;
					if(totalDistance< min_dis){
						min_dis=totalDistance;
						in1=i;
						in2=j;
						in3=z;
						mem_dis2020to2030=dis_2020To2030;
						mem_dis2030to2050=dis_2030To2050;
						mem_totalDistsance=totalDistance;
								
					}
				}
			}
			//System.out.println("2020: "+in1+" 2030: "+in2+" 2050: "+in3 +" total distance: "+totalDistance);
			System.out.println(in1+"\t"+in2+"\t"+in3+"\t"+mem_dis2020to2030+"\t"+mem_dis2030to2050+"\t"+mem_totalDistsance);
		}
		
	}
	
	/*
	 * This method will find 2 scenarios (2030, 2030) for each 2020 scenario 
	 */
	public void find2ScnaeiosForTransition2020to2030(){
		int in1=1, in2=-1, in3=-1;
		double mem_dis2020to2030=-1.0, mem_dis2030to2050=-1.0, mem_totalDistsance=-1.0;
		double totalDistance=0.0;
		double dis_2020To2030=-1.0, dis_2030To2050=-1.0;
		System.out.println("2020\t2030\tDis 2020-2030");
		for(int i=0;i<numberOf2020Solutions;i++){
			double min_dis = Double.MAX_VALUE;
			in1=1; in2=-1; in3=-1;
			for(int j=numberOf2020Solutions;j<numberOf2020Solutions+numberOf2030Solutions;j++){
				 dis_2020To2030 = calculateDistance(NormalizedSolutions.get(i), NormalizedSolutions.get(j));
				 if(dis_2020To2030< min_dis){
						min_dis=dis_2020To2030;
						in1=i;
						in2=j;
						mem_dis2020to2030=dis_2020To2030;
								
					}
				}
			System.out.println(in1+"\t"+in2+"\t"+mem_dis2020to2030);	
		}
			//System.out.println("2020: "+in1+" 2030: "+in2+" 2050: "+in3 +" total distance: "+totalDistance);
					
	}
	
	/*
	 * This method will find 2 scenarios (2030, 2050) for each 2020 scenario 
	 */
	public void find2ScnaeiosForTransition2030to2050(){
		int in1=1, in2=-1, in3=-1;
		double mem_dis2020to2030=-1.0, mem_dis2030to2050=-1.0, mem_totalDistsance=-1.0;
		double totalDistance=0.0;
		double dis_2020To2030=-1.0, dis_2030To2050=-1.0;
		System.out.println("2030\t2050\tDis 2030-2050");
		for(int i=numberOf2020Solutions;i<numberOf2020Solutions+numberOf2030Solutions;i++){
			double min_dis = Double.MAX_VALUE;
			in1=1; in2=-1; in3=-1;
			for(int j=numberOf2020Solutions+numberOf2030Solutions;j<numberOf2020Solutions+numberOf2030Solutions+numberOf2050Solutions;j++){
				 dis_2030To2050 = calculateDistance(NormalizedSolutions.get(i), NormalizedSolutions.get(j));
				 if(dis_2030To2050< min_dis){
						min_dis=dis_2030To2050;
						in1=i;
						in2=j;
						mem_dis2020to2030=dis_2030To2050;
								
					}
				}
			System.out.println(in1+"\t"+in2+"\t"+mem_dis2020to2030);	
		}
			//System.out.println("2020: "+in1+" 2030: "+in2+" 2050: "+in3 +" total distance: "+totalDistance);
					
	}
	
	
	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		TransitionPath ob = new TransitionPath(10,10,10,9);
		ob.readSolutions();
		ob.preProcessingIndividuals();
		ob.printNOrmalizedValues();
		//ob.find3ScnaeiosForTransition();
		//ob.find2ScnaeiosForTransition();
		//ob.find2ScnaeiosForTransition2020to2030();
		ob.find2ScnaeiosForTransition2030to2050();
		//System.out.println("Indices");
		//ob.returnIndicesthatToBeKept();
		System.out.println();
	}

}
