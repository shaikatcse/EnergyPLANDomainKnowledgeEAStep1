package reet.fbk.eu.OptimizeEnergyPLANWithAccuracy.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jmetal.core.Variable;
import jmetal.util.JMException;

/*
 * This class implement the repair method for the decision variable genes
 * The class will take a gene at a time and map the gene closest to the multiple of 
 * the stepsize 
 * 
 */

public class RepairDVGene {
	int stepSize;

	public RepairDVGene(int stepSize) {
		this.stepSize = stepSize;
	}

	Variable doRepair(Variable gene)
	{
		try {
			double geneOriginalValue = gene.getValue();
			double geneTmpValue = new BigDecimal(geneOriginalValue).setScale(0, RoundingMode.HALF_UP).doubleValue();
			double geneRepairValue = ((geneTmpValue%this.stepSize)>= stepSize/2 ? 
					(geneTmpValue + (stepSize - geneTmpValue% stepSize)): (geneTmpValue- (geneTmpValue%stepSize)));
			gene.setValue(geneRepairValue);
			
		} catch (JMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gene;
		
	}
}
