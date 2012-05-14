/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.transform;

import ca.ubc.magic.profiler.dist.model.granularity.EntityConstraintModel;

/**
 *
 * @author nima
 */
public class ModuleCoarsenerFactory {
 
    public enum ModuleCoarsenerType{
        BUNDLE("Bundle Coarsener"),
        THREAD_BUNDLE ("Thread Bundle Coarsener"),
        REQUEST_BUNDLE("Request Bundle Coarsener"),
        EXTENDED_REQUEST_BUNDLE("Extended Request Bundle Coarsener"),
        COARSE_REQUEST_BUNDLE("Coarse Request Bundle Coarsener"),
        LOW_LATENCY_EXTENDED_REQUEST_BUNDLE("Low Latency Extended Request Bundle Coarsener"),
        LOW_LATENCY_COARSE_REQUEST_BUNDLE("Low Latency Coarse Request Bundle Coarsener");
        
         private String text;

         ModuleCoarsenerType(String text) {
            this.text = text;
         }

         public String getText() {
            return this.text;
         }

          public static ModuleCoarsenerType fromString(String text) {
            if (text != null) {
                for (ModuleCoarsenerType b : ModuleCoarsenerType.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                      return b;
                    }
                }
            }
            return null;
         }
    }
    
    public static IModuleCoarsener getModuleCoarsener(
            ModuleCoarsenerType type, EntityConstraintModel constraintModel){
        switch (type){
            case BUNDLE:
                return new BundleModuleCoarsener(constraintModel);
            case THREAD_BUNDLE:
                return new ThreadBasedBundleModuleCoarsener(constraintModel);
            case REQUEST_BUNDLE:
                return new RequestBasedBundleModuleCoarsener(constraintModel);
            case EXTENDED_REQUEST_BUNDLE:
                return new ExtendedRequestBasedBundleModuleCoarsener(constraintModel);
            case COARSE_REQUEST_BUNDLE:
                return new CoarseRequestBasedBundleModuleCoarsener(constraintModel);
            case LOW_LATENCY_EXTENDED_REQUEST_BUNDLE:
                return new LowLatencyExtendedRequestBasedBundleModuleCoarsener(constraintModel);
            case LOW_LATENCY_COARSE_REQUEST_BUNDLE:
                return new LowLatencyCoarseRequestBasedBundleModuleCoarsener(constraintModel);
            default:
                throw new RuntimeException("No proper partitioner found");
        }
    }    
    
}
