set terminal pdf enhanced 
set output 'paretoFronts2DS.pdf'

set multiplot

#main plot

set xlabel 'CO_2 emission (kt)'
set ylabel 'Annual cost (kEuro)'
set datafile separator ","
plot 'ParetoFrontsTransition.csv' \
						using 1:2 lt 1 linecolor rgb "#66CC00" t "2020:2DS", \
                     '' using 3:4 lt 4 linecolor rgb "#00CCCC" t "2030:2DS",\
					 '' using 5:6 lt 3 linecolor rgb "#6600CC" t "2050:2DS"



#now set option for the smaller plot
set size 0.6,0.40
set origin 0.37,0.45
set xlabel ""
set ylabel ""
set ytics 4000
set yrange [144000:154000]
plot 'ParetoFrontsTransition.csv'  using 3:4 lt 4 linecolor rgb "#00CCCC" notitle

unset multiplot
unset size
unset origin
unset ytics
unset yrange

unset output
unset terminal