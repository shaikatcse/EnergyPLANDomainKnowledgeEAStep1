Problem: reet.fbk.eu.OptimizeEnergyPLANCIVIS.CEIS.Problem.EnergyPLANProblemCivisCEIS4DWithTransport (night charging)
Algorithm: EnergyPLANProblemCivisCEIS4D
// index - 1 -> oil boiler heat percentage
// index - 2 -> LPG boiler heat percentage
// index - 3 -> Biomass boiler heat percentage
// index - 4 -> Biomass micro chp heat percentage
// last percentage will go to individual HP percentage
// index - 5 -> electric car percentage  
			
Boolean favorGenesforRE[] ={true, false, null, true, true, true };
Boolean favorGenesforConventionalPP[] ={false, null, null, true, false, false };
Boolean favorGenesforLFC[]={false, null, null, null, true, null};
Boolean favorGenesforESD[] ={true, false, false, true, true, true};
			
number of runs =3

algorithm.setInputParameter("populationSize", 150);
algorithm.setInputParameter("maxEvaluations", 11250);
// for spea2
algorithm.setInputParameter("archiveSize",150);

Crossover: SBX
Crossover probability: 0.9

Mutation: GeneralModifiedPolynomialMutationForEnergySystems
Mutation Probability : 1/6

Selection: BinaryTournament

Initialization:
Parameters regarding smal initialization:
theta = 6.0
MaxDistributionIndex = 3
numberOfIndevPerCombination = 1
numberOfRandomIndividuals=0

Stopping Criteria:
nGenLT=20
nGenUnCh=5
alpha=0.05