package reet.fbk.eu.OptimizeEnergyPLANCEIS2021.util;

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
import java.util.Random;
import java.util.StringTokenizer;

import jmetal.core.Solution;
import jmetal.util.JMException;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.poi.util.SystemOutLogger;

import reet.fbk.eu.OptimizeEnergyPLANCIVIS.ParseFile.EnergyPLANFileParseForCivis;
import reet.fbk.eu.OptimizeEnergyPLANTrentoProvince.parseFile.EnergyPLANFileParseForTrentoProvince;
import reet.fbk.eu.OptimizeEnergyPLANVdN.parseFile.EnergyPLANFileParseForVdN;

public class ExtractEnergyPLANParametersCEIS2021 {

	static String inputs[] = { "PV_Cap", "Hydro_Cap", /*
														 * "Biomass_CHP_Cap", "NGas_CHP_Cap", "HP_Cap",
														 * "Oil_boiler_Cap", "NGas_boiler_Cap", "Biomass_boiler_Cap",
														 * "SolarThermal",
														 */
			"NumberOfConCars", "NumberOfEVCars", "NumberOfFCEVCars", "Eloctrolyzer_Cap", "ElecStorage_Turbine_Cap",
			"ElecStorage_Pump_Cap", "ElecStorage_Storage_Cap" };

	static String outputsinEnergyPLANMap[] = { "AnnualPVElectr.", "AnnualHH-CHPElectr.", "AnnualHH-HPElectr.",
			"AnnualFlexibleElectr.", "AnnualH2demand", "AnnualOilBoilerheat", "AnnualNGasBoilerheat",
			"AnnualBiomassBoilerheat", "AnnualNgasmCHPheat", "AnnualBiomassmCHPheat", "AnnualHPheat",
			"AnnualHH SolarHeat", "AnnualImportElectr.", "AnnualExportElectr.", "Oil Consumption",
			"Biomass Consumption", "Ngas Consumption", /*
														 * "Gasoil/Diesel", "Biomass",
														 */ "Bottleneck", "Electricity exchange", "Variable costs",
			"Fixed operation costs", "AdditionalCost",
			// "ConCarInvestmentCost","EVCarInvestmentCost","FCEVCarInvestmentCost",
			"Annual Investment costs", "CO2Emission", "AnnualCost", "oilBoilerFuelDemand", "ngasBoilerFuelDemand",
			"biomassBoilerFuelDemand", "AnnualNgasmCHPheat", "AnnualBiomassmCHPheat", "AnnualHPheat",
			"AnnualHH SolarHeat", "transOilDemand", "OilBoilerSolarInput", "NGasBoilerSolarInput", "NGasCHPSolarInput",
			"BiomassBoilerSolarInput", "BiomassCHPSolarInput", "HPSolarInput",
			/*
			 * "OilBoilerUtilization", "NGasBoilerUtilization",
			 * "biomassBoilerSolarUtilization", "biomassCHPSolarUtilization",
			 * "HPSolarUtilization", "Annual MaximumImportElectr.",
			 * "Annual MaximumExportElectr."
			 */ };

	static String outputsinFile[] = { "AnnualPV", "AnnualCHPelec", "AnnualHPelec", "AnnualElecCar", "AnnualH2demand",
			"AnnualOilBoilerheat", "AnnualNGasBoilerheat", "AnnualBiomassBoilerheat", "AnnualNgasmCHPheat",
			"AnnualBiomassmCHPheat", "AnnualHPheat", "AnnualSolarThermalheat", "AnnualImport", "AnnualExport",
			"OilConsumption", "BiomassConsumption", "NGasConsuption", /*
																		 * "PetrolCost", "BiomassCost",
																		 */ "Bottleneck",
			"TotalElectricityExchangeCost", "TotalVariableCost", "FixedOperationCosts", "AdditionalCost",
			// "ConCarInvestmentCost","EVCarInvestmentCost","FCEVInvestmentCost",
			"InvestmentCost", "CO2-Emission", "AnnualCost", "oilBoilerFuelDemand", "NGasBoilerFuelDemand",
			"biomassBoilerFuelDemand", "AnnualNgasmCHPheat", "AnnualBiomassmCHPheat", "AnnualHPheat",
			"AnnualSolarThermal", "transOilDemand", "OilBoilerSolarInput", "NGasBoilerSolarInput", "NGasCHPSolarInput",
			"BiomassBoilerSolarInput", "BiomassCHPSolarInput", "HPSolarInput",
			/*
			 * "OilBoilerUtilization", "NGasBoilerUtilization",
			 * "biomassBoilerSolarUtilization", "biomassCHPSolarUtilization",
			 * "HPSolarUtilization", "Annual MaximumImportElectr.",
			 * "Annual MaximumExportElectr."
			 */
	};

