package reet.fbk.eu.OptimizeEnergyPLANTrentoProvince.Problem;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import org.apache.commons.collections.MultiMap;

import reet.fbk.eu.OptimizeEnergyPLANTrentoProvince.parseFile.EnergyPLANFileParseForTrentoProvince;
import reet.fbk.eu.OptimizeEnergyPLANVdN.parseFile.EnergyPLANFileParseForVdN;

/*
 * This is a problem file that is dealing with VdN (mainly introduction of electric cars)
 */
public class EnergyPLANProblemTrentoProvinceV9 extends Problem {

	
	
	MultiMap energyplanmMap;
	String simulatedYear;
	

	
	public static final int averageKMPerYearPerCar = 12900;
	public static final double maxH2DemandInDistribution  = 0.000209257;
	public static final double sumH2DemandInDistribution = 1.0; 
	
	
	
	//transport
	public long totalKMRunByCars ;
	
	
	//other variable
	public double totalHeatDemand;
	public double DHHeatProduction;
	
	//cars efficiencies
	public double efficiencyConCar; // KWh/km
	public double efficiencyEVCar; // KWh/km
	public double efficiencyFCEVCar; // KWh/km

	public double efficiencyBiomassCHPThermal;
	public double efficiencynGasCHPThermal;
	
	// H2
	public double efficiencyElectrolyzerTrans;
	
	public double oilBoilerEfficiency;
	public double nGasBoilerEfficiency;
	public double biomassBoilerEfficiency;
	public double COP;
	
	
	
	//grid distribution cost
	public double elecGridDistributionCost;
	
	//building envelop energy efficiency
	double buildingEnvelopesEnergyEfficiency;
	
	//number of heating element
	int numberOfHeatingTechnologies = 6;
	double upperBoundsHeat[];
	double lowerBoundsHeat[];
	
	//number of different types of cars
	int numberOfDifferentTypesOfCars = 3;
	long lowerBoundsTrans[];
	long upperBoundsTrans[];
	
	//electrical storage
	double lowerBoundsElecStorage[];
	double upperBoundsElecStorage[];
	
	//Hydro capacity
	double lowerBoundsHydroCap[];
	double upperBoundsHydroCap[];
	
	/**
	 * Creates a new instance of problem ZDT1.
	 * 
	 * @param numberOfVariables
	 *            Number of variables.
	 * @param solutionType
	 *            The solution type must "Real", "BinaryReal, and "ArrayReal".
	 */
		  
