set terminal pdf enhanced 
set output '2050compromisedScenarios.pdf'

set multiplot layout 1,2 

set datafile separator ","

set title '2050:2DS'

set xlabel 'CO_2 emission (kt)'
set ylabel 'Annual cost (KEuro)'

set key font ",7"
plot '2050compromisedScenarios.csv' using 1:2 lt 1 linecolor rgb "red" t 'Pareto-front', \
							 '' using 3:4 lt 1 linecolor rgb "blue" t 'compromised scenario'
unset key

set title '2050:4DS'
set xlabel 'CO_2 emission (kt)'
set ylabel 'Annual cost (KEuro)'

set key font ",7"
plot '2050compromisedScenarios.csv' using 6:7 lt 1 linecolor rgb "red" t 'Pareto-front', \
							 '' using 8:9 lt 1 linecolor rgb "blue" t 'compromised scenario'
unset key


unset multiplot
unset output
unset terminal