	static String inputUnits[] = { "MWe", "MWe", /* "MWe", "MWe", "MWth", "MWth", "MWth", "TWh", */ "1000 units",
			"1000 units", "1000 units", "MWe", "MW", "MW", "GWh" };
	static String outputUnits[] = { "TWh", "TWh", // "AnnualPV", "AnnualCHPelec",
			"TWh", "TWh", "TWh", "TWh", "TWh", // "AnnualHPelec", "AnnualElecCar", "AnnualH2demand",
												// "AnnualOilBoilerheat", "AnnualNGasBoilerheat",
			"TWh", "TWh", "TWh", "TWh", "TWh", // "AnnualBiomassBoilerheat", "AnnualNgasmCHPheat",
												// "AnnualBiomassmCHPheat", "AnnualHPheat", "AnnualSolarThermalheat",
			"TWh", "TWh", "TWh", // "AnnualImport", "AnnualExport", "OilConsumption",
			"TWh", "TWh", // "BiomassConsumption", "NGasConsuption",
			/*
			 * "MEuro", //"DieselCost", "MEuro",
			 */ "MEuro", // "BiomassCost", "TotalElectricityExchangeCost",
			"MEuro", "MEuro", "MEuro", // "TotalVariableCost", "FixedOperationCosts", "AdditionalCost",
			// "KEuro", "KEuro", "KEuro",
			// //"ConCarInvestmentCost","EVCarInvestmentCost","FCEVInvestmentCost",
			"MEuro", "Mt", "MEuro", // "InvestmentCost","CO2-Emission", "Total Local System Emission", "AnnualCost",
			"TWh", "TWh", "TWh", "TWh", "TWh", "TWh", "TWh", // "oilBoilerFuelDemand", "NGasBoilerFuelDemand",
																// "biomassBoilerFuelDemand", "AnnualNgasmCHPheat",
																// "AnnualBiomassmCHPheat, "AnnualHPheat",
																// "AnnualSolarThermal",
			"TWh", // "transOilDemand"
			"TWh", "TWh", "TWh", "TWh", "TWh", "TWh", // "OilBoilerSolatInput",
														// "NGasBoilerSolarInput","NGasCHPSolarInput",
														// "BiomassBoilerSolarInput", "BiomassCHPSolarInput",
														// "HPSolarInput"
			/*
			 * "TWh","TWh","TWh", "TWh","TWh", //"OilBoilerUtilization",
			 * "NGasBoilerUtilization", "biomassBoilerSolarUtilization",
			 * "biomassCHPSolarUtilization", "HPSolarUtilization" "MW", "MW"
			 */
	};

	String simulatedYear, simulatedScenario;
	/*
	 * VdN data
	 */
	// Heat

	// public static final double maxOfAllHeatDistribution = 0.000221902;
	// public static final double sumOfAllHeatDistributions=1.0;

	// common data for all scenarios

	public static final int averageKMPerYearPerCar = 12900;
	public static final double maxH2DemandInDistribution = 0.000209257;
	public static final double sumH2DemandInDistribution = 1.0;

	// transport
	public long totalKMRunByCars;

	// other variable
	public double totalHeatDemand;
	public double DHHeatProduction;

	// cars efficiencies
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
	// grid distribution cost
	public double elecGridDistributionCost;

	// building envelop energy efficiency
	double buildingEnvelopesEnergyEfficiency;

	public ExtractEnergyPLANParametersCEIS2021(String simulatedYear) {

		this.simulatedYear = simulatedYear;

		if (this.simulatedYear.equals("2025")) {

			// Heat
			totalHeatDemand = 6.927; // in TWh
			DHHeatProduction = 0.234; // in TWh

			// cars
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

			elecGridDistributionCost = 191.01;

			buildingEnvelopesEnergyEfficiency = 1077117; // MWh

		} else if (this.simulatedYear.equals("2030")) {
			// Heat
			totalHeatDemand = 6.888; // in TWh
			DHHeatProduction = 0.233; // in TWh

			// cars
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

			elecGridDistributionCost = 213.61;

			buildingEnvelopesEnergyEfficiency = 1147372;

		} else if (this.simulatedYear.equals("2050")) {
			// Heat
			totalHeatDemand = 6.408; // in TWh
			DHHeatProduction = 0.217; // in TWh

			// cars
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

			elecGridDistributionCost = 236.31;

			buildingEnvelopesEnergyEfficiency = 1363980;
		}

	}

	

	public void extractAllEnergyPLANParametersFromAVARFile(String fileName) throws JMException, IOException {
		MultiMap energyplanmMap = null;
		// TODO Auto-generated method stub

		FileInputStream fos = new FileInputStream(fileName);
		InputStreamReader isr = new InputStreamReader(fos);
		BufferedReader br = new BufferedReader(isr);

		this.readProfiles();

		String line;
		while ((line = br.readLine()) != null) {
			energyplanmMap = this.simulateEnergyPLAN(line);
			this.WriteEnergyPLANParametersToFile(energyplanmMap, fileName);
		}

		br.close();
	}

