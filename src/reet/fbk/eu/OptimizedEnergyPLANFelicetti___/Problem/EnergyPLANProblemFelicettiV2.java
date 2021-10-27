package reet.fbk.eu.OptimizedEnergyPLANFelicetti.Problem;

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

import reet.fbk.eu.OptimizeEnergyPLANTrentoProvince.parseFile.EnergyPLANFileParseForTrentoProvince;
import reet.fbk.eu.OptimizeEnergyPLANTrentoProvince.util.ExtractEnergyPLANParametersTrentoProvinceV10;
import reet.fbk.eu.OptimizeEnergyPLANVdN.parseFile.EnergyPLANFileParseForVdN;
import reet.fbk.eu.OptimizedEnergyPLANFelicetti.parseFile.EnergyPLANFileParseForFelicetti;
import reet.fbk.eu.OptimizedEnergyPLANFelicetti.util.ExtractEnergyPLANParametersFelicetti;

/*
 * This is a problem file that is dealing with VdN (mainly introduction of electric cars)
 */
public class EnergyPLANProblemFelicettiV2 extends Problem {


	

	HashMap <ArrayList<String>, MultiMap > hmForAllSolutions;
	
	ExtractEnergyPLANParametersFelicetti ex;
	
	
	MultiMap energyplanmMap;
	String simulatedYear, folder;
	

	
	//public static final int averageKMPerYearPerCar = 12900;
	//public static final double maxH2DemandInDistribution  = 0.000209257;
	//public static final double sumH2DemandInDistribution = 1.0; 
	
	
	
	//transport
	//public long totalKMRunByCars ;
	
	
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
	
	//number of heating element
	int numberOfHeatingTechnologies = 4;
	double upperBoundsHeat[];
	double lowerBoundsHeat[];
	
	
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
		  
