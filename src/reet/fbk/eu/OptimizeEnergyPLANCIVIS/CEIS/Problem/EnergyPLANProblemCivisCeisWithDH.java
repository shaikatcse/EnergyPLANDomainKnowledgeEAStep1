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

/*
 * The class represent the problem of CEIS where the heat demand is met by District heating with CHP & HP   
 */
public class EnergyPLANProblemCivisCeisWithDH extends Problem {

	MultiMap energyplanmMap;
	public static final double totalHeatdemand = 42.37;
	public static final int boilerLifeTime = 20;
	public static final int PVLifeTime = 25;
	public static final int HydroLifeTime = 20;
	public static final int hp2LifeTime = 20;
	public static final int geoBoreHoleLifeTime = 100;
	public static final double COP = 3.1;
	public static final double interest = 0.04;
	public static final int currentPVCapacity = 5328;
	public static final int currentHydroCapcity = 4000;
	// public static final int currentNumberOfBoilers = 12220;
	//public static final double maxHeatDemandInScaleOf1 = 0.000312745;
	// public static final int currentNumberOfBiomassBoiler = 7290;
	// public static final int currentNumberOfOilBoiler = 3055;
	// public static final int currentNumberOfNgasBoiler = 1875;
	public static final int largeValueOfBoiler = 17500;
	public static final double boilerCostInKEuro = 0.15;
	public static final double hp2CostInKEuro = 2.7;
	public static final double fixedMOForHP2Percentage = 0.002;
	public static final double fixedMOForBoilerinPercentage=0.03;

