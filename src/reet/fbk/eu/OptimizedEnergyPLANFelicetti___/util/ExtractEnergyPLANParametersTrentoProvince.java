package reet.fbk.eu.OptimizeEnergyPLANTrentoProvince.util;

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
import reet.fbk.eu.OptimizeEnergyPLANTrentoProvince.parseFile.EnergyPLANFileParseForTrentoProvince;
import reet.fbk.eu.OptimizeEnergyPLANVdN.parseFile.EnergyPLANFileParseForVdN;

public class ExtractEnergyPLANParametersTrentoProvince {


	static String inputs[] = { "PV_Cap", /*"Biomass_CHP_Cap", "NGas_CHP_Cap", "HP_Cap",
			"Oil_boiler_Cap", "NGas_boiler_Cap", "Biomass_boiler_Cap", "SolarThermal", */
			"NumberOfConCars", "NumberOfEVCars", "NumberOfFCEVCars", "Eloctrolyzer_Cap"  };
	
	static String outputsinEnergyPLANMap[] = { "AnnualPVElectr.", "AnnualHH-CHPElectr.",
			"AnnualHH-HPElectr.", "AnnualFlexibleElectr.", "AnnualH2demand", "AnnualOilBoilerheat", "AnnualNGasBoilerheat",
			"AnnualBiomassBoilerheat", "AnnualNgasmCHPheat", "AnnualBiomassmCHPheat", "AnnualHPheat", "AnnualHH SolarHeat",
			"AnnualImportElectr.", "AnnualExportElectr.", "Oil Consumption",
			"Biomass Consumption", "Ngas Consumption", 
			/*"Gasoil/Diesel",
			"Biomass",*/ "Electricity exchange",
			"Variable costs", "Fixed operation costs", "AdditionalCost",
			//"ConCarInvestmentCost","EVCarInvestmentCost","FCEVCarInvestmentCost",
			"Annual Investment costs", "CO2Emission", "AnnualCost", 
			"oilBoilerFuelDemand", "ngasBoilerFuelDemand", "biomassBoilerFuelDemand", "AnnualNgasmCHPheat", "AnnualBiomassmCHPheat", "AnnualHPheat", "AnnualHH SolarHeat", 
			"transOilDemand", 
			"OilBoilerSolarInput", "NGasBoilerSolarInput","NGasCHPSolarInput", "BiomassBoilerSolarInput", "BiomassCHPSolarInput", "HPSolarInput",
			/*"OilBoilerUtilization",	"NGasBoilerUtilization", "biomassBoilerSolarUtilization", "biomassCHPSolarUtilization", "HPSolarUtilization",
			"Annual MaximumImportElectr.", "Annual MaximumExportElectr."*/};

	static String outputsinFile[] = { "AnnualPV", "AnnualCHPelec",
			"AnnualHPelec", "AnnualElecCar", "AnnualH2demand", "AnnualOilBoilerheat", "AnnualNGasBoilerheat",
			"AnnualBiomassBoilerheat", "AnnualNgasmCHPheat", "AnnualBiomassmCHPheat", "AnnualHPheat", "AnnualSolarThermalheat", 
			"AnnualImport", "AnnualExport", "OilConsumption",
			"BiomassConsumption", "NGasConsuption", 
			/*"PetrolCost", 
			"BiomassCost",*/ "TotalElectricityExchangeCost", 
			"TotalVariableCost", "FixedOperationCosts", "AdditionalCost", 
			//"ConCarInvestmentCost","EVCarInvestmentCost","FCEVInvestmentCost", 
			"InvestmentCost","CO2-Emission", "AnnualCost", 
			"oilBoilerFuelDemand", "NGasBoilerFuelDemand", "biomassBoilerFuelDemand", "AnnualNgasmCHPheat", "AnnualBiomassmCHPheat", "AnnualHPheat", "AnnualSolarThermal", 
			"transOilDemand",
			"OilBoilerSolarInput", "NGasBoilerSolarInput", "NGasCHPSolarInput", "BiomassBoilerSolarInput", "BiomassCHPSolarInput", "HPSolarInput",
			/*"OilBoilerUtilization",	"NGasBoilerUtilization", "biomassBoilerSolarUtilization", "biomassCHPSolarUtilization", "HPSolarUtilization",
			"Annual MaximumImportElectr.", "Annual MaximumExportElectr."*/
	};

