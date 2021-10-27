unset xrange 
unset yrange 
unset xlabel
unset ylabel
set yrange [-10:15500]
set key outside horizontal  
#set xrange [3:4.5]
set xlabel "CO2 emission in t/year"
set ylabel "Capacity in KW"
plot "Top10%CO2emission" using 23:2 title "HP",  "Top10%CO2emission" using 23:1 title "CHP" lt rgb 'blue', "Top10%CO2emission" using 23:3 title "PV", "Top10%CO2emission" using 23:4 title "Boiler", 
unset xrange 
unset yrange 
unset xlabel
unset ylabel