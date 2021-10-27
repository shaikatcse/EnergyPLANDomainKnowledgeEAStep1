The 4 results are produced by NSGAIIForDK for Aalborg data. 
There are 6 decision variable (CHP group 3, HP for group 3, PP, wind, offshore wind, PV). 
There are two objectives (CO2 emssion and annual cost). 
There are three constraints.
1. Maximumimport <=160
2. minimum grid stablization factor >=100
3. Annual heat balance <=0

NSGAIIForDK parameters:
population: 100
max evaluation: 10000
Crossover: SBXCrossover, probability: 0.9, distribution index: 10
Mutation: GeneralModifiedPolynomialMutationForRes, Probability: 1.0/problem.getNumberOfVariables(), distribution index: 10
selection: binary tournament
Domain Knowledge:
Boolean favorGenesforRE[] ={true, true, null, true, true, true, null};
Boolean favorGenesforConventionalPP[] ={false, false, null, false, false, false, null};
order: chp3, HP, PP, wind, offshore, PV, boiler

PP and boiler capacity is allocsted deterministically.

Number of runs: 4

