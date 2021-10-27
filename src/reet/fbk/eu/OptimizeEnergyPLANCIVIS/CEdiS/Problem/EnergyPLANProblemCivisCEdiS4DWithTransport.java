package reet.fbk.eu.OptimizeEnergyPLANCIVIS.CEdiS.Problem;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.MultiMap;

import reet.fbk.eu.OptimizeEnergyPLANCIVIS.ParseFile.*;

/*
 * This is a problem file that is dealing with Transport (mainly introduction of electric cars)
 */
public class EnergyPLANProblemCivisCEdiS4DWithTransport extends Problem {

	MultiMap energyplanmMap;

	public static final double indvBoilerCostInKEuro = 0.625;
	public static final double PVInvestmentCostInKEuro = 2.6;
	public static final double hydroInvestmentCostInKEuro = 1.9;
	public static final double individualBoilerInvestmentCostInKEuro = 0.588;
	public static final double interest = 0.04;

	public static final double currentPVCapacity = 5566;
	public static final double currentHydroCapacity = 4592;
	public static final double currentIndvBiomassBoilerCapacity = 7903;
	public static final double currentIndvOilBoilerCapacity = 3688;
	public static final double currentIndvNgasBoilerCapacity = 13175;

	public static final double totalHeatDemand = 51.30;

	public static final double boilerLifeTime = 15;
	public static final double PVLifeTime = 30;
	public static final double HydroLifeTime = 50;
	public static final double geoBoreHoleLifeTime = 100;

	public static final double COP = 3.2;

	public static final double maxHeatDemandInDistribution = 1.0;
	public static final double sumOfAllHeatDistributions = 3106.96;

	public static final double geoBoreholeCostInKWe = 3.2;

	public static final double oilBoilerEfficiency = 0.80;
	public static final double ngasBoilerEfficiency = 0.90;
	public static final double biomassBoilerEfficiency = 0.75;

	public static final double addtionalCostPerGWhinKEuro = 106.27;
	
	//Transport related data
	public static final int currentNumberOfPertrolCars  = 2537; 
	public static final int currentNumberOfDieselCars  = 1923;
	public static final int averageKMPerYearForPetrolCar = 7250;
	public static final int averageKMPerYearForDieselCar = 13400;
	// lower calorific value (LCV): KWh/l (ref: http://www.withouthotair.com/c3/page_31.shtml) check with Diego
	public static final double LCVPetrol = 8.86;
	public static final double LCVDiesel = 10.12;
	public static final double KWhPerKMElecCar = 0.168;
	public static final double petrolCarRunsKMperL = 15.5;
	public static final double DieselCarRunsKMperL = 18.2;
	public static final int totalKMRunByCars = 44161450;
	public static final double costOfElectricCarInKeuro = 18.690;
	public static final int electricCarLifeTime = 15;
	public static final double electricCarOperationalAndMaintanenceCost = 0.055; //5.5 percent of Investment cost (costOfElectricCarInKeuro)
	
