/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.granularity;

import ca.ubc.magic.profiler.dist.control.Constants;

/**
 *
 * @author nima
 */
public enum ConstraintType {
        
    NULL(Constants.NULL_STRING),
    ROOT("root"),
    EXPOSE("expose"),
    IGNORE("ignore"),
    REPLICABLE("replicable"),
    NON_REPLICABLE("non-replicable");

    private String text;

    ConstraintType(String text) {
       this.text = text;
    }

    public String getText() {
       return this.text;
    }

    public static ConstraintType fromString(String text) {
       if (text != null) {
            for (ConstraintType b : ConstraintType.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                  return b;
                }
            }
        }
        return null;
    }
}
