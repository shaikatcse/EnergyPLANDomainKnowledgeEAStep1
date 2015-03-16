This is the results (FUN and VAR) of 10 different runs with 10 different seeds with NSGAII with smart initilization for building true Pareto front.

Problem: EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution (modified to work without PP and boiler capacity, now, the number of decision variable is 5. The file will be found in initilization folder)

Population: 100
Evolution: 7000
crossover Probabilty: 0.9
Mutation probability: 0.2
Distribution index: 10
Algorithm: NSGA-IIForSI
selection: Binary Tournament 2.

long seed [] = {343434, 551254, 145845, 555541, 551641,625882,985312,458745, 228424, 7811554 };

Boolean favorGenesforRE[] ={true, true, true, true, true};
Boolean favorGenesforConventionalPP[] ={false, false, false, false, false};

Parameters regarding smart initialization:
theta = 6.0
MaxDistributionIndex = 4
numberOfIndevPerCombination = 3