set terminal pdf enhanced
set output 'CIVISThermalDemand.pdf'

set xdata time
set timefmt "%m/%d/%Y"
set format x "%b"
set format y "%.1t*10^{%S}";
set xlabel "Months"
set ylabel "KWh"
set yrange [0: 9500000 ]

set key top center

plot "thermal_data.txt" using 1:3  notitle with points linestyle 1, \
     "" using 1:3 notitle smooth csplines with lines linestyle 1, \
     1 / 0 title "Energy demand for space heating" with linespoints linestyle 1, \
	\
	"thermal_data.txt" using 1:5  notitle with points linestyle 2, \
     "" using 1:5 notitle smooth csplines with lines linestyle 2, \
     1 / 0 title "Energy demand for hot sanitary water" with linespoints linestyle 2, \
	\
	 "thermal_data.txt" using 1:7  notitle with points linestyle 3, \
     "" using 1:7 notitle smooth csplines with lines linestyle 3, \
     1 / 0 title "Total thermal energy demand" with linespoints linestyle 3

unset key
	 unset yrange
unset ylabel
unset xlabel	 
unset format
unset xdata
unset timefmt
unset output
unset terminal
