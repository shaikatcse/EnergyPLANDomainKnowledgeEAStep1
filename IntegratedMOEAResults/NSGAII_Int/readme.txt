This is the results (FUN and VAR) of 30 different runs with integrated NSGAII (Smart initialization + DK mutataion + Stopping criteria).

Problem: EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution 
Population: 100
Evolution: 7000
Crossover: SBX
Mutation: GeneralModifiedPolynomialMutationForEnergySystems (reet.fbk.eu.jmetal.operators.mutation.ModifiedPolynomial)
crossover Probabilty: 0.9
Mutation probability: 0.2
Distribution index: 10
Algorithm: NSGAIIForDKandSCandSI (reet.fbk.eu.jmetal.metaheuristics.nsgaII)
selection: Binary Tournament 2.

Domain-knowledge (same used for Smart Initialization and DKMutataion):
Boolean favorGenesforRE[] = { true, true, true, true, true };
Boolean favorGenesforConventionalPP[] = { false, false, false,false, false };

Parameters for Stopping criteria:
nGenLT = 20
noGenUnCh = 5;
significanceValue = 0.05;

Output:
FUN*: Fun files for different run
VAR*: var files for diferent run
StoppingGen: stopping generation fr different run

mergeFuUN_NInt: merge all Pareto-front of 30 runs for NSGA-II integrated/combined approach