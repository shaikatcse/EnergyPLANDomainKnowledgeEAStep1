The 4 results are produced by NSGAII for Aalborg data. 
There are 6 decision variable (CHP group 3, HP for group 3, PP, wind, offshore wind, PV). 
There are two objectives (CO2 emssion and annual cost). 
There are three constraints.
1. Maximumimport <=160
2. minimum grid stablization factor >=100
3. Annual heat balance <=0

NSGAII parameters:
population: 100
max evaluation: 15000
Crossover: SBXCrossover, probability: 0.9, distribution index: 10
Mutation: PolynomialMutation, Probability: 1.0/problem.getNumberOfVariables(), distribution index: 10
selection: binary tournament

This results are generated where PP capccity is determine by optimizer.