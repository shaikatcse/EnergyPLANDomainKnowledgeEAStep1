#this scipt is used to generate a boxplot with four indicator (hypervolume, IGD, Epsilon, spead)

#postscript("indicators_NSGAII_SPEA2.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
#pdf("combined_comparison.pdf", onefile=FALSE, width=8.5, height = 6)
jpeg(filename = "comNSGAII_SIandSPEA2_SI.jpeg", width = 8.5, height = 6.0, units = "in", pointsize = 10, res = 1000)


NSGAIIresultDirectory<-"."
SPEA2resultDirectory<-"."

NSGAIIqIndicator <- function(indicator)
{
fileNSGAIIsi<-paste(NSGAIIresultDirectory, "NSGAIIWithoutTrackWithSI", sep="/")
fileNSGAIIsi<-paste(fileNSGAIIsi, indicator, sep="/")
NSGAIIsi_results<-scan(fileNSGAIIsi)

fileSPEA2si<-paste(SPEA2resultDirectory, "SPEA2WithoutTrackWithSI", sep="/")
fileSPEA2si<-paste(fileSPEA2si, indicator, sep="/")
SPEA2si_results<-scan(fileSPEA2si)

algs<-c("NSGAII_SI","SPEA2_SI")
boxplot(NSGAIIsi_results,SPEA2si_results,names=algs, notch = FALSE)
titulo <-paste(indicator)
title(main=titulo)
}


par(mfrow=c(2,4))
indicator<-"HV"
NSGAIIqIndicator(indicator)
indicator<-"IGD"
NSGAIIqIndicator(indicator)
indicator<-"Epsilon"
NSGAIIqIndicator(indicator)
indicator<-"Spread"
NSGAIIqIndicator(indicator)


dev.off()
