package reet.fbk.eu.jmetal.stoppingCriteria;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

public class MWTest {

	public MWTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		double [] x = {0.80, 0.83, 1.89, 1.04, 1.45, 1.38, 1.91, 1.64, 0.73, 1.46};
		double[] y={1.15, 0.88, 0.90, 0.74, 1.21};
		
		MannWhitneyUTest mwt =  new MannWhitneyUTest();
		
		double w = mwt.mannWhitneyU(x, y); 
		double p = mwt.mannWhitneyUTest(x, y);
		System.out.println(w+" "+p);
	}

}
