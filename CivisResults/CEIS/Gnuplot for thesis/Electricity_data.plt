set terminal pdf enhanced
set output 'CIVISElecData.pdf'

set xdata time
set timefmt "%m/%d/%Y"
set format x "%b"
set format y "%.1t*10^{%S}";
set xlabel "Months"
set ylabel "KWh"
set yrange [0: 4500000 ]

set key top right

plot "Elec_data.txt" using 1:2  notitle with points linestyle 1, \
     "" using 1:2 notitle smooth csplines with lines linestyle 1, \
     1 / 0 title "Hydro" with linespoints linestyle 1, \
	\
	"Elec_data.txt" using 1:3  notitle with points linestyle 2, \
     "" using 1:3 notitle smooth csplines with lines linestyle 2, \
     1 / 0 title "PV" with linespoints linestyle 2, \
	\
	 "Elec_data.txt" using 1:4  notitle with points linestyle 3, \
     "" using 1:4 notitle smooth csplines with lines linestyle 3, \
     1 / 0 title "Biogas" with linespoints linestyle 3, \
	 \
	 "Elec_data.txt" using 1:5  notitle with points linestyle 4, \
     "" using 1:5 notitle smooth csplines with lines linestyle 4, \
     1 / 0 title "Demand" with linespoints linestyle 4, \
	 \
	 "Elec_data.txt" using 1:6  notitle with points linestyle 5, \
     "" using 1:6 notitle smooth csplines with lines linestyle 5, \
     1 / 0 title "Total Production" with linespoints linestyle 5
	 	 

unset key
unset yrange
unset ylabel
unset xlabel	 
unset format
unset xdata
unset timefmt
unset output
unset terminal
