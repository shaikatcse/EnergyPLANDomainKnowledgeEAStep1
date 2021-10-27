package reet.fbk.eu.OptimizeEnergyPLANVdN.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import jmetal.core.Solution;
import jmetal.util.JMException;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import reet.fbk.eu.OptimizeEnergyPLANCIVIS.ParseFile.EnergyPLANFileParseForCivis;
import reet.fbk.eu.OptimizeEnergyPLANVdN.parseFile.EnergyPLANFileParseForVdN;

public class ExtractEnergyPLANParametersVdN4DWithTransport {

	// static MultiMap energyplanmMap;

	static String inputs[] = { "PV_Cap", "Biomass_CHP_Cap", "HP_Cap",
			"Oil_boiler_Cap", "NGas_boiler_Cap", "Biomass_boiler_Cap", "SolarThermal", "NumberOfConCars", "NumberOfEVCars","NumberOfFCEVCars", "Eloctrolyzer_Cap"  };
	
	static String outputsinEnergyPLANMap[] = { "AnnualPVElectr.", "AnnualHH-CHPElectr.",
			"AnnualHH-HPElectr.", "AnnualFlexibleElectr.", "AnnualH2demand", "AnnualOilBoilerheat", "AnnualNGasBoilerheat",
			"AnnualBiomassBoilerheat", "AnnualmCHPheat", "AnnualHPheat", "AnnualHH SolarHeat",
			"AnnualImportElectr.", "AnnualExportElectr.", "Oil Consumption",
			"Biomass Consumption", "Ngas Consumption", 
			"Gasoil/Diesel",
			"Biomass", "Electricity exchange",
			"Variable costs", "Fixed operation costs", "AdditionalCost",
			//"ConCarInvestmentCost","EVCarInvestmentCost","FCEVCarInvestmentCost",
			"Annual Investment costs", "CO2-emission (corrected)", "ToatlSystemEmission", "AnnualCost",
			"LoadFollowingCapacity", "ESD", 
			"oilBoilerFuelDemand", "ngasBoilerFuelDemand", "biomassBoilerFuelDemand", "AnnualmCHPheat", "AnnualHPheat", "AnnualHH SolarHeat", 
			"transOilDemand", 
			"OilSolarInput", "NGasSolarInput", "BiomassBoilerSolarInput", "BiomassCHPSolarInput", "HPSolarInput",
			"OilSolarUtilization",	"nGasSolarUtilization", "biomassBoilerSolarUtilization", "biomassCHPSolarUtilization", "HPSolarUtilization",
			"Annual MaximumImportElectr.", "Annual MaximumExportElectr."};

	static String outputsinFile[] = { "AnnualPV", "AnnualCHPelec",
			"AnnualHPelec", "AnnualElecCar", "AnnualH2demand", "AnnualOilBoilerheat", "AnnualNGasBoilerheat",
			"AnnualBiomassBoilerheat", "AnnualmCHPheat", "AnnualHPheat", "AnnualSolarThermalheat", 
			"AnnualImport", "AnnualExport", "OilConsumption",
			"BiomassConsumption", "NGasConsuption", 
			"DieselCost", 
			"BiomassCost", "TotalElectricityExchangeCost", 
			"TotalVariableCost", "FixedOperationCosts", "AdditionalCost", 
			//"ConCarInvestmentCost","EVCarInvestmentCost","FCEVInvestmentCost", 
			"InvestmentCost","CO2-Emission", "Total Local System Emission", "AnnualCost", 
			"LoadFollowingCapacity", "ESD",
			"oilBoilerFuelDemand", "NGasBoilerFuelDemand", "biomassBoilerFuelDemand", "AnnualmCHPheat", "AnnualHPheat", "AnnualSolarThermal", 
			"transOilDemand",
			"OilSolarInput", "NGasSolarInput", "BiomassBoilerSolarInput", "BiomassCHPSolarInput", "HPSolarInput",
			"OilSolarUtilization",	"nGasSolarUtilization", "biomassBoilerSolarUtilization", "biomassCHPSolarUtilization", "HPSolarUtilization",
			"Annual MaximumImportElectr.", "Annual MaximumExportElectr."
	};

