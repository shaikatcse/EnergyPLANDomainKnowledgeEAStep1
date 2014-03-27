#seed={545782, 455875, 547945, 458478, 981354, 652262, 562366, 365652, 456545, 549235 }

set terminal pdf
set output 'compareMutation11.pdf'

seed= "365652";
poly_mutation_path="../Results/SBX_Poly/"
DKMutation_path="../Results/Mutation_Pr_0.4/SBX_DKMutation/"

set title 'Pareto-front for two Mutations'
set xlabel 'CO2 emssion (in Mton)'
set ylabel 'Annual Cost (in Million DKK)'
plot poly_mutation_path.'FUN_SBX_Poly_seed_'.seed using 1:2 title 'Polynomial Mutation' lc rgb 'blue' ,\
DKMutation_path.'FUN_SBX_DKMutation_seed_'.seed using 1:2 title 'DK Mutation' lc rgb 'red'
