/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.control;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author nima
 */
public class Constants {
    
    public static final String NULL_STRING = "null";
    
    /**
     * Constants for the default profiler module path and the default host path
     */
    public static final String DEFAULT_PROFILE_XML_PATH = "resources/dist-model/Profile-20120504-190723.xml";
    public static final String DEFAULT_HOST_XML_PATH = "resources/dist-model/host-magic.xml";
    public static final String DEFAULT_CONSTRAINT_XML_PATH = "resources/dist-model/moduleconstraints-aries3.xml";
    
    /**
     * These are the set of constants used for METIS
     */
    public static final String METIS_PARTITIONING_CODE = "011";    
    public static final String HMETIS_PARTITIONING_CODE = "1";
    
    public static final String METIS_INPUT = "metisIn.txt";
    public static final String METIS_FIXED = "fixedIn.txt";
    public static final String METIS_OUTPUT_PART = ".part.";
    
    public static final String METIS_HOME = "resources/hmetis/";
    public static final String METIS_OUT_DIR = "out";
    public static final String METIS_EXECUTABLE = "./shmetis";
    
    public static final int    METIS_PARTITIONS_NUM = 2;
    public static final int    METIS_PARTITIONS_UBFACTOR = 49;
    public static final String METIS_DONT_CARE_PARTITION = "-1";
    
    
    public static final double INFINITE_WEIGHT = 1.0E8;
    
    public static final String NOT_APPLICABLE = "NA";
    
    /**
     * Factors for cost conversion when doing variable deployments
     */
    public static final double LINEAR_COST_CONVERSION_FACTOR = 8640.0;
    
     /**
     * 
     * The value is used to normalize data transfer time, and execution time
     * for the execution units in the dependency graph.
     * 
     * 1.0   corresponds to 1 second
     * 1.0E3 corresponds to 1 millisecond
     * 1.0E6 corresponds to 1 microsecond
     * 1.0E9 corresponds to 1 nanosecond
     */
    public static final double SEC_NORMALIZATION_FACTOR = 1.0;
    public static final double MILLISEC_NORMALIZATION_FACTOR = 1.0E3;
    public static final double MICROSEC_NORMALIZATION_FACTOR = 1.0E6;
    
    /**
     * The following is the default partition id for modules added to the
     * module model of a given application when there is still no partitioning
     * in place.
     */
    public static final int     INVALID_PARTITION_ID = -1;
    public static final char    DONT_CARE_CHAR = 'X';
    
    /**
     * The handshake parameter for the constant time in millisecond that it takes for every 
     * connection to establish.
     */
    // TODO nimak - the current handshake cost applies to every data exchange.
    //              it can be modified so that it only affects the first connection.
    public static final double DEFAULT_HANDSHAKE_COST = 1;

    /**
     * The cost for marshaling and unmarshaling data (i.e, lifting and lowering)
     */
    public static final double DEFAULT_LIFTING_LOWERING_COST = 0;
    
    /**
     * The root node name to be used by ThreadBasedBundleModuleCoarsener to determine
     * which module should be the start module when creating the dependency tree for
     * nodes in the dependency graph of the thread separated modules.
     */
//    public static final String ROOT_NODE_NAME_RUBIS = "org.ops4j.pax.web.service";
//    
//    public static final String ROOT_NODE_NAME_ARIESTRADER_L1 = "org.ops4j.pax.web.pax-web-jetty-bundle";
//    public static final String ROOT_NODE_NAME_ARIESTRADER_L2 = "org.apache.aries.sample.ariestrader.web";
//   
//    public static final List<String> ROOT_NODE_LIST = new ArrayList<String>(Arrays.asList(new String[] {
//        ROOT_NODE_NAME_ARIESTRADER_L1
//    }));
    
    /**
     * The set of module names for which the coarsening process needs to be ignored.
     * This set is used in ThreadBasedBundleModuleCoarsener in order to initialize
     * a set of nodes that do not need to contribute to the dependency graph of the
     * application if there is no necessity for their existence.
     */
//    public static final HashSet<String> THREAD_COARSENER_IGNORE_SET_RUBIS = new HashSet<String>(Arrays.asList(new String[] {
//        
//            "org.ops4j.pax.web.service",
//            "com.notehive.osgi.hibernate-samples.hibernate-classes",
//            "org.ops4j.pax.logging.pax-logging-api",
//            "org.ops4j.pax.logging.pax-logging-service"
//        
//    }));
//    
//     public static final HashSet<String> THREAD_COARSENER_IGNORE_SET_ARIESTRADER = new HashSet<String>(Arrays.asList(new String[] {
//        
//            "org.ops4j.pax.web.pax-web-jetty-bundle",
//            "org.apache.aries.blueprint;blueprint.graceperiod:=false",
//            "org.ops4j.pax.logging.pax-logging-api",
//            "org.ops4j.pax.logging.pax-logging-service",
//            "com.mysql.jdbc",
//            "javax.servlet-api",
//            "org.ops4j.pax.web.pax-web-jsp",
//            "org.ops4j.pax.web.pax-web-extender-war"            
//    }));
//     
//     public static final HashSet<String> THREAD_COARSENER_IGNORE_SET = THREAD_COARSENER_IGNORE_SET_ARIESTRADER;
    
     /**
      * The set of module names for which there should be only one instance in the dependency graph.
      * These are the non-replicable set of modules which drive the overall deployment decisions for
      * the application based on the dependency model extracted by the application.
      */
//     public static final HashSet<String> NON_REPLICABLE_SET_RUBIS = new HashSet<String>(
//            Arrays.asList(new String[] {
//                "com.notehive.osgi.rubis.hibernate-osgi-rubis-item-session",
//                "com.notehive.osgi.rubis.hibernate-osgi-rubis-bid-session",
//                "com.notehive.osgi.rubis.hibernate-osgi-rubis-buy-session",
//                "com.notehive.osgi.rubis.hibernate-osgi-rubis-user-session",
//                "com.notehive.osgi.rubis.hibernate-osgi-rubis-comment-session"
//                
//            }));
//     
//     public static final HashSet<String> NON_REPLICABLE_SET = NON_REPLICABLE_SET_RUBIS;
    
    /**
     * The following is the list of all nodes considered as web nodes to be collapsed together
     * under one node while dealing with application execution costs. Web nodes should not be separated
     * and their separation should not be considered in the model.
     */
    public static final Set<String> WEB_NODES = new HashSet<String>(Arrays.asList(
            new String[] {
                "javax.servlet-api",
                "org.ops4j.pax.web.pax-web-jsp",
                "org.ops4j.pax.web.pax-web-jetty-bundle"
            }));
    
    public static final String SYNTHETIC_NODE = "Synthetic_Entry-Node";
}
