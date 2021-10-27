#this scipt is used to generate a boxplot with four indicator (hypervolume, IGD, Epsilon, spead)

postscript("indicators_NSGAII_SPEA2_test.eps", horizontal=FALSE, onefile=FALSE, height=6, width=8.5, pointsize=12)
#pdf("combined_comparison.pdf", onefile=FALSE, width=8.5, height = 6)
#jpeg(filename = "combined_comparison.jpeg", width = 8.5, height = 6, units = "in", pointsize = 10, res = 1000)


NSGAIIresultDirectory<-"."

NSGAIIqIndicator <- function(indicator)
{
	fileNSGAII<-paste(NSGAIIresultDirectory, "NSGAIIWithoutTrack", sep="/")
	#fileNSGAII<-paste(fileNSGAII,"With_mutation_pr_0.2", sep="/")
#filePolynomialMutation<-paste(filePolynomialMutation, problem, sep="/")
	fileNSGAII<-paste(fileNSGAII, indicator, sep="/")
	NSGAII_results<-scan(fileNSGAII)

	fileNSGAIIsi<-paste(NSGAIIresultDirectory, "NSGAIIWithoutTrackWithSI", sep="/")
	fileNSGAIIsi<-paste(fileNSGAIIsi, "WithoutRM", sep="/")
	#fileNSGAIIsi<-paste(fileNSGAIIsi, "With_mutation_pr_0.2", sep="/")

	#fileDKMutation<-paste(fileDKMutation, problem, sep="/")
	fileNSGAIIsi<-paste(fileNSGAIIsi, indicator, sep="/")
	NSGAIIsi_results<-scan(fileNSGAIIsi)

	#3rd algorithm
	fileNSGAIIsi_DivMax<-paste(NSGAIIresultDirectory, "NSGAIIWithoutTrackWithSI", sep="/")
	fileNSGAIIsi_DivMax<-paste(fileNSGAIIsi_DivMax, "WithoutRM", sep="/")
	fileNSGAIIsi_DivMax<-paste(fileNSGAIIsi_DivMax, "DivMax", sep="/")
	fileNSGAIIsi_DivMax<-paste(fileNSGAIIsi_DivMax, "Configuration 2", sep="/")

	#fileDKMutation<-paste(fileDKMutation, problem, sep="/")
	fileNSGAIIsi_DivMax<-paste(fileNSGAIIsi_DivMax, indicator, sep="/")
	NSGAIIsi_DivMax_results<-scan(fileNSGAIIsi_DivMax)

	
	algs<-c("RI","SI", "SIDivMax")
	boxplot(NSGAII_results,NSGAIIsi_results,NSGAIIsi_DivMax_results, names=algs, notch = FALSE)
	titulo <-paste(indicator, "NSGAII", sep=":")
	title(main=titulo)
}


SPEA2resultDirectory<-"."

SPEA2qIndicator <- function(indicator)
{
	fileSPEA2<-paste(SPEA2resultDirectory, "SPEA2WithoutTrack", sep="/")
	#fileSPEA2<-paste(fileSPEA2, "With_mutation_pr_0.2", sep="/")
#filePolynomialMutation<-paste(filePolynomialMutation, problem, sep="/")
	fileSPEA2<-paste(fileSPEA2, indicator, sep="/")
	SPEA2_results<-scan(fileSPEA2)

fileSPEA2si<-paste(SPEA2resultDirectory, "SPEA2WithoutTrackWithSI", sep="/")
fileSPEA2si<-paste(fileSPEA2si, "WithoutRM", sep="/")
#fileSPEA2si<-paste(fileSPEA2si, "With_mutation_pr_0.2", sep="/")
#fileDKMutation<-paste(fileDKMutation, problem, sep="/")
fileSPEA2si<-paste(fileSPEA2si, indicator, sep="/")
SPEA2si_results<-scan(fileSPEA2si)

#3rd algo (DivMax)
fileSPEA2si_DivMax<-paste(SPEA2resultDirectory, "SPEA2WithoutTrackWithSI", sep="/")
fileSPEA2si_DivMax<-paste(fileSPEA2si_DivMax, "WithoutRM", sep="/")
fileSPEA2si_DivMax<-paste(fileSPEA2si_DivMax, "DivMax", sep="/")
fileSPEA2si_DivMax<-paste(fileSPEA2si_DivMax, "Configuration 2", sep="/")
#fileDKMutation<-paste(fileDKMutation, problem, sep="/")
fileSPEA2si_DivMax<-paste(fileSPEA2si_DivMax, indicator, sep="/")
SPEA2si_DivMax_results<-scan(fileSPEA2si_DivMax)

algs<-c("RI","SI", "SIDivMax")
boxplot(SPEA2_results,SPEA2si_results,SPEA2si_DivMax_results, names=algs, notch = FALSE)
titulo <-paste(indicator, "SPEA2", sep=":")
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

par(mfrow=c(2,4), cex.lab=2.5, cex.axis=1.5)
indicator<-"HV"
NSGAIIqIndicator(indicator)
indicator<-"IGD"
NSGAIIqIndicator(indicator)
indicator<-"Epsilon"
NSGAIIqIndicator(indicator)
indicator<-"Spread"
NSGAIIqIndicator(indicator)

indicator<-"HV"
SPEA2qIndicator(indicator)
indicator<-"IGD"
SPEA2qIndicator(indicator)
indicator<-"Epsilon"
SPEA2qIndicator(indicator)
indicator<-"Spread"
SPEA2qIndicator(indicator)

#indicator<-"HV"
#SPEA2qIndicator(indicator, "OptimizeElecEnergy_SPEA2")
#indicator<-"IGD"
#SPEA2qIndicator(indicator, "OptimizeElecEnergy_SPEA2")
#indicator<-"GD"
#SPEA2qIndicator(indicator, "OptimizeElecEnergy_SPEA2")
#indicator<-"Epsilon"
#SPEA2qIndicator(indicator, "OptimizeElecEnergy_SPEA2")

dev.off()
