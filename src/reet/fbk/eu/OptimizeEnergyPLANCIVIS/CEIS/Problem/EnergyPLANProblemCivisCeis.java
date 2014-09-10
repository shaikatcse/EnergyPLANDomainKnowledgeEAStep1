package reet.fbk.eu.OptimizeEnergyPLANCIVIS.CEIS.Problem;

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

import reet.fbk.eu.OptimizeEnergyPLANCIVIS.ParseFile.*;

public class EnergyPLANProblemCivisCeis extends Problem {

	MultiMap energyplanmMap;

	/**
	 * Creates a new instance of problem ZDT1.
	 * 
	 * @param numberOfVariables
	 *            Number of variables.
	 * @param solutionType
	 *            The solution type must "Real", "BinaryReal, and "ArrayReal".
	 */
	public EnergyPLANProblemCivisCeis(String solutionType) {
		numberOfVariables_ = 2;
		numberOfObjectives_ = 2;
		/*at this moment, there are two objectives,
		0 -> PV
		1 -> Heat pump	
		*/
		//numberOfConstraints_ = 3;
		
		problemName_ = "OptimizeEnergyPLANCivisCeis";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		// Establishes upper and lower limits for the variables
		int var;

		// capacities for CHP, HP, PP
		// index - 0 -> PV
		// index - 1 -> HP
		
		for (var = 0; var < numberOfVariables_-1; var++) {
			// capacities of wind, off-shore wind, PV and condencing power unit
			lowerLimit_[var] = 5327.0;
			upperLimit_[var] = 15000.0;
		} // for
		
		//HP: 0 to 42
		lowerLimit_[1] = 0.0;
		upperLimit_[1] = 33.0;
		

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
				+ "\".\\src\\reet\\fbk\\eu\\OptimizeEnergyPLANCIVIS\\CEIS\\problem\\data\\Civis_CEIS.txt\" "
				+ "-m \"modification.txt\" -ascii \"result.txt\" ";
		try {
			// Process process = new
			// ProcessBuilder(energyPLANrunCommand).start();
			Process process = Runtime.getRuntime().exec(energyPLANrunCommand);
			process.waitFor();
			process.destroy();
			EnergyPLANFileParseForCivis epfp = new EnergyPLANFileParseForCivis(".\\result.txt");
			energyplanmMap = epfp.parseFile();

			Iterator it;
			Collection<String> col;

			// extracting maximum Boiler configuration (group # 3)
			/*col = (Collection<String>) energyplanmMap
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
			epfp = new EnergyPLANFileParseForCivis(".\\result.txt");
			energyplanmMap = epfp.parseFile();*/
			
			// objective # 1
			col = (Collection<String>) energyplanmMap
					.get("CO2-emission (corrected)");
			it = col.iterator();
			solution.setObjective(0, Double.parseDouble(it.next().toString()));
			// objective # 2
			col = (Collection<String>) energyplanmMap.get("TOTAL ANNUAL COSTS");
			it = col.iterator();
			String totalAnnualCost = it.next().toString();
			String extractCost = totalAnnualCost.substring(0, totalAnnualCost.indexOf("1000") );
			
			solution.setObjective(1, Double.parseDouble(extractCost));
			
		
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
								.equals("Grid Stabilisation requierments are NOT fullfilled") 
								&& !warning.equals("Critical Excess Electricity Production"))
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
		/*Iterator it;
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
		}*/
		/*
		 * if (constraints[0] < 0.0) {
		 * solution.setOverallConstraintViolation(constrints);
		 * solution.setNumberOfViolatedConstraint(1);
		 */

	/*	solution.setOverallConstraintViolation(totalViolation);
		solution.setNumberOfViolatedConstraint(numberOfViolation);*/

	}

	void writeModificationFile(Solution solution) throws JMException {

		

		// PV
		double pv = solution.getDecisionVariables()[0].getValue();
		// HP
		double HP = solution.getDecisionVariables()[1].getValue();

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

			/*str = "input_cap_chp3_el=";
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
			bw.newLine();*/

			str = "input_RES1_capacity=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(pv);
			bw.write(str);
			bw.newLine();
			
			str = "input_HH_HP_heat=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(HP);
			bw.write(str);
			bw.newLine();
			
			double totalHeatDemand = 7.65 + 4.17 + 20.87;
			
			if(HP<7.65){
				//reduce Oil boiler
				double oilBoilerHeatDemand = 7.65 - HP;
				double oilBoilerFuelConsumption = oilBoilerHeatDemand/0.8; //0.8 is the efficiency
				
				str = "input_fuel_Households[2]=";
				bw.write(str);
				bw.newLine();
				str = "" + oilBoilerFuelConsumption;
				bw.write(str);
				bw.newLine();
				
			}else if(HP>7.65 && HP<11.82){
				//reduce Ngas boiler
				double nGasBoilerHeatDemand = 11.82 - HP;
				double nGasBoilerFuelConsumption = nGasBoilerHeatDemand/0.9; //0.9 is the efficiency
				
				//make oil boiler 0
				str = "input_fuel_Households[2]=";
				bw.write(str);
				bw.newLine();
				str = "" + 0;
				bw.write(str);
				bw.newLine();
				
				str = "input_fuel_Households[3]=";
				bw.write(str);
				bw.newLine();
				str = "" + nGasBoilerFuelConsumption;
				bw.write(str);
				bw.newLine();
				
			}else{
				//reduce Biomass boiler
				
				double biomassBoilerHeatDemand = totalHeatDemand - HP;
				double biomassBoilerFuelConsumption = biomassBoilerHeatDemand/0.7; //0.7 is the efficiency
				
				//make oil boiler 0
				str = "input_fuel_Households[2]=";
				bw.write(str);
				bw.newLine();
				str = "" + 0;
				bw.write(str);
				bw.newLine();
				
				//make Ngas boiler 0
				str = "input_fuel_Households[3]=";
				bw.write(str);
				bw.newLine();
				str = "" + 0;
				bw.write(str);
				bw.newLine();
				
				
				str = "input_fuel_Households[4]=";
				bw.write(str);
				bw.newLine();
				str = "" + biomassBoilerFuelConsumption;
				bw.write(str);
				bw.newLine();
			}
			

			/*str = "input_RES2_capacity=";
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
			bw.newLine();*/
			
			
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
