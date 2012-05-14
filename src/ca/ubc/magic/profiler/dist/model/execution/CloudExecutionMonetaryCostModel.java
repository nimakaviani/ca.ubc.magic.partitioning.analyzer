/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.execution;

import ca.ubc.magic.profiler.dist.model.Host;

/**
 *
 * @author nima
 */
public class CloudExecutionMonetaryCostModel implements IExecutionCostModel {

    public double getExecutionCost(Host targetHost, Host baseHost, double usage) {
        return ExecutionRate.getExecutionCost(targetHost, baseHost, usage);
    }
    
}
