#seed={545782, 455875, 547945, 458478, 981354, 652262, 562366, 365652, 456545, 549235 }

set terminal pdf
set output 'compareMutation.pdf'

seed= "365652";

set title 'Pareto-front for two Mutations'
set xlabel 'CO2 emssion (in Mton)'
set ylabel 'Annual Cost'  
plot '../SBX_Poly\FUN_SBX_Poly_seed_'.seed using 1:2 title 'Polynomial Mutation' lc rgb 'blue' ,\
'../SBX_DKMutation\FUN_SBX_DKMutation_seed_'.seed using 1:2 title 'DK Mutation' lc rgb 'red'
