package reet.fbk.eu.OptimizeEnergyPLANVdN.problem;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.math3.random.CorrelatedRandomVectorGenerator;

import reet.fbk.eu.OptimizeEnergyPLANCIVIS.ParseFile.*;
import reet.fbk.eu.OptimizeEnergyPLANVdN.parseFile.EnergyPLANFileParseForVdN;

/*
 * This is a problem file that is dealing with VdN (mainly introduction of electric cars)
 */
public class EnergyPLANProblemVdN2DWithElecVehicleModifiedCO2 extends Problem {

	
	
	MultiMap energyplanmMap;
	String simulatedYear, simulatedScenario;
	

	/*
	 * VdN data
	 */
	
	//common data for all scenarios
	//CO2 content related data
	public static final double co2Coal=95.0; //unit: kg/GJ
	public static final double co2Oil=74.0;
	public static final double co2NGas = 56.7;
	
	public static final int averageKMPerYearPerCar = 12900;
	//public static final double maxH2DemandInDistribution = 1.0;
	//public static final double sumH2DemandInDistribution = 2928.0; 
	
	//Demands: 2020
	
	
	//transport
	public static final int totalKMRunByCars = 250000000;
	
	
	//other variable
	public double totalHeatDemand;
	
	//cars efficiencies
	public double efficiencyConCar; // KWh/km
	public double efficiencyEVCar; // KWh/km
	public double efficiencyFCEVCar; // KWh/km

	public double efficiencyBiomassCHP;
	
	// H2
	public double efficiencyElectrolyzerTrans;
	
	public double oilBoilerEfficiency;
	public double nGasBoilerEfficiency;
	public double biomassBoilerEfficiency;
	public double COP;
	
	public double coalShare;
	public double oilShare;
	public double nGasShare;
	
	// additional cost
	public double addtionalCostPerGWhinKEuro;
	
	
	
	
	/*//2020 scenario: 2DS (for 4DS, we need to change additional prices)

	// Heat
	public static final double totalHeatDemand = 253.70; // in GWh
	

	public static final double efficiencyConCar = 0.607; // KWh/km
	public static final double efficiencyEVCar = 0.169; // KWh/km
	public static final double efficiencyFCEVCar = 0.334; // KWh/km
	
	public static final double efficiencyBiomassCHP = 0.66;

	// H2
	public static final double efficiencyElectrolyzerTrans = 0.74;

	public static final double oilBoilerEfficiency = 0.85;
	public static final double nGasBoilerEfficiency = 0.95;
	public static final double biomassBoilerEfficiency = 0.80;
	public static final double COP = 4.54;

	//Fuel distribution related data
	//2020 scenario
	public static final double coalShare=14.0;
	public static final double oilShare=5.6;
	public static final double nGasShare=45.0;
	
	// additional cost (2DS)
	public static final double addtionalCostPerGWhinKEuro = 136.36;*/
	
	
	/*//2030 scenario: 2DS (for 4DS, we need to change additional prices)
	
	// Heat
	public static final double totalHeatDemand = 249.84; // in GWh
	
	
	//cars
	public static final double efficiencyConCar = 0.549; // KWh/km
	public static final double efficiencyEVCar = 0.145; // KWh/km
	public static final double efficiencyFCEVCar = 0.267; // KWh/km

	public static final double efficiencyBiomassCHP = 0.66;
	
	// H2
	public static final double efficiencyElectrolyzerTrans = 0.75;
	
	public static final double oilBoilerEfficiency = 0.87;
	public static final double nGasBoilerEfficiency = 0.97;
	public static final double biomassBoilerEfficiency = 0.82;
	public static final double COP = 5.07;
	
	public static final double coalShare=10.4;
	public static final double oilShare=4.1;
	public static final double nGasShare=33.4;
	
	// additional cost
	public static final double addtionalCostPerGWhinKEuro = 144.33;*/
	
	
	/*//2050 scenario: 2DS (for 4DS, we need to change additional prices)
	// Heat
	public static final double totalHeatDemand = 256.11; // in GWh
		
		
	//cars
	public static final double efficiencyConCar = 0.497; // KWh/km
	public static final double efficiencyEVCar = 0.117; // KWh/km
	public static final double efficiencyFCEVCar = 0.200; // KWh/km

	public static final double efficiencyBiomassCHP = 0.66;
	
	// H2
	public static final double efficiencyElectrolyzerTrans = 0.78;
	
	public static final double oilBoilerEfficiency = 0.89;
	public static final double nGasBoilerEfficiency = 0.99;
	public static final double biomassBoilerEfficiency = 0.84;
	public static final double COP = 5.46;
	
	public static final double coalShare=3.3;
	public static final double oilShare=1.3;
	public static final double nGasShare=10.5;
	
	// additional cost
	public static final double addtionalCostPerGWhinKEuro = 158.37;

	*/
	
