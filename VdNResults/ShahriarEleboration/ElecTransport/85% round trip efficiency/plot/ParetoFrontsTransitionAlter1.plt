set terminal pdf enhanced 
set output 'paretoFrontsTransitionAlter1.pdf'

set xlabel 'CO_2 emission (kt)'
set ylabel 'Annual cost (kEuro)'

set label "Transition from 2020 to 2030" at 30, 143000 rotate by 3 font ",8" front
set label "Transition from 2030 to 2050" at 10, 144000 font ",8" front
set label "Transient Scenarios  (in red color) " at 25, 184500

set datafile separator ","
set arrow from 44.04416771, 146891 to 29.93407394, 146092 front lw 2 lc rgb "blue"
set arrow from 29.93407394, 146092 to 1.551402904, 147341 front lw 2 lc rgb "black"
plot 'ParetoFrontsTransition.csv' using 1:2 lt 4  linecolor rgb "#FF9999" t "2020:2DS", \
                     '' using 3:4 lt 6 linecolor rgb "#FFCC99" t "2030:2DS",\
					 '' using 5:6 lt 8 linecolor rgb "#99CCFF" t "2050:2DS",\
					 '' using 7:8 lt 5 linecolor rgb "#66CC00" t "2020:Target Scenarios",\
					 '' using 9:10 lt 7 linecolor rgb "#00CCCC" t "2030:Target Scenarios",\
					 '' using 11:12 lt 9 linecolor rgb "#6600CC" t "2050:Target Scenarios",\
					 '' using 13:14 lt 5 linecolor rgb "red" notitle ,\
					 '' using 15:16 lt 7 linecolor rgb "red" notitle,\
					 '' using 17:18 lt 9 linecolor rgb "red" notitle
					 



unset label
unset arrow
					 
					 
unset output
unset terminal