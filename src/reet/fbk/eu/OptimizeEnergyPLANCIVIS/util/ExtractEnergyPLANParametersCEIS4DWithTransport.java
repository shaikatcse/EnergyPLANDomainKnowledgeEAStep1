package reet.fbk.eu.OptimizeEnergyPLANCIVIS.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
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

public class ExtractEnergyPLANParametersCEIS4DWithTransport {

	// static MultiMap energyplanmMap;

	static String inputs[] = { "PV_Cap", "Biomass_CHP_Cap", "HP_Cap",
			"Oil_boiler_Cap", "LPG_boiler_Cap", "Biomass_boiler_Cap", "NumberOfPetrolCars", "NumberOfDieselCars", "NumberOfElectricCars" };
	static String outputsinEnergyPLANMap[] = { "AnnualPV", "AnnualHH-elec.CHP",
			"AnnualHH-elec.HP", "Annualflexibleeldemand", "AnnualOilBoilerheat", "AnnualLPGBoilerheat",
			"AnnualBiomassBoilerheat", "AnnualmCHPheat", "AnnualHPheat",
			"Annualimport", "Annualexport", "Oil Consumption",
			"Biomass Consumption", "Ngas Consumption", "Gasoil/Diesel",
			"Petrol/JP", "Biomass", "Total Electricity exchange",
			"Total variable costs", "Fixed operation costs", "AdditionalCost","ElecCarInvestmentCost",
			"InvestmentCost", "CO2-emission (corrected)", "AnnualCost",
			"LoadFollowingCapacity", "ESD", 
			"oilBoilerFuelDemand", "ngasBoilerFuelDemand", "biomassBoilerFuelDemand", "AnnualmCHPheat", "AnnualHPheat", "reducedPetrolDemand", "reducedDieselDemand" };

	static String outputsinFile[] = { "AnnualPV", "AnnualCHPelec",
			"AnnualHPelec", "AnnualElecCar", "AnnualOilBoilerheat", "AnnualLPGBoilerheat",
			"AnnualBiomassBoilerheat", "AnnualmCHPheat", "AnnualHPheat",
			"AnnualImport", "AnnualExport", "OilConsumption",
			"BiomassConsumption", "LPGConsuption", "DieselCost", "PetrolCost",
			"BiomassCost", "TotalElectricityExchangeCost", "TotalVariableCost",
			"FixedOperationCosts", "AdditionalCost", "ElecCarInvestmentCost", "InvestmentCost",
			"CO2-Emission", "AnnualCost", "LoadFollowingCapacity", "ESD",
			"oilBoilerFuelDemand", "LPGBoilerFuelDemand", "biomassBoilerFuelDemand", "AnnualmCHPheat", "AnnualHPheat", "reducedPetrolDemand", "reducedDieselDemand"};

	static String inputUnits[] = { "KWe", "KWe", "KWe", "KWth", "KWth", "KWth", " ", " ", " " };
	static String outputUnits[] = { "GWh", "GWh", "GWh", "GWh", "GWh", "GWh", "GWh",
			"GWh", "GWh", "GWh", "GWh", "GWh", "GWh", "GWh", "KEuro", "KEuro",
			"KEuro", "KEuro", "KEuro", "KEuro", "KEuro", "KEuro","KEuro", "Mt",
			"KEuro", "", "", "GWh","GWh","GWh","GWh","GWh","GWh","GWh"  };

	public static final double PVInvestmentCostInKEuro = 2.6; 
	public static final double hydroInvestmentCostInKEuro = 1.9;
	public static final double individualBoilerInvestmentCostInKEuro = 0.588; 
	public static final double BiogasInvestmentCostInKEuro = 4.0;
	public static final double interest = 0.04;

	public static final double currentPVCapacity = 7514;
	public static final double currentHydroCapacity = 4000;
	public static final double currentBiogasCapacity = 500;
	public static final double currentIndvBiomassBoilerCapacity = 14306;
	public static final double currentIndvOilBoilerCapacity = 9155;
	public static final double currentIndvLPGBoilerCapacity = 3431;

	public static final double totalHeatDemand = 55.82;

	public static final double boilerLifeTime = 15;
	public static final double PVLifeTime = 30;
	public static final double HydroLifeTime = 50;
	public static final double BiogasLifeTime = 20;
	public static final double geoBoreHoleLifeTime = 100;

	public static final double COP = 3.2;

