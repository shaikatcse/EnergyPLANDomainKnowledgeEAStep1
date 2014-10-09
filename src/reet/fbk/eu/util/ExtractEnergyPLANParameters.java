package reet.fbk.eu.util;

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

import reet.fbk.eu.OprimizeEnergyPLAN.file.parse.EnergyPLANFileParse;

public class ExtractEnergyPLANParameters {

	// static MultiMap energyplanmMap;

	static String inputs[]={"CHP3_Cap","HP3_Cap", "PP_Cap", "OnShoreWind_Cap","OffShoreWind_Cap","PV_Cap", "Boiler_Cap"};
	static String outputs[]={"Annualchp3heat", "Annualchpelec.", "Annualhp3heat", "Annualppelec.", "Annualwindpower", "AnnualOffshorewind",
		"AnnualPV", "Annualboilerheat r", "Annualimport", "Annualexport", "CO2-emission (corrected)", "TOTAL ANNUAL COSTS"
	};
	
	public ExtractEnergyPLANParameters() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {

		
		MultiMap energyplanmMap = null;
		// TODO Auto-generated method stub
		FileInputStream fos = new FileInputStream(args[0]);
		InputStreamReader isr = new InputStreamReader(fos);
		BufferedReader br = new BufferedReader(isr);

		String line;
		while ((line = br.readLine()) != null) {
			energyplanmMap = simulateEnergyPLAN(line);
			WriteEnergyPLANParametersToFile(energyplanmMap, args[0]);
		}

		br.close();
	}

	static void WriteEnergyPLANParametersToFile(MultiMap energyPLANMap,
			String path) throws IOException {
		Iterator it;
		Collection<String> col;

		File filetmp = new File(path);
		String arrayStr[]=path.split("\\\\");
		
		File file = new File(filetmp.getParent() + "\\allParameters_"+arrayStr[arrayStr.length-1] +".txt");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();

			// create header of the file
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write("CHP3_C HP3_C PP_Cap Wind_C OffShoreWind_C PV_C Boiler3_C "
					+ "Anl_CHP3_heat_P Anl_CHP_elec_P Anl_HP3_heat_P Anl_PP_P Anl_Wind_P Anl_OffShoreWind_P "
					+ "Anl_PV_P Anl_Boiler_P Import Export CO2Emission Annual_Cost");
			bw.newLine();
			bw.write("(MW) (MW) (MW) (MW) (MW) (MW) (MJ) "
					+ "(TWh) (TWh) (TWh) (TWh) (TWh) (TWh) "
					+ "(TWh) (TWh) (TWh) (TWh) (Mton) (DKK)");
			bw.newLine();
			bw.close();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);

