/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.simulator.control;

import ca.ubc.magic.profiler.dist.model.ModuleModel;
import ca.ubc.magic.profiler.dist.model.report.ReportModel;
import ca.ubc.magic.profiler.simulator.framework.SimulationUnit;
import java.util.HashSet;

/**
 *
 * @author nima
 */
public class StaticTimeSimulator extends AbstractSimulator implements Runnable {
    
    SimulationUnit mUnit = null;
    ModuleModel mModuleModel;
    
     public StaticTimeSimulator(){        
        listeners = new HashSet<ISimulatorListener>();        
    }

    public void init(ModuleModel moduleModel) {
        mModuleModel = moduleModel;
    }

    public void simulate(SimulationUnit unit) throws RuntimeException {
        mUnit = unit;        
    }

    public void run() throws RuntimeException {
        
        if (mUnit == null)
            throw new RuntimeException("No Simulation unit is provided for simulation purposes");
        
        ReportModel localReport = new ReportModel(mUnit.getDistModel().getHostModel().getHostMap().values());
        
        for (String moduleName : mUnit.getDistModel().getModuleMap().keySet())
            mModuleModel.getModuleMap().get(moduleName).setPartitionId(
                    mUnit.getDistModel().getModuleMap().get(moduleName).getPartitionId());
        
        localReport = CostAnalyzerHelper.analyzeCosts(mUnit.getName(), mModuleModel,mUnit.getDistModel().getHostModel());
        
         for (ISimulatorListener l : listeners){
            l.valueChanged(localReport);    
            l.unitSimulationOver(localReport);
         }
    }
}
