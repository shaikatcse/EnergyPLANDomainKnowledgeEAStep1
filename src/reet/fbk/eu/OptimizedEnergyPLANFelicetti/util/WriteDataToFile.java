package reet.fbk.eu.OptimizedEnergyPLANFelicetti.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.MultiMap;

public class WriteDataToFile {
	static String inputs[] = { "PV_Cap", "WavePower_Cap", /*"Biomass_CHP_Cap", "NGas_CHP_Cap", "HP_Cap",
			"Oil_boiler_Cap", "NGas_boiler_Cap", "Biomass_boiler_Cap", "SolarThermal", */
			"NumberOfDieselCars", "NumberOfPetrolCars", "NumberOfEVCars", "IndvEloctrolyzer_Cap", 
			"H2FeedStock", "FeedStockElectrolyzer_Cap", "FeedStockH2(imp)",
			"ElecStorage_Turbine_Cap","ElecStorage_Pump_Cap", "ElecStorage_Storage_Cap" };	
	
	static String outputsinEnergyPLANMap[] = { "AnnualPVElectr.", "AnnualHH-CHPElectr.",
			"AnnualHH-HPElectr.", "AnnualFlexibleElectr.", "AnnualH2demand", "AnnualOilBoilerheat", "AnnualNGasBoilerheat",
			"AnnualBiomassBoilerheat", "AnnualNgasmCHPheat", "AnnualBiomassmCHPheat", "AnnualHPheat", "AnnualHH SolarHeat",
			"AnnualImportElectr.", "AnnualExportElectr.", "Oil Consumption",
			"Biomass Consumption", "Ngas Consumption", 
			/*"Gasoil/Diesel",
			"Biomass",*/ "Bottleneck", "Electricity exchange",
			"Variable costs", "Fixed operation costs", "AdditionalCost",
			//"ConCarInvestmentCost","EVCarInvestmentCost","FCEVCarInvestmentCost",
			"Annual Investment costs", "CO2Emission", "AnnualCost", 
			"oilBoilerFuelDemand", "ngasBoilerFuelDemand", "biomassBoilerFuelDemand", "AnnualNgasmCHPheat", "AnnualH2mCHPheat", "AnnualBiomassmCHPheat", "AnnualHPheat", "TotalSolarThermalInput", 
			"transOilDemand", 
			"OilBoilerSolarInput", "NGasBoilerSolarInput","NGasCHPSolarInput", "BiomassBoilerSolarInput", "BiomassCHPSolarInput", "HPSolarInput", "H2CHPSolarInput", 
			"OilBoilerSolarUtilization",	"NGasBoilerSolarUtilization", "NgasCHPSolarUtilization", "biomassBoilerSolarUtilization", "biomassCHPSolarUtilization", "HPSolarUtilization",
			"Annual MaximumImportElectr.", "Annual MaximumExportElectr."}; 

	static String outputsinFile[] = { "AnnualPV", "AnnualCHPelec",
			"AnnualHPelec", "AnnualElecCar", "AnnualH2demand", "AnnualOilBoilerheat", "AnnualNGasBoilerheat",
			"AnnualBiomassBoilerheat", "AnnualNgasmCHPheat", "AnnualBiomassmCHPheat", "AnnualHPheat", "AnnualSolarThermalheat", 
			"AnnualImport", "AnnualExport", "OilConsumption",
			"BiomassConsumption", "NGasConsuption", 
			/*"PetrolCost", 
			"BiomassCost",*/ "Bottleneck", "TotalElectricityExchangeCost", 
			"TotalVariableCost", "FixedOperationCosts", "AdditionalCost", 
			//"ConCarInvestmentCost","EVCarInvestmentCost","FCEVInvestmentCost", 
			"InvestmentCost","CO2-Emission", "AnnualCost", 
			"oilBoilerFuelDemand", "NGasBoilerFuelDemand", "biomassBoilerFuelDemand", "AnnualNgasmCHPheat", "AnnualH2mCHPheat","AnnualBiomassmCHPheat", "AnnualHPheat", "AnnualSolarThermalInput", 
			"transOilDemand",
			"OilBoilerSolarInput", "NGasBoilerSolarInput", "NGasCHPSolarInput", "BiomassBoilerSolarInput", "BiomassCHPSolarInput", "HPSolarInput","H2CHPSolarInput",
			"OilBoilerSolarUtilization",	"NGasBoilerSolarUtilization", "NgasCHPSolarUtilization","biomassBoilerSolarUtilization", "biomassCHPSolarUtilization", "HPSolarUtilization",
			"Annual MaximumImportElectr.", "Annual MaximumExportElectr."
	};

