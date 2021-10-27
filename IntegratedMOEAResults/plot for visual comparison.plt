set terminal pdf enhanced
set output 'visual_comparison_Com.pdf'

set xrange [-0.6:0.7]
set xlabel "CO_2 emission (in Mt)"
set ylabel "Annual cost (in  Million DKK)"

plot "SPEA2\\FUN_seed_154568" using 1:2 title "SPEA2" , "SPEA2_Int\\WithoutRM\\With_mutation_pr_0.1\\Criteria1\\FUN_Init_747584_seed_758115" using 1:2 title "SPEA2\\_Int"

unset output