		/*// extract EnergyPLAN input data
		String input = "";
		String tmpStr = "";
		// CHP3 capacity
		col = (Collection<String>) energyPLANMap.get("CHP3_Cap");
		it = col.iterator();
		tmpStr = "" + (int) Double.parseDouble(it.next().toString());
		input = input + tmpStr + " ";

		// HP3 capacity
		col = (Collection<String>) energyPLANMap.get("HP3_Cap");
		it = col.iterator();
		tmpStr = "" + (int) Double.parseDouble(it.next().toString());
		input = input + tmpStr + " ";

		// PP capacity
		col = (Collection<String>) energyPLANMap.get("PP_Cap");
		it = col.iterator();
		tmpStr = "" + (int) Double.parseDouble(it.next().toString());
		input = input + tmpStr + " ";

		// OnShaore wind capacity
		col = (Collection<String>) energyPLANMap.get("OnShoreWind_Cap");
		it = col.iterator();
		tmpStr = "" + (int) Double.parseDouble(it.next().toString());
		input = input + tmpStr + " ";

		// OffShaore wind capacity
		col = (Collection<String>) energyPLANMap.get("OffShoreWind_Cap");
		it = col.iterator();
		tmpStr = "" + (int) Double.parseDouble(it.next().toString());
		input = input + tmpStr + " ";

		// PV capacity
		col = (Collection<String>) energyPLANMap.get("PV_Cap");
		it = col.iterator();
		tmpStr = "" + (int) Double.parseDouble(it.next().toString());
		input = input + tmpStr + " ";

		// Boiler capacity
		col = (Collection<String>) energyPLANMap.get("Boiler_Cap");
		it = col.iterator();
		tmpStr = "" + (int) Double.parseDouble(it.next().toString());
		input = input + tmpStr + " ";

		bw.write(input);

		// extract energyPLAN output
		String output = "";

		// Annual chp3 heat prduction
		col = (Collection<String>) energyPLANMap.get("Annualchp3heat");
		it = col.iterator();
		output = output + it.next().toString() + " ";

		// Annual hp3 heat production
		col = (Collection<String>) energyPLANMap.get("Annualhp3heat");
		it = col.iterator();
		output = output + it.next().toString() + " ";

		// Anuual PP electricity production
		col = (Collection<String>) energyPLANMap.get("AnnaulPPelec.");
		it = col.iterator();
		output = output + it.next().toString() + " ";

		// Annual OnShaore wind production
		col = (Collection<String>) energyPLANMap.get("Annaulwindpower");
		it = col.iterator();
		output = output + it.next().toString() + " ";

		// Annual OffShaore wind production
		col = (Collection<String>) energyPLANMap.get("AnnaulOffshorewind");
		it = col.iterator();
		output = output + it.next().toString() + " ";

		// Annual OffShaore wind production
		col = (Collection<String>) energyPLANMap.get("AnnaulPV");
		it = col.iterator();
		output = output + it.next().toString() + " ";

		// Annual boiler Production
		col = (Collection<String>) energyPLANMap.get("Annualboilerheat r");
		it = col.iterator();
		output = output + it.next().toString() + " ";

		// Annual import
		col = (Collection<String>) energyPLANMap.get("Annualimport");
		it = col.iterator();
		output = output + it.next().toString() + " ";

		// Annual export
		col = (Collection<String>) energyPLANMap.get("Annualexport");
		it = col.iterator();
		output = output + it.next().toString() + " ";

		// CO2 emission
		col = (Collection<String>) energyPLANMap
				.get("CO2-emission (corrected)");
		it = col.iterator();
		output = output + it.next().toString() + " ";

		// Annual cost
		col = (Collection<String>) energyPLANMap.get("TOTAL ANNUAL COSTS");
		it = col.iterator();
		output = output + it.next().toString() + " ";

		bw.write(output);*/
		
		String input="", output="";
		for(int i=0;i<inputs.length;i++){
			col = (Collection<String>) energyPLANMap.get(inputs[i]);
			it = col.iterator();
			input = input + it.next() + " ";
		}
		for(int i=0;i<outputs.length;i++){
			col = (Collection<String>) energyPLANMap.get(outputs[i]);
			it = col.iterator();
			output = output + it.next() + " ";
		}
		
