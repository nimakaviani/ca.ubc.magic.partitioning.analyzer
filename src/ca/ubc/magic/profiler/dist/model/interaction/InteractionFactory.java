/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.interaction;

/**
 *
 * @author nima
 */
public class InteractionFactory {
    
    public enum InteractionCostType{
        IGNORE("None"),
        AVG_TRANSMISSION_TIME("Average Transmission Time"),
        LOW_LATENCY_AVG_TRANSMISSION_TIME("One Latency Avg. Transmission Time"),
        SIMPLE_MONETARY ("Simple Monetary Cost"),
        CLOUD_MONETARY ("Cloud Monetary Cost");
        
         private String text;

         InteractionCostType(String text) {
            this.text = text;
         }

         public String getText() {
            return this.text;
         }

          public static InteractionCostType fromString(String text) {
            if (text != null) {
                for (InteractionCostType b : InteractionCostType.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                      return b;
                    }
                }
            }
            return null;
         }
    }       
    
    public static IInteractionCostModel getInteractionCostModel(InteractionCostType type){
        switch(type){
            case IGNORE:
                return new IgnoreInteractionCostModel();
            case AVG_TRANSMISSION_TIME:
                return new AvgTransmissionTimeCostModel();
            case LOW_LATENCY_AVG_TRANSMISSION_TIME:
                return new OneLatencyAvgTransmissionTimeCostModel();
            case SIMPLE_MONETARY:
                return new SimpleMonetaryCostModel();
            case CLOUD_MONETARY:
                return new CloudMonetaryCostModel();
            default:
                throw new RuntimeException("No proper match for the type of InteractionCostModel");
        }
    }
}
