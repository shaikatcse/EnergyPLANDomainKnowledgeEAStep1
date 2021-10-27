unset xrange 
unset yrange 
unset xlabel
unset ylabel
unset colorbox
unset label

set terminal pdf enhanced
set output 'BestAnnualCostResultsWithDV.pdf'
	
set palette defined (\
	0  '#990000', \
    1  '#0055bb', \
    2  '#FF0000', \
	3  '#800080', \
	4  '#FF00FF', \
	5  '#008000', \
	6  '#00FF00', \
	7 '#a200ff', \
	8 '#0000FF', \
	9 '#008080', \
	10 '#000000' \
	)
	
	
	
#set multiplot layout 2,1 rowsfirst
	
set yrange [0:20000]
set xrange [13400:15600]
	
labels="S1 S2 S3 S4 S5 S6 S7 S8 S9 S10 RS"
set samples words(labels) 
key_x = 15400
key_y = 3000
key_dy = 1500
	
#set key bottom textcolor rgb "#000000" 
set key below textcolor rgb "#000000" 

set label "Reference Scenario (emis: 13.09kt)" at 15220, 500 rotate left
set label "S1 (emission: 9.16kt)" at 13511, 4000 rotate left
set label "S2 (emission: 9.40kt)" at 13855, 4000 rotate left
set label "S10 (emission: 8.26kt)" at 14513, 4000 rotate left


YMIN=0
YMAX=20000
set xlabel "Annual cost in KEuro"
set ylabel "Capacity in KW"
plot "BestACScenarios" using 7:1 title "PV(e)" lc rgb 'black' pt 12 , \
"BestACScenarios" using 7:2 title "Wood mCHP(th)" lc rgb 'black' pt 2, \
"BestACScenarios" using 7:3 title "GSHP(th)" lc rgb 'black' pt 3, \
"BestACScenarios" using 7:4 title "Oil Boiler(th)" lc rgb 'black' pt 4, \
"BestACScenarios" using 7:5 title "LPG Boiler(th)" lc rgb 'black' pt 6, \
"BestACScenarios" using 7:6 title "Wood Boiler(th)" lc rgb 'black' pt 8, \
"BestACScenarios" u 7:(YMAX):8 w impulses palette notitle, \
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
unset output
unset terminal

#:26 w labels font ", 6" rotate by 90  point offset character 0, character 1 tc rgb "blue"