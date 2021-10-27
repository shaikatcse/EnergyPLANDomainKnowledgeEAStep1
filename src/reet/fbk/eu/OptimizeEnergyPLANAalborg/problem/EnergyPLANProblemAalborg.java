package reet.fbk.eu.OptimizeEnergyPLANAalborg.problem;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.variable.ArrayReal;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;
import jmetal.util.wrapper.XReal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import reet.fbk.eu.OprimizeEnergyPLAN.file.parse.EnergyPLANFileParse;

public class EnergyPLANProblemAalborg extends Problem {

	MultiMap energyplanmMap;

	/**
	 * Creates a new instance of problem ZDT1.
	 * 
	 * @param numberOfVariables
	 *            Number of variables.
	 * @param solutionType
	 *            The solution type must "Real", "BinaryReal, and "ArrayReal".
	 */
	public EnergyPLANProblemAalborg(String solutionType) {
		numberOfVariables_ = 8;
		numberOfObjectives_ = 3;
		numberOfConstraints_ = 3;
		problemName_ = "OptimizeEnergyPLANAalborg";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		// Establishes upper and lower limits for the variables
		int var;

		// capacities for CHP, HP, PP
		// index - 0 -> CHP
		// index - 1 -> HP
		// index - 2 -> PP

		for (var = 0; var < 3; var++) {
			// capacities of wind, off-shore wind, PV and condencing power unit
			lowerLimit_[var] = 0.0;
			upperLimit_[var] = 1000.0;
		} // for

		for (; var < numberOfVariables_-1; var++) {
			// capacities of wind (index: 3) , off-shore wind (index: 4) , PV
			// (index: 5)
			lowerLimit_[var] = 0.0;
			upperLimit_[var] = 1500.0;
		} // for
		
		//	capacity of heat storage group 3
		lowerLimit_[var] = 0.0;
		upperLimit_[var] = 5.0;
		
		//capacity for boiler, its a dummy, the value will be set in evaluation methods
		//the boiler capacity do not need to be optimize, it is just use here to print the value
		lowerLimit_[7]=0.0;
		upperLimit_[7]=10000.0;
		
		/*
		 * for (; var < numberOfVariables_; var++) { // share of coal, oil and
		 * natural-gas lowerLimit_[var] = 0.0; upperLimit_[var] = 1.0;
		 * 
		 * }
		 */

		if (solutionType.compareTo("Real") == 0)
			solutionType_ = new RealSolutionType(this);
		else {
			System.out.println("Error: solution type " + solutionType
					+ " invalid");
			System.exit(-1);
		}
	} // constructor end

