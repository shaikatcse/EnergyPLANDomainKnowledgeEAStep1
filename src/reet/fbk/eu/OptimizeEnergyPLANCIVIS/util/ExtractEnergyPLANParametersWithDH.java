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

public class ExtractEnergyPLANParametersWithDH {

	// static MultiMap energyplanmMap;

	static String inputs[] = { "CHP_Cap", "HP_Cap", "PV_Cap", "Boiler_Cap" };
	static String outputsinEnergyPLANMap[] = { "AnnualPV", "Annualchpelec.", "Annualchp2heat", "Annualhp2heat" , "Annualhpelec.", "Annualboilerheat",
		 "Annualimport", "Annualexport",
		 "Oil Consumption", "Biomass Consumption",
		 "Gasoil/Diesel", "Petrol/JP", "Biomass",
		"Total Electricity exchange", "Total variable costs",
			"Fixed operation costs", "AdditionalCost", "InvestmentCost",
			"CO2-emission (corrected)", "AnnualCost", "LoadFollowingCapacity"};
	
	static String outputsinFile[] = { "AnnualPV", "AnnualCHPelec", "AnnualCHPheat", "AnnualHPheat" , "AnnualHPelec", "AnnualBiolerHeat",
		 "AnnualImport", "AnnualExport",
		 "OilConsumption", "BiomassConsumption",
		 "DieselCost", "PetrolCost", "BiomassCost",
		"TotalElectricityExchangeCost", "TotalVariableCost",
			"FixedOperationCosts", "AdditionalCost", "InvestmentCost",
			"CO2-Emission", "AnnualCost", "LoadFollowingCapacity"};
	
	
	
	static String inputUnits[] ={"KW", "KW", "KW", "KW"};
	static String outputUnits[]={"GWh", "GWh", "GWh","GWh","GWh","GWh",
		 "GWh","GWh",
	     "GWh","GWh",
	     "KEuro","KEuro", "KEuro",
	    "KEuro","KEuro",
	    "KEuro","KEuro","KEuro",
	    "Mt", "KEuro", ""};

	int currentPVCapacity, currentHydroCapcity,largeValueOfBoiler ;

	 double totalHeatdemand;
	int boilerLifeTime, PVLifeTime, HydroLifeTime, geoBoreHoleLifeTime;
	double boilerCostInKEuro;
	double interest, fixedMOForBoilerinPercentage;
	double COP;

	public ExtractEnergyPLANParametersWithDH(String siteName) {
		// TODO Auto-generated constructor stub
		if (siteName.equals("CEIS")) {

			MultiMap energyplanmMap;
		//	totalHeatdemand = 55.77;
			boilerLifeTime = 20;
			PVLifeTime = 25;
			HydroLifeTime = 20;
			geoBoreHoleLifeTime = 100;
			COP = 3.1;
			interest = 0.04;
			currentPVCapacity = 5328;
			currentHydroCapcity = 4000;
			// public static final int currentNumberOfBoilers = 12220;
			//public static final double maxHeatDemandInScaleOf1 = 0.000312745;
			// public static final int currentNumberOfBiomassBoiler = 7290;
			// public static final int currentNumberOfOilBoiler = 3055;
			// public static final int currentNumberOfNgasBoiler = 1875;
			largeValueOfBoiler = 17500;
			boilerCostInKEuro = 0.15;
			fixedMOForBoilerinPercentage=0.03;
			

		} else if (siteName.equals("CEDIS")) {
			/*currentPVCapacity = 5566;
			currentHydroCapacity = 4592;


			boilerLifeTime = 20;
			PVLifeTime = 25;
			HydroLifeTime = 20;
			interest = 0.04;
			geoBoreHoleLifeTime = 100;

			COP = 3.2;

			maxHeatDemandInScaleOf1 = 0.000322065;*/

		} else
			System.exit(-1);

	}

