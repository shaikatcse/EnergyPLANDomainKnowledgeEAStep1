package reet.fbk.eu.OptimizeEnergyPLANTrentoProvince.Problem;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.MultiMap;

import reet.fbk.eu.OptimizeEnergyPLANTrentoProvince.parseFile.EnergyPLANFileParseForTrentoProvince;
import reet.fbk.eu.OptimizeEnergyPLANVdN.parseFile.EnergyPLANFileParseForVdN;

/*
 * This is a problem file that is dealing with VdN (mainly introduction of electric cars)
 */
public class EnergyPLANProblemTrentoProvince extends Problem {

	
	
	MultiMap energyplanmMap;
	String simulatedYear;
	

	
	public static final int averageKMPerYearPerCar = 12900;
	public static final double maxH2DemandInDistribution  = 0.000209257;
	public static final double sumH2DemandInDistribution = 1.0; 
	
	
	
	//transport
	public long totalKMRunByCars ;
	
	
	//other variable
	public double totalHeatDemand;
	public double DHHeatProduction;
	
	//cars efficiencies
	public double efficiencyConCar; // KWh/km
	public double efficiencyEVCar; // KWh/km
	public double efficiencyFCEVCar; // KWh/km

	public double efficiencyBiomassCHPThermal;
	public double efficiencynGasCHPThermal;
	
	// H2
	public double efficiencyElectrolyzerTrans;
	
	public double oilBoilerEfficiency;
	public double nGasBoilerEfficiency;
	public double biomassBoilerEfficiency;
	public double COP;
	
	
	
	//grid distribution cost
	public double elecGridDistributionCost;
	
	//building envelop energy efficiency
	double buildingEnvelopesEnergyEfficiency;
	
	
	/**
	 * Creates a new instance of problem ZDT1.
	 * 
	 * @param numberOfVariables
	 *            Number of variables.
	 * @param solutionType
	 *            The solution type must "Real", "BinaryReal, and "ArrayReal".
	 */
		  
	public EnergyPLANProblemTrentoProvince(String solutionType, String simulatedYear) {
		
		this.simulatedYear=simulatedYear;
				
		numberOfVariables_ = 14;
		numberOfObjectives_ = 2;
		numberOfConstraints_ = 1;

		problemName_ = "OptimizeEnergyPLANTrentoProvince";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		// Establishes upper and lower limits for the variables
		int var;

		// PV upper and lower limit
		lowerLimit_[0] = 100.0;
		upperLimit_[0] = 1000.0;

		// other are the percentage from limit [0,1]
		for (var = 1; var < numberOfVariables_; var++) {
			lowerLimit_[var] = 0.0;
			upperLimit_[var] = 1.0;
		} // for

		if (solutionType.compareTo("Real") == 0)
			solutionType_ = new RealSolutionType(this);
		else {
			System.out.println("Error: solution type " + solutionType
					+ " invalid");
			System.exit(-1);
		}
	
	
	 if(this.simulatedYear.equals("2030")){
			// Heat
			totalHeatDemand = 6.285; // in TWh
			DHHeatProduction = 0.284; // in TWh
			
			//cars
			efficiencyConCar = 0.460; // KWh/km
			efficiencyFCEVCar = 0.299; // KWh/km
			efficiencyEVCar = 0.133; // Kwh/km
			totalKMRunByCars = 10071000000L;
			
			efficiencynGasCHPThermal = 0.48;
			efficiencyBiomassCHPThermal = 0.48;
			
			
			oilBoilerEfficiency = 0.85;
			nGasBoilerEfficiency = 0.95;
			biomassBoilerEfficiency = 0.78;
			COP = 3.97;
			
			// H2
			efficiencyElectrolyzerTrans = 0.7217;
	
			elecGridDistributionCost =  203.44;
			
			buildingEnvelopesEnergyEfficiency = 1082602;
			
	 }else if(this.simulatedYear.equals("2050")){
			// Heat
			totalHeatDemand =  5.769; // in TWh
			DHHeatProduction = 0.232; // in TWh	
				
			//cars
			efficiencyConCar = 0.340; // KWh/km
			efficiencyFCEVCar = 0.226; // KWh/km
			efficiencyEVCar = 0.106; // Kwh/km
			totalKMRunByCars = 10700000000L;
			
			
			efficiencynGasCHPThermal = 0.50;
			efficiencyBiomassCHPThermal = 0.50;
			
			
			oilBoilerEfficiency = 0.90;
			nGasBoilerEfficiency = 1.0;
			biomassBoilerEfficiency = 0.80;
			COP = 4.30;
			
			// H2
			efficiencyElectrolyzerTrans = 0.7535;
								
			elecGridDistributionCost =  204.59;
			
			buildingEnvelopesEnergyEfficiency = 1238009;
	 }
		
	
	} // constructor end