	static String inputUnits[] = { "MWe", /*"MWe", "MWe", "MWth", "MWth", "MWth", "TWh",*/ "1000 units", "1000 units", "1000 units", "MWe" };
	static String outputUnits[] = { "TWh", "TWh", // "AnnualPV", "AnnualCHPelec",
			"TWh", "TWh", "TWh", "TWh", "TWh", //"AnnualHPelec", "AnnualElecCar", "AnnualH2demand", "AnnualOilBoilerheat", "AnnualNGasBoilerheat",
			"TWh", "TWh", "TWh", "TWh", "TWh", //"AnnualBiomassBoilerheat", "AnnualNgasmCHPheat", "AnnualBiomassmCHPheat", "AnnualHPheat", "AnnualSolarThermalheat", 
			"TWh", "TWh", "TWh",  //"AnnualImport", "AnnualExport", "OilConsumption",
			"TWh", "TWh",  //"BiomassConsumption", "NGasConsuption", 
			/*"MEuro",  //"DieselCost", 
			"MEuro",*/ "MEuro", //"BiomassCost", "TotalElectricityExchangeCost",
			"MEuro", "MEuro", "MEuro", //"TotalVariableCost", "FixedOperationCosts", "AdditionalCost", 
			//"KEuro", "KEuro", "KEuro", //"ConCarInvestmentCost","EVCarInvestmentCost","FCEVInvestmentCost", 
			"MEuro", "Mt", "MEuro", //"InvestmentCost","CO2-Emission", "Total Local System Emission", "AnnualCost", 
			"TWh","TWh","TWh","TWh","TWh","TWh","TWh", //"oilBoilerFuelDemand", "NGasBoilerFuelDemand", "biomassBoilerFuelDemand", "AnnualNgasmCHPheat", "AnnualBiomassmCHPheat, "AnnualHPheat", "AnnualSolarThermal", 
			"TWh",   //"transOilDemand"
			"TWh","TWh","TWh", "TWh","TWh","TWh", //"OilBoilerSolatInput", "NGasBoilerSolarInput","NGasCHPSolarInput", "BiomassBoilerSolarInput", "BiomassCHPSolarInput", "HPSolarInput" 
			/*"TWh","TWh","TWh", "TWh","TWh", //"OilBoilerUtilization",	"NGasBoilerUtilization", "biomassBoilerSolarUtilization", "biomassCHPSolarUtilization", "HPSolarUtilization"
			"MW", "MW"*/
	};

	
	String simulatedYear, simulatedScenario;
	/*
	 * VdN data
	 */
	// Heat
	
	public static final double maxOfAllHeatDistribution = 0.000221902;
	public static final double sumOfAllHeatDistributions=1.0;

	//common data for all scenarios
	
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

	public double biomassCHPThermalEfficiency;
	public double nGasCHPThermalEfficiency;
	
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


	public ExtractEnergyPLANParametersTrentoProvince(String simulatedYear) {
		
		this.simulatedYear=simulatedYear;
		

		if(this.simulatedYear.equals("2030")){
			// Heat
			totalHeatDemand = 6.285; // in TWh
			DHHeatProduction = 0.284; // in TWh
			
			//cars
			efficiencyConCar = 0.460; // KWh/km
			efficiencyFCEVCar = 0.299; // KWh/km
			efficiencyEVCar = 0.133; // Kwh/km
			totalKMRunByCars = 10071000000L;
			
			nGasCHPThermalEfficiency = 0.48;
			biomassCHPThermalEfficiency = 0.48;
			
			
			oilBoilerEfficiency = 0.85;
			nGasBoilerEfficiency = 0.95;
			biomassBoilerEfficiency = 0.78;
			COP = 3.97;
			
			// H2
			efficiencyElectrolyzerTrans = 0.7217;
	
			elecGridDistributionCost =  203.44;
			
			buildingEnvelopesEnergyEfficiency = 1082602;

		}else if(this.simulatedYear.equals("2050")){
			// Heat
			totalHeatDemand =  5.769; // in TWh
			DHHeatProduction = 0.232; // in TWh	
				
			//cars
			efficiencyConCar = 0.340; // KWh/km
			efficiencyFCEVCar = 0.226; // KWh/km
			efficiencyEVCar = 0.106; // Kwh/km
			totalKMRunByCars = 10700000000L;
			
			
			nGasCHPThermalEfficiency = 0.50;
			biomassCHPThermalEfficiency = 0.50;
			
			
			oilBoilerEfficiency = 0.90;
			nGasBoilerEfficiency = 1.0;
			biomassBoilerEfficiency = 0.80;
			COP = 4.30;
			
			// H2
			efficiencyElectrolyzerTrans = 0.7535;
								
			elecGridDistributionCost =  204.59;
			
			buildingEnvelopesEnergyEfficiency = 1238009;
		}
		
	}

