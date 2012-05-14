/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.execution;

/**
 *
 * @author nima
 */
public class ExecutionFactory {
    
     public enum ExecutionCostType{
        
        EXECUTION_TIME("Average Execution Time"),
        CLOUD_MONETARY ("Cloud Monetary Execution Charge");
        
         private String text;

         ExecutionCostType(String text) {
            this.text = text;
         }

         public String getText() {
            return this.text;
         }

          public static ExecutionCostType fromString(String text) {
            if (text != null) {
                for (ExecutionCostType b : ExecutionCostType.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                      return b;
                    }
                }
            }
            return null;
         }
    }       
    
    public static IExecutionCostModel getInteractionCostModel(ExecutionCostType type){
        switch(type){
            case EXECUTION_TIME:
                return new ExecutionTimeCostModel();
            case CLOUD_MONETARY:
                return new CloudExecutionMonetaryCostModel();
            default:
                throw new RuntimeException("No proper match for the type of InteractionCostModel");
        }
    }
}
