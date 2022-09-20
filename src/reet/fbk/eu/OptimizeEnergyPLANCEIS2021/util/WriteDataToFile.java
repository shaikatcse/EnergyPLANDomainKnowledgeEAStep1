package reet.fbk.eu.OptimizeEnergyPLANCEIS2021.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.MultiMap;

class EnergyPLANOutput {
	String technicalKeywordInMap, keywordForOutput, unit;

	public EnergyPLANOutput(String technicalKeywordInMap, String keywordForOutput, String unit) {
		super();
		this.technicalKeywordInMap = technicalKeywordInMap;
		this.keywordForOutput = keywordForOutput;
		this.unit = unit;
	}

	public String getTechnicalKeywordInMap() {
		return technicalKeywordInMap;
	}

	public String getKeywordForOutput() {
		return keywordForOutput;
	}

	public String getUnit() {
		return unit;
	}

}

public class WriteDataToFile {

	public static ArrayList<EnergyPLANOutput> output = new ArrayList();

	public WriteDataToFile() {

	}

	/*
	 * static String inputs[] = { "PV_Cap", "WavePower_Cap", /*"Biomass_CHP_Cap",
	 * "NGas_CHP_Cap", "HP_Cap", "Oil_boiler_Cap", "NGas_boiler_Cap",
	 * "Biomass_boiler_Cap", "SolarThermal",
	 */
	/*
	 * "NumberOfDieselCars", "NumberOfPetrolCars", "NumberOfEVCars",
	 * "IndvEloctrolyzer_Cap", "H2FeedStock", "FeedStockElectrolyzer_Cap",
	 * "FeedStockH2(imp)", "ElecStorage_Turbine_Cap","ElecStorage_Pump_Cap",
	 * "ElecStorage_Storage_Cap", "WindPower_Cap", "TidalPower_Cap",
	 * "H2IndvStorage", "H2TransStorage" };
	 * 
	 * static String outputsinEnergyPLANMap[] = { "AnnualPVElectr.",
	 * "AnnualWindElectr.", "AnnualWaveElectr.", "AnnualTidalElectr.",
	 * "AnnualHH-CHPElectr.", "AnnualHH-HPElectr.", "AnnualFlexibleElectr.",
	 * "AnnualH2demand", "AnnualOilBoilerheat", "AnnualNGasBoilerheat",
	 * "AnnualBiomassBoilerheat", "AnnualNgasmCHPheat", "AnnualElecHeatingheat",
	 * "AnnualH2mCHPheat", "AnnualHH SolarHeat", "AnnualImportElectr.",
	 * "AnnualExportElectr.", "Oil Consumption", "Biomass Consumption",
	 * "Ngas Consumption", /*"Gasoil/Diesel", "Biomass",
	 */ /*
		 * "Bottleneck", "Electricity exchange", "Variable costs",
		 * "Fixed operation costs", "AdditionalCost",
		 * //"ConCarInvestmentCost","EVCarInvestmentCost","FCEVCarInvestmentCost",
		 * "Annual Investment costs", "CO2Emission", "AnnualCost",
		 * "oilBoilerFuelDemand", "ngasBoilerFuelDemand", "biomassBoilerFuelDemand",
		 * "AnnualNgasmCHPheat", "AnnualH2mCHPheat", "AnnualBiomassmCHPheat",
		 * "AnnualHPheat", "AnnualHH SolarHeat", "transOilDemand",
		 * "OilBoilerSolarInput", "NGasBoilerSolarInput","NGasCHPSolarInput",
		 * "BiomassBoilerSolarInput", "H2CHPSolarInput", "ElecHeatingSolarInput",
		 * "OilBoilerUtilization", "NGasBoilerUtilization",
		 * "BiomassBoilersolarUtilization", "NgasCHPSolarUtilization",
		 * "H2CHPSolarUtilization","ElecHeatingSolarUtilization",
		 * "Annual MaximumImportElectr.", "Annual MaximumExportElectr."};
		 * 
		 * 
		 * static String outputsinFile[] = { "AnnualPV", "AnnualWind", "AnnualWave",
		 * "AnnualTidal", "AnnualCHP ", "AnnualHPelec", "AnnualElecCar",
		 * "AnnualH2demand", "AnnualOilBoilerheat", "AnnualNGasBoilerheat",
		 * "AnnualBiomassBoilerheat", "AnnualNgasmCHPheat", "AnnualElecHeatingheat",
		 * "AnnualH2mCHPheat", "AnnualSolarThermalheat", "AnnualImport", "AnnualExport",
		 * "OilConsumption", "BiomassConsumption", "NGasConsuption", /*"PetrolCost",
		 * "BiomassCost",
		 */ /*
			 * "Bottleneck", "TotalElectricityExchangeCost", "TotalVariableCost",
			 * "FixedOperationCosts", "AdditionalCost",
			 * //"ConCarInvestmentCost","EVCarInvestmentCost","FCEVInvestmentCost",
			 * "InvestmentCost","CO2-Emission", "AnnualCost", "oilBoilerFuelDemand",
			 * "NGasBoilerFuelDemand", "biomassBoilerFuelDemand", "AnnualNgasmCHPheat",
			 * "AnnualH2mCHPheat","AnnualBiomassmCHPheat", "AnnualHPheat",
			 * "AnnualSolarThermal", "transOilDemand", "OilBoilerSolarInput",
			 * "NGasBoilerSolarInput", "NGasCHPSolarInput", "BiomassBoilerSolarInput",
			 * "H2CHPSolarInput", "ElecHeatingSolarInput", "OilBoilerUtilization",
			 * "NGasBoilerUtilization", "BiomassBoilersolarUtilization",
			 * "NgasCHPSolarUtilization",
			 * "H2CHPSolarUtilization","ElecHeatingSolarUtilization",
			 * "Annual MaximumImportElectr.", "Annual MaximumExportElectr."
			 * 
			 * };
			 * 
			 * static String inputUnits[] = { "MWe", "MWe", /*"MWe", "MWe", "MWth", "MWth",
			 * "MWth", "TWh",
			 */ /*
				 * "1000 units", "1000 units", "1000 units", "MWe", "MWh","MWe", "MWh", "MW",
				 * "MW", "GWh","MW","MW", "GWh", "Gwh" }; static String outputUnits[] = { "TWh",
				 * "TWh", "TWh","TWh","TWh",// "AnnualPV", "AnnualCHPelec", "TWh", "TWh", "TWh",
				 * "TWh", "TWh", //"AnnualHPelec", "AnnualElecCar", "AnnualH2demand",
				 * "AnnualOilBoilerheat", "AnnualNGasBoilerheat", "TWh", "TWh", "TWh","TWh",
				 * "TWh", "TWh", //"AnnualBiomassBoilerheat", "AnnualNgasmCHPheat",
				 * "AnnualBiomassmCHPheat", "AnnualHPheat", "AnnualSolarThermalheat", "TWh",
				 * "TWh", "TWh", //"AnnualImport", "AnnualExport", "OilConsumption", "TWh",
				 * "TWh", //"BiomassConsumption", "NGasConsuption", /*"MEuro", //"DieselCost",
				 * "MEuro",
				 */ /*
					 * "MEuro", //"BiomassCost", "TotalElectricityExchangeCost", "MEuro", "MEuro",
					 * "MEuro", //"TotalVariableCost", "FixedOperationCosts", "AdditionalCost",
					 * //"KEuro", "KEuro", "KEuro",
					 * //"ConCarInvestmentCost","EVCarInvestmentCost","FCEVInvestmentCost", "MEuro",
					 * "Mt", "MEuro", //"InvestmentCost","CO2-Emission",
					 * "Total Local System Emission", "AnnualCost",
					 * "TWh","TWh","TWh","TWh","TWh","TWh","TWh", //"oilBoilerFuelDemand",
					 * "NGasBoilerFuelDemand", "biomassBoilerFuelDemand", "AnnualNgasmCHPheat",
					 * "AnnualBiomassmCHPheat, "AnnualHPheat", "AnnualSolarThermal", "TWh",
					 * //"transOilDemand" "TWh","TWh","TWh", "TWh","TWh","TWh",
					 * //"OilBoilerSolatInput", "NGasBoilerSolarInput","NGasCHPSolarInput",
					 * "BiomassBoilerSolarInput", "BiomassCHPSolarInput", "HPSolarInput"
					 * "TWh","TWh","TWh", "TWh","TWh","TWh", //"OilBoilerUtilization",
					 * "NGasBoilerUtilization", "biomassBoilerSolarUtilization",
					 * "biomassCHPSolarUtilization", "HPSolarUtilization" "MW", "MW" };
					 */

