#postscript("indicators_NSGAII_SPEA2.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
pdf("indicators_SPEA2_SPEA2si.pdf", onefile=FALSE, width=10)

SPEA2resultDirectory<-"."

NSGAIIqIndicator <- function(indicator)
{
	fileSPEA2<-paste(SPEA2resultDirectory, "SPEA2WithoutTrack", sep="/")
#filePolynomialMutation<-paste(filePolynomialMutation, problem, sep="/")
	fileSPEA2<-paste(fileSPEA2, indicator, sep="/")
	SPEA2_results<-scan(fileSPEA2)

fileSPEA2si<-paste(SPEA2resultDirectory, "SPEA2WithoutTrackWithSI", sep="/")
#fileDKMutation<-paste(fileDKMutation, problem, sep="/")
fileSPEA2si<-paste(fileSPEA2si, indicator, sep="/")
SPEA2si_results<-scan(fileSPEA2si)

algs<-c("SPEA2","SPEA_SI")
boxplot(SPEA2_results,SPEA2si_results,names=algs, notch = FALSE)
titulo <-paste(indicator)
title(main=titulo)
}

#SPEA2resultDirectory<-"./MutationStudySPEA2/data/"

#SPEA2qIndicator <- function(indicator, problem)
#{
#filePolynomialMutation<-paste(SPEA2resultDirectory, "PolynomialMutation", sep="/")
#filePolynomialMutation<-paste(filePolynomialMutation, problem, sep="/")
#filePolynomialMutation<-paste(filePolynomialMutation, indicator, sep="/")
#PolynomialMutation<-scan(filePolynomialMutation)

#fileDKMutation<-paste(SPEA2resultDirectory, "DKMutation", sep="/")
#fileDKMutation<-paste(fileDKMutation, problem, sep="/")
#fileDKMutation<-paste(fileDKMutation, indicator, sep="/")
#DKMutation<-scan(fileDKMutation)

#algs<-c("Polynomial","DKMutation")
#boxplot(PolynomialMutation,DKMutation,names=algs, notch = FALSE)
#titulo <-paste(indicator, "SPEA2", sep=":")
#title(main=titulo)
#}

par(mfrow=c(2,3))
indicator<-"HV"
NSGAIIqIndicator(indicator)
indicator<-"IGD"
NSGAIIqIndicator(indicator)
indicator<-"GD"
NSGAIIqIndicator(indicator)
indicator<-"Epsilon"
NSGAIIqIndicator(indicator)
indicator<-"Spread"
NSGAIIqIndicator(indicator)

#indicator<-"HV"
#SPEA2qIndicator(indicator, "OptimizeElecEnergy_SPEA2")
#indicator<-"IGD"
#SPEA2qIndicator(indicator, "OptimizeElecEnergy_SPEA2")
#indicator<-"GD"
#SPEA2qIndicator(indicator, "OptimizeElecEnergy_SPEA2")
#indicator<-"Epsilon"
#SPEA2qIndicator(indicator, "OptimizeElecEnergy_SPEA2")


