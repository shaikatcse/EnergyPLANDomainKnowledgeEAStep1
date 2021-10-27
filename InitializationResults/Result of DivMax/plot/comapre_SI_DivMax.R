#this scipt is used to generate a boxplot with four indicator (hypervolume, IGD, Epsilon, spead)

postscript("Compare_SI_DivMax.eps", horizontal=FALSE, onefile=FALSE, height=6, width=8.5, pointsize=12)
#pdf("combined_comparison.pdf", onefile=FALSE, width=8.5, height = 6)
#jpeg(filename = "combined_comparison.jpeg", width = 8.5, height = 6, units = "in", pointsize = 10, res = 1000)


NSGAIIresultDirectory<-"../"

NSGAIIqIndicator <- function(indicator)
{
	#fileNSGAII<-paste(NSGAIIresultDirectory, "NSGAII", sep="/")
	#fileNSGAII<-paste(fileNSGAII, indicator, sep="/")
	#NSGAII_results<-scan(fileNSGAII)

	fileNSGAIIsi<-paste(NSGAIIresultDirectory, "NSGAII_WithoutRM_SI", sep="/")
	fileNSGAIIsi<-paste(fileNSGAIIsi, indicator, sep="/")
	NSGAIIsi_results<-scan(fileNSGAIIsi)

	#3rd algorithm (DivMa : conf 1)
	fileNSGAIIsi_DivMax_conf1<-paste(NSGAIIresultDirectory, "NSGAII_WithoutRM_DivMax_Conf1", sep="/")
	fileNSGAIIsi_DivMax_conf1<-paste(fileNSGAIIsi_DivMax_conf1, indicator, sep="/")
	NSGAIIsi_DivMax_conf1_results<-scan(fileNSGAIIsi_DivMax_conf1)

	#4th algorithm (DivMax : conf 2)
	#fileNSGAIIsi_DivMax_conf2<-paste(NSGAIIresultDirectory, "NSGAII_WithoutRM_DivMax_Conf2", sep="/")
	#fileNSGAIIsi_DivMax_conf2<-paste(fileNSGAIIsi_DivMax_conf2, indicator, sep="/")
	#NSGAIIsi_DivMax_conf2_results<-scan(fileNSGAIIsi_DivMax_conf2)

	#5th algorithm (DivMax : conf 3)
	#fileNSGAIIsi_DivMax_conf3<-paste(NSGAIIresultDirectory, "NSGAII_WithoutRM_DivMax_Conf3", sep="/")
	#fileNSGAIIsi_DivMax_conf3<-paste(fileNSGAIIsi_DivMax_conf3, indicator, sep="/")
	#NSGAIIsi_DivMax_conf3_results<-scan(fileNSGAIIsi_DivMax_conf3)

	
	algs<-c("SI", "DM")
	boxplot(NSGAIIsi_results,NSGAIIsi_DivMax_conf1_results, names=algs, notch = FALSE)
	titulo <-paste(indicator, "NSGAII", sep=":")
	title(main=titulo)
}


SPEA2resultDirectory<-"../"

SPEA2qIndicator <- function(indicator)
{
	fileSPEA2si<-paste(SPEA2resultDirectory, "SPEA2_WithoutRM_SI", sep="/")
	fileSPEA2si<-paste(fileSPEA2si, indicator, sep="/")
	SPEA2si_results<-scan(fileSPEA2si)

	#3rd algorithm (DivMa : conf 1)
	fileSPEA2si_DivMax_conf1<-paste(SPEA2resultDirectory, "SPEA2_WithoutRM_DivMax_Conf1", sep="/")
	fileSPEA2si_DivMax_conf1<-paste(fileSPEA2si_DivMax_conf1, indicator, sep="/")
	SPEA2si_DivMax_conf1_results<-scan(fileSPEA2si_DivMax_conf1)

algs<-c("SI", "DM")
	boxplot(SPEA2si_results,SPEA2si_DivMax_conf1_results, names=algs, notch = FALSE)
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