	static String inputUnits[] = { "KWe", "KWe", "KWe", "KWth", "KWth", "KWth", "GWh", "1000 units", "1000 units", "1000 units", "KWe" };
	static String outputUnits[] = { "GWh", "GWh", // "AnnualPV", "AnnualCHPelec",
			"GWh", "GWh", "GWh", "GWh", "GWh", //"AnnualHPelec", "AnnualElecCar", "AnnualH2demand", "AnnualOilBoilerheat", "AnnualNGasBoilerheat",
			"GWh", "GWh", "GWh", "GWh", //"AnnualBiomassBoilerheat", "AnnualmCHPheat", "AnnualHPheat", "AnnualSolarThermalheat", 
			"GWh", "GWh", "GWh",  //"AnnualImport", "AnnualExport", "OilConsumption",
			"GWh", "GWh",  //"BiomassConsumption", "NGasConsuption", 
			"KEuro",  //"DieselCost", 
			"KEuro", "KEuro", //"BiomassCost", "TotalElectricityExchangeCost",
			"KEuro", "KEuro", "KEuro", //"TotalVariableCost", "FixedOperationCosts", "AdditionalCost", 
			//"KEuro", "KEuro", "KEuro", //"ConCarInvestmentCost","EVCarInvestmentCost","FCEVInvestmentCost", 
			"KEuro", "Kt","kt", "KEuro", //"InvestmentCost","CO2-Emission", "Total Local System Emission", "AnnualCost", 
			"", "", //"LoadFollowingCapacity", "ESD",
			"GWh","GWh","GWh","GWh","GWh","GWh", //"oilBoilerFuelDemand", "NGasBoilerFuelDemand", "biomassBoilerFuelDemand", "AnnualmCHPheat", "AnnualHPheat", "AnnualSolarThermal", 
			"GWh",   //"transOilDemand"
			"GWh","GWh","GWh","GWh","GWh", //"OilSolarInput", "NGasSolarInput", "BiomassBoilerSolarInput", "BiomassCHPSolarInput", "HPSolarInput" 
			"GWh","GWh","GWh", "GWh","GWh", //"OilSolarUtilization",	"nGasSolarUtilization", "biomassBoilerSolarUtilization", "biomassCHPSolarUtilization", "HPSolarUtilization"
			"KW", "KW"
	};

	/*
	 * VdN data
	 */
	public static final int averageKMPerYearPerCar = 12900;
	public static final int totalKMRunByCars = 250000000;
	public static final double maxH2DemandInDistribution = 1.0;
	public static final double sumH2DemandInDistribution = 2928.0; 
	// Heat
	public static final double totalHeatDemand = 243.97; // in GWh
	public static final double maxOfAllHeatDistribution = 0.000221902;
	public static final double sumOfAllHeatDistributions=1.0;

	//CO2 content related data
	public static final double co2Coal=95.0; //unit: kg/GJ
	public static final double co2Oil=74.0;
	public static final double co2NGas = 56.7;

	
	/*
	//2020 scenario

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
	
	// additional cost
	public static final double addtionalCostPerGWhinKEuro = 131.0;*/
	
	
	/*//2030 scenario
	
	//cars
	public static final double efficiencyConCar = 0.549; // KWh/km
	public static final double efficiencyEVCar = 0.145; // KWh/km
	public static final double efficiencyFCEVCar = 0.267; // KWh/km

	public static final double efficiencyBiomassCHP = 0.66;
	
	// H2
	public static final double efficiencyElectrolyzerTrans = 0.75;
	
	public static final double oilBoilerEfficiency = 0.90;
	public static final double nGasBoilerEfficiency = 1.0;
	public static final double biomassBoilerEfficiency = 0.85;
	public static final double COP = 5.07;
	
	public static final double coalShare=10.4;
	public static final double oilShare=4.1;
	public static final double nGasShare=33.4;
	
	// additional cost
	public static final double addtionalCostPerGWhinKEuro = 144.0;*/
	
	//2050 scenario
	
	//cars
	public static final double efficiencyConCar = 0.497; // KWh/km
	public static final double efficiencyEVCar = 0.117; // KWh/km
	public static final double efficiencyFCEVCar = 0.200; // KWh/km

	public static final double efficiencyBiomassCHP = 0.66;
	
	// H2
	public static final double efficiencyElectrolyzerTrans = 0.78;
	
	public static final double oilBoilerEfficiency = 0.95;
	public static final double nGasBoilerEfficiency = 1.0;
	public static final double biomassBoilerEfficiency = 0.90;
	public static final double COP = 5.46;
	
	public static final double coalShare=3.3;
	public static final double oilShare=1.3;
	public static final double nGasShare=10.5;
	
	// additional cost
	public static final double addtionalCostPerGWhinKEuro = 176.0;

