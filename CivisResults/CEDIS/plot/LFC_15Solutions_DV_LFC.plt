unset xrange 
unset yrange 
unset xlabel
unset ylabel
unset colorbox
set palette defined (\
	0  '#000000', \
	1  '#C0C0C0', \
    2  '#0055bb', \
    3  '#808080', \
    4  '#800000', \
	5  '#FF0000', \
	6  '#800080', \
	7  '#FF00FF', \
	8  '#008000', \
	9  '#00FF00', \
	10 '#bf8bff', \
	11 '#FFFF00', \
	12 '#a200ff', \
	13 '#0000FF', \
	14 '#008080', \
	15 '#F0DD74' \
	)

#set multiplot layout 2,1 rowsfirst
	
set yrange [0:10000]
set xrange [0.44:0.50]
	
labels="CUR LB1 LB2 LB3 LB4 LB5 LB6 LB7 LB8 LB9 LB10 LB11 LB12 LB13 LB14 LB15"

set samples words(labels) 
key_x = 0.495
key_y = 3700
key_dy = 400
	
set key bottom textcolor rgb "#000000" 

YMIN=0
YMAX=10000
set xlabel "LFC"
set ylabel "Capacity in KW"
plot \
"LFC_15Solutions" using 24:2 title "GSHP" pt 5,\
"LFC_15Solutions" using 24:1 title "CHP" pt 7, \
"LFC_15Solutions" using 24:3 title "PV" pt 9, \
"LFC_15Solutions" using 24:4 title "Boiler" pt 12, \
"LFC_15Solutions" u 24:(YMAX):30 w impulses palette notitle, \
'+' using (key_x):(key_y + $0*key_dy):(word(labels, int($0+1))):0 \
    with labels left offset 1,-0.1 point pt 7 palette t ''

set arrow from 0.4485, 0 to 0.4485, 10000 nohead lc rgb 'black'

#plot "Bottom15AnnualCost" using 26:2 title "HP" ,  "Bottom15AnnualCost" using 26:1 title "CHP" , \
"Bottom15AnnualCost" using 26:3 title "PV" , \
"Bottom15AnnualCost" using 26:4 title "Boiler" , \
"Bottom15AnnualCost" u 26:(YMAX):30 w impulses palette notitle, \
'+' using (key_x):(key_y + $0*key_dy):(word(labels, int($0+1))):0 \
    with labels left offset 1,-0.1 point pt 7 palette t ''
	
unset xrange 
unset yrange 
unset xlabel
unset ylabel


#:26 w labels font ", 6" rotate by 90  point offset character 0, character 1 tc rgb "blue" 