		bw.write(input+output);
		bw.newLine();
		bw.close();

	}

	static MultiMap simulateEnergyPLAN(String individual) {
		MultiMap energyplanmMap = null;
		writeModificationFile(individual);
		String energyPLANrunCommand = ".\\EnergyPLAN_SEP_2013\\EnergyPLAN.exe -i "
				+ "\".\\src\\reet\\fbk\\eu\\OptimizeEnergyPLANAalborg\\Aalborg_2050_Plan_A_44%ForOptimization_2objctives.txt\" "
				+ "-m \"modification.txt\" -ascii \"result.txt\" ";
		try {
			// Process process = new
			// ProcessBuilder(energyPLANrunCommand).start();
			Process process = Runtime.getRuntime().exec(energyPLANrunCommand);
			process.waitFor();
			process.destroy();
			EnergyPLANFileParse epfp = new EnergyPLANFileParse(".\\result.txt");
			energyplanmMap = epfp.parseFile();

			/*Iterator it;
			Collection<String> col;

			// extracting maximum Boiler configuration (group # 3)
			col = (Collection<String>) energyplanmMap
					.get("Maximumboilerheat r");
			it = col.iterator();
			double maximumBoilerGroup3 = Double.parseDouble(it.next()
					.toString());
			modifyModificationFile(maximumBoilerGroup3);

			// run EnergyPLAN for 2nd time, after adjust the boiler3 capacity
			process = Runtime.getRuntime().exec(energyPLANrunCommand);
			process.waitFor();
			process.destroy();
			epfp = new EnergyPLANFileParse(".\\result.txt");
			energyplanmMap = epfp.parseFile();*/

			// put inputs into energyPLANMap
			StringTokenizer st = new StringTokenizer(individual);
		
			int i=0;
			while(st.hasMoreTokens()){
				String tmp = ""+(int)Math.round(Double.parseDouble(st.nextToken()));
				energyplanmMap.put(inputs[i], tmp);
				i++;
			}
			/*energyplanmMap.put(inputs[i], (int)maximumBoilerGroup3);

			// check warning
			col = (Collection<String>) energyplanmMap.get("WARNING");
			if (col != null) {
				/*
				 * System.out.println("No warning"); } else {
				 */
			/*	@SuppressWarnings("rawtypes")
				Iterator it3 = col.iterator();
				String warning = it3.next().toString();
				if (!warning.equals("PP too small. Critical import is needed")
						&& !warning
								.equals("Grid Stabilisation requierments are NOT fullfilled"))
					throw new IOException("warning!!" + warning);
				// System.out.println("Warning " + it3.next().toString());

			}*/
		} catch (IOException e) {
			System.out.println("Energyplan.exe has some problem");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Energyplan interrupted");
		}

		return energyplanmMap;

	}

	static void writeModificationFile(String individual) {
		StringTokenizer st = new StringTokenizer(individual);
		// CHP group 3
		double CHPGr3 = Double.parseDouble(st.nextElement().toString());

		// HP group 3
		double HPGr3 = Double.parseDouble(st.nextElement().toString());

		// PP
		double PP = Double.parseDouble(st.nextElement().toString());

		// wind
		double wind = Double.parseDouble(st.nextElement().toString());
		// off-shore wind
		double offShoreWind = Double.parseDouble(st.nextElement().toString());
		// PV
		double PV = Double.parseDouble(st.nextElement().toString());
		
		// boiler
		double boiler = Double.parseDouble(st.nextElement().toString());
		// heat starage group 3
		// double heatStorageGr3 =
		// solution.getDecisionVariables()[6].getValue();

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

			str = "input_cap_chp3_el=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(CHPGr3);
			//str = "" + (int) CHPGr3;
			bw.write(str);
			bw.newLine();

			str = "input_cap_hp3_el=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(HPGr3);
			//str = "" + (int) HPGr3;
			bw.write(str);
			bw.newLine();

			str = "input_cap_pp_el=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(PP);
			//str = "" + (int) PP;
			bw.write(str);
			bw.newLine();

			str = "input_RES1_capacity=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(wind);
			//str = "" + (int) wind;
			bw.write(str);
			bw.newLine();

			str = "input_RES2_capacity=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(offShoreWind);
			//str = "" + (int) offShoreWind;
			bw.write(str);
			bw.newLine();

			str = "input_RES3_capacity=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(PV);
			//str = "" + (int) PV;
			bw.write(str);
			bw.newLine();
			
			str = "input_cap_boiler3_th=";
			bw.write(str);
			bw.newLine();
			str = "" + (int) Math.round(boiler);
			//str = "" + (int) modification;
			bw.write(str);
			bw.newLine();
			
			bw.close();
			// file.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	static void modifyModificationFile(double modification) {
		// now only modify boiler in group # 3
		try {

			File file = new File("modification.txt");
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);

			String str = "input_cap_boiler3_th=";
			bw.write(str);
			bw.newLine();
			// str = "" + (int) Math.round(modification);
			str = "" + (int) modification;
			bw.write(str);
			bw.newLine();

			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