	public ExtractEnergyPLANParametersVdN4DWithTransport() {
		// TODO Auto-generated constructor stub

	}

	public static void main(String[] args) throws IOException, JMException {

		MultiMap energyplanmMap = null;
		// TODO Auto-generated method stub
		ExtractEnergyPLANParametersVdN4DWithTransport ob = new ExtractEnergyPLANParametersVdN4DWithTransport();
		FileInputStream fos = new FileInputStream(args[0]);
		InputStreamReader isr = new InputStreamReader(fos);
		BufferedReader br = new BufferedReader(isr);

		ob.readProfiles();
		
		String line;
		while ((line = br.readLine()) != null) {
			energyplanmMap = ob.simulateEnergyPLAN(line);
			ob.WriteEnergyPLANParametersToFile(energyplanmMap, args[0]);
		}

		br.close();
	}

	void WriteEnergyPLANParametersToFile(MultiMap energyPLANMap, String path)
			throws IOException {
		Iterator it;
		Collection<String> col;

		File filetmp = new File(path);
		String arrayStr[] = path.split("\\\\");

		File file = new File(filetmp.getParent() + "\\allParameters_"
				+ arrayStr[arrayStr.length - 1] + ".txt");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();

			// create header of the file
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			/*
			 * bw.write(
			 * "PV_C HP_Heat_P OilBoilerHeat NgasBoilerHeat BiomassBoilerHeat "
			 * +
			 * "AnlPV_P AnlImport AnlExport AnlOilBoilerFuel AnlNgasBoilerFuel "
			 * +
			 * "AnlBiomassBoilerFuel AnlOilCost AnlLPGCost AnlBiomassCost AnlElecExchage TotalVariableCost FixedOperationalCost "
			 * +
			 * "AdditionalCost InvestmentCost CO2Emission AnnualCost LoadFollowingCapacity"
			 * );
			 */

			String headings = "";
			for (int i = 0; i < inputs.length; i++) {
				headings += inputs[i] + ";";
			}
			for (int i = 0; i < outputsinFile.length; i++) {
				headings += outputsinFile[i] + ";";
			}
			bw.write(headings);

			bw.newLine();

			String units = "";
			for (int i = 0; i < inputUnits.length; i++) {
				units += inputUnits[i] + ";";
			}
			for (int i = 0; i < outputUnits.length; i++) {
				units += outputUnits[i] + ";";
			}
			bw.write(units);

			/*
			 * bw.write(
			 * "(KW) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) " +
			 * "(GWh) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) "
			 * + "(Mt) (KEuro)");
			 */

			bw.newLine();
			bw.close();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);

		String input = "", output = "";
		for (int i = 0; i < inputs.length; i++) {
			col = (Collection<String>) energyPLANMap.get(inputs[i]);
			it = col.iterator();
			input = input + it.next() + ";";
		}
		
