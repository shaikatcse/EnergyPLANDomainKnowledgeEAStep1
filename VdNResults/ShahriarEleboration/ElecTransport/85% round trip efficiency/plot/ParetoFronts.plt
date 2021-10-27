set terminal pdf enhanced 
set output 'paretoFronts.pdf'

set xlabel 'CO_2 emissions (kt)'
set ylabel 'Annual cost (kEuro)'


set datafile separator ","
plot 'ParetoFronts.csv' using 1:2 lt 4  linecolor rgb "red" t "2020:2DS", \
                     '' using 3:4 lt 4 linecolor rgb "#0000FF" t "2030:2DS",\
					 '' using 5:6 lt 4 linecolor rgb "#CCCC00" t "2050:2DS",\
					 '' using 7:8 lt 11 linecolor rgb "#66CC00" t "2020:4DS",\
					 '' using 9:10 lt 11 linecolor rgb "#00CCCC" t "2030:4DS",\
					 '' using 11:12 lt 11 linecolor rgb "#6600CC" t "2050:4DS"
					 
unset output
unset terminal