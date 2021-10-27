set terminal pdf enhanced
set output 'visual_comparison_mutation.pdf'

#set terminal eps enhanced
#set output 'visual comparison mutation.eps'


set xrange [-0.6:0.7]
set xlabel "CO_2 emissions (in Mt)"
set ylabel "Annual cost (in  Million DKK)"

plot "SPEA2\\FUN_seed_447514" using 1:2 title "SPEA2 with PM" , "SPEA2WithDKMutation\\With_mutation_pr_0.1\\FUN_seed_154568" using 1:2 title "SPEA2 with SM"

unset output