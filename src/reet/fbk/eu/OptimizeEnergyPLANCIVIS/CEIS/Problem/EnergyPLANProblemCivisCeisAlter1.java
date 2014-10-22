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
 * The main difference from EnergyPLANProblemCivisCeis class is that in this simulation HP is not prioritize. 
 * So, boiler are not replace by one after another by HP. 
 * All the boiler parameters and HP are free. But they all together correspond total demands (26.05 GWh).   
 */
public class EnergyPLANProblemCivisCeisAlter1 extends Problem {

	MultiMap energyplanmMap;
	public static final double totalHeatdemand = 26.05;
	public static final int boilerLifeTime = 20;
	public static final int PVLifeTime = 25;
	public static final int HydroLifeTime = 20;
	public static final int geoBoreHoleLifeTime = 100;
	public static final double COP = 3.2;
	public static final double interest = 0.04;
	public static final int currentPVCapacity = 5328;
	public static final int currentHydroCapcity = 4000;
	public static final int currentNumberOfBoilers = 12220;
	public static final double maxHeatDemandInScaleOf1=0.000312745;
	public static final int currentNumberOfBiomassBoiler = 7290;
	public static final int currentNumberOfOilBoiler = 3055;
	public static final int currentNumberOfNgasBoiler = 1875;


	/**
	 * Creates a new instance of problem ZDT1.
	 * 
	 * @param numberOfVariables
	 *            Number of variables.
	 * @param solutionType
	 *            The solution type must "Real", "BinaryReal, and "ArrayReal".
	 */
	public EnergyPLANProblemCivisCeisAlter1(String solutionType) {
		numberOfVariables_ = 5;
		numberOfObjectives_ = 2;
		/*
		 * at this moment, there are two objectives, 0 -> PV (0-10000) 1 -> Heat
		 * pump (0-1) 2 -> Oil Boiler (0-1) 3 -> Ngas BOiler (0-1) 4 -> Biomass
		 * boiler (0-1)
		 */
		// numberOfConstraints_ = 3;

		problemName_ = "OptimizeEnergyPLANCivisCeis";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		// Establishes upper and lower limits for the variables
		int var;

		// capacities for CHP, HP, PP
		// index - 0 -> PV
		// index - 1 -> HP

		// pv capacity
		lowerLimit_[0] = 5328.0;
		upperLimit_[0] = 10000.0;

		for (var = 1; var < numberOfVariables_; var++) {
			// capacities of HP and boilers
			lowerLimit_[var] = 0.0;
			upperLimit_[var] = 1.0;
		} // for

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
				+ "\".\\src\\reet\\fbk\\eu\\OptimizeEnergyPLANCIVIS\\CEIS\\data\\Civis_CEIS.txt\" "
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

			// calculate additional cost
			// (hydroProduction+PVproduction+Import-Export)*average additional
			// cost (85.74)
			double additionalCost = Math.round((hydroPowerProduction
					+ PVproduction + Import - Export) * 85.74);

			double percents[] = new double[4];
			// 0-> HP, 1->oil, 2->Ngas, 3->Boimass
			for (int i = 0; i < 4; i++)
				percents[i] = solution.getDecisionVariables()[i + 1].getValue();

			double total = 0;
			for (int i = 0; i < 4; i++)
				total = total + percents[i];

			double boilerHeatDemands[] = new double[3];
			double HPheatDemand = percents[0] / total * totalHeatdemand;
			for (int i = 1; i < 4; i++)
				boilerHeatDemands[i-1] = percents[i] / total * totalHeatdemand;

			double numberOfBoilerforNewHeatDemand = Math
					.round(maxHeatDemandInScaleOf1
							* (boilerHeatDemands[0] + boilerHeatDemands[1] + boilerHeatDemands[2])
							* Math.pow(10, 6) * 1.5);

			double reductionInvestmentCost;
			if (numberOfBoilerforNewHeatDemand <= currentNumberOfBoilers) {

				// reduced investment cost = number of boiler to meet new heat
				// demand after introducing HP* boiler cost + current PV
				// capccity *
				// pv cost + Hydro * hydro cost
				// see anual investment cost formual in EnergyPLAN manual

				reductionInvestmentCost = (numberOfBoilerforNewHeatDemand * 0.625 * interest)
						/ (1 - Math.pow((1 + interest), -boilerLifeTime))
						+ (currentPVCapacity * 3.978 * interest)
						/ (1 - Math.pow((1 + interest), -PVLifeTime))
						+ (currentHydroCapcity * 3.51 * interest)
						/ (1 - Math.pow((1 + interest), -HydroLifeTime));
			} else {
				reductionInvestmentCost = (currentNumberOfBoilers * 0.625 * interest)
						/ (1 - Math.pow((1 + interest), -boilerLifeTime))
						+ (currentPVCapacity * 3.978 * interest)
						/ (1 - Math.pow((1 + interest), -PVLifeTime))
						+ (currentHydroCapcity * 3.51 * interest)
						/ (1 - Math.pow((1 + interest), -HydroLifeTime));
			}
			
			//additional cost for extra installaition of Biomass boiler
			double additionalCostForBiomassBoiler=0.0;
			double numberOfBiomassBoilerforNewHeatDemand = Math
					.round(maxHeatDemandInScaleOf1
							* ( boilerHeatDemands[2])
							* Math.pow(10, 6) * 1.5);
			if(numberOfBiomassBoilerforNewHeatDemand>currentNumberOfBiomassBoiler)
				additionalCostForBiomassBoiler=0.933*(numberOfBoilerforNewHeatDemand-currentNumberOfBiomassBoiler);
			
			
			//additional cost for extra installaition of Oil boiler
			double additionalCostForOilBoiler=0.0;
			double numberOfOilBoilerforNewHeatDemand = Math
					.round(maxHeatDemandInScaleOf1
							* ( boilerHeatDemands[1])
							* Math.pow(10, 6) * 1.5);
			if(numberOfOilBoilerforNewHeatDemand>currentNumberOfOilBoiler)
				additionalCostForOilBoiler=0.933*(numberOfOilBoilerforNewHeatDemand-currentNumberOfOilBoiler);
			
			//additional cost for extra installaition of Ngas boiler
			double additionalCostForNgasBoiler=0.0;
			double numberOfNgasBoilerforNewHeatDemand = Math
					.round(maxHeatDemandInScaleOf1
							* ( boilerHeatDemands[2])
							* Math.pow(10, 6) * 1.5);
			if(numberOfNgasBoilerforNewHeatDemand>currentNumberOfNgasBoiler)
				additionalCostForNgasBoiler=0.933*(numberOfNgasBoilerforNewHeatDemand-currentNumberOfNgasBoiler);
			
			
			double numberOfHeatPump = Math.round(maxHeatDemandInScaleOf1 * HPheatDemand
					* Math.pow(10, 6) / COP);
			double geoBoreHoleInvestmentCost = (numberOfHeatPump * 3.2 * interest)
					/ (1 - Math.pow((1 + interest), -geoBoreHoleLifeTime));

			// extract
			col = (Collection<String>) energyplanmMap
					.get("Annual Investment costs");
			it = col.iterator();
			String invest = it.next().toString();
			String investmentCostStr = invest.substring(0,
					invest.lastIndexOf("1000"));
			double investmentCost = Double.parseDouble(investmentCostStr);
			double realInvestmentCost = investmentCost
					- reductionInvestmentCost + geoBoreHoleInvestmentCost + additionalCostForOilBoiler +additionalCostForNgasBoiler+additionalCostForBiomassBoiler;

			double actualAnnualCost = totalVariableCost + fixedOperationalCost
					+ realInvestmentCost + additionalCost;

			solution.setObjective(1, actualAnnualCost);

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
		/*
		 * Iterator it; Collection<String> col;
		 * 
		 * col = (Collection<String>) energyplanmMap.get("Maximumimport"); it =
		 * col.iterator(); int maximumImport =
		 * Integer.parseInt(it.next().toString()); col = (Collection<String>)
		 * energyplanmMap.get("Minimumstab.-load"); it = col.iterator(); int
		 * mimimumGridStabPercentage = Integer.parseInt(it.next().toString());
		 * 
		 * //constraints about heat3-balance: balance<=0 col =
		 * (Collection<String>) energyplanmMap.get("Annualheat3-balance"); it =
		 * col.iterator(); double annualHeat3Balance =
		 * Double.parseDouble(it.next().toString());
		 * 
		 * 
		 * double constraints[] = new double[numberOfConstraints_];
		 * constraints[0] = 160 - maximumImport; constraints[1] =
		 * mimimumGridStabPercentage - 100; constraints[2] = 0 -
		 * annualHeat3Balance;
		 * 
		 * double totalViolation = 0.0; int numberOfViolation = 0; for (int i =
		 * 0; i < numberOfConstraints_; i++) { if (constraints[i] < 0.0) {
		 * totalViolation += constraints[0]; numberOfViolation++; } }
		 */
		/*
		 * if (constraints[0] < 0.0) {
		 * solution.setOverallConstraintViolation(constrints);
		 * solution.setNumberOfViolatedConstraint(1);
		 */

		/*
		 * solution.setOverallConstraintViolation(totalViolation);
		 * solution.setNumberOfViolatedConstraint(numberOfViolation);
		 */

	}

