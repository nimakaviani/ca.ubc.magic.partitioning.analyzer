/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.control.filters;

import ca.ubc.magic.profiler.dist.control.Constants;
import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.model.ModuleModel;
import ca.ubc.magic.profiler.dist.transform.IInteractionFilter;
import ca.ubc.magic.profiler.dist.transform.IModuleFilter;

/**
 *
 * @author nima
 */
public class FilterHelper {
    public static final int H2_INFEASIBLE_HOST = 2;
    public static final int H1_INFEASIBLE_HOST = 1;
    
    public static final String INFEASIBLE_HOST  = "Infeasible Host Filter";
    public static final String INFEASIBLE_HOST_THREAD  = "Infeasible Host Filter Threaded";
    public static final String BIND_SYNTHETIC_NODE  = "Bind Synthetic Node";
    public static final String INFEASIBLE_SPLIT = "Infeasible Split Filter";
    public static final String INFEASIBLE_SPLIT_THREAD = "Infeasible Split Filter Threaded";
    public static final String INFEASIBLE_SYNTHETIC = "Infeasible Synthetic Node";
    
    public static IModuleFilter setModuleFilter(ModuleModel moduleModel, HostModel hostModel){
        String[] moduleNames = {
                // The following filters are for filtering Rubis
                "com.notehive.osgi.rubis.hibernate-osgi-rubis-user-session",
                "com.notehive.osgi.rubis.hibernate-osgi-rubis-buy-session",                
                "com.notehive.osgi.rubis.hibernate-osgi-rubis-bid-session",
                
                // The followings are for filtering sample modules
                "d",
                
                // The followings are for filtering ariestrader
                "org.apache.aries.samples.ariestrader.beans:AccountProfileDataBeanImpl",
                "org.apache.aries.samples.ariestrader.beans:HoldingDataBeanImpl"
        };
        return new InfeasibleHostFilter(moduleModel, hostModel, 
                FilterHelper.INFEASIBLE_HOST, moduleNames, H2_INFEASIBLE_HOST);
    }
    
    public static IModuleFilter setModuleFilterThread(ModuleModel moduleModel, HostModel hostModel){
        String[] moduleNames = {
                // The following filters are for filtering Rubis
                "com.notehive.osgi.rubis.hibernate-osgi-rubis-user-session",
                "com.notehive.osgi.rubis.hibernate-osgi-rubis-buy-session",                
                "com.notehive.osgi.rubis.hibernate-osgi-rubis-bid-session",
                
                // The following are for filtering ariestrader
                "org.apache.aries.samples.ariestrader.beans:AccountProfileDataBeanImpl",
                "org.apache.aries.samples.ariestrader.beans:QuoteDataBeanImpl",
                "org.apache.aries.samples.ariestrader.beans:HoldingDataBeanImpl"
        };
        return new InfeasibleHostFilterRegEx(moduleModel, hostModel, 
                FilterHelper.INFEASIBLE_HOST_THREAD, moduleNames, H2_INFEASIBLE_HOST);
    }
    
    public static IModuleFilter setSyntheticNodeModuleFitler(ModuleModel moduleModel, HostModel hostModel){
        
        String[] moduleNames = {
            Constants.SYNTHETIC_NODE
        };
        
        return new InfeasibleHostFilterRegEx(moduleModel, hostModel, 
                FilterHelper.INFEASIBLE_HOST_THREAD, moduleNames, H1_INFEASIBLE_HOST);
    }
    
    
    public static IInteractionFilter setInteractionFilter(ModuleModel moduleModel){
        String[][] modulePairNames = { {"com.notehive.osgi.hibernate-samples.hibernate-classes",
            "com.notehive.osgi.rubis.hibernate-osgi-rubis-comment-session"}
        };
        
        return new InfeasibleEdgeCutFilter(moduleModel, INFEASIBLE_SPLIT, modulePairNames);
    }
    
    public static IInteractionFilter setInterctionFilterThread(ModuleModel moduleModel){
        String[][] modulePairNames = {
            {"org.apache.aries.samples.ariestrader.beans",
            "org.apache.aries.samples.ariestrader.persist.jdbc"}
        };
        
        return new InfeasibleEdgeCutFilterRegEx(moduleModel, INFEASIBLE_SPLIT_THREAD, modulePairNames);
    }
}
