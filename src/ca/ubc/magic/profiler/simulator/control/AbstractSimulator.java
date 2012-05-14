/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.simulator.control;

import ca.ubc.magic.profiler.dist.model.report.ReportModel;
import java.util.Set;

/**
 *
 * @author nima
 */
public abstract class AbstractSimulator implements ISimulator {
    
    ReportModel report;
    Set<ISimulatorListener> listeners;
    Thread listenerUpdateThread;
    
    public void addListener(ISimulatorListener l) {
        listeners.add(l);
    }

    public void removeListener(ISimulatorListener l) {
        listeners.remove(l);
    }
    
    protected class ListenerUpdate implements Runnable {

        public void run() {
            try{
                while(true){
                    for (ISimulatorListener l : listeners){
                        l.valueChanged(report);                        
                    }
                    Thread.sleep(100);
                }
            }catch(Exception e){                
                System.out.println("Simulation report interrupted");
            }
        }        
    }
    
}
