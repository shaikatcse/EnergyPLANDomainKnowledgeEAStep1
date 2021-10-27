This is the results (FUN and VAR) of 30 different runs with 30 different seeds with NSGAII with domain-knowledge mutation for.

Problem: EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution
Population: 100
Evolution: 7000
Crossover: SBX
Mutation: GeneralModifiedPolynomialMutationForEnergySystems (reet.fbk.eu.jmetal.operators.mutation.ModifiedPolynomial)
crossover Probabilty: 0.9
Mutation probability: 0.2
Distribution index: 10
Algorithm: NSGA-II
selection: Binary Tournament 2.


//run#1 to run# 10
seed [] = {545782, 455875, 547945, 458478, 981354, 652262, 562366, 365652, 456545, 549235 };

//2nd 10 runs with 10 new seeds (run # 11 to run #20)
long seed [] = {
				161395,
				276644,
				309259,
				370995,
				468245,
				515856,
				668763,
				698156,
				756252,
				930462

				
		};

//3rd seed for nsgaii (run#21 to run#30)
long seed [] = {
				170339,
				259029,
				288470,
				353882,
				626495,
				683991,
				714786,
				989776,
				992177,
				999921
			
		};
The seeds are same that is used in initilization experiment. It'll help to compare with typical NSGAII as two algorithms starts from same initialized individuals.


mergeFUN_NDK: merge all 30 runs fo NSGA-II with DK mutation