	public static final double maxHeatDemandInDistribution = 1.0;
	public static final double sumOfAllHeatDistributions = 3112.94;

	public static final double geoBoreholeCostInKWe = 3.2;

	public static final double oilBoilerEfficiency = 0.80;
	public static final double ngasBoilerEfficiency = 0.90;
	public static final double biomassBoilerEfficiency = 0.75;

	public static final double addtionalCostPerGWhinKEuro = 106.27;
	
	//Transport related data
	public static final int currentNumberOfPertrolCars  = 2762; 
	public static final int currentNumberOfDieselCars  = 2094;
	public static final int averageKMPerYearForPetrolCar = 7250;
	public static final int averageKMPerYearForDieselCar = 13400;
	// lower calorific value (LCV): KWh/l (ref: http://www.withouthotair.com/c3/page_31.shtml) check with Diego
	public static final double LCVPetrol = 8.86;
	public static final double LCVDiesel = 10.12;
	public static final double KWhPerKMElecCar = 0.168;
	public static final double petrolCarRunsKMperL = 15.5;
	public static final double DieselCarRunsKMperL = 18.2;
	public static final int totalKMRunByCars = 48084100;
	public static final double costOfElectricCarInKeuro = 18.690;
	public static final int electricCarLifeTime = 15;
	public static final double electricCarOperationalAndMaintanenceCost = 0.055; //5.5 percent of Investment cost (costOfElectricCarInKeuro) 
	

	public ExtractEnergyPLANParametersCEIS4DWithTransport() {
		// TODO Auto-generated constructor stub

	}