	static public void init() {
		
		output.add(new EnergyPLANOutput("PVCap", "PVCap", "kWe"));
		output.add(new EnergyPLANOutput("RiverHydroCap", "RiverHydroCap", "kWe"));
		output.add(new EnergyPLANOutput("BioGasCap", "BioGasCap", "kWe"));
		
		output.add(new EnergyPLANOutput("ElecStorage_Turbine_Cap", "ElecStorage_Turbine_Cap", "KWe"));
		output.add(new EnergyPLANOutput("ElecStorage_Pump_Cap", "ElecStorage_Pump_Cap", "KWe"));
		output.add(new EnergyPLANOutput("ElecStorage_Storage_Cap", "ElecStorage_Storage_Cap", "MWh"));
		
		output.add(new EnergyPLANOutput("H2Storage", "H2Storage", "MWh"));
		
		output.add(new EnergyPLANOutput("P2PH2Electrolyzer", "P2PH2Electrolyzer", "KWe"));
		output.add(new EnergyPLANOutput("P2PH2FuelCell", "P2PH2FuelCell", "KWe"));
		output.add(new EnergyPLANOutput("P2PH2Storage", "P2PH2Storage", "MWh"));
		
		output.add(new EnergyPLANOutput("coalBoilerHeatDemand", "coalBoilerHeatDemand", "GWh"));
		output.add(new EnergyPLANOutput("oilBoilerHeatDemand", "oilBoilerHeatDemand", "GWh"));
		output.add(new EnergyPLANOutput("nGasBoilerHeatDemand", "nGasBoilerHeatDemand", "GWh"));
		output.add(new EnergyPLANOutput("biomassBoilerHeatDemand", "biomassBoilerHeatDemand", "GWh"));
		output.add(new EnergyPLANOutput("h2mCHPHeatDemand", "h2mCHPHeatDemand", "GWh"));
		output.add(new EnergyPLANOutput("nGasmCHPHeatDemand", "nGasmCHPHeatDemand", "GWh"));
		output.add(new EnergyPLANOutput("biomassmCHPHeatDemand", "biomassmCHPHeatDemand", "GWh"));
		output.add(new EnergyPLANOutput("hpHeatDemand", "hpHeatDemand", "GWh"));
									
		output.add(new EnergyPLANOutput("coalBoilerSolarInput", "coalBoilerSolarInput", "GWh"));
		output.add(new EnergyPLANOutput("oilBoilerSolarInput", "oilBoilerSolarInput", "GWh"));
		output.add(new EnergyPLANOutput("nGasBoilerSolarInput", "nGasBoilerSolarInput", "GWh"));
		output.add(new EnergyPLANOutput("biomassBoilerSolarInput", "biomassBoilerSolarInput", "GWh"));
		output.add(new EnergyPLANOutput("h2mCHPSolarInput", "h2mCHPSolarInput", "GWh"));
		output.add(new EnergyPLANOutput("nGasmCHPSolarInput", "nGasmCHPSolarInput", "GWh"));
		output.add(new EnergyPLANOutput("biomassmCHPSolarInput", "biomassmCHPSolarInput", "GWh"));
		output.add(new EnergyPLANOutput("hpSolarInput", "hpSolarInput", "GWh"));
		
		output.add(new EnergyPLANOutput("coalBoilerSolarStorage", "coalBoilerSolarStorage", "days"));
		output.add(new EnergyPLANOutput("oilBoilerSolarStorage", "oilBoilerSolarStorage", "days"));
		output.add(new EnergyPLANOutput("nGasBoilerSolarStorage", "nGasBoilerSolarStorage", "days"));
		output.add(new EnergyPLANOutput("biomassBoilerSolarStorage", "biomassBoilerSolarStorage", "days"));
		output.add(new EnergyPLANOutput("h2mCHPSolarStorage", "h2mCHPSolarStorage", "days"));
		output.add(new EnergyPLANOutput("nGasmCHPSolarStorage", "nGasmCHPSolarStorage", "days"));
		output.add(new EnergyPLANOutput("biomassmCHPSolarStorage", "biomassmCHPSolarStorage", "days"));
		output.add(new EnergyPLANOutput("hpSolarStorage", "hpSolarStorage", "days"));
		
		
		output.add(new EnergyPLANOutput("dieselCarDemandInKM", "dieselCarDemandInKM", "KM"));
		output.add(new EnergyPLANOutput("dieselCarDemandInGWh", "dieselCarDemandInGWh", "GWh"));	
		output.add(new EnergyPLANOutput("NumberOfDieselCars", "NumberOfDieselCars", "1 unit"));
		
		output.add(new EnergyPLANOutput("elCarDemandInKM", "elCarDemandInKM", "KM"));
		output.add(new EnergyPLANOutput("elCarDemandInGWh", "elCarDemandInGWh", "GWh"));	
		output.add(new EnergyPLANOutput("NumberOfElCars", "NumberOfElCars", "1 unit"));
		
		output.add(new EnergyPLANOutput("h2CarDemandInKM", "h2CarDemandInKM", "KM"));
		output.add(new EnergyPLANOutput("h2CarDemandInGWh", "h2CarDemandInGWh", "GWh"));	
		output.add(new EnergyPLANOutput("NumberOfH2Cars", "NumberOfH2Cars", "1 unit"));
		
			
		output.add(new EnergyPLANOutput("ElectrolyzerCapacity", "ElectrolyzerCapacity", "kWe"));
			
		
		output.add(new EnergyPLANOutput("AnnualPVElectr.", "AnnualPV", "GWh"));
		output.add(new EnergyPLANOutput("AnnualWindElectr.", "AnnualBiogas", "GWh"));
		output.add(new EnergyPLANOutput("AnnualRiverElectr.", "AnnualRiverHydro", "GWh"));
		output.add(new EnergyPLANOutput("AnnualPumpElectr.", "AnnualPumpElectr.", "GWh"));
		output.add(new EnergyPLANOutput("AnnualTurbineElectr.", "AnnualTurbineElectr.", "GWh"));
		output.add(new EnergyPLANOutput("AnnualPump2Electr.", "AnnualPump2Electr.", "GWh"));
		output.add(new EnergyPLANOutput("AnnualTurbine2Electr.", "AnnualTurbine2Electr.", "GWh"));
		
		
		
		output.add(new EnergyPLANOutput("AnnualHH-CHPElectr.", "AnnualCHP", "GWh"));
		output.add(new EnergyPLANOutput("AnnualHH SolarHeat", "AnnualSolarThermalheat", "GWh"));
		output.add(new EnergyPLANOutput("AnnualImportElectr.", "AnnualImport", "GWh"));
		output.add(new EnergyPLANOutput("AnnualExportElectr.", "AnnualExport", "GWh"));
		output.add(new EnergyPLANOutput("Coal Consumption", "CoalConsumption", "GWh")); 
		output.add(new EnergyPLANOutput("Oil Consumption", "OilConsumption", "GWh"));
		output.add(new EnergyPLANOutput("Biomass Consumption", "BiomassConsumption", "GWh"));
		output.add(new EnergyPLANOutput("Ngas Consumption", "NgasConsumption", "GWh"));
		output.add(new EnergyPLANOutput("Bottleneck", "Bottleneck", "kEuro"));
		output.add(new EnergyPLANOutput("Electricity exchange", "TotalElectricityExchangeCost", "kEuro"));
		output.add(new EnergyPLANOutput("calculatedVariableCost", "calculatedVariableCost", "kEuro"));
		output.add(new EnergyPLANOutput("calculatedOperationalCost", "calculatedOperationalCost", "kEuro"));
		output.add(new EnergyPLANOutput("calculatedInvestmentCost", "calculatedInvestmentCost", "kEuro"));	
		output.add(new EnergyPLANOutput("CO2Emission", "CO2Emission", "kton"));
		output.add(new EnergyPLANOutput("AnnualCost", "AnnualCost", "kEuro"));
		
		output.add(new EnergyPLANOutput("coalBoilerFuelDemand", "coalBoilerFuelDemand", "GWh"));
		output.add(new EnergyPLANOutput("oilBoilerFuelDemand", "oilBoilerFuelDemand", "GWh"));
		output.add(new EnergyPLANOutput("nGasBoilerFuelDemand", "nGasBoilerFuelDemand", "GWh"));
		output.add(new EnergyPLANOutput("biomassBoilerFuelDemand", "biomassBoilerFuelDemand", "GWh"));
	
		output.add(new EnergyPLANOutput("coalBoilerSolarUtilization",    "coalBoilerSolarUtilization", "GWh"));
		output.add(new EnergyPLANOutput("oilBoilerSolarUtilization",     "oilBoilerSolarUtilization", "GWh"));
		output.add(new EnergyPLANOutput("nGasBoilerSolarUtilization",    "nGasBoilerSolarUtilization", "GWh"));
		output.add(new EnergyPLANOutput("biomassBoilerSolarUtilization", "biomassBoilerSolarUtilization", "GWh"));
		output.add(new EnergyPLANOutput("h2mCHPSolarUtilization",        "h2mCHPrSolarUtilization", "GWh"));
		output.add(new EnergyPLANOutput("nGasmCHPSolarUtilization",      "nGasmCHPSolarUtilization", "GWh"));
		output.add(new EnergyPLANOutput("biomassmCHPSolarUtilization",   "biomassmCHPSolarUtilization", "GWh"));
		output.add(new EnergyPLANOutput("hpSolarUtilization",            "hpSolarUtilization", "GWh"));
		                                 
		output.add(new EnergyPLANOutput("Annual MaximumImportElectr.", "Annual Maximum Import", "kW"));
		output.add(new EnergyPLANOutput("Annual MaximumExportElectr.", "Annual Maximum Export", "kW"));
		
			
		
			

		

	}

