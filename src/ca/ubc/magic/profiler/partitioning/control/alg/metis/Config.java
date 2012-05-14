package ca.ubc.magic.profiler.partitioning.control.alg.metis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * Helper class to read data from a Java properties file.
 * 
 */
public class Config {

	private static Properties props;
	public static enum AttributeType {REAL, NOMINAL}
	
	public static void loadProps(InputStream is) throws IOException {
		props = new Properties();
		props.load(is);
	}
	
	public static String getProperty(String name) {
		return props.getProperty(name);
	}
	
	public static AttributeType getColumnType(String columnName) {
		String typeName = Config.getProperty(columnName + ".type");
		if(typeName == null) {
			return AttributeType.REAL;
		} else {
			return AttributeType.valueOf(typeName.toUpperCase());
		}
	}
}