	public static void main(String[] args) throws IOException, JMException {

		MultiMap energyplanmMap = null;
		// TODO Auto-generated method stub
		ExtractEnergyPLANParametersCEIS4DWithTransport ob = new ExtractEnergyPLANParametersCEIS4DWithTransport();
		FileInputStream fos = new FileInputStream(args[0]);
		InputStreamReader isr = new InputStreamReader(fos);
		BufferedReader br = new BufferedReader(isr);

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

				// index - 1 -> oil boiler heat percentage
				// index - 2 -> LPG boiler heat percentage
				// index - 3 -> Biomass boiler heat percentage
				// index - 4 -> Biomass micro chp heat percentage
				// last percentage will go to individual HP percentage
				// Oil-boiler heat percentage
				// index - 5 -> electric car percentage  
				
				double percentages[] = new double[4];
				for (int i = 0; i < 4; i++) {
					percentages[i] = Double.parseDouble(st.nextToken().toString());
				}

				Arrays.sort(percentages);

				// oil-boiler percentage
				double oilBoilerHeatPercentage = percentages[0];
				// LPG-boiler heat percentage
				double LPGBoilerHeatPercentage = percentages[1] - percentages[0];
				// biomass-boiler heat percentage
				double biomassBoilerHeatPercentage = percentages[2] - percentages[1];
				// biomass chp heat percentage
				double biomassCHPHeatPercentage = percentages[3] - percentages[2];

				// heat pump heat percentage
				double hpHeatPercentage = 1.0 - percentages[3];
				
				//electric car percentage
				double electricCarPercentage = Double.parseDouble(st.nextToken().toString());
				int reducedNumberOfPetrolCars = (int) Math.round(currentNumberOfPertrolCars - currentNumberOfPertrolCars * electricCarPercentage);
				int reducedNumberOfDieselCars = (int) Math.round(currentNumberOfDieselCars - currentNumberOfDieselCars * electricCarPercentage);
				double reducedPetrolDemandInGWh = reducedNumberOfPetrolCars * averageKMPerYearForPetrolCar * LCVPetrol / (petrolCarRunsKMperL*1000000);
				double reducedDieselDemandInGWh = reducedNumberOfDieselCars * averageKMPerYearForDieselCar * LCVDiesel / (DieselCarRunsKMperL*1000000);
				
				int elecCarRunKM = totalKMRunByCars - (reducedNumberOfPetrolCars * averageKMPerYearForPetrolCar) - (reducedNumberOfDieselCars * averageKMPerYearForDieselCar);
				double elecCarElectricityDemandInGWh =  elecCarRunKM * KWhPerKMElecCar / 1000000;
				double totalNumberOfElectricCars =  (int) Math.round(currentNumberOfPertrolCars + currentNumberOfDieselCars - reducedNumberOfPetrolCars - reducedNumberOfDieselCars); 

				
		MultiMap energyplanMap = null;
		MultiMap modifyMap = new MultiValueMap();
		modifyMap = writeModificationFile(pv, oilBoilerHeatPercentage,LPGBoilerHeatPercentage, biomassBoilerHeatPercentage,biomassCHPHeatPercentage,
				 hpHeatPercentage,  reducedPetrolDemandInGWh,  reducedDieselDemandInGWh, elecCarElectricityDemandInGWh, "NC");

		String energyPLANrunCommand = ".\\EnergyPLAN_SEP_2013\\EnergyPLAN.exe -i "
				+ "\".\\src\\reet\\fbk\\eu\\OptimizeEnergyPLANCIVIS\\CEIS\\data\\CEIS_Complete_Current.txt\" "
				+ "-m \"modification.txt\" -ascii \"result.txt\" ";
		try {
			// Process process = new
			// ProcessBuilder(energyPLANrunCommand).start();
			Process process = Runtime.getRuntime().exec(energyPLANrunCommand);
			process.waitFor();
			process.destroy();
			EnergyPLANFileParseForCivis epfp = new EnergyPLANFileParseForCivis(
					".\\result.txt");
			energyplanMap = epfp.parseFile();

			energyplanMap.putAll(modifyMap);
			
			
			//put transport information
			DecimalFormat df = new DecimalFormat("#");
	        df.setMaximumFractionDigits(2);
			
			energyplanMap.put("NumberOfPetrolCars", reducedNumberOfPetrolCars);
			energyplanMap.put("NumberOfDieselCars", reducedNumberOfDieselCars);
			energyplanMap.put("NumberOfElectricCars", totalNumberOfElectricCars);
			energyplanMap.put("reducedPetrolDemand", df.format(reducedPetrolDemandInGWh));
			energyplanMap.put("reducedDieselDemand", df.format(reducedDieselDemandInGWh));
			
			Iterator it;
			Collection<String> col;

			// objective # 2
			col = (Collection<String>) energyplanMap
					.get("Total variable costs");
			it = col.iterator();
			String totalVariableCostStr = it.next().toString();
			totalVariableCostStr = totalVariableCostStr.substring(0,
					totalVariableCostStr.lastIndexOf("1000"));
			double totalVariableCost = Double.parseDouble(totalVariableCostStr);

			col = (Collection<String>) energyplanMap
					.get("Fixed operation costs");
			it = col.iterator();
			String fixedOperationalCostStr = it.next().toString();
			fixedOperationalCostStr = fixedOperationalCostStr.substring(0,
					fixedOperationalCostStr.lastIndexOf("1000"));
			double fixedOperationalCost = Double
					.parseDouble(fixedOperationalCostStr);

			col = (Collection<String>) energyplanMap.get("AnnualHydropower");
			it = col.iterator();
			double hydroPowerProduction = Double.parseDouble(it.next()
					.toString());
			// extract anual PV production
			col = (Collection<String>) energyplanMap.get("AnnualPV");
			it = col.iterator();
			double PVproduction = Double.parseDouble(it.next().toString());

			// extract annual import
			col = (Collection<String>) energyplanMap.get("Annualimport");
			it = col.iterator();
			double Import = Double.parseDouble(it.next().toString());

			// extract annual export
			col = (Collection<String>) energyplanMap.get("Annualexport");
			it = col.iterator();
			double Export = Double.parseDouble(it.next().toString());
			
			//extract biomass CHP electricity production
			col = (Collection<String>) energyplanMap.get("AnnualHH-elec.CHP");
			it = col.iterator();
			double biomassCHPElecProduction = Double.parseDouble(it.next().toString());


			// calculate additional cost
			// (hydroProduction+PVproduction+Import-Export)*average additional
			// cost (85.74)
			double totalAdditionalCost = Math.round((hydroPowerProduction
					+ PVproduction + Import - Export + biomassCHPElecProduction)
					* addtionalCostPerGWhinKEuro);		

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

			double newCapacityLPGBoiler = Math
					.round((totalHeatDemand * LPGBoilerHeatPercentage)
							* Math.pow(10, 6) * 1.5 / sumOfAllHeatDistributions);
			double investmentCostReductionLPGBoiler = 0.0;
			if (newCapacityLPGBoiler > currentIndvLPGBoilerCapacity) {
				investmentCostReductionLPGBoiler = (currentIndvLPGBoilerCapacity
						* individualBoilerInvestmentCostInKEuro * interest)
						/ (1 - Math.pow((1 + interest), -boilerLifeTime));
			} else {
				investmentCostReductionLPGBoiler = (newCapacityLPGBoiler
						* individualBoilerInvestmentCostInKEuro * interest)
						/ (1 - Math.pow((1 + interest), -boilerLifeTime));
			}

			double reductionInvestmentCost = (currentPVCapacity
					* PVInvestmentCostInKEuro * interest)
					/ (1 - Math.pow((1 + interest), -PVLifeTime))
					+ (currentHydroCapacity * hydroInvestmentCostInKEuro * interest)
					/ (1 - Math.pow((1 + interest), -HydroLifeTime))
					+ (currentBiogasCapacity * BiogasInvestmentCostInKEuro * interest)
					/ (1 - Math.pow((1 + interest), -BiogasLifeTime))
					+ investmentCostReductionBiomassBoiler
					+ investmentCostReductionOilBoiler
					+ investmentCostReductionLPGBoiler;
			
			
					
			// extract
			col = (Collection<String>) energyplanMap
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
			energyplanMap.put("ElecCarInvestmentCost",Math.round(totalInvestmentCostOfElectricCars * 100.0) / 100.0 );
			
			double actualAnnualCost = totalVariableCost + fixedOperationalCost
					+ realInvestmentCost + totalAdditionalCost + totalInvestmentCostOfElectricCars + totalFixOperationalAndInvestmentCostOfElectricCars;
			
			
		
			col = (Collection<String>) energyplanMap.get("Annualelec.demand");
			it = col.iterator();
			double annualElecDemand = Double.parseDouble(it.next().toString());
			
			//Transport annual electricity demand
			col = (Collection<String>) energyplanMap
					.get("Annualflexibleeldemand");
			it = col.iterator();
			double transportElecDemand = Double.parseDouble(it.next().toString());

			//Individual house HP electric demand
			col = (Collection<String>) energyplanMap.get("AnnualHH-elec.HP");
			it = col.iterator();
			double annualHPdemand = Double.parseDouble(it.next().toString());
			
			energyplanMap.put("AdditionalCost", totalAdditionalCost);
			energyplanMap.put("InvestmentCost", realInvestmentCost);
			energyplanMap.put("AnnualCost", actualAnnualCost);
			energyplanMap
					.put("LoadFollowingCapacity", (Import + Export) / (annualElecDemand+transportElecDemand +annualHPdemand));

			//extract ngas consuption
			col = (Collection<String>) energyplanMap
					.get("Ngas Consumption");
			it = col.iterator();
			double nGasConsumption = Double.parseDouble(it.next().toString());
			
			//extract oil consumption
			col = (Collection<String>) energyplanMap
					.get("Oil Consumption");
			it = col.iterator();
			double oilConsumption = Double.parseDouble(it.next().toString());
			
			//extract biomass consupmtion
			col = (Collection<String>) energyplanMap.get("Biomass Consumption");
			it = col.iterator();
			double BiomassConsumption = Double.parseDouble(it.next().toString());
			
			//extract biogas production (it is named as wave power)
			col = (Collection<String>) energyplanMap.get("AnnualWavepower");
			it = col.iterator();
			double BiogasElecProduction = Double.parseDouble(it.next()
					.toString());
			
			
			double PVPEF = 1.0;
			double HYPEF = 1.0;
			double BioGasPEF = 1/0.262;
			double BiomassPEF = 1/0.18;
			double PEFImport = 2.17; 
			
			double totalPEForElecrcity = PVproduction * PVPEF + hydroPowerProduction * HYPEF + BiogasElecProduction*BioGasPEF + biomassCHPElecProduction * BiomassPEF;
			double totalLocalElecProduction = PVproduction + hydroPowerProduction + BiogasElecProduction+biomassCHPElecProduction;
			double PEFLocalElec = totalPEForElecrcity/ totalLocalElecProduction;
			
			double totalPEConsumtion = (totalLocalElecProduction - Export) * PEFLocalElec + Import * PEFImport + BiomassConsumption + oilConsumption + nGasConsumption
					+ (totalHeatDemand* hpHeatPercentage - totalHeatDemand* hpHeatPercentage/COP);
			
			double ESD = (Import * PEFImport + oilConsumption + nGasConsumption)/totalPEConsumtion;
			
			energyplanMap.put("ESD", ESD);
			
		} catch (IOException e) {
			System.out.println("Energyplan.exe has some problem");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Energyplan interrupted");
		}

		return energyplanMap;

	}