	/**
	 * Evaluates a solution.
	 * 
	 * @param solution
	 *            The solution to evaluate.
	 * @throws JMException
	 */
	@SuppressWarnings("unchecked")
	public void evaluate(Solution solution) throws JMException {

		// decision variables
		// index - 0 -> PV Capacity
		// index - 1 -> oil boiler heat percentage
		// index - 2 -> nGas boiler heat percentage
		// index - 3 -> nGas micro chp heat percentage
		// index - 4 -> Biomass boiler heat percentage
		// index - 5 -> Biomass micro chp heat percentage
		// last percentage will go to individual HP percentage
		// index - 6 -> EV car parcentage
		// index - 7 -> FCEV(H2) car percentage
		

		// index -8 -> solar thermal percentage in oil boiler
		// index -9 -> solar thermal percentage in nGas boiler
		// index 10 -> solar thermal percentage in bnGas CHP
		// index -11 -> solar thermal percentage in biomass boiler
		// index -12 -> solar thermal percentage in biomass CHP
		// index -13 -> solar thermal percentage in HP
		
		// PV
		double pv = solution.getDecisionVariables()[0].getValue();

		double heatPercentages[] = new double[5];
		for (int i = 0; i < 5; i++) {
			heatPercentages[i] = solution.getDecisionVariables()[i + 1]
					.getValue();
		}

		Arrays.sort(heatPercentages);

		for (int i = 1; i < 6; i++) {
			solution.getDecisionVariables()[i].setValue(heatPercentages[i - 1]);
		}
		// oil-boiler percentage
		double oilBoilerHeatPercentage = heatPercentages[0];
		// Ngas-boiler heat percentage
		double nGasBoilerHeatPercentage = heatPercentages[1]
				- heatPercentages[0];
		// ngas chp heat parcentage
		double nGasCHPHeatPercentage = heatPercentages[2]
				- heatPercentages[1];
		// biomass-boiler heat percentage
		double biomassBoilerHeatPercentage = heatPercentages[3]
				- heatPercentages[2];
		// biomass chp heat percentage
		double biomassCHPHeatPercentage = heatPercentages[4]
				- heatPercentages[3];

		// heat pump heat percentage
		double hpHeatPercentage = 1.0 - heatPercentages[4];

		// car percentages
		double carPercentages[] = new double[2];
		for (int i = 6; i < 8; i++) {
			carPercentages[i - 6] = solution.getDecisionVariables()[i]
					.getValue();
		}

		Arrays.sort(carPercentages);

		for (int i = 6; i < 8; i++) {
			solution.getDecisionVariables()[i].setValue(carPercentages[i - 6]);
		}

		// EV percentage of run
		double EVCarPercentage = carPercentages[0];

		// FCEV car percentage of run
		double FCEVCarPercentage = carPercentages[1] - carPercentages[0];

		// conventional car percentage of run
		double conCarPercentage = 1 - carPercentages[1];

		double totalKMRunByConCar = (long) totalKMRunByCars * conCarPercentage;
		double totalKMRunByEVCar = (long) totalKMRunByCars * EVCarPercentage;
		double totalKMRunByFCEVCar = (long) totalKMRunByCars * FCEVCarPercentage;

		double totalPetrolDemandInTWhForTrns = totalKMRunByConCar
				* efficiencyConCar / Math.pow(10, 9);
		double totalElecDemandInTWhForTrns = totalKMRunByEVCar
				* efficiencyEVCar / Math.pow(10, 9);
		double totalH2DemandInTWhForTrns = totalKMRunByFCEVCar
				* efficiencyFCEVCar / Math.pow(10, 9);
				
		// solar thermal percentage
		double oilBoilerSolarPercentage = solution.getDecisionVariables()[8]
				.getValue();
		double nGasBoilerSolarPercentage = solution.getDecisionVariables()[9]
				.getValue();
		double nGasCHPSolarPercentage = solution.getDecisionVariables()[10]
				.getValue();
		double biomassBoilerSolarPercentage = solution.getDecisionVariables()[11]
				.getValue();
		double biomassCHPSolarPercentage = solution.getDecisionVariables()[12]
				.getValue();
		double hpSolarPercentage = solution.getDecisionVariables()[13]
				.getValue();

		try{
		writeIntoInputFile(pv, oilBoilerHeatPercentage,
				nGasBoilerHeatPercentage, nGasCHPHeatPercentage, 
				biomassBoilerHeatPercentage, biomassCHPHeatPercentage, 
				hpHeatPercentage,
				totalPetrolDemandInTWhForTrns, totalElecDemandInTWhForTrns,
				totalH2DemandInTWhForTrns, oilBoilerSolarPercentage,
				nGasBoilerSolarPercentage, biomassBoilerSolarPercentage,
				nGasCHPSolarPercentage, biomassCHPSolarPercentage, hpSolarPercentage);
		}catch(IOException e){
			System.out.print("Pobrlem writting in modified Input file");
		}
		
		String energyPLANrunCommand = ".\\EnergyPLAN_12.3\\EnergyPLAN.exe -i "
				+ "\".\\modifiedInput.txt\" "
				+ "-ascii \"result.txt\" ";
		try {
			Process process = Runtime.getRuntime().exec(energyPLANrunCommand);
			process.waitFor();
			process.destroy();
			EnergyPLANFileParseForTrentoProvince epfp = new EnergyPLANFileParseForTrentoProvince(
					".\\result.txt");
			energyplanmMap = epfp.parseFile();

			Iterator it;
			Collection<String> col;

			// objective # 1
			col = (Collection<String>) energyplanmMap
					.get("CO2-emission (total)");
			it = col.iterator();
			double localCO2emission =  Double.parseDouble(it.next().toString());
			
			solution.setObjective(0,localCO2emission);

			// objective # 2			
			
			col = (Collection<String>) energyplanmMap.get("Variable costs");
			it = col.iterator();
			String totalVariableCostStr = it.next().toString();
			double totalVariableCost = Double.parseDouble(totalVariableCostStr);

			col = (Collection<String>) energyplanmMap
					.get("Fixed operation costs");
			it = col.iterator();
			String fixedOperationalCostStr = it.next().toString();
			double fixedOperationalCost = Double
					.parseDouble(fixedOperationalCostStr);

			// extract
			col = (Collection<String>) energyplanmMap
					.get("Annual Investment costs");
			it = col.iterator();
			String invest = it.next().toString();
			double investmentCost = Double.parseDouble(invest);

			
			//calculation of additional cost
			//read annual electricity demand
			col = (Collection<String>) energyplanmMap.get("AnnualElectr.Demand");
			it = col.iterator();
			String fixedElecDemandStr = it.next().toString();
			double fixedElecDemand = Double.parseDouble(fixedElecDemandStr);
			
			//read HP electric demand
			col = (Collection<String>) energyplanmMap.get("AnnualHH-HPElectr.");
			it = col.iterator();
			String HPElecDemandStr = it.next().toString();
			double HPElecDemand = Double.parseDouble(HPElecDemandStr);
			

			//calculate Transportation electricity demand which is totalElecDemandInTWhForTrns
			
			//variable cost
			double additionalVariableCost = (fixedElecDemand + HPElecDemand + totalElecDemandInTWhForTrns)*elecGridDistributionCost;
			
			//fixed operational cost
			double additionalFixedOperationalCost = DHHeatProduction*3600*0.145/100*0.76;
			
			//annual invesment cost
			double additionalAnnaulInvestmentCost = 	(DHHeatProduction *3600* 0.145 /40)+
											(DHHeatProduction *3600* 0.145 /40/100*3)+
											(buildingEnvelopesEnergyEfficiency*0.0033/30)+
											(buildingEnvelopesEnergyEfficiency* 0.0033 /30/100*3);
			
			double totalAdditionalCost = additionalAnnaulInvestmentCost + additionalFixedOperationalCost
					+additionalVariableCost;
			
			
			double actualAnnualCost = totalVariableCost + fixedOperationalCost
					+ investmentCost + totalAdditionalCost;

			solution.setObjective(1, actualAnnualCost);

			
			// check warning
			col = (Collection<String>) energyplanmMap.get("WARNING");
			if (col != null) {
				/*
				 * System.out.println("No warning"); } else {
				 */
				@SuppressWarnings("rawtypes")
				Iterator it3 = col.iterator();
				String warning = it3.next().toString();
				if (!warning.equals("PP too small. Critical import is needed")
						&& !warning
								.equals("Grid Stabilisation requierments are NOT fullfilled")
						&& !warning
								.equals("Critical Excess Electricity Production"))
					throw new IOException("warning!!" + warning);
				// System.out.println("Warning " + it3.next().toString());

			}
		} catch (IOException e) {
			System.out.println("Energyplan.exe has some problem");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Energyplan interrupted");
		}
	}

