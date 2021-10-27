#This is a R file that generate a boxplot for six different problem for NSGA-II.
#There are 6 rows and three columns. Each row presents a problem and 1st column present stop generation, 2nd column presents HVd and third column presents epsd.
#Someone needs to select appropriate path to work with.


postscript("ALL_NSGAII_DTLZ_Pres.eps", horizontal=TRUE, onefile=TRUE, height=5, width=5, pointsize=8)
#pdf("All_NSGAII.pdf", width=7, height=11,pointsize=10) 
#resultDirectory<-"C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/NSGAIISC"
#path to NSGA-II result directory
resultDirectory<-"C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/NSGAIISC"

#default number of generations for each problem
returnStdStGen <- function(problem)
{
if(problem =="ZDT1")
 return (200)
 else if(problem =="ZDT2")
 return (200)
 else if(problem =="ZDT3")
 return (200)
 else if(problem =="ZDT4")
 return (200)
 else if(problem =="DTLZ2")
 return (300)
 else if(problem =="DTLZ5")
 return (200)
}

funStPlot <- function(problem)
{
#stGenHVdNew is the file that contains stopping generations for 30 runs when AHD+Div is used
StgenHVD<-paste(resultDirectory, problem, sep="/")
StgenHVD<-paste(StgenHVD, "StGenHVDNew", sep="/")
StgenHVD<-scan(StgenHVD)

#stGenAHDNew is the file that contains stopping generations for 30 runs when only AHD is used
StgenAHD<-paste(resultDirectory, problem, sep="/")
StgenAHD<-paste(StgenAHD, "StGenAHDNew", sep="/")
StgenAHD<-scan(StgenAHD)

#stGenDVNew is the file that contains stopping generations for 30 runs when only Div is used
StgenDV<-paste(resultDirectory, problem, sep="/")
StgenDV<-paste(StgenDV, "StGenDVNew", sep="/")
StgenDV<-scan(StgenDV)

#stopOCD is the file that contains stopping generations for 30 runs when OCD is used
StgenOCD<-paste(resultDirectory, problem, sep="/")
StgenOCD<-paste(StgenOCD, "StopOCD", sep="/")
StgenOCD<-scan(StgenOCD)

ind<-c("AHD+Div", "AHD", "Div", "OCD" )
line<-returnStdStGen(problem)


if(problem =="DTLZ2"){
	#ylin provided the ylimit of boxplt
	#for "DTLX2 it is necessary because ylimit is too low to get a horizontal line at default number of generation
	boxplot(StgenHVD,StgenAHD,StgenDV,StgenOCD ,names=ind,  notch = FALSE, outline=FALSE, ylim=c(0,310))
}
else{

	boxplot(StgenHVD,StgenAHD,StgenDV,StgenOCD ,names=ind,  notch = FALSE, outline=FALSE)
}
#boxplot(StgenHVD,StgenAHD,StgenDV,names=ind, notch = FALSE, outline=FALSE, ylim=c(0,line)
abline(h=line)
titulo <-paste(problem, "StopGen", sep=":")
title(font.main = 1, main=titulo)
}

funHVDPlot <- function(problem)
{
#HVDNew is the file that contains HVd for 30 runs when AHD+Div is used
HVD<-paste(resultDirectory, problem, sep="/")
HVD<-paste(HVD, "HVDNew", sep="/")
HVD<-scan(HVD)

#HVDAHDNew is the file that contains HVd for 30 runs when AHD is used
HVDAHD<-paste(resultDirectory, problem, sep="/")
HVDAHD<-paste(HVDAHD, "HVDAHDNew", sep="/")
HVDAHD<-scan(HVDAHD)

#HVDDVNew is the file that contains HVd for 30 runs when Div is used
HVDDV<-paste(resultDirectory, problem, sep="/")
HVDDV<-paste(HVDDV, "HVDDVNew", sep="/")
HVDDV<-scan(HVDDV)

#HVDOCD is the file that contains HVd for 30 runs when OCD is used
HVDOCD<-paste(resultDirectory, problem, sep="/")
HVDOCD<-paste(HVDOCD, "HVDOCD", sep="/")
HVDOCD<-scan(HVDOCD)

#ind<-c(expression('HVD'[all]), expression('HVD'[AHD]), expression('HVD'[DV]) )
ind<-c("AHD+Div", "AHD", "Div", "OCD" )
boxplot(HVD,HVDAHD,HVDDV,HVDOCD, names=ind, notch = FALSE, outline=FALSE)
abline(h=0.0)
#titulo<-paste(problem, expression('HV'[d]), sep=":")
title( main=substitute(problem*':HV'[d], list(problem = problem)))
}

