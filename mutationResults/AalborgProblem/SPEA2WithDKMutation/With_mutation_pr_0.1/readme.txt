This is the results (FUN and VAR) of 30 different runs with 30 different seeds with SPEA2 with domain-knowledge mutation.

Problem: EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution
Population: 100
Evolution: 7000
Crossover: SBX
Mutation: GeneralModifiedPolynomialMutationForEnergySystems (reet.fbk.eu.jmetal.operators.mutation.ModifiedPolynomial)
crossover Probabilty: 0.9
Mutation probability: 0.1
Distribution index: 10
Algorithm: SPEA2
selection: Binary Tournament 2.
archiveSize: 100

long seed [] = {
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
 };
The seeds are same that is used in initilization experiment. It'll help to compare with typical SPEA2 as two algorithms starts from same initialized individuals.

mergeFUN_SDK: merge all 30 runs for SPEA2 with DK mutation

N.B.: These results are reported in ASOC paper