	void writeModificationFile(Solution solution) throws JMException {

		// PV
		double pv = solution.getDecisionVariables()[0].getValue();
		double percents[] = new double[4];
		// 0-> HP, 1->oil, 2->Ngas, 3->Boimass
		for (int i = 0; i < 4; i++)
			percents[i] = solution.getDecisionVariables()[i + 1].getValue();

		double total = 0;
		for (int i = 0; i < 4; i++)
			total = total + percents[i];

		double heatDemands[] = new double[4];
		for (int i = 0; i < 4; i++)
			heatDemands[i] = percents[i] / total * totalHeatdemand;

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

			str = "input_HH_HP_heat=";
			bw.write(str);
			bw.newLine();
			str = "" + heatDemands[0];
			bw.write(str);
			bw.newLine();

			double oilBoilerFuelConsumption = heatDemands[1] / 0.8; // 0.8 is
																	// the
																	// efficiency

			str = "input_fuel_Households[2]=";
			bw.write(str);
			bw.newLine();
			str = "" + oilBoilerFuelConsumption;
			bw.write(str);
			bw.newLine();

			double nGasBoilerFuelConsumption = heatDemands[2] / 0.9; // 0.9 is
																		// the
																		// efficiency

			str = "input_fuel_Households[3]=";
			bw.write(str);
			bw.newLine();
			str = "" + nGasBoilerFuelConsumption;
			bw.write(str);
			bw.newLine();

			double biomassBoilerFuelConsumption = heatDemands[3] / 0.7; // 0.7
																		// is
																		// the
																	// efficiency
			str = "input_fuel_Households[4]=";
			bw.write(str);
			bw.newLine();
			str = "" + biomassBoilerFuelConsumption;
			bw.write(str);
			bw.newLine();

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
