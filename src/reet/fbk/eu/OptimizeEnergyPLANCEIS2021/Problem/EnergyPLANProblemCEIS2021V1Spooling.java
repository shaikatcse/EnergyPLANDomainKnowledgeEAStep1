package reet.fbk.eu.OptimizeEnergyPLANCEIS2021.Problem;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.jfree.date.AnnualDateRule;

import reet.fbk.eu.OptimizeEnergyPLANCEIS2021.parseFile.EnergyPLANFileParseForCEIS2021;
import reet.fbk.eu.OptimizeEnergyPLANCEIS2021.util.ExtractEnergyPLANParametersCEIS2021;

class DecisionVarible{
	ArrayList <String> EnergyPLANVariableName;
	int index;
	double lowerBound, upperBound;
	String catagory;
	
	DecisionVarible(ArrayList <String> EnergyPLANVariableName, int index, 
			double lowerbound, double upperBound, String catagory){
		this.EnergyPLANVariableName = EnergyPLANVariableName;
		this.index = index;
		this.lowerBound = lowerbound;
		this.upperBound = upperBound;
		this.catagory = catagory;
	}
	
}

/*
 * This is a problem file that is dealing with VdN (mainly introduction of electric cars)
 */
public class EnergyPLANProblemCEIS2021V1Spooling extends Problem {

	HashMap<ArrayList<String>, MultiMap> hmForAllSolutions;
	ExtractEnergyPLANParametersCEIS2021 ex;

	MultiMap energyplanmMap;
	String simulatedYear, folder;

	public static final int averageKMPerYearPerCar = 12900;
	
	HashMap<String, DecisionVarible> dvHM=new HashMap<String, DecisionVarible>();
	
	// transport
	public long totalKMRunByCars;

	// other variable
	public double totalHeatDemand;

	// cars efficiencies
	public double efficiencyDieselCar; // KWh/km
	public double efficiencyH2Car; // KWh/km
	public double efficiencyEVCar; // KWh/km

	
	// H2
	public double efficiencyElectrolyzer;

	public double coalBoilerEfficiency;
	public double oilBoilerEfficiency;
	public double nGasBoilerEfficiency;
	public double biomassBoilerEfficiency;
	public double COP;


	/**
	 * Creates a new instance of problem ZDT1.
	 * 
	 * @param numberOfVariables
	 *            Number of variables.
	 * @param solutionType
	 *            The solution type must "Real", "BinaryReal, and "ArrayReal".
	 */

