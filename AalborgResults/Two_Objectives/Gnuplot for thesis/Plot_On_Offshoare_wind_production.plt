set terminal pdf enhanced
set output 'AalborgProblemOnOffShoreWindPro.pdf'

set xrange [0:1500]
set yrange [0:3.0]

set xlabel 'Capacity (MW)'
set ylabel 'Production (TWh)'

plot "On_OffShore_wind_production.txt" using 1:2 lt 10 title "OnShore Wind", "On_OffShore_wind_production.txt" using 3:4 lt 21 title "OffShore Wind"

unset output
unset terminal
