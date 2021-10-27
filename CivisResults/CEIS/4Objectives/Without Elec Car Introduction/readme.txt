Problem: reet.fbk.eu.OptimizeEnergyPLANCIVIS.CEIS.Problem.EnergyPLANProblemCivisCEIS4D
Algorithm: EnergyPLANProblemCivisCEIS4D
// decision variables
// index - 0 -> PV Capacity
// index - 1 -> oil boiler heat percentage
// index - 2 -> Ngas boiler heat percentage
// index - 3 -> Biomass boiler heat percentage
// index - 4 -> Biomass micro chp heat percentage
			
Boolean favorGenesforRE[] ={true, false, null, true, true };
Boolean favorGenesforConventionalPP[] ={false, null, null, true, false };
Boolean favorGenesforLFC[]={false, null, null, null, true};
Boolean favorGenesforESD[] ={true, false, false, true, true};

number of runs=3

algorithm.setInputParameter("populationSize", 150);
algorithm.setInputParameter("maxEvaluations", 11250);
// for spea2
algorithm.setInputParameter("archiveSize",150);

Crossover: SBX
Crossover probability: 0.9

Mutation: GeneralModifiedPolynomialMutationForEnergySystems
Mutation Probability : 1/5

Selection: BinaryTournament

Initialization:
Parameters regarding smal initialization:
theta = 6.0
MaxDistributionIndex = 3
numberOfIndevPerCombination = 3
numberOfRandomIndividuals=0

Stopping Criteria:
nGenLT=20
nGenUnCh=5
alpha=0.05