#set terminal pdf enhanced 
#set output 'TargetScenarios.pdf'

set terminal pdf enhanced
set output 'HypoTargetScenarios.pdf'

set xlabel 'CO_2 emission'
set ylabel 'Annual cost'

unset xtics
unset ytics



set xrange [-0.9:0.9]
set yrange [2200:6000]

#set xtics 0.1
#set ytics 500

offset=0.07

#set label "50-55% emission reduction" at 44.7, 150000 rotate by 90 font ",8" front

#set label "50-55%" at 46, 166000 rotate by 90 font ",10" front
#set label "emission reduction" at 47, 160000 rotate by 90 font ",8" front

#set label "65-70%" at 31.5, 166000 rotate by 90 font ",10" front
#set label "emission reduction" at 32.5, 160000 rotate by 90 font ",8" front

#set label "95-100%" at 2.5, 160000 rotate by 90 font ",10" front
#set label "emission reduction" at 3.5, 155000 rotate by 90 font ",8" front

#set datafile separator ","
#set arrow from 44.04416771-offset, 140000 to 44.04416771-offset, 181000 front nohead lc rgb "black"
#set arrow from 49+offset, 140000 to 49+offset, 181000 front nohead lc rgb "black"

set arrow from 0.3-offset, 2200 to 0.3-offset, 5000 front nohead lc rgb "black"
set arrow from 0.3+offset, 2200 to 0.3+offset, 5000 front nohead lc rgb "black"

set label "CO2_{L}^{Y1}" at 0.3-offset-0.03, 5150 font ",10" front
set label "CO2_{U}^{Y1}" at 0.3+offset-0.03, 5150 font ",10" front

set arrow from -0.4-offset, 2200 to -0.4-offset, 5000 front nohead lc rgb "black"
set arrow from -0.4+offset, 2200 to -0.4+offset, 5000 front nohead lc rgb "black"

set label "CO2_{L}^{Y2}" at -0.4-offset-0.03, 5150 font ",10" front
set label "CO2_{U}^{Y2}" at -0.4+offset-0.03, 5150 font ",10" front


#transition
set arrow from 0.3, 3000 to -0.4, 3050 front lw 4 lc rgb "black"
set label "Transition from Y1 to Y2" at -0.25, 2920  font ",8" front

plot 'AalborgData.txt' using 1:2  linecolor rgb "red" t "Pareto-front: Y1",  \
					''	using 3:4 linecolor rgb "blue" t "pareto-front: Y2"
					 


#unset label
unset arrow
			
unset label			
					 
unset output
unset terminal