	public void WriteEnergyPLANParametersToFile(MultiMap energyPLANMap, String path) throws IOException {
		Iterator it;
		Collection<String> col;

		File filetmp = new File(path);
		String arrayStr[] = path.split("\\\\");

		File file = new File(filetmp + "\\allParameters_" + arrayStr[arrayStr.length - 1] + ".txt");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();

			// create header of the file
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			/*
			 * bw.write( "PV_C HP_Heat_P OilBoilerHeat NgasBoilerHeat BiomassBoilerHeat " +
			 * "AnlPV_P AnlImport AnlExport AnlOilBoilerFuel AnlNgasBoilerFuel " +
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
			 * bw.write( "(KW) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) " +
			 * "(GWh) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) " +
			 * "(Mt) (KEuro)");
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
			try {
				col = (Collection<String>) energyPLANMap

						.get(outputsinEnergyPLANMap[i]);
				it = col.iterator();
				String str = it.next().toString();
				if (str.equals("1000"))
					System.out.println("la la");
				if (!str.equals("1000") && str.endsWith("1000")) {
					str = str.substring(0, str.lastIndexOf("1000"));
					str = Double.parseDouble(str) + "";
				}
				output = output + str + ";";
			} catch (NullPointerException e) {
				System.out.println(i);
			}
		}
		bw.write(input + output);
		bw.newLine();
		bw.close();

	}

	public void WriteEnergyPLANParametersFromEvaluateToFile(MultiMap energyPLANMap, String path) throws IOException {
		Iterator it;
		Collection<String> col;

		File filetmp = new File(path);
		String arrayStr[] = path.split("\\\\");

		File file = new File(filetmp + "\\allSolutions" + arrayStr[arrayStr.length - 1] + ".txt");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();

			// create header of the file
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			/*
			 * bw.write( "PV_C HP_Heat_P OilBoilerHeat NgasBoilerHeat BiomassBoilerHeat " +
			 * "AnlPV_P AnlImport AnlExport AnlOilBoilerFuel AnlNgasBoilerFuel " +
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
			 * bw.write( "(KW) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) " +
			 * "(GWh) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) " +
			 * "(Mt) (KEuro)");
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
			try {
				col = (Collection<String>) energyPLANMap

						.get(outputsinEnergyPLANMap[i]);
				it = col.iterator();
				String str = it.next().toString();
				if (str.equals("1000"))
					System.out.println("la la");
				if (!str.equals("1000") && str.endsWith("1000")) {
					str = str.substring(0, str.lastIndexOf("1000"));
					str = Double.parseDouble(str) + "";
				}
				output = output + str + ";";
			} catch (NullPointerException e) {
				System.out.println(i);
			}
		}
		bw.write(input + output);
		bw.newLine();
		bw.close();

	}

	public MultiMap simulateEnergyPLAN(String individual) throws JMException {

		StringTokenizer st = new StringTokenizer(individual);
		// decision variables
		// index - 0 -> PV Capacity
		// index - 1 -> oil boiler heat percentage
		// index - 2 -> nGas boiler heat percentage
		// index - 3 -> nGas micro chp heat percentage
		// index - 4 -> Biomass boiler heat percentage
		// index - 5 -> Biomass micro chp heat percentage
		// last percentage will go to individual HP percentage
		// index - 6 -> EV car parcentage
		// index - 7 -> FCEV(H2) car percentage

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

		// PV
		double pv = Double.parseDouble(st.nextToken().toString());

		// index - 1 -> oil boiler heat percentage
		// index - 2 -> nGas boiler heat percentage
		// index - 3 -> nGas micro chp heat percentage
		// index - 4 -> Biomass boiler heat percentage
		// index - 5 -> Biomass micro chp heat percentage
		// index 6 -> individual HP percentage
		double oilBoilerHeatDemand = Double.parseDouble(st.nextToken().toString());
		double nGasBoilerHeatDemand = Double.parseDouble(st.nextToken().toString());
		double nGasCHPHeatDemand = Double.parseDouble(st.nextToken().toString());
		double biomassBoilerHeatDemand = Double.parseDouble(st.nextToken().toString());
		double biomassCHPHeatDemand = Double.parseDouble(st.nextToken().toString());
		double hpHeatDemand = Double.parseDouble(st.nextToken().toString());

		double totalKMRunByConCar = (double) Double.parseDouble(st.nextToken().toString());
		double totalKMRunByEVCar = (double) Double.parseDouble(st.nextToken().toString());
		double totalKMRunByFCEVCar = (double) Double.parseDouble(st.nextToken().toString());

		double totalPetrolDemandInTWhForTrns = totalKMRunByConCar * efficiencyConCar / Math.pow(10, 9);
		double totalElecDemandInTWhForTrns = totalKMRunByEVCar * efficiencyEVCar / Math.pow(10, 9);
		double totalH2DemandInTWhForTrns = totalKMRunByFCEVCar * efficiencyFCEVCar / Math.pow(10, 9);

		// solar thermal percentage
		double oilBoilerSolarPercentage = Double.parseDouble(st.nextToken().toString());
		double nGasBoilerSolarPercentage = Double.parseDouble(st.nextToken().toString());
		double nGasCHPSolarPercentage = Double.parseDouble(st.nextToken().toString());
		double biomassBoilerSolarPercentage = Double.parseDouble(st.nextToken().toString());
		double biomassCHPSolarPercentage = Double.parseDouble(st.nextToken().toString());
		double hpSolarPercentage = Double.parseDouble(st.nextToken().toString());

		// electrical storage
		double elecStorageCapacity = Double.parseDouble(st.nextToken().toString());

		// hydro capacity
		double hydroCapacity = Double.parseDouble(st.nextToken().toString());

		MultiMap energyplanMap = null;
		MultiMap modifyMap = new MultiValueMap();
		try {
			modifyMap = writeIntoInputFile(pv, oilBoilerHeatDemand, nGasBoilerHeatDemand, nGasCHPHeatDemand,
					biomassBoilerHeatDemand, biomassCHPHeatDemand, hpHeatDemand, totalPetrolDemandInTWhForTrns,
					totalElecDemandInTWhForTrns, totalH2DemandInTWhForTrns, oilBoilerSolarPercentage,
					nGasBoilerSolarPercentage, biomassBoilerSolarPercentage, nGasCHPSolarPercentage,
					biomassCHPSolarPercentage, hpSolarPercentage, elecStorageCapacity, hydroCapacity);
		} catch (IOException e) {
			System.out.print("Pobrlem writting in modified Input file");
		}

		String energyPLANrunCommand = ".\\EnergyPLAN_12.3\\EnergyPLAN.exe -i " + "\".\\modifiedInput.txt\" "
				+ "-ascii \"result.txt\" ";
		try {
			// Process process = new
			// ProcessBuilder(energyPLANrunCommand).start();
			Process process = Runtime.getRuntime().exec(energyPLANrunCommand);
			process.waitFor();
			process.destroy();
			EnergyPLANFileParseForTrentoProvince epfp = new EnergyPLANFileParseForTrentoProvince(".\\result.txt");
			energyplanMap = epfp.parseFile();
			energyplanMap.putAll(modifyMap);

			Iterator it;
			Collection<String> col;

			// objective # 1
			col = (Collection<String>) energyplanMap.get("CO2-emission (total)");
			it = col.iterator();
			double localCO2emission = Double.parseDouble(it.next().toString());

			// extract import
			col = (Collection<String>) energyplanMap.get("AnnualImportElectr.");
			it = col.iterator();
			double elecImport = Double.parseDouble(it.next().toString());

			double co2InImportedEleOil = 0.0, co2InImportedEleNGas = 0.0;
			if (simulatedYear.equals("2025")) {
				co2InImportedEleOil = ((elecImport / 0.51 * 1.83 / 100) * 0.267);
				co2InImportedEleNGas = ((elecImport / 0.51 * 47.96 / 100) * 0.202);

			} else if (simulatedYear.equals("2030")) {
				co2InImportedEleOil = ((elecImport / 0.53 * 0.66 / 100) * 0.267);
				co2InImportedEleNGas = ((elecImport / 0.53 * 43.94 / 100) * 0.202);
			} else if (simulatedYear.equals("2050")) {
				co2InImportedEleNGas = ((elecImport / 0.56 * 12 / 100) * 0.202);
			}

			localCO2emission = localCO2emission + co2InImportedEleOil + co2InImportedEleNGas;

			energyplanMap.put("CO2Emission", localCO2emission);

			col = (Collection<String>) energyplanMap.get("Variable costs");
			it = col.iterator();
			String totalVariableCostStr = it.next().toString();
			double totalVariableCost = Double.parseDouble(totalVariableCostStr);

			col = (Collection<String>) energyplanMap.get("Bottleneck");
			it = col.iterator();
			String bottleneckStr = it.next().toString();
			double bottlenack = Double.parseDouble(bottleneckStr);
			energyplanMap.put("Bottleneck", localCO2emission);

			totalVariableCost -= bottlenack;

			col = (Collection<String>) energyplanMap.get("Fixed operation costs");
			it = col.iterator();
			String fixedOperationalCostStr = it.next().toString();
			double fixedOperationalCost = Double.parseDouble(fixedOperationalCostStr);

			// extract
			col = (Collection<String>) energyplanMap.get("Annual Investment costs");
			it = col.iterator();
			String invest = it.next().toString();
			double investmentCost = Double.parseDouble(invest);

			// calculation of additional cost
			// read annual electricity demand
			col = (Collection<String>) energyplanMap.get("AnnualElectr.Demand");
			it = col.iterator();
			String fixedElecDemandStr = it.next().toString();
			double fixedElecDemand = Double.parseDouble(fixedElecDemandStr);

			// read HP electric demand
			col = (Collection<String>) energyplanMap.get("AnnualHH-HPElectr.");
			it = col.iterator();
			String HPElecDemandStr = it.next().toString();
			double HPElecDemand = Double.parseDouble(HPElecDemandStr);

			// read electrolyzer elec uses
			col = (Collection<String>) energyplanMap.get("AnnualTrans H2Electr.");
			it = col.iterator();
			String ElectrolyzerElecDemandStr = it.next().toString();
			double electrolyzerElecDemand = Double.parseDouble(ElectrolyzerElecDemandStr);

			// read pump elec uses
			col = (Collection<String>) energyplanMap.get("AnnualPumpElectr.");
			it = col.iterator();
			String pumpElectricStr = it.next().toString();
			double pumpElectric = Double.parseDouble(pumpElectricStr);

			// calculate Transportation electricity demand which is
			// totalElecDemandInTWhForTrns

			// variable cost
			double additionalVariableCost = (fixedElecDemand + HPElecDemand + totalElecDemandInTWhForTrns
					+ electrolyzerElecDemand + pumpElectric) * elecGridDistributionCost;

			// fixed operational cost
			double additionalFixedOperationalCost = DHHeatProduction * 3600 * 0.145 / 100 * 0.76;

			// annual invesment cost
			double additionalAnnaulInvestmentCost = (DHHeatProduction * 3600 * 0.145 / 40)
					+ (DHHeatProduction * 3600 * 0.145 / 40 / 100 * 6)
					+ (buildingEnvelopesEnergyEfficiency * 0.0033 / 30)
					+ (buildingEnvelopesEnergyEfficiency * 0.0033 / 30 / 100 * 6);

			double totalAdditionalCost = additionalAnnaulInvestmentCost + additionalFixedOperationalCost
					+ additionalVariableCost;

			double actualAnnualCost = totalVariableCost + fixedOperationalCost + investmentCost + totalAdditionalCost;

			energyplanMap.put("AdditionalCost", totalAdditionalCost);
			energyplanMap.put("AnnualCost", actualAnnualCost);

			col = (Collection<String>) energyplanMap.get("AnnualOilBoilerheat");
			it = col.iterator();
			double AnnualOilBoilerheat = Double.parseDouble(it.next().toString());
			col = (Collection<String>) energyplanMap.get("OilBoilerSolarInput");
			it = col.iterator();
			double OilBoilerSolarInput = Double.parseDouble(it.next().toString());

			double OilBoilerUtilization = solarUtilizationCalculation(AnnualOilBoilerheat, OilBoilerSolarInput);
			energyplanMap.put("OilBoilerUtilization", OilBoilerUtilization);

			col = (Collection<String>) energyplanMap.get("AnnualNGasBoilerheat");
			it = col.iterator();
			double AnnualNGasBoilerheat = Double.parseDouble(it.next().toString());
			col = (Collection<String>) energyplanMap.get("NGasBoilerSolarInput");
			it = col.iterator();
			double NGasBoilerInput = Double.parseDouble(it.next().toString());
			double NGasBoilerUtilization = solarUtilizationCalculation(AnnualNGasBoilerheat, NGasBoilerInput);
			energyplanMap.put("NGasBoilerUtilization", NGasBoilerUtilization);

			col = (Collection<String>) energyplanMap.get("AnnualBiomassBoilerheat");
			it = col.iterator();
			double AnnualBiomassBoilerheat = Double.parseDouble(it.next().toString());
			col = (Collection<String>) energyplanMap.get("BiomassBoilerSolarInput");
			it = col.iterator();
			double BiomassBoilerSolarInput = Double.parseDouble(it.next().toString());
			double biomassBoilerSolarUtilization = solarUtilizationCalculation(AnnualBiomassBoilerheat,
					BiomassBoilerSolarInput);
			energyplanMap.put("biomassBoilerSolarUtilization", biomassBoilerSolarUtilization);

			col = (Collection<String>) energyplanMap.get("AnnualNgasmCHPheat");
			it = col.iterator();
			double AnnualNgasmCHPheat = Double.parseDouble(it.next().toString());
			col = (Collection<String>) energyplanMap.get("NGasCHPSolarInput");
			it = col.iterator();
			double nGasCHPSolarInput = Double.parseDouble(it.next().toString());
			double nGasCHPSolarUtilization = solarUtilizationCalculation(AnnualNgasmCHPheat, nGasCHPSolarInput);
			energyplanMap.put("NgasCHPSolarUtilization", nGasCHPSolarUtilization);

			col = (Collection<String>) energyplanMap.get("AnnualBiomassmCHPheat");
			it = col.iterator();
			double AnnualBiomassmCHPheat = Double.parseDouble(it.next().toString());
			col = (Collection<String>) energyplanMap.get("BiomassCHPSolarInput");
			it = col.iterator();
			double BiomassCHPSolarInput = Double.parseDouble(it.next().toString());
			double biomassCHPSolarUtilization = solarUtilizationCalculation(AnnualBiomassmCHPheat,
					BiomassCHPSolarInput);
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

	public MultiMap writeIntoInputFile(double pv, double oilBoilerHeatDemand, double nGasBoilerHeatDemand,
			double nGasCHPHeatDemand, double biomassBoilerHeatDemand, double biomassCHPHeatDemand, double hpHeatDemand,
			double totalPetrolDemandInTWhForTrns, double totalElecDemandInTWhForTrns, double totalH2DemandInTWhForTrns,
			double oilBoilerSolarPercentage, double nGasBoilerSolarPercentage, double biomassBoilerSolarPercentage,
			double nGasCHPSolarPercentage, double biomassCHPSolarPercentage, double hpSolarPercentage,
			double elecStorageCapacity, double hydroCapacity) throws IOException {

		MultiMap modifyMap = new MultiValueMap();

		modifyMap.put("AnnualOilBoilerheat", Math.round(oilBoilerHeatDemand * 100.0) / 100.0); // this is done for 2
																								// decimal place
																								// precision
		modifyMap.put("AnnualNGasBoilerheat", Math.round(nGasBoilerHeatDemand * 100.0) / 100.0);
		modifyMap.put("AnnualBiomassBoilerheat", Math.round(biomassBoilerHeatDemand * 100.0) / 100.0);
		modifyMap.put("AnnualNgasmCHPheat", Math.round(nGasCHPHeatDemand * 100.0) / 100.0);
		modifyMap.put("AnnualBiomassmCHPheat", Math.round(biomassCHPHeatDemand * 100.0) / 100.0);
		modifyMap.put("AnnualHPheat", Math.round(hpHeatDemand * 100.0) / 100.0);

		modifyMap.put("PV_Cap", (int) Math.round(pv));
		modifyMap.put("Hydro_Cap", (int) Math.round(hydroCapacity));

		String modifiedParameters[] = { "input_RES1_capacity=", // pv (0)
				"input_fuel_Households[2]=", // oil boiler (1)
				"input_HH_oilboiler_Solar=", // oil Solar Thermal (2)
				"input_fuel_Households[3]=", // nGas boiler (3)
				"input_HH_ngasboiler_Solar=", // nGas boiler Solar Thermal (4)
				"input_HH_NgasCHP_heat=", // nGas micro chp (5)
				"input_HH_NgasCHP_solar=", // nGas microp chp solar (6)
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

				"input_cap_pump_el=", // Pump capacity (20)
				"input_cap_turbine_el=", // turbine capacity (21)
				"input_cap_turbine_el=", // elec storage capacity (22)

				"input_hydro_cap=", // hydro capacity (23)
				"input_hydro_watersupply=" // hydro water supply (24)

		};

		String cooresPondingValues[] = new String[modifiedParameters.length];

		File modifiedInput = new File("modifiedInput.txt");
		if (modifiedInput.exists()) {
			modifiedInput.delete();
		}
		modifiedInput.createNewFile();

		FileWriter fw = new FileWriter(modifiedInput.getAbsoluteFile());
		BufferedWriter modifiedInputbw = new BufferedWriter(fw);

		String path = "";
		if (simulatedYear.equals("2025")) {
			path = ".\\EnergyPLAN_12.3\\energyPlan Data\\Data\\PEAP\\PEAP_PAT_LC_2025_DH_v10_opt.txt";
		} else if (simulatedYear.equals("2030")) {
			path = ".\\EnergyPLAN_12.3\\energyPlan Data\\Data\\PEAP\\PEAP_PAT_LC_2030_DH_v10_opt.txt";
		} else if (simulatedYear.equals("2050")) {
			path = ".\\EnergyPLAN_12.3\\energyPlan Data\\Data\\PEAP\\PEAP_PAT_LC_2050_DH_v10_opt.txt";
		}

		BufferedReader mainInputbr = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-16"));

		double totalSolarThermalInput = 0.0;

		cooresPondingValues[0] = "" + pv;

		double oilBoilerFuelDemand = oilBoilerHeatDemand / oilBoilerEfficiency;
		cooresPondingValues[1] = "" + oilBoilerFuelDemand;
		modifyMap.put("oilBoilerFuelDemand", oilBoilerFuelDemand);

		double oilBoilerSolarThermal = oilBoilerFuelDemand * oilBoilerSolarPercentage;
		cooresPondingValues[2] = "" + oilBoilerSolarThermal;
		totalSolarThermalInput += oilBoilerSolarThermal;
		modifyMap.put("OilBoilerSolarInput", oilBoilerSolarThermal);

		double nGasBoilerFuelDemand = nGasBoilerHeatDemand / nGasBoilerEfficiency;
		cooresPondingValues[3] = "" + nGasBoilerFuelDemand;
		modifyMap.put("ngasBoilerFuelDemand", nGasBoilerFuelDemand);

		double nGasBoilerSolarThermal = nGasBoilerFuelDemand * nGasBoilerSolarPercentage;
		cooresPondingValues[4] = "" + nGasBoilerSolarThermal;
		totalSolarThermalInput += nGasBoilerSolarThermal;
		modifyMap.put("NGasBoilerSolarInput", nGasBoilerSolarThermal);

		cooresPondingValues[5] = "" + nGasCHPHeatDemand;

		double nGasCHPSolarThermal = nGasCHPHeatDemand * efficiencynGasCHPThermal * nGasCHPSolarPercentage;
		cooresPondingValues[6] = "" + nGasCHPSolarThermal;
		totalSolarThermalInput += nGasCHPSolarThermal;
		modifyMap.put("NGasCHPSolarInput", nGasCHPSolarThermal);

		double biomassBoilerFuelDemand = biomassBoilerHeatDemand / biomassBoilerEfficiency;
		cooresPondingValues[7] = "" + biomassBoilerFuelDemand;
		modifyMap.put("biomassBoilerFuelDemand", biomassBoilerFuelDemand);

		double biomassBoilerSolarThermal = biomassBoilerFuelDemand * biomassBoilerSolarPercentage;
		cooresPondingValues[8] = "" + biomassBoilerSolarThermal;
		totalSolarThermalInput += biomassBoilerSolarThermal;
		modifyMap.put("BiomassBoilerSolarInput", biomassBoilerSolarThermal);

		cooresPondingValues[9] = "" + biomassCHPHeatDemand;

		double biomassCHPSolarThermal = biomassCHPHeatDemand * efficiencyBiomassCHPThermal * biomassCHPSolarPercentage;
		cooresPondingValues[10] = "" + biomassCHPSolarThermal;
		totalSolarThermalInput += biomassCHPSolarThermal;
		modifyMap.put("BiomassCHPSolarInput", biomassCHPSolarThermal);

		cooresPondingValues[11] = "" + hpHeatDemand;

		double hpSolarThermal = hpHeatDemand * hpSolarPercentage;
		cooresPondingValues[12] = "" + hpSolarThermal;
		totalSolarThermalInput += hpSolarThermal;
		modifyMap.put("HPSolarInput", hpSolarThermal);

		modifyMap.put("SolarThermal", totalSolarThermalInput);

		cooresPondingValues[13] = "" + totalPetrolDemandInTWhForTrns;
		modifyMap.put("transOilDemand", totalPetrolDemandInTWhForTrns);

		// "NumberOfConCars", "NumberOfEVCars","NumberOfFCEVCars"

		double numberOfConCars = (totalPetrolDemandInTWhForTrns * Math.pow(10, 9))
				/ (efficiencyConCar * averageKMPerYearPerCar * Math.pow(10, 3));
		cooresPondingValues[14] = "" + numberOfConCars;
		modifyMap.put("NumberOfConCars", numberOfConCars);

		cooresPondingValues[15] = "" + totalElecDemandInTWhForTrns;

		double numberOfEVCars = totalElecDemandInTWhForTrns * Math.pow(10, 9)
				/ (efficiencyEVCar * averageKMPerYearPerCar * Math.pow(10, 3));
		cooresPondingValues[16] = "" + numberOfEVCars;
		modifyMap.put("NumberOfEVCars", numberOfEVCars);

		cooresPondingValues[17] = "" + totalH2DemandInTWhForTrns;
		modifyMap.put("AnnualH2demand", totalH2DemandInTWhForTrns);

		double numberOfFCEVCars = totalH2DemandInTWhForTrns * Math.pow(10, 9)
				/ (efficiencyFCEVCar * averageKMPerYearPerCar * Math.pow(10, 3));
		cooresPondingValues[18] = "" + numberOfFCEVCars;
		modifyMap.put("NumberOfFCEVCars", numberOfFCEVCars);

		int electrolyzerCapacity = (int) Math.floor(maxH2DemandInDistribution * totalH2DemandInTWhForTrns
				* Math.pow(10, 6) / (efficiencyElectrolyzerTrans * sumH2DemandInDistribution)) + 1;
		cooresPondingValues[19] = "" + electrolyzerCapacity;
		modifyMap.put("Eloctrolyzer_Cap", electrolyzerCapacity);

		cooresPondingValues[20] = "" + elecStorageCapacity * 1000 / 2;
		modifyMap.put("ElecStorage_Turbine_Cap", elecStorageCapacity * 1000 / 2);

		cooresPondingValues[21] = "" + elecStorageCapacity * 1000 / 2;
		modifyMap.put("ElecStorage_Pump_Cap", elecStorageCapacity * 1000 / 2);

		cooresPondingValues[22] = "" + elecStorageCapacity;
		modifyMap.put("ElecStorage_Storage_Cap", elecStorageCapacity);

		cooresPondingValues[23] = "" + hydroCapacity;
		cooresPondingValues[24] = "" + hydroCapacity * 0.002664;

		String line;
		while ((line = mainInputbr.readLine()) != null) {
			line.trim();
			boolean trackBreak = false;
			for (int i = 0; i < modifiedParameters.length; i++) {
				if (line.startsWith(modifiedParameters[i]) || line.endsWith(modifiedParameters[i])) {
					modifiedInputbw.write(line + "\n");
					line = mainInputbr.readLine();
					line = line.replace(line, cooresPondingValues[i]);
					modifiedInputbw.write(line + "\n");
					trackBreak = true;
					break;
				}
			}
			if (trackBreak)
				continue;
			else
				modifiedInputbw.write(line + "\n");

		}

		modifiedInputbw.close();
		mainInputbr.close();

		return modifyMap;
	}

	ArrayList<Double> thermalDeamndProfile = new ArrayList();
	ArrayList<Double> solarThermalProductionProfile = new ArrayList();
	Double sumOfThermalDemandProfile;
	Double sumOfSolarThermalProductionProfile;
	Double averageHourlyHeatingDemand;

	public void readProfiles(String simulatedYear) throws IOException {

		BufferedReader brthermalDeamndProfile=null;
		BufferedReader brsolarThermalProductionProfile=null;
		
		if(simulatedYear.equals("2030")) {	
			brthermalDeamndProfile = new BufferedReader(
					new FileReader(".\\EnergyPLAN161\\energyPlan Data\\Distributions\\CEIS_Thermal_Demand_LC_2030.txt"));
			brsolarThermalProductionProfile = new BufferedReader(
					new FileReader(".\\EnergyPLAN161\\energyPlan Data\\Distributions\\CEIS_SolarThermal_Hourly_Production_2030.txt"));
		}else if(simulatedYear.equals("2050")) {
			brthermalDeamndProfile = new BufferedReader(
					new FileReader(".\\EnergyPLAN161\\energyPlan Data\\Distributions\\CEIS_Thermal_Demand_LC_2050.txt"));
			brsolarThermalProductionProfile = new BufferedReader(
					new FileReader(".\\EnergyPLAN161\\energyPlan Data\\Distributions\\CEIS_SolarThermal_Hourly_Production_2050.txt"));
		}
		
		String line;

		sumOfThermalDemandProfile = 0.0;

		line = brthermalDeamndProfile.readLine();
		while ((line = brthermalDeamndProfile.readLine()) != null) {
			Double temp = Double.parseDouble(line);
			sumOfThermalDemandProfile += temp;
			thermalDeamndProfile.add(temp);

		}

		sumOfSolarThermalProductionProfile = 0.0;
		line = brsolarThermalProductionProfile.readLine();
		while ((line = brsolarThermalProductionProfile.readLine()) != null) {
			Double temp1 = Double.parseDouble(line);
			sumOfSolarThermalProductionProfile += temp1;
			solarThermalProductionProfile.add(temp1);

		}
		System.out.println(sumOfSolarThermalProductionProfile);

	}

	public double solarUtilizationCalculation(double heatDemand, double solarInput, double storageDays) {
		
		
		double solarUtilization = 0.0;
		double storageContent = 0.0;

		// storage capacity
		double storage = (heatDemand / 8784)*  storageDays  * 24;

		double startingStorageContent = 0.0, endingStorageContent = 0.0, percentage = 0.0;

		/*int run = 0;
		do {
			
			
			if(run >=  10)
				break;*/
			
			startingStorageContent = endingStorageContent;
			storageContent = startingStorageContent;
			solarUtilization = 0.0;
			
			
			for (int i = 0; i < thermalDeamndProfile.size(); i++) {
				double hourSolarThermalproduction = solarInput * solarThermalProductionProfile.get(i)
						/ sumOfSolarThermalProductionProfile;
				double hourThermalDemand = heatDemand * thermalDeamndProfile.get(i) / sumOfThermalDemandProfile;

				/*
				 * if(hourSolarThermalproduction>hourThermalDemand)
				 * solarUtilization+=hourThermalDemand; else
				 * solarUtilization+=hourSolarThermalproduction;
				 */

				if (hourSolarThermalproduction <= hourThermalDemand) {
					solarUtilization += (hourSolarThermalproduction
							+ Math.min(storageContent, (hourThermalDemand - hourSolarThermalproduction)));
					storageContent -= Math.min(storageContent, (hourThermalDemand - hourSolarThermalproduction));
					if (storageContent < 0)
						storageContent = 0.0;
				} else {
					solarUtilization += (hourThermalDemand);
					storageContent += (hourSolarThermalproduction - hourThermalDemand);
					if (storageContent >= (storage))
						storageContent = storage;

				}

				

			}
			endingStorageContent = storageContent;
			
			/*if(Math.abs(startingStorageContent - endingStorageContent) != 0.0)
				percentage =  Math.abs(startingStorageContent - endingStorageContent)/Math.max(startingStorageContent, endingStorageContent)*100.0;
			else
				percentage = 0.0;
			
			run++;
		}while( percentage >= 10.0 );
		*/
		
		
		return solarUtilization;
		}
	
