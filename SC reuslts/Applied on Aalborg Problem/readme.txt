This folder contains the results of our proposed stopping criteria (ADV+DIV) applied on Aalborg problem (reet.fbk.eu.OptimizeEnergyPLANAalborg.problem.EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution). 

populationSize: 100
MaxEvaluation: 10000
Crossover: SBX
Crossover probability: 0.9
Mutation: GeneralModifiedPolynomialMutationForEnergySystems
Mutation probability: 1/numberOfDecisionVariables
Boolean favorGenesforRE[] ={true, true, null, true, true, true, null};
Boolean favorGenesforConventionalPP[] ={false, false, null, false, false, false, null};
Algotithm: NSGAII (NSGAIIForDKandSC; reet.fbk.eu.jmetal.metaheuristics.nsgaII.NSGAIIForDKandSC )

Stopping Criteria parameters:

nGenLT = 20
noGenUnCh = 5;
significanceValue = 0.05;
maxEvalution=10000