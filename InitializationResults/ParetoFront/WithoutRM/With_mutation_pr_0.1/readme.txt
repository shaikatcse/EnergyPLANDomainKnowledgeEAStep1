All the Pareto fronts found in 240 different runs are merged. And new Pareto front is built.

mergeFUN_N: all Pareto front from 30 runs with NSGAII with mutation probability 0.1 
mergeFUN_NSI_WRM: all Pareto front from 30 runs with NSGAII with smart inililization without random individuals with mutation probability 0.1 
mergeFUN_S: all Pareto front from 30 runs with SPEA2 with mutation probability 0.1 
mergeFUN_SSI_WRM: all Pareto front from 30 runs with SPEA2 with smart inililization without random individuals with mutation probability 0.1 
mergeFUN_NDK: all Pareto front from 30 runs with NSGAII with DK/smart mutation with mutation probability 0.1 
mergeFUN_SDK: all Pareto front from 30 runs with SPEA2 with DK/smart mutation with mutation probability 0.1 
mergeFUN_NInt_WRM_AllEvo: all Pareto front from 30 runs with NSGAII with integrated/combined approach without random individuals in initialization phase (With mutation probability 0.1) and all evaluations completed
mergeFUN_SInt_WRM_AllEvo: all Pareto front from 30 runs with SPEA2 with integrated/combined approach without random individuals in initialization phase (With mutation probability 0.1) and all evaluations completed
mergeFUN: merge mergeFUN_*
mergeFUN.pf: Pareto front after merging smart initializzation+ DK/smart mutation+integratedMOEA results
