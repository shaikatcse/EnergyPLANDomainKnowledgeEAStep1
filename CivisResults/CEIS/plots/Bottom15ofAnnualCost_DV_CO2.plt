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
	14 '#008080' \
	)

#set multiplot layout 2,1 rowsfirst
	
set yrange [0:9000]
set xrange [0:8]
	
labels="BAC1 BAC2 BAC3 BAC4 BAC5 BAC6 BAC7 BAC8 BAC9 BAC10 BAC11 BAC12 BAC13 BAC14 BAC15"
set samples words(labels) 
key_x = 7.2
key_y = 3000
key_dy = 400
	
set key bottom textcolor rgb "#000000" 

YMIN=0
YMAX=9000
set xlabel "CO2 emission in t/year"
set ylabel "Capacity in KW"
plot "Bottom15AnnualCost" using 26:2 title "HP" pt 5, "Bottom15AnnualCost" using 26:1 title "CHP" pt 7, \
"Bottom15AnnualCost" using 26:3 title "PV" pt 9, \
"Bottom15AnnualCost" using 26:4 title "Boiler" pt 12, \
"Bottom15AnnualCost" u 26:(YMAX):30 w impulses palette notitle, \
'+' using (key_x):(key_y + $0*key_dy):(word(labels, int($0+1))):0 \
    with labels left offset 1,-0.1 point pt 7 palette t ''


#plot "Bottom15AnnualCost" using 26:2 title "HP" pt 5,  "Bottom15AnnualCost" using 26:1 title "CHP" pt 7, \
"Bottom15AnnualCost" using 26:3 title "PV" pt 9, \
"Bottom15AnnualCost" using 26:4 title "Boiler" pt 12, \
"Bottom15AnnualCost" u 26:(YMAX):30 w impulses palette notitle, \
'+' using (key_x):(key_y + $0*key_dy):(word(labels, int($0+1))):0 \
    with labels left offset 1,-0.1 point pt 7 palette t ''
	
unset xrange 
unset yrange 
unset xlabel
unset ylabel


#:26 w labels font ", 6" rotate by 90  point offset character 0, character 1 tc rgb "blue"