The 4 results are produced by NSGAIIForDK for Aalborg data. 

There are 7 decision variable (CHP group 3, HP for group 3, PP, wind, offshore wind, PV, boiler). 
PP and boiler capcity is dertermine deterministically

There are two objectives (CO2 emssion and annual cost). 
There are three constraints.
1. Maximumimport <=160
2. minimum grid stablization factor >=100
3. Annual heat balance <=0

Algorithm: NSGAIIForDK (reet.fbk.eu.jmetal.metaheuristics.nsgaII)

NSGAII parameters:
population: 100
max evaluation: 15000
Crossover: SBXCrossover, probability: 0.9, distribution index: 10
Mutation: GeneralModifiedPolynomialMutationForRes (reet.fbk.eu.jmetal.operators.mutation.ModifiedPolynomial)
Probability: 1.0/problem.getNumberOfVariables(), distribution index: 10
Domain-knowledge:
Boolean favorGenesforRE[] ={true, true, null, true, true, true, null};
Boolean favorGenesforConventionalPP[] ={false, false, null, false, false, false, null};
ture-> increase, false -> decrease
selection: binary tournament

