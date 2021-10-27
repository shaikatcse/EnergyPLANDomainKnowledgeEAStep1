This is the results (FUN and VAR) of 30 different runs with 30 different seeds with NSGAII with smart initilization for building true Pareto front.

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

long seed [] = {343434, 551254, 145845, 555541, 551641,625882,985312,458745, 228424, 7811554 };

//2nd seed for NSGAII (run # 11 to run # 20)
long seed [] = {
				251843,
				351008,
				427848,
				434479,
				607483,
				618448,
				685039,
				764699,
				765255,
				915076

};
		
//3rd seed for NSGAII (run # 21 to run # 30)
long seed [] = {
				198045,
				210901,
				447778,
				613516,
				619821,
				702847,
				741577,
				747201,
				789666,
				830237

};

read initial population from "C:\Users\mahbub\Documents\GitHub\EnergyPLANDomainKnowledgeEAStep1\InitializationResults\InitIndividualWithSI" folder

mergeFUN_NSI: all Pareto front from 30 runs with NSGAII with smart inililization