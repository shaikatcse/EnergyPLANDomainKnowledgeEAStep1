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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import jmetal.core.Solution;
import jmetal.util.JMException;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import reet.fbk.eu.OptimizeEnergyPLANCIVIS.ParseFile.EnergyPLANFileParseForCivis;

;

public class ExtractEnergyPLANParameters {

	// static MultiMap energyplanmMap;

	static String inputs[] = { "PV_Cap", "HP_Cap" };
	static String outputs[] = { "AnnualPV", "Annualimport", "Annualexport",
			"OilBoilerHeat", "OilBoilerFuel", "NgasBoilerHeat",
			"NgasBoilerFuel", "BiomassBoilerHeat", "BiomassBoilerFuel",
			"Gasoil/Diesel", "Total Ngas Exchange costs", "Biomass",
			"Total Electricity exchange", "Total variable costs",
			"Fixed operation costs", "AdditionalCost", "InvestmentCost",
			"CO2-emission (corrected)", "AnnualCost" };

	int currentPVCapacity, currentHydroCapacity;
	double OilBoilerHeatdemand, nGasBoilerHeatDemand, biomassBoilerHeatDemand;
	double maxHeatDemandInScaleOf1;

	int boilerLifeTime, PVLifeTime, HydroLifeTime, geoBoreHoleLifeTime;
	double interest;
	double COP;

	public ExtractEnergyPLANParameters(String siteName) {
		// TODO Auto-generated constructor stub
		if (siteName.equals("CEIS")) {

			currentPVCapacity = 5328;
			currentHydroCapacity = 4000;

			OilBoilerHeatdemand = 6.51;
			nGasBoilerHeatDemand = 4.00;
			biomassBoilerHeatDemand = 15.54;

			boilerLifeTime = 20;
			PVLifeTime = 25;
			HydroLifeTime = 20;
			interest = 0.04;
			geoBoreHoleLifeTime = 100;

			COP = 3.2;

			maxHeatDemandInScaleOf1 = 0.000312745;

		} else if (siteName.equals("CEDIS")) {
			currentPVCapacity = 5566;
			currentHydroCapacity = 4592;

			OilBoilerHeatdemand = 10.07;
			nGasBoilerHeatDemand = 3.40;
			biomassBoilerHeatDemand = 8.81;

			boilerLifeTime = 20;
			PVLifeTime = 25;
			HydroLifeTime = 20;
			interest = 0.04;
			geoBoreHoleLifeTime = 100;

			COP = 3.2;

			maxHeatDemandInScaleOf1 = 0.000322065;

		} else
			System.exit(-1);

	}

	public static void main(String[] args) throws IOException {

		MultiMap energyplanmMap = null;
		// TODO Auto-generated method stub
		ExtractEnergyPLANParameters ob = new ExtractEnergyPLANParameters(
				args[1]);
		FileInputStream fos = new FileInputStream(args[0]);
		InputStreamReader isr = new InputStreamReader(fos);
		BufferedReader br = new BufferedReader(isr);

		String line;
		while ((line = br.readLine()) != null) {
			energyplanmMap = ob.simulateEnergyPLAN(line, args[1]);
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

			bw.write("PV_C HP_Heat_P "
					+ "AnlPV_P AnlImport AnlExport AnlOilBoilerHeat AnlOilBoilerFuel AnlNgasBoilerHeat AnlNgasBoilerFuel "
					+ "AnlBiomassBoilerHeat AnlBiomassBoilerFuel AnlOilCost AnlLPGCost AnlBiomassCost AnlElecExchage TotalVariableCost FixedOperationalCost "
					+ "AdditionalCost InvestmentCost CO2Emission AnnualCost");
			bw.newLine();
			bw.write("(KW) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) "
					+ "(GWh) (GWh) (GWh) (GWh) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) "
					+ "(Mt) (KEuro)");
			bw.newLine();
			bw.close();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);

		String input = "", output = "";
		for (int i = 0; i < inputs.length; i++) {
			col = (Collection<String>) energyPLANMap.get(inputs[i]);
			it = col.iterator();
			input = input + it.next() + " ";
		}
		for (int i = 0; i < outputs.length; i++) {
			col = (Collection<String>) energyPLANMap.get(outputs[i]);
			it = col.iterator();
			String str = it.next().toString();
			if (str.endsWith("1000")) {
				str = str.substring(0, str.lastIndexOf("1000"));
				str = Double.parseDouble(str) + "";
			}
			output = output + str + " ";
		}

		bw.write(input + output);
		bw.newLine();
		bw.close();

	}