		for (int i = 0; i < outputsinEnergyPLANMap.length; i++) {
			try{
				col = (Collection<String>) energyPLANMap
			
					.get(outputsinEnergyPLANMap[i]);
			it = col.iterator();
			String str = it.next().toString();
			if (str.endsWith("1000")) {
				str = str.substring(0, str.lastIndexOf("1000"));
				str = Double.parseDouble(str) + "";
			}
			output = output + str + ";";
		}
			catch(NullPointerException e){
			System.out.println(i);
		}
		}
		bw.write(input + output);
		bw.newLine();
		bw.close();

	}

	MultiMap simulateEnergyPLAN(String individual) throws JMException {

		StringTokenizer st = new StringTokenizer(individual);
		// PV
		// PV
		double pv = Double.parseDouble(st.nextToken().toString());

		// decision variables
				// index - 0 -> PV Capacity
				// index - 1 -> oil boiler heat percentage
				// index - 2 -> nGas boiler heat percentage
				// index - 3 -> Biomass boiler heat percentage
				// index - 4 -> Biomass micro chp heat percentage
				// last percentage will go to individual HP percentage
				// index - 5 -> electric car percentage
				// index - 6 -> FCEV car percentage

				// index -7 -> solar thermal percentage in oil boiler
				// index -8 -> solar thermal percentage in nGas boiler
				// index -9 -> solar thermal percentage in biomass boiler
				// index -10 -> solar thermal percentage in biomass CHP
				// index -11 -> solar thermal percentage in HP

				double heatPercentages[] = new double[4];
				for (int i = 0; i < 4; i++) {
					heatPercentages[i] = Double.parseDouble(st.nextToken().toString());
				}

				Arrays.sort(heatPercentages);

				
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

				// car percentages
				double carPercentages[] = new double[2];
				for (int i = 5; i < 7; i++) {
					carPercentages[i - 5] = Double.parseDouble(st.nextToken().toString());
				}

				Arrays.sort(carPercentages);

				
				// EV percentage of run
				double EVCarPercentage = carPercentages[0];

				// FCEV car percentage of run
				double FCEVCarPercentage = carPercentages[1] - carPercentages[0];

				// conventional car percentage of run
				double conCarPercentage = 1 - carPercentages[1];

				double totalKMRunByConCar = (int) totalKMRunByCars * conCarPercentage;
				double totalKMRunByEVCar = (int) totalKMRunByCars * EVCarPercentage;
				double totalKMRunByFCEVCar = (int) totalKMRunByCars * FCEVCarPercentage;

				double totalDieselDemandInGWhForTrns = totalKMRunByConCar
						* efficiencyConCar / Math.pow(10, 6);
				double totalElecDemandInGWhForTrns = totalKMRunByEVCar
						* efficiencyEVCar / Math.pow(10, 6);
				double totalH2DemandInGWhForTrns = totalKMRunByFCEVCar
						* efficiencyFCEVCar / Math.pow(10, 6);

				// solar thermal percentage
				double oilSolarPercentage = Double.parseDouble(st.nextToken().toString());
				double nGasSolarPercentage = Double.parseDouble(st.nextToken().toString());
				double biomassBoilerSolarPercentage = Double.parseDouble(st.nextToken().toString());
				double biomassCHPSolarPercentage = Double.parseDouble(st.nextToken().toString());
				double hpSolarPercentage = Double.parseDouble(st.nextToken().toString());
				
				
				
		MultiMap energyplanMap = null;
		MultiMap modifyMap = new MultiValueMap();
		try{
			modifyMap = writeIntoInputFile(pv, oilBoilerHeatPercentage,
					nGasBoilerHeatPercentage, biomassBoilerHeatPercentage,
					biomassCHPHeatPercentage, hpHeatPercentage,
					totalDieselDemandInGWhForTrns, totalElecDemandInGWhForTrns,
					totalH2DemandInGWhForTrns, oilSolarPercentage,
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
			energyplanMap = epfp.parseFile();
			energyplanMap.putAll(modifyMap);

			Iterator it;
			Collection<String> col;

			// objective # 1
			col = (Collection<String>) energyplanMap
					.get("CO2-emission (total)");
			it = col.iterator();
			double localCO2emission =  Double.parseDouble(it.next().toString());
			
			//extract import
			col = (Collection<String>) energyplanMap
					.get("AnnualImportElectr.");
			it = col.iterator();
			double elecImport = Double.parseDouble(it.next().toString());
			
			double co2InImportedEleCoal =( (elecImport*coalShare/100.0) * co2Coal *3600.0 )/ 1000000.0; //1GWh = 3600 GJ
			double co2InImportedEleOil =( (elecImport*oilShare/100.0) * co2Oil *3600.0 )/ 1000000.0; //1GWh = 3600 GJ
			double co2InImportedEleNGas =( (elecImport*nGasShare/100.0) * co2NGas *3600.0 )/ 1000000.0; //1GWh = 3600 GJ
			
			localCO2emission = localCO2emission + co2InImportedEleCoal + co2InImportedEleOil + co2InImportedEleNGas;
			energyplanMap.put("ToatlSystemEmission", localCO2emission);
			
			// objective # 2
			col = (Collection<String>) energyplanMap.get("Variable costs");
			it = col.iterator();
			String totalVariableCostStr = it.next().toString();
			double totalVariableCost = Double.parseDouble(totalVariableCostStr);

			col = (Collection<String>) energyplanMap
					.get("Fixed operation costs");
			it = col.iterator();
			String fixedOperationalCostStr = it.next().toString();
			double fixedOperationalCost = Double
					.parseDouble(fixedOperationalCostStr);

			col = (Collection<String>) energyplanMap.get("AnnualHydroElectr.");
			it = col.iterator();
			double hydroPowerProduction = Double.parseDouble(it.next()
					.toString());
			// extract anual PV production
			col = (Collection<String>) energyplanMap.get("AnnualPVElectr.");
			it = col.iterator();
			double PVproduction = Double.parseDouble(it.next().toString());

			// extract annual import
			col = (Collection<String>) energyplanMap
					.get("AnnualImportElectr.");
			it = col.iterator();
			double Import = Double.parseDouble(it.next().toString());

			// extract annual export
			col = (Collection<String>) energyplanMap
					.get("AnnualExportElectr.");
			it = col.iterator();
			double Export = Double.parseDouble(it.next().toString());

			// extract biomass CHP electricity production
			col = (Collection<String>) energyplanMap
					.get("AnnualHH-CHPElectr.");
			it = col.iterator();
			double biomassCHPElecProduction = Double.parseDouble(it.next()
					.toString());

			// calculate additional cost
			// (hydroProduction+PVproduction+Import-Export)*average additional
			// cost (85.74)
			double totalAdditionalCost = Math
					.round((hydroPowerProduction + PVproduction + Import
							- Export + biomassCHPElecProduction)
							* addtionalCostPerGWhinKEuro);
			
			col = (Collection<String>) energyplanMap
					.get("Annual Investment costs");
			it = col.iterator();
			String invest = it.next().toString();
			double investmentCost = Double.parseDouble(invest);

			double actualAnnualCost = totalVariableCost + fixedOperationalCost
					+ investmentCost + totalAdditionalCost;
					
			energyplanMap.put("AdditionalCost", totalAdditionalCost);
			energyplanMap.put("AnnualCost", actualAnnualCost);
			
			// Trasportation
			col = (Collection<String>) energyplanMap
					.get("AnnualFlexibleElectr.");
			it = col.iterator();
			double transportElecDemand = Double.parseDouble(it.next()
					.toString());

			col = (Collection<String>) energyplanMap
					.get("AnnualElectr.Demand");
			it = col.iterator();
			double annualElecDemand = Double.parseDouble(it.next().toString());

			// Individual house HP electric demand
			col = (Collection<String>) energyplanMap.get("AnnualHH-HPElectr.");
			it = col.iterator();
			double annualHPdemand = Double.parseDouble(it.next().toString());

			energyplanMap.put("LoadFollowingCapacity", (Import + Export)
					/ (annualElecDemand + transportElecDemand + annualHPdemand));
		
			col = (Collection<String>) energyplanMap.get("Ngas Consumption");
			it = col.iterator();
			double nGasConsumption = Double.parseDouble(it.next().toString());

			// extract oil consumption
			col = (Collection<String>) energyplanMap.get("Oil Consumption");
			it = col.iterator();
			double oilConsumption = Double.parseDouble(it.next().toString());

			// extract biomass consupmtion
			col = (Collection<String>) energyplanMap
					.get("Biomass Consumption");
			it = col.iterator();
			double BiomassConsumption = Double
					.parseDouble(it.next().toString());

			// extract solar thermal heat
			col = (Collection<String>) energyplanMap.get("AnnualHH SolarHeat");
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
			energyplanMap.put("ESD", ESD);
			
			
			col = (Collection<String>) energyplanMap.get("AnnualOilBoilerheat");
			it = col.iterator();
			double AnnualOilBoilerheat = Double.parseDouble(it.next().toString());
			col = (Collection<String>) energyplanMap.get("OilSolarInput");
			it = col.iterator();
			double OilSolarInput = Double.parseDouble(it.next().toString());
			
			double OilSolarUtilization = solarUtilizationCalculation(AnnualOilBoilerheat, OilSolarInput);
			energyplanMap.put("OilSolarUtilization", OilSolarUtilization);
			
			
			col = (Collection<String>) energyplanMap.get("AnnualNGasBoilerheat");
			it = col.iterator();
			double AnnualNGasBoilerheat = Double.parseDouble(it.next().toString());
			col = (Collection<String>) energyplanMap.get("NGasSolarInput");
			it = col.iterator();
			double NGasSolarInput = Double.parseDouble(it.next().toString());
			double nGasSolarUtilization = solarUtilizationCalculation(AnnualNGasBoilerheat, NGasSolarInput);
			energyplanMap.put("nGasSolarUtilization", nGasSolarUtilization);
			
	
			col = (Collection<String>) energyplanMap.get("AnnualBiomassBoilerheat");
			it = col.iterator();
			double AnnualBiomassBoilerheat = Double.parseDouble(it.next().toString());
			col = (Collection<String>) energyplanMap.get("BiomassBoilerSolarInput");
			it = col.iterator();
			double BiomassBoilerSolarInput = Double.parseDouble(it.next().toString());
			double biomassBoilerSolarUtilization = solarUtilizationCalculation(AnnualBiomassBoilerheat, BiomassBoilerSolarInput);
			energyplanMap.put("biomassBoilerSolarUtilization", biomassBoilerSolarUtilization);
			
			col = (Collection<String>) energyplanMap.get("AnnualmCHPheat");
			it = col.iterator();
			double AnnualmCHPheat = Double.parseDouble(it.next().toString());
			col = (Collection<String>) energyplanMap.get("BiomassCHPSolarInput");
			it = col.iterator();
			double BiomassCHPSolarInput = Double.parseDouble(it.next().toString());
			double biomassCHPSolarUtilization = solarUtilizationCalculation(AnnualmCHPheat, BiomassCHPSolarInput);
			energyplanMap.put("biomassCHPSolarUtilization", biomassCHPSolarUtilization);
			
			
			col = (Collection<String>) energyplanMap.get("AnnualHPheat");
			it = col.iterator();
			double AnnualHPheat = Double.parseDouble(it.next().toString());
			col = (Collection<String>) energyplanMap.get("HPSolarInput");
			it = col.iterator();
			double HPSolarInput = Double.parseDouble(it.next().toString());
			double HPSolarUtilization = solarUtilizationCalculation(AnnualHPheat, HPSolarInput);
			energyplanMap.put("HPSolarUtilization", HPSolarUtilization);
			
		
		} catch (IOException e) {
			System.out.println("Energyplan.exe has some problem");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Energyplan interrupted");
		}

		return energyplanMap;

	}

	public MultiMap writeIntoInputFile(double pv, double oilBoilerHeatPercentage,
			double nGasBoilerHeatPercentage,
			double biomassBoilerHeatPercentage,
			double biomassCHPHeatPercentage, double hpHeatPercentage,
			double totalDieselDemandInGWhForTrns,
			double totalElecDemandInGWhForTrns,
			double totalH2DemandInGWhForTrns, double oilSolarPercentage,
			double nGasSolarPercentage, double biomassBoilerSolarPercentage,
			double biomassCHPSolarPercentage, double hpSolarPercentage)
			throws IOException {
		
		
		
		MultiMap modifyMap = new MultiValueMap();
		double totalOilBoilerHeat = oilBoilerHeatPercentage * totalHeatDemand;
		double totalLPGBoilerHeat = nGasBoilerHeatPercentage * totalHeatDemand;
		double totalBiomassBoilerHeat = biomassBoilerHeatPercentage
				* totalHeatDemand;
		double totalnGasCHPHeat = biomassCHPHeatPercentage * totalHeatDemand;
		double totalHPHeat = hpHeatPercentage * totalHeatDemand;
		
		modifyMap.put("AnnualOilBoilerheat",
				Math.round(totalOilBoilerHeat * 100.0) / 100.0); //this is done for 2 decimal place precision
		modifyMap.put("AnnualNGasBoilerheat",
				Math.round(totalLPGBoilerHeat * 100.0) / 100.0);
		modifyMap.put("AnnualBiomassBoilerheat",
				Math.round(totalBiomassBoilerHeat * 100.0) / 100.0);
		modifyMap.put("AnnualmCHPheat",
				Math.round(totalnGasCHPHeat * 100.0) / 100.0);
		modifyMap.put("AnnualHPheat", Math.round(totalHPHeat * 100.0) / 100.0);

		
		//special calculation for Biomass Boiler Biomass comsuption
		
		
		
		double oilBCap = maxOfAllHeatDistribution * totalOilBoilerHeat * Math.pow(10, 6)  
				/ sumOfAllHeatDistributions;
		double NGasBCap = maxOfAllHeatDistribution * totalLPGBoilerHeat * Math.pow(10, 6) 
				/ sumOfAllHeatDistributions;
		double BiomassBCap = maxOfAllHeatDistribution * totalBiomassBoilerHeat * Math.pow(10, 6) 
				/ sumOfAllHeatDistributions;
		double biomassCHPCap = maxOfAllHeatDistribution * totalnGasCHPHeat * Math.pow(10, 6)
				/ sumOfAllHeatDistributions;
		double HPCap = maxOfAllHeatDistribution * totalHPHeat * Math.pow(10, 6)
				/ (COP * sumOfAllHeatDistributions);

		modifyMap.put(inputs[0], (int) Math.round(pv));
		modifyMap.put(inputs[1], (int) Math.round(biomassCHPCap));
		modifyMap.put(inputs[2], (int) Math.round(HPCap));
		modifyMap.put(inputs[3], (int) Math.round(oilBCap));
		modifyMap.put(inputs[4], (int) Math.round(NGasBCap));
		modifyMap.put(inputs[5], (int) Math.round(BiomassBCap));
		
		

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
				"input_fuel_Transport[2]=", // Diesel demand (11)
				"Input_Size_transport_conventional_cars=", // number of
															// convensional cars
															// (12)
				"input_transport_TWh=", // EV car electricity demand (13)
				"Input_Size_transport_electric_cars=", // number of electric car
														// (14)
				"input_fuel_Transport[6]=", // hydrogen demand for Transport
											// (15)
				"Input_Size_transport_other_vehicles1=", // number of FCEV car
															// (16)
				"input_cap_ELTtrans_el=" // //corresponding eletrolyzer capacity
											// (17)
		};

		String cooresPondingValues[] = new String[18];

		File modifiedInput = new File("modifiedInput.txt");
		if (modifiedInput.exists()) {
			modifiedInput.delete();
		}
		modifiedInput.createNewFile();

		FileWriter fw = new FileWriter(modifiedInput.getAbsoluteFile());
		BufferedWriter modifiedInputbw = new BufferedWriter(fw);

		String path ="C:\\Users\\mahbub\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\EnergyPLAN_12.3\\energyPlan Data\\Data\\VdN_2050.txt";
    	BufferedReader mainInputbr = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-16"));
    	
		
    	double totalSolarThermalInput=0.0;
    	
		cooresPondingValues[0] = "" + pv;
		
		double oilBoilerFuelDemand = oilBoilerHeatPercentage * totalHeatDemand
				/ oilBoilerEfficiency;
		cooresPondingValues[1] = "" + oilBoilerFuelDemand;
		modifyMap.put("oilBoilerFuelDemand", oilBoilerFuelDemand);
		
		double oilSolarThermal = oilBoilerFuelDemand * oilSolarPercentage;
		cooresPondingValues[2] = "" + oilSolarThermal;
		totalSolarThermalInput+=oilSolarThermal;
		modifyMap.put("OilSolarInput", oilSolarThermal);
		
		double ngasBoilerFuelDemand = nGasBoilerHeatPercentage
				* totalHeatDemand / nGasBoilerEfficiency;
		cooresPondingValues[3] = "" + ngasBoilerFuelDemand;
		modifyMap.put("ngasBoilerFuelDemand", ngasBoilerFuelDemand);

		double nGasSolarThermal = ngasBoilerFuelDemand * nGasSolarPercentage;
		cooresPondingValues[4] = "" + nGasSolarThermal;
		totalSolarThermalInput+=nGasSolarThermal;
		modifyMap.put("NGasSolarInput", nGasSolarThermal);

		double biomassBoilerFuelDemand = biomassBoilerHeatPercentage
				* totalHeatDemand / biomassBoilerEfficiency;
		cooresPondingValues[5] = "" + biomassBoilerFuelDemand;
		modifyMap.put("biomassBoilerFuelDemand", biomassBoilerFuelDemand);

		double biomassBoilerSolarThermal = biomassBoilerFuelDemand
				* biomassBoilerSolarPercentage;
		cooresPondingValues[6] = "" + biomassBoilerSolarThermal;
		totalSolarThermalInput+=biomassBoilerSolarThermal;
		modifyMap.put("BiomassBoilerSolarInput", biomassBoilerSolarThermal);

		double baiomassCHPHeatDemand = biomassCHPHeatPercentage
				* totalHeatDemand;
		cooresPondingValues[7] = "" + baiomassCHPHeatDemand;

		double biomassCHPSolarThermal = baiomassCHPHeatDemand * efficiencyBiomassCHP *
				 biomassCHPSolarPercentage;
		cooresPondingValues[8] = "" + biomassCHPSolarThermal;
		totalSolarThermalInput+=biomassCHPSolarThermal;
		modifyMap.put("BiomassCHPSolarInput", biomassCHPSolarThermal);

		double hpHEatDemand = hpHeatPercentage * totalHeatDemand;
		cooresPondingValues[9] = "" + hpHEatDemand;

		double hpSolarThermal = hpHeatPercentage * totalHeatDemand
				* hpSolarPercentage;
		cooresPondingValues[10] = "" + hpSolarThermal;
		totalSolarThermalInput+=hpSolarThermal;
		modifyMap.put("HPSolarInput", hpSolarThermal);
		
		modifyMap.put("SolarThermal", totalSolarThermalInput);
		
		cooresPondingValues[11] = "" + totalDieselDemandInGWhForTrns;
		modifyMap.put("transOilDemand", totalDieselDemandInGWhForTrns);
		

		//"NumberOfConCars", "NumberOfEVCars","NumberOfFCEVCars"
		
		double numberOfConCars = (totalDieselDemandInGWhForTrns * Math.pow(10,
				6))
				/ (efficiencyConCar * averageKMPerYearPerCar * Math.pow(10, 3));
		cooresPondingValues[12] = "" + numberOfConCars;
		modifyMap.put("NumberOfConCars", numberOfConCars);

		cooresPondingValues[13] = "" + totalElecDemandInGWhForTrns;

		double numberOfEVCars = totalElecDemandInGWhForTrns * Math.pow(10, 6)
				/ (efficiencyEVCar * averageKMPerYearPerCar * Math.pow(10, 3));
		cooresPondingValues[14] = "" + numberOfEVCars;
		modifyMap.put("NumberOfEVCars", numberOfEVCars);

		cooresPondingValues[15] = "" + totalH2DemandInGWhForTrns;
		modifyMap.put("AnnualH2demand", totalH2DemandInGWhForTrns);
				 
		
		double numberOfFCEVCars = totalH2DemandInGWhForTrns
				* Math.pow(10, 6)
				/ (efficiencyFCEVCar * averageKMPerYearPerCar * Math.pow(10, 3));
		cooresPondingValues[16] = "" + numberOfFCEVCars;
		modifyMap.put("NumberOfFCEVCars", numberOfFCEVCars);
		
		int electrolyzerCapacity = (int) Math.floor(maxH2DemandInDistribution
				* totalH2DemandInGWhForTrns * Math.pow(10, 6)
				/ (efficiencyElectrolyzerTrans * sumH2DemandInDistribution))+1;
		cooresPondingValues[17] = "" + electrolyzerCapacity;
		modifyMap.put("Eloctrolyzer_Cap", electrolyzerCapacity);

		String line;
		while ((line = mainInputbr.readLine()) != null) {
			line.trim();
			boolean trackBreak=false;
			for (int i = 0; i < 18; i++) {
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
		
		return modifyMap;
	}
	
	ArrayList<Double> thermalDeamndProfile = new ArrayList();
	ArrayList<Double> solarThermalProductionProfile = new ArrayList();
	Double sumOfThermalDemandProfile;
	Double sumOfSolarThermalProductionProfile;
	
	void readProfiles() throws IOException{
		
		BufferedReader brthermalDeamndProfile = new BufferedReader(new FileReader("C:\\Users\\mahbub\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\EnergyPLAN_12.3\\energyPlan Data\\Distributions\\VdN thermal_2_forJAVA.txt"));
		BufferedReader brsolarThermalProductionProfile = new BufferedReader(new FileReader("C:\\Users\\mahbub\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\EnergyPLAN_12.3\\energyPlan Data\\Distributions\\VdN_solar_thermal_profile2_forJAVA.txt"));
		String line;
		
		sumOfThermalDemandProfile=0.0;
		while((line = brthermalDeamndProfile.readLine()) != null){
			Double temp=Double.parseDouble(line);
			sumOfThermalDemandProfile+=temp;
			thermalDeamndProfile.add(temp);
			
		}
		
		sumOfSolarThermalProductionProfile=0.0;
		while((line = brsolarThermalProductionProfile.readLine()) != null){
			Double temp1=Double.parseDouble(line);
			sumOfSolarThermalProductionProfile+=temp1;
			solarThermalProductionProfile.add(temp1);
			
		}
		System.out.println(sumOfSolarThermalProductionProfile);
		
	}
	
	double solarUtilizationCalculation(double heatDemand, double solarInput){
		double solarUtilization=0.0;
		for(int i=0;i<thermalDeamndProfile.size();i++){
			double hourSolarThermalproduction = solarInput*solarThermalProductionProfile.get(i)/sumOfSolarThermalProductionProfile;
			double hourThermalDemand = heatDemand * thermalDeamndProfile.get(i)/sumOfThermalDemandProfile;
			if(hourSolarThermalproduction>hourThermalDemand)
				solarUtilization+=hourThermalDemand;
			else
				solarUtilization+=hourSolarThermalproduction;
			
		}
		return solarUtilization;
		
	}
}
