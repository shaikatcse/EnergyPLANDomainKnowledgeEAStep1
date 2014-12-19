unset xrange 
unset yrange 
unset xlabel
unset ylabel
set key outside 
set yrange [0:9000]
set xrange [0:7]
set xlabel "CO2 emission in t/year"
set ylabel "Capacity in KW"
plot "Bottom10%ofAnnualCost" using 23:2 title "HP",  "Bottom10%ofAnnualCost" using 23:1 title "CHP", "Bottom10%ofAnnualCost" using 23:3 title "PV", "Bottom10%ofAnnualCost" using 23:4 title "Boiler", 
unset xrange 
unset yrange 
unset xlabel
unset ylabel