	/**
	 * Creates a new instance of problem ZDT1.
	 * 
	 * @param numberOfVariables
	 *            Number of variables.
	 * @param solutionType
	 *            The solution type must "Real", "BinaryReal, and "ArrayReal".
	 */
	public EnergyPLANProblemCivisCeisWithDH(String solutionType) {
		/*
		 * 0-> chp2 1 -> hp2 2 -> pv capacity 3 -> electrolyser 2 4 -> hydrogen
		 * strage 2
		 */
		numberOfVariables_ = 3;
		/*
		 * 0 -> CO2 emission 1 -> annual cost 2 -> load following capccity
		 */
		numberOfObjectives_ = 3;
		numberOfConstraints_ =1;

		problemName_ = "OptimizeEnergyPLANCivisCompletewithDH";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		// Establishes upper and lower limits for the variables
		int var;

		// chp2 capacity (KWe)
		lowerLimit_[0] = 0;
		upperLimit_[0] = 6000;

		// hp2 capacity (KWe)
		lowerLimit_[1] = 0;
		upperLimit_[1] = 6000;

		// pv cacpity (KWe)
		lowerLimit_[2] = 5328.0;
		upperLimit_[2] = 15000.0;

		/*// electrolyser 2 (KWe)
		lowerLimit_[3] = 0;
		upperLimit_[3] = 500;

		// hydrogen storage 2 (MWh)
		lowerLimit_[4] = 0;
		upperLimit_[4] = 2000;*/

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
				+ "\".\\src\\reet\\fbk\\eu\\OptimizeEnergyPLANCIVIS\\CEIS\\data\\CEIS_Complete.txt\" "
				+ "-m \"modification.txt\" -ascii \"result.txt\" ";
		try {
			// Process process = new
			// ProcessBuilder(energyPLANrunCommand).start();
			Process process = Runtime.getRuntime().exec(energyPLANrunCommand);
			process.waitFor();
			process.destroy();
			EnergyPLANFileParseForCivis epfp = new EnergyPLANFileParseForCivis(
					".\\result.txt");
			energyplanmMap = epfp.parseFile();

			Iterator it;
			Collection<String> col;

			// extracting maximum Boiler configuration (group # 3)
			/*
			 * col = (Collection<String>) energyplanmMap
			 * .get("Maximumboilerheat r"); it = col.iterator(); double
			 * maximumBoilerGroup3 = Double.parseDouble(it.next() .toString());
			 * modifyModificationFile(maximumBoilerGroup3); //set the decision
			 * variable according to the maximum boiler capacity
			 * solution.getDecisionVariables()[7].setValue(maximumBoilerGroup3);
			 * //run EnergyPLAN for 2nd time, after adjust the boiler3 capacity
			 * process = Runtime.getRuntime().exec(energyPLANrunCommand);
			 * process.waitFor(); process.destroy(); epfp = new
			 * EnergyPLANFileParseForCivis(".\\result.txt"); energyplanmMap =
			 * epfp.parseFile();
			 */

			// objective # 1
			col = (Collection<String>) energyplanmMap
					.get("CO2-emission (corrected)");
			it = col.iterator();
			solution.setObjective(0, Double.parseDouble(it.next().toString()));
			// objective # 2
			col = (Collection<String>) energyplanmMap
					.get("Total variable costs");
			it = col.iterator();
			String totalVariableCostStr = it.next().toString();
			totalVariableCostStr = totalVariableCostStr.substring(0,
					totalVariableCostStr.lastIndexOf("1000"));
			double totalVariableCost = Double.parseDouble(totalVariableCostStr);

			col = (Collection<String>) energyplanmMap
					.get("Fixed operation costs");
			it = col.iterator();
			String fixedOperationalCostStr = it.next().toString();
			fixedOperationalCostStr = fixedOperationalCostStr.substring(0,
					fixedOperationalCostStr.lastIndexOf("1000"));
			double fixedOperationalCost = Double
					.parseDouble(fixedOperationalCostStr);

			// calculate additional cost
			// extrect annual hydro production
			col = (Collection<String>) energyplanmMap.get("AnnualHydropower");
			it = col.iterator();
			double hydroPowerProduction = Double.parseDouble(it.next()
					.toString());
			// extract anual PV production
			col = (Collection<String>) energyplanmMap.get("AnnualPV");
			it = col.iterator();
			double PVproduction = Double.parseDouble(it.next().toString());

			// extract annual import
			col = (Collection<String>) energyplanmMap.get("Annualimport");
			it = col.iterator();
			double Import = Double.parseDouble(it.next().toString());

			// extract annual export
			col = (Collection<String>) energyplanmMap.get("Annualexport");
			it = col.iterator();
			double Export = Double.parseDouble(it.next().toString());
			
			col = (Collection<String>) energyplanmMap.get("Annualchpelec.");
			it = col.iterator();
			double chpElecProduction = Double.parseDouble(it.next().toString());
			
			col = (Collection<String>) energyplanmMap.get("Annualhpelec.");
			it = col.iterator();
			double hpElecConsumption = Double.parseDouble(it.next().toString());

			// calculate additional cost
			// (hydroProduction+PVproduction+Import-Export)*average additional
			// cost (85.74)
			double additionalCost = Math.round((hydroPowerProduction
					+ PVproduction + Import - Export + chpElecProduction ) * 85.74);

			double reductionInvestmentCost;
			// reduced investment cost = current PV
			// capccity *
			// pv cost + Hydro * hydro cost
			// see anual investment cost formual in EnergyPLAN manual

			reductionInvestmentCost = (currentPVCapacity * 3.978 * interest)
					/ (1 - Math.pow((1 + interest), -PVLifeTime))
					+ (currentHydroCapcity * 3.51 * interest)
					/ (1 - Math.pow((1 + interest), -HydroLifeTime));

			// extracting maximum Boiler configuration (group # 3)
			col = (Collection<String>) energyplanmMap.get("Maximumboilerheat");
			it = col.iterator();
			double maximumBoilerGroup2 = Double.parseDouble(it.next()
					.toString());

			reductionInvestmentCost = reductionInvestmentCost + Math
					.round(((largeValueOfBoiler - maximumBoilerGroup2)
							* boilerCostInKEuro * interest)
							/ (1 - Math.pow((1 + interest), -boilerLifeTime)));

			double reduceFixedOMCost = Math
					.round(((largeValueOfBoiler - maximumBoilerGroup2)
							* boilerCostInKEuro * fixedMOForBoilerinPercentage));

			//checking for HP2's access capacity
			if(maximumBoilerGroup2==0){
				col = (Collection<String>) energyplanmMap.get("Maximumhpelec.");
				it = col.iterator();
				double maximumHP2elec = Double.parseDouble(it.next()
						.toString());
				

				reductionInvestmentCost = reductionInvestmentCost + Math
						.round(((solution.getDecisionVariables()[1].getValue() - maximumHP2elec)
								* hp2CostInKEuro * interest)
								/ (1 - Math.pow((1 + interest), -hp2LifeTime)));

				reduceFixedOMCost = reduceFixedOMCost+ Math
						.round(((solution.getDecisionVariables()[1].getValue() - maximumHP2elec)
								* hp2CostInKEuro * fixedMOForHP2Percentage));

				//set the decision variable accordingly
				solution.getDecisionVariables()[1].setValue(maximumHP2elec);
				
			}
			
			
			//double numberOfHeatPump = Math.round(maxHeatDemandInScaleOf1
					//* HPheatDemand * Math.pow(10, 6) / COP);
			//double geoBoreHoleInvestmentCost = (numberOfHeatPump * 3.2 * interest)
			//		/ (1 - Math.pow((1 + interest), -geoBoreHoleLifeTime));

			// extract
			col = (Collection<String>) energyplanmMap
					.get("Annual Investment costs");
			it = col.iterator();
			String invest = it.next().toString();
			String investmentCostStr = invest.substring(0,
					invest.lastIndexOf("1000"));
			double investmentCost = Double.parseDouble(investmentCostStr);
			double realInvestmentCost = investmentCost
					- reductionInvestmentCost ;//+ geoBoreHoleInvestmentCost;

			double actualAnnualCost = totalVariableCost + fixedOperationalCost -reduceFixedOMCost
					+ realInvestmentCost + additionalCost ;

			solution.setObjective(1, actualAnnualCost);

			// 3rd objective
			col = (Collection<String>) energyplanmMap.get("Annualelec.demand");
			it = col.iterator();
			double annualElecDemand = Double.parseDouble(it.next().toString());

			solution.setObjective(2, (Import + Export) / annualElecDemand);

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
						&& !warning
								.equals("Critical Excess Electricity Production"))
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

		// constraints about heat3-balance: balance<=0
		col = (Collection<String>) energyplanmMap.get("Biomass Consumption");
		it = col.iterator();
		double annualBiomassConsumption = Double.parseDouble(it.next().toString());

		double constraints[] = new double[numberOfConstraints_];
		constraints[0] = 56.78 - annualBiomassConsumption;
		
		double totalViolation = 0.0;
		int numberOfViolation = 0;
		for (int i = 0; i < numberOfConstraints_; i++) {
			if (constraints[i] < 0.0) {
				totalViolation += constraints[0];
				numberOfViolation++;
			}
		}
	
		solution.setOverallConstraintViolation(totalViolation);
		solution.setNumberOfViolatedConstraint(numberOfViolation);

	}

