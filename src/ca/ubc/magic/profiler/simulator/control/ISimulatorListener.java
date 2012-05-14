/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.simulator.control;

import ca.ubc.magic.profiler.dist.model.report.ReportModel;

/**
 *
 * @author nima
 */
public interface ISimulatorListener {
    public void valueChanged(ReportModel model);
    
    public void unitSimulationOver(ReportModel model);
}