	public EnergyPLANProblemCEIS2021V1Spooling(String solutionType, String simulatedYear, String folder) {

		this.simulatedYear = simulatedYear;
		this.folder = folder;

		numberOfVariables_ = 35;
		numberOfObjectives_ = 2;
		numberOfConstraints_ = 3;

		problemName_ = "EnergyPLANProblemCEIS2021";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		
			
		int indexOfDV= 0;
		if(simulatedYear.equals("2030")){
		
		// decision variables
		// index - 0 -> HydroCap
		dvHM.put("RiverHydroCap", new DecisionVarible(new ArrayList<String>( Arrays.asList("input_RES3_capacity=")), indexOfDV++, 0, 4732, "Renewable"));
		// index - 1 -> PVCap
		dvHM.put("PVCap", new DecisionVarible(new ArrayList<String>( Arrays.asList("input_RES2_capacity=")), indexOfDV++, 0, 39420, "Renewable"));
		//index 2-> Biogas (considered as wind)
		dvHM.put("BioGasCap", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_RES1_capacity=")), indexOfDV++ ,0 , 350, "Renewable"));
		//index 3-> Battery1 (Electric storage 1)
		dvHM.put("Battery1", new DecisionVarible(new ArrayList<String> (Arrays.asList( "input_cap_pump_el=", //Pump capacity (20)
				"input_cap_turbine_el=", //turbine capacity (21)
				"input_storage_pump_cap=")) //storage cap 
				, indexOfDV++, 0, 13140, "El Storage 1")); 
		//index 4-> H2Storage 
		dvHM.put("H2Storage", new DecisionVarible(new ArrayList<String>( Arrays.asList("input_H2storage_trans_cap=")), indexOfDV++, 0, 13140, "H2 Storage"));
		//index 5-> P2PH2Electrolyzer (considered as elec storage 2 charge)
		dvHM.put("P2PH2Electrolyzer", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_cap_pump_el2=")), indexOfDV++, 0, 12528, "El Storage 2"));
		//index 6-> P2PH2Electrolyzer (considered as elec storage 2 discharge)
		dvHM.put("P2PH2FuelCell", new DecisionVarible(new ArrayList<String>( Arrays.asList("input_cap_turbine_el2=")), indexOfDV++, 0 , 8836, "El Storage 2"));
		//index 7-> P2PH2Storage (considered as elec storage 2 Storage)
		dvHM.put("P2PH2Storage", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_storage_pump_cap2=")), indexOfDV++, 0, 13140, "El Storage 2"));
		
		//index 8-> coalBoilerHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("coalBoilerHeatDemand", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_fuel_Households[1]=")), indexOfDV++, 0, 42.407, "Heat demand"));
		//index 9-> oilBoilerHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("oilBoilerHeatDemand", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_fuel_Households[2]=")), indexOfDV++, 0, 42.407, "Heat demand"));
		//index 10-> nGasBoilerHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("nGasBoilerHeatDemand", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_fuel_Households[3]=")), indexOfDV++, 0, 42.407, "Heat demand"));
		//index 11-> biomassBoilerHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("biomassBoilerHeatDemand", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_fuel_Households[4]=")), indexOfDV++, 0, 16.142, "Heat demand"));
		//index 12-> h2mCHPHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("h2mCHPHeatDemand", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_H2CHP_heat=")), indexOfDV++, 0, 42.407, "Heat demand"));
		//index 13-> nGasmCHPHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("nGasmCHPHeatDemand", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_NgasCHP_heat=")), indexOfDV++, 0, 42.407, "Heat demand"));
		//index 14-> biomassmCHPHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("biomassmCHPHeatDemand", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_BioCHP_heat=")), indexOfDV++, 0, 42.407, "Heat demand"));
		//index 15-> hpHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("hpHeatDemand", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_HP_heat=")), indexOfDV++, 0, 42.407, "Heat demand"));
				
		//index 16-> coalBoilerHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("coalBoilerSolarInput", new DecisionVarible(new ArrayList<String>( Arrays.asList("input_HH_coalboiler_Solar=")), indexOfDV++, 0, 1.0, "Solar Thermal"));
		//index 17-> oilBoilerHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("oilBoilerSolarInput", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_oilboiler_Solar=")), indexOfDV++, 0, 1.0, "Solar Thermal"));
		//index 18-> nGasBoilerHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("nGasBoilerSolarInput", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_ngasboiler_Solar=")), indexOfDV++, 0, 1.0, "Solar Thermal"));
		//index 19-> biomassBoilerHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("biomassBoilerSolarInput", new DecisionVarible(new ArrayList<String>( Arrays.asList("input_HH_bioboiler_Solar=")), indexOfDV++, 0, 1.0, "Solar Thermal"));
		//index 20-> h2mCHPHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("h2mCHPSolarInput", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_H2CHP_solar=")), indexOfDV++, 0, 1.0, "Solar Thermal"));
		//index 21-> nGasmCHPHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("nGasmCHPSolarInput", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_NgasCHP_solar=")), indexOfDV++, 0, 1.0, "Solar Thermal"));
		//index 22-> biomassmCHPHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("biomassmCHPSolarInput", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_BioCHP_solar=")), indexOfDV++, 0, 1.0, "Solar Thermal"));
		//index 23-> hpHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("hpSolarInput", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_HP_solar=")), indexOfDV++, 0, 1.0, "Solar Thermal"));
		
		//index 24-> coalBoilerHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("coalBoilerSolarStorage", new DecisionVarible(new ArrayList<String>(  Arrays.asList("Input_HH_coalboiler_Storage=")), indexOfDV++, 0, 366, "Solar storage"));
		//index 25-> oilBoilerHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("oilBoilerSolarStorage", new DecisionVarible(new ArrayList<String>( Arrays.asList("Input_HH_oilboiler_Storage=")), indexOfDV++, 0, 366, "Solar storage"));
		//index 26-> nGasBoilerHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("nGasBoilerSolarStorage", new DecisionVarible(new ArrayList<String>(  Arrays.asList("Input_HH_Ngasboiler_Storage=")), indexOfDV++, 0, 366, "Solar storage"));
		//index 27-> biomassBoilerHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("biomassBoilerSolarStorage", new DecisionVarible(new ArrayList<String>(  Arrays.asList("Input_HH_Bioboiler_Storage=")), indexOfDV++, 0, 366, "Solar storage"));
		//index 28-> h2mCHPHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("h2mCHPSolarStorage", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_H2CHP_storage=")), indexOfDV++, 0, 366, "Solar storage"));
		//index 29-> nGasmCHPHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("nGasmCHPSolarStorage", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_NgasCHP_storage=")), indexOfDV++, 0, 366, "Solar storage"));
		//index 30-> biomassmCHPHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("biomassmCHPSolarStorage", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_BioCHP_storage=")), indexOfDV++, 0, 366, "Solar storage"));
		//index 31-> hpHeatDemand (considered as elec storage 2 Storage)
		dvHM.put("hpSolarStorage", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_HP_storage=")), indexOfDV++, 0, 366, "Solar storage"));
		
		//index 32-> diselDemand (considered as elec storage 2 Storage)
		dvHM.put("dieselCarDemandInKM", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_fuel_Transport[2]=", 
				"Input_Size_transport_conventional_cars=")), indexOfDV++, 1, 129572000 , "Transport"));
		//index 33-> diselDemand (considered as elec storage 2 Storage)
		dvHM.put("elCarDemandInKM", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_transport_TWh=", // EV car electricity demand (15)
				"Input_Size_transport_electric_cars=")), indexOfDV++, 1, 129572000, "Transport"));
		//index 34-> diselDemand (considered as elec storage 2 Storage)
		dvHM.put("h2CarDemandInKM", new DecisionVarible(new ArrayList<String>( Arrays.asList("input_fuel_Transport[6]=")),  indexOfDV++, 1, 129572000, "Transport"));
	}else if (simulatedYear.equals("2050")) {
		// index - 0 -> HydroCap
				dvHM.put("RiverHydroCap", new DecisionVarible(new ArrayList<String>( Arrays.asList("input_RES3_capacity=")), indexOfDV++, 0, 4732, "Renewable"));
				// index - 1 -> PVCap
				dvHM.put("PVCap", new DecisionVarible(new ArrayList<String>( Arrays.asList("input_RES2_capacity=")), indexOfDV++, 0, 41845, "Renewable"));
				//index 2-> Biogas (considered as wind)
				dvHM.put("BioGasCap", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_RES1_capacity=")), indexOfDV++ ,0 , 350, "Renewable"));
				//index 3-> Battery1 (Electric storage 1)
				dvHM.put("Battery1", new DecisionVarible(new ArrayList<String> (Arrays.asList( "input_cap_pump_el=", //Pump capacity (20)
						"input_cap_turbine_el=", //turbine capacity (21)
						"input_storage_pump_cap=")) //storage cap 
						, indexOfDV++, 0, 13140, "El Storage 1")); 
				//index 4-> H2Storage 
				dvHM.put("H2Storage", new DecisionVarible(new ArrayList<String>( Arrays.asList("input_H2storage_trans_cap=")), indexOfDV++, 0, 13140, "H2 Storage"));
				//index 5-> P2PH2Electrolyzer (considered as elec storage 2 charge)
				dvHM.put("P2PH2Electrolyzer", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_cap_pump_el2=")), indexOfDV++, 0, 12528, "El Storage 2"));
				//index 6-> P2PH2Electrolyzer (considered as elec storage 2 discharge)
				dvHM.put("P2PH2FuelCell", new DecisionVarible(new ArrayList<String>( Arrays.asList("input_cap_turbine_el2=")), indexOfDV++, 0 , 8836, "El Storage 2"));
				//index 7-> P2PH2Storage (considered as elec storage 2 Storage)
				dvHM.put("P2PH2Storage", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_storage_pump_cap2=")), indexOfDV++, 0, 13140, "El Storage 2"));
				
				//index 8-> coalBoilerHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("coalBoilerHeatDemand", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_fuel_Households[1]=")), indexOfDV++, 0, 30.391, "Heat demand"));
				//index 9-> oilBoilerHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("oilBoilerHeatDemand", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_fuel_Households[2]=")), indexOfDV++, 0, 30.391, "Heat demand"));
				//index 10-> nGasBoilerHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("nGasBoilerHeatDemand", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_fuel_Households[3]=")), indexOfDV++, 0, 30.391, "Heat demand"));
				//index 11-> biomassBoilerHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("biomassBoilerHeatDemand", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_fuel_Households[4]=")), indexOfDV++, 0, 11.568, "Heat demand"));
				//index 12-> h2mCHPHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("h2mCHPHeatDemand", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_H2CHP_heat=")), indexOfDV++, 0, 30.391, "Heat demand"));
				//index 13-> nGasmCHPHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("nGasmCHPHeatDemand", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_NgasCHP_heat=")), indexOfDV++, 0, 30.391, "Heat demand"));
				//index 14-> biomassmCHPHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("biomassmCHPHeatDemand", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_BioCHP_heat=")), indexOfDV++, 0, 30.391, "Heat demand"));
				//index 15-> hpHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("hpHeatDemand", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_HP_heat=")), indexOfDV++, 0, 30.391, "Heat demand"));
						
				//index 16-> coalBoilerHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("coalBoilerSolarInput", new DecisionVarible(new ArrayList<String>( Arrays.asList("input_HH_coalboiler_Solar=")), indexOfDV++, 0, 1.0, "Solar Thermal"));
				//index 17-> oilBoilerHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("oilBoilerSolarInput", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_oilboiler_Solar=")), indexOfDV++, 0, 1.0, "Solar Thermal"));
				//index 18-> nGasBoilerHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("nGasBoilerSolarInput", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_ngasboiler_Solar=")), indexOfDV++, 0, 1.0, "Solar Thermal"));
				//index 19-> biomassBoilerHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("biomassBoilerSolarInput", new DecisionVarible(new ArrayList<String>( Arrays.asList("input_HH_bioboiler_Solar=")), indexOfDV++, 0, 1.0, "Solar Thermal"));
				//index 20-> h2mCHPHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("h2mCHPSolarInput", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_H2CHP_solar=")), indexOfDV++, 0, 1.0, "Solar Thermal"));
				//index 21-> nGasmCHPHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("nGasmCHPSolarInput", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_NgasCHP_solar=")), indexOfDV++, 0, 1.0, "Solar Thermal"));
				//index 22-> biomassmCHPHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("biomassmCHPSolarInput", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_BioCHP_solar=")), indexOfDV++, 0, 1.0, "Solar Thermal"));
				//index 23-> hpHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("hpSolarInput", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_HP_solar=")), indexOfDV++, 0, 1.0, "Solar Thermal"));
				
				//index 24-> coalBoilerHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("coalBoilerSolarStorage", new DecisionVarible(new ArrayList<String>(  Arrays.asList("Input_HH_coalboiler_Storage=")), indexOfDV++, 0, 366, "Solar storage"));
				//index 25-> oilBoilerHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("oilBoilerSolarStorage", new DecisionVarible(new ArrayList<String>( Arrays.asList("Input_HH_oilboiler_Storage=")), indexOfDV++, 0, 366, "Solar storage"));
				//index 26-> nGasBoilerHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("nGasBoilerSolarStorage", new DecisionVarible(new ArrayList<String>(  Arrays.asList("Input_HH_Ngasboiler_Storage=")), indexOfDV++, 0, 366, "Solar storage"));
				//index 27-> biomassBoilerHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("biomassBoilerSolarStorage", new DecisionVarible(new ArrayList<String>(  Arrays.asList("Input_HH_Bioboiler_Storage=")), indexOfDV++, 0, 366, "Solar storage"));
				//index 28-> h2mCHPHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("h2mCHPSolarStorage", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_H2CHP_storage=")), indexOfDV++, 0, 366, "Solar storage"));
				//index 29-> nGasmCHPHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("nGasmCHPSolarStorage", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_NgasCHP_storage=")), indexOfDV++, 0, 366, "Solar storage"));
				//index 30-> biomassmCHPHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("biomassmCHPSolarStorage", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_BioCHP_storage=")), indexOfDV++, 0, 366, "Solar storage"));
				//index 31-> hpHeatDemand (considered as elec storage 2 Storage)
				dvHM.put("hpSolarStorage", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_HH_HP_storage=")), indexOfDV++, 0, 366, "Solar storage"));
				
				//index 32-> diselDemand (considered as elec storage 2 Storage)
				dvHM.put("dieselCarDemandInKM", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_fuel_Transport[2]=", 
						"Input_Size_transport_conventional_cars=")), indexOfDV++, 1, 137674000, "Transport"));
				//index 33-> diselDemand (considered as elec storage 2 Storage)
				dvHM.put("elCarDemandInKM", new DecisionVarible(new ArrayList<String>(  Arrays.asList("input_transport_TWh=", // EV car electricity demand (15)
						"Input_Size_transport_electric_cars=")), indexOfDV++, 1, 137674000, "Transport"));
				//index 34-> diselDemand (considered as elec storage 2 Storage)
				dvHM.put("h2CarDemandInKM", new DecisionVarible(new ArrayList<String>( Arrays.asList("input_fuel_Transport[6]=")),  indexOfDV++, 1, 137674000, "Transport"));
				
	}
	
		
		//setting lower and upper limits	
		for (DecisionVarible value : dvHM.values()) {
			lowerLimit_[value.index] = value.lowerBound;
			upperLimit_[value.index] = value.upperBound;
		}
		
		if (solutionType.compareTo("Real") == 0)
			solutionType_ = new RealSolutionType(this);
		else {
			System.out.println("Error: solution type " + solutionType + " invalid");
			System.exit(-1);
		}

		if (this.simulatedYear.equals("2030")) {

			// Heat
			totalHeatDemand = 42.40; // in GWh

			// efficiencyBiomassBoilerThermal = 0.92;

			coalBoilerEfficiency = 0.97;
			oilBoilerEfficiency = 0.87;
			nGasBoilerEfficiency = 0.97;
			biomassBoilerEfficiency = 0.78;
			

			efficiencyDieselCar = 0.442; // KWh/km
			efficiencyEVCar = 0.133; // Kwh/km
			efficiencyH2Car = 0.239;
			totalKMRunByCars = 129572000L;

			efficiencyElectrolyzer = 0.7217;

			//

		
		}else if(this.simulatedYear.equals("2050")) {
			// Heat
			totalHeatDemand = 30.391; // in GWh

			// efficiencyBiomassBoilerThermal = 0.92;

			coalBoilerEfficiency = 0.98;
			oilBoilerEfficiency = 0.88;
			nGasBoilerEfficiency = 0.98;
			biomassBoilerEfficiency = 0.8;
			

			efficiencyDieselCar = 0.336; // KWh/km
			efficiencyEVCar = 0.106; // Kwh/km
			efficiencyH2Car = 0.181;
			totalKMRunByCars = 137674000L;
			
			efficiencyElectrolyzer = 0.7535;

			//

		}

		hmForAllSolutions = new HashMap();

		ex = new ExtractEnergyPLANParametersCEIS2021(simulatedYear);

		try {
			ex.readProfiles(simulatedYear);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} // constructor end

	public MultiMap createModificationFiles(Solution solution, int serial) throws JMException {

		MultiMap modifyMap = new MultiValueMap();
					
			repairHeatVariables(solution);
			repairTransVariables(solution);

			try {
				modifyMap = writeIntoInputFile(solution,".\\modifiedInput"+serial
				
						);
			} catch (IOException e) {
				System.out.print("Pobrlem writting in modified Input file");
			}

			return modifyMap;
		}
	

	public void simulateAllScenarios(int numberOfScenarios) {
		
		String runSpoolEnergyPLAN = ".\\EnergyPLAN161\\EnergyPLAN.exe -spool "+numberOfScenarios+ "  ";
		for(int i=0;i<numberOfScenarios;i++) {
			runSpoolEnergyPLAN = runSpoolEnergyPLAN + "modifiedInput"+i+".txt ";
		}
		runSpoolEnergyPLAN = runSpoolEnergyPLAN + "-ascii run";
		
			
		try {
			Process process = Runtime.getRuntime().exec(runSpoolEnergyPLAN);
			process.waitFor();
			process.destroy();

		} catch (IOException e) {
			System.out.println("Energyplan.exe has some problem");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Energyplan interrupted");
		}
		
	}
	
	public void extractInformation(Solution solution, MultiMap modifyMap, int serial) {

		
		EnergyPLANFileParseForCEIS2021 epfp = new EnergyPLANFileParseForCEIS2021(".\\EnergyPLAN161\\spool\\results\\modifiedInput"+serial+".txt.txt");
		energyplanmMap = epfp.parseFile();
		energyplanmMap.putAll(modifyMap);

		Iterator it;
		Collection<String> col;

		// objective # 1
		col = (Collection<String>) energyplanmMap.get("CO2-emission (total)");
		it = col.iterator();
		double localCO2emission = Double.parseDouble(it.next().toString());

		// extract import
		col = (Collection<String>) energyplanmMap.get("AnnualImportElectr.");
		it = col.iterator();
		double elecImport = Double.parseDouble(it.next().toString());

		double co2InImportedEleOil = 0.0, co2InImportedEleNGas = 0.0, co2InImportedEleCoal = 0.0;

		if(simulatedYear.equals("2030")) {
			co2InImportedEleOil=((elecImport/0.53*0.66/100)*0.267);
			co2InImportedEleNGas=((elecImport/0.53*43.94/100)*0.202);
		}else if(simulatedYear.equals("2050")) {
			co2InImportedEleOil=((elecImport/0.56*0.00/100)*0.267);
			co2InImportedEleNGas=((elecImport/0.56*12.00/100)*0.202);
		}
		
		localCO2emission = localCO2emission + co2InImportedEleOil + co2InImportedEleNGas ;
		energyplanmMap.put("CO2Emission", localCO2emission);

		solution.setObjective(0, localCO2emission);

		// objective # 2

		col = (Collection<String>) energyplanmMap.get("Variable costs");
		it = col.iterator();
		String totalVariableCostStr = it.next().toString();
		double variableCost = Double.parseDouble(totalVariableCostStr);

		col = (Collection<String>) energyplanmMap.get("Bottleneck");
		it = col.iterator();
		String bottleneckStr = it.next().toString();
		double bottlenack = Double.parseDouble(bottleneckStr);
		energyplanmMap.put("Bottleneck", bottlenack);

		variableCost -= bottlenack;

		col = (Collection<String>) energyplanmMap.get("Fixed operation costs");
		it = col.iterator();
		String fixedOperationalCostStr = it.next().toString();
		double fixedOperationalCost = Double.parseDouble(fixedOperationalCostStr);

		// extract
		col = (Collection<String>) energyplanmMap.get("Annual Investment costs");
		it = col.iterator();
		String invest = it.next().toString();
		double investmentCost = Double.parseDouble(invest);

		//Extra formulas: chp Investment cost
		col = (Collection<String>) energyplanmMap.get("h2mCHPHeatDemand");
		it = col.iterator();
		String AnnualH2mCHPheatStr = it.next().toString();
		double AnnualH2mCHPheatheat = Double.parseDouble(AnnualH2mCHPheatStr);

		double investmentCostAnnualH2mCHPheat=0.0; 
		if(simulatedYear.equals("2030")) {
			investmentCostAnnualH2mCHPheat = (((AnnualH2mCHPheatheat * Math.pow(10, 6)) / 15000)
				*  3.725 * 0.03)/ (1-Math.pow( (1+0.03) , (-20)) );  
		}else if(simulatedYear.equals("2050")) {
			investmentCostAnnualH2mCHPheat = (((AnnualH2mCHPheatheat * Math.pow(10, 6)) / 15000)
					*  3.725 * 0.03)/ (1-Math.pow( (1+0.03) , (-20)) ); 
		}
				
		investmentCost +=  investmentCostAnnualH2mCHPheat;
		
		double operationalCostAnnualH2mCHPheat = 0.0;
		if(simulatedYear.equals("2030")){
			operationalCostAnnualH2mCHPheat =   (AnnualH2mCHPheatheat*Math.pow(10,6)/15000)*0.0417*3.725;
		}else if(simulatedYear.equals("2050")){
			operationalCostAnnualH2mCHPheat =   (AnnualH2mCHPheatheat*Math.pow(10,6)/15000)*0.0417*3.725;
		}
		
		fixedOperationalCost += operationalCostAnnualH2mCHPheat;
		
		
		col = (Collection<String>) energyplanmMap.get("nGasmCHPHeatDemand");
		it = col.iterator();
		String AnnualnGasmCHPheatStr = it.next().toString();
		double AnnualnGasmCHPheat = Double.parseDouble(AnnualnGasmCHPheatStr);
		double FU_N_Gas_CHP= 0.284618279; 
		double investmentCostAnnualnGasmCHPheat = 0.0;
		if(simulatedYear.equals("2030")) {
			investmentCostAnnualnGasmCHPheat = ((AnnualnGasmCHPheat*Math.pow(10,6)/15000)*((0.9*1000*0.38*15)/(0.48*FU_N_Gas_CHP*366*24))
					*0.03)/(1-Math.pow((1+0.03),(-20))) + AnnualnGasmCHPheat*0.174*3600*0.03/(1- Math.pow((1+0.03),(-40)));
			
		}else if(simulatedYear.equals("2050")) {
			investmentCostAnnualnGasmCHPheat = ((AnnualnGasmCHPheat*Math.pow(10,6)/15000)*((0.9*1000*0.40*15)/(0.50*FU_N_Gas_CHP*366*24))
					*0.03)/(1-Math.pow((1+0.03),(-20)))+ 
					+ AnnualnGasmCHPheat*0.174*3600*0.03/(1- Math.pow((1+0.03),(-40)));
			
		}
		
		investmentCost += investmentCostAnnualnGasmCHPheat;
		
		double operationalCostAnnualnGasmCHPheat=0.0;
		if(simulatedYear.equals("2030")) {
			operationalCostAnnualnGasmCHPheat = (AnnualnGasmCHPheat*Math.pow(10,6)/15000)*0.0391*
					((0.9*1000*0.38*15)/(0.48*FU_N_Gas_CHP*366*24))+AnnualnGasmCHPheat*0.0076*0.174*3600;
		}else if(simulatedYear.equals("2050")) {
			operationalCostAnnualnGasmCHPheat = (AnnualnGasmCHPheat*Math.pow(10,6)/15000)*0.0399*
			((0.9*1000*0.40*15)/(0.48*FU_N_Gas_CHP*366*24))+AnnualnGasmCHPheat*0.0076*0.174*3600;
		}
		fixedOperationalCost += operationalCostAnnualnGasmCHPheat;
		
		
		col = (Collection<String>) energyplanmMap.get("biomassmCHPHeatDemand");
		it = col.iterator();
		String biomassmCHPHeatDemandStr = it.next().toString();
		double AnnualBiomassmCHPheat = Double.parseDouble(biomassmCHPHeatDemandStr);
		double FU_Biogas_CHP=0.284618279;
		double investmentCostBiomassmCHP=0.0;
		if(simulatedYear.equals("2030")) {
			investmentCostBiomassmCHP = ((AnnualBiomassmCHPheat *Math.pow(10,6) /15000) * ((5.5*1000*0.18*15)/(0.67*FU_Biogas_CHP*366*24))*
				0.03)/(1- Math.pow((1+0.03),(-20)))
				+AnnualBiomassmCHPheat*0.174*3600*0.03/(1-Math.pow((1+0.03),(-40)));	
		}else if(simulatedYear.equals("2050")) {
			investmentCostBiomassmCHP = ((AnnualBiomassmCHPheat *Math.pow(10,6) /15000) * ((5.3*1000*0.20*15)/(0.70*FU_Biogas_CHP*366*24))*
					0.03)/(1- Math.pow((1+0.03),(-20)))
					+AnnualBiomassmCHPheat*0.174*3600*0.03/(1-Math.pow((1+0.03),(-40)));
		}
		investmentCost += investmentCostBiomassmCHP;
		
		double operationalCostBiomassmCHP=0.0;
		if(simulatedYear.equals("2030")) {
			operationalCostBiomassmCHP = (AnnualBiomassmCHPheat*Math.pow(10,6)/15000)*0.0083*((5.5*1000*0.18*15)/
					(0.67*FU_Biogas_CHP*366*24))+AnnualBiomassmCHPheat*0.0076*0.174*3600;
		}else if (simulatedYear.equals("2050")) {
			operationalCostBiomassmCHP = (AnnualBiomassmCHPheat*Math.pow(10,6)/15000)*0.0085*((5.3*1000*0.20*15)/
					(0.7*FU_Biogas_CHP*366*24))+AnnualBiomassmCHPheat*0.0076*0.174*3600;
		}
		fixedOperationalCost += operationalCostBiomassmCHP;
		
		//grid distribution cost
		col = (Collection<String>) energyplanmMap.get("AnnualElectr.Demand");
		it = col.iterator();
		String fixedElecDemandStr = it.next().toString();
		double fixedElecDemand = Double.parseDouble(fixedElecDemandStr);

		col = (Collection<String>) energyplanmMap.get("elCarDemandInGWh");
		it = col.iterator();
		String elCarDemandInGWhStr = it.next().toString();
		double elCarDemandInGWh = Double.parseDouble(elCarDemandInGWhStr);

		col = (Collection<String>) energyplanmMap.get("AnnualHH-HPElectr.");
		it = col.iterator();
		String hpElecDemandStr = it.next().toString();
		double hpElecDemand = Double.parseDouble(hpElecDemandStr);

		//cost for H2 cars
		col = (Collection<String>) energyplanmMap.get("h2CarDemandInKM");
		it = col.iterator();
		String h2CarDemandInKMStr = it.next().toString();
		double h2CarDemandInKM = Long.parseLong(h2CarDemandInKMStr);
 
		double elForH2ForTransport = h2CarDemandInKM * 0.239 / 0.7217 / Math.pow(10 , 6);

		col = (Collection<String>) energyplanmMap.get("h2mCHPHeatDemand");
		it = col.iterator();
		String h2mCHPHeatDemandStr = it.next().toString();
		double h2mCHPHeatDemand = Double.parseDouble(h2mCHPHeatDemandStr);
		
		double elForH2Heat  = h2mCHPHeatDemand / 0.97 / 0.7217 / Math.pow(10 , 6);
		
		//el storage 1
		col = (Collection<String>) energyplanmMap.get("AnnualPumpElectr.");
		it = col.iterator();
		String elecStorage1ChargeStr = it.next().toString();
		double elecStorage1Charge = Double.parseDouble(elecStorage1ChargeStr);
		
		//el storage 2
		col = (Collection<String>) energyplanmMap.get("AnnualPump2Electr.");
		it = col.iterator();
		String elecStorage2ChargeStr = it.next().toString();
		double elecStorage2Charge = Double.parseDouble(elecStorage2ChargeStr);
		
		double elecGridDistributionCost=0.0;
		if(simulatedYear.equals("2030")) {
			elecGridDistributionCost=213.61*(1-0.48)*(fixedElecDemand
				+elCarDemandInGWh+hpElecDemand
				+elecStorage1Charge+ elecStorage2Charge+elForH2ForTransport+ elForH2Heat -elecImport ) + 213.61 * elecImport;
		}else if(simulatedYear.equals("2050")) {
			elecGridDistributionCost=236.31*(1-0.486)*(fixedElecDemand
					+elCarDemandInGWh+hpElecDemand
					+elecStorage1Charge+ elecStorage2Charge+elForH2ForTransport+ elForH2Heat -elecImport ) + 236.31 * elecImport;
		}
		variableCost += elecGridDistributionCost;
			
		double varibleCostForH2=0.0;
		if(simulatedYear.equals("2030")) {		
			varibleCostForH2=(h2mCHPHeatDemand/0.97)*58.81;
		}else if (simulatedYear.equals("2050")) {
			varibleCostForH2=(h2mCHPHeatDemand/0.98)*72.59;
		}
		variableCost += varibleCostForH2;
				
		//cost for H2 cars
		
		double investmentCostForH2Cars=0.0;
		if(simulatedYear.equals("2030")) {
			investmentCostForH2Cars= h2CarDemandInKM/12900*32.491*0.03/(1-Math.pow((1+0.03),(-12)))
					+h2CarDemandInKM/12900*1.7*0.03/(1-Math.pow((1+0.03),(-12)));
		}else if(simulatedYear.equals("2050")) {
			investmentCostForH2Cars= h2CarDemandInKM/12900*26.761*0.03/(1-Math.pow((1+0.03),(-12)))
					+h2CarDemandInKM/12900*0.7*0.03/(1-Math.pow((1+0.03),(-12)));
		}
		investmentCost += investmentCostForH2Cars;
		
		double operationalCostForH2Cars=0.0;
		if(simulatedYear.equals("2030")) {
			operationalCostForH2Cars = h2CarDemandInKM/12900*0.025*32.491+(h2CarDemandInKM/12900)*1.7*0.0418;
		}else if(simulatedYear.equals("2050")) {
			operationalCostForH2Cars = h2CarDemandInKM/12900*0.0304*26.761+(h2CarDemandInKM/12900)*0.7*0.0732;
		}
		fixedOperationalCost += operationalCostForH2Cars;
		
		//cost for solar heat storage
		double investmentCostForSolarHeatStorage=0, operationalCostForSolarHeatStorage=0 ;
		
		for ( String key : dvHM.keySet() ) {
			if(dvHM.get(key).catagory.equals("Solar storage")){
				col = (Collection<String>) energyplanmMap.get(key);
				it = col.iterator();
				String solarStorageInDaysStr = it.next().toString();
				double solarStorageInDays = (int) Double.parseDouble(solarStorageInDaysStr);
				
				String heatDemandKey = key.substring(0, key.length() - 7) + "Input";
				col = (Collection<String>) energyplanmMap.get(heatDemandKey);
				it = col.iterator();
				String heatDemandStr = it.next().toString();
				double heatDemand = (int) Double.parseDouble(heatDemandStr);
				
				if(simulatedYear.equals("2030")) {
					investmentCostForSolarHeatStorage += solarStorageInDays*heatDemand/366*3000*0.03/(1- Math.pow((1+0.03),(-30)));
					operationalCostForSolarHeatStorage += solarStorageInDays *0.007*heatDemand/366*3000;
				}else if (simulatedYear.equals("2050")) {
					investmentCostForSolarHeatStorage += solarStorageInDays*heatDemand/366*3000*0.03/(1- Math.pow((1+0.03),(-30)));
					operationalCostForSolarHeatStorage += solarStorageInDays *0.007*heatDemand/366*3000;
				}
		    }
		}
		investmentCost += investmentCostForSolarHeatStorage;
		fixedOperationalCost += operationalCostForSolarHeatStorage;

		//building cost
		double investmentCostForEnergyEfficiencyBuilding=0.0;
		if(simulatedYear.equals("2030")) {
			investmentCostForEnergyEfficiencyBuilding=17.826*3300*0.03/(1-Math.pow((1+0.03),(-30)));
		}else if(simulatedYear.equals("2050")) {
			investmentCostForEnergyEfficiencyBuilding=21.249*3300*0.03/(1-Math.pow((1+0.03),(-30)));
		}
		investmentCost += investmentCostForEnergyEfficiencyBuilding;
		
		//calculateion cost for h2 electrolyzer
		
		col = (Collection<String>) energyplanmMap.get("Annual MaximumH2Electr.");
		it = col.iterator();
		String electrolyzerCapStr = it.next().toString();
		double electrolyzerCap = (int) Double.parseDouble(electrolyzerCapStr);
		energyplanmMap.put("ElectrolyzerCapacity", electrolyzerCap);
		
		double electrolyzerInvestmentUnitCost=0, electrolyzerOMUnitCost=0 ;
		int lifeTime =0;
		if(simulatedYear.equals("2030")){
			electrolyzerInvestmentUnitCost = 0.742; electrolyzerOMUnitCost = 13.0;
			lifeTime = 9;
		}else if(simulatedYear.equals("2050")) {
			electrolyzerInvestmentUnitCost = 0.598; electrolyzerOMUnitCost = 13.0;
			lifeTime = 11;
		}
		
		double electrolyzerInvestmentCost = ((electrolyzerCap*electrolyzerInvestmentUnitCost) * 0.03)/ 
				(1 - Math.pow(1+0.03,-lifeTime )) ; 
		investmentCost += electrolyzerInvestmentCost;
		
		//addtional OM cost
		double electrolyzerOMCost = (electrolyzerCap*electrolyzerInvestmentUnitCost) * electrolyzerOMUnitCost/100.0;
		fixedOperationalCost += electrolyzerOMCost;
		
		
		/*double totalAdditionalCost = additionalCostAnnualH2mCHPheat + additionalCostAnnualnGasmCHPheat
									+ additionalCostBiomassBioler + elecGridDistributionCost + additionalCostForH2Cars
									* costForSolarHeatStorage; */

		double actualAnnualCost = variableCost + fixedOperationalCost + investmentCost ;

		energyplanmMap.put("calculatedVariableCost", variableCost);
		energyplanmMap.put("calculatedOperationalCost", fixedOperationalCost);
		energyplanmMap.put("calculatedInvestmentCost", investmentCost);
		
		energyplanmMap.put("AnnualCost", actualAnnualCost);

		solution.setObjective(1, actualAnnualCost);

		for ( String key : dvHM.keySet() ) {
			if(dvHM.get(key).catagory.equals("Heat demand")){
				col = (Collection<String>) energyplanmMap.get(key);
				it = col.iterator();
				String AnnualXHeatStr = it.next().toString();
				double AnnualXHeat =  Double.parseDouble(AnnualXHeatStr);
				
				String solarInputKey = key.substring(0, key.length() - 10) + "SolarInput";
				col = (Collection<String>) energyplanmMap.get(solarInputKey);
				it = col.iterator();
				String xSolarInputStr = it.next().toString();
				double xSolarInput =  Double.parseDouble(xSolarInputStr);
				
				String solarStorageKey = key.substring(0, key.length() - 10) + "SolarStorage";
				col = (Collection<String>) energyplanmMap.get(solarStorageKey);
				it = col.iterator();
				String xSolarStorageStr = it.next().toString();
				double xSolarStorage = (int) Double.parseDouble(xSolarStorageStr);
				
				double xSolarUtilization = 0.0;
				if(solarInputKey.equals("hpSolarInput")) {
					xSolarUtilization = ex.solarUtilizationCalculation(solarInputKey, AnnualXHeat, xSolarInput, xSolarStorage );
				}else {
					xSolarUtilization = ex.solarUtilizationCalculation( AnnualXHeat, xSolarInput, xSolarStorage );
				}
				
				energyplanmMap.put(  key.substring(0, key.length() - 10) + "SolarUtilization", xSolarUtilization);

		    }
		}
		
		
		//constraints
		col = (Collection<String>) energyplanmMap.get("PVCap");
		it = col.iterator();
		double PVCap = Double.parseDouble(it.next().toString());
		
		col = (Collection<String>) energyplanmMap.get("TotalSolarThermal");
		it = col.iterator();
		double annualSolarThermalInput = Double.parseDouble(it.next().toString());
		
		/*col = (Collection<String>) energyplanmMap.get("AnnaulImportElectr.");
		it = col.iterator();
		double annualImportElec = Double.parseDouble(it.next().toString());
	
		col = (Collection<String>) energyplanmMap.get("AnnaulExportElectr.");
		it = col.iterator();
		double annualExportElec = Double.parseDouble(it.next().toString());
	*/
		
		
		// extract import
		col = (Collection<String>) energyplanmMap.get("Annual MaximumImportElectr.");
		it = col.iterator();
		double annualMaximumImportElec = Double.parseDouble(it.next().toString());
		
		// extract Export
		col = (Collection<String>) energyplanmMap.get("Annual MaximumExportElectr.");
		it = col.iterator();
		double annualMaximumExportElec = Double.parseDouble(it.next().toString());
		
		double [] constraint = new double[this.getNumberOfConstraints()];
		
		double totalLand2030 = 310264, totalLand2050=660086 ;
		
		double	constraintOnImport=0; //in KW
		double	constraintOnExport=0; //in KW
		
		if(simulatedYear.equals("2030")) {
			constraint[0] =  totalLand2030 - (PVCap-636.72)/0.125 + (annualSolarThermalInput * 
				Math.pow( 10,  6) / 705) ;
		}else if(simulatedYear.equals("2050")) {
			constraint[0] =  totalLand2050 - (PVCap-636.72)/0.133 + (annualSolarThermalInput * 
					Math.pow( 10,  6) / 720) ;
		}
		
	
		constraint[1] =  constraintOnImport - annualMaximumImportElec;
		constraint[2] =  constraintOnExport - annualMaximumExportElec;
		double total = 0.0;
	    int number = 0;
	    for (int i = 0; i < this.getNumberOfConstraints(); i++) {
	      if (constraint[i]<0.0){
	        total+=constraint[i];
	        number++;
	      }
		}
	        
	    solution.setOverallConstraintViolation(total);    
	    solution.setNumberOfViolatedConstraint(number); 
			

			hmForAllSolutions.put(solutonToString(solution), energyplanmMap);
		}
	

	
	
	public void evaluateAll(SolutionSet solutionSet) throws JMException {
		
		ArrayList<MultiMap> multiMapList = new ArrayList<MultiMap>();
		
		
		for(int i=0;i<solutionSet.size();i++) {
			Solution solution = solutionSet.get(i);
			MultiMap mm = new MultiValueMap() ;
			mm=createModificationFiles(solution, i );
			multiMapList.add(mm);
			
		}
		
		simulateAllScenarios(solutionSet.size());
		
		for(int i=0;i<solutionSet.size();i++) {
			Solution solution = solutionSet.get(i);
			MultiMap mm = multiMapList.get(i); 
			extractInformation(solution, mm, i);
		
		}
	}

	/**
	 * Evalua tes a solution.
	 * 
	 * @param solution
	 *            The solution to evaluate.
	 * @throws JMException
	 */
	@SuppressWarnings("unchecked")
	public void evaluate(Solution solution) throws JMException {

			}

	@SuppressWarnings("unchecked")
	public void evaluateConstraints(Solution solution) throws JMException {

	}

	MultiMap writeIntoInputFile(final Solution s, String fileName) throws IOException, JMException {

		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

		MultiMap modifyMap = new MultiValueMap();

	
		File modifiedInput = new File(".\\EnergyPLAN161\\spool\\"+fileName+".txt");
		if (modifiedInput.exists()) {
			modifiedInput.delete();
		}
		modifiedInput.createNewFile();

		FileWriter fw = new FileWriter(modifiedInput.getAbsoluteFile());
		BufferedWriter modifiedInputbw = new BufferedWriter(fw);

		String path = "";
		if (simulatedYear.equals("2030")) {
			path = ".\\EnergyPLAN161\\energyPlan Data\\Data\\CEIS_2030_BAU.txt";
		}else if (simulatedYear.equals("2050")) {
			path = ".\\EnergyPLAN161\\energyPlan Data\\Data\\CEIS_2050_BAU.txt";
		}

		BufferedReader mainInputbr = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-16"));

		double totalSolarThermalInput = 0.0;

		list.add(new ArrayList<String>() { 
				{	add (dvHM.get("PVCap").EnergyPLANVariableName.get(0)); 
					add( s.getDecisionVariables()[dvHM.get("PVCap").index].getValue()+""); 
				}
			} );
		modifyMap.put("PVCap", (int) s.getDecisionVariables()[dvHM.get("PVCap").index].getValue());

		
		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("RiverHydroCap").EnergyPLANVariableName.get(0)); 
				add( s.getDecisionVariables()[dvHM.get("RiverHydroCap").index].getValue()+""); 
			}
		} );
		modifyMap.put("RiverHydroCap", (int) s.getDecisionVariables()[dvHM.get("RiverHydroCap").index].getValue());


		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("BioGasCap").EnergyPLANVariableName.get(0)); 
				add( s.getDecisionVariables()[dvHM.get("BioGasCap").index].getValue()+""); 
			}
		} );
		modifyMap.put("BioGasCap", (int) s.getDecisionVariables()[dvHM.get("BioGasCap").index].getValue());


		
		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("Battery1").EnergyPLANVariableName.get(0)); 
				add( s.getDecisionVariables()[dvHM.get("Battery1").index].getValue()*1000/2+""); 
			}
		} );

		modifyMap.put("ElecStorage_Turbine_Cap", s.getDecisionVariables()[dvHM.get("Battery1").index].getValue() * 1000 / 2);

		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("Battery1").EnergyPLANVariableName.get(1)); 
				add( s.getDecisionVariables()[dvHM.get("Battery1").index].getValue()*1000/2+""); 
			}
		} );

		modifyMap.put("ElecStorage_Pump_Cap", s.getDecisionVariables()[dvHM.get("Battery1").index].getValue() * 1000 / 2);

		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("Battery1").EnergyPLANVariableName.get(2)); 
				add( s.getDecisionVariables()[dvHM.get("Battery1").index].getValue()+""); 
			}
		} );
		modifyMap.put("ElecStorage_Storage_Cap", s.getDecisionVariables()[dvHM.get("Battery1").index].getValue());

		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("H2Storage").EnergyPLANVariableName.get(0)); 
				add( s.getDecisionVariables()[dvHM.get("H2Storage").index].getValue()+""); 
			}
		} );
		modifyMap.put("H2Storage", s.getDecisionVariables()[dvHM.get("H2Storage").index].getValue());

		
		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("P2PH2Electrolyzer").EnergyPLANVariableName.get(0)); 
				add( s.getDecisionVariables()[dvHM.get("P2PH2Electrolyzer").index].getValue()+""); 
			}
		} );

		modifyMap.put("P2PH2Electrolyzer", s.getDecisionVariables()[dvHM.get("P2PH2Electrolyzer").index].getValue());

		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("P2PH2FuelCell").EnergyPLANVariableName.get(0)); 
				add( s.getDecisionVariables()[dvHM.get("P2PH2FuelCell").index].getValue()+""); 
			}
		} );

		modifyMap.put("P2PH2FuelCell", s.getDecisionVariables()[dvHM.get("P2PH2FuelCell").index].getValue() );

		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("P2PH2Storage").EnergyPLANVariableName.get(0)); 
				add( s.getDecisionVariables()[dvHM.get("P2PH2Storage").index].getValue()+""); 
			}
		} );
		modifyMap.put("P2PH2Storage", s.getDecisionVariables()[dvHM.get("P2PH2Storage").index].getValue());
		
		
		
		//heat 
		//fuel deamds for heat
		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("coalBoilerHeatDemand").EnergyPLANVariableName.get(0)); 
				add( s.getDecisionVariables()[dvHM.get("coalBoilerHeatDemand").index].getValue() / coalBoilerEfficiency+"" ); 
			}
		} );
		modifyMap.put("coalBoilerFuelDemand", s.getDecisionVariables()[dvHM.get("coalBoilerHeatDemand").index].getValue() / coalBoilerEfficiency);
		modifyMap.put("coalBoilerHeatDemand", s.getDecisionVariables()[dvHM.get("coalBoilerHeatDemand").index].getValue());

		
		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("oilBoilerHeatDemand").EnergyPLANVariableName.get(0)); 
				add( s.getDecisionVariables()[dvHM.get("oilBoilerHeatDemand").index].getValue() / oilBoilerEfficiency+""); 
			}
		} );
		modifyMap.put("oilBoilerFuelDemand", s.getDecisionVariables()[dvHM.get("oilBoilerHeatDemand").index].getValue() / oilBoilerEfficiency);
		modifyMap.put("oilBoilerHeatDemand", s.getDecisionVariables()[dvHM.get("oilBoilerHeatDemand").index].getValue());


		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("nGasBoilerHeatDemand").EnergyPLANVariableName.get(0)); 
				add( s.getDecisionVariables()[dvHM.get("nGasBoilerHeatDemand").index].getValue() / nGasBoilerEfficiency+""); 
			}
		} );
		modifyMap.put("nGasBoilerFuelDemand", s.getDecisionVariables()[dvHM.get("nGasBoilerHeatDemand").index].getValue() / nGasBoilerEfficiency);
		modifyMap.put("nGasBoilerHeatDemand", s.getDecisionVariables()[dvHM.get("nGasBoilerHeatDemand").index].getValue());

		
		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("biomassBoilerHeatDemand").EnergyPLANVariableName.get(0)); 
				add( s.getDecisionVariables()[dvHM.get("biomassBoilerHeatDemand").index].getValue() / biomassBoilerEfficiency+""); 
			}
		} );
		modifyMap.put("biomassBoilerFuelDemand", s.getDecisionVariables()[dvHM.get("biomassBoilerHeatDemand").index].getValue() / biomassBoilerEfficiency);
		modifyMap.put("biomassBoilerHeatDemand", s.getDecisionVariables()[dvHM.get("biomassBoilerHeatDemand").index].getValue());


		//heat
		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("h2mCHPHeatDemand").EnergyPLANVariableName.get(0)); 
				add( s.getDecisionVariables()[dvHM.get("h2mCHPHeatDemand").index].getValue()+"" ); 
			}
		} );
		modifyMap.put("h2mCHPHeatDemand", s.getDecisionVariables()[dvHM.get("h2mCHPHeatDemand").index].getValue());

	
		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("nGasmCHPHeatDemand").EnergyPLANVariableName.get(0)); 
				add( s.getDecisionVariables()[dvHM.get("nGasmCHPHeatDemand").index].getValue() +""); 
			}
		} );
		modifyMap.put("nGasmCHPHeatDemand", s.getDecisionVariables()[dvHM.get("nGasmCHPHeatDemand").index].getValue());

		
		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("biomassmCHPHeatDemand").EnergyPLANVariableName.get(0)); 
				add( s.getDecisionVariables()[dvHM.get("biomassmCHPHeatDemand").index].getValue() +""); 
			}
		} );
		modifyMap.put("biomassmCHPHeatDemand", s.getDecisionVariables()[dvHM.get("biomassmCHPHeatDemand").index].getValue());

		
		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("hpHeatDemand").EnergyPLANVariableName.get(0)); 
				add( s.getDecisionVariables()[dvHM.get("hpHeatDemand").index].getValue()+"" ); 
			}
		} );
		modifyMap.put("hpHeatDemand", s.getDecisionVariables()[dvHM.get("hpHeatDemand").index].getValue());
	
		//solar tharmal
		
		for ( final String key : dvHM.keySet() ) {
			if(dvHM.get(key).catagory.equals("Solar Thermal")){
				final String heatDemandKey = key.substring(0, key.length() - 10) + "HeatDemand";
				list.add(new ArrayList<String>() { 
					{	add (dvHM.get(key).EnergyPLANVariableName.get(0)); 
						add( s.getDecisionVariables()[dvHM.get(heatDemandKey).index].getValue()
								* s.getDecisionVariables()[dvHM.get(key).index].getValue() +""); 
					}
				} );
				totalSolarThermalInput += s.getDecisionVariables()[dvHM.get(heatDemandKey).index].getValue()
						* s.getDecisionVariables()[dvHM.get(key).index].getValue();
				modifyMap.put(key, s.getDecisionVariables()[dvHM.get(heatDemandKey).index].getValue()
						* s.getDecisionVariables()[dvHM.get(key).index].getValue());
				
			}
		
		}
		modifyMap.put("TotalSolarThermal", totalSolarThermalInput);
		
		//solar storage
		for ( final String key : dvHM.keySet() ) {
			if(dvHM.get(key).catagory.equals("Solar storage")){
				list.add(new ArrayList<String>()  { 
					{	add (dvHM.get(key).EnergyPLANVariableName.get(0)); 
						add( s.getDecisionVariables()[dvHM.get(key).index].getValue() +""); 
					}
				} );
				modifyMap.put(key, s.getDecisionVariables()[dvHM.get(key).index].getValue());

			}
		
		}
		
		//Transport
		//Diesel demand
		final double totalDieselDemandInGWhForTrns = (long) s.getDecisionVariables()[  dvHM.get("dieselCarDemandInKM").index].getValue() * efficiencyDieselCar / Math.pow(10, 6);
		list.add(new ArrayList<String>() { 														
			{	add (dvHM.get("dieselCarDemandInKM").EnergyPLANVariableName.get(0)); 
				add( totalDieselDemandInGWhForTrns +"" ); 
			}
		} );

		//number of disel car
		final double numberOfDieselCars = (long) s.getDecisionVariables()[  dvHM.get("dieselCarDemandInKM").index].getValue() / averageKMPerYearPerCar;
				
		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("dieselCarDemandInKM").EnergyPLANVariableName.get(1)); 
				add( numberOfDieselCars+"" ); 
			}
		} );
		
		modifyMap.put("dieselCarDemandInKM", (long) s.getDecisionVariables()[  dvHM.get("dieselCarDemandInKM").index].getValue());
		modifyMap.put("dieselCarDemandInGWh", totalDieselDemandInGWhForTrns);
		modifyMap.put("NumberOfDieselCars", numberOfDieselCars);

		
		//elec car demand
		final double totalElceDemandInGWhForTrns = (long) s.getDecisionVariables()[  dvHM.get("elCarDemandInKM").index].getValue() * efficiencyEVCar / Math.pow(10, 6);
		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("elCarDemandInKM").EnergyPLANVariableName.get(0)); 
				add( totalElceDemandInGWhForTrns +"" ); 
			}
		} );

		//number of el car
		final double numberOfElCars = (long) s.getDecisionVariables()[  dvHM.get("elCarDemandInKM").index].getValue() / averageKMPerYearPerCar;

		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("elCarDemandInKM").EnergyPLANVariableName.get(1)); 
				add( numberOfElCars+"" ); 
			}
		} );
		modifyMap.put("elCarDemandInKM", (long) s.getDecisionVariables()[  dvHM.get("elCarDemandInKM").index].getValue());
		modifyMap.put("elCarDemandInGWh", totalElceDemandInGWhForTrns);
		modifyMap.put("NumberOfElCars", numberOfElCars);
		
		//h2 car demand
		final double totalH2DemandInGWhForTrns = (long) s.getDecisionVariables()[  dvHM.get("h2CarDemandInKM").index].getValue() * efficiencyH2Car / Math.pow(10, 6);
		
		list.add(new ArrayList<String>() { 
			{	add (dvHM.get("h2CarDemandInKM").EnergyPLANVariableName.get(0)); 
				add( totalH2DemandInGWhForTrns +"" ); 
			}
		} );
		
		final double numberOfH2Cars = (long) s.getDecisionVariables()[  dvHM.get("h2CarDemandInKM").index].getValue() / averageKMPerYearPerCar;
		
		modifyMap.put("h2CarDemandInKM", (long) s.getDecisionVariables()[  dvHM.get("h2CarDemandInKM").index].getValue());
		modifyMap.put("h2CarDemandInGWh", totalH2DemandInGWhForTrns);
		modifyMap.put("NumberOfH2Cars", numberOfH2Cars);
		
		String line;
		while ((line = mainInputbr.readLine()) != null) {
			line.trim();
			boolean trackBreak = false;
			for (int i = 0; i < list.size(); i++) {
				if (line.startsWith(list.get(i).get(0)) || line.endsWith(list.get(i).get(0))) {
					modifiedInputbw.write(line + "\n");
					line = mainInputbr.readLine();
					line = line.replace(line, list.get(i).get(1));
					modifiedInputbw.write(line + "\n");
					trackBreak = true;
					break;
				}
			}
			if (trackBreak)
				continue;
			else
				modifiedInputbw.write(line + "\n");

		}

		modifiedInputbw.close();
		mainInputbr.close();

		return modifyMap;
	}

	
	
	
	void repairHeatVariables(Solution s) {

		ArrayList<Double> rangesAL = new ArrayList<Double>(numberOfVariables_);
		for(int i = 0;i<numberOfVariables_;i++) {
			rangesAL.add(null);
		}
		ArrayList<Double> arrayAL = new ArrayList<Double>(numberOfVariables_);
		for(int i = 0;i<numberOfVariables_;i++) {
			arrayAL.add(null);
		}
		
		
		for (DecisionVarible value : dvHM.values()) {
		    if(value.catagory.equals("Heat demand")){
		    	rangesAL.set(value.index, value.upperBound- value.lowerBound);
		    	//rangesAL.add(value.index, value.upperBound- value.lowerBound);
		    	try {
					arrayAL.set(value.index,s.getDecisionVariables()[value.index].getValue())  ;
				} catch (JMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
			
		}
		
		
		while (Math.abs(addArrayD(arrayAL) - totalHeatDemand) > 0.05) {
		double arraySum = addArrayD(arrayAL);
		if (arraySum > totalHeatDemand) {
			// we need to reduce from each technology
			for (DecisionVarible value : dvHM.values()) {
			    if(value.catagory.equals("Heat demand")){
						
				double reduceForThisTech = (arraySum - totalHeatDemand) * rangesAL.get(value.index) / addArrayD(rangesAL);
				if ((arrayAL.get(value.index) - reduceForThisTech) < value.lowerBound) {
					arrayAL.set(value.index, value.lowerBound) ;
				} else {
					arrayAL.set(value.index, arrayAL.get(value.index) - reduceForThisTech);
				}
			}
			}

			} else {
				// we need to increase from each technology
				for (DecisionVarible value : dvHM.values()) {
				    if(value.catagory.equals("Heat demand")){
					double increaseForThisTech = (totalHeatDemand - arraySum) *  rangesAL.get(value.index) / addArrayD(rangesAL);
					if ((arrayAL.get(value.index) + increaseForThisTech) > value.upperBound) {
						arrayAL.set(value.index, value.upperBound);
					} else {
						arrayAL.set(value.index,  arrayAL.get(value.index) + increaseForThisTech);
					}
				}
				}
			}
		}

		for (DecisionVarible value : dvHM.values()) {
		    if(value.catagory.equals("Heat demand")){
			try {
				s.getDecisionVariables()[value.index].setValue(arrayAL.get(value.index));
			} catch (JMException e) {
				System.out.println("something wrone in heat calculation");
			}
		}
		}

	}

	static double addArrayD(ArrayList <Double> arr) {
		double sum = 0.0;
		for (int i = 0; i < arr.size(); i++) {
			if(arr.get(i)!=null)
				sum += arr.get(i);
		}
		return sum;
	}

	static long addArrayL(ArrayList<Long> arr) {
		long sum = 0L;
		for (int i = 0; i < arr.size(); i++) {
			if(arr.get(i)!=null)
				sum += arr.get(i);
		}
		return sum;
	}

	ArrayList<String> solutonToString(Solution s) {
		ArrayList<String> str = new ArrayList();
		for (int i = 0; i < numberOfVariables_; i++) {
			try {
				str.add(s.getDecisionVariables()[i].getValue() + "");
			} catch (JMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return str;
	}

	public HashMap<ArrayList<String>, MultiMap> getTheList() {
		return hmForAllSolutions;
	}

	void repairTransVariables(Solution s) {
		
		
		ArrayList<Long> rangesAL = new ArrayList(numberOfVariables_);
		ArrayList<Long> arrayAL = new ArrayList(numberOfVariables_);
		
		for(int i = 0;i<numberOfVariables_;i++) {
			rangesAL.add(null);
		}
		for(int i = 0;i<numberOfVariables_;i++) {
			arrayAL.add(null);
		}
		for (DecisionVarible value : dvHM.values()) {
		    if(value.catagory.equals("Transport")){
		    	rangesAL.set(value.index, (long)(value.upperBound- value.lowerBound));
		    	try {
					arrayAL.set(value.index,(long)s.getDecisionVariables()[value.index].getValue())  ;
				} catch (JMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
			
		}
		

		while (Math.abs(addArrayL(arrayAL) - totalKMRunByCars) > 10) {
			long arraySum = addArrayL(arrayAL);
			if (arraySum > totalKMRunByCars) {
				for (DecisionVarible value : dvHM.values()) {
				    if(value.catagory.equals("Transport")){
				    	BigInteger a = new BigInteger(Long.toString(arraySum - totalKMRunByCars));
						BigInteger b = new BigInteger(Long.toString(rangesAL.get(value.index)));
						BigInteger c = a.multiply(b);
						BigInteger d = new BigInteger(Long.toString(addArrayL(rangesAL)));
						BigInteger e = c.divide(d);
						long reduceForThisTech = e.longValue();
						if ((arrayAL.get(value.index) - reduceForThisTech) < value.lowerBound) {
							arrayAL.set(value.index, (long) value.lowerBound);
						} else {
							arrayAL.set(value.index,(long) arrayAL.get(value.index) - reduceForThisTech);
						}
					}
				}
			} else {
				// we need to increase from each technology
				for (DecisionVarible value : dvHM.values()) {
				    if(value.catagory.equals("Transport")){
						BigInteger a = new BigInteger(Long.toString(totalKMRunByCars - arraySum));
						BigInteger b = new BigInteger(Long.toString(rangesAL.get(value.index)));
						BigInteger c = a.multiply(b);
						BigInteger d = new BigInteger(Long.toString(addArrayL(rangesAL)));
						BigInteger e = c.divide(d);
						long increaseForThisTech = e.longValue();
						if ((arrayAL.get(value.index) + increaseForThisTech) > value.upperBound) {
							arrayAL.set(value.index,  (long) value.upperBound);
						} else {
							arrayAL.set(value.index,(long) (arrayAL.get(value.index) + increaseForThisTech));
						}
					}
			}	}
		}

		for (DecisionVarible value : dvHM.values()) {
		    if(value.catagory.equals("Transport")){
		    	try {
		    		s.getDecisionVariables()[value.index].setValue((double) arrayAL.get(value.index));
		    	} catch (JMException e) {
		    		System.out.println("something wrone in heat calculation");
		    	}
		    }
		}
	}

}
