package reet.fbk.eu.jmetal.stoppingCriteria;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

public class KSTest {

	public KSTest() {
		// TODO Auto-generated constructor stub
	}

	static double[] readFile() throws IOException{
		BufferedReader br= new BufferedReader(new FileReader("test.txt"));
		String str;
		double [] arr=new double[300];
		int i=0;
		while( (str=br.readLine()) != null){
			arr[i++]=Double.parseDouble(str);
		}
		return arr;
		
	}
	static void performTest(double [] array){
	
		KolmogorovSmirnovTest kst =  new KolmogorovSmirnovTest();
	for(int i=35;i<300;i++){
		double []x = Arrays.copyOfRange(array, (i-35), i-5);
		double []y = Arrays.copyOfRange(array, i-5, i);
		//System.out.println(Arrays.toString(x));
		//System.out.println(Arrays.toString(y));
		double pValue=kst.kolmogorovSmirnovTest(x, y);
	
		System.out.println(i+" "+pValue);
	}
}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		double [] arr =readFile();
		performTest(arr);

	}

}
