package reet.fbk.eu.OptimizeEnergyPLANCIVIS.CEdiS.Problem;

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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import reet.fbk.eu.OptimizeEnergyPLANCIVIS.ParseFile.*;

public class EnergyPLANProblemCivisCEdiS extends Problem {

	MultiMap energyplanmMap;

	public static final double indvBoilerCostInKEuro = 0.625;
	public static final double currentPVCapacity = 5566;
	public static final double currentHydroCapacity = 4592;
	public static final double currentCapacityOfIndvBoiler = 24768;
	public static final double totalHeatDemand = 51.30;

	public static final double boilerLifeTime = 20;
	public static final double PVLifeTime = 25;
	public static final double HydroLifeTime = 20;
	public static final double interest = 0.04;

	public static final double COP = 3.2;

	public static final double maxHeatDemandInDistribution = 1.0;
	public static final double sumOfAllHeatDistributions = 3106.96;

	public static final double PVInvestmentCost = 3.987;
	public static final double hydroInvestmentCost = 3.51;

	/**
	 * Creates a new instance of problem ZDT1.
	 * 
	 * @param numberOfVariables
	 *            Number of variables.
	 * @param solutionType
	 *            The solution type must "Real", "BinaryReal, and "ArrayReal".
	 */
	public EnergyPLANProblemCivisCEdiS(String solutionType) {
		numberOfVariables_ = 5;
		numberOfObjectives_ = 3;
		numberOfConstraints_ =1;
		/*
		 * at this moment, there are two objectives, 0 -> PV 1 -> Heat pump
		 */
		// numberOfConstraints_ = 3;

		problemName_ = "OptimizeEnergyPLANCivisCeDis";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		// Establishes upper and lower limits for the variables
		int var;

		// decision variables
		// index - 0 -> PV Capacity
		// index - 1 -> oil boiler heat percentage
		// index - 2 -> Ngas boiler heat percentage
		// index - 3 -> Biomass boiler heat percentage
		// index - 4 -> Ngas micro chp heat percentage
		// last percentage will go to individual HP percentage

		// PV upper and lower limit
		lowerLimit_[0] = 5565.0;
		upperLimit_[0] = 15000.0;

		// other are the percentage from limit [0,1]
		for (var = 1; var < numberOfVariables_; var++) {
			// capacities of wind, off-shore wind, PV and condencing power unit
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
		
		// PV
		double pv = solution.getDecisionVariables()[0].getValue();
		
		// index - 1 -> oil boiler heat percentage
				// index - 2 -> Ngas boiler heat percentage
				// index - 3 -> Biomass boiler heat percentage
				// index - 4 -> Ngas micro chp heat percentage
				// last percentage will go to individual HP percentage 
		// Oil-boiler heat percentage
		
		double percentages[] = new double[4];
		for(int i=0;i<4;i++){
			percentages[i] = solution.getDecisionVariables()[i+1].getValue();
		}
		
		Arrays.sort(percentages);
		
		double oilBoilerHeatPercentage = percentages[0];
		// Ngas-boiler heat percentage
		double ngasBoilerHeatPercentage = percentages[1] - percentages [0];
		// biomass-boiler heat percentage
		double biomassBoilerHeatPercentage = percentages[2] - percentages [1];
		// ngas chp heat percentage
		double ngasCHPHeatPercentage = percentages[3] - percentages [2];

		// heta pump  heat percentage
		double hpHeatPercentage = 1 - percentages[3];

		writeModificationFile(pv, oilBoilerHeatPercentage,ngasBoilerHeatPercentage, biomassBoilerHeatPercentage , ngasCHPHeatPercentage, hpHeatPercentage);
		String energyPLANrunCommand = ".\\EnergyPLAN_SEP_2013\\EnergyPLAN.exe -i "
				+ "\".\\src\\reet\\fbk\\eu\\OptimizeEnergyPLANCIVIS\\CEdiS\\data\\CEdiS_current.txt\" " 
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

						
			// objective # 1
			col = (Collection<String>) energyplanmMap
					.get("CO2-emission (corrected)");
			it = col.iterator();
			solution.setObjective(0, Double.parseDouble(it.next().toString()));
			// objective # 2
			col = (Collection<String>) energyplanmMap.get("Total variable costs");
			it = col.iterator();
			String totalVariableCostStr = it.next().toString();
			totalVariableCostStr = totalVariableCostStr.substring(0, totalVariableCostStr.lastIndexOf("1000") );
			double totalVariableCost = Double.parseDouble(totalVariableCostStr);
			
			col = (Collection<String>) energyplanmMap.get("Fixed operation costs");
			it = col.iterator();
			String fixedOperationalCostStr = it.next().toString();
			fixedOperationalCostStr = fixedOperationalCostStr.substring(0, fixedOperationalCostStr.lastIndexOf("1000") );
			double fixedOperationalCost = Double.parseDouble(fixedOperationalCostStr);
			
						
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
			
			col = (Collection<String>) energyplanmMap.get("AnnualHH-elec.CHP");
			it = col.iterator();
			double chpElecProduction = Double.parseDouble(it.next().toString());
			
			// calculate additional cost
			// (hydroProduction+PVproduction+Import-Export)*average additional
			// cost (85.74)
			double additionalCost = Math.round((hydroPowerProduction
					+ PVproduction + Import - Export + chpElecProduction ) * 85.74);
			
			/*
			 * now, calculate how many boiler need to diassamble 
			 */
			
					
			// new capacity of individual boilers
		/*	double newHeatdemandForBoilers = (totalHeatDemand * oilBoilerHeatPercentage + totalHeatDemand * ngasBoilerHeatPercentage + totalHeatDemand * biomassBoilerHeatPercentage);
			double capacityOfBoilerforNewHeatDemand = Math.round(maxHeatDemandInDistribution * newHeatdemandForBoilers*Math.pow(10, 6)*1.5/sumOfAllHeatDistributions);*/
			
			int geoBoreHoleLifeTime=100;
			double capacityOfHeatPump =  Math.round( (maxHeatDemandInDistribution * hpHeatPercentage * totalHeatDemand *Math.pow(10, 6)) / (COP*sumOfAllHeatDistributions) );
			double geoBoreHoleInvestmentCost = (capacityOfHeatPump * 3.2 * interest)/(1-Math.pow((1+interest),-geoBoreHoleLifeTime));
			
			//see anual investment cost formual in EnergyPLAN manual 
			
			double reductionInvestmentCost = (currentPVCapacity * PVInvestmentCost * interest)/(1-Math.pow((1+interest),-PVLifeTime)) +
					(currentHydroCapacity*hydroInvestmentCost*interest)/(1-Math.pow((1+interest), -HydroLifeTime)) ;
			
			
			
			//extract 
			col = (Collection<String>) energyplanmMap
					.get("Annual Investment costs");
			it = col.iterator();
			String invest= it.next().toString();
			String investmentCostStr = invest.substring(0, invest.lastIndexOf("1000") );
			double investmentCost = Double.parseDouble(investmentCostStr);
			double realInvestmentCost = investmentCost-reductionInvestmentCost +geoBoreHoleInvestmentCost;
			
			double actualAnnualCost = totalVariableCost + fixedOperationalCost + realInvestmentCost + additionalCost;
			
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
		Iterator it;
		Collection<String> col;

		// constraints about heat3-balance: balance<=0
		col = (Collection<String>) energyplanmMap.get("Biomass Consumption");
		it = col.iterator();
		double annualBiomassConsumption = Double.parseDouble(it.next()
				.toString());

		double constraints[] = new double[numberOfConstraints_];
		constraints[0] = 33.83 - annualBiomassConsumption;

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

	void writeModificationFile(double pv, double oilBoilerHeatPercentage,
			double ngasBoilerHeatPercentage, double biomassBoilerHeatPercentage, 
			double ngasCHPHeatPercentage,double hpHeatPercentage) throws JMException {



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

			str = "input_RES1_capacity=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(pv);
			bw.write(str);
			bw.newLine();

			// oil boiler fuel demand
			str = "input_fuel_Households[2]=";
			bw.write(str);
			bw.newLine();
			double oilBoilerFuelDemand = oilBoilerHeatPercentage
					* totalHeatDemand /0.8;
			str = "" + oilBoilerFuelDemand;
			bw.write(str);
			bw.newLine();

			// Ngas boiler fuel demand
			str = "input_fuel_Households[3]=";
			bw.write(str);
			bw.newLine();
			double ngasBoilerFuelDemand = ngasBoilerHeatPercentage
					* totalHeatDemand /0.9;
			str = "" + ngasBoilerFuelDemand;
			bw.write(str);
			bw.newLine();

			// biomass boiler fuel demand
			str = "input_fuel_Households[4]=";
			bw.write(str);
			bw.newLine();
			double biomassBoilerFuelDemand = biomassBoilerHeatPercentage
					* totalHeatDemand /0.7;
			str = "" + biomassBoilerFuelDemand;
			bw.write(str);
			bw.newLine();

			// ngas micro chp heat demand
			str = "input_HH_NgasCHP_heat=";
			bw.write(str);
			bw.newLine();
			str = "" + ngasCHPHeatPercentage * totalHeatDemand;
			bw.write(str);
			bw.newLine();

			// heat pump heat demand
			str = "input_HH_HP_heat=";
			bw.write(str);
			bw.newLine();
			str = "" + hpHeatPercentage * totalHeatDemand;
			bw.write(str);
			bw.newLine();

			bw.close();
			// file.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
