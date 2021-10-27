unset xrange 
unset yrange 
unset xlabel
unset ylabel
unset colorbox

set palette defined (\
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

set multiplot layout 2,1 rowsfirst
	
#set yrange [0:9000]
set yrange [10420:10510]
set ytics 10	
#labels="scr1 scr2 scr3 scr4 scr5 scr6 scr7 scr8 scr9 scr10 scr11 scr12 scr13 scr14 scr15"
#set samples words(labels) 
#key_x = 10473
#key_y = 3000
#key_dy = 400
	
set key top textcolor rgb "#000000" 

YMIN=0
YMAX=9000
set ylabel "Annual Cost Keuro"
#set ylabel "Capacity"

set xtics nomirror rotate by -45
set boxwidth 0.1
set style fill solid

plot "LFC_15Solutions" every ::1 using 30:28:30:xtic(31) with boxes palette notitle

unset ytics
unset yrange
unset ylabel

set ylabel "CO2 emission in t/year"
set yrange [1:6.5]
set ytics 0.5
plot "LFC_15Solutions" every ::1 using 30:26:30:xtic(31) with boxes palette notitle


# ,  "Bottom15AnnualCost" using 24:1 title "CHP" , \
"Bottom15AnnualCost" using 24:3 title "PV" , \
"Bottom15AnnualCost" using 24:4 title "Boiler" , \
"Bottom15AnnualCost" u 24:(YMAX):30 w impulses palette notitle, \
'+' using (key_x):(key_y + $0*key_dy):(word(labels, int($0+1))):0 \
    with labels left offset 1,-0.1 point pt 7 palette t ''


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