	MultiMap simulateEnergyPLAN(String individual, String siteName) {
		MultiMap energyplanMap = null;
		MultiMap modifyMap = new MultiValueMap();
		modifyMap = writeModificationFile(individual);
		String energyPLANrunCommand="";
		if (siteName.equals("CEIS")) {
			energyPLANrunCommand = ".\\EnergyPLAN_SEP_2013\\EnergyPLAN.exe -i "
					+ "\".\\src\\reet\\fbk\\eu\\OptimizeEnergyPLANCIVIS\\CEIS\\problem\\data\\Civis_CEIS.txt\" "
					+ "-m \"modification.txt\" -ascii \"result.txt\" ";
		} else {
			energyPLANrunCommand = ".\\EnergyPLAN_SEP_2013\\EnergyPLAN.exe -i "
					+ "\".\\src\\reet\\fbk\\eu\\OptimizeEnergyPLANCIVIS\\CEdiS\\data\\CediS_data.txt\" "
					+ "-m \"modification.txt\" -ascii \"result.txt\" ";
		}
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

			Iterator it;
			Collection<String> col;

			col = (Collection<String>) energyplanMap.get("TOTAL ANNUAL COSTS");
			it = col.iterator();
			String totalAnnualCost = it.next().toString();
			String extractCost = totalAnnualCost.substring(0,
					totalAnnualCost.lastIndexOf("1000"));
			double extractCostfromEnergyPLAN = Double.parseDouble(extractCost);

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

			// calculate additional cost
			// extrect annual hydro production
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

			// calculate additional cost
			// (hydroProduction+PVproduction+Import-Export)*average additional
			// cost (85.74)
			double additionalCost = Math.round((hydroPowerProduction
					+ PVproduction + Import - Export) * 85.74);

			/*
			 * now, calculte how many boiler need to diassamble
			 */

			// total heat demand=Oil boiler + Ngas boiler + Biomass boiler
			double totalHeatDemand = OilBoilerHeatdemand + nGasBoilerHeatDemand
					+ biomassBoilerHeatDemand;

			// HP

			StringTokenizer st = new StringTokenizer(individual);
			st.nextToken();
			double HP = Double.parseDouble(st.nextToken().toString());
			

			double reducedHeatdemand = totalHeatDemand - HP;
			double numberOfBoilerforNewHeatDemand = Math
					.round(maxHeatDemandInScaleOf1 * reducedHeatdemand
							* Math.pow(10, 6) * 1.5);

			double numberOfHeatPump = Math.round(maxHeatDemandInScaleOf1 * HP
					* Math.pow(10, 6) / COP);
			double geoBoreHoleInvestmentCost = (numberOfHeatPump * 3.2 * interest)
					/ (1 - Math.pow((1 + interest), -geoBoreHoleLifeTime));

			// reduced investment cost = number of boiler to meet new heat
			// demand after introducing HP* boiler cost + current PV capccity *
			// pv cost + Hydro * hydro cost
			// see anual investment cost formual in EnergyPLAN manual

			double reductionInvestmentCost = (numberOfBoilerforNewHeatDemand * 0.625 * interest)
					/ (1 - Math.pow((1 + interest), -boilerLifeTime))
					+ (currentPVCapacity * 3.978 * interest)
					/ (1 - Math.pow((1 + interest), -PVLifeTime))
					+ (currentHydroCapacity * 3.51 * interest)
					/ (1 - Math.pow((1 + interest), -HydroLifeTime));

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
			double actualAnnualCost = totalVariableCost + fixedOperationalCost
					+ realInvestmentCost + additionalCost;

			energyplanMap.put("AdditionalCost", additionalCost);
			energyplanMap.put("InvestmentCost", realInvestmentCost);
			energyplanMap.put("AnnualCost", actualAnnualCost);

		} catch (IOException e) {
			System.out.println("Energyplan.exe has some problem");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Energyplan interrupted");
		}