	@SuppressWarnings("unchecked")
	public void evaluateConstraints(Solution solution) throws JMException {
		Iterator it;
		Collection<String> col;

		// constraints about heat3-balance: balance<=0
		col = (Collection<String>) energyplanmMap.get("Biomass Consumption");
		it = col.iterator();
		double annualBiomassConsumption = Double.parseDouble(it.next()
				.toString());

		double constraints[] = new double[numberOfConstraints_];
		constraints[0] = 2.46 - annualBiomassConsumption;

		double totalViolation = 0.0;
		int numberOfViolation = 0;
		for (int i = 0; i < numberOfConstraints_; i++) {
			if (constraints[i] < 0.0) {
				totalViolation += constraints[0];
				numberOfViolation++;
			}
		}

		solution.setOverallConstraintViolation(totalViolation);
		solution.setNumberOfViolatedConstraint(numberOfViolation);

	}


	void writeIntoInputFile(double pv, double oilBoilerHeatPercentage,
			double nGasBoilerHeatPercentage, double nGasCHPHeatPercentage, 
			double biomassBoilerHeatPercentage,
			double biomassCHPHeatPercentage, double hpHeatPercentage,
			double totalPetrolDemandInTWhForTrns,
			double totalElecDemandInTWhForTrns,
			double totalH2DemandInTWhForTrns, double oilBoilerSolarPercentage,
			double nGasBoilerSolarPercentage, double biomassBoilerSolarPercentage,
			double nGasCHPSolarPercentage, double biomassCHPSolarPercentage, double hpSolarPercentage)
			throws IOException {

		// decision variables
		// index - 0 -> PV Capacity
		// index - 1 -> oil boiler heat percentage
		// index - 2 -> nGas boiler heat percentage
		// index - 3 -> nGas micro chp heat percentage
		// index - 4 -> Biomass boiler heat percentage
		// index - 5 -> Biomass micro chp heat percentage
		// last percentage will go to individual HP percentage
		// index - 6 -> EV car parcentage
		// index - 7 -> FCEV(H2) car percentage
		//last percentage goes to conventional car

		// index -8 -> solar thermal percentage in oil boiler
		// index -9 -> solar thermal percentage in nGas boiler
		// index 10 -> solar thermal percentage in bnGas CHP
		// index -11 -> solar thermal percentage in biomass boiler
		// index -12 -> solar thermal percentage in biomass CHP
		// index -13 -> solar thermal percentage in HP

		
		
		String modifiedParameters[] = { "input_RES1_capacity=", // pv (0)
				"input_fuel_Households[2]=", // oil boiler (1)
				"input_HH_oilboiler_Solar=", // oil Solar Thermal (2)
				"input_fuel_Households[3]=", // nGas boiler (3)
				"input_HH_ngasboiler_Solar=", // nGas boiler Solar Thermal (4)
				"input_HH_NgasCHP_heat=",    // nGas micro chp (5)
				"input_HH_NgasCHP_solar=",   // nGas microp chp solar (6)
				"input_fuel_Households[4]=", // bioMass boiler (7)
				"input_HH_bioboiler_Solar=", // biomas boiler Solar thermal (8)
				"input_HH_BioCHP_heat=", // biomass CHP (9)
				"input_HH_BioCHP_solar=", // biomass CHP Solar thermal (10)
				"input_HH_HP_heat=", // HP (11)
				"input_HH_HP_solar=", // HP Solar thermal (12)
				"input_fuel_Transport[5]=", // Petrol demand (13)
				"Input_Size_transport_conventional_cars=", // number of convensional cars (14)
				"input_transport_TWh=", // EV car electricity demand (15)
				"Input_Size_transport_electric_cars=", // number of electric car (16)
			
				"input_fuel_Transport[6]=", // hydrogen demand for Transport (17)
				"Input_Size_transport_other_vehicles1=", // number of FCEV car (18)
				"input_cap_ELTtrans_el=" // //corresponding eletrolyzer capacity (19)
		};

		String cooresPondingValues[] = new String[modifiedParameters.length];

		File modifiedInput = new File("modifiedInput.txt");
		if (modifiedInput.exists()) {
			modifiedInput.delete();
		}
		modifiedInput.createNewFile();

		FileWriter fw = new FileWriter(modifiedInput.getAbsoluteFile());
		BufferedWriter modifiedInputbw = new BufferedWriter(fw);
		
		String path=""; 
		if(simulatedYear.equals("2030") ){
			path ="C:\\Users\\shaik\\Desktop\\EnergyPLAN_12.3\\energyPlan Data\\Data\\PEAP\\PEAP_PAT_LC_2030_DH_v4_opt.txt";
		}else if(simulatedYear.equals("2050")) {
			path ="C:\\Users\\shaik\\Desktop\\EnergyPLAN_12.3\\energyPlan Data\\Data\\PEAP\\PEAP_PAT_LC_2050_DH_v4_opt.txt";
		}
		
    	BufferedReader mainInputbr = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-16"));
    	
		
		cooresPondingValues[0] = "" + pv;

		double oilBoilerFuelDemand = oilBoilerHeatPercentage * totalHeatDemand
				/ oilBoilerEfficiency;
		cooresPondingValues[1] = "" + oilBoilerFuelDemand;

		double oilBoilerSolarThermal = oilBoilerFuelDemand * oilBoilerSolarPercentage;
		cooresPondingValues[2] = "" + oilBoilerSolarThermal;

		double nGasBoilerFuelDemand = nGasBoilerHeatPercentage
				* totalHeatDemand / nGasBoilerEfficiency;
		cooresPondingValues[3] = "" + nGasBoilerFuelDemand;
		
		double nGasBoilerSolarThermal = nGasBoilerFuelDemand * nGasBoilerSolarPercentage;
		cooresPondingValues[4] = "" + nGasBoilerSolarThermal;

		double nGasCHPHeatDemand = nGasCHPHeatPercentage
				* totalHeatDemand;
		cooresPondingValues[5] = "" + nGasCHPHeatDemand; 
		
		double nGasCHPSolarThermal = nGasCHPHeatDemand * efficiencynGasCHPThermal *
				 nGasCHPSolarPercentage;
		cooresPondingValues[6] = "" + nGasCHPSolarThermal;
		
		double biomassBoilerFuelDemand = biomassBoilerHeatPercentage
				* totalHeatDemand / biomassBoilerEfficiency;
		cooresPondingValues[7] = "" + biomassBoilerFuelDemand;

		double biomassBoilerSolarThermal = biomassBoilerFuelDemand
				* biomassBoilerSolarPercentage;
		cooresPondingValues[8] = "" + biomassBoilerSolarThermal;

		double baiomassCHPHeatDemand = biomassCHPHeatPercentage
				* totalHeatDemand;
		cooresPondingValues[9] = "" + baiomassCHPHeatDemand;

		double biomassCHPSolarThermal = baiomassCHPHeatDemand * efficiencyBiomassCHPThermal *
				 biomassCHPSolarPercentage;
		cooresPondingValues[10] = "" + biomassCHPSolarThermal;

		double hpHEatDemand = hpHeatPercentage * totalHeatDemand;
		cooresPondingValues[11] = "" + hpHEatDemand;

		double hpSolarThermal = hpHeatPercentage * totalHeatDemand
				* hpSolarPercentage;
		cooresPondingValues[12] = "" + hpSolarThermal;

		cooresPondingValues[13] = "" + totalPetrolDemandInTWhForTrns;

		double numberOfConCars = (totalPetrolDemandInTWhForTrns * Math.pow(10, 9))
				/ (efficiencyConCar * averageKMPerYearPerCar * Math.pow(10, 3));
		cooresPondingValues[14] = "" + numberOfConCars;

		
		cooresPondingValues[15] = "" + totalElecDemandInTWhForTrns;

		double numberOfEVCars = totalH2DemandInTWhForTrns * Math.pow(10, 9)
				/ (efficiencyEVCar * averageKMPerYearPerCar * Math.pow(10, 3));
		cooresPondingValues[16] = "" + numberOfEVCars;

		cooresPondingValues[17] = "" + totalH2DemandInTWhForTrns;

		double numberOfFCEVCars = totalH2DemandInTWhForTrns
				* Math.pow(10, 9)
				/ (efficiencyFCEVCar * averageKMPerYearPerCar * Math.pow(10, 3));
		cooresPondingValues[18] = "" + numberOfFCEVCars;

		double electrolyzerCapacity = (int) Math.floor(maxH2DemandInDistribution
				* totalH2DemandInTWhForTrns * Math.pow(10, 6)
				/ (efficiencyElectrolyzerTrans * sumH2DemandInDistribution))+1;
		cooresPondingValues[19] = "" + electrolyzerCapacity;

		String line;
		while ((line = mainInputbr.readLine()) != null) {
			line.trim();
			boolean trackBreak=false;
			for (int i = 0; i < modifiedParameters.length; i++) {
				if (line.startsWith(modifiedParameters[i]) || line.endsWith(modifiedParameters[i])) {
					modifiedInputbw.write(line+"\n" );
					line = mainInputbr.readLine();
					line=line.replace(line, cooresPondingValues[i]);
					modifiedInputbw.write(line+"\n");
					trackBreak=true;
					break;
				}
			}
			if(trackBreak)
				continue;
			else
				modifiedInputbw.write(line+"\n");

		}

		modifiedInputbw.close();
		mainInputbr.close();
	}

}