	/**
	 * Evaluates a solution.
	 * 
	 * @param solution
	 *            The solution to evaluate.
	 * @throws JMException
	 */
	@SuppressWarnings("unchecked")
	public void evaluate(Solution solution) throws JMException {

		writeModificationFile(solution);
		String energyPLANrunCommand = ".\\EnergyPLAN_SEP_2013\\EnergyPLAN.exe -i "
				+ "\".\\src\\reet\\fbk\\eu\\OptimizeEnergyPLANAalborg\\Aalborg_2050_Plan_A_44%ForOptimization.txt\" "
				+ "-m \"modification.txt\" -ascii \"result.txt\" ";
		try {
			// Process process = new
			// ProcessBuilder(energyPLANrunCommand).start();
			Process process = Runtime.getRuntime().exec(energyPLANrunCommand);
			process.waitFor();
			process.destroy();
			EnergyPLANFileParse epfp = new EnergyPLANFileParse(".\\result.txt");
			energyplanmMap = epfp.parseFile();

			Iterator it;
			Collection<String> col;

			// extracting maximum Boiler configuration (group # 3)
			col = (Collection<String>) energyplanmMap
					.get("Maximumboilerheat r");
			it = col.iterator();
			double maximumBoilerGroup3 = Double.parseDouble(it.next()
					.toString());
			modifyModificationFile(maximumBoilerGroup3);
			//set the decision variable according to the maximum boiler capacity
			solution.getDecisionVariables()[7].setValue(maximumBoilerGroup3);
			//run EnergyPLAN for 2nd time, after adjust the boiler3 capacity
			process = Runtime.getRuntime().exec(energyPLANrunCommand);
			process.waitFor();
			process.destroy();
			epfp = new EnergyPLANFileParse(".\\result.txt");
			energyplanmMap = epfp.parseFile();
			
			// objective # 1
			col = (Collection<String>) energyplanmMap
					.get("CO2-emission (corrected)");
			it = col.iterator();
			solution.setObjective(0, Double.parseDouble(it.next().toString()));
			// objective # 2
			col = (Collection<String>) energyplanmMap.get("TOTAL ANNUAL COSTS");
			it = col.iterator();
			solution.setObjective(1, Double.parseDouble(it.next().toString()));
			// objective # 3
			col = (Collection<String>) energyplanmMap
					.get("Annualimport");
			it = col.iterator();
			double annualImport = Double.parseDouble(it.next().toString());
			col = (Collection<String>) energyplanmMap
					.get("Annualppelec.");
			it = col.iterator();
			annualImport = annualImport+Double.parseDouble(it.next().toString());
			
			col = (Collection<String>) energyplanmMap
					.get("Annualexport");
			it = col.iterator();
			double annualExport = Double.parseDouble(it.next().toString());
			col = (Collection<String>) energyplanmMap
					.get("Annualelec.demand");
			it = col.iterator();
			double annualElecDemand = Double.parseDouble(it.next().toString());
						
			solution.setObjective(2, (annualImport+annualExport)/annualElecDemand);

			// check warning
			col = (Collection<String>) energyplanmMap.get("WARNING");
			if (col != null) {
				/*
				 * System.out.println("No warning"); } else {
				 */
				@SuppressWarnings("rawtypes")
				Iterator it3 = col.iterator();
				String warning = it3.next().toString();
				if (!warning.equals("PP too small. Critical import is needed")
						&& !warning
								.equals("Grid Stabilisation requierments are NOT fullfilled"))
					throw new IOException("warning!!" + warning);
				// System.out.println("Warning " + it3.next().toString());

			}
		} catch (IOException e) {
			System.out.println("Energyplan.exe has some problem");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Energyplan interrupted");
		}
	}

	@SuppressWarnings("unchecked")
	public void evaluateConstraints(Solution solution) throws JMException {
		Iterator it;
		Collection<String> col;

		col = (Collection<String>) energyplanmMap.get("Maximumimport");
		it = col.iterator();
		int maximumImport = Integer.parseInt(it.next().toString());
		col = (Collection<String>) energyplanmMap.get("Minimumstab.-load");
		it = col.iterator();
		int mimimumGridStabPercentage = Integer.parseInt(it.next().toString());

		//constraints about heat3-balance: balance<=0
		col = (Collection<String>) energyplanmMap.get("Annualheat3-balance");
		it = col.iterator();
		double annualHeat3Balance = Double.parseDouble(it.next().toString());

		
		double constraints[] = new double[numberOfConstraints_];
		constraints[0] = 160 - maximumImport;
		constraints[1] = mimimumGridStabPercentage - 100;
		constraints[2] = 0 - annualHeat3Balance;
		
		double totalViolation = 0.0;
		int numberOfViolation = 0;
		for (int i = 0; i < numberOfConstraints_; i++) {
			if (constraints[i] < 0.0) {
				totalViolation += constraints[0];
				numberOfViolation++;
			}
		}
		/*
		 * if (constraints[0] < 0.0) {
		 * solution.setOverallConstraintViolation(constrints);
		 * solution.setNumberOfViolatedConstraint(1);
		 */

		solution.setOverallConstraintViolation(totalViolation);
		solution.setNumberOfViolatedConstraint(numberOfViolation);

	}

