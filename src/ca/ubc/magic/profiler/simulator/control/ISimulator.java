/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.simulator.control;

import ca.ubc.magic.profiler.simulator.framework.SimulationUnit;

/**
 *
 * @author nima
 */
public interface ISimulator {
    public void simulate(SimulationUnit unit) throws Exception;
    public void addListener(ISimulatorListener l);
    public void removeListener(ISimulatorListener l);
}
