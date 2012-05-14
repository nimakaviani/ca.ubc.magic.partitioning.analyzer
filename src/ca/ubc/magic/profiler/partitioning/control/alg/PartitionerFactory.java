/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.control.alg;

import ca.ubc.magic.profiler.partitioning.control.alg.metis.MetisPartitioner;
import ca.ubc.magic.profiler.partitioning.control.alg.preflowpush.PreflowPushPartitioner;
import ca.ubc.magic.profiler.partitioning.control.alg.simplex.LpSolvePartitionerExtended4Cost;
import ca.ubc.magic.profiler.partitioning.control.alg.simplex.SimplexPartitioner;
import ca.ubc.magic.profiler.partitioning.control.alg.simplex.SimplexPartitionerExtended;
import ca.ubc.magic.profiler.partitioning.control.alg.simplex.SimplexPartitionerExtended4Cost;

/**
 *
 * @author nima
 */
public class PartitionerFactory {
    
    public enum PartitionerType{
        MIN_MAX_PREFLOW_PUSH("MinMax (Preflow Push)"),
        MULTI_WAY_METIS ("Multi-way (hMetis)"),
        ILP("BIP (Simplex)"),
        EXTENDED_ILP("Extended BIP (Simplex)"),
        CLOUD_EXTENDED_ILP("Cloud BIP (Simplex)"),
        CLOUD_EXTENDED_LPSOVE("Cloud lp_solve (Simplex)");
        
         private String text;

         PartitionerType(String text) {
            this.text = text;
         }

         public String getText() {
            return this.text;
         }

          public static PartitionerType fromString(String text) {
            if (text != null) {
                for (PartitionerType b : PartitionerType.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                      return b;
                    }
                }
            }
            return null;
         }
    }       
    
    public static IPartitioner getPartitioner(PartitionerType type){
        switch (type){
            case ILP:
                return new SimplexPartitioner();
            case EXTENDED_ILP:
                return new SimplexPartitionerExtended();
            case CLOUD_EXTENDED_ILP:
                return new SimplexPartitionerExtended4Cost();
            case  CLOUD_EXTENDED_LPSOVE:
            	return new LpSolvePartitionerExtended4Cost();
            case MULTI_WAY_METIS:
                return new MetisPartitioner();
            case MIN_MAX_PREFLOW_PUSH:
                return new PreflowPushPartitioner();
            default:
             throw new RuntimeException("No proper partitioner found");
        }
    }    
}
