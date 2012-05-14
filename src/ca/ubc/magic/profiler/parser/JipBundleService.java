package ca.ubc.magic.profiler.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class JipBundleService {
	
	private List<String> classList;
	private String id;
	private String in;
	private String out;
	private String total;
	
	public static final String SERVICE_ID = "ID";
	public static final String SERVICE_CLASS = "Class";
	public static final String SERVICE_IN_DATA = "Incoming Data";
	public static final String SERVICE_OUT_DATA = "Outgiong Data";
	public static final String SERVICE_TOTAL_DATA = "Total Data";
	
	public JipBundleService (String _classList, String _id,
			String _in, String _out, String _total){
		
		classList = new ArrayList<String>();
		
		StringTokenizer tokenizer = new StringTokenizer(_classList, ",");
		while(tokenizer.hasMoreElements())
			classList.add(tokenizer.nextToken().trim());
		id = _id;
		in = _in;
		out = _out;
		total = _total;
	}
	
	public String getID(){
		return id;
	}
	
	public String getInData(){
		return in;
	}
	
	public String getoutData(){
		return out;
	}
	
	public String getTotalData(){
		return total;
	}
	
	public boolean isInClassList(String className){
		if (classList.contains(className))
			return true;
		return false;
	}
	
	public List<String> getClassList(){
		return classList;
	}
	
	public String getClassListString(){
		StringBuffer buffer = new StringBuffer();
		for (String className : classList)
			buffer.append(className +", ");
		return buffer.toString();
	}
}