	void writeModificationFile(Solution solution) throws JMException {

		// chp2
		double chp2 = solution.getDecisionVariables()[0].getValue();
		// hp2
		double hp2 = solution.getDecisionVariables()[1].getValue();
		// PV
		double pv = solution.getDecisionVariables()[2].getValue();
		/*// electrolyser 2
		double electroluser2 = solution.getDecisionVariables()[3].getValue();
		// hydrogen storage 2
		double hs2 = solution.getDecisionVariables()[0].getValue();*/

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

			/*
			 * str = "input_cap_chp3_el="; bw.write(str); bw.newLine(); str = ""
			 * + (int) Math.round(CHPGr3); bw.write(str); bw.newLine();
			 * 
			 * str = "input_cap_hp3_el="; bw.write(str); bw.newLine(); str = ""
			 * + (int) Math.round(HPGr3); bw.write(str); bw.newLine();
			 * 
			 * str = "input_cap_pp_el="; bw.write(str); bw.newLine(); str = "" +
			 * (int) Math.round(PP); bw.write(str); bw.newLine();
			 */

			str = "input_RES1_capacity=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(pv);
			bw.write(str);
			bw.newLine();

			str = "input_cap_chp2_el=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(chp2);
			bw.write(str);
			bw.newLine();

			str = "input_cap_hp2_el=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(hp2);
			bw.write(str);
			bw.newLine();

		/*	str = "input_cap_elt2_el=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(electroluser2);
			bw.write(str);
			bw.newLine();

			str = "input_H2storage_gr2_cap=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(hs2);
			bw.write(str);
			bw.newLine();*/

			/*
			 * double OilBoilerHeatdemand = 6.51, nGasBoilerHeatDemand=4.00,
			 * biomassBoilerHeatDemand = 15.54; // total heat demand=Oil boiler
			 * + Ngas boiler + Biomass boiler double totalHeatDemand =
			 * OilBoilerHeatdemand + nGasBoilerHeatDemand +
			 * biomassBoilerHeatDemand;
			 * 
			 * if(HP<=OilBoilerHeatdemand){ //reduce Oil boiler double
			 * reducedOilBoilerHeatDemand = OilBoilerHeatdemand - HP; double
			 * oilBoilerFuelConsumption = reducedOilBoilerHeatDemand/0.8; //0.8
			 * is the efficiency
			 * 
			 * str = "input_fuel_Households[2]="; bw.write(str); bw.newLine();
			 * str = "" + oilBoilerFuelConsumption; bw.write(str); bw.newLine();
			 * 
			 * }else if(HP>OilBoilerHeatdemand &&
			 * HP<=OilBoilerHeatdemand+nGasBoilerHeatDemand){ //reduce Ngas
			 * boiler double reducedNGasBoilerHeatDemand =
			 * OilBoilerHeatdemand+nGasBoilerHeatDemand - HP; double
			 * nGasBoilerFuelConsumption = reducedNGasBoilerHeatDemand/0.9;
			 * //0.9 is the efficiency
			 * 
			 * //make oil boiler 0 str = "input_fuel_Households[2]=";
			 * bw.write(str); bw.newLine(); str = "" + 0; bw.write(str);
			 * bw.newLine();
			 * 
			 * str = "input_fuel_Households[3]="; bw.write(str); bw.newLine();
			 * str = "" + nGasBoilerFuelConsumption; bw.write(str);
			 * bw.newLine();
			 * 
			 * }else{ //reduce Biomass boiler
			 * 
			 * double reducedBiomassBoilerHeatDemand = totalHeatDemand - HP;
			 * double biomassBoilerFuelConsumption =
			 * reducedBiomassBoilerHeatDemand/0.7; //0.7 is the efficiency
			 * 
			 * //make oil boiler 0 str = "input_fuel_Households[2]=";
			 * bw.write(str); bw.newLine(); str = "" + 0; bw.write(str);
			 * bw.newLine();
			 * 
			 * //make Ngas boiler 0 str = "input_fuel_Households[3]=";
			 * bw.write(str); bw.newLine(); str = "" + 0; bw.write(str);
			 * bw.newLine();
			 * 
			 * 
			 * str = "input_fuel_Households[4]="; bw.write(str); bw.newLine();
			 * str = "" + biomassBoilerFuelConsumption; bw.write(str);
			 * bw.newLine(); }
			 */

			/*
			 * str = "input_RES2_capacity="; bw.write(str); bw.newLine(); str =
			 * "" + (int) Math.round(offShoreWind); bw.write(str); bw.newLine();
			 * 
			 * str = "input_RES3_capacity="; bw.write(str); bw.newLine(); str =
			 * "" + (int) Math.round(PV); bw.write(str); bw.newLine();
			 * 
			 * str="input_storage_gr3_cap="; bw.write(str); bw.newLine(); str =
			 * "" + (double) Math.round(heatStorageGr3*100)/100; bw.write(str);
			 * bw.newLine();
			 */

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

}