	public EnergyPLANProblemTrentoProvinceV9(String solutionType, String simulatedYear) {
		
		this.simulatedYear=simulatedYear;
				
		numberOfVariables_ = 18;
		numberOfObjectives_ = 2;
		numberOfConstraints_ = 1;

		problemName_ = "OptimizeEnergyPLANTrentoProvince";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		// Establishes upper and lower limits for the variables
		int var;

		// decision variables
		// index - 0 -> PV Capacity
		// index - 1 -> oil boiler heat percentage
		// index - 2 -> nGas boiler heat percentage
		// index - 3 -> nGas micro chp heat percentage
		// index - 4 -> Biomass boiler heat percentage
		// index - 5 -> Biomass micro chp heat percentage
		// index 6 -> individual HP percentage
		
		// index - 7 -> ICE cars
		// index - 8 -> EV car parcentage
		// index - 9 -> FCEV(H2) car percentage
		

		// index -10 -> solar thermal percentage in oil boiler
		// index -11 -> solar thermal percentage in nGas boiler
		// index 12 -> solar thermal percentage in bnGas CHP
		// index -13 -> solar thermal percentage in biomass boiler
		// index -14 -> solar thermal percentage in biomass CHP
		// index -15 -> solar thermal percentage in HP

		//index -16 -> electrical storage capacity
		//index 17 -> hydro capacity
		
		if(simulatedYear.equals("2025")) {
			// PV upper and lower limit		
			lowerLimit_[0] = 0.0;
			upperLimit_[0] = 435.372;
			
			// index - 1 -> oil boiler heat percentage
			// index - 2 -> nGas boiler heat percentage
			// index - 3 -> nGas micro chp heat percentage
			// index - 4 -> Biomass boiler heat percentage
			// index - 5 -> Biomass micro chp heat percentage
			// index 6 ->  individual HP percentage
		
			lowerBoundsHeat= new double [] {0.000, 0.000, 0.000, 0.000, 0.000, 0.000};
			upperBoundsHeat= new double [] {6.927, 6.927, 6.927, 1.392, 0.034, 6.927};	

			// index - 7 -> ICE cars
			// index - 8 -> EV car parcentage
			// index - 9 -> FCEV(H2) car percentage
			lowerBoundsTrans= new long [] {0000000000L, 0000000000L, 0000000000L };
			upperBoundsTrans= new long [] {9821000000L, 9821000000L, 9821000000L };

			//index -16 -> electrical storage capacity
			lowerBoundsElecStorage= new double [] { 0.000};
			upperBoundsElecStorage= new double [] { 1.741 };	
	
			//index -17-> hydro capacity
			lowerBoundsHydroCap= new double [] { 0.000};
			upperBoundsHydroCap= new double [] { 1748.439 };	
	
			
		}else if(simulatedYear.equals("2030")) {
			// PV upper and lower limit		
			lowerLimit_[0] = 0.0;
			upperLimit_[0] = 658.627;
			
			// index - 1 -> oil boiler heat percentage
			// index - 2 -> nGas boiler heat percentage
			// index - 3 -> nGas micro chp heat percentage
			// index - 4 -> Biomass boiler heat percentage
			// index - 5 -> Biomass 	micro chp heat percentage
			// index 6 ->  individual HP percentage
		
			lowerBoundsHeat= new double [] {0.000, 0.000, 0.000, 0.000, 0.000, 0.000};
			upperBoundsHeat= new double [] {6.888, 6.888, 6.888, 1.384, 0.038, 6.888};	

			// index - 7 -> ICE cars
			// index - 8 -> EV car parcentage
			// index - 9 -> FCEV(H2) car percentage
			lowerBoundsTrans= new long [] {00000000000L, 00000000000L, 00000000000L };
			upperBoundsTrans= new long [] {10071000000L, 10071000000L, 10071000000L };


			//index -16 -> electrical storage capacity 
			lowerBoundsElecStorage= new double [] { 0.000};
			upperBoundsElecStorage= new double [] { 2.635 };	
			
			//index -17-> hydro capacity
			lowerBoundsHydroCap= new double [] { 0.000};
			upperBoundsHydroCap= new double [] { 1753.920 };	

		}else if(simulatedYear.equals("2050")) {
			// PV upper and lower limit		
			lowerLimit_[0] = 0.0;
			upperLimit_[0] = 1380.453;
			
			// index - 1 -> oil boiler heat percentage
			// index - 2 -> nGas boiler heat percentage
			// index - 3 -> nGas micro chp heat percentage
			// index - 4 -> Biomass boiler heat percentage
			// index - 5 -> Biomass micro chp heat percentage
			// index 6 ->  individual HP percentage
		
			lowerBoundsHeat= new double [] {0.000, 0.000, 0.000, 0.000, 0.000, 0.000};
			upperBoundsHeat= new double [] {6.408, 6.408, 6.408, 1.288, 0.044, 6.408};	

			// index - 7 -> ICE cars
			// index - 8 -> EV car parcentage
			// index - 9 -> FCEV(H2) car percentage
			lowerBoundsTrans= new long [] {00000000000L, 00000000000L, 00000000000L };
			upperBoundsTrans= new long [] {10700000000L, 10700000000L, 10700000000L };

			//index -16 -> electrical storage capacity
			lowerBoundsElecStorage= new double [] { 0.000};
			upperBoundsElecStorage= new double [] { 5.522 };	
			
			//index -17-> hydro capacity
			lowerBoundsHydroCap= new double [] { 0.000};
			upperBoundsHydroCap= new double [] { 1877.934 };	
		}
				
		
		// other are the percentage from limit [0,1]
		for (var = 1; var <=6; var++) {
			lowerLimit_[var] = lowerBoundsHeat[var-1];
			upperLimit_[var] = upperBoundsHeat[var-1];
		} // for
		
		// other are the percentage from limit [0,1]
		for (int i = 7; i <=9; i++) {
			lowerLimit_[i] = (double) lowerBoundsTrans[i-7];
			upperLimit_[i] = (double) upperBoundsTrans[i-7];
		} // for
		
		for (int i = 10; i <=15; i++) {
			lowerLimit_[i] = 0.0;
			upperLimit_[i] = 1.0;
		}
		
		lowerLimit_[16] = lowerBoundsElecStorage[0];
		upperLimit_[16] = upperBoundsElecStorage[0];
		
		lowerLimit_[17] = lowerBoundsHydroCap[0];
		upperLimit_[17] = upperBoundsHydroCap[0];
		
		
		
			if (solutionType.compareTo("Real") == 0)
			solutionType_ = new RealSolutionType(this);
		else {
			System.out.println("Error: solution type " + solutionType
					+ " invalid");
			System.exit(-1);
		}
	
		 if(this.simulatedYear.equals("2025")){
			
			// Heat
			 totalHeatDemand = 6.927; // in TWh
			 DHHeatProduction = 0.234; // in TWh

			 //cars
			 efficiencyConCar = 0.498; // KWh/km
			 efficiencyFCEVCar = 0.329; // KWh/km
			 efficiencyEVCar = 0.142; // Kwh/km
			 totalKMRunByCars = 9821000000L;

			 efficiencynGasCHPThermal = 0.47;
			 efficiencyBiomassCHPThermal = 0.47;


			 oilBoilerEfficiency = 0.86;
			 nGasBoilerEfficiency = 0.96;
			 biomassBoilerEfficiency = 0.77;
			 COP = 3.35;

			 // H2
			 efficiencyElectrolyzerTrans = 0.7173;

			 elecGridDistributionCost =  191.01;

			 buildingEnvelopesEnergyEfficiency =  1077117 ; //MWh
		
		 }
		 else if(this.simulatedYear.equals("2030")){
			// Heat
			 totalHeatDemand = 6.888; // in TWh
			 DHHeatProduction = 0.233; // in TWh
			
			//cars
			efficiencyConCar = 0.460; // KWh/km
			efficiencyFCEVCar = 0.299; // KWh/km
			efficiencyEVCar = 0.133; // Kwh/km
			totalKMRunByCars = 10071000000L;
			
			efficiencynGasCHPThermal = 0.48;
			efficiencyBiomassCHPThermal = 0.48;
			
			
			oilBoilerEfficiency = 0.87;
			nGasBoilerEfficiency = 0.97;
			biomassBoilerEfficiency = 0.78;
			COP = 3.44;
			
			// H2
			efficiencyElectrolyzerTrans = 0.7217;
	
			elecGridDistributionCost =  213.61;
			
			buildingEnvelopesEnergyEfficiency = 1147372;
			
	 }else if(this.simulatedYear.equals("2050")){
			// Heat
		 	totalHeatDemand =  6.408; // in TWh
		 	DHHeatProduction = 0.217; // in TWh
		 
			//cars
			efficiencyConCar = 0.340; // KWh/km
			efficiencyFCEVCar = 0.226; // KWh/km
			efficiencyEVCar = 0.106; // Kwh/km
			totalKMRunByCars = 10700000000L;
			
			
			efficiencynGasCHPThermal = 0.50;
			efficiencyBiomassCHPThermal = 0.50;
			
			
			oilBoilerEfficiency = 0.88;
			nGasBoilerEfficiency = 0.98;
			biomassBoilerEfficiency = 0.80;
			COP = 3.75;
			
			// H2
			efficiencyElectrolyzerTrans = 0.7535;
								
			elecGridDistributionCost =  236.31;
			
			buildingEnvelopesEnergyEfficiency = 1363980;
	 }
		
	
	} // constructor end




