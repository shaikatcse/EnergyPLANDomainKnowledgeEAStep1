unset xrange 
unset yrange 
unset xlabel
unset ylabel
set yrange [0:1200]
set xrange [3:4.5]
set xlabel "CO2 emission in t/year"
set ylabel "Capacity in KW"
plot "LFClesThan0.45" using 23:2 title "HP",  "LFClesThan0.45" using 23:1 title "CHP" lt rgb 'blue'#, "LFClesThan0.45" using 23:3 title "PV"
unset xrange 
unset yrange 
unset xlabel
unset ylabel