	static public void WriteEnergyPLANParametersToFile(MultiMap energyPLANMap, File filePath) throws IOException {

		Iterator it;
		Collection<String> col;

		// if file doesnt exists, then create it
		if (!filePath.exists()) {
			filePath.createNewFile();

			// create header of the file
			FileWriter fw = new FileWriter(filePath.getAbsoluteFile());
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

			for (int i = 0; i < output.size(); i++) {
				headings += output.get(i).getKeywordForOutput() + ";";
			}

			bw.write(headings);

			bw.newLine();

			String units = "";
			for (int i = 0; i < output.size(); i++) {
				units += output.get(i).getUnit() + ";";
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

		FileWriter fw = new FileWriter(filePath.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);

		/*
		 * String input = "", output = ""; for (int i = 0; i < inputs.length; i++) { col
		 * = (Collection<String>) energyPLANMap.get(inputs[i]); try{ it =
		 * col.iterator(); }catch(NullPointerException e) { input = input + ";";
		 * continue; } input = input + it.next() + ";"; }
		 */

		String outputLine = "";

		for (int i = 0; i < output.size(); i++) {
			try {
				col = (Collection<String>) energyPLANMap

						.get(output.get(i).getTechnicalKeywordInMap());
				it = col.iterator();
				String str = it.next().toString();
				if (!str.equals("1000") && str.endsWith("1000")) {
					str = str.substring(0, str.lastIndexOf("1000"));
					str = Double.parseDouble(str) + "";
				}
				outputLine = outputLine + str + ";";
			} catch (NullPointerException e) {
				outputLine = outputLine + ";";
				continue;

			}
		}
		bw.write(outputLine);
		bw.newLine();
		bw.close();

	}

}