	void writeModificationFile(Solution solution) throws JMException {

		// DecimalFormat twoDForm = new DecimalFormat("0.00");

		// CHP group 3
		double CHPGr3 = solution.getDecisionVariables()[0].getValue();

		// HP group 3
		double HPGr3 = solution.getDecisionVariables()[1].getValue();

		// PP
		double PP = solution.getDecisionVariables()[2].getValue();

		// wind
		double wind = solution.getDecisionVariables()[3].getValue();
		// off-shore wind
		double offShoreWind = solution.getDecisionVariables()[4].getValue();
		// PV
		double PV = solution.getDecisionVariables()[5].getValue();
		//heat starage group 3
		double heatStorageGr3 = solution.getDecisionVariables()[6].getValue();
		
		/*
		 * // PP coal share double PP_coal_share =
		 * solution.getDecisionVariables()[4].getValue(); // pp oil sahre double
		 * PP_oil_share = solution.getDecisionVariables()[5].getValue(); // pp
		 * Ngas share double PP_ngas_share =
		 * solution.getDecisionVariables()[6].getValue();
		 * 
		 * final double PP_coal_eff=0.35; final double PP_oil_eff=0.45; final
		 * double PP_ngas_eff=0.55;
		 * 
		 * //efficiency calculation for PP //normalized the share double
		 * nor_PP_coal_share = PP_coal_share /
		 * (PP_coal_share+PP_oil_share+PP_ngas_share); double nor_PP_oil_share =
		 * PP_oil_share / (PP_coal_share+PP_oil_share+PP_ngas_share); double
		 * nor_PP_ngas_share = PP_ngas_share /
		 * (PP_coal_share+PP_oil_share+PP_ngas_share);
		 * 
		 * 
		 * double overall_eff_other = ((PP*nor_PP_coal_share)*PP_coal_eff +
		 * (PP*nor_PP_oil_share)*PP_oil_eff +
		 * (PP*nor_PP_ngas_share)*PP_ngas_eff)/PP;
		 * 
		 * //efficiency calculation from Dr. Marco
		 * 
		 * double overall_eff_marco = 1 / ((nor_PP_coal_share/PP_coal_eff) +
		 * (nor_PP_oil_share/PP_oil_eff) + (nor_PP_ngas_share/PP_ngas_eff) ) ;
		 */

		try {

			File file = new File("modification.txt");
			if (file.exists()) {
				file.delete();

			}

			file.createNewFile();

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			String str = "EnergyPLAN version";
			bw.write(str);
			bw.newLine();
			str = "698";
			bw.write(str);
			bw.newLine();

			str = "input_cap_chp3_el=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(CHPGr3);
			bw.write(str);
			bw.newLine();

			str = "input_cap_hp3_el=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(HPGr3);
			bw.write(str);
			bw.newLine();

			str = "input_cap_pp_el=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(PP);
			bw.write(str);
			bw.newLine();

			str = "input_RES1_capacity=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(wind);
			bw.write(str);
			bw.newLine();

			str = "input_RES2_capacity=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(offShoreWind);
			bw.write(str);
			bw.newLine();

			str = "input_RES3_capacity=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(PV);
			bw.write(str);
			bw.newLine();
			
			str="input_storage_gr3_cap=";
			bw.write(str);
			bw.newLine();
			str = "" +  (double) Math.round(heatStorageGr3*100)/100;
			bw.write(str);
			bw.newLine();
			
			/*
			 * str = "input_fuel_PP[1]="; bw.write(str); bw.newLine(); str = ""
			 * + twoDForm.format(PP_coal_share); bw.write(str); bw.newLine();
			 * 
			 * str = "input_fuel_PP[2]="; bw.write(str); bw.newLine(); str = ""
			 * + twoDForm.format(PP_oil_share); bw.write(str); bw.newLine();
			 * 
			 * str = "input_fuel_PP[3]="; bw.write(str); bw.newLine(); str = ""
			 * + twoDForm.format(PP_ngas_share); bw.write(str); bw.newLine();
			 * 
			 * str = "input_eff_pp_el="; bw.write(str); bw.newLine(); str = "" +
			 * twoDForm.format(overall_eff_marco); bw.write(str); bw.newLine();
			 */

			bw.close();
			// file.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void modifyModificationFile(double modification) throws JMException {
		// now only modify boiler in group # 3
		try {

			File file = new File("modification.txt");
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);

			String str = "input_cap_boiler3_th=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(modification);
			bw.write(str);
			bw.newLine();

			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
