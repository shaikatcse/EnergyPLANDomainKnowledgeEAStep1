package reet.fbk.eu.OptimizeEnergyPLANTrentoProvince.util;

import java.math.BigInteger;
import java.util.Random;

import com.sun.org.glassfish.external.statistics.RangeStatistic;

public class Test {

	public static void main(String args[]) {
	
		long totalKMRunByCars = 10071000000L;
		Random r = new Random();
		
		long lowerBoundsTrans[]= {2000000000L, 2000000000L, 1000000000L  };
		long upperBoundsTrans[]= {4000000000L, 5000000000L, 2000000000L };
		long ranges[] = new long[lowerBoundsTrans.length];
		for(int i=0;i<ranges.length;i++) {
			ranges[i] = upperBoundsTrans[i]-lowerBoundsTrans[i];
		}
		
		long array[] = new long[lowerBoundsTrans.length];
		
	for(int z=0;z<1000;z++) {	
		for(int i=0;i<array.length;i++) {
			
			double randomValue = lowerBoundsTrans[i] + (long) ((upperBoundsTrans[i] - lowerBoundsTrans[i]) * r.nextDouble());		
			array[i] = (long) randomValue;
			
		}
		
		while(Math.abs(addArray(array) - totalKMRunByCars) >10) {
		long arraySum = addArray(array);
		if(arraySum>totalKMRunByCars) {
			//we need to reduce from each technology
			for(int i=0;i<array.length;i++) {
				BigInteger a = new BigInteger(Long.toString(arraySum-totalKMRunByCars));
				BigInteger b = new  BigInteger(Long.toString(ranges[i]));
				BigInteger	c =	a.multiply(b);
				BigInteger d = new  BigInteger(Long.toString(addArray(ranges)));
				BigInteger e = c.divide(d);
				long reduceForThisTech = e.longValue();
				if((array[i] - reduceForThisTech) < lowerBoundsTrans[i]) {
					array[i] = lowerBoundsTrans[i];
				}else {
					array[i]=array[i]-reduceForThisTech;
				}
			}
			
		}else {
			//we need to increase from each technology
			for(int i=0;i<array.length;i++) {
				BigInteger a = new BigInteger(Long.toString( totalKMRunByCars-arraySum ));
				BigInteger b = new  BigInteger(Long.toString(ranges[i]));
				BigInteger	c =	a.multiply(b);
				BigInteger d = new  BigInteger(Long.toString(addArray(ranges)));
				BigInteger e = c.divide(d);
				long increaseForThisTech = e.longValue();
				if((array[i] + increaseForThisTech) > upperBoundsTrans[i]) {
					array[i] = upperBoundsTrans[i];
				}else {
					array[i]=array[i]+increaseForThisTech;
				}
			}
		}
	}
	
	System.out.println(array[0]+" "+array[1]+" "+array[2]+" "+Math.abs(totalKMRunByCars-addArray(array)));
	
	}
	//	heatCalculation();
	
	}
	
	static void heatCalculation() {
		Random r = new Random();
		
		
		double totalHeatDemand = 6.285; // in TWh
		
		double lowerBoundsHeat[]= {0.00, 2.00, 0.30, 1.0, 0.0 };
		double upperBoundsHeat[]= {0.20, 4.50, 1.50, 1.60, 1.0};
		double ranges[] = new double[lowerBoundsHeat.length];
		for(int i=0;i<ranges.length;i++) {
			ranges[i] = upperBoundsHeat[i]-lowerBoundsHeat[i];
		}
		
		double array[] = new double[lowerBoundsHeat.length];
		
		for(int z=0;z<1000;z++) {
			for(int i=0;i<array.length;i++) {
				double randomValue = lowerBoundsHeat[i] + (upperBoundsHeat[i] - lowerBoundsHeat[i]) * r.nextDouble();		
				array[i] = randomValue;
				
			}
			
			while(Math.abs(addArray(array) - totalHeatDemand) >0.005) {
			double arraySum = addArray(array);
			if(arraySum>totalHeatDemand) {
				//we need to reduce from each technology
				for(int i=0;i<array.length;i++) {
					double reduceForThisTech = (arraySum-totalHeatDemand)*ranges[i]/addArray(ranges);
					if((array[i] - reduceForThisTech) < lowerBoundsHeat[i]) {
						array[i] = lowerBoundsHeat[i];
					}else {
						array[i]=array[i]-reduceForThisTech;
					}
				}
				
			}else {
				//we need to increase from each technology
				for(int i=0;i<array.length;i++) {
					double increaseForThisTech = (totalHeatDemand-arraySum)*ranges[i]/addArray(ranges);
					if((array[i] + increaseForThisTech) > upperBoundsHeat[i]) {
						array[i] = upperBoundsHeat[i];
					}else {
						array[i]=array[i]+increaseForThisTech;
					}
				}
			}
		}
		

			System.out.println(array[0]+" "+array[1]+" "+array[2]+" "+Math.abs(addArray(array)));
		}
	}
	
	static double addArray(double []arr) {
		double sum=0.0;
		for(int i=0;i<arr.length;i++) {
			sum+=arr[i];
		}
		return sum;
	}
	static long addArray(long []arr) {
		long sum=0L;
		for(int i=0;i<arr.length;i++) {
			sum+=arr[i];
		}
		return sum;
	}


}
