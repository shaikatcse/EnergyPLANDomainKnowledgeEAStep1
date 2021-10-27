set terminal pdf enhanced 
set output 'CompromisedScenarios.pdf'

#set multiplot layout 1,2 

set datafile separator ","

set xrange [-0.6:0.6]
set yrange [2000:6000]

#set title '2050:2DS'

set xlabel 'CO_2 emissions (in Mton)'
set ylabel 'Annual cost (in Million DKK)'

plot 'CompromisedScenarios.csv' using 1:2 lt 1 linecolor rgb "red" t 'Pareto-front', \
							 '' using 3:4 lt 1 linecolor rgb "blue" t 'Compromised scenarios', \
							 '' using 5:6 lt 3 linecolor rgb "green" t 'Ideal point'
unset key

#set title '2050:4DS'
#set xlabel 'CO_2 emissions (in Mton)'
#set ylabel 'Annual cost (in )'

#set key font ",7"
#plot '2050compromisedScenarios.csv' using 6:7 lt 1 linecolor rgb "red" t 'Pareto-front', \
#							 '' using 8:9 lt 1 linecolor rgb "blue" t 'compromised scenario'
#unset key


#unset multiplot
unset output
unset terminal