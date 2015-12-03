SPEA2 and SPEA2_SI forlder have the average of each indicator values with each generation of 30 runs. The used Pareto front is "InitializationResults\\ParetoFront\\WithoutRM\\With_mutation_pr_0.1\\mergeFUN.pf".

SPEA2 configuration:
Problem: EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution
Population: 100
Evolution: 7000
Crossover: SBX
Mutation: polynomial
crossover Probabilty: 0.9
Mutation probability: 0.1
Distribution index: 10
Algorithm: SPEA2
selection: Binary Tournament 2.
archiveSize: 100

SPEA2 seeds:
	126113,
	148456,
	154568,
	212139,
	274587,
	289982,
	368817,
	387808,
	418353,
	430544,
	434057,
	447514,
	458475,
	467542,
	471922,
	500024,
	585464,
	596970,
	622980,
	632376,
	686544,
	712584,
	784248,
	808035,
	833359,
	879899,
	888346,
	953022,
	975572,
	983349




SPEA2_DK Configurations:

Problem: EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution (modified to work without PP and boiler capacity, now, the number of decision variable is 5. The file will be found in initilization folder)

Population: 100
Evolution: 7000
Crossover: SBX
Mutation: GeneralModifiedPolynomialMutationForRes (DKMutation)
crossover Probabilty: 0.9
Mutation probability: 0.1
Distribution index: 10
Algorithm:SPEA2ForSI
selection: Binary Tournament 2.

seed:
	126113,
	148456,
	154568,
	212139,
	274587,
	289982,
	368817,
	387808,
	418353,
	430544,
	434057,
	447514,
	458475,
	467542,
	471922,
	500024,
	585464,
	596970,
	622980,
	632376,
	686544,
	712584,
	784248,
	808035,
	833359,
	879899,
	888346,
	953022,
	975572,
	983349



Results:
Comparison of SPEA2 with SPEA2_DK for each generation (on average).xlsx