	public EnergyPLANProblemFelicettiV2(String solutionType, String simulatedYear,
			String folder) {
		
		this.simulatedYear=simulatedYear;
		this.folder = folder;		
		
		numberOfVariables_ = 10;
		numberOfObjectives_ = 2;
		numberOfConstraints_ = 0;

		problemName_ = "EnergyPLANProblemFelicettiV2";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		// Establishes upper and lower limits for the variables
		int var;

		// decision variables
		// index - 0 -> PV Capacity
		
		// index - 1 -> Biomass micro chp heat PRODUCTION
		// index - 2 -> nGas micro chp heat production
		// index - 3 -> nGas boiler heat production
		// index - 4 -> Biomass boiler heat production
		
		// index - 5 -> solar thermal percentage in biomass CHP
		//index    6 -> solar thermal percentage in bnGas CHP
		// index - 7 -> solar thermal percentage in nGas boiler
		// index - 8 -> solar thermal percentage in biomass boiler
		
		//index -9 -> electrical storage capacity
		
		if(simulatedYear.equals("2018")) {
			// PV upper and lower limit		
			lowerLimit_[0] = 0.0;
			upperLimit_[0] = 1617.00;
			
			
			lowerBoundsHeat= new double [] {0.000, 0.000, 0.000, 0.000};
			upperBoundsHeat= new double [] {6.690, 6.690, 6.690, 6.690};	

		
			// electrical storage capacity
			lowerBoundsElecStorage= new double [] { 0.000};
			upperBoundsElecStorage= new double [] { 6.468 };	
	
			
			
		}		
		// other are the percentage from limit [0,1]
		for (var = 1; var <=4; var++) {
			lowerLimit_[var] = lowerBoundsHeat[var-1];
			upperLimit_[var] = upperBoundsHeat[var-1];
		} // for
		
	
		//solr thermal
		for (int i = 5; i <=8; i++) {
			lowerLimit_[i] = 0.0;
			upperLimit_[i] = 1.0;
		}
		
		lowerLimit_[9] = lowerBoundsElecStorage[0];
		upperLimit_[9] = upperBoundsElecStorage[0];
		
		
		
		
			if (solutionType.compareTo("Real") == 0)
			solutionType_ = new RealSolutionType(this);
		else {
			System.out.println("Error: solution type " + solutionType
					+ " invalid");
			System.exit(-1);
		}
	
		 if(this.simulatedYear.equals("2018")){
			
			// Heat
			 totalHeatDemand = 6.690; // in GWh
			 
			
			 efficiencynGasCHPThermal = 0.45;
			 efficiencyBiomassCHPThermal = 0.64;


			 nGasBoilerEfficiency = 0.94;
			 biomassBoilerEfficiency = 0.75;
		
		
			 // 
			 elecGridDistributionCost =  132.92;

		
		 }
			 	
			ex = new ExtractEnergyPLANParametersTrentoProvinceV10(simulatedYear);
			
			
			hmForAllSolutions = new HashMap();
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
		
		// index - 1 -> Biomass micro chp heat PRODUCTION
		// index - 2 -> nGas micro chp heat production
		// index - 3 -> nGas boiler heat production
		// index - 4 -> Biomass boiler heat production
		
		double biomassCHPHeatDemand = solution.getDecisionVariables()[1].getValue();
		double nGasCHPHeatDemand = solution.getDecisionVariables()[2].getValue();
		double nGasBoilerHeatDemand = solution.getDecisionVariables()[3].getValue();
		double biomassBoilerHeatDemand = solution.getDecisionVariables()[4].getValue();
				
		

		// index - 5 -> solar thermal percentage in biomass CHP
		//index    6 -> solar thermal percentage in bnGas CHP
		// index - 7 -> solar thermal percentage in nGas boiler
		// index - 8 -> solar thermal percentage in biomass boiler

		
		double biomassCHPSolarPercentage = solution.getDecisionVariables()[5]
				.getValue();
		double nGasCHPSolarPercentage = solution.getDecisionVariables()[6]
				.getValue();
		double nGasBoilerSolarPercentage = solution.getDecisionVariables()[7]
				.getValue();
		double biomassBoilerSolarPercentage = solution.getDecisionVariables()[8]
				.getValue();
		
		
		//electrical storage
		double elecStorageCapacity = solution.getDecisionVariables()[9]
				.getValue();
		
		
		try{
		 modifyMap = writeIntoInputFile(pv, 
				nGasBoilerHeatDemand, nGasCHPHeatDemand, 
				biomassBoilerHeatDemand, biomassCHPHeatDemand, 
				nGasBoilerSolarPercentage, biomassBoilerSolarPercentage,
				nGasCHPSolarPercentage, biomassCHPSolarPercentage,  elecStorageCapacity );
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
		
		EnergyPLANFileParseForFelicetti epfp = new EnergyPLANFileParseForFelicetti(
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
		
		if(simulatedYear.equals("2018")) {
			
			co2InImportedEleCoal =  ( (elecImport/0.44 *13.26/100) * 0.341 );
			co2InImportedEleOil  =( (elecImport/0.44 *3.46/100) * 0.267 );
			 co2InImportedEleNGas =( (elecImport/0.44 *38.82/100) * 0.202);
		
		}
		
		localCO2emission = localCO2emission  + co2InImportedEleOil + co2InImportedEleNGas + co2InImportedEleCoal;
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
		double additionalVariableCost = elecImport*elecGridDistributionCost;
		
			
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
		energyplanmMap.put("NGasBoilerSolarUtilization", NGasBoilerUtilization);
		

		col = (Collection<String>) energyplanmMap.get("AnnualBiomassBoilerheat");
		it = col.iterator();
		double AnnualBiomassBoilerheat = Double.parseDouble(it.next().toString());
		col = (Collection<String>) energyplanmMap.get("BiomassBoilerSolarInput");
		it = col.iterator();
		double BiomassBoilerSolarInput = Double.parseDouble(it.next().toString());
		double biomassBoilerSolarUtilization = ex.solarUtilizationCalculation(AnnualBiomassBoilerheat, BiomassBoilerSolarInput);
		energyplanmMap.put("biomassBoilerSolarUtilization", biomassBoilerSolarUtilization);
		
		col = (Collection<String>) energyplanmMap.get("AnnualNgasmCHPheat");
		it = col.iterator();
		double AnnualNgasmCHPheat = Double.parseDouble(it.next().toString());
		col = (Collection<String>) energyplanmMap.get("NGasCHPSolarInput");
		it = col.iterator();
		double nGasCHPSolarInput = Double.parseDouble(it.next().toString());
		double nGasCHPSolarUtilization = ex.solarUtilizationCalculation(AnnualNgasmCHPheat, nGasCHPSolarInput);
		energyplanmMap.put("NgasCHPSolarUtilization", nGasCHPSolarUtilization);
		
		col = (Collection<String>) energyplanmMap.get("AnnualBiomassmCHPheat");
		it = col.iterator();
		double AnnualBiomassmCHPheat = Double.parseDouble(it.next().toString());
		col = (Collection<String>) energyplanmMap.get("BiomassCHPSolarInput");
		it = col.iterator();
		double BiomassCHPSolarInput = Double.parseDouble(it.next().toString());
		double biomassCHPSolarUtilization = ex.solarUtilizationCalculation(AnnualBiomassmCHPheat, BiomassCHPSolarInput);
		energyplanmMap.put("biomassCHPSolarUtilization", biomassCHPSolarUtilization);
		
		
		
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
			double nGasBoilerHeatDemand, double nGasCHPHeatDemand, 
			double biomassBoilerHeatDemand,
			double biomassCHPHeatDemand, 
			double nGasBoilerSolarPercentage, double biomassBoilerSolarPercentage,
			double nGasCHPSolarPercentage, double biomassCHPSolarPercentage, 
			double elecStorageCapacity)
			throws IOException {
		
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		
		
		
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

		
	MultiMap modifyMap = new MultiValueMap();
		
		modifyMap.put("AnnualNGasBoilerheat",
				Math.round(nGasBoilerHeatDemand * 100.0) / 100.0);
		modifyMap.put("AnnualBiomassBoilerheat",
				Math.round(biomassBoilerHeatDemand * 100.0) / 100.0);
		modifyMap.put("AnnualNgasmCHPheat",
				Math.round(nGasCHPHeatDemand * 100.0) / 100.0);
		modifyMap.put("AnnualBiomassmCHPheat",
				Math.round(biomassCHPHeatDemand * 100.0) / 100.0);


		modifyMap.put("PV_Cap", (int) Math.round(pv));

		// decision variables
		// index - 0 -> PV Capacity
		
		// index - 1 -> Biomass micro chp heat PRODUCTION
		// index - 2 -> nGas micro chp heat production
		// index - 3 -> nGas boiler heat production
		// index - 4 -> Biomass boiler heat production
		
		// index - 5 -> solar thermal percentage in biomass CHP
		//index    6 -> solar thermal percentage in nGas CHP
		// index - 7 -> solar thermal percentage in nGas boiler
		// index - 8 -> solar thermal percentage in biomass boiler
		
		//index -9 -> electrical storage capacity

		
		ArrayList<String> pvHM = new ArrayList<String>(Arrays.asList("input_RES1_capacity="));
		ArrayList<String> nGasBoilerHM = new ArrayList<String>(Arrays.asList("input_fuel_Households[3]="));
		ArrayList<String> nGasBoilerSolarHM = new ArrayList<String>(Arrays.asList("input_HH_ngasboiler_Solar="));
		ArrayList<String> nGasmCHPHM = new ArrayList<String>(Arrays.asList("input_HH_NgasCHP_heat="));
		ArrayList<String> nGasmCHPSolarHM = new ArrayList<String>(Arrays.asList("input_HH_NgasCHP_solar="));
		ArrayList<String> biomassBoilerHM = new ArrayList<String>(Arrays.asList("input_fuel_Households[4]="));
		ArrayList<String> biomassBoilerSolarHM = new ArrayList<String>(Arrays.asList("input_HH_bioboiler_Solar="));
		ArrayList<String> biomassmCHPHM = new ArrayList<String>(Arrays.asList("input_HH_BioCHP_heat="));
		ArrayList<String> biomassmCHPSolarHM = new ArrayList<String>(Arrays.asList("input_HH_BioCHP_solar="));
			
		//electrical storage
		ArrayList<String> pumpCapHM = new ArrayList<String>(Arrays.asList("input_cap_pump_el="));
		ArrayList<String> turCapHM = new ArrayList<String>(Arrays.asList("input_cap_turbine_el"));
		ArrayList<String> elStorageCapHM = new ArrayList<String>(Arrays.asList("input_storage_pump_cap="));
		
	
		File modifiedInput = new File("modifiedInput.txt");
		if (modifiedInput.exists()) {
			modifiedInput.delete();
		}
		modifiedInput.createNewFile();

		FileWriter fw = new FileWriter(modifiedInput.getAbsoluteFile());
		BufferedWriter modifiedInputbw = new BufferedWriter(fw);
		
		String path=""; 
		if(simulatedYear.equals("2018") ){
			path =".\\EnergyPLAN_12.3\\energyPlan Data\\Data\\Felicetti_baseline2018_IH_v2.txt"; 
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
		
		//cooresPondingValues[6] = "" + nGasCHPSolarThermal;
		totalSolarThermalInput+=nGasCHPSolarThermal;
		modifyMap.put("NGasCHPSolarInput", nGasCHPSolarThermal);
		
		double biomassBoilerFuelDemand = biomassBoilerHeatDemand / biomassBoilerEfficiency;
		biomassBoilerHM.add("" + biomassBoilerFuelDemand);
		list.add(biomassBoilerHM);
		//cooresPondingValues[7] = "" + biomassBoilerFuelDemand;
		modifyMap.put("biomassBoilerFuelDemand", biomassBoilerFuelDemand);

		
		double biomassBoilerSolarThermal = biomassBoilerFuelDemand * biomassBoilerSolarPercentage;
		biomassBoilerSolarHM.add("" + biomassBoilerSolarThermal);
		list.add(biomassBoilerSolarHM);
		
		//cooresPondingValues[8] = "" + biomassBoilerSolarThermal;
		totalSolarThermalInput+=biomassBoilerSolarThermal;
		modifyMap.put("BiomassBoilerSolarInput", biomassBoilerSolarThermal);


		biomassmCHPHM.add("" + biomassCHPHeatDemand);
		list.add(biomassmCHPHM);
		
		//cooresPondingValues[9] = "" + biomassCHPHeatDemand;

		double biomassCHPSolarThermal = biomassCHPHeatDemand * efficiencyBiomassCHPThermal *
				 biomassCHPSolarPercentage;
		biomassmCHPSolarHM.add("" + biomassCHPSolarThermal);
		list.add(biomassmCHPSolarHM);
		
		//cooresPondingValues[10] = "" + biomassCHPSolarThermal;
		totalSolarThermalInput+=biomassCHPSolarThermal;
		modifyMap.put("BiomassCHPSolarInput", biomassCHPSolarThermal);

				
		modifyMap.put("SolarThermal", totalSolarThermalInput);
		

				
				
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
	
}
