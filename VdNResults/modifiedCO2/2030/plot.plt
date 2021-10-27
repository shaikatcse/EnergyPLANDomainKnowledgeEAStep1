set term png            
set output "Pareto-front.png" 

set multiplot layout 2, 1 title "Pareto-front for 2030" font ",14"
set datafile separator ";"

set xlabel "CO2 emission (kt)"
set ylabel "Annaul cost (kEuro)"
set cblabel "LFC"

plot "allParameters_mergeVAR.pf.txt" every ::2 using 36:37:38 pt 7 palette notitle 

unset key
unset xlabel
unset ylabel

set xlabel "CO2 emission (kt)"
set ylabel "Annaul cost (kEuro)"
set cblabel "ESD"
plot "allParameters_mergeVAR.pf.txt" every ::2 using 36:37:39 pt 7 palette notitle 

unset multiplot
unset key
unset output
