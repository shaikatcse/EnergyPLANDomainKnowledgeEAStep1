This is the results (FUN and VAR) of 30 different runs with integrated NSGAII (Smart initialization + DK mutataion + Stopping criteria).

Problem: EnergyPLANProblemAalborg2ObjectivesWith1EnergyPLANEvolution 
Population: 100
Evolution: 7000
Crossover: SBX
Mutation: GeneralModifiedPolynomialMutationForEnergySystems (reet.fbk.eu.jmetal.operators.mutation.ModifiedPolynomial)
crossover Probabilty: 0.9
Mutation probability: 0.1
Distribution index: 10
Algorithm: SPEA2ForDKandSCandSI (reet.fbk.eu.jmetal.metaheuristics.nsgaII)
selection: Binary Tournament 2.

Domain-knowledge (same used for Smart Initialization and DKMutataion):
Boolean favorGenesforRE[] = { true, true, true, true, true };
Boolean favorGenesforConventionalPP[] = { false, false, false,false, false };

Seeds:
long seed[]={
			131528,
			194125,
			199140,
			286828,
			287192,
			303839,
			304408,
			325235,
			337877,
			347354,
			
			376366,
			449029,
			472366,
			507361,
			526317,
			570490,
			571961,
			585378,
			607688,
			664876,
			
			684002,
			692766,
			749363,
			853504,
			860186,
			892574,
			925410,
			931203,
			962803,
			991907

	};

The simutationa run unill all the evaluations are completed. FUN and VAR files for all evaluaitons completed are kept in AllEvoCom folder. Additionally, there are two stopping criteria (folder: criteria1, criteria2). 

There are two stopping criteria.
Criterion # 1:
Parameters for Stopping criteria:
nGenLT = 20
noGenUnCh = 5;
significanceValue = 0.05;
Criterion#2:
nGenLT=15 
nGenUnCh=7 
alpha=0.05
AllEvoCom:
all 700 evaluations are completed.

Initial population read from 'C:\Users\mahbub\Documents\GitHub\EnergyPLANDomainKnowledgeEAStep1\InitializationResults\InitIndividualWithSI\WithoutRM' folder. These population are generated without random individuals.

Output:
FUN*: Fun files for different run
VAR*: var files for diferent run
StoppingGen: stopping generation fr different run

mergeFuUN_NInt_WRM: merge all Pareto-front of 30 runs for NSGA-II integrated/combined approach when stopping criteria # 1 is activated.
mergeFuUN_SInt_WRM_AllEvo: merge all Pareto-front of 30 runs for NSGA-II integrated/combined approach with all evaluations completed.

N.B.: results are criteria # 1 are reported in ASOC paper