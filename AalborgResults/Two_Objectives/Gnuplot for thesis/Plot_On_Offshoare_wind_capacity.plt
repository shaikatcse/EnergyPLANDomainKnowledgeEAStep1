set terminal pdf enhanced
set output 'AalborgProblemOnOffShoreWindCap.pdf'

set xrange [-0.6:0.6]
set yrange [0000:1600]

set xlabel 'CO_2 emissions (in Mton)'
set ylabel 'Capacity (MW)'

plot "On_OffShore_wind_capacity.txt" using 1:2 title "OnShore Wind", "On_OffShore_wind_capacity.txt" using 1:3 title "OffShore Wind"

unset output
unset terminal