funEpsDPlot <- function(problem)
{
#EpsDNew is the file that contains epsd for 30 runs when AHD+Div is used
EpsD<-paste(resultDirectory, problem, sep="/")
EpsD<-paste(EpsD, "EpsDNew", sep="/")
EpsD<-scan(EpsD)

#EpsDAHDNew is the file that contains epsd for 30 runs when AHD is used
EpsAHD<-paste(resultDirectory, problem, sep="/")
EpsAHD<-paste(EpsAHD, "EpsDAHDNew", sep="/")
EpsAHD<-scan(EpsAHD)

#EpsDDVNew is the file that contains epsd for 30 runs when Div is used
EpsDDV<-paste(resultDirectory, problem, sep="/")
EpsDDV<-paste(EpsDDV, "EpsDDVNew", sep="/")
EpsDDV<-scan(EpsDDV)

#EpsDOCD is the file that contains epsd for 30 runs when OCD is used
EpsDOCD<-paste(resultDirectory, problem, sep="/")
EpsDOCD<-paste(EpsDOCD, "EpsDOCD", sep="/")
EpsDOCD<-scan(EpsDOCD)

#ind<-c(expression('EpsD'[all]), expression('EpsD'[AHD]), expression('EpsD'[DV]) )
ind<-c("AHD+Div", "AHD", "Div", "OCD" )
boxplot(EpsD,EpsAHD,EpsDDV,EpsDOCD, names=ind, notch = FALSE, outline=FALSE)
abline(h=0.0)
#titulo <-paste(problem,expression('HV'[D]), sep=":")
title( main=substitute(problem*':eps'[d], list(problem = problem)))
}


par(mfrow = c(3,2), mar=c(2, 2, 2, 2) + 0.1)
#par(mfrow=c(6,3))
prob1<-"ZDT1"
#funStPlot("ZDT1")
#funStPlot("ZDT2")
#funStPlot("ZDT3")
#funStPlot("ZDT4")
funStPlot("DTLZ2")
funStPlot("DTLZ5")

#funHVDPlot("ZDT1")
#funHVDPlot("ZDT2")
#funHVDPlot("ZDT3")
#funHVDPlot("ZDT4")
funHVDPlot("DTLZ2")
funHVDPlot("DTLZ5")

#funEpsDPlot("ZDT1")
#funEpsDPlot("ZDT2")
#funEpsDPlot("ZDT3")
#funEpsDPlot("ZDT4")
funEpsDPlot("DTLZ2")
funEpsDPlot("DTLZ5")


#funHVDPlot(prob1)
#funEpsDPlot(prob1)

#prob1<-"ZDT2"
#funStPlot(prob1)
#funHVDPlot(prob1)
#funEpsDPlot(prob1)

#prob1<-"ZDT3"
#funStPlot(prob1)
#funHVDPlot(prob1)
#funEpsDPlot(prob1)

#prob1<-"ZDT4"
#funStPlot(prob1)
#funHVDPlot(prob1)
#funEpsDPlot(prob1)

#prob1<-"DTLZ2"
#funStPlot(prob1)
#funHVDPlot(prob1)
#funEpsDPlot(prob1)

#prob1<-"DTLZ5"
#funStPlot(prob1)
#funHVDPlot(prob1)
#funEpsDPlot(prob1)


dev.off()