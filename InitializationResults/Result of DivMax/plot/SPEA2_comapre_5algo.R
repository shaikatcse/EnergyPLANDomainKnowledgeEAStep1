#this scipt is used to generate a boxplot with four indicator (hypervolume, IGD, Epsilon, spead)

postscript("Compare_5Confs_SPEA2.eps", horizontal=FALSE, onefile=FALSE, height=6, width=8.5, pointsize=12)
#pdf("combined_comparison.pdf", onefile=FALSE, width=8.5, height = 6)
#jpeg(filename = "combined_comparison.jpeg", width = 8.5, height = 6, units = "in", pointsize = 10, res = 1000)


SPEA2resultDirectory<-"../"

SPEA2qIndicator <- function(indicator)
{
	fileSPEA2<-paste(SPEA2resultDirectory, "SPEA2", sep="/")
	fileSPEA2<-paste(fileSPEA2, indicator, sep="/")
	SPEA2_results<-scan(fileSPEA2)

	fileSPEA2si<-paste(SPEA2resultDirectory, "SPEA2_WithoutRM_SI", sep="/")
	fileSPEA2si<-paste(fileSPEA2si, indicator, sep="/")
	SPEA2si_results<-scan(fileSPEA2si)

	#3rd algorithm (DivMa : conf 1)
	fileSPEA2si_DivMax_conf1<-paste(SPEA2resultDirectory, "SPEA2_WithoutRM_DivMax_Conf1", sep="/")
	fileSPEA2si_DivMax_conf1<-paste(fileSPEA2si_DivMax_conf1, indicator, sep="/")
	SPEA2si_DivMax_conf1_results<-scan(fileSPEA2si_DivMax_conf1)

	#4th algorithm (DivMax : conf 2)
	fileSPEA2si_DivMax_conf2<-paste(SPEA2resultDirectory, "SPEA2_WithoutRM_DivMax_Conf2", sep="/")
	fileSPEA2si_DivMax_conf2<-paste(fileSPEA2si_DivMax_conf2, indicator, sep="/")
	SPEA2si_DivMax_conf2_results<-scan(fileSPEA2si_DivMax_conf2)

	#5th algorithm (DivMax : conf 3)
	fileSPEA2si_DivMax_conf3<-paste(SPEA2resultDirectory, "SPEA2_WithoutRM_DivMax_Conf3", sep="/")
	fileSPEA2si_DivMax_conf3<-paste(fileSPEA2si_DivMax_conf3, indicator, sep="/")
	SPEA2si_DivMax_conf3_results<-scan(fileSPEA2si_DivMax_conf3)

	
	algs<-c("RI","SI", "DC1", "DC2", "DC3")
	boxplot(SPEA2_results,SPEA2si_results,SPEA2si_DivMax_conf1_results, SPEA2si_DivMax_conf2_results, SPEA2si_DivMax_conf3_results, names=algs, notch = FALSE)
	titulo <-paste(indicator, "SPEA2", sep=":")
	title(main=titulo)
}





par(mfrow=c(2,2), cex.lab=2.5, cex.axis=1.5)
indicator<-"HV"
SPEA2qIndicator(indicator)
indicator<-"IGD"
SPEA2qIndicator(indicator)
indicator<-"Epsilon"
SPEA2qIndicator(indicator)
indicator<-"Spread"
SPEA2qIndicator(indicator)


dev.off()