		return energyplanMap;

	}

	public MultiMap writeModificationFile(String individual) {

		MultiMap modifyMap = new MultiValueMap();

		StringTokenizer st = new StringTokenizer(individual);
		// PV
		double pv = Double.parseDouble(st.nextToken().toString());

		// HP
		double HP = Double.parseDouble(st.nextToken().toString());

		modifyMap.put(inputs[0], (int) Math.round(pv));
		modifyMap.put(inputs[1], HP);
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

			/*
			 * str = "input_cap_chp3_el="; bw.write(str); bw.newLine(); str = ""
			 * + (int) Math.round(CHPGr3); bw.write(str); bw.newLine();
			 * 
			 * str = "input_cap_hp3_el="; bw.write(str); bw.newLine(); str = ""
			 * + (int) Math.round(HPGr3); bw.write(str); bw.newLine();
			 * 
			 * str = "input_cap_pp_el="; bw.write(str); bw.newLine(); str = "" +
			 * (int) Math.round(PP); bw.write(str); bw.newLine();
			 */

			str = "input_RES1_capacity=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(pv);
			bw.write(str);
			bw.newLine();

			str = "input_HH_HP_heat=";
			bw.write(str);
			bw.newLine();
			str = "" + HP;
			bw.write(str);
			bw.newLine();

			// total heat demand=Oil boiler + Ngas boiler + Biomass boiler
			double totalHeatDemand = OilBoilerHeatdemand + nGasBoilerHeatDemand
					+ biomassBoilerHeatDemand;

			if (HP <= OilBoilerHeatdemand) {
				// reduce Oil boiler
				double reducedOilBoilerHeatDemand = OilBoilerHeatdemand - HP;
				double oilBoilerFuelConsumption = reducedOilBoilerHeatDemand / 0.8; // 0.8
																					// is
																					// the
																					// efficiency

				str = "input_fuel_Households[2]=";
				bw.write(str);
				bw.newLine();
				str = "" + oilBoilerFuelConsumption;
				bw.write(str);
				bw.newLine();

				modifyMap.put("OilBoilerHeat", reducedOilBoilerHeatDemand);
				modifyMap.put("OilBoilerFuel", oilBoilerFuelConsumption);

				modifyMap.put("NgasBoilerHeat", nGasBoilerHeatDemand);
				modifyMap.put("NgasBoilerFuel", nGasBoilerHeatDemand / 0.9);

				modifyMap.put("BiomassBoilerHeat", biomassBoilerHeatDemand);
				modifyMap.put("BiomassBoilerFuel",
						biomassBoilerHeatDemand / 0.7);

			} else if (HP > OilBoilerHeatdemand
					&& HP <= OilBoilerHeatdemand + nGasBoilerHeatDemand) {
				// reduce Ngas boiler
				double reducedNGasBoilerHeatDemand = OilBoilerHeatdemand
						+ nGasBoilerHeatDemand - HP;
				double nGasBoilerFuelConsumption = reducedNGasBoilerHeatDemand / 0.9; // 0.9
																						// is
																						// the
																						// efficiency

				// make oil boiler 0
				str = "input_fuel_Households[2]=";
				bw.write(str);
				bw.newLine();
				str = "" + 0;
				bw.write(str);
				bw.newLine();

				str = "input_fuel_Households[3]=";
				bw.write(str);
				bw.newLine();
				str = "" + nGasBoilerFuelConsumption;
				bw.write(str);
				bw.newLine();

				modifyMap.put("OilBoilerHeat", 0);
				modifyMap.put("OilBoilerFuel", 0);

				modifyMap.put("NgasBoilerHeat", reducedNGasBoilerHeatDemand);
				modifyMap.put("NgasBoilerFuel", nGasBoilerFuelConsumption);

				modifyMap.put("BiomassBoilerHeat", biomassBoilerHeatDemand);
				modifyMap.put("BiomassBoilerFuel",
						biomassBoilerHeatDemand / 0.7);

			} else {
				// reduce Biomass boiler

				double reducedBiomassBoilerHeatDemand = totalHeatDemand - HP;
				double biomassBoilerFuelConsumption = reducedBiomassBoilerHeatDemand / 0.7; // 0.7
																							// is
																							// the
																							// efficiency

				// make oil boiler 0
				str = "input_fuel_Households[2]=";
				bw.write(str);
				bw.newLine();
				str = "" + 0;
				bw.write(str);
				bw.newLine();

				// make Ngas boiler 0
				str = "input_fuel_Households[3]=";
				bw.write(str);
				bw.newLine();
				str = "" + 0;
				bw.write(str);
				bw.newLine();

				str = "input_fuel_Households[4]=";
				bw.write(str);
				bw.newLine();
				str = "" + biomassBoilerFuelConsumption;
				bw.write(str);
				bw.newLine();

				modifyMap.put("OilBoilerHeat", 0);
				modifyMap.put("OilBoilerFuel", 0);

				modifyMap.put("NgasBoilerHeat", 0);
				modifyMap.put("NgasBoilerFuel", 0);

				modifyMap.put("BiomassBoilerHeat",
						reducedBiomassBoilerHeatDemand);
				modifyMap
						.put("BiomassBoilerFuel", biomassBoilerFuelConsumption);

			}

			/*
			 * str = "input_RES2_capacity="; bw.write(str); bw.newLine(); str =
			 * "" + (int) Math.round(offShoreWind); bw.write(str); bw.newLine();
			 * 
			 * str = "input_RES3_capacity="; bw.write(str); bw.newLine(); str =
			 * "" + (int) Math.round(PV); bw.write(str); bw.newLine();
			 * 
			 * str="input_storage_gr3_cap="; bw.write(str); bw.newLine(); str =
			 * "" + (double) Math.round(heatStorageGr3*100)/100; bw.write(str);
			 * bw.newLine();
			 */

			/*
			 * str = "input_fuel_PP[1]="; bw.write(str); bw.newLine(); str = ""
			 * + twoDForm.format(PP_coal_share); bw.write(str); bw.newLine();
			 * 
			 * str = "input_fuel_PP[2]="; bw.write(str); bw.newLine(); str = ""
			 * + twoDForm.format(PP_oil_share); bw.write(str); bw.newLine();
			 * 
			 * str = "input_fuel_PP[3]="; bw.write(str); bw.newLine(); str = ""
			 * + twoDForm.format(PP_ngas_share); bw.write(str); bw.newLine();
			 * 
			 * str = "input_eff_pp_el="; bw.write(str); bw.newLine(); str = "" +
			 * twoDForm.format(overall_eff_marco); bw.write(str); bw.newLine();
			 */

			bw.close();

			// file.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return modifyMap;
	}
}
