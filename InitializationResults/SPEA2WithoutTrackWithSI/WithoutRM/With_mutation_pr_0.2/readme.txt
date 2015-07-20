This is the results (FUN and VAR) of 10 different runs with 10 different seeds with NSGAII with smart initilization for building true Pareto front.

Problem: EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution (modified to work without PP and boiler capacity, now, the number of decision variable is 5. The file will be found in initilization folder)

Population: 100
Evolution: 7000
Crossover: SBX
Mutation: polynomial
crossover Probabilty: 0.9
Mutation probability: 0.2 (mutation probability is increased to match with DKMutation where 0.2 is used as mutation probability)
Distribution index: 10
Algorithm:SPEA2ForSI
selection: Binary Tournament 2.

	long seed[]={
			125386,
			145295,
			158570,
			207621,
			227297,
			243858,
			354528,
			356090,
			378578,
			384873,
			
			425374,
			437331,
			461628,
			538947,
			544774,
			631545,
			637384,
			647647,
			647848,
			712308,
			
			733779,
			764889,
			857578,
			866208,
			867882,
			878532,
			891747,
			917083,
			919749,
			957363

	};
	
				

read initial population from "C:\Users\mahbub\Documents\GitHub\EnergyPLANDomainKnowledgeEAStep1\InitializationResults\InitIndividualWithSI\WithoutRM" folder. These population are without random individuals.

mergeFUN_SSI_WRM: all Pareto front from 30 runs with SPEA2 with smart inililization