	public static void main(String[] args) throws IOException, JMException {

		MultiMap energyplanmMap = null;
		// TODO Auto-generated method stub
		ExtractEnergyPLANParametersTrentoProvince ob = new ExtractEnergyPLANParametersTrentoProvince("2030");
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
	
	public void extractAllEnergyPLANParametersFromAVARFile(String fileName) throws JMException, IOException{
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

		// PV
		double pv = Double.parseDouble(st.nextToken().toString());

	
		double heatPercentages[] = new double[5];
		for (int i = 0; i < 5; i++) {
			heatPercentages[i] = Double.parseDouble(st.nextToken().toString());
		}

		Arrays.sort(heatPercentages);

		// oil-boiler percentage
		double oilBoilerHeatPercentage = heatPercentages[0];
		// Ngas-boiler heat percentage
		double nGasBoilerHeatPercentage = heatPercentages[1]
				- heatPercentages[0];
		// ngas chp heat parcentage
		double nGasCHPHeatPercentage = heatPercentages[2]
				- heatPercentages[1];
		// biomass-boiler heat percentage
		double biomassBoilerHeatPercentage = heatPercentages[3]
				- heatPercentages[2];
		// biomass chp heat percentage
		double biomassCHPHeatPercentage = heatPercentages[4]
				- heatPercentages[3];

		// heat pump heat percentage
		double hpHeatPercentage = 1.0 - heatPercentages[4];

		// car percentages
		double carPercentages[] = new double[2];
		for (int i = 0; i < 2; i++) {
			carPercentages[i] = Double.parseDouble(st.nextToken().toString());
		}
		Arrays.sort(carPercentages);
		// EV percentage of run
		double EVCarPercentage = carPercentages[0];

		// FCEV car percentage of run
		double FCEVCarPercentage = carPercentages[1] - carPercentages[0];

		// conventional car percentage of run
		double conCarPercentage = 1 - carPercentages[1];

				
		double totalKMRunByConCar = (long) totalKMRunByCars * conCarPercentage;
		double totalKMRunByEVCar = (long) totalKMRunByCars * EVCarPercentage;
		double totalKMRunByFCEVCar = (long) totalKMRunByCars * FCEVCarPercentage;

		double totalPetrolDemandInTWhForTrns = totalKMRunByConCar
				* efficiencyConCar / Math.pow(10, 9);
		double totalElecDemandInTWhForTrns = totalKMRunByEVCar
				* efficiencyEVCar / Math.pow(10, 9);
		double totalH2DemandInTWhForTrns = totalKMRunByFCEVCar
				* efficiencyFCEVCar / Math.pow(10, 9);
		
		
		// solar thermal percentage
		double oilBoilerSolarPercentage = Double.parseDouble(st.nextToken().toString());
		double nGasBoilerSolarPercentage = Double.parseDouble(st.nextToken().toString());
		double nGasCHPSolarPercentage = Double.parseDouble(st.nextToken().toString());
		double biomassBoilerSolarPercentage = Double.parseDouble(st.nextToken().toString());
		double biomassCHPSolarPercentage = Double.parseDouble(st.nextToken().toString());
		double hpSolarPercentage = Double.parseDouble(st.nextToken().toString());
				
				
				
		MultiMap energyplanMap = null;
		MultiMap modifyMap = new MultiValueMap();
		try{
			modifyMap = writeIntoInputFile(pv, oilBoilerHeatPercentage,
					nGasBoilerHeatPercentage, nGasCHPHeatPercentage, 
					biomassBoilerHeatPercentage, biomassCHPHeatPercentage, 
					hpHeatPercentage,
					totalPetrolDemandInTWhForTrns, totalElecDemandInTWhForTrns,
					totalH2DemandInTWhForTrns, oilBoilerSolarPercentage,
					nGasBoilerSolarPercentage, biomassBoilerSolarPercentage,
					nGasCHPSolarPercentage, biomassCHPSolarPercentage, hpSolarPercentage);
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
			EnergyPLANFileParseForTrentoProvince epfp = new EnergyPLANFileParseForTrentoProvince(
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
			energyplanMap.put("CO2Emission", localCO2emission);
			

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

			// extract
			col = (Collection<String>) energyplanMap
					.get("Annual Investment costs");
			it = col.iterator();
			String invest = it.next().toString();
			double investmentCost = Double.parseDouble(invest);

			
			//calculation of additional cost
			//read annual electricity demand
			col = (Collection<String>) energyplanMap.get("AnnualElectr.Demand");
			it = col.iterator();
			String fixedElecDemandStr = it.next().toString();
			double fixedElecDemand = Double.parseDouble(fixedElecDemandStr);
			
			//read HP electric demand
			col = (Collection<String>) energyplanMap.get("AnnualHH-HPElectr.");
			it = col.iterator();
			String HPElecDemandStr = it.next().toString();
			double HPElecDemand = Double.parseDouble(HPElecDemandStr);
			

			//calculate Transportation electricity demand which is totalElecDemandInTWhForTrns
			
			//variable cost
			double additionalVariableCost = (fixedElecDemand + HPElecDemand + totalElecDemandInTWhForTrns)*elecGridDistributionCost;
			
			//fixed operational cost
			double additionalFixedOperationalCost = DHHeatProduction*3600*0.145/100*0.76;
			
			//annual invesment cost
			double additionalAnnaulInvestmentCost = 	(DHHeatProduction *3600* 0.145 /40)+
											(DHHeatProduction *3600* 0.145 /40/100*3)+
											(buildingEnvelopesEnergyEfficiency*0.0033/30)+
											(buildingEnvelopesEnergyEfficiency* 0.0033 /30/100*3);
			
			double totalAdditionalCost = additionalAnnaulInvestmentCost + additionalFixedOperationalCost
					+additionalVariableCost;
			
			
			double actualAnnualCost = totalVariableCost + fixedOperationalCost
					+ investmentCost + totalAdditionalCost;

					
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
			double biomassBoilerSolarUtilization = solarUtilizationCalculation(AnnualBiomassBoilerheat, BiomassBoilerSolarInput);
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
			double biomassCHPSolarUtilization = solarUtilizationCalculation(AnnualBiomassmCHPheat, BiomassCHPSolarInput);
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
			double nGasBoilerHeatPercentage, double nGasCHPHeatPercentage, 
			double biomassBoilerHeatPercentage,
			double biomassCHPHeatPercentage, double hpHeatPercentage,
			double totalPetrolDemandInTWhForTrns,
			double totalElecDemandInTWhForTrns,
			double totalH2DemandInTWhForTrns, double oilBoilerSolarPercentage,
			double nGasBoilerSolarPercentage, double biomassBoilerSolarPercentage,
			double nGasCHPSolarPercentage, double biomassCHPSolarPercentage, double hpSolarPercentage)
			throws IOException {
		
		
		
		MultiMap modifyMap = new MultiValueMap();
		double totalOilBoilerHeat = oilBoilerHeatPercentage * totalHeatDemand;
		double totalNgasBoilerHeat = nGasBoilerHeatPercentage * totalHeatDemand;
		double totalBiomassBoilerHeat = biomassBoilerHeatPercentage
				* totalHeatDemand;
		double totalNgasCHPHeat = nGasCHPHeatPercentage * totalHeatDemand;
		double totalBiomassCHPHeat = biomassCHPHeatPercentage * totalHeatDemand;
		double totalHPHeat = hpHeatPercentage * totalHeatDemand;
		
		modifyMap.put("AnnualOilBoilerheat",
				Math.round(totalOilBoilerHeat * 100.0) / 100.0); //this is done for 2 decimal place precision
		modifyMap.put("AnnualNGasBoilerheat",
				Math.round(totalNgasBoilerHeat * 100.0) / 100.0);
		modifyMap.put("AnnualBiomassBoilerheat",
				Math.round(totalBiomassBoilerHeat * 100.0) / 100.0);
		modifyMap.put("AnnualNgasmCHPheat",
				Math.round(totalNgasCHPHeat * 100.0) / 100.0);
		modifyMap.put("AnnualBiomassmCHPheat",
				Math.round(totalBiomassCHPHeat * 100.0) / 100.0);
		modifyMap.put("AnnualHPheat", Math.round(totalHPHeat * 100.0) / 100.0);


		modifyMap.put(inputs[0], (int) Math.round(pv));
			
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
				"input_cap_ELTtrans_el=" // //corresponding eletrolyzer capacity (19)
		};

		String cooresPondingValues[] = new String[20];

		File modifiedInput = new File("modifiedInput.txt");
		if (modifiedInput.exists()) {
			modifiedInput.delete();
		}
		modifiedInput.createNewFile();

		FileWriter fw = new FileWriter(modifiedInput.getAbsoluteFile());
		BufferedWriter modifiedInputbw = new BufferedWriter(fw);

		String path=""; 
		if(simulatedYear.equals("2025") ){
			path =".\\EnergyPLAN_12.3\\energyPlan Data\\Data\\PEAP\\PEAP_PAT_LC_2025_DH_v5_opt.txt";
		}
		else if(simulatedYear.equals("2030") ){
			path =".\\EnergyPLAN_12.3\\energyPlan Data\\Data\\PEAP\\PEAP_PAT_LC_2030_DH_v5_opt.txt";
		}else if(simulatedYear.equals("2050")) {
			path =".\\EnergyPLAN_12.3\\energyPlan Data\\Data\\PEAP\\PEAP_PAT_LC_2050_DH_v5_opt.txt";
		}
		
		BufferedReader mainInputbr = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-16"));
    	
    	double totalSolarThermalInput=0.0;
    	
		cooresPondingValues[0] = "" + pv;
		
		double oilBoilerFuelDemand = oilBoilerHeatPercentage * totalHeatDemand
				/ oilBoilerEfficiency;
		cooresPondingValues[1] = "" + oilBoilerFuelDemand;
		modifyMap.put("oilBoilerFuelDemand", oilBoilerFuelDemand);
		
		double OilBoilerThermal = oilBoilerFuelDemand * oilBoilerSolarPercentage;
		cooresPondingValues[2] = "" + OilBoilerThermal;
		totalSolarThermalInput+=OilBoilerThermal;
		modifyMap.put("OilBoilerSolarInput", OilBoilerThermal);
		
		double ngasBoilerFuelDemand = nGasBoilerHeatPercentage
				* totalHeatDemand / nGasBoilerEfficiency;
		cooresPondingValues[3] = "" + ngasBoilerFuelDemand;
		modifyMap.put("ngasBoilerFuelDemand", ngasBoilerFuelDemand);

		double nGasBoilerSolarThermal = ngasBoilerFuelDemand * nGasBoilerSolarPercentage;
		cooresPondingValues[4] = "" + nGasBoilerSolarThermal;
		totalSolarThermalInput+=nGasBoilerSolarThermal;
		modifyMap.put("NGasBoilerSolarInput", nGasBoilerSolarThermal);

		double nGasCHPFuelDemand = nGasCHPHeatPercentage
				* totalHeatDemand / nGasCHPThermalEfficiency;
		cooresPondingValues[5] = "" + nGasCHPFuelDemand;
		modifyMap.put("NGasCHPFuelDemand", nGasCHPFuelDemand);

		double nGasCHPSolarThermal = nGasCHPFuelDemand * nGasBoilerSolarPercentage;
		cooresPondingValues[6] = "" + nGasCHPSolarThermal;
		totalSolarThermalInput+=nGasCHPSolarThermal;
		modifyMap.put("NGasCHPSolarInput", nGasCHPSolarThermal);
		
		
		double biomassBoilerFuelDemand = biomassBoilerHeatPercentage
				* totalHeatDemand / biomassBoilerEfficiency;
		cooresPondingValues[7] = "" + biomassBoilerFuelDemand;
		modifyMap.put("biomassBoilerFuelDemand", biomassBoilerFuelDemand);

		double biomassBoilerSolarThermal = biomassBoilerFuelDemand
				* biomassBoilerSolarPercentage;
		cooresPondingValues[8] = "" + biomassBoilerSolarThermal;
		totalSolarThermalInput+=biomassBoilerSolarThermal;
		modifyMap.put("BiomassBoilerSolarInput", biomassBoilerSolarThermal);

		double baiomassCHPHeatDemand = biomassCHPHeatPercentage
				* totalHeatDemand;
		cooresPondingValues[9] = "" + baiomassCHPHeatDemand;

		double biomassCHPSolarThermal = baiomassCHPHeatDemand * biomassCHPThermalEfficiency *
				 biomassCHPSolarPercentage;
		cooresPondingValues[10] = "" + biomassCHPSolarThermal;
		totalSolarThermalInput+=biomassCHPSolarThermal;
		modifyMap.put("BiomassCHPSolarInput", biomassCHPSolarThermal);

		double hpHEatDemand = hpHeatPercentage * totalHeatDemand;
		cooresPondingValues[11] = "" + hpHEatDemand;

		double hpSolarThermal = hpHeatPercentage * totalHeatDemand
				* hpSolarPercentage;
		cooresPondingValues[12] = "" + hpSolarThermal;
		totalSolarThermalInput+=hpSolarThermal;
		modifyMap.put("HPSolarInput", hpSolarThermal);
		
		modifyMap.put("SolarThermal", totalSolarThermalInput);
		
		cooresPondingValues[13] = "" + totalPetrolDemandInTWhForTrns;
		modifyMap.put("transOilDemand", totalPetrolDemandInTWhForTrns);
		

		//"NumberOfConCars", "NumberOfEVCars","NumberOfFCEVCars"
		
		double numberOfConCars = (totalPetrolDemandInTWhForTrns * Math.pow(10,9))
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
				 
		
		double numberOfFCEVCars = totalH2DemandInTWhForTrns
				* Math.pow(10, 9)
				/ (efficiencyFCEVCar * averageKMPerYearPerCar * Math.pow(10, 3));
		cooresPondingValues[18] = "" + numberOfFCEVCars;
		modifyMap.put("NumberOfFCEVCars", numberOfFCEVCars);
		
		int electrolyzerCapacity = (int) Math.floor(maxH2DemandInDistribution
				* totalH2DemandInTWhForTrns * Math.pow(10, 6)
				/ (efficiencyElectrolyzerTrans * sumH2DemandInDistribution))+1;
		cooresPondingValues[19] = "" + electrolyzerCapacity;
		modifyMap.put("Eloctrolyzer_Cap", electrolyzerCapacity);

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
		
		return modifyMap;
	}
	
	ArrayList<Double> thermalDeamndProfile = new ArrayList();
	ArrayList<Double> solarThermalProductionProfile = new ArrayList();
	Double sumOfThermalDemandProfile;
	Double sumOfSolarThermalProductionProfile;
	
	void readProfiles() throws IOException{
		
		BufferedReader brthermalDeamndProfile = new BufferedReader(new FileReader(".\\EnergyPLAN_12.3\\energyPlan Data\\Distributions\\PEAP PAT indiv heat demand profile.txt"));
		BufferedReader brsolarThermalProductionProfile = new BufferedReader(new FileReader(".\\EnergyPLAN_12.3\\energyPlan Data\\Distributions\\PEAP PAT solar thermal prod profile.txt"));
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
