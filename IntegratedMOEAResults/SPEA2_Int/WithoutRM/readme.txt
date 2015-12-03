This is the results (FUN and VAR) of 30 different runs with integrated SPEA2(Smart initialization + DK mutataion + Stopping criteria).

Problem: EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution 
Population: 100
Evolution: 7000
ArchiveSize: 100
Crossover: SBX
Mutation: GeneralModifiedPolynomialMutationForEnergySystems (reet.fbk.eu.jmetal.operators.mutation.ModifiedPolynomial)
crossover Probabilty: 0.9
Mutation probability: 0.2
Distribution index: 10
Algorithm: SPEA2ForDKandSCandSI (reet.fbk.eu.jmetal.metaheuristics.spea2)
selection: Binary Tournament 2.

Domain-knowledge (same used for Smart Initialization and DKMutataion):
Boolean favorGenesforRE[] = { true, true, true, true, true };
Boolean favorGenesforConventionalPP[] = { false, false, false,false, false };

long seed[]={
			116023,
			122077,
			148412,
			229494,
			234118,
			250755,
			289773,
			315371,
			366586,
			386609,
			
			403236,
			529557,
			542538,
			544241,
			625137,
			629633,
			630311,
			668240,
			686408,
			701282,
			
			710450,
			736124,
			758115,
			811191,
			861135,
			889553,
			922683,
			929630,
			943600,
			983919

	};

Parameters for Stopping criteria:
nGenLT = 20
noGenUnCh = 5;
significanceValue = 0.05;
initial population read from 'C:\Users\mahbub\Documents\GitHub\EnergyPLANDomainKnowledgeEAStep1\InitializationResults\InitIndividualWithSI\WithoutRM' folder. These population are generated without random individuals.

Output:
FUN*: Fun files for different run
VAR*: var files for diferent run
StoppingGen: stopping generation fr different run

mergeFUN_SInt_WRM: merge all Pareto-front for 30 runs with SPEA2 Integrated/combined approach