	/**
	 * Evalua	tes a solution.
	 * 
	 * @param solution
	 *            The solution to evaluate.
	 * @throws JMException
	 */
	@SuppressWarnings("unchecked")
	public void evaluate(Solution solution) throws JMException {

		// decision variables
		// index - 0 -> PV Capacity
	
		
		// index - 7 -> ICE cars
		// index - 8 -> EV car parcentage
		// index - 9 -> FCEV(H2) car percentage
		

		// index -10 -> solar thermal percentage in oil boiler
		// index -11 -> solar thermal percentage in nGas boiler
		// index 12 -> solar thermal percentage in bnGas CHP
		// index -13 -> solar thermal percentage in biomass boiler
		// index -14 -> solar thermal percentage in biomass CHP
		// index -15 -> solar thermal percentage in HP
		// index 16 -> electrical storage
		// index 17 -> hydro capacity
		
		
		// PV
		double pv = solution.getDecisionVariables()[0].getValue();

		repairHeatVariables(solution);
		// index - 1 -> oil boiler heat percentage
		// index - 2 -> nGas boiler heat percentage
		// index - 3 -> nGas micro chp heat percentage
		// index - 4 -> Biomass boiler heat percentage
		// index - 5 -> Biomass micro chp heat percentage
		// index 6 -> individual HP percentage
		double oilBoilerHeatDemand = solution.getDecisionVariables()[1].getValue();
		double nGasBoilerHeatDemand = solution.getDecisionVariables()[2].getValue();
		double nGasCHPHeatDemand = solution.getDecisionVariables()[3].getValue();
		double biomassBoilerHeatDemand = solution.getDecisionVariables()[4].getValue();
		double biomassCHPHeatDemand = solution.getDecisionVariables()[5].getValue();
		double hpHeatDemand = solution.getDecisionVariables()[6].getValue();
		
		
		repairTransVariables(solution);
		
		double totalKMRunByConCar = (long) solution.getDecisionVariables()[7].getValue();
		double totalKMRunByEVCar = (long) solution.getDecisionVariables()[8].getValue();
		double totalKMRunByFCEVCar = (long) solution.getDecisionVariables()[9].getValue();

		double totalPetrolDemandInTWhForTrns = totalKMRunByConCar
				* efficiencyConCar / Math.pow(10, 9);
		double totalElecDemandInTWhForTrns = totalKMRunByEVCar
				* efficiencyEVCar / Math.pow(10, 9);
		double totalH2DemandInTWhForTrns = totalKMRunByFCEVCar
				* efficiencyFCEVCar / Math.pow(10, 9);
				
		// solar thermal percentage
		double oilBoilerSolarPercentage = solution.getDecisionVariables()[10]
				.getValue();
		double nGasBoilerSolarPercentage = solution.getDecisionVariables()[11]
				.getValue();
		double nGasCHPSolarPercentage = solution.getDecisionVariables()[12]
				.getValue();
		double biomassBoilerSolarPercentage = solution.getDecisionVariables()[13]
				.getValue();
		double biomassCHPSolarPercentage = solution.getDecisionVariables()[14]
				.getValue();
		double hpSolarPercentage = solution.getDecisionVariables()[15]
				.getValue();

		//electrical storage
		double elecStorageCapacity = solution.getDecisionVariables()[16]
				.getValue();
		
		//hydro capacity
		double hydroCapacity = solution.getDecisionVariables()[17]
				.getValue();
		try{
		writeIntoInputFile(pv, oilBoilerHeatDemand,
				nGasBoilerHeatDemand, nGasCHPHeatDemand, 
				biomassBoilerHeatDemand, biomassCHPHeatDemand, 
				hpHeatDemand,
				totalPetrolDemandInTWhForTrns, totalElecDemandInTWhForTrns,
				totalH2DemandInTWhForTrns, oilBoilerSolarPercentage,
				nGasBoilerSolarPercentage, biomassBoilerSolarPercentage,
				nGasCHPSolarPercentage, biomassCHPSolarPercentage, hpSolarPercentage, elecStorageCapacity, hydroCapacity);
		}catch(IOException e){
			System.out.print("Pobrlem writting in modified Input file");
		}
		
		String energyPLANrunCommand = ".\\EnergyPLAN_12.3\\EnergyPLAN.exe -i "
				+ "\".\\modifiedInput.txt\" "
				+ "-ascii \"result.txt\" ";
		try{
			Process process = Runtime.getRuntime().exec(energyPLANrunCommand);
			process.waitFor();
			process.destroy();
			
		} catch (IOException e) {
			System.out.println("Energyplan.exe has some problem");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Energyplan interrupted");
		}
		
		EnergyPLANFileParseForTrentoProvince epfp = new EnergyPLANFileParseForTrentoProvince(
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
		
		double co2InImportedEleOil = 0.0, co2InImportedEleNGas=0.0; 
		if(simulatedYear.equals("2025")) {
			co2InImportedEleOil  =( (elecImport/0.51 *1.83/100) * 0.267 );
			co2InImportedEleNGas =( (elecImport/0.51 *47.96/100) * 0.202);
		
		}else if(simulatedYear.equals("2030")) {
			co2InImportedEleOil =( (elecImport/0.53 *0.66/100) * 0.267  );
			co2InImportedEleNGas =( (elecImport/0.53 *43.94/100) * 0.202  );
		}else if(simulatedYear.equals("2050")) {
			co2InImportedEleNGas =( (elecImport/0.56 *12/100) * 0.202 );
		}
		
		localCO2emission = localCO2emission  + co2InImportedEleOil + co2InImportedEleNGas;
		solution.setObjective(0,localCO2emission);
			

		// objective # 2			
		
		col = (Collection<String>) energyplanmMap.get("Variable costs");
		it = col.iterator();
		String totalVariableCostStr = it.next().toString();
		double totalVariableCost = Double.parseDouble(totalVariableCostStr);
		
		col = (Collection<String>) energyplanmMap.get("Bottleneck");
		it = col.iterator();
		String bottleneckStr = it.next().toString();
		double bottlenack =  Double.parseDouble(bottleneckStr);

		totalVariableCost -= bottlenack;
		
		col = (Collection<String>) energyplanmMap
				.get("Fixed operation costs");
		it = col.iterator();
		String fixedOperationalCostStr = it.next().toString();
		double fixedOperationalCost = Double
				.parseDouble(fixedOperationalCostStr);

		// extract
		col = (Collection<String>) energyplanmMap
				.get("Annual Investment costs");
		it = col.iterator();
		String invest = it.next().toString();
		double investmentCost = Double.parseDouble(invest);

		
		//calculation of additional cost
		//read annual electricity demand
		col = (Collection<String>) energyplanmMap.get("AnnualElectr.Demand");
		it = col.iterator();
		String fixedElecDemandStr = it.next().toString();
		double fixedElecDemand = Double.parseDouble(fixedElecDemandStr);
		
		//read HP electric demand
		col = (Collection<String>) energyplanmMap.get("AnnualHH-HPElectr.");
		it = col.iterator();
		String HPElecDemandStr = it.next().toString();
		double HPElecDemand = Double.parseDouble(HPElecDemandStr);
		
		//read electrolyzer elec uses
		col = (Collection<String>) energyplanmMap.get("AnnualTrans H2Electr.");
		it = col.iterator();
		String ElectrolyzerElecDemandStr = it.next().toString();
		double electrolyzerElecDemand = Double.parseDouble(ElectrolyzerElecDemandStr);
		
		//read pump elec uses
		col = (Collection<String>) energyplanmMap.get("AnnualPumpElectr.");
		it = col.iterator();
		String pumpElectricStr = it.next().toString();
		double pumpElectric = Double.parseDouble(pumpElectricStr);
		
			

		//calculate Transportation electricity demand which is totalElecDemandInTWhForTrns
		
		//variable cost
		double additionalVariableCost = (fixedElecDemand + HPElecDemand + totalElecDemandInTWhForTrns+electrolyzerElecDemand
				+ pumpElectric)*elecGridDistributionCost;
		
		//fixed operational cost
		double additionalFixedOperationalCost = DHHeatProduction*3600*0.145/100*0.76;
			
		//annual invesment cost
		double additionalAnnaulInvestmentCost = 	(DHHeatProduction *3600* 0.145 /40)+
										(DHHeatProduction *3600* 0.145 /40/100*6)+
										(buildingEnvelopesEnergyEfficiency*0.0033/30)+
										(buildingEnvelopesEnergyEfficiency* 0.0033 /30/100*6);
		
		double totalAdditionalCost = additionalAnnaulInvestmentCost + additionalFixedOperationalCost
				+additionalVariableCost;
		
		
		double actualAnnualCost = totalVariableCost + fixedOperationalCost
				+ investmentCost + totalAdditionalCost;

		solution.setObjective(1, actualAnnualCost);

			
			// check warning
			/*col = (Collection<String>) energyplanmMap.get("WARNING");
			if (col != null) {
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
		}*/
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
		constraints[0] = 2.46 - annualBiomassConsumption;

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


	void writeIntoInputFile(double pv, double oilBoilerHeatDemand,
			double nGasBoilerHeatDemand, double nGasCHPHeatDemand, 
			double biomassBoilerHeatDemand,
			double biomassCHPHeatDemand, double hpHeatDemand,
			double totalPetrolDemandInTWhForTrns,
			double totalElecDemandInTWhForTrns,
			double totalH2DemandInTWhForTrns, double oilBoilerSolarPercentage,
			double nGasBoilerSolarPercentage, double biomassBoilerSolarPercentage,
			double nGasCHPSolarPercentage, double biomassCHPSolarPercentage, double hpSolarPercentage,
			double elecStorageCapacity, double hydroCapacity)
			throws IOException {

		// decision variables
		// index - 0 -> PV Capacity
		// index - 1 -> oil boiler heat percentage
		// index - 2 -> nGas boiler heat percentage
		// index - 3 -> nGas micro chp heat percentage
		// index - 4 -> Biomass boiler heat percentage
		// index - 5 -> Biomass micro chp heat percentage
		// index 6 -> individual HP percentage
		// index - 6 -> EV car parcentage
		// index - 7 -> FCEV(H2) car percentage
		//last percentage goes to conventional car

		// index -8 -> solar thermal percentage in oil boiler
		// index -9 -> solar thermal percentage in nGas boiler
		// index 10 -> solar thermal percentage in bnGas CHP
		// index -11 -> solar thermal percentage in biomass boiler
		// index -12 -> solar thermal percentage in biomass CHP
		// index -13 -> solar thermal percentage in HP
		// index -14 -> solar thermal percentage in biomass CHP
		// index -15 -> solar thermal percentage in HP
		// index 16 -> electrical storage
		// index 17 -> hydro capacity

		
		String modifiedParameters[] = { "input_RES1_capacity=", // pv (0)
				"input_fuel_Households[2]=", // oil boiler (1)
				"input_HH_oilboiler_Solar=", // oil Solar Thermal (2)
				"input_fuel_Households[3]=", // nGas boiler (3)
				"input_HH_ngasboiler_Solar=", // nGas boiler Solar Thermal (4)
				"input_HH_NgasCHP_heat=",    // nGas micro chp (5)
				"input_HH_NgasCHP_solar=",   // nGas microp chp solar (6)
				"input_fuel_Households[4]=", // bioMass boiler (7)
				"input_HH_bioboiler_Solar=", // biomas boiler Solar thermal (8)
				"input_HH_BioCHP_heat=", // biomass CHP (9)
				"input_HH_BioCHP_solar=", // biomass CHP Solar thermal (10)
				"input_HH_HP_heat=", // HP (11)
				"input_HH_HP_solar=", // HP Solar thermal (12)
				"input_fuel_Transport[5]=", // Petrol demand (13)
				"Input_Size_transport_conventional_cars=", // number of convensional cars (14)
				"input_transport_TWh=", // EV car electricity demand (15)
				"Input_Size_transport_electric_cars=", // number of electric car (16)
			
				"input_fuel_Transport[6]=", // hydrogen demand for Transport (17)
				"Input_Size_transport_other_vehicles1=", // number of FCEV car (18)
				"input_cap_ELTtrans_el=", // //corresponding eletrolyzer capacity (19)
				
				"input_cap_pump_el=", //Pump capacity (20)
				"input_cap_turbine_el=", //turbine capacity (21)
				"input_cap_turbine_el=", // elec storage capacity (22)
		
		
				"input_hydro_cap=", //hydro capacity (23)
				"input_hydro_watersupply=" //hydro water supply
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
		if(simulatedYear.equals("2025") ){
			path =".\\EnergyPLAN_12.3\\energyPlan Data\\Data\\PEAP\\PEAP_PAT_LC_2025_DH_v9_opt.txt";
		}else if(simulatedYear.equals("2030") ){
			path =".\\EnergyPLAN_12.3\\energyPlan Data\\Data\\PEAP\\PEAP_PAT_LC_2030_DH_v9_opt.txt";
		}else if(simulatedYear.equals("2050")) {
			path =".\\EnergyPLAN_12.3\\energyPlan Data\\Data\\PEAP\\PEAP_PAT_LC_2050_DH_v9_opt.txt";
		}
		
    	BufferedReader mainInputbr = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-16"));
    	
		
		cooresPondingValues[0] = "" + pv;

		double oilBoilerFuelDemand = oilBoilerHeatDemand	/ oilBoilerEfficiency;
		cooresPondingValues[1] = "" + oilBoilerFuelDemand;

		double oilBoilerSolarThermal = oilBoilerFuelDemand * oilBoilerSolarPercentage;
		cooresPondingValues[2] = "" + oilBoilerSolarThermal;

		double nGasBoilerFuelDemand = nGasBoilerHeatDemand / nGasBoilerEfficiency;
		cooresPondingValues[3] = "" + nGasBoilerFuelDemand;
		
		double nGasBoilerSolarThermal = nGasBoilerFuelDemand * nGasBoilerSolarPercentage;
		cooresPondingValues[4] = "" + nGasBoilerSolarThermal;

		cooresPondingValues[5] = "" + nGasCHPHeatDemand; 
		
		double nGasCHPSolarThermal = nGasCHPHeatDemand * efficiencynGasCHPThermal * nGasCHPSolarPercentage;
		cooresPondingValues[6] = "" + nGasCHPSolarThermal;
		
		double biomassBoilerFuelDemand = biomassBoilerHeatDemand / biomassBoilerEfficiency;
		cooresPondingValues[7] = "" + biomassBoilerFuelDemand;

		double biomassBoilerSolarThermal = biomassBoilerFuelDemand * biomassBoilerSolarPercentage;
		cooresPondingValues[8] = "" + biomassBoilerSolarThermal;

		cooresPondingValues[9] = "" + biomassCHPHeatDemand;

		double biomassCHPSolarThermal = biomassCHPHeatDemand * efficiencyBiomassCHPThermal *
				 biomassCHPSolarPercentage;
		cooresPondingValues[10] = "" + biomassCHPSolarThermal;

		cooresPondingValues[11] = "" + hpHeatDemand;

		double hpSolarThermal = hpHeatDemand* hpSolarPercentage;
		cooresPondingValues[12] = "" + hpSolarThermal;

		cooresPondingValues[13] = "" + totalPetrolDemandInTWhForTrns;

		double numberOfConCars = (totalPetrolDemandInTWhForTrns * Math.pow(10, 9))
				/ (efficiencyConCar * averageKMPerYearPerCar * Math.pow(10, 3));
		cooresPondingValues[14] = "" + numberOfConCars;

		
		cooresPondingValues[15] = "" + totalElecDemandInTWhForTrns;

		double numberOfEVCars = totalElecDemandInTWhForTrns * Math.pow(10, 9)
				/ (efficiencyEVCar * averageKMPerYearPerCar * Math.pow(10, 3));
		cooresPondingValues[16] = "" + numberOfEVCars;

		cooresPondingValues[17] = "" + totalH2DemandInTWhForTrns;

		double numberOfFCEVCars = totalH2DemandInTWhForTrns
				* Math.pow(10, 9)
				/ (efficiencyFCEVCar * averageKMPerYearPerCar * Math.pow(10, 3));
		cooresPondingValues[18] = "" + numberOfFCEVCars;

		double electrolyzerCapacity = (int) Math.floor(maxH2DemandInDistribution
				* totalH2DemandInTWhForTrns * Math.pow(10, 6)
				/ (efficiencyElectrolyzerTrans * sumH2DemandInDistribution))+1;
		cooresPondingValues[19] = "" + electrolyzerCapacity;

		cooresPondingValues[20] = "" + elecStorageCapacity  * 1000 / 2;
		cooresPondingValues[21] = "" + elecStorageCapacity  * 1000 / 2;
		cooresPondingValues[22] = "" + elecStorageCapacity;
		
		
		cooresPondingValues[23] = ""+hydroCapacity;
		cooresPondingValues[24] = ""+hydroCapacity*0.002664;
		
		
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
	

	void repairHeatVariables(Solution s) {
		
		double ranges[] = new double[numberOfHeatingTechnologies];
		for(int i=0;i<ranges.length;i++) {
			ranges[i] = upperBoundsHeat[i]-lowerBoundsHeat[i];
		}
		
		double array[] = new double[lowerBoundsHeat.length];
		
			for(int i=0;i<numberOfHeatingTechnologies;i++) {
				try {
					array[i]= s.getDecisionVariables()[i+1].getValue();
				} catch (JMException e) {
					System.out.println("something wrone in heat calculation");
				}
			}
			
			
			while(Math.abs(addArray(array) - totalHeatDemand) >0.005) {
			double arraySum = addArray(array);
			if(arraySum>totalHeatDemand) {
				//we need to reduce from each technology
				for(int i=0;i<array.length;i++) {
					double reduceForThisTech = (arraySum-totalHeatDemand)*ranges[i]/addArray(ranges);
					if((array[i] - reduceForThisTech) < lowerBoundsHeat[i]) {
						array[i] = lowerBoundsHeat[i];
					}else {
						array[i]=array[i]-reduceForThisTech;
					}
				}
				
			}else {
				//we need to increase from each technology
				for(int i=0;i<array.length;i++) {
					double increaseForThisTech = (totalHeatDemand-arraySum)*ranges[i]/addArray(ranges);
					if((array[i] + increaseForThisTech) > upperBoundsHeat[i]) {
						array[i] = upperBoundsHeat[i];
					}else {
						array[i]=array[i]+increaseForThisTech;
					}
				}
			}
		}
		
			for (int i = 0; i < numberOfHeatingTechnologies; i++) {
				try {
					s.getDecisionVariables()[i+1].setValue(array[i ]);
				} catch (JMException e) {
					System.out.println("something wrone in heat calculation");
				}
			}
		
	}
	
	void repairTransVariables(Solution s) {
		long ranges[] = new long[lowerBoundsTrans.length];
		for(int i=0;i<ranges.length;i++) {
			ranges[i] = upperBoundsTrans[i]-lowerBoundsTrans[i];
		}
		
		long array[] = new long[lowerBoundsTrans.length];
		
		for(int i=0;i<numberOfDifferentTypesOfCars;i++) {
			try {
				array[i]= (long) s.getDecisionVariables()[i+7].getValue();
			} catch (JMException e) {
				System.out.println("something wrone in trans repair");
			}
		}
	
		
		while(Math.abs(addArray(array) - totalKMRunByCars) >10) {
		long arraySum = addArray(array);
		if(arraySum>totalKMRunByCars) {
			//we need to decrease from each technology
			for(int i=0;i<array.length;i++) {
				BigInteger a = new BigInteger(Long.toString(arraySum-totalKMRunByCars));
				BigInteger b = new  BigInteger(Long.toString(ranges[i]));
				BigInteger	c =	a.multiply(b);
				BigInteger d = new  BigInteger(Long.toString(addArray(ranges)));
				BigInteger e = c.divide(d);
				long reduceForThisTech = e.longValue();
				if((array[i] - reduceForThisTech) < lowerBoundsTrans[i]) {
					array[i] = lowerBoundsTrans[i];
				}else {
					array[i]=array[i]-reduceForThisTech;
				}
			}
			
		}else {
			//we need to increase from each technology
			for(int i=0;i<array.length;i++) {
				BigInteger a = new BigInteger(Long.toString( totalKMRunByCars-arraySum ));
				BigInteger b = new  BigInteger(Long.toString(ranges[i]));
				BigInteger	c =	a.multiply(b);
				BigInteger d = new  BigInteger(Long.toString(addArray(ranges)));
				BigInteger e = c.divide(d);
				long increaseForThisTech = e.longValue();
				if((array[i] + increaseForThisTech) > upperBoundsTrans[i]) {
					array[i] = upperBoundsTrans[i];
				}else {
					array[i]=array[i]+increaseForThisTech;
				}
			}
		}
		}
		
		for (int i = 0; i < numberOfDifferentTypesOfCars; i++) {
			try {
				s.getDecisionVariables()[i+7].setValue(array[i ]);
			} catch (JMException e) {
				System.out.println("something wrone in heat calculation");
			}
		}
	
	

	}
	
	static double addArray(double []arr) {
		double sum=0.0;
		for(int i=0;i<arr.length;i++) {
			sum+=arr[i];
		}
		return sum;
	}
	static long addArray(long []arr) {
		long sum=0L;
		for(int i=0;i<arr.length;i++) {
			sum+=arr[i];
		}
		return sum;
	}


}