	/**
	 * Creates a new instance of problem ZDT1.
	 * 
	 * @param numberOfVariables
	 *            Number of variables.
	 * @param solutionType
	 *            The solution type must "Real", "BinaryReal, and "ArrayReal".
	 */
	public EnergyPLANProblemCivisCEdiS4DWithTransport(String solutionType) {
		numberOfVariables_ = 6;
		numberOfObjectives_ = 4;
		numberOfConstraints_ = 1;
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
		// index - 5 -> electric car percentage  

		// PV upper and lower limit
		lowerLimit_[0] = 5565.0;
		upperLimit_[0] = 61000.0;

		// other are the percentage from limit [0,1]
		for (var = 1; var < numberOfVariables_; var++) {
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
		// index - 5 -> electric car percentage  
		
		double percentages[] = new double[4];
		for (int i = 0; i < 4; i++) {
			percentages[i] = solution.getDecisionVariables()[i + 1].getValue();
		}

		Arrays.sort(percentages);

		for (int i = 1; i < numberOfVariables_-1; i++) {
			solution.getDecisionVariables()[i].setValue(percentages[i - 1]);
		}
		// oil-boiler percentage
		double oilBoilerHeatPercentage = percentages[0];
		// Ngas-boiler heat percentage
		double ngasBoilerHeatPercentage = percentages[1] - percentages[0];
		// biomass-boiler heat percentage
		double biomassBoilerHeatPercentage = percentages[2] - percentages[1];
		// ngas chp heat percentage
		double ngasCHPHeatPercentage = percentages[3] - percentages[2];

		// heat pump heat percentage
		double hpHeatPercentage = 1.0 - percentages[3];
		
		//electric car percentage
		double electricCarPercentage = solution.getDecisionVariables()[5].getValue();
		int reducedNumberOfPetrolCars = (int) Math.round(currentNumberOfPertrolCars - currentNumberOfPertrolCars * electricCarPercentage);
		int reducedNumberOfDieselCars = (int) Math.round(currentNumberOfDieselCars - currentNumberOfDieselCars * electricCarPercentage);
		double reducedPetrolDemandInGWh = reducedNumberOfPetrolCars * averageKMPerYearForPetrolCar * LCVPetrol / (petrolCarRunsKMperL*1000000);
		double reducedDieselDemandInGWh = reducedNumberOfDieselCars * averageKMPerYearForDieselCar * LCVDiesel / (DieselCarRunsKMperL*1000000);
		
		int elecCarRunKM = totalKMRunByCars - (reducedNumberOfPetrolCars * averageKMPerYearForPetrolCar) - (reducedNumberOfDieselCars * averageKMPerYearForDieselCar);
		double elecCarElectricityDemandInGWh =  elecCarRunKM * KWhPerKMElecCar / 1000000;
		double totalNumberOfElectricCars =  (int) Math.round(currentNumberOfPertrolCars + currentNumberOfDieselCars - reducedNumberOfPetrolCars - reducedNumberOfDieselCars); 
		
		
		writeModificationFile(pv, oilBoilerHeatPercentage,
				ngasBoilerHeatPercentage, biomassBoilerHeatPercentage,
				ngasCHPHeatPercentage, hpHeatPercentage, reducedPetrolDemandInGWh, reducedDieselDemandInGWh, elecCarElectricityDemandInGWh, "NC");
		String energyPLANrunCommand = ".\\EnergyPLAN_SEP_2013\\EnergyPLAN.exe -i "
				+ "\".\\src\\reet\\fbk\\eu\\OptimizeEnergyPLANCIVIS\\CEdiS\\data\\CEdiS_current.txt\" "
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
			double totalAdditionalCost = Math.round((hydroPowerProduction
					+ PVproduction + Import - Export + chpElecProduction)
					* addtionalCostPerGWhinKEuro);

			// new capacity of individual boilers
			/*
			 * double newHeatdemandForBoilers = (totalHeatDemand *
			 * oilBoilerHeatPercentage + totalHeatDemand *
			 * ngasBoilerHeatPercentage + totalHeatDemand *
			 * biomassBoilerHeatPercentage); double
			 * capacityOfBoilerforNewHeatDemand =
			 * Math.round(maxHeatDemandInDistribution *
			 * newHeatdemandForBoilers*Math.pow(10,
			 * 6)*1.5/sumOfAllHeatDistributions);
			 */

			double capacityOfHeatPump = Math.round((maxHeatDemandInDistribution
					* hpHeatPercentage * totalHeatDemand * Math.pow(10, 6))
					/ (COP * sumOfAllHeatDistributions));
			double geoBoreHoleInvestmentCost = (capacityOfHeatPump
					* geoBoreholeCostInKWe * interest)
					/ (1 - Math.pow((1 + interest), -geoBoreHoleLifeTime));

			// see annual investment cost formula in EnergyPLAN manual

			double newCapacityBiomassBoiler = Math
					.round((totalHeatDemand * biomassBoilerHeatPercentage)
							* Math.pow(10, 6) * 1.5 / sumOfAllHeatDistributions);
			double investmentCostReductionBiomassBoiler = 0.0;
			if (newCapacityBiomassBoiler > currentIndvBiomassBoilerCapacity) {
				investmentCostReductionBiomassBoiler = (currentIndvBiomassBoilerCapacity
						* individualBoilerInvestmentCostInKEuro * interest)
						/ (1 - Math.pow((1 + interest), -boilerLifeTime));
			} else {
				investmentCostReductionBiomassBoiler = (newCapacityBiomassBoiler
						* individualBoilerInvestmentCostInKEuro * interest)
						/ (1 - Math.pow((1 + interest), -boilerLifeTime));
			}

			double newCapacityOilBoiler = Math
					.round((totalHeatDemand * oilBoilerHeatPercentage)
							* Math.pow(10, 6) * 1.5 / sumOfAllHeatDistributions);
			double investmentCostReductionOilBoiler = 0.0;
			if (newCapacityOilBoiler > currentIndvOilBoilerCapacity) {
				investmentCostReductionOilBoiler = (currentIndvOilBoilerCapacity
						* individualBoilerInvestmentCostInKEuro * interest)
						/ (1 - Math.pow((1 + interest), -boilerLifeTime));
			} else {
				investmentCostReductionOilBoiler = (newCapacityOilBoiler 
						* individualBoilerInvestmentCostInKEuro * interest)
						/ (1 - Math.pow((1 + interest), -boilerLifeTime));
			}

			double newCapacityNgasBoiler = Math
					.round((totalHeatDemand * ngasBoilerHeatPercentage)
							* Math.pow(10, 6) * 1.5 / sumOfAllHeatDistributions);
			double investmentCostReductionNgasBoiler = 0.0;
			if (newCapacityNgasBoiler > currentIndvNgasBoilerCapacity) {
				investmentCostReductionNgasBoiler = (currentIndvNgasBoilerCapacity
						* individualBoilerInvestmentCostInKEuro * interest)
						/ (1 - Math.pow((1 + interest), -boilerLifeTime));
			} else {
				investmentCostReductionNgasBoiler = (newCapacityNgasBoiler
						* individualBoilerInvestmentCostInKEuro * interest)
						/ (1 - Math.pow((1 + interest), -boilerLifeTime));
			}

			double reductionInvestmentCost = (currentPVCapacity
					* PVInvestmentCostInKEuro * interest)
					/ (1 - Math.pow((1 + interest), -PVLifeTime))
					+ (currentHydroCapacity * hydroInvestmentCostInKEuro * interest)
					/ (1 - Math.pow((1 + interest), -HydroLifeTime))
					+ investmentCostReductionBiomassBoiler
					+ investmentCostReductionOilBoiler
					+ investmentCostReductionNgasBoiler;

			// extract
			col = (Collection<String>) energyplanmMap
					.get("Annual Investment costs");
			it = col.iterator();
			String invest = it.next().toString();
			String investmentCostStr = invest.substring(0,
					invest.lastIndexOf("1000"));
			double investmentCost = Double.parseDouble(investmentCostStr);
			double realInvestmentCost = investmentCost
					- reductionInvestmentCost + geoBoreHoleInvestmentCost;

			//Electric car related cost
			double totalInvestmentCostOfElectricCars = (totalNumberOfElectricCars * costOfElectricCarInKeuro * interest)/ (1 - Math.pow((1 + interest), -electricCarLifeTime));
			double totalFixOperationalAndInvestmentCostOfElectricCars = totalNumberOfElectricCars * costOfElectricCarInKeuro * electricCarOperationalAndMaintanenceCost ;
			
			double actualAnnualCost = totalVariableCost + fixedOperationalCost
					+ realInvestmentCost + totalAdditionalCost + totalInvestmentCostOfElectricCars + totalFixOperationalAndInvestmentCostOfElectricCars;

			solution.setObjective(1, actualAnnualCost);

			
			//3rd objective
			//Trasportation
			col = (Collection<String>) energyplanmMap
					.get("Annualflexibleeldemand");
			it = col.iterator();
			double transportElecDemand = Double.parseDouble(it.next().toString());
			
			
			col = (Collection<String>) energyplanmMap.get("Annualelec.demand");
			it = col.iterator();
			double annualElecDemand = Double.parseDouble(it.next().toString());

			//Individual house HP electric demand
			col = (Collection<String>) energyplanmMap.get("AnnualHH-elec.HP");
			it = col.iterator();
			double annualHPdemand = Double.parseDouble(it.next().toString());
			
			solution.setObjective(2, (Import + Export) / (annualElecDemand+transportElecDemand +annualHPdemand) );
			
			//extract ngas consuption
			col = (Collection<String>) energyplanmMap
					.get("Ngas Consumption");
			it = col.iterator();
			double nGasConsumption = Double.parseDouble(it.next().toString());
			
			//extract oil consumption
			col = (Collection<String>) energyplanmMap
					.get("Oil Consumption");
			it = col.iterator();
			double oilConsumption = Double.parseDouble(it.next().toString());
			
			
			//extract biomass consupmtion
			col = (Collection<String>) energyplanmMap.get("Biomass Consumption");
			it = col.iterator();
			double BiomassConsumption = Double.parseDouble(it.next().toString());
			
			double PVPEF = 1.0;
			double HYPEF = 1.0;
			double ngasmCHPPEF = 1/0.50;
			double PEFImport = 2.17; 
			
			double totalPEForElecrcity = PVproduction * PVPEF + hydroPowerProduction * HYPEF + chpElecProduction * ngasmCHPPEF;
			double totalLocalElecProduction = PVproduction + hydroPowerProduction + chpElecProduction; 
			double PEFLocalElec = totalPEForElecrcity/ totalLocalElecProduction;
			
			double totalPEConsumtion = (totalLocalElecProduction - Export) * PEFLocalElec + Import * PEFImport + BiomassConsumption + oilConsumption + nGasConsumption
					+ (totalHeatDemand* hpHeatPercentage - totalHeatDemand* hpHeatPercentage/COP);
			
			double ESD = (Import * PEFImport + oilConsumption + nGasConsumption)/totalPEConsumtion;
						
			solution.setObjective(3, ESD);
			
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
			double ngasBoilerHeatPercentage,
			double biomassBoilerHeatPercentage, double ngasCHPHeatPercentage,
			double hpHeatPercentage, double reducedPetrolDemand, double reducedDieselDemand, double elecCarDemand, String elecChangeProfile) throws JMException {

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
					* totalHeatDemand / oilBoilerEfficiency;
			str = "" + oilBoilerFuelDemand;
			bw.write(str);
			bw.newLine();

			// Ngas boiler fuel demand
			str = "input_fuel_Households[3]=";
			bw.write(str);
			bw.newLine();
			double ngasBoilerFuelDemand = ngasBoilerHeatPercentage
					* totalHeatDemand / ngasBoilerEfficiency;
			str = "" + ngasBoilerFuelDemand;
			bw.write(str);
			bw.newLine();

			// biomass boiler fuel demand
			str = "input_fuel_Households[4]=";
			bw.write(str);
			bw.newLine();
			double biomassBoilerFuelDemand = biomassBoilerHeatPercentage
					* totalHeatDemand / biomassBoilerEfficiency;
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
			
			//Electric car electricity demand
			str = "input_transport_TWh=";
			bw.write(str);
			bw.newLine();
			str = "" + elecCarDemand;
			bw.write(str);
			bw.newLine();
			
			//to handle scintific number
			DecimalFormat df = new DecimalFormat("#");
	        df.setMaximumFractionDigits(2);
			//Reduced Diesel demand 
			str = "input_fuel_Transport[2]=";
			bw.write(str);
			bw.newLine();
			str = "" + df.format(reducedDieselDemand);
			bw.write(str);
			bw.newLine();
			
			//reduced petrol demand
			str = "input_fuel_Transport[5]=";
			bw.write(str);
			bw.newLine();
			str = "" + df.format(reducedPetrolDemand);
			bw.write(str);
			bw.newLine();
			
			if(elecChangeProfile.equals("DC")){
				// day charge profile
				str = "Filnavn_transport=";
				bw.write(str);
				bw.newLine();
				str = "CIVIS_Transport_DC.txt";
				bw.write(str);
				bw.newLine();
			}
			else if(elecChangeProfile.equals("NC")){
				// day charge profile
				str = "Filnavn_transport=";
				bw.write(str);
				bw.newLine();
				str = "CIVIS_Transport_NC.txt";
				bw.write(str);
				bw.newLine();
			}else{
				throw new JMException("chrging profile not gien");
			}

			bw.close();
			// file.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
