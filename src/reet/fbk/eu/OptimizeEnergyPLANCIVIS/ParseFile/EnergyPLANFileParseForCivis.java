package reet.fbk.eu.OptimizeEnergyPLANCIVIS.ParseFile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.StringTokenizer;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

public class EnergyPLANFileParseForCivis{

	BufferedReader br = null;
	MultiMap multiMap = new MultiValueMap();

	public EnergyPLANFileParseForCivis(String filePath) {
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

			while ((line = br.readLine()) != null) {
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

			}
			/*for (int i = 1; i < l1.length; i++) {
				System.out.println(l1[i] + " " + l2[i]);
			}*/
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
	
	public static void main(String args[]){
		MultiMap energyplanmMap;
		EnergyPLANFileParseForCivis epfp = new EnergyPLANFileParseForCivis(".\\result.txt");
		energyplanmMap = epfp.parseFile();
	}

}