package reet.fbk.eu.OptimizeEnergyPLANCEIS2021.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.MultiMap;

 class EnergyPLANOutput{
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
	
	
	
	public static ArrayList <EnergyPLANOutput> output = new ArrayList();
	
	public WriteDataToFile() {
			
	}
	
	/*static String inputs[] = { "PV_Cap", "WavePower_Cap", /*"Biomass_CHP_Cap", "NGas_CHP_Cap", "HP_Cap",
			"Oil_boiler_Cap", "NGas_boiler_Cap", "Biomass_boiler_Cap", "SolarThermal", */
	/*		"NumberOfDieselCars", "NumberOfPetrolCars", "NumberOfEVCars", "IndvEloctrolyzer_Cap", 
			"H2FeedStock", "FeedStockElectrolyzer_Cap", "FeedStockH2(imp)",
			"ElecStorage_Turbine_Cap","ElecStorage_Pump_Cap", "ElecStorage_Storage_Cap", "WindPower_Cap", "TidalPower_Cap", "H2IndvStorage", "H2TransStorage" };
	
	static String outputsinEnergyPLANMap[] = { "AnnualPVElectr.", "AnnualWindElectr.", "AnnualWaveElectr.", "AnnualTidalElectr.", "AnnualHH-CHPElectr.",
			"AnnualHH-HPElectr.", "AnnualFlexibleElectr.", "AnnualH2demand", "AnnualOilBoilerheat", "AnnualNGasBoilerheat",
			"AnnualBiomassBoilerheat", "AnnualNgasmCHPheat", "AnnualElecHeatingheat", "AnnualH2mCHPheat", "AnnualHH SolarHeat",
			"AnnualImportElectr.", "AnnualExportElectr.", "Oil Consumption",
			"Biomass Consumption", "Ngas Consumption", 
			/*"Gasoil/Diesel",
			"Biomass",*/ /*"Bottleneck", "Electricity exchange",
			"Variable costs", "Fixed operation costs", "AdditionalCost",
			//"ConCarInvestmentCost","EVCarInvestmentCost","FCEVCarInvestmentCost",
			"Annual Investment costs", "CO2Emission", "AnnualCost", 
			"oilBoilerFuelDemand", "ngasBoilerFuelDemand", "biomassBoilerFuelDemand", "AnnualNgasmCHPheat", "AnnualH2mCHPheat", "AnnualBiomassmCHPheat", "AnnualHPheat", "AnnualHH SolarHeat", 
			"transOilDemand", 
			"OilBoilerSolarInput", "NGasBoilerSolarInput","NGasCHPSolarInput", "BiomassBoilerSolarInput", "H2CHPSolarInput", "ElecHeatingSolarInput", 
			"OilBoilerUtilization",	"NGasBoilerUtilization", "BiomassBoilersolarUtilization", "NgasCHPSolarUtilization", "H2CHPSolarUtilization","ElecHeatingSolarUtilization",
			"Annual MaximumImportElectr.", "Annual MaximumExportElectr."};
	         

	static String outputsinFile[] = { "AnnualPV", "AnnualWind", "AnnualWave", "AnnualTidal", "AnnualCHP ",
			"AnnualHPelec", "AnnualElecCar", "AnnualH2demand", "AnnualOilBoilerheat", "AnnualNGasBoilerheat",
			"AnnualBiomassBoilerheat", "AnnualNgasmCHPheat", "AnnualElecHeatingheat", "AnnualH2mCHPheat", "AnnualSolarThermalheat", 
			"AnnualImport", "AnnualExport", "OilConsumption",
			"BiomassConsumption", "NGasConsuption", 
			/*"PetrolCost", 
			"BiomassCost",*/ /*"Bottleneck", "TotalElectricityExchangeCost", 
			"TotalVariableCost", "FixedOperationCosts", "AdditionalCost", 
			//"ConCarInvestmentCost","EVCarInvestmentCost","FCEVInvestmentCost", 
			"InvestmentCost","CO2-Emission", "AnnualCost", 
			"oilBoilerFuelDemand", "NGasBoilerFuelDemand", "biomassBoilerFuelDemand", "AnnualNgasmCHPheat", "AnnualH2mCHPheat","AnnualBiomassmCHPheat", "AnnualHPheat", "AnnualSolarThermal", 
			"transOilDemand",
			"OilBoilerSolarInput", "NGasBoilerSolarInput", "NGasCHPSolarInput", "BiomassBoilerSolarInput", "H2CHPSolarInput", "ElecHeatingSolarInput",
			"OilBoilerUtilization",	"NGasBoilerUtilization", "BiomassBoilersolarUtilization", "NgasCHPSolarUtilization", "H2CHPSolarUtilization","ElecHeatingSolarUtilization",
			"Annual MaximumImportElectr.", "Annual MaximumExportElectr."
			 
	};

	static String inputUnits[] = { "MWe", "MWe", /*"MWe", "MWe", "MWth", "MWth", "MWth", "TWh",*/ /*"1000 units", "1000 units", "1000 units", "MWe", "MWh","MWe", "MWh", "MW", "MW", "GWh","MW","MW", "GWh", "Gwh" };
	static String outputUnits[] = { "TWh", "TWh", "TWh","TWh","TWh",// "AnnualPV", "AnnualCHPelec",
			"TWh", "TWh", "TWh", "TWh", "TWh", //"AnnualHPelec", "AnnualElecCar", "AnnualH2demand", "AnnualOilBoilerheat", "AnnualNGasBoilerheat",
			"TWh", "TWh", "TWh","TWh", "TWh", "TWh", //"AnnualBiomassBoilerheat", "AnnualNgasmCHPheat", "AnnualBiomassmCHPheat", "AnnualHPheat", "AnnualSolarThermalheat", 
			"TWh", "TWh", "TWh",  //"AnnualImport", "AnnualExport", "OilConsumption",
			"TWh", "TWh",  //"BiomassConsumption", "NGasConsuption", 
			/*"MEuro",  //"DieselCost", 
			"MEuro",*/ /*"MEuro", //"BiomassCost", "TotalElectricityExchangeCost",
			"MEuro", "MEuro", "MEuro", //"TotalVariableCost", "FixedOperationCosts", "AdditionalCost", 
			//"KEuro", "KEuro", "KEuro", //"ConCarInvestmentCost","EVCarInvestmentCost","FCEVInvestmentCost", 
			"MEuro", "Mt", "MEuro", //"InvestmentCost","CO2-Emission", "Total Local System Emission", "AnnualCost", 
			"TWh","TWh","TWh","TWh","TWh","TWh","TWh", //"oilBoilerFuelDemand", "NGasBoilerFuelDemand", "biomassBoilerFuelDemand", "AnnualNgasmCHPheat", "AnnualBiomassmCHPheat, "AnnualHPheat", "AnnualSolarThermal", 
			"TWh",   //"transOilDemand"
			"TWh","TWh","TWh", "TWh","TWh","TWh", //"OilBoilerSolatInput", "NGasBoilerSolarInput","NGasCHPSolarInput", "BiomassBoilerSolarInput", "BiomassCHPSolarInput", "HPSolarInput" 
			"TWh","TWh","TWh", "TWh","TWh","TWh", //"OilBoilerUtilization",	"NGasBoilerUtilization", "biomassBoilerSolarUtilization", "biomassCHPSolarUtilization", "HPSolarUtilization"
			"MW", "MW"
	};*/

	
	static public void init() {
		
		output.add(new EnergyPLANOutput("PV_Cap", "PV_Cap", "kWe"));
		output.add(new EnergyPLANOutput("WavePower_Cap", "WavePower_Cap", "kWe"));
		output.add(new EnergyPLANOutput("WindPower_Cap", "WindPower_Cap", "kWe"));
		output.add(new EnergyPLANOutput("TidalPower_Cap", "TidalPower_Cap", "kWe"));
		
		output.add(new EnergyPLANOutput("NumberOfDieselCars", "NumberOfDieselCars", "1000 units"));
		output.add(new EnergyPLANOutput("NumberOfPetrolCars", "NumberOfPetrolCars", "1000 units"));
		output.add(new EnergyPLANOutput("NumberOfEVCars", "NumberOfEVCars", "1000 units"));
			
		output.add(new EnergyPLANOutput("IndvEloctrolyzer_Cap", "IndvEloctrolyzer_Cap", "kWe"));
		output.add(new EnergyPLANOutput("H2IndvStorage", "H2IndvStorage", "MWh"));
		
		output.add(new EnergyPLANOutput("H2FeedStock", "H2FeedStock", "MWh"));
		output.add(new EnergyPLANOutput("FeedStockElectrolyzer_Cap", "FeedStockElectrolyzer_Cap", "kWe"));
		output.add(new EnergyPLANOutput("FeedStockH2(imp)", "FeedStockH2(imp)", "MWh"));
		output.add(new EnergyPLANOutput("H2TransStorage", "H2TransStorage", "MWh"));
		
		
		output.add(new EnergyPLANOutput("ElecStorage_Turbine_Cap", "ElecStorage_Turbine_Cap", "kW"));
		output.add(new EnergyPLANOutput("ElecStorage_Pump_Cap", "ElecStorage_Pump_Cap", "kW"));
		output.add(new EnergyPLANOutput("ElecStorage_Storage_Cap", "ElecStorage_Storage_Cap", "kW"));
				
		output.add(new EnergyPLANOutput("AnnualPVElectr.", "AnnualPV", "GWh"));
		output.add(new EnergyPLANOutput("AnnualWindElectr.", "AnnualWind", "GWh"));
		output.add(new EnergyPLANOutput("AnnualWaveElectr.", "AnnualWave", "GWh"));
		output.add(new EnergyPLANOutput("AnnualTidalElectr.", "AnnualTidel", "GWh"));
		
		output.add(new EnergyPLANOutput("AnnualHH-CHPElectr.", "AnnualCHP", "GWh"));
		//output.add(new EnergyPLANOutput("AnnualHH-HPElectr.", "AnnualHP", "GWh"));	
		output.add(new EnergyPLANOutput("AnnualFlexibleElectr.", "AnnualElecCar", "GWh"));	
		output.add(new EnergyPLANOutput("AnnualH2demand.", "AnnualH2demand", "GWh"));
		output.add(new EnergyPLANOutput("AnnualOilBoilerheat", "AnnualOilBoilerheat", "GWh"));
		output.add(new EnergyPLANOutput("AnnualNGasBoilerheat", "AnnualNGasBoilerheat", "GWh"));
		output.add(new EnergyPLANOutput("AnnualBiomassBoilerheat", "AnnualBiomassBoilerheat", "GWh"));
		output.add(new EnergyPLANOutput("AnnualNgasmCHPheat", "AnnualNgasmCHPheat", "GWh"));
		output.add(new EnergyPLANOutput("AnnualElecHeatingheat", "AnnualElecHeatingheat", "GWh"));
		output.add(new EnergyPLANOutput("AnnualH2mCHPheat", "AnnualH2mCHPheat", "GWh"));
		output.add(new EnergyPLANOutput("AnnualHH SolarHeat", "AnnualSolarThermalheat", "GWh"));
		output.add(new EnergyPLANOutput("AnnualImportElectr.", "AnnualImport", "GWh"));
		output.add(new EnergyPLANOutput("AnnualExportElectr.", "AnnualExport", "GWh"));
		output.add(new EnergyPLANOutput("Oil Consumption", "OilConsumption", "GWh"));
		output.add(new EnergyPLANOutput("Biomass Consumption", "BiomassConsumption", "GWh"));
		output.add(new EnergyPLANOutput("Ngas Consumption", "NgasConsumption", "GWh"));
		output.add(new EnergyPLANOutput("Bottleneck", "Bottleneck", "GWh"));
		output.add(new EnergyPLANOutput("Electricity exchange", "TotalElectricityExchangeCost", "kEuro"));
		output.add(new EnergyPLANOutput("Variable costs", "TotalVariableCost", "kEuro"));
		output.add(new EnergyPLANOutput("Fixed operation costs", "FixedOperationCosts", "kEuro"));
		output.add(new EnergyPLANOutput("AdditionalCost", "AdditionalCost", "kEuro"));
		output.add(new EnergyPLANOutput("Annual Investment costs", "Annual Investment costs", "kEuro"));	
		output.add(new EnergyPLANOutput("CO2Emission", "CO2Emission", "kton"));
		output.add(new EnergyPLANOutput("AnnualCost", "AnnualCost", "kton"));
		
		output.add(new EnergyPLANOutput("oilBoilerFuelDemand", "oilBoilerFuelDemand", "GWh"));
		output.add(new EnergyPLANOutput("ngasBoilerFuelDemand", "ngasBoilerFuelDemand", "GWh"));
		output.add(new EnergyPLANOutput("biomassBoilerFuelDemand", "biomassBoilerFuelDemand", "GWh"));
		output.add(new EnergyPLANOutput("AnnualBiomassmCHPheat", "AnnualBiomassmCHPheat", "GWh"));
		//output.add(new EnergyPLANOutput("AnnualHPheat", "AnnualHPheat", "GWh"));
		output.add(new EnergyPLANOutput("transOilDemand", "transOilDemand", "GWh"));
		
		output.add(new EnergyPLANOutput("OilBoilerSolarInput", "OilBoilerSolarInput", "GWh"));
		output.add(new EnergyPLANOutput("NGasBoilerSolarInput", "NGasBoilerSolarInput", "GWh"));
		output.add(new EnergyPLANOutput("NGasCHPSolarInput", "NGasCHPSolarInput", "GWh"));
		output.add(new EnergyPLANOutput("BiomassBoilerSolarInput", "BiomassBoilerSolarInput", "GWh"));
		output.add(new EnergyPLANOutput("H2CHPSolarInput", "H2CHPSolarInput", "GWh"));
		output.add(new EnergyPLANOutput("ElecHeatingSolarInput", "ElecHeatingSolarInput", "GWh"));
		
		output.add(new EnergyPLANOutput("OilBoilerUtilization", "OilBoilerUtilization", "GWh"));
		output.add(new EnergyPLANOutput("NGasBoilerUtilization", "NGasBoilerUtilization", "GWh"));
		output.add(new EnergyPLANOutput("BiomassBoilersolarUtilization", "BiomassBoilersolarUtilization", "GWh"));
		output.add(new EnergyPLANOutput("NgasCHPSolarUtilization", "NgasCHPSolarUtilization", "GWh"));
		output.add(new EnergyPLANOutput("H2CHPSolarUtilization", "H2CHPSolarUtilization", "GWh"));
		output.add(new EnergyPLANOutput("ElecHeatingSolarUtilization", "ElecHeatingSolarUtilization", "GWh"));
		
		output.add(new EnergyPLANOutput("Annual MaximumImportElectr.", "Annual Maximum Import", "kW"));
		output.add(new EnergyPLANOutput("Annual MaximumExportElectr.", "Annual Maximum Export", "kW"));
		
			
		
			

		

	}
	static public void WriteEnergyPLANParametersToFile(MultiMap energyPLANMap, File filePath)
			throws IOException {
		
		
		Iterator it;
		Collection<String> col;

		// if file doesnt exists, then create it
		if (!filePath.exists()) {
			filePath.createNewFile();

			// create header of the file
			FileWriter fw = new FileWriter(filePath.getAbsoluteFile());
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
			 * bw.write(
			 * "(KW) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) " +
			 * "(GWh) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) "
			 * + "(Mt) (KEuro)");
			 */

			bw.newLine();
			bw.close();
		}

		
		FileWriter fw = new FileWriter(filePath.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);

		/*String input = "", output = "";
		for (int i = 0; i < inputs.length; i++) {
			col = (Collection<String>) energyPLANMap.get(inputs[i]);
			try{
				it = col.iterator();
			}catch(NullPointerException e) {
				input = input  + ";";
				continue;
			}
			input = input + it.next() + ";";
		}*/
		
		String outputLine="";
		
		for (int i = 0; i < output.size(); i++) {
			try{
				col = (Collection<String>) energyPLANMap
			
					.get(output.get(i).getTechnicalKeywordInMap());
			it = col.iterator();
			String str = it.next().toString();
			if (!str.equals("1000") && str.endsWith("1000")) {
				str = str.substring(0, str.lastIndexOf("1000"));
				str = Double.parseDouble(str) + "";
			}
			outputLine = outputLine + str + ";";
		}
			catch(NullPointerException e){
				outputLine = outputLine +  ";";
				continue;
				
		}
		}
		bw.write(outputLine);
		bw.newLine();
		bw.close();

	}

	

}