	public double solarUtilizationCalculation(String xSolarInput, double heatDemand, double solarInput, double storageDays) {
		
		
		double solarUtilization = 0.0;
		double storageContent = 0.0;

		// storage capacity
		double storage = (heatDemand / 8784)*  storageDays  * 24;

		double startingStorageContent = 0.0, endingStorageContent = 0.0, percentage = 0.0;

		int run = 0;
		do {
			
			
			if(run >=  10)
				break;
			
			startingStorageContent = endingStorageContent;
			storageContent = startingStorageContent;
			solarUtilization = 0.0;
			
			
			for (int i = 0; i < thermalDeamndProfile.size(); i++) {
				double hourSolarThermalproduction = solarInput * solarThermalProductionProfile.get(i)
						/ sumOfSolarThermalProductionProfile;
				double hourThermalDemand = heatDemand * thermalDeamndProfile.get(i) / sumOfThermalDemandProfile;

				/*
				 * if(hourSolarThermalproduction>hourThermalDemand)
				 * solarUtilization+=hourThermalDemand; else
				 * solarUtilization+=hourSolarThermalproduction;
				 */

				if (hourSolarThermalproduction <= hourThermalDemand) {
					solarUtilization += (hourSolarThermalproduction
							+ Math.min(storageContent, (hourThermalDemand - hourSolarThermalproduction)));
					storageContent -= Math.min(storageContent, (hourThermalDemand - hourSolarThermalproduction));
					if (storageContent < 0)
						storageContent = 0.0;
				} else {
					solarUtilization += (hourThermalDemand);
					storageContent += (hourSolarThermalproduction - hourThermalDemand);
					if (storageContent >= (storage))
						storageContent = storage;

				}

				

			}
			endingStorageContent = storageContent;
			
			if(Math.abs(startingStorageContent - endingStorageContent) != 0.0)
				percentage =  Math.abs(startingStorageContent - endingStorageContent)/Math.max(startingStorageContent, endingStorageContent)*100.0;
			else
				percentage = 0.0;
			
			run++;
		}while( percentage >= 10.0 );
		
		
		
		return solarUtilization;
		}

	public static void main(String[] args) throws IOException, JMException {

		/*
		 * MultiMap energyplanmMap = null; ExtractEnergyPLANParametersTrentoProvinceV10
		 * ob = new ExtractEnergyPLANParametersTrentoProvinceV10(args[1]);
		 * FileInputStream fos = new FileInputStream(args[0]); InputStreamReader isr =
		 * new InputStreamReader(fos); BufferedReader br = new BufferedReader(isr);
		 * 
		 * ob.readProfiles();
		 * 
		 * String line; while ((line = br.readLine()) != null) { energyplanmMap =
		 * ob.simulateEnergyPLAN(line);
		 * ob.WriteEnergyPLANParametersToFile(energyplanmMap, args[0]); }
		 * 
		 * br.close();
		 */

		ExtractEnergyPLANParametersCEIS2021 f = new ExtractEnergyPLANParametersCEIS2021("2030");
		f.readProfiles("2030");
		System.out.println(f.solarUtilizationCalculation("c", 42.43, 4.52, 317));

	}
	
}
