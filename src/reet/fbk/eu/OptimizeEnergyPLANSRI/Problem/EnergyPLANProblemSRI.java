package reet.fbk.eu.OptimizeEnergyPLANSRI.Problem;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.jfree.date.AnnualDateRule;

import reet.fbk.eu.OptimizeEnergyPLANSRI.parseFile.EnergyPLANFileParseForSRI;
import reet.fbk.eu.OptimizeEnergyPLANSRI.util.ExtractEnergyPLANParametersSRI;
import reet.fbk.eu.OptimizeEnergyPLANTrentoProvince.parseFile.EnergyPLANFileParseForTrentoProvince;
import reet.fbk.eu.OptimizeEnergyPLANTrentoProvince.util.ExtractEnergyPLANParametersTrentoProvinceV10;
import reet.fbk.eu.OptimizeEnergyPLANVdN.parseFile.EnergyPLANFileParseForVdN;
import reet.fbk.eu.OptimizedEnergyPLANFelicetti.parseFile.EnergyPLANFileParseForFelicetti;

/*
 * This is a problem file that is dealing with VdN (mainly introduction of electric cars)
 */
public class EnergyPLANProblemSRI extends Problem {


	

	HashMap <ArrayList<String>, MultiMap > hmForAllSolutions;
	ExtractEnergyPLANParametersSRI ex;
	
	
	MultiMap energyplanmMap;
	String simulatedYear, folder;
	

	
	public static final int averageKMPerYearPerCar = 21333;
	public static final double maxIndvH2DemandInDistribution  = 0.0001356;
	public static final double sumIndvH2DemandInDistribution = 1.0; 
	
	public static final double maxFeedStockH2DemandInDistribution  = 0.000248211;
	public static final double sumFeedStockH2DemandInDistribution = 1.0; 
	
	
	
	public static final double totalH2FeedStock = 123.62; 
	
	//transport
	public long totalKMRunByCars ;
	
	
	//other variable
	public double totalHeatDemand;
	
	//cars efficiencies
	public double efficiencyDieselCar; // KWh/km
	public double efficiencyPetrolCar; // KWh/km
	public double efficiencyEVCar; // KWh/km

	public double efficiencyH2CHPThermal;
	public double efficiencynGasCHPThermal;
	
	// H2
	public double efficiencyElectrolyzer;
	
	public double oilBoilerEfficiency;
	public double nGasBoilerEfficiency;
	public double biomassBoilerEfficiency;
	public double COP;
	
	
	
	//grid distribution cost
	public double elecGridDistributionCost;
	
	//number of heating element
	int numberOfHeatingTechnologies = 3;
	double upperBoundsHeat[];
	double lowerBoundsHeat[];
	
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
		  
