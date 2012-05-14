package ca.ubc.magic.profiler.partitioning.control.alg.metis;

import ca.ubc.magic.profiler.dist.control.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MetisUtil {
    
        private static String mSolution = null;
	
	private static void executeProcess(String command, String inFile, String workingDir) throws InterruptedException, IOException {
		Process proc = Runtime.getRuntime().exec(command + " " + inFile, null,
                        new File(workingDir));
		final InputStream input = proc.getInputStream();
		final InputStream error = proc.getErrorStream();
		Thread tIn = new StreamCleaner(input);                
		Thread tErr = new StreamCleaner(error);
                tIn.start();
                tErr.start();
		proc.waitFor();
                tIn.join();
                tErr.join();
	}
        
        public static void fixedFile(String workingDir, String fixedFileName, String content) throws Exception {
            String name = new File(workingDir + fixedFileName).getAbsolutePath();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(name)));
            pw.println(content);
            pw.close();
        }
	
	public static void metis(String metis, String workingDir, UndirectedGraph graph, 
                int parts, int ubFactor) throws Exception {
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(workingDir + Constants.METIS_INPUT)));
		graph.tohMetis(pw);
		pw.close();
		
                String absPath = new File(workingDir).getAbsolutePath();
		executeProcess(metis, Constants.METIS_INPUT + " " + Constants.METIS_FIXED + " " + parts + " " + ubFactor, absPath);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(absPath + 
                        System.getProperty("file.separator") +
                        Constants.METIS_INPUT + Constants.METIS_OUTPUT_PART + parts)));
		graph.readPartitions(br);
		br.close();
	}
	

	private static class StreamCleaner extends Thread {
                BufferedReader bin;
		
		public StreamCleaner(InputStream input) {
			InputStreamReader in = new InputStreamReader(input);
                        bin = new BufferedReader(in);
                        
		}

		@Override
		public void run() {
			try {
                            String line;
                            while( (line = bin.readLine()) != null){
                                System.out.println(line);
                                if (line.contains("Hyperedge Cut:") &&
                                        line.contains("(minimize)"))
                                    mSolution = line.replaceAll("\t", " ").replaceAll("               ", " ");
                            }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
        
        public static String getSolution(){
            return mSolution;
        }
}
