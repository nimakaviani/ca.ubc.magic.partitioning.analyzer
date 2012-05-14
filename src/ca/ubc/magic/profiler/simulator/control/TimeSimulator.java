/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.simulator.control;

import ca.ubc.magic.profiler.dist.model.DistributionModel;
import ca.ubc.magic.profiler.dist.model.execution.ExecutionRate;
import ca.ubc.magic.profiler.dist.model.Host;
import ca.ubc.magic.profiler.dist.model.HostPair;
import ca.ubc.magic.profiler.dist.model.interaction.InteractionData;
import ca.ubc.magic.profiler.dist.transform.IModuleCoarsener;
import ca.ubc.magic.profiler.dist.model.report.ReportModel;
import ca.ubc.magic.profiler.parser.JipFrame;
import ca.ubc.magic.profiler.parser.JipRun;
import ca.ubc.magic.profiler.simulator.framework.SimulationUnit;
import java.util.HashSet;

/**
 *
 * @author nima
 */
public class TimeSimulator extends AbstractSimulator implements Runnable {
    
    JipRun mRun;
    DistributionModel mDistModel;
    IModuleCoarsener  mCoarsener;
    
    
    public TimeSimulator(){        
        listeners = new HashSet<ISimulatorListener>();        
    }
    
    public void init(JipRun run, IModuleCoarsener coarsener){
        mRun = run;
        mCoarsener = coarsener;        
    }
    
    public void run() throws RuntimeException {
        if (mDistModel == null)
            throw new RuntimeException("The simulation unit or the distrbution model"
                    + "are not properly set");
        
        double pastExecCost = 0;
        listenerUpdateThread = new Thread(new ListenerUpdate()); 
        listenerUpdateThread.start();
        try{
            for(Long threadId: mRun.threads()) {
                for (JipFrame f: mRun.interactions(threadId)) {                
                    simulate(f);                    
                }                
                pastExecCost = report.getCostModel().getExecutionCost();
            }            
            report.setFinalized(Boolean.TRUE);    
            listenerUpdateThread.join(2000);
        }catch(Exception e){          
          e.printStackTrace();
        }finally{
            for (ISimulatorListener l : listeners)
                l.unitSimulationOver(report);                                
        }
    }

    public void simulate(SimulationUnit unit) throws RuntimeException {
        mDistModel = unit.getDistModel();
        report = new ReportModel(mDistModel.getHostModel().getHostMap().values());
    }   
    
    private void simulate(JipFrame f) throws RuntimeException {        
        report.getUnitModel().setFrom(mCoarsener.getFrameModuleName(f));        
        calculateExecutionTime(f);
        for (JipFrame child: f.getChildren()) {                        
            report.getUnitModel().setTo(mCoarsener.getFrameModuleName(child));
            calculateCommunicationTime(f, child);
            simulate (child);
        }        
    }
    
    private void calculateExecutionTime(JipFrame f) throws RuntimeException {
        String moduleName = mCoarsener.getFrameModuleName(f);
        int    hostId = mDistModel.getModuleMap().get(moduleName).getPartitionId();
        Host   host = mDistModel.getHostModel().getHostMap().get(hostId);       
        if (host.getDefault())
            report.getCostModel().addExecutionCost(f.getNetTime());
        else{
            report.getCostModel().addExecutionCost(
                    ExecutionRate.getExecutionTime(
                        host, mDistModel.getHostModel().getDefaultHost(), f.getNetTime()));
        }
    }
    
    private void calculateCommunicationTime(JipFrame p, JipFrame c) throws RuntimeException {
        Host sourceHost = mDistModel.getHostModel().getHostMap().get(mDistModel.getModuleMap().get(
                mCoarsener.getFrameModuleName(p)).getPartitionId());
        Host targetHost = mDistModel.getHostModel().getHostMap().get(mDistModel.getModuleMap().get(
                mCoarsener.getFrameModuleName(c)).getPartitionId());
        if (sourceHost.getId() == targetHost.getId())
            return;
        HostPair s2t = new HostPair(sourceHost, targetHost);        
        report.getCostModel().addCommunicationCost(
                s2t.getInteractionCost(mDistModel.getHostModel(), 
                    new InteractionData(c.getDataFromParent(), 
                            c.getDataToParent(), 
                            c.getCountFromParent(),
                            c.getCountToParent())).getAvgCost());
    }
    
    
}
