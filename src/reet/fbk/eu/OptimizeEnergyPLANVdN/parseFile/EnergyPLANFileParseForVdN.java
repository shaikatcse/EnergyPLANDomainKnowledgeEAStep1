package reet.fbk.eu.OptimizeEnergyPLANVdN.parseFile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

public class EnergyPLANFileParseForVdN{

	BufferedReader br = null;
	MultiMap multiMap = new MultiValueMap();

	public EnergyPLANFileParseForVdN(String filePath) {
		try {
			br = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		}
	}

	public MultiMap parseFile() {

		try {

			String line;
			boolean trackAnnualCost = false;

			/*while ((line = br.readLine()) != null) {
				//System.out.println(line);

				StringTokenizer stringTokenizerbyEqual = new StringTokenizer(
						line, "=");
				StringTokenizer stringTokenizerbyColon = new StringTokenizer(
						line, ":");

				if (stringTokenizerbyEqual.countTokens() > 1) {

					String tempKey = stringTokenizerbyEqual.nextToken();
					String key = tempKey.trim();

					if (key.equals("TOTAL ANNUAL COSTS"))
						trackAnnualCost = true;
					while (stringTokenizerbyEqual.hasMoreElements()) {
						String str = stringTokenizerbyEqual.nextToken();
						String str1 = str.replaceAll("[^0-9.-]", "");

						// String str1 = str.replaceAll("[^0-9.]", "");
						// String str1 = str.replaceAll("\\s+", "");
						// String str2 = str1.replaceAll("[^a-zA-Z]", "");
						multiMap.put(key, str1);
					//	System.out.println(key + " " + str1);

					}
				} else if (stringTokenizerbyColon.countTokens() > 1) {
					String tmpKey = stringTokenizerbyColon.nextToken();

					String key = tmpKey.replaceAll("[^a-zA-Z ]", "");
					key = key.trim();

					while (stringTokenizerbyColon.hasMoreElements()) {
						String str = stringTokenizerbyColon.nextToken();
						String str1 = new String();
						if (key.equals("WARNING")){
							str1 = str.replaceAll("[^a-zA-Z. ]", "");
							str1 = str1.trim();
						}
						else {

							str1 = str.replaceAll("[^0-9.-]", "");
						}
						multiMap.put(key, str1);
						//System.out.println(key + " " + str1);

					}
				}
				if (trackAnnualCost == true)
					break;
			}

			String temp = br.readLine();
			temp = br.readLine();

			String line1 = br.readLine();
			String line2 = br.readLine();

			String l1[] = line1.split("\0");
			String l2[] = line2.split("\0");

			for (int i = 0; i < l1.length; i++) {
				l1[i] = l1[i].trim();
				l2[i] = l2[i].trim();

			}*/
			/*for (int i = 1; i < l1.length; i++) {
				System.out.println(l1[i] + " " + l2[i]);
			}*/
			
			//read 2 lines starting with "ANNUAL CO2 EMISSIONS (kt):"
			while (!(line = br.readLine()).startsWith("ANNUAL CO2 EMISSIONS (kt):") ) {
				;
			}
			
			//1st line
			line = br.readLine();
			String StringSplit[] = line.split("\0");
			String tmpKey=StringSplit[0].trim(); String tmpValue = StringSplit[1].trim();
			multiMap.put(tmpKey, tmpValue);
			//2nd line
			line = br.readLine();
			StringSplit = line.split("\0");
			tmpKey=StringSplit[0].trim(); tmpValue = StringSplit[1].trim();
			multiMap.put(tmpKey, tmpValue);
			
			//read 3 lines starting with "SHARE OF RES (incl. Biomass):"
			while (!(line = br.readLine()).startsWith("SHARE OF RES (incl. Biomass):") ) {
				;
			}
			
			//1st line
			line = br.readLine();
			StringSplit = line.split("\0");
			tmpKey=StringSplit[0].trim(); tmpValue = StringSplit[1].trim();
			multiMap.put(tmpKey, tmpValue);
			//2nd line
			line = br.readLine();
			StringSplit = line.split("\0");
			tmpKey=StringSplit[0].trim(); tmpValue = StringSplit[1].trim();
			multiMap.put(tmpKey, tmpValue);
			//3rd line
			line = br.readLine();
			StringSplit = line.split("\0");
			tmpKey=StringSplit[0].trim(); tmpValue = StringSplit[1].trim();
			multiMap.put(tmpKey, tmpValue);
			
			//read 12 lines starting with "ANNUAL FUEL CONSUMPTIONS (GWh/year)"
			while (!(line = br.readLine()).startsWith("ANNUAL FUEL CONSUMPTIONS (GWh/year)") ) {
				;
			}
			for(int i=0;i<12;i++){
				line = br.readLine();
				StringSplit = line.split("\0");
				tmpKey=StringSplit[0].trim(); tmpValue = StringSplit[1].trim();
				multiMap.put(tmpKey, tmpValue);
								
			}
			
			//read 9 lines starting with "ANNUAL COSTS (k EUR)"
			while (!(line = br.readLine()).startsWith("ANNUAL COSTS (k EUR)") ) {
				;
			}
			for(int i=0;i<9;i++){
				line = br.readLine();
				StringSplit = line.split("\0");
				tmpKey=StringSplit[0].trim(); tmpValue = StringSplit[1].trim();
				multiMap.put(tmpKey, tmpValue);
			}
			
			//read "Ngas Exchange costs"
			while (!(line = br.readLine()).startsWith("Ngas Exchange costs") ) {
				;
			}
			StringSplit = line.split("\0");
			tmpKey=StringSplit[0].trim(); tmpValue = StringSplit[2].trim();
			multiMap.put(tmpKey, tmpValue);
		
			//read "Electricity exchange"
			while (!(line = br.readLine()).startsWith("Electricity exchange") ) {
				;
			}
			StringSplit = line.split("\0");
			tmpKey=StringSplit[0].trim(); tmpValue = StringSplit[2].trim();
			multiMap.put(tmpKey, tmpValue);
			//read import
			line = br.readLine();
			StringSplit = line.split("\0");
			tmpKey=StringSplit[0].trim(); tmpValue = StringSplit[3].trim();
			multiMap.put(tmpKey, tmpValue);
			//read export
			line = br.readLine();
			StringSplit = line.split("\0");
			tmpKey=StringSplit[0].trim(); tmpValue = StringSplit[3].trim();
			multiMap.put(tmpKey, tmpValue);
			
			
			//read "Variable costs"
			while (!(line = br.readLine()).startsWith("Variable costs") ) {
				;
			}
			StringSplit = line.split("\0");
			tmpKey=StringSplit[0].trim(); tmpValue = StringSplit[1].trim();
			multiMap.put(tmpKey, tmpValue);
			//read "Fixed operation costs"
			while (!(line = br.readLine()).startsWith("Fixed operation costs") ) {
				;
			}
			StringSplit = line.split("\0");
			tmpKey=StringSplit[0].trim(); tmpValue = StringSplit[1].trim();
			multiMap.put(tmpKey, tmpValue);
			
			//read "Annual Investment costs"
			while (!(line = br.readLine()).startsWith("Annual Investment costs") ) {
				;
			}
			StringSplit = line.split("\0");
			tmpKey=StringSplit[0].trim(); tmpValue = StringSplit[1].trim();
			multiMap.put(tmpKey, tmpValue);
			
			//read "TOTAL ANNUAL COSTS"
			while (!(line = br.readLine()).startsWith("TOTAL ANNUAL COSTS") ) {
				;
			}
			StringSplit = line.split("\0");
			tmpKey=StringSplit[0].trim(); tmpValue = StringSplit[1].trim();
			multiMap.put(tmpKey, tmpValue);
			
			//skip 12 lines
			for(int i=0;i<12;i++){
				String temp = br.readLine();
			}
		
			String line1 = br.readLine();
			String line2 = br.readLine();

			String l1[] = line1.split("\0");
			String l2[] = line2.split("\0");

			for (int i = 0; i < l1.length; i++) {
				l1[i] = l1[i].trim();
				l2[i] = l2[i].trim();

			}
			while (!(line = br.readLine()).equals("TOTAL FOR ONE YEAR (GWh/year):")) {
				;
			}
			// read annual line
			line = br.readLine();
			String lineTmp[] = line.split("\0");
			for (int i = 0; i < lineTmp.length; i++) {
				lineTmp[i] = lineTmp[i].trim();
			}

			for (int i = 1; i < lineTmp.length; i++) {
				String key = "Annual" + l1[i] + l2[i];
				String value = lineTmp[i];
				multiMap.put(key, value);

			}

			// read monthly line
			line = br.readLine();
			line = br.readLine();

			for (int j = 0; j < 12; j++) {
				line = br.readLine();
				lineTmp = line.split("\0");
				for (int i = 0; i < lineTmp.length; i++) {
					lineTmp[i] = lineTmp[i].trim();
				}
				for (int i = 1; i < lineTmp.length; i++) {
					String key = lineTmp[0] + l1[i] + l2[i];
					String value = lineTmp[i];
					multiMap.put(key, value);

				}
			}
			
			//read average, maximum, minimum
			line = br.readLine();
			for (int j = 0; j < 3; j++) {
				line = br.readLine();
				lineTmp = line.split("\0");
				for (int i = 0; i < lineTmp.length; i++) {
					lineTmp[i] = lineTmp[i].trim();
				}
				for (int i = 1; i < lineTmp.length; i++) {
					String key = lineTmp[0] + l1[i] + l2[i];
					String value = lineTmp[i];
					multiMap.put(key, value);

				}
			}
			
			//read hour values
			/*line = br.readLine();
			line = br.readLine();
			for (int j = 0; j < 8784; j++) {
				line = br.readLine();
				lineTmp = line.split("\0");
				for (int i = 0; i < lineTmp.length; i++) {
					lineTmp[i] = lineTmp[i].trim();
				}
				for (int i = 1; i < lineTmp.length; i++) {
					String key = lineTmp[0] + l1[i] + l2[i];
					String value = lineTmp[i];
					multiMap.put(key, value);

				}
			}*/
			

			//additional calculation
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return multiMap;
	}
	@SuppressWarnings("unchecked")
	public static void main(String args[]){
		MultiMap energyplanmMap;
		EnergyPLANFileParseForVdN epfp = new EnergyPLANFileParseForVdN(".\\result.txt");
		energyplanmMap = epfp.parseFile();
		
		Iterator it;
		Collection<String> col;

		// objective # 1
		col = (Collection<String>) energyplanmMap
				.get("CO2-emission (corrected)");
		it = col.iterator();
		
		// objective # 2
		col = (Collection<String>) energyplanmMap
				.get("Variable costs");
		it = col.iterator();
		String totalVariableCostStr = it.next().toString();
		double totalVariableCost = Double.parseDouble(totalVariableCostStr);

		col = (Collection<String>) energyplanmMap
				.get("Fixed operation costs");
		it = col.iterator();
		String fixedOperationalCostStr = it.next().toString();
		double fixedOperationalCost = Double
				.parseDouble(fixedOperationalCostStr);

		col = (Collection<String>) energyplanmMap.get("AnnualHydroElectr.");
		it = col.iterator();
		double hydroPowerProduction = Double.parseDouble(it.next()
				.toString());
		// extract anual PV production
		col = (Collection<String>) energyplanmMap.get("AnnualPVElectr.");
		it = col.iterator();
		double PVproduction = Double.parseDouble(it.next().toString());
		
	
		// extract annual import
		col = (Collection<String>) energyplanmMap.get("AnnualImportElectr.");
		it = col.iterator();
		double Import = Double.parseDouble(it.next().toString());

		// extract annual export
		col = (Collection<String>) energyplanmMap.get("AnnualExportElectr.");
		it = col.iterator();
		double Export = Double.parseDouble(it.next().toString());

		//extract biomass CHP electricity production
		col = (Collection<String>) energyplanmMap.get("AnnualHH-CHPElectr.");
		it = col.iterator();
		double biomassCHPElecProduction = Double.parseDouble(it.next().toString());

		

		col = (Collection<String>) energyplanmMap
				.get("Annual Investment costs");
		it = col.iterator();
		String invest = it.next().toString();
		double investmentCost = Double.parseDouble(invest);
		
		col = (Collection<String>) energyplanmMap
				.get("AnnualFlexibleElectr.");
		it = col.iterator();
		double transportElecDemand = Double.parseDouble(it.next().toString());
		
		
		col = (Collection<String>) energyplanmMap.get("AnnualElectr.Demand");
		it = col.iterator();
		double annualElecDemand = Double.parseDouble(it.next().toString());

		//Individual house HP electric demand
		col = (Collection<String>) energyplanmMap.get("AnnualHH-HPElectr.");
		it = col.iterator();
		double annualHPdemand = Double.parseDouble(it.next().toString());
		
		
		
		
		col = (Collection<String>) energyplanmMap
				.get("Ngas Consumption");
		it = col.iterator();
		double nGasConsumption = Double.parseDouble(it.next().toString());
		
		//extract oil consumption
		col = (Collection<String>) energyplanmMap
				.get("Oil ConsumptionHOUSEHOLDS");
		it = col.iterator();
		double oilConsumption = Double.parseDouble(it.next().toString());
		
		//extract biomass consupmtion
		col = (Collection<String>) energyplanmMap.get("Biomass Consumption");
		it = col.iterator();
		double BiomassConsumption = Double.parseDouble(it.next().toString());
		
		//extract solar thermal heat
		col = (Collection<String>) energyplanmMap.get("AnnualHH SolarHeat");
		it = col.iterator();
		double solaThermal = Double.parseDouble(it.next().toString());
		
		
	
	}
	
	

}