#seed for nsgaii
#seed={545782, 455875, 547945, 458478, 981354, 652262, 562366, 365652, 456545, 549235 }
#seed for spea2
#long seed[]={102354,986587,456987,159753, 216557,589632,471259,523486,4158963,745896}; 

set terminal pdf enhanced
set output 'compareMutationtwoalgo.pdf'

set multiplot layout 1,2 title "Comparison of Patero-fronts found by different algorithms using different mutations"
set key font ",9"

#seed= "652262"; prospetive seed
seed="545782"

poly_mutation_path_NSGAII="../Results/MutationStudyNSGAII/data/PolynomialMutation/OptimizeElecEnergy_NSGAII/"
DKMutation_path_NSGAII="../Results/MutationStudyNSGAII/data/DKMutation/OptimizeElecEnergy_NSGAII/"
#truePF_path="../Results/truePF/mergefun.pf"

set xrange [0:16]
set yrange [12000:30000]

set title 'NSGAII'
set xlabel 'CO2 emssion (in Mton)'
set ylabel 'Annual Cost (in Million DKK)'

plot poly_mutation_path_NSGAII.'FUN_SBX_PolynomialMutation_seed_'.seed using 1:2 title 'Polynomial Mutation' lc rgb 'blue' ,\
DKMutation_path_NSGAII.'FUN_SBX_ModifiedPolynomialMutation_seed_'.seed using 1:2 title 'DK Mutation' lc rgb 'red' 
#,\truePF_path using 1:2 title 'True PF' lc rgb 'pink',

#spea2: 745896

poly_mutation_path_SPEA2="../Results/MutationStudySPEA2/data/PolynomialMutation/OptimizeElecEnergy_SPEA2/"
DKMutation_path_SPEA2="../Results/MutationStudySPEA2/data/DKMutation/OptimizeElecEnergy_SPEA2/"

seed= "745896";

set xrange [0:16]
set yrange [12000:30000]

set title 'SPEA2'
set xlabel 'CO2 emssion (in Mton)'
set ylabel 'Annual Cost (in Million DKK)'

plot poly_mutation_path_SPEA2.'FUN_SBX_PolynomialMutation_seed_'.seed using 1:2 title 'Polynomial Mutation' lc rgb 'blue' ,\
DKMutation_path_SPEA2.'FUN_SBX_PolynomialMutation_seed_'.seed using 1:2 title 'DK Mutation' lc rgb 'red' 
#,\truePF_path using 1:2 title 'True PF' lc rgb 'pink',

unset multiplot