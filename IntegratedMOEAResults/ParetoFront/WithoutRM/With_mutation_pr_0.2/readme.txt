All the Pareto fronts found in 240 different runs are merged. Here, all the results considred the mutation probability 0.2. And new Pareto front is built. 

mergeFUN_N_M0.2: all Pareto front from 30 runs with NSGAII
mergeFUN_NSI_WRM_M0.2: all Pareto front from 30 runs with NSGAII with smart inililization without random individuals
mergeFUN_S_M0.2: all Pareto front from 30 runs with SPEA2
mergeFUN_SSI_WRM_M0.2: all Pareto front from 30 runs with SPEA2 with smart inililization without random individuals
mergeFUN_NDK: all Pareto front from 30 runs with NSGAII with DK/smart mutation
mergeFUN_SDK: all Pareto front from 30 runs with SPEA2 with DK/smart mutation
mergeFUN_NInt_WRM: all Pareto front from 30 runs with NSGAII with integrated/combined approach without random individuals in initialization phase
mergeFUN_SInt_WRM: all Pareto front from 30 runs with SPEA2 with integrated/combined approach without random individuals in initialization phase
mergeFUN: merge mergeFUN_*
mergeFUN.pf: Pareto front after merging smart initializzation+ DK/smart mutation+integratedMOEA results
