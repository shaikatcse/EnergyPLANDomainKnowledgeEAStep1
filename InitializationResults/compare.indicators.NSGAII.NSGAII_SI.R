#postscript("indicators_NSGAII_SPEA2.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
pdf("indicators_NSGAII_NSGAIIsi.pdf", onefile=FALSE, width=10)

NSGAIIresultDirectory<-"."

NSGAIIqIndicator <- function(indicator)
{
	fileNSGAII<-paste(NSGAIIresultDirectory, "NSGAIIWithoutTrack", sep="/")
#filePolynomialMutation<-paste(filePolynomialMutation, problem, sep="/")
	fileNSGAII<-paste(fileNSGAII, indicator, sep="/")
	NSGAII_results<-scan(fileNSGAII)

fileNSGAIIsi<-paste(NSGAIIresultDirectory, "NSGAIIWithoutTrackWithSI", sep="/")
#fileDKMutation<-paste(fileDKMutation, problem, sep="/")
fileNSGAIIsi<-paste(fileNSGAIIsi, indicator, sep="/")
NSGAIIsi_results<-scan(fileNSGAIIsi)

algs<-c("NSGAII","NSGAIISI")
boxplot(NSGAII_results,NSGAIIsi_results,names=algs, notch = FALSE)
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


