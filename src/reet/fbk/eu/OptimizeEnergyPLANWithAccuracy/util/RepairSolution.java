package reet.fbk.eu.OptimizeEnergyPLANWithAccuracy.util;

import jmetal.core.Solution;
import jmetal.core.Variable;

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
		// repiar the fuel share
		Variable fuelShare[] = repairFuelGene.doRepair(
				solution.getDecisionVariables()[4],
				solution.getDecisionVariables()[5],
				solution.getDecisionVariables()[6]);

		for (int j = 0; j < 3; j++) {
			solution.getDecisionVariables()[j] = fuelShare[j];
		}
		
		return solution;
	}
}
