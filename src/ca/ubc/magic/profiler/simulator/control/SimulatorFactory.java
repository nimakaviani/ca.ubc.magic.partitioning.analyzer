/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.simulator.control;

/**
 *
 * @author nima
 */
public class SimulatorFactory {
    
    public enum SimulatorType{
        
        STATIC_TIME_SIMULATOR ("Static Time Simulator (No Trace Replay)"),
        TIME_SIMULATOR("Time Simulator"),
        NONE("Select a Simulator ...");
        
         private String text;

         SimulatorType(String text) {
            this.text = text;
         }

         public String getText() {
            return this.text;
         }

          public static SimulatorType fromString(String text) {
            if (text != null) {
                for (SimulatorType b : SimulatorType.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                      return b;
                    }
                }
            }
            return null;
         }
    }       
    
    public static ISimulator getSimulator(SimulatorType type){
        switch(type){
            case TIME_SIMULATOR:
                return new TimeSimulator();
            case STATIC_TIME_SIMULATOR:
                return new StaticTimeSimulator();
            default:
                throw new RuntimeException("Simulator Type is not supported");
        }
    }
}