	static String inputUnits[] = { "MWe", "MWe", /*"MWe", "MWe", "MWth", "MWth", "MWth", "TWh",*/ "1000 units", "1000 units", "1000 units", "MWe", "MWh","MWe", "MWh", "MW", "MW", "GWh" };
	static String outputUnits[] = { "TWh", "TWh", // "AnnualPV", "AnnualCHPelec",
			"TWh", "TWh", "TWh", "TWh", "TWh", //"AnnualHPelec", "AnnualElecCar", "AnnualH2demand", "AnnualOilBoilerheat", "AnnualNGasBoilerheat",
			"TWh", "TWh", "TWh","TWh", "TWh", "TWh", //"AnnualBiomassBoilerheat", "AnnualNgasmCHPheat", "AnnualBiomassmCHPheat", "AnnualHPheat", "AnnualSolarThermalheat", 
			"TWh", "TWh", "TWh",  //"AnnualImport", "AnnualExport", "OilConsumption",
			"TWh", "TWh",  //"BiomassConsumption", "NGasConsuption", 
			/*"MEuro",  //"DieselCost", 
			"MEuro",*/ "MEuro", //"BiomassCost", "TotalElectricityExchangeCost",
			"MEuro", "MEuro", "MEuro", //"TotalVariableCost", "FixedOperationCosts", "AdditionalCost", 
			//"KEuro", "KEuro", "KEuro", //"ConCarInvestmentCost","EVCarInvestmentCost","FCEVInvestmentCost", 
			"MEuro", "Mt", "MEuro", //"InvestmentCost","CO2-Emission", "Total Local System Emission", "AnnualCost", 
			"TWh","TWh","TWh","TWh","TWh","TWh","TWh", //"oilBoilerFuelDemand", "NGasBoilerFuelDemand", "biomassBoilerFuelDemand", "AnnualNgasmCHPheat", "AnnualBiomassmCHPheat, "AnnualHPheat", "AnnualSolarThermal", 
			"TWh",   //"transOilDemand"
			"TWh","TWh","TWh", "TWh","TWh","TWh", "TWh", "TWh", //"OilBoilerSolatInput", "NGasBoilerSolarInput","NGasCHPSolarInput", "BiomassBoilerSolarInput", "BiomassCHPSolarInput", "HPSolarInput" 
			"TWh","TWh","TWh", "TWh","TWh", //"OilBoilerUtilization",	"NGasBoilerUtilization", "biomassBoilerSolarUtilization", "biomassCHPSolarUtilization", "HPSolarUtilization"
			"MW", "MW"
	};

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

		
		FileWriter fw = new FileWriter(filePath.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);

		String input = "", output = "";
		for (int i = 0; i < inputs.length; i++) {
			col = (Collection<String>) energyPLANMap.get(inputs[i]);
			try{
				it = col.iterator();
			}catch(NullPointerException e) {
				input = input  + ";";
				continue;
			}
			input = input + it.next() + ";";
		}
		
		for (int i = 0; i < outputsinEnergyPLANMap.length; i++) {
			try{
				col = (Collection<String>) energyPLANMap
			
					.get(outputsinEnergyPLANMap[i]);
			it = col.iterator();
			String str = it.next().toString();
			if(str.equals("1000"))
				System.out.println("la la");
			if (!str.equals("1000") && str.endsWith("1000")) {
				str = str.substring(0, str.lastIndexOf("1000"));
				str = Double.parseDouble(str) + "";
			}
			output = output + str + ";";
		}
			catch(NullPointerException e){
				output = output +  ";";
				System.out.println(i);
				continue;
				
		}
		}
		bw.write(input + output);
		bw.newLine();
		bw.close();

	}

	

}
