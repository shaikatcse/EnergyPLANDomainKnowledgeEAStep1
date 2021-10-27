set terminal pdf enhanced
set output 'AalborgProblemDiterministicPPCap.pdf'

set xrange [-0.6:0.6]
set yrange [0:300]

unset key

set xlabel 'CO_2 emissions (Mt)'
set ylabel 'Capacity (MW)'

plot "Deterministic_PP_capacity.txt" using 1:2 title "PP"

unset output
unset terminal
