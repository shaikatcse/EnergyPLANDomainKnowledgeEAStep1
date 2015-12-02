This is the results (FUN and VAR) of 30 different runs with 30 different seeds with spea2 with smart initilization for building true Pareto front.

Problem: EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution (modified to work without PP and boiler capacity, now, the number of decision variable is 5. The file will be found in initilization folder)

Population: 100
Evolution: 7000
Crossover: SBX
Mutation: polynomial
archive size: 100
crossover Probabilty: 0.9
Mutation probability: 0.1
Distribution index: 10
Algorithm: SPEA2ForSI
selection: Binary Tournament 2.

run # 1 to run# 10
long seed [] = {857578, 647647, 647848, 891747, 957363, 538947, 425374, 637384, 125386, 243858 };

//2nd seed for spea2 (run# 11 to run 20)
long seed [] = {
				145295,
				378578,
				384873,
				437331,
				461628,
				544774,
				631545,
				712308,
				733779,
				878532
		};

//3rd seed for spea2 (run# 21 to run# 30)
long seed [] = {
				158570,
				207621,
				227297,
				354528,
				356090,
				764889,
				866208,
				867882,
				917083,
				919749
};
read initial population from "C:\Users\mahbub\Documents\GitHub\EnergyPLANDomainKnowledgeEAStep1\InitializationResults\InitIndividualWithSI" folder. These population are without random individuals.

mergeFUN_SSI: all Pareto front from 30 runs with SPEA2 with smart inililization
