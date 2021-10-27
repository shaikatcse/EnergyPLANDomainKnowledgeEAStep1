package reet.fbk.eu.qualityIndicator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import jmetal.experiments.Experiment;
import jmetal.util.JMException;

public class BoxPlotScript {

	// public String experimentName_;
	public String[] algorithmNameList_; // List of the names of the algorithms
										// to be executed
	// public String[] problemList_; // List of problems to be solved
	public String[] indicatorList_; // List of the quality indicators to be
									// applied
	public String experimentBaseDirectory_; // Directory to store the results

	BoxPlotScript() {

		algorithmNameList_ = null;
		// problemList_ = null;
		indicatorList_ = null;

		experimentBaseDirectory_ = "";

	}

	public void generateScripts(int rows, int cols, String[] problems,
			String prefix, boolean notch) throws IOException {
		// STEP 1. Creating R output directory

		String rDirectory = "R";
		rDirectory = experimentBaseDirectory_ + "/" + rDirectory;
		System.out.println("R    : " + rDirectory);
		File rOutput;
		rOutput = new File(rDirectory);
		if (!rOutput.exists()) {
			new File(rDirectory).mkdirs();
			System.out.println("Creating " + rDirectory + " directory");
		}

		for (int indicator = 0; indicator < indicatorList_.length; indicator++) {
			System.out.println("Indicator: " + indicatorList_[indicator]);
			String rFile = rDirectory + "/" + prefix + "."
					+ indicatorList_[indicator] + ".Boxplot.R";

			FileWriter os = new FileWriter(rFile, false);
			os.write("postscript(\""
					+ prefix
					+ "."
					+ indicatorList_[indicator]
					+ ".Boxplot.eps\", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)"
					+ "\n");
			// os.write("resultDirectory<-\"../data/" + experimentName_ +"\"" +
			// "\n");
			os.write("resultDirectory<-\"../data/" + "\"" + "\n");
			os.write("qIndicator <- function(indicator, problem)" + "\n");
			os.write("{" + "\n");

			for (int i = 0; i < algorithmNameList_.length; i++) {
				os.write("file" + algorithmNameList_[i]
						+ "<-paste(resultDirectory, \"" + algorithmNameList_[i]
						+ "\", sep=\"/\")" + "\n");
				os.write("file" + algorithmNameList_[i] + "<-paste(file"
						+ algorithmNameList_[i] + ", " + "problem, sep=\"/\")"
						+ "\n");
				os.write("file" + algorithmNameList_[i] + "<-paste(file"
						+ algorithmNameList_[i] + ", "
						+ "indicator, sep=\"/\")" + "\n");
				os.write(algorithmNameList_[i] + "<-scan(" + "file"
						+ algorithmNameList_[i] + ")" + "\n");
				os.write("\n");
			} // for

			os.write("algs<-c(");
			for (int i = 0; i < algorithmNameList_.length - 1; i++) {
				os.write("\"" + algorithmNameList_[i] + "\",");
			} // for
			os.write("\"" + algorithmNameList_[algorithmNameList_.length - 1]
					+ "\")" + "\n");

			os.write("boxplot(");
			for (int i = 0; i < algorithmNameList_.length; i++) {
				os.write(algorithmNameList_[i] + ",");
			} // for
			if (notch) {
				os.write("names=algs, notch = TRUE)" + "\n");
			} else {
				os.write("names=algs, notch = FALSE)" + "\n");
			}
			os.write("titulo <-paste(indicator, problem, sep=\":\")" + "\n");
			os.write("title(main=titulo)" + "\n");

			os.write("}" + "\n");

			os.write("par(mfrow=c(" + rows + "," + cols + "))" + "\n");

			os.write("indicator<-\"" + indicatorList_[indicator] + "\"" + "\n");

			for (String problem : problems) {
				os.write("qIndicator(indicator, \"" + problem + "\")" + "\n");
			}

			os.close();
		} // for
	} // generateRBoxplotScripts

	public static void main(String args[]) throws JMException, IOException {
		// Configure the R scripts to be generated
		int rows;
		int columns;
		String prefix;
		String[] problems;

		rows = 2;
		columns = 3;
		prefix = new String("NSGAII");
		problems = new String[] { "OptimizeElecEnergy_NSGAII" };

		BoxPlotScript bps = new BoxPlotScript();

		bps.algorithmNameList_ = new String[] {  "PolynomialMutation", "DKMutation" };
		bps.indicatorList_ = new String[] {"HV", "Spread", "IGD", "Epsilon", "GD"} ;

		int numberOfAlgorithms = bps.algorithmNameList_.length;

		bps.experimentBaseDirectory_ = "C:\\Users\\mahbub\\Documents\\GitHub\\EnergyPLANDomainKnowledgeEAStep1\\Results\\"
				+"MutationStudyNSGAII";

		boolean notch;
		bps.generateScripts(rows, columns, problems, prefix, notch = false);
	}

}