	public static void main(String[] args) throws IOException {

		MultiMap energyplanmMap = null;
		// TODO Auto-generated method stub
		ExtractEnergyPLANParametersWithDH ob = new ExtractEnergyPLANParametersWithDH(
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

		/*	bw.write("PV_C HP_Heat_P OilBoilerHeat NgasBoilerHeat BiomassBoilerHeat "
					+ "AnlPV_P AnlImport AnlExport AnlOilBoilerFuel AnlNgasBoilerFuel "
					+ "AnlBiomassBoilerFuel AnlOilCost AnlLPGCost AnlBiomassCost AnlElecExchage TotalVariableCost FixedOperationalCost "
					+ "AdditionalCost InvestmentCost CO2Emission AnnualCost LoadFollowingCapacity");*/
			
			String headings="";
			for(int i=0;i<inputs.length;i++){
				headings+=inputs[i]+" ";
			}
			for(int i=0;i<outputsinFile.length;i++){
				headings+=outputsinFile[i]+" ";
			}
			bw.write(headings);					
		
			bw.newLine();
			
			String units="";
			for(int i=0;i<inputUnits.length;i++){
				units+=inputUnits[i]+" ";
			}
			for(int i=0;i<outputUnits.length;i++){
				units+=outputUnits[i]+" ";
			}
			bw.write(units);
			
			/*bw.write("(KW) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) (GWh) "
					+ "(GWh) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) (KEuro) "
					+ "(Mt) (KEuro)");*/
			
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
		for (int i = 0; i < outputsinEnergyPLANMap.length; i++) {
			col = (Collection<String>) energyPLANMap.get(outputsinEnergyPLANMap[i]);
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

		String energyPLANrunCommand = ".\\EnergyPLAN_SEP_2013\\EnergyPLAN.exe -i "
				+ "\".\\src\\reet\\fbk\\eu\\OptimizeEnergyPLANCIVIS\\CEIS\\data\\CEIS_Complete.txt\" "
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
			
			Iterator it;
			Collection<String> col;

			// extracting maximum Boiler configuration (group # 3)
			/*
			 * col = (Collection<String>) energyplanmMap
			 * .get("Maximumboilerheat r"); it = col.iterator(); double
			 * maximumBoilerGroup3 = Double.parseDouble(it.next() .toString());
			 * modifyModificationFile(maximumBoilerGroup3); //set the decision
			 * variable according to the maximum boiler capacity
			 * solution.getDecisionVariables()[7].setValue(maximumBoilerGroup3);
			 * //run EnergyPLAN for 2nd time, after adjust the boiler3 capacity
			 * process = Runtime.getRuntime().exec(energyPLANrunCommand);
			 * process.waitFor(); process.destroy(); epfp = new
			 * EnergyPLANFileParseForCivis(".\\result.txt"); energyplanmMap =
			 * epfp.parseFile();
			 */

		
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
			
			col = (Collection<String>) energyplanMap.get("Annualchpelec.");
			it = col.iterator();
			double chpElecProduction = Double.parseDouble(it.next().toString());
			
			col = (Collection<String>) energyplanMap.get("Annualhpelec.");
			it = col.iterator();
			double hpElecConsumption = Double.parseDouble(it.next().toString());

			// calculate additional cost
			// (hydroProduction+PVproduction+Import-Export)*average additional
			// cost (85.74)
			double additionalCost = Math.round((hydroPowerProduction
					+ PVproduction + Import - Export + chpElecProduction ) * 85.74);

			double reductionInvestmentCost;
			// reduced investment cost = current PV
			// capccity *
			// pv cost + Hydro * hydro cost
			// see anual investment cost formual in EnergyPLAN manual

			reductionInvestmentCost = (currentPVCapacity * 3.978 * interest)
					/ (1 - Math.pow((1 + interest), -PVLifeTime))
					+ (currentHydroCapcity * 3.51 * interest)
					/ (1 - Math.pow((1 + interest), -HydroLifeTime));

			// extracting maximum Boiler configuration (group # 3)
			col = (Collection<String>) energyplanMap.get("Maximumboilerheat");
			it = col.iterator();
			double maximumBoilerGroup2 = Double.parseDouble(it.next()
					.toString());
			energyplanMap.put("Boiler_Cap", maximumBoilerGroup2);

			reductionInvestmentCost = reductionInvestmentCost + Math
					.round(((largeValueOfBoiler - maximumBoilerGroup2)
							* boilerCostInKEuro * interest)
							/ (1 - Math.pow((1 + interest), -boilerLifeTime)));

			double reduceFixedOMCost = Math
					.round(((largeValueOfBoiler - maximumBoilerGroup2)
							* boilerCostInKEuro * fixedMOForBoilerinPercentage));

			//double numberOfHeatPump = Math.round(maxHeatDemandInScaleOf1
					//* HPheatDemand * Math.pow(10, 6) / COP);
			//double geoBoreHoleInvestmentCost = (numberOfHeatPump * 3.2 * interest)
			//		/ (1 - Math.pow((1 + interest), -geoBoreHoleLifeTime));

			// extract
			col = (Collection<String>) energyplanMap
					.get("Annual Investment costs");
			it = col.iterator();
			String invest = it.next().toString();
			String investmentCostStr = invest.substring(0,
					invest.lastIndexOf("1000"));
			double investmentCost = Double.parseDouble(investmentCostStr);
			double realInvestmentCost = investmentCost
					- reductionInvestmentCost ;//+ geoBoreHoleInvestmentCost;

			double actualAnnualCost = totalVariableCost + fixedOperationalCost -reduceFixedOMCost
					+ realInvestmentCost + additionalCost ;

			
			// 3rd objective
			col = (Collection<String>) energyplanMap.get("Annualelec.demand");
			it = col.iterator();
			double annualElecDemand = Double.parseDouble(it.next().toString());

			//solution.setObjective(2, (Import + Export) / annualElecDemand);

			
			energyplanMap.put("AdditionalCost", additionalCost);
			energyplanMap.put("InvestmentCost", realInvestmentCost);
			energyplanMap.put("AnnualCost", actualAnnualCost);
			energyplanMap.put("LoadFollowingCapacity", (Import+Export)/annualElecDemand);
	
							   
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
		// chp2
	double chp2 = Double.parseDouble(st.nextToken().toString());
		// hp2
	double hp2 = Double.parseDouble(st.nextToken().toString());
	// PV
	double pv = Double.parseDouble(st.nextToken().toString());
		
				
		modifyMap.put(inputs[0], (int) Math.round(chp2));
		modifyMap.put(inputs[1], (int) Math.round(hp2));
		modifyMap.put(inputs[2], (int) Math.round(pv));
		
		
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

			str = "input_cap_chp2_el=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(chp2);
			bw.write(str);
			bw.newLine();

			str = "input_cap_hp2_el=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(hp2);
			bw.write(str);
			bw.newLine();
			
			bw.close();

			// file.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return modifyMap;
	}
}
