#set terminal pdf enhanced 
#set output 'TargetScenarios.pdf'

set terminal pdf enhanced
set output 'TargetScenarios.pdf'

set xlabel 'CO_2 emission (kt)'
set ylabel 'Annual cost (kEuro)'

set xrange [0:55]
set xtics 5

offset=0.5

#set label "50-55% emission reduction" at 44.7, 150000 rotate by 90 font ",8" front

set label "50-55%" at 46, 166000 rotate by 90 font ",10" front
set label "emission reduction" at 47, 160000 rotate by 90 font ",8" front

set label "65-70%" at 31.5, 166000 rotate by 90 font ",10" front
set label "emission reduction" at 32.5, 160000 rotate by 90 font ",8" front

set label "95-100%" at 2.5, 160000 rotate by 90 font ",10" front
set label "emission reduction" at 3.5, 155000 rotate by 90 font ",8" front

set datafile separator ","
set arrow from 44.04416771-offset, 140000 to 44.04416771-offset, 181000 front nohead lc rgb "black"
set arrow from 49+offset, 140000 to 49+offset, 181000 front nohead lc rgb "black"

set arrow from 29.69583392-offset, 140000 to 29.69583392-offset, 181000 front nohead lc rgb "black"
set arrow from 34.17427419+offset, 140000 to 34.17427419+offset, 181000 front nohead lc rgb "black"

set arrow from 0.855292302-offset, 140000 to 0.855292302-offset, 178000 front nohead lc rgb "black"
set arrow from 4.43319011+offset, 140000 to 4.43319011+offset,   178000 front nohead lc rgb "black"

#set arrow from 29.93407394, 146092 to 1.551402904, 147341 front lw 2 lc rgb "black"

plot 'ParetoFrontsTransition.csv' using 1:2 lt 4  linecolor rgb "#FF9999" t "2020:2DS:Pareto-front", \
                     '' using 3:4 lt 6 linecolor rgb "#FFCC99" t "2030:2DS:Pareto-front",\
					 '' using 5:6 lt 8 linecolor rgb "#99CCFF" t "2050:2DS:Pareto-front",\
					 '' using 7:8 lt 5 linecolor rgb "#66CC00" t "2020:2DS:Target Scenarios",\
					 '' using 9:10 lt 7 linecolor rgb "#00CCCC" t "2030:2DS:Target Scenarios",\
					 '' using 11:12 lt 9 linecolor rgb "#6600CC" t "2050:2DS:Target Scenarios",
					 


#unset label
unset arrow
				 
					 
unset output
unset terminal