	public EnergyPLANProblemSRI(String solutionType, String simulatedYear,
			String folder) {
		
		this.simulatedYear=simulatedYear;
		this.folder = folder;		
		
		numberOfVariables_ = 13;
		numberOfObjectives_ = 2;
		numberOfConstraints_ = 0;

		problemName_ = "EnergyPLANProblemFelicettiV2";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		// Establishes upper and lower limits for the variables
		int var;

		// decision variables
		// index - 0 -> PV Capacity
		// 
		
		// index - 1 -> nGas micro chp heat production
		// index - 2 -> nGas boiler heat production
		// index - 3 -> H2  micro chp heat production 
		
		
		// index - 4 -> solar thermal percentage in nGas chp
		//index    5 -> solar thermal percentage in nGas boiler
		// index - 6 -> solar thermal percentage in h2 chp
		
		// index - 7 -> Diesel car parcentage
		// index - 8 -> Petrol cars percentage
		// index - 9 -> EV car percentage
				
		
		//index -10 -> electrical storage capacity
		//index 11 -> wave power capacity
		
		//index 12-> H2 transport (feed stock)
		
		if(simulatedYear.equals("2025")) {
			// PV upper and lower limit		
			lowerLimit_[0] = 0.0;
			upperLimit_[0] = 54190.00;
			
			
			lowerBoundsHeat= new double [] {0.000, 0.000, 0.000 };
			upperBoundsHeat= new double [] {1177.06, 1177.06, 1177.06 };	

		
			lowerBoundsTrans= new long [] {0000000L, 0000000L, 0000000L };
			upperBoundsTrans= new long [] {1152001L, 1152001L, 1152001L };

			
			// electrical storage capacity
			lowerBoundsElecStorage= new double [] { 0.000};
			upperBoundsElecStorage= new double [] { 108.0 };	
	
			//wave power
			lowerLimit_[11]=0.0;
			upperLimit_[11]=1884.0;
			
			//feed stock hydrogen
			lowerLimit_[12]=0.0;
			upperLimit_[12]=123.62;
		}		
		// other are the percentage from limit [0,1]
		for (var = 1; var <=3; var++) {
			lowerLimit_[var] = lowerBoundsHeat[var-1];
			upperLimit_[var] = upperBoundsHeat[var-1];
		} // for
		
	
		//solr thermal
		for (int i = 4; i <=6; i++) {
			lowerLimit_[i] = 0.0;
			upperLimit_[i] = 1.0;
		}
		
		for (int i = 7; i <=9; i++) {
			lowerLimit_[i] = (double) lowerBoundsTrans[i-7];
			upperLimit_[i] = (double) upperBoundsTrans[i-7];
		} // for
		
		lowerLimit_[10] = lowerBoundsElecStorage[0];
		upperLimit_[10] = upperBoundsElecStorage[0];
		
		
		
		
			if (solutionType.compareTo("Real") == 0)
			solutionType_ = new RealSolutionType(this);
		else {
			System.out.println("Error: solution type " + solutionType
					+ " invalid");
			System.exit(-1);
		}
	
		 if(this.simulatedYear.equals("2025")){
			
			// Heat
			 totalHeatDemand = 1177.06; // in GWh
			 
			
			 efficiencynGasCHPThermal = 0.45;
			 efficiencyH2CHPThermal = 0.86;


			 nGasBoilerEfficiency = 0.86;
			 
		
			 efficiencyDieselCar = 0.485; // KWh/km
			 efficiencyPetrolCar = 0.538; // KWh/km
			 efficiencyEVCar = 0.144; // Kwh/km
			 totalKMRunByCars = 1152001L;

			 
			 efficiencyElectrolyzer = 0.7;
			 
			 // 
			 		 
			 elecGridDistributionCost =  49.995;

		
			 
		 }
			 	
			
			hmForAllSolutions = new HashMap();
			
			ex = new ExtractEnergyPLANParametersSRI(simulatedYear);
			
			try {
				ex.readProfiles();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

		MultiMap modifyMap = new MultiValueMap();
		
		
		
		
		// PV
		double pv = solution.getDecisionVariables()[0].getValue();

		
		repairHeatVariables(solution);
		
		// index - 1 -> nGas micro chp heat production
				// index - 2 -> nGas boiler heat production
				// index - 3 -> H2  micro chp heat production 
				
				
		double nGasCHPHeatDemand = solution.getDecisionVariables()[1].getValue();
		double nGasBoilerHeatDemand = solution.getDecisionVariables()[2].getValue();
		double h2CHPHeatDemand = solution.getDecisionVariables()[3].getValue();
				
				
				
				// index - 4 -> solar thermal percentage in nGas chp
				//index    5 -> solar thermal percentage in nGas boiler
				// index - 6 -> solar thermal percentage in h2 chp

			
		double nGasCHPSolarPercentage = solution.getDecisionVariables()[4]
				.getValue();
		double nGasBoilerSolarPercentage = solution.getDecisionVariables()[5]
				.getValue();
		double h2CHPSolarPercentage = solution.getDecisionVariables()[6]
				.getValue();
		
		
		// index - 7 -> Diesel car parcentage
				// index - 8 -> Petrol cars percentage
				// index - 9 -> EV car percentage
		repairTransVariables(solution);
		
		double totalKMRunByDieselCar = (long) solution.getDecisionVariables()[7].getValue();
		double totalKMRunByPetrolCar = (long) solution.getDecisionVariables()[8].getValue();
		double totalKMRunByEVCar = (long) solution.getDecisionVariables()[9].getValue();
		
		double totalDieselDemandInTWhForTrns = totalKMRunByDieselCar
				* efficiencyDieselCar / Math.pow(10, 6);
		double totalPetrolDemandInTWhForTrns = totalKMRunByPetrolCar
				* efficiencyPetrolCar / Math.pow(10, 6);
		double totalElecDemandInTWhForTrns = totalKMRunByEVCar
				* efficiencyEVCar / Math.pow(10, 6);
		
		//electrical storage
		double elecStorageCapacity = solution.getDecisionVariables()[10]
				.getValue();
		
		//wave power
		double wavePower = solution.getDecisionVariables()[11].getValue();
		
		//H2 transport (feed stock)
		double H2FeedStock = solution.getDecisionVariables()[12].getValue();
		
	
		try{
		 modifyMap = writeIntoInputFile(pv, 
				 nGasCHPHeatDemand, nGasBoilerHeatDemand,   h2CHPHeatDemand, 
				nGasCHPSolarPercentage, nGasBoilerSolarPercentage, h2CHPSolarPercentage,  
				totalDieselDemandInTWhForTrns, totalPetrolDemandInTWhForTrns, totalElecDemandInTWhForTrns,
				elecStorageCapacity, wavePower, H2FeedStock );
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
		
		EnergyPLANFileParseForSRI epfp = new EnergyPLANFileParseForSRI(
				".\\result.txt");
		energyplanmMap = epfp.parseFile();
		energyplanmMap.putAll(modifyMap);
		
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
		
		double co2InImportedEleOil = 0.0, co2InImportedEleNGas=0.0, co2InImportedEleCoal=0.0;  
		
			
		co2InImportedEleOil  =( (elecImport/0.51 *1.93/100) * 0.267 );
		co2InImportedEleNGas =( (elecImport/0.51 *44.25/100) * 0.202);
		
		//CO2 related to H2 feed stock (imported)
		double co2InImportedH2 = (totalH2FeedStock - H2FeedStock)*0.2525;
		
		
		localCO2emission = localCO2emission  + co2InImportedEleOil + co2InImportedEleNGas + co2InImportedH2;
		energyplanmMap.put("CO2Emission", localCO2emission);
		
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
		energyplanmMap.put("Bottleneck", bottlenack);

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
		double additionalVariableCost = (totalH2FeedStock - H2FeedStock) * 61.17 + elecImport*elecGridDistributionCost;
		
			
		double totalAdditionalCost = additionalVariableCost;
		
		
		double actualAnnualCost = totalVariableCost + fixedOperationalCost
				+ investmentCost + totalAdditionalCost;
		
		energyplanmMap.put("AdditionalCost", totalAdditionalCost);
		energyplanmMap.put("AnnualCost", actualAnnualCost);

		solution.setObjective(1, actualAnnualCost);

			
		
		col = (Collection<String>) energyplanmMap.get("AnnualNGasBoilerheat");
		it = col.iterator();
		double AnnualNGasBoilerheat = Double.parseDouble(it.next().toString());
		
		col = (Collection<String>) energyplanmMap.get("NGasBoilerSolarInput");
		it = col.iterator();
		double NGasBoilerInput = Double.parseDouble(it.next().toString());
		double NGasBoilerUtilization = ex.solarUtilizationCalculation(AnnualNGasBoilerheat, NGasBoilerInput);
		energyplanmMap.put("NGasBoilerUtilization", NGasBoilerUtilization);
		

		col = (Collection<String>) energyplanmMap.get("AnnualNgasmCHPheat");
		it = col.iterator();
		double AnnualNgasmCHPheat = Double.parseDouble(it.next().toString());
		col = (Collection<String>) energyplanmMap.get("NGasCHPSolarInput");
		it = col.iterator();
		double nGasCHPSolarInput = Double.parseDouble(it.next().toString());
		double nGasCHPSolarUtilization = ex.solarUtilizationCalculation(AnnualNgasmCHPheat, nGasCHPSolarInput);
		energyplanmMap.put("NgasCHPSolarUtilization", nGasCHPSolarUtilization);
		
		col = (Collection<String>) energyplanmMap.get("AnnualH2mCHPheat");
		it = col.iterator();
		double AnnualH2mCHPheat = Double.parseDouble(it.next().toString());
		col = (Collection<String>) energyplanmMap.get("H2CHPSolarInput");
		it = col.iterator();
		double H2CHPSolarInput = Double.parseDouble(it.next().toString());
		double H2CHPSolarUtilization = ex.solarUtilizationCalculation(AnnualH2mCHPheat, H2CHPSolarInput);
		energyplanmMap.put("H2CHPSolarUtilization", H2CHPSolarUtilization);
		
		
		
		this.evaluateConstraints(solution);
	
		
		if(solution.getOverallConstraintViolation()>=0) {
			String line="";
			for(int i=0;i<solution.numberOfVariables();i++) {
				line = line + " " +solution.getDecisionVariables()[i];
			}
			//MultiMap energyplanmMapForSimulation;
			
			//energyplanmMapForSimulation = ex.simulateEnergyPLAN(line);
				//ex.WriteEnergyPLANParametersFromEvaluateToFile(energyplanmMap, folder);
				hmForAllSolutions.put(solutonToString(solution), energyplanmMap);
			
		}
		
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
	
	}


	MultiMap writeIntoInputFile(double pv, 
			double nGasCHPHeatDemand, double nGasBoilerHeatDemand,  double h2CHPHeatDemand, 
			double nGasCHPSolarPercentage, double nGasBoilerSolarPercentage, double h2CHPSolarPercentage,  
			double totalDieselDemandInTWhForTrns, double totalPetrolDemandInTWhForTrns, double totalElecDemandInTWhForTrns,
			double elecStorageCapacity, double wavePower, double H2FeedStock)
			throws IOException {
		
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		
		// index - 0 -> PV Capacity
		// 
		
		// index - 1 -> nGas micro chp heat production
		// index - 2 -> nGas boiler heat production
		// index - 3 -> H2  micro chp heat production 
		
		
		// index - 4 -> solar thermal percentage in nGas chp
		//index    5 -> solar thermal percentage in nGas boiler
		// index - 6 -> solar thermal percentage in h2 chp
		
		// index - 7 -> Diesel car parcentage
		// index - 8 -> Petrol cars percentage
		// index - 9 -> EV car percentage
				
		
		//index -10 -> electrical storage capacity
		//index 11 -> wave power capacity
		
		//index 12-> H2 transport (feed stock)

		
	MultiMap modifyMap = new MultiValueMap();
		
		modifyMap.put("AnnualNGasBoilerheat",
				Math.round(nGasBoilerHeatDemand * 100.0) / 100.0);
		modifyMap.put("AnnualNgasmCHPheat",
				Math.round(nGasCHPHeatDemand * 100.0) / 100.0);
		modifyMap.put("AnnualH2mCHPheat",
				Math.round(h2CHPHeatDemand * 100.0) / 100.0);


		modifyMap.put("PV_Cap", (int) Math.round(pv));

		// decision variables
		
		
		ArrayList<String> pvHM = new ArrayList<String>(Arrays.asList("input_RES1_capacity="));
		ArrayList<String> nGasBoilerHM = new ArrayList<String>(Arrays.asList("input_fuel_Households[3]="));
		ArrayList<String> nGasBoilerSolarHM = new ArrayList<String>(Arrays.asList("input_HH_ngasboiler_Solar="));
		ArrayList<String> nGasmCHPHM = new ArrayList<String>(Arrays.asList("input_HH_NgasCHP_heat="));
		ArrayList<String> nGasmCHPSolarHM = new ArrayList<String>(Arrays.asList("input_HH_NgasCHP_solar="));
		ArrayList<String> h2mCHPHM = new ArrayList<String>(Arrays.asList("input_HH_H2CHP_heat="));
		ArrayList<String> h2mCHPSolarHM = new ArrayList<String>(Arrays.asList("input_HH_H2CHP_solar="));
		ArrayList<String> indvElectrolyzerCapacityHM = new ArrayList<String>(Arrays.asList("input_cap_eltCHPindv_el="));
		
		
		
		// transport
		ArrayList<String> dieselDemandTrnsHM = new ArrayList<String>(Arrays.asList("input_fuel_Transport[2]="));
		ArrayList<String> petrolDemandTrnsHM = new ArrayList<String>(Arrays.asList("input_fuel_Transport[3]="));
		ArrayList<String> elecDemandTrnsHM = new ArrayList<String>(Arrays.asList("input_transport_TWh="));
		
		ArrayList<String> numberOfConCarsHM = new ArrayList<String>(Arrays.asList("Input_Size_transport_conventional_cars="));
		ArrayList<String> numberOfEVCarsHM = new ArrayList<String>(Arrays.asList("Input_Size_transport_electric_cars="));
			
		
		//electrical storage
		ArrayList<String> pumpCapHM = new ArrayList<String>(Arrays.asList("input_cap_pump_el="));
		ArrayList<String> turCapHM = new ArrayList<String>(Arrays.asList("input_cap_turbine_el"));
		ArrayList<String> elStorageCapHM = new ArrayList<String>(Arrays.asList("input_storage_pump_cap="));
		
		//wave power
		ArrayList<String> wavePowerHM = new ArrayList<String>(Arrays.asList("input_RES2_capacity="));
		
		//H2 (feed stock) , modeled as H2 transport
		ArrayList<String> h2FeedStockHM = new ArrayList<String>(Arrays.asList("input_fuel_Transport[6]="));
		ArrayList<String> feedStockH2ElectrolyzerHM = new ArrayList<String>(Arrays.asList("input_cap_ELTtrans_el="));
			
		
	
		File modifiedInput = new File("modifiedInput.txt");
		if (modifiedInput.exists()) {
			modifiedInput.delete();
		}
		modifiedInput.createNewFile();

		FileWriter fw = new FileWriter(modifiedInput.getAbsoluteFile());
		BufferedWriter modifiedInputbw = new BufferedWriter(fw);
		
		String path=""; 
		if(simulatedYear.equals("2025") ){
			path =".\\EnergyPLAN_12.3\\energyPlan Data\\Data\\SRI_2025.txt"; 
		}
		
    	BufferedReader mainInputbr = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-16"));
    	
    	double totalSolarThermalInput=0.0;
		
    	
    	pvHM.add("" + pv);
    	list.add(pvHM);
    	//cooresPondingValues[0] = "" + pv;

		

		double nGasBoilerFuelDemand = nGasBoilerHeatDemand / nGasBoilerEfficiency;
		nGasBoilerHM.add("" + nGasBoilerFuelDemand);
		list.add(nGasBoilerHM);
		//cooresPondingValues[3] = "" + nGasBoilerFuelDemand;
		modifyMap.put("ngasBoilerFuelDemand", nGasBoilerFuelDemand);

		
		double nGasBoilerSolarThermal = nGasBoilerFuelDemand * nGasBoilerSolarPercentage;
		nGasBoilerSolarHM.add("" + nGasBoilerSolarThermal);
		list.add(nGasBoilerSolarHM);
		//cooresPondingValues[4] = "" + nGasBoilerSolarThermal;
		totalSolarThermalInput+=nGasBoilerSolarThermal;
		modifyMap.put("NGasBoilerSolarInput", nGasBoilerSolarThermal);


		nGasmCHPHM.add("" + nGasCHPHeatDemand);
		list.add(nGasmCHPHM);
		//cooresPondingValues[5] = "" + nGasCHPHeatDemand; 
		
		
		double nGasCHPSolarThermal = nGasCHPHeatDemand * efficiencynGasCHPThermal * nGasCHPSolarPercentage;
		nGasmCHPSolarHM.add("" + nGasCHPSolarThermal);
		list.add(nGasmCHPSolarHM);
		totalSolarThermalInput+=nGasCHPSolarThermal;
		modifyMap.put("NGasCHPSolarInput", nGasCHPSolarThermal);

		h2mCHPHM.add("" + h2CHPHeatDemand);
		list.add(h2mCHPHM);

		double h2CHPSolarThermal = h2CHPHeatDemand * efficiencyH2CHPThermal * h2CHPSolarPercentage;
		h2mCHPSolarHM.add("" + h2CHPSolarThermal);
		list.add(h2mCHPSolarHM);
		totalSolarThermalInput+=h2CHPSolarThermal;
		modifyMap.put("H2CHPSolarInput", h2CHPSolarThermal);

		//setting individual H2 electrolyzer cap
		double indvH2CHPFueldemand =  h2CHPHeatDemand/ efficiencyH2CHPThermal;
		double indvElectrolyzerCapacity = (int) Math.floor(maxIndvH2DemandInDistribution
				* indvH2CHPFueldemand * Math.pow(10, 6)
				/ (efficiencyElectrolyzer * sumIndvH2DemandInDistribution))+1;
		indvElectrolyzerCapacityHM.add(""+indvElectrolyzerCapacity);
		list.add(indvElectrolyzerCapacityHM);
		modifyMap.put("IndvEloctrolyzer_Cap", indvElectrolyzerCapacity);
		
		modifyMap.put("SolarThermal", totalSolarThermalInput);
		

		dieselDemandTrnsHM.add(""+totalDieselDemandInTWhForTrns);
		list.add(dieselDemandTrnsHM);
		
		double numberOfDieselCars = totalDieselDemandInTWhForTrns * Math.pow(10, 6)
				/ (efficiencyDieselCar * averageKMPerYearPerCar * Math.pow(10, 3));
		modifyMap.put("NumberOfDieselCars", numberOfDieselCars);
		
		petrolDemandTrnsHM.add(""+totalPetrolDemandInTWhForTrns);
		list.add(petrolDemandTrnsHM);
		
		double numberOfPetrolCars = totalPetrolDemandInTWhForTrns * Math.pow(10, 6)
				/ (efficiencyPetrolCar * averageKMPerYearPerCar * Math.pow(10, 3));
		modifyMap.put("NumberOfPetrolCars", numberOfPetrolCars);
		
		numberOfConCarsHM.add(""+(numberOfDieselCars+numberOfPetrolCars));
		list.add(numberOfConCarsHM);
		
		elecDemandTrnsHM.add(""+totalElecDemandInTWhForTrns);
		list.add(elecDemandTrnsHM);
		
		double numberOfEVCars = totalElecDemandInTWhForTrns * Math.pow(10, 6)
				/ (efficiencyEVCar * averageKMPerYearPerCar * Math.pow(10, 3));
		modifyMap.put("NumberOfEVCars", numberOfEVCars);
		
		numberOfEVCarsHM.add(""+(numberOfEVCars));
		list.add(numberOfEVCarsHM);
		
					
				
		turCapHM.add("" + elecStorageCapacity  * 1000 / 2);
		list.add(turCapHM);
		
		//cooresPondingValues[20] = "" + elecStorageCapacity  * 1000 / 2;
		modifyMap.put("ElecStorage_Turbine_Cap", elecStorageCapacity  * 1000 / 2);
		
		pumpCapHM.add("" + elecStorageCapacity  * 1000 / 2);
		list.add(pumpCapHM);
		//cooresPondingValues[21] = "" + elecStorageCapacity  * 1000 / 2;
		modifyMap.put("ElecStorage_Pump_Cap", elecStorageCapacity  * 1000 / 2);
		
		elStorageCapHM.add("" + elecStorageCapacity);
		list.add(elStorageCapHM);
		//cooresPondingValues[22] = "" + elecStorageCapacity;
		modifyMap.put("ElecStorage_Storage_Cap", elecStorageCapacity);
		
		//wave power ORC
		wavePowerHM.add(""+wavePower);
		list.add(wavePowerHM);
		modifyMap.put("WavePower_Cap", wavePower);
		
		//h2 transport /feed stock)
		h2FeedStockHM.add(""+H2FeedStock);
		list.add(h2FeedStockHM);
		
		modifyMap.put("H2FeedStock", H2FeedStock);
		double feedStockH2electrolyzerCapacity = (int) Math.floor(maxFeedStockH2DemandInDistribution
				* H2FeedStock * Math.pow(10, 6)
				/ (efficiencyElectrolyzer * sumFeedStockH2DemandInDistribution))+1;
		feedStockH2ElectrolyzerHM.add(""+feedStockH2electrolyzerCapacity);			
		list.add(feedStockH2ElectrolyzerHM);
		modifyMap.put("FeedStockElectrolyzer_Cap", feedStockH2electrolyzerCapacity);
		
		modifyMap.put("FeedStockH2(imp)", (totalH2FeedStock - H2FeedStock));
		
		
		
		String line;
		/*while ((line = mainInputbr.readLine()) != null) {
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

		}*/
		
		while ((line = mainInputbr.readLine()) != null) {
			line.trim();
			boolean trackBreak=false;
			for (int i=0;i<list.size();i++) {
				if (line.startsWith(list.get(i).get(0)) || line.endsWith(list.get(i).get(0))) {
					modifiedInputbw.write(line+"\n" );
					line = mainInputbr.readLine();
					line=line.replace(line, list.get(i).get(1));
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
		
		return modifyMap;
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

	 ArrayList<String> solutonToString(Solution s) {
		ArrayList<String> str = new ArrayList();
		for(int i=0;i<numberOfVariables_;i++) {
			try {
				str.add(s.getDecisionVariables()[i].getValue()+"");
			} catch (JMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return str;
	}
	 
	 public HashMap<ArrayList<String>, MultiMap> getTheList(){
		 return hmForAllSolutions;
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
			
			
}
