#seed for nsgaii
#seed={545782, 455875, 547945, 458478, 981354, 652262, 562366, 365652, 456545, 549235 }
#seed for spea2
#long seed[]={102354,986587,456987,159753, 216557,589632,471259,523486,4158963,745896}; 

#set terminal pdf
#set output 'compareMutation11.pdf'

seed= "102354";
poly_mutation_path="../Results/SPEA2_SBX_PolynomialMutation/"
DKMutation_path="../Results/SPEA2_SBX_ModifiedPolynomialMutation/"
truePF_path="../Results/truePF/mergefun.pf"
set xrange [0:16]
set yrange [12000:30000]

set title 'Pareto-front for two mutations'
set xlabel 'CO2 emssion (in Mton)'
set ylabel 'Annual Cost (in Million DKK)'

plot poly_mutation_path.'FUN_SBX_PolynomialMutation_seed_'.seed using 1:2 title 'Polynomial Mutation' lc rgb 'blue' ,\
DKMutation_path.'FUN_SBX_PolynomialMutation_seed_'.seed using 1:2 title 'DK Mutation' lc rgb 'red' ,\
truePF_path using 1:2 title 'True PF' lc rgb 'pink',
