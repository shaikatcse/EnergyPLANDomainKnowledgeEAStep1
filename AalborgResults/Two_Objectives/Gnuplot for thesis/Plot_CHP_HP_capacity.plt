set terminal pdf enhanced
set output 'AalborgProblemCHPHPCap.pdf'

set xrange [-0.6:0.6]
set yrange [0:100]

set xlabel 'CO_2 emissions (Mt)'
set ylabel 'Capacity (MW)'

plot "CHP_HP_capacity.txt" using 1:2 lt 10 title "CHP", "CHP_HP_capacity.txt" using 1:3 lt 21 title "HP"

unset output
unset terminal
