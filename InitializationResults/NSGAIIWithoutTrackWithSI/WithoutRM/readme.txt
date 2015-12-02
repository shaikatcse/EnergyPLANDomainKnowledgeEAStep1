This is the results (FUN and VAR) of 10 different runs with 30 different seeds with NSGAII with smart initilization for building true Pareto front.

Problem: EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution (modified to work without PP and boiler capacity, now, the number of decision variable is 5. The file will be found in initilization folder)

Population: 100
Evolution: 7000
Crossover: SBX
Mutation: polynomial
crossover Probabilty: 0.9
Mutation probability: 0.1
Distribution index: 10
Algorithm:NSGAIIForSI
selection: Binary Tournament 2.

long seed[] ={
			145845,
			198045,
			210901,
			228424,
			251843,
			343434,
			351008,
			427848,
			434479,
			447778,
			458745,
			551254,
			551641,
			555541,
			607483,
			
			613516,
			618448,
			619821,
			625882,
			685039,
			702847,
			741577,
			747201,
			764699,
			765255,
			789666,
			830237,
			915076,
			985312,
			7811554
	};
				

read initial population from "C:\Users\mahbub\Documents\GitHub\EnergyPLANDomainKnowledgeEAStep1\InitializationResults\InitIndividualWithSI\WithoutRM" folder. These population are without random individuals.

mergeFUN_NSI_WRM: all Pareto front from 30 runs with NSGAII with smart inililization

N.B: this results are reported in ASOC paper