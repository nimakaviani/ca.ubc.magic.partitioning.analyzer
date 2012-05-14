/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.granularity;

/**
 *
 * @author nima
 */
public enum CodeUnitType {
        
    METHOD("Method"),
    CLASS ("Class"),
    COMPONENT("Component"),
    DEFAULT("Default");

     private String text;

     CodeUnitType(String text) {
        this.text = text;
     }

     public String getText() {
        return this.text;
     }

      public static CodeUnitType fromString(String text) {
        if (text != null) {
            for (CodeUnitType b : CodeUnitType.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                  return b;
                }
            }
        }
        return null;
     }
}
