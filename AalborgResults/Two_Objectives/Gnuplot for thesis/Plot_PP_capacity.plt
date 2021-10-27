set terminal pdf enhanced
set output 'AalborgProblemPPCap.pdf'

set xrange [-0.6:0.6]
set yrange [0:1100]

unset key

set xlabel 'CO_2 emissions (Mt)'
set ylabel 'Capacity (MW)'

plot "PP_capacity.txt" using 1:2 title "PP"

unset output
unset terminal
