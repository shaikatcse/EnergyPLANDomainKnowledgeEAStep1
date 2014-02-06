package reet.fbk.eu.OprimizeEnergyPLAN.jmetal.operators.mutation.Real;

/*this class simplely implements mutation that favoring RE based on
 * Normal distribution
 * 
 * 
 */

import org.apache.commons.math3.distribution.NormalDistribution;

public class DKMutation {
	
	
	public static void main(String args[]){
		NormalDistribution nd=new NormalDistribution(0, 1);
		System.out.println(nd.density(0.2));
	}
}
