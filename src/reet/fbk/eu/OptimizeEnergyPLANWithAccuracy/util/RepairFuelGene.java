package reet.fbk.eu.OptimizeEnergyPLANWithAccuracy.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import jmetal.core.Variable;
import jmetal.encodings.variable.Real;
import jmetal.util.JMException;

/*
 * The class represent a doRepair() funtion that can reapir three variables for fuel (now the function is considering about
 * coal, oil and natural-gas, but it is possible to extend it to incorporate boi-mass also). The function receive 3 varibles
 * with double value between 0 to 1. The function maps those variables close to the multiple of given step size. The function  
 * also take care that the summation of three varibales are 1. 
 */

public class RepairFuelGene {
	double stepSize;

	public RepairFuelGene(double stepSize) {
		this.stepSize = stepSize;
	}
	
	public Variable [] doRepair(Variable coal, Variable oil, Variable nGas){
		
		Variable fuelShare[] = new Variable[3];
		fuelShare[0]=coal;
		fuelShare[1]=oil;
		fuelShare[2]=nGas;
		
		double repairedValue[] =new double[3];
		
		try {
			double originalSum=0.0;
			double sum=0.0;
		
			for(int i=0;i<3;i++){
			originalSum += fuelShare[i].getValue();;
			repairedValue[i]= fuelShare[i].getValue();
		}	
			
		for(int i=0;i<3;i++){
			repairedValue[i]=repairedValue[i]/originalSum;
		}
			
		for(int i=0;i<3;i++){
			double tmpValue = new BigDecimal(repairedValue[i]).setScale(2, RoundingMode.HALF_UP).doubleValue();
			repairedValue[i] = ((tmpValue%this.stepSize)>= stepSize/2 ? 
					(tmpValue + (stepSize - tmpValue% stepSize)): (tmpValue- (tmpValue%stepSize)));
			sum+=repairedValue[i];
		}
		
		if(sum!=1.00){
			Random rm=new Random();
			int randomPosition = rm.nextInt()%3;
			if(sum<1.00){
				repairedValue[randomPosition]=repairedValue[randomPosition] + (1.00-sum); 
			}else{
				repairedValue[randomPosition]=repairedValue[randomPosition] - (sum-1.00);
			}
			
			
		}
		/*
		 * now modify back to the gene according the corrected value
		 * 	coal	oil 	ngas	sum
	rand	0.217817721	0.782617313	0.283045762	1.283480796
	scaling	0.169708594	0.609761607	0.220529799	
	rounding	0.15	0.6	0.2	0.95
	modified 	0.192522119	0.770088478	0.256696159	1.219306757

		 */
		for(int i=0;i<3;i++){
			fuelShare[i].setValue(repairedValue[i]*originalSum);
		}
		
		
			}
		catch (JMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fuelShare;
		}
	
//testing 
	
	public static void main(String args[]) throws JMException{
		Variable coal=new Real();
		Variable oil=new Real();
		Variable nGas=new Real();
		
		coal.setValue(0.333333);
		oil.setValue(0.33333);
		nGas.setValue(0.333334);
		
		RepairFuelGene rfg = new RepairFuelGene(0.1);
		Variable reparirdVariable[];
		
		reparirdVariable=rfg.doRepair(coal, oil, nGas);
		
		
	}

}

