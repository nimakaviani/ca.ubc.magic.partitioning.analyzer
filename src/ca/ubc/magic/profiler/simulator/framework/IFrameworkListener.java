/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.simulator.framework;

import ca.ubc.magic.profiler.dist.model.report.ReportModel;

/**
 *
 * @author nima
 */
public interface IFrameworkListener {
    
    public void simulationAdded(SimulationUnit unit);
    
    public void simulationRemoved(SimulationUnit unit);
    
    public void updateSimulationReport(SimulationUnit unit, ReportModel report);
    
    public void updateBestSimReport(SimulationUnit unit);
}
