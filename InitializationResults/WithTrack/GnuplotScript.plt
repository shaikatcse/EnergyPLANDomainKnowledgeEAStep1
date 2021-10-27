cd 'C:\Users\mahbub\Documents\GitHub\EnergyPLANDomainKnowledgeEAStep1\InitializationResults\WithTrack'
set terminal postscript eps enhanced color font 'Helvetica, 18'
set output 'Compare_avg_ind.eps'

set multiplot layout 2, 2 font ",16"
set xlabel "Generations"
set ylabel "Average HV"
set key bottom right
set key font ",16"
set xtics font ", 14"
set ytics font ", 14"

plot 'SPEA2_with_SPEA2_SI.txt' using 4:5 w lp  title "SPEA2", 'SPEA2_with_SPEA2_SI.txt' using 4:6 w lp lc rgb "blue" title "SPEA2\\_SI"

unset key
set xlabel "Generations"
set ylabel "Average IGD"
set key top right
set key font ",16"
set xtics font ", 14"
set ytics font ", 14"
plot 'SPEA2_with_SPEA2_SI.txt' using 7:8 w lp title "SPEA2", 'SPEA2_with_SPEA2_SI.txt' using 7:9 w lp lc rgb "blue" title "SPEA2\\_SI"

unset key
set xlabel "Generations"
set ylabel "Average Epsilon"
set key top right
set key font ",16"
set xtics font ", 14"
set ytics font ", 14"
plot 'SPEA2_with_SPEA2_SI.txt' using 1:2 w lp title "SPEA2", 'SPEA2_with_SPEA2_SI.txt' using 1:3 w lp lc rgb "blue" title "SPEA2\\_SI"

unset key
set xlabel "Generations"
set ylabel "Average Spread"
set key top right
set key font ",16"
set xtics font ", 14"
set ytics font ", 14"
plot 'SPEA2_with_SPEA2_SI.txt' using 10:11 w lp title "SPEA2", 'SPEA2_with_SPEA2_SI.txt' using 10:12 w lp lc rgb "blue" title "SPEA2\\_SI"

unset multiplot
set output

