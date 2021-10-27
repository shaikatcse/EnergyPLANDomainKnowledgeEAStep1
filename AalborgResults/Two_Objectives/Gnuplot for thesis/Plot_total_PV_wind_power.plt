set terminal pdf enhanced
set output 'AalborgProblemTotalPVWindPower.pdf'

set xrange [-0.6:0.6]
set yrange [0000:3000]

set xlabel 'CO_2 emissions (in Mton)'
set ylabel 'Capacity (MW)'

plot "Total_PV_Wind_power.txt" using 1:2 title "Total Wind power", "Total_PV_Wind_power.txt" using 1:3 title "PV"

unset output
unset terminal
