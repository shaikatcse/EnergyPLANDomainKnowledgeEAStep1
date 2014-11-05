In the simulation we want to replace all the individual boilers using district heating. District heating contains CHP, HP and boilers. There are alos eletroliser for district heating where excess electricity from PV can be converted to H2 and use the H2 as CHP fuel.
In the first stage, boiler capacity is very high (17500 KJ/s, KW). In the second stage, the boiler capacity is fixed by using maximul use of boiler.

Decision variables:
0-> chp2 1 -> hp2 2 -> pv capacity 3 -> electrolyser 2 4 -> hydrogenstorage 2
Objectives:
0 -> CO2 emission 1 -> annual cost 2 -> load following capccity

Constraints:
Biomass consumption<=56.78 GWh