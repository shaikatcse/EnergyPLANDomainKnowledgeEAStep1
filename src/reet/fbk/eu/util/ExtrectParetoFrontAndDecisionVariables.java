package reet.fbk.eu.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class ExtrectParetoFrontAndDecisionVariables {

	String FUNFileName_;
	String VARFileName_;

	int dimensions_;
	int numberOfDecisionVariables_;

	List<Point> points_ = new LinkedList<Point>();

	private class Point {
		double[] vector_;
		double[] decisionVector_;

		public Point(double[] vector, double[] decisionVector) {
			vector_ = vector;
			decisionVector_ = decisionVector;
		}

		/*
		 * public Point (int size) { vector_ = new double[size]; for (int i = 0;
		 * i < size; i++) vector_[i] = 0.0f; }
		 */

	}

	/**
	 * @author Juan J. Durillo Creates a new instance
	 * @param name
	 *            : the name of the file
	 */
	public ExtrectParetoFrontAndDecisionVariables(String FUNFileName,
			String VARFileName, int dimensions, int numberOfDecisionVariables) {
		FUNFileName_ = FUNFileName;
		VARFileName_ = VARFileName;
		dimensions_ = dimensions;
		numberOfDecisionVariables_ = numberOfDecisionVariables;
		loadInstance();

	} // ReadInstance

	/**
	 * Read the points instance from file
	 */
	public void loadInstance() {

		try {
			// read the objetives values
			File archivo = new File(FUNFileName_);
			FileReader fr = null;
			BufferedReader br = null;
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);

			// read the corresponding decision variable
			File dcFile = new File(VARFileName_);
			FileReader dcfr = null;
			BufferedReader dcbr = null;
			dcfr = new FileReader(dcFile);
			dcbr = new BufferedReader(dcfr);

			// File reading
			String line;
			int lineCnt = 0;
			line = br.readLine(); // reading the first line (special case)

			String lineDecicionVarible = dcbr.readLine();

			while (line != null) {
				double objectiveValues[] = new double[dimensions_];
				double decisionVariables[] = new double[numberOfDecisionVariables_];

				StringTokenizer stobj = new StringTokenizer(line);
				StringTokenizer stdec = new StringTokenizer(lineDecicionVarible);
				try {
					for (int i = 0; i < dimensions_; i++) {
						objectiveValues[i] = new Double(stobj.nextToken());
					}

					for (int i = 0; i < numberOfDecisionVariables_; i++) {
						decisionVariables[i] = new Double(stdec.nextToken());
					}

					Point auxPoint = new Point(objectiveValues,
							decisionVariables);
					add(auxPoint);

					line = br.readLine();
					lineDecicionVarible = dcbr.readLine();
					lineCnt++;
				} catch (NumberFormatException e) {
					System.err.println("Number in a wrong format in line "
							+ lineCnt);
					System.err.println(line);
					line = br.readLine();
					lineCnt++;
				} catch (NoSuchElementException e2) {
					System.err.println("Line " + lineCnt
							+ " does not have the right number of objectives");
					System.err.println(line);
					line = br.readLine();
					lineCnt++;
				}
			}
			br.close();
			dcbr.close();
		} catch (FileNotFoundException e3) {
			System.err.println("The file " + FUNFileName_ + " or " + VARFileName_  
					+ " has not been found in your file system");
		} catch (IOException e3) {
			System.err.println("The file " + FUNFileName_
					+ " has not been found in your file system");
		}

	} // loadInstance

	public void add(Point point) {
		Iterator<Point> iterator = points_.iterator();

		while (iterator.hasNext()) {
			Point auxPoint = iterator.next();
			int flag = compare(point, auxPoint);

			if (flag == -1) { // A solution in the list is dominated by the new
								// one
				iterator.remove();

			} else if (flag == 1) { // The solution is dominated
				return;
			}
		} // while
		points_.add(point);

	} // add

	public int compare(Point one, Point two) {
		int flag1 = 0, flag2 = 0;
		for (int i = 0; i < dimensions_; i++) {
			if (one.vector_[i] < two.vector_[i])
				flag1 = 1;

			if (one.vector_[i] > two.vector_[i])
				flag2 = 1;
		}

		if (flag1 > flag2) // one dominates
			return -1;

		if (flag2 > flag1) // two dominates
			return 1;

		return 0; // both are non dominated
	}

	public void writeParetoFront() {
		try {
			/* Open the objective output file */
			FileOutputStream fosObj = new FileOutputStream(FUNFileName_ + ".pf");
			OutputStreamWriter oswObj = new OutputStreamWriter(fosObj);
			BufferedWriter bwObj = new BufferedWriter(oswObj);

			/* Open the decision variables output file */
			FileOutputStream fosDC = new FileOutputStream(VARFileName_ + ".pf");
			OutputStreamWriter oswDC = new OutputStreamWriter(fosDC);
			BufferedWriter bwDC = new BufferedWriter(oswDC);

			for (Point auxPoint : points_) {
				String auxObj = "";
				String auxDC = "";

				for (int i = 0; i < auxPoint.vector_.length; i++) {
					auxObj += auxPoint.vector_[i] + " ";

				}
				bwObj.write(auxObj);
				bwObj.newLine();

				for (int i = 0; i < auxPoint.decisionVector_.length; i++) {
					auxDC += auxPoint.decisionVector_[i] + " ";
				}
				bwDC.write(auxDC);
				bwDC.newLine();
			}

			/* Close the file */
			bwObj.close();
			bwDC.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length != 4) {
			System.out.println("Wrong number of arguments: ");
			System.out
					.println("Sintex: java ExtractParetoFront <FUNfile> <VARfile> <dimensions> <numberOfDecicionVariables>");
			System.out
					.println("\t<FUNfile> is a file containing all objective values");
			System.out
					.println("\t<VARfile> is a file containing all corresponding decision variable values");
			System.out
					.println("\t<dimensions> represents the number of dimensions of the problem");
			System.out
					.println("\t<numberOfDecicionVariables> represents the number of decision varibales of the problem");
			System.exit(-1);
		}

		ExtrectParetoFrontAndDecisionVariables epf = new ExtrectParetoFrontAndDecisionVariables(
				args[0], args[1], new Integer(args[2]), new Integer(args[3]));

		epf.writeParetoFront();
	}
}