	/**
	 * Creates a new instance of problem ZDT1.
	 * 
	 * @param numberOfVariables
	 *            Number of variables.
	 * @param solutionType
	 *            The solution type must "Real", "BinaryReal, and "ArrayReal".
	 */
	public EnergyPLANProblemVdN2DWithElecVehicleModifiedCO2(String solutionType, String simulatedYear, String simulatedScenario) {
		
		this.simulatedYear=simulatedYear;
		this.simulatedScenario=simulatedScenario;
				
		numberOfVariables_ = 11;
		numberOfObjectives_ = 2;
		numberOfConstraints_ = 1;

		problemName_ = "OptimizeEnergyPLANVdN";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		// Establishes upper and lower limits for the variables
		int var;

		// decision variables
		// index - 0 -> PV Capacity
		// index - 1 -> oil boiler heat percentage
		// index - 2 -> nGas boiler heat percentage
		// index - 3 -> Biomass boiler heat percentage
		// index - 4 -> Biomass micro chp heat percentage
		// last percentage will go to individual HP percentage
		// index - 5 -> electric car percentage
		

		// index -6 -> solar thermal percentage in oil boiler
		// index -7 -> solar thermal percentage in nGas boiler
		// index -8 -> solar thermal percentage in biomass boiler
		// index -9 -> solar thermal percentage in biomass CHP
		// index -10 -> solar thermal percentage in HP

		// PV upper and lower limit
		lowerLimit_[0] = 936.0;
		upperLimit_[0] = 40000.0;

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
	
	
		if(this.simulatedYear.equals("2020")){
			totalHeatDemand = 253.70; // in GWh
			

			efficiencyConCar = 0.607; // KWh/km
			efficiencyEVCar = 0.169/0.85; // KWh/km; divided by 0.85 (which is "round-trip battery efficiency)
			efficiencyFCEVCar = 0.334; // KWh/km
			
			efficiencyBiomassCHP = 0.66;

			// H2
			efficiencyElectrolyzerTrans = 0.74;

			oilBoilerEfficiency = 0.85;
			nGasBoilerEfficiency = 0.95;
			biomassBoilerEfficiency = 0.80;
			COP = 4.54;

			//Fuel distribution related data
			//2020 scenario
			coalShare=14.0;
			oilShare=5.6;
			nGasShare=45.0;
			
			// additional cost (2DS)
			addtionalCostPerGWhinKEuro = 136.36;
			
		}else if(this.simulatedYear.equals("2030")){
			// Heat
			totalHeatDemand = 249.84; // in GWh
			
			
			//cars
			efficiencyConCar = 0.549; // KWh/km
			efficiencyEVCar = 0.145/0.85; // KWh/km; divided by 0.85 (which is "round-trip battery efficiency)
			efficiencyFCEVCar = 0.267; // KWh/km

			efficiencyBiomassCHP = 0.66;
			
			// H2
			efficiencyElectrolyzerTrans = 0.75;
			
			oilBoilerEfficiency = 0.87;
			nGasBoilerEfficiency = 0.97;
			biomassBoilerEfficiency = 0.82;
			COP = 5.07;
			
			coalShare=10.4;
			oilShare=4.1;
			nGasShare=33.4;
			
			// additional cost
			addtionalCostPerGWhinKEuro = 144.33;
		}else if(this.simulatedYear.equals("2050")){
			// Heat
			totalHeatDemand = 256.11; // in GWh
				
				
			//cars
			efficiencyConCar = 0.497; // KWh/km
			efficiencyEVCar = 0.117/0.85; // KWh/km; divided by 0.85 (which is "round-trip battery efficiency)
			efficiencyFCEVCar = 0.200; // KWh/km

			efficiencyBiomassCHP = 0.66;
			
			// H2
			efficiencyElectrolyzerTrans = 0.78;
			
			oilBoilerEfficiency = 0.89;
			nGasBoilerEfficiency = 0.99;
			biomassBoilerEfficiency = 0.84;
			COP = 5.46;
			
			coalShare=3.3;
			oilShare=1.3;
			nGasShare=10.5;
			
			// additional cost
			addtionalCostPerGWhinKEuro = 158.37;
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

		// decision variables
		// index - 0 -> PV Capacity
		// index - 1 -> oil boiler heat percentage
		// index - 2 -> nGas boiler heat percentage
		// index - 3 -> Biomass boiler heat percentage
		// index - 4 -> Biomass micro chp heat percentage
		// last percentage will go to individual HP percentage
		// index - 5 -> electric car percentage
		

		// index -6 -> solar thermal percentage in oil boiler
		// index -7 -> solar thermal percentage in nGas boiler
		// index -8 -> solar thermal percentage in biomass boiler
		// index -9 -> solar thermal percentage in biomass CHP
		// index -10 -> solar thermal percentage in HP

		double heatPercentages[] = new double[4];
		for (int i = 0; i < 4; i++) {
			heatPercentages[i] = solution.getDecisionVariables()[i + 1]
					.getValue();
		}

		Arrays.sort(heatPercentages);

		for (int i = 1; i < 5; i++) {
			solution.getDecisionVariables()[i].setValue(heatPercentages[i - 1]);
		}
		// oil-boiler percentage
		double oilBoilerHeatPercentage = heatPercentages[0];
		// Ngas-boiler heat percentage
		double nGasBoilerHeatPercentage = heatPercentages[1]
				- heatPercentages[0];
		// biomass-boiler heat percentage
		double biomassBoilerHeatPercentage = heatPercentages[2]
				- heatPercentages[1];
		// ngas chp heat percentage
		double biomassCHPHeatPercentage = heatPercentages[3]
				- heatPercentages[2];

		// heat pump heat percentage
		double hpHeatPercentage = 1.0 - heatPercentages[3];

		// cars' percentages
		double EVCarPercentage = solution.getDecisionVariables()[5].getValue();
		double conCarPercentage = 1- EVCarPercentage;
		
		double totalKMRunByConCar = (int) totalKMRunByCars * conCarPercentage;
		double totalKMRunByEVCar = (int) totalKMRunByCars * EVCarPercentage;
		//double totalKMRunByFCEVCar = (int) totalKMRunByCars * FCEVCarPercentage;

		double totalDieselDemandInGWhForTrns = totalKMRunByConCar
				* efficiencyConCar / Math.pow(10, 6);
		double totalElecDemandInGWhForTrns = totalKMRunByEVCar
				* efficiencyEVCar / Math.pow(10, 6);
		//double totalH2DemandInGWhForTrns = totalKMRunByFCEVCar
				//* efficiencyFCEVCar / Math.pow(10, 6);

		// solar thermal percentage
		double oilSolarPercentage = solution.getDecisionVariables()[6]
				.getValue();
		double nGasSolarPercentage = solution.getDecisionVariables()[7]
				.getValue();
		double biomassBoilerSolarPercentage = solution.getDecisionVariables()[8]
				.getValue();
		double biomassCHPSolarPercentage = solution.getDecisionVariables()[9]
				.getValue();
		double hpSolarPercentage = solution.getDecisionVariables()[10]
				.getValue();

		try{
		writeIntoInputFile(pv, oilBoilerHeatPercentage,
				nGasBoilerHeatPercentage, biomassBoilerHeatPercentage,
				biomassCHPHeatPercentage, hpHeatPercentage,
				totalDieselDemandInGWhForTrns, totalElecDemandInGWhForTrns,
				/*totalH2DemandInGWhForTrns,*/ oilSolarPercentage,
				nGasSolarPercentage, biomassBoilerSolarPercentage,
				biomassCHPSolarPercentage, hpSolarPercentage);
		}catch(IOException e){
			System.out.print("Pobrlem writting in modified Input file");
		}
		
		String energyPLANrunCommand = ".\\EnergyPLAN_12.3\\EnergyPLAN.exe -i "
				+ "\".\\modifiedInput.txt\" "
				+ "-ascii \"result.txt\" ";
		try {
			// Process process = new
			// ProcessBuilder(energyPLANrunCommand).start();
			Process process = Runtime.getRuntime().exec(energyPLANrunCommand);
			process.waitFor();
			process.destroy();
			EnergyPLANFileParseForVdN epfp = new EnergyPLANFileParseForVdN(
					".\\result.txt");
			energyplanmMap = epfp.parseFile();

			Iterator it;
			Collection<String> col;

			// objective # 1
			col = (Collection<String>) energyplanmMap
					.get("CO2-emission (total)");
			it = col.iterator();
			double localCO2emission =  Double.parseDouble(it.next().toString());
			
			//extract import
			col = (Collection<String>) energyplanmMap
					.get("AnnualImportElectr.");
			it = col.iterator();
			double elecImport = Double.parseDouble(it.next().toString());
			
			double co2InImportedEleCoal =( (elecImport*coalShare/100.0) * co2Coal *3600.0 )/ 1000000.0; //1GWh = 3600 GJ
			double co2InImportedEleOil =( (elecImport*oilShare/100.0) * co2Oil *3600.0 )/ 1000000.0; //1GWh = 3600 GJ
			double co2InImportedEleNGas =( (elecImport*nGasShare/100.0) * co2NGas *3600.0 )/ 1000000.0; //1GWh = 3600 GJ
			
			localCO2emission = localCO2emission + co2InImportedEleCoal + co2InImportedEleOil + co2InImportedEleNGas;
			solution.setObjective(0,localCO2emission);

			// objective # 2
			col = (Collection<String>) energyplanmMap.get("Variable costs");
			it = col.iterator();
			String totalVariableCostStr = it.next().toString();
			double totalVariableCost = Double.parseDouble(totalVariableCostStr);

			col = (Collection<String>) energyplanmMap
					.get("Fixed operation costs");
			it = col.iterator();
			String fixedOperationalCostStr = it.next().toString();
			double fixedOperationalCost = Double
					.parseDouble(fixedOperationalCostStr);

			col = (Collection<String>) energyplanmMap.get("AnnualHydroElectr.");
			it = col.iterator();
			double hydroPowerProduction = Double.parseDouble(it.next()
					.toString());
			// extract anual PV production
			col = (Collection<String>) energyplanmMap.get("AnnualPVElectr.");
			it = col.iterator();
			double PVproduction = Double.parseDouble(it.next().toString());

			// extract annual import
			col = (Collection<String>) energyplanmMap
					.get("AnnualImportElectr.");
			it = col.iterator();
			double Import = Double.parseDouble(it.next().toString());

			// extract annual export
			col = (Collection<String>) energyplanmMap
					.get("AnnualExportElectr.");
			it = col.iterator();
			double Export = Double.parseDouble(it.next().toString());

			// extract biomass CHP electricity production
			col = (Collection<String>) energyplanmMap
					.get("AnnualHH-CHPElectr.");
			it = col.iterator();
			double biomassCHPElecProduction = Double.parseDouble(it.next()
					.toString());

			// calculate additional cost
		
			double totalAdditionalCost = Math
					.round((hydroPowerProduction + PVproduction + Import
							- Export + biomassCHPElecProduction)
							* addtionalCostPerGWhinKEuro);

		

			// extract
			col = (Collection<String>) energyplanmMap
					.get("Annual Investment costs");
			it = col.iterator();
			String invest = it.next().toString();
			double investmentCost = Double.parseDouble(invest);

			double actualAnnualCost = totalVariableCost + fixedOperationalCost
					+ investmentCost + totalAdditionalCost;

			solution.setObjective(1, actualAnnualCost);

			/*// 3rd objective
			// Trasportation
			col = (Collection<String>) energyplanmMap
					.get("AnnualFlexibleElectr.");
			it = col.iterator();
			double transportElecDemand = Double.parseDouble(it.next()
					.toString());

			col = (Collection<String>) energyplanmMap
					.get("AnnualElectr.Demand");
			it = col.iterator();
			double annualElecDemand = Double.parseDouble(it.next().toString());

			// Individual house HP electric demand
			col = (Collection<String>) energyplanmMap.get("AnnualHH-HPElectr.");
			it = col.iterator();
			double annualHPdemand = Double.parseDouble(it.next().toString());

			solution.setObjective(2, (Import + Export)
					/ (annualElecDemand + transportElecDemand + annualHPdemand));*/

			/*col = (Collection<String>) energyplanmMap.get("Ngas Consumption");
			it = col.iterator();
			double nGasConsumption = Double.parseDouble(it.next().toString());

			// extract oil consumption
			col = (Collection<String>) energyplanmMap.get("Oil Consumption");
			it = col.iterator();
			double oilConsumption = Double.parseDouble(it.next().toString());

			// extract biomass consupmtion
			col = (Collection<String>) energyplanmMap
					.get("Biomass Consumption");
			it = col.iterator();
			double BiomassConsumption = Double
					.parseDouble(it.next().toString());

			// extract solar thermal heat
			col = (Collection<String>) energyplanmMap.get("AnnualHH SolarHeat");
			it = col.iterator();
			double solaThermal = Double.parseDouble(it.next().toString());

			double PVPEF = 1.0;
			double HYPEF = 1.0;
			double BiomassPEF = 1 / 0.17;
			double PEFImport = 2.17;

			double totalPEForElecrcity = PVproduction * PVPEF
					+ hydroPowerProduction * HYPEF + biomassCHPElecProduction
					* BiomassPEF;
			double totalLocalElecProduction = PVproduction
					+ hydroPowerProduction + biomassCHPElecProduction;
			double PEFLocalElec = totalPEForElecrcity
					/ totalLocalElecProduction;

			double totalPEConsumtion = (totalLocalElecProduction - Export)
					* PEFLocalElec
					+ Import
					* PEFImport
					+ BiomassConsumption
					+ oilConsumption
					+ nGasConsumption
					+ (totalHeatDemand * hpHeatPercentage - totalHeatDemand
							* hpHeatPercentage / COP) + solaThermal;

			double ESD = (Import * PEFImport + oilConsumption + nGasConsumption)
					/ totalPEConsumtion;

			solution.setObjective(2, ESD);*/

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
		constraints[0] = 98.84 - annualBiomassConsumption;

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
			double nGasBoilerHeatPercentage,
			double biomassBoilerHeatPercentage,
			double biomassCHPHeatPercentage, double hpHeatPercentage,
			double totalDieselDemandInGWhForTrns,
			double totalElecDemandInGWhForTrns,
			double totalH2DemandInGWhForTrns, double oilSolarPercentage,
			double nGasSolarPercentage, double biomassBoilerSolarPercentage,
			double biomassCHPSolarPercentage, double hpSolarPercentage)
			throws JMException {

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

			// Related solar thermal
			str = "input_HH_oilboiler_Solar=";
			bw.write(str);
			bw.newLine();
			str = "" + oilBoilerFuelDemand * oilSolarPercentage;
			bw.write(str);
			bw.newLine();

			// Ngas boiler fuel demand
			str = "input_fuel_Households[3]=";
			bw.write(str);
			bw.newLine();
			double ngasBoilerFuelDemand = nGasBoilerHeatPercentage
					* totalHeatDemand / nGasBoilerEfficiency;
			str = "" + ngasBoilerFuelDemand;
			bw.write(str);
			bw.newLine();

			// Related solar thermal
			str = "input_HH_ngasboiler_Solar=";
			bw.write(str);
			bw.newLine();
			str = "" + ngasBoilerFuelDemand * nGasSolarPercentage;
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

			// Related solar thermal
			str = "input_HH_bioboiler_Solar=";
			bw.write(str);
			bw.newLine();
			str = "" + biomassBoilerFuelDemand * biomassBoilerSolarPercentage;
			bw.write(str);
			bw.newLine();

			// biomass micro chp heat demand
			str = "input_HH_BioCHP_heat=";
			bw.write(str);
			bw.newLine();
			str = "" + biomassCHPHeatPercentage * totalHeatDemand;
			bw.write(str);
			bw.newLine();

			// Related solar thermal
			str = "input_HH_bioCHP_solar=";
			bw.write(str);
			bw.newLine();
			str = "" + biomassCHPHeatPercentage * totalHeatDemand
					* biomassCHPSolarPercentage;
			bw.write(str);
			bw.newLine();

			// heat pump heat demand
			str = "input_HH_HP_heat=";
			bw.write(str);
			bw.newLine();
			str = "" + hpHeatPercentage * totalHeatDemand;
			bw.write(str);
			bw.newLine();

			// Related solar thermal
			str = "input_HH_HP_Solar=";
			bw.write(str);
			bw.newLine();
			str = "" + hpHeatPercentage * totalHeatDemand * hpSolarPercentage;
			bw.write(str);
			bw.newLine();

			// to handle scintific number
			DecimalFormat df = new DecimalFormat("#");
			df.setMaximumFractionDigits(2);

			// Diesel demand
			str = "input_fuel_Transport[2]=";
			bw.write(str);
			bw.newLine();
			str = "" + df.format(totalDieselDemandInGWhForTrns);
			bw.write(str);
			bw.newLine();

			// number of convensional cars
			double numberOfConCars = (totalDieselDemandInGWhForTrns * Math.pow(
					10, 6))
					/ (efficiencyConCar * averageKMPerYearPerCar * Math.pow(10,
							3));
			str = "Input_Size_transport_conventional_cars=";
			bw.write(str);
			bw.newLine();
			str = "" + numberOfConCars;
			bw.write(str);
			bw.newLine();

			// Electric car electricity demand
			str = "input_transport_TWh=";
			bw.write(str);
			bw.newLine();
			str = "" + totalElecDemandInGWhForTrns;
			bw.write(str);
			bw.newLine();

			// number of electric car
			double numberOfEVCars = totalElecDemandInGWhForTrns
					* Math.pow(10, 6)
					/ (efficiencyEVCar * averageKMPerYearPerCar * Math.pow(10,
							3));
			str = "Input_Size_transport_electric_cars=";
			bw.write(str);
			bw.newLine();
			str = "" + numberOfEVCars;
			bw.write(str);
			bw.newLine();

			/*// hydrogen demand for Transport
			str = "input_fuel_Transport[6]=";
			bw.write(str);
			bw.newLine();
			str = "" + df.format(totalH2DemandInGWhForTrns);
			bw.write(str);
			bw.newLine();

			// number of FCEV car
			double numberOfFCEVCars = totalH2DemandInGWhForTrns
					* Math.pow(10, 6)
					/ (efficiencyFCEVCar * averageKMPerYearPerCar * Math.pow(
							10, 3));
			str = "Input_Size_transport_other_vehicles1=";
			bw.write(str);
			bw.newLine();
			str = "" + numberOfFCEVCars;
			bw.write(str);
			bw.newLine();

			// corresponding eletrolyzer capacity
			int electrolyzerCapacity = (int) Math
					.floor(maxH2DemandInDistribution
							* totalH2DemandInGWhForTrns
							* Math.pow(10, 6)
							/ (efficiencyElectrolyzerTrans * sumH2DemandInDistribution));
			str = "input_cap_ELTtrans_el=";
			bw.write(str);
			bw.newLine();
			str = "" + electrolyzerCapacity;
			bw.write(str);
			bw.newLine();*/

			bw.close();
			// file.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void writeIntoInputFile(double pv, double oilBoilerHeatPercentage,
			double nGasBoilerHeatPercentage,
			double biomassBoilerHeatPercentage,
			double biomassCHPHeatPercentage, double hpHeatPercentage,
			double totalDieselDemandInGWhForTrns,
			double totalElecDemandInGWhForTrns,
			/*double totalH2DemandInGWhForTrns,*/ double oilSolarPercentage,
			double nGasSolarPercentage, double biomassBoilerSolarPercentage,
			double biomassCHPSolarPercentage, double hpSolarPercentage)
			throws IOException {

		String modifiedParameters[] = { "input_RES1_capacity=", // pv (0)
				"input_fuel_Households[2]=", // oil boiler (1)
				"input_HH_oilboiler_Solar=", // oil Solar Thermal (2)
				"input_fuel_Households[3]=", // nGas boiler (3)
				"input_HH_ngasboiler_Solar=", // nGas Solar Thermal (4)
				"input_fuel_Households[4]=", // bioMass boiler (5)
				"input_HH_bioboiler_Solar=", // biomas Solar thermal (6)
				"input_HH_BioCHP_heat=", // biomass CHP (7)
				"input_HH_BioCHP_solar=", // biomass CHP Solar thermal (8)
				"input_HH_HP_heat=", // HP (9)
				"input_HH_HP_solar=", // HP Solar thermal (10)
				"input_fuel_Transport[5]=", // Petrol demand (11)
				"Input_Size_transport_conventional_cars=", // number of
															// convensional cars
															// (12)
				"input_transport_TWh=", // EV car electricity demand (13)
				"Input_Size_transport_electric_cars=", // number of electric car
														// (14)
				/*"input_fuel_Transport[6]=", // hydrogen demand for Transport
											// (15)
				"Input_Size_transport_other_vehicles1=", // number of FCEV car
															// (16)
				"input_cap_ELTtrans_el=" // //corresponding eletrolyzer capacity
											// (17)*/
		};

		String cooresPondingValues[] = new String[modifiedParameters.length];

		File modifiedInput = new File("modifiedInput.txt");
		if (modifiedInput.exists()) {
			modifiedInput.delete();
		}
		modifiedInput.createNewFile();

		FileWriter fw = new FileWriter(modifiedInput.getAbsoluteFile());
		BufferedWriter modifiedInputbw = new BufferedWriter(fw);
		
		String path=""; 
		if(simulatedYear.equals("2020") && simulatedScenario.equals("2DS")){
			path ="C:\\Users\\mahbub\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\EnergyPLAN_12.3\\energyPlan Data\\Data\\VdN SH Eleboration\\VdN_SH_2020_Opt_Scenario_2DS_El_mob.txt";
		}else if(simulatedYear.equals("2020") && simulatedScenario.equals("4DS")){
			path ="C:\\Users\\mahbub\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\EnergyPLAN_12.3\\energyPlan Data\\Data\\VdN SH Eleboration\\VdN_SH_2020_Opt_Scenario_4DS_El_mob.txt";
		}else if(simulatedYear.equals("2030") && simulatedScenario.equals("2DS")){
			path ="C:\\Users\\mahbub\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\EnergyPLAN_12.3\\energyPlan Data\\Data\\VdN SH Eleboration\\VdN_SH_2030_Opt_Scenario_2DS_El_mob.txt";
		}else if(simulatedYear.equals("2030") && simulatedScenario.equals("4DS")){
			path ="C:\\Users\\mahbub\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\EnergyPLAN_12.3\\energyPlan Data\\Data\\VdN SH Eleboration\\VdN_SH_2030_Opt_Scenario_4DS_El_mob.txt";
		}else if(simulatedYear.equals("2050") && simulatedScenario.equals("2DS")){
			path ="C:\\Users\\mahbub\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\EnergyPLAN_12.3\\energyPlan Data\\Data\\VdN SH Eleboration\\VdN_SH_2050_Opt_Scenario_2DS_El_mob.txt";
		}else if(simulatedYear.equals("2050") && simulatedScenario.equals("4DS")){
			path ="C:\\Users\\mahbub\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\EnergyPLAN_12.3\\energyPlan Data\\Data\\VdN SH Eleboration\\VdN_SH_2050_Opt_Scenario_4DS_El_mob.txt";
		}
		
    	BufferedReader mainInputbr = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-16"));
    	
		
		cooresPondingValues[0] = "" + pv;

		double oilBoilerFuelDemand = oilBoilerHeatPercentage * totalHeatDemand
				/ oilBoilerEfficiency;
		cooresPondingValues[1] = "" + oilBoilerFuelDemand;

		double oilSolarThermal = oilBoilerFuelDemand * oilSolarPercentage;
		cooresPondingValues[2] = "" + oilSolarThermal;

		double ngasBoilerFuelDemand = nGasBoilerHeatPercentage
				* totalHeatDemand / nGasBoilerEfficiency;
		cooresPondingValues[3] = "" + ngasBoilerFuelDemand;

		double nGasSolarThermal = ngasBoilerFuelDemand * nGasSolarPercentage;
		cooresPondingValues[4] = "" + nGasSolarThermal;

		double biomassBoilerFuelDemand = biomassBoilerHeatPercentage
				* totalHeatDemand / biomassBoilerEfficiency;
		cooresPondingValues[5] = "" + biomassBoilerFuelDemand;

		double biomassBoilerSolarThermal = biomassBoilerFuelDemand
				* biomassBoilerSolarPercentage;
		cooresPondingValues[6] = "" + biomassBoilerSolarThermal;

		double baiomassCHPHeatDemand = biomassCHPHeatPercentage
				* totalHeatDemand;
		cooresPondingValues[7] = "" + baiomassCHPHeatDemand;

		double biomassCHPSolarThermal = baiomassCHPHeatDemand * efficiencyBiomassCHP *
				 biomassCHPSolarPercentage;
		cooresPondingValues[8] = "" + biomassCHPSolarThermal;

		double hpHEatDemand = hpHeatPercentage * totalHeatDemand;
		cooresPondingValues[9] = "" + hpHEatDemand;

		double hpSolarThermal = hpHeatPercentage * totalHeatDemand
				* hpSolarPercentage;
		cooresPondingValues[10] = "" + hpSolarThermal;

		cooresPondingValues[11] = "" + totalDieselDemandInGWhForTrns;

		double numberOfConCars = (totalDieselDemandInGWhForTrns * Math.pow(10,
				6))
				/ (efficiencyConCar * averageKMPerYearPerCar * Math.pow(10, 3));
		cooresPondingValues[12] = "" + numberOfConCars;

		cooresPondingValues[13] = "" + totalElecDemandInGWhForTrns;

		double numberOfEVCars = totalElecDemandInGWhForTrns * Math.pow(10, 6)
				/ (efficiencyEVCar * averageKMPerYearPerCar * Math.pow(10, 3));
		cooresPondingValues[14] = "" + numberOfEVCars;

		/*cooresPondingValues[15] = "" + totalH2DemandInGWhForTrns;

		double numberOfFCEVCars = totalH2DemandInGWhForTrns
				* Math.pow(10, 6)
				/ (efficiencyFCEVCar * averageKMPerYearPerCar * Math.pow(10, 3));
		cooresPondingValues[16] = "" + numberOfFCEVCars;

		int electrolyzerCapacity = (int) Math.floor(maxH2DemandInDistribution
				* totalH2DemandInGWhForTrns * Math.pow(10, 6)
				/ (efficiencyElectrolyzerTrans * sumH2DemandInDistribution))+1;
		cooresPondingValues[17] = "" + electrolyzerCapacity;*/

		String line;
		while ((line = mainInputbr.readLine()) != null) {
			line.trim();
			boolean trackBreak=false;
			for (int i = 0; i < modifiedParameters.length; i++) {
				if (line.startsWith(modifiedParameters[i]) || line.endsWith(modifiedParameters[i])) {
					modifiedInputbw.write(line+"\n" );
					line = mainInputbr.readLine();
					line=line.replace(line, cooresPondingValues[i]);
					modifiedInputbw.write(line+"\n");
					trackBreak=true;
					break;
				}
			}
			if(trackBreak)
				continue;
			else
				modifiedInputbw.write(line+"\n");

		}

		modifiedInputbw.close();
		mainInputbr.close();
	}

}
