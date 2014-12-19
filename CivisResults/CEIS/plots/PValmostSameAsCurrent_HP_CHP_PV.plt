unset xrange 
unset yrange 
unset xlabel
unset ylabel
#set yrange [0:17000]
set xrange [0:6.5]
set xlabel "CO2 emission in t/year"
set ylabel "Capacity in KW"
plot "PValmostSameAsCurrent" using 23:2 title "HP",  "PValmostSameAsCurrent" using 23:1 title "CHP" lt rgb 'blue', "PValmostSameAsCurrent" using 23:4 title "Boiler" lt rgb 'green'
unset xrange 
unset yrange 
unset xlabel
unset ylabel