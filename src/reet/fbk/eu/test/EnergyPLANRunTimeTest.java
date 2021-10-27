package reet.fbk.eu.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EnergyPLANRunTimeTest {

	private static void copyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}
	
	public static void main(String[] args) throws IOException {
		
		/*for(int i=0;i<100;i++) {
			File source = new File("C:\\Users\\shaik\\Desktop\\EnergyPLAm15.1\\spool\\Aalborg2050_vision.txt");
			File dest = new File("C:\\Users\\shaik\\Desktop\\EnergyPLAm15.1\\spool\\Aalborg2050_vision"+i+".txt");
			dest.createNewFile();
			copyFileUsingStream(source,dest);
		}*/
		
		int numberOfRun= 100;
		String runSpoolEnergyPLAN = "C:\\Users\\shaik\\Desktop\\EnergyPLAm15.1\\energyplan.exe -spool "+numberOfRun+ " ";
		for(int i=0;i<numberOfRun;i++) {
			runSpoolEnergyPLAN = runSpoolEnergyPLAN + "Aalborg2050_vision"+i+".txt ";
		}
		runSpoolEnergyPLAN = runSpoolEnergyPLAN + "-ascii run";
		
		long initTime = System.currentTimeMillis();
		
		try{
			Process process = Runtime.getRuntime().exec(runSpoolEnergyPLAN);
			process.waitFor();
			process.destroy();
			
		} catch (IOException e) {
			System.out.println("Energyplan.exe has some problem");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Energyplan interrupted");
		}
		
		long estimatedTime = System.currentTimeMillis() - initTime;
		
		System.out.println("Energyplan spool: Total execution time: " + estimatedTime/1000.0 + "s");
		
		initTime = System.currentTimeMillis();
					
		for(int i = 0;i<numberOfRun; i++) {
		
			String runSequentialEnergyplan = "C:\\Users\\shaik\\Desktop\\EnergyPLAm15.1\\energyplan.exe -i "+ 
			"C:\\Users\\shaik\\Desktop\\EnergyPLAm15.1\\spool\\Aalborg2050_vision"+i+".txt -ascii result"+i+".txt";
			try{
				Process process = Runtime.getRuntime().exec(runSequentialEnergyplan);
				process.waitFor();
				process.destroy();
				
			} catch (IOException e) {
				System.out.println("Energyplan.exe has some problem");
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("Energyplan interrupted");
			}
			
		}
			estimatedTime = System.currentTimeMillis() - initTime;
			System.out.println("EnergyPLAN sequential: Total execution time: " + estimatedTime/1000.0 + "s");
					
		
			initTime = System.currentTimeMillis();
			
			for(int i = 0;i<numberOfRun; i++) {
			
				String runSequentialEnergyplan = "C:\\Users\\shaik\\eclipse-workspace\\EnergyPLANDomainKnowledgeEA\\EnergyPLAN_12.3_test\\energyplan.exe -i "+ 
				"C:\\Users\\shaik\\Desktop\\EnergyPLAm15.1\\spool\\Aalborg2050_vision"+i+".txt -ascii result"+i+".txt";
				try{
					Process process = Runtime.getRuntime().exec(runSequentialEnergyplan);
					process.waitFor();
					process.destroy();
					
				} catch (IOException e) {
					System.out.println("Energyplan.exe has some problem");
					e.printStackTrace();
				} catch (InterruptedException e) {
					System.out.println("Energyplan interrupted");
				}
				
			}
				estimatedTime = System.currentTimeMillis() - initTime;
				System.out.println("EnergyPLAN12.3 sequential: Total execution time: " + estimatedTime/1000.0 + "s");
			
		
		
		
				

	}

}
