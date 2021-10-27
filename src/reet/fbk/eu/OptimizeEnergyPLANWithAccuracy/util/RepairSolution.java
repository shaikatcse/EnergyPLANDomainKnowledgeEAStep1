package reet.fbk.eu.OptimizeEnergyPLANWithAccuracy.util;

import java.util.HashMap;







import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.variable.Real;
import jmetal.problems.singleObjective.Sphere;
import jmetal.util.JMException;




public class RepairSolution {

	// for repairing decision variable regarding Off-shaore wind, on-shore wind,
	// solar and PP
	private RepairDVGene repairDVGene;
	// for repairing decision variable regarding share of fuel
	private RepairFuelGene repairFuelGene;

	final int stepSizeForDV = 50;
	final double stepSizeForFuelShare = 0.1;
	
	public RepairSolution() {
		repairDVGene = new RepairDVGene(stepSizeForDV);
		repairFuelGene = new RepairFuelGene(stepSizeForFuelShare);

	}

	public Solution doRepair(Solution solution) {
		// repair decision variables
		for (int j = 0; j < 4; j++) {
			solution.getDecisionVariables()[j] = repairDVGene
					.doRepair(solution.getDecisionVariables()[j]);

		}
		// repair the fuel share
		
		Variable fuelShare[] = repairFuelGene.doRepair(
				solution.getDecisionVariables()[4],
				solution.getDecisionVariables()[5],
				solution.getDecisionVariables()[6]);

		for (int j = 4; j < 7; j++) {
			solution.getDecisionVariables()[j] = fuelShare[j-4];
		}
		
		return solution;
	}

public static void main(String args[]) throws JMException{
	
		// TODO Auto-generated method stub
		HashMap hm = new HashMap();

		double distributionIndex = 20.0;

		hm.put("probability", 1.0);
		hm.put("distributionIndex", distributionIndex);

		Problem problem = new Sphere("Real", 7);

		Real p1[] = new Real[7];
		p1[0] = new Real();
		p1[1] = new Real();
		p1[2] = new Real();
		p1[3] = new Real();
		p1[4] = new Real();
		p1[5] = new Real();
		p1[6] = new Real();

		p1[0].setValue(1565.12);
		p1[1].setValue(2568.12);
		p1[2].setValue(5648.12);
		p1[3].setValue(2354.12);
		p1[4].setValue(0.124);
		p1[5].setValue(0.874);
		p1[6].setValue(0.446);
		//p1[0].setValue(b);
		
		
		Real p2[] = new Real[1];
		p2[0] = new Real();

		Solution s1 = new Solution();
		s1.setDecisionVariables((Variable[]) p1);
		
		RepairSolution rs=new RepairSolution();
		rs.doRepair(s1);
		
		
}

}