	public MultiMap writeModificationFile(double pv, double oilBoilerHeatPercentage,
			double LPGBoilerHeatPercentage,
			double biomassBoilerHeatPercentage, double biomassCHPHeatPercentage,
			double hpHeatPercentage, double reducedPetrolDemand, double reducedDieselDemand, double elecCarDemand, String elecChangeProfile) throws JMException {

		MultiMap modifyMap = new MultiValueMap();

		double totalOilBoilerHeat = oilBoilerHeatPercentage * totalHeatDemand;
		double totalLPGBoilerHeat = LPGBoilerHeatPercentage * totalHeatDemand;
		double totalBiomassBoilerHeat = biomassBoilerHeatPercentage
				* totalHeatDemand;
		double totalnGasCHPHeat = biomassCHPHeatPercentage * totalHeatDemand;
		double totalHPHeat = hpHeatPercentage * totalHeatDemand;

		modifyMap.put("AnnualOilBoilerheat",
				Math.round(totalOilBoilerHeat * 100.0) / 100.0); //this is done for 2 decimal place precision
		modifyMap.put("AnnualLPGBoilerheat",
				Math.round(totalLPGBoilerHeat * 100.0) / 100.0);
		modifyMap.put("AnnualBiomassBoilerheat",
				Math.round(totalBiomassBoilerHeat * 100.0) / 100.0);
		modifyMap.put("AnnualmCHPheat",
				Math.round(totalnGasCHPHeat * 100.0) / 100.0);
		modifyMap.put("AnnualHPheat", Math.round(totalHPHeat * 100.0) / 100.0);

		double oilBCap = totalOilBoilerHeat * Math.pow(10, 6) * 1.5
				/ sumOfAllHeatDistributions;
		double LPGBCap = totalLPGBoilerHeat * Math.pow(10, 6) * 1.5
				/ sumOfAllHeatDistributions;
		double BiomassBCap = totalBiomassBoilerHeat * Math.pow(10, 6) * 1.5
				/ sumOfAllHeatDistributions;
		double biomassCHPCap = totalnGasCHPHeat * Math.pow(10, 6)
				/ sumOfAllHeatDistributions;
		double HPCap = totalHPHeat * Math.pow(10, 6)
				/ (COP * sumOfAllHeatDistributions);

		modifyMap.put(inputs[0], (int) Math.round(pv));
		modifyMap.put(inputs[1], (int) Math.round(biomassCHPCap));
		modifyMap.put(inputs[2], (int) Math.round(HPCap));
		modifyMap.put(inputs[3], (int) Math.round(oilBCap));
		modifyMap.put(inputs[4], (int) Math.round(LPGBCap));
		modifyMap.put(inputs[5], (int) Math.round(BiomassBCap));

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
			modifyMap.put("oilBoilerFuelDemand", oilBoilerFuelDemand);

			// Ngas boiler fuel demand
			str = "input_fuel_Households[3]=";
			bw.write(str);
			bw.newLine();
			double ngasBoilerFuelDemand = LPGBoilerHeatPercentage
					* totalHeatDemand / ngasBoilerEfficiency;
			str = "" + ngasBoilerFuelDemand;
			bw.write(str);
			bw.newLine();
			modifyMap.put("ngasBoilerFuelDemand", ngasBoilerFuelDemand);

			// biomass boiler fuel demand
			str = "input_fuel_Households[4]=";
			bw.write(str);
			bw.newLine();
			double biomassBoilerFuelDemand = biomassBoilerHeatPercentage
					* totalHeatDemand / biomassBoilerEfficiency;
			str = "" + biomassBoilerFuelDemand;
			bw.write(str);
			bw.newLine();
			modifyMap.put("biomassBoilerFuelDemand", biomassBoilerFuelDemand);

			// biomass micro chp heat demand
			str = "input_HH_BioCHP_heat=";
			bw.write(str);
			bw.newLine();
			str = "" + biomassCHPHeatPercentage * totalHeatDemand;
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
				throw new JMException("chrging profile not given");
			}

			bw.close();
			// file.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return